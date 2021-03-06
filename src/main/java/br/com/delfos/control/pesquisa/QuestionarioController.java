package br.com.delfos.control.pesquisa;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Controller;

import br.com.delfos.app.QuestionarioApp;
import br.com.delfos.control.generic.AbstractController;
import br.com.delfos.dao.pesquisa.QuestionarioDAO;
import br.com.delfos.except.view.FXValidatorException;
import br.com.delfos.model.pesquisa.Questionario;
import br.com.delfos.util.LeitorDeFXML;
import br.com.delfos.view.AlertAdapter;
import br.com.delfos.view.manipulador.ScreenUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * @author Jéssica Giori
 *
 */
@Controller
public class QuestionarioController extends AbstractController<Questionario, QuestionarioDAO> {

	@FXML
	@NotNull
	private DatePicker dtVencimento;

	@FXML
	private Label lblStatus;

	@FXML
	private Button btnSalvar;

	@FXML
	@NotNull
	private DatePicker dtInicio;

	@FXML
	@NotNull
	private CheckBox cbAutenticavel;

	@FXML
	@NotNull
	private TextField txtNome;

	@FXML
	private TextField txtCod;

	@FXML
	private TextArea txtDesc;

	@FXML
	private Button btnNovo;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TabPane tbPerguntas;

	@FXML
	private Label lblDuracao;

	private PerguntaController perguntaController;

	// método que libera o campo data de vencimento e muda a cor dele
	private Callback<DatePicker, DateCell> factoryDeVencimento = param -> new DateCell() {
		@Override
		public void updateItem(LocalDate item, boolean empty) {
			super.updateItem(item, empty);

			if (item.isBefore(QuestionarioController.this.dtInicio.getValue().plusDays(1))) {
				this.setDisable(true);
				this.setStyle("-fx-background-color: #ffc0cb;");
			}

			QuestionarioController.this.vencimentoPesquisa.ifPresent(vencimento -> {
				if (item.isAfter(vencimento)) {
					this.setDisable(true);
					this.setStyle("-fx-background-color: #ffc0cb;");
				}
			});

			long p = QuestionarioController.this.getTotalDeDias(item);
			this.setTooltip(new Tooltip(String.format("Seu questionário durará %s dia(s).", p)));
		};
	};

	private Optional<LocalDate> vencimentoPesquisa = Optional.empty();

	// ações dos botões
	@FXML
	private void handleButtonNovo(ActionEvent event) {
		this.clear();
	}

	@FXML
	private void handleButtonSalvar(ActionEvent event) {
		try {
			this.salvar(toValue(), this).ifPresent(x -> {
				this.posiciona(Optional.ofNullable(x));
				AlertAdapter.information("Todas as alterações foram salvas.",
						"As informações modificadas foram atualizadas automaticamente.");
				QuestionarioApp.close();
			});
		} catch (FXValidatorException e) {
			AlertAdapter.requiredDataNotInformed(e);
		}
	}

	// monta o objeto do questionario que está na tela para ser salvo
	@Override
	protected Questionario toValue() {
		Questionario q = new Questionario();
		q.setId(this.txtCod.getText().isEmpty() ? null : Long.parseLong(this.txtCod.getText()));

		q.setNome(this.txtNome.getText());
		q.setDescricao(this.txtDesc.getText());
		q.setDataInicio(this.dtInicio.getValue());
		q.setVencimento(this.dtVencimento.getValue());
		q.setAutenticavel(this.cbAutenticavel.isSelected());

		q.addPerguntas(Optional.ofNullable(new HashSet<>(perguntaController.getPerguntas())));

		return q;
	}

	// pesquisa por codigo
	@FXML
	private void pesquisa() {
		pesquisaPorCodigo();
	}

	public void init(Optional<Questionario> questionario, Optional<LocalDate> vencimentoPesquisa) {
		this.posiciona(questionario);
		this.setVencimentoPesquisa(vencimentoPesquisa);
	}

	private void setVencimentoPesquisa(Optional<LocalDate> vencimentoPesquisa) {
		this.vencimentoPesquisa = vencimentoPesquisa;
	}

	public void clear() {
		ScreenUtils.limpaCampos(this.rootPane);
		this.dtInicio.setValue(LocalDate.now());
		this.perguntaController.clear();
		this.cbAutenticavel.setSelected(true);
	}

	protected void posiciona(Optional<Questionario> value) {
		value.ifPresent(quest -> {
			this.txtCod.setText(quest.getId() == null ? "" : String.valueOf(quest.getId()));
			this.txtNome.setText(quest.getNome());
			this.txtDesc.setText(quest.getDescricao());
			this.dtInicio.setValue(quest.getDataInicio() == null ? LocalDate.now() : quest.getDataInicio());
			this.dtVencimento.setValue(quest.getVencimento());
			this.cbAutenticavel.setSelected(quest.isAutenticavel());

			this.perguntaController.setPerguntas(quest.getPerguntas());

		});

	}

	@FXML
	private void printa(ActionEvent event) {
		this.setDias(this.getTotalDeDias(this.dtVencimento.getValue()));
	}

	// calculo de duração do questionario
	private void setDias(Long a) {
		this.lblDuracao.setVisible(a == 0 ? false : true);
		this.lblDuracao.setText(a != 0 ? String.format("Duração: %d dia(s)", a) : "");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.dtInicio.setEditable(false);
		this.dtInicio.disarm();
		this.dtInicio.setValue(LocalDate.now());
		this.dtVencimento.setDayCellFactory(this.factoryDeVencimento);
		this.btnNovo.setText("Limpar");

		// ABRE TELA DE PERGUNTA DENTRO DA ABA CORRETA
		this.configTabPergunta();

		this.clear();

	}

	private void configTabPergunta() {
		try {

			FXMLLoader loader = LeitorDeFXML.getLoader("fxml/pergunta/PerguntaView.fxml");

			BorderPane load = (BorderPane) loader.load();
			this.perguntaController = loader.getController();

			Tab tab = new Tab("Perguntas");
			tab.setContent(load);
			this.tbPerguntas.getTabs().add(tab);
			this.tbPerguntas.getSelectionModel().select(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private long getTotalDeDias(LocalDate item) {
		return ChronoUnit.DAYS.between(QuestionarioController.this.dtInicio.getValue(), item);
	}

	public Questionario getTarget() {
		return toValue();
	}

}
