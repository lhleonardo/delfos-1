package br.com.delfos.control;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.delfos.dao.pesquisa.QuestionarioDAO;
import br.com.delfos.model.pesquisa.Questionario;
import br.com.delfos.view.AlertBuilder;
import br.com.delfos.view.manipulador.ManipuladorDeComponentes;
import br.com.delfos.view.manipulador.ManipuladorDeTelas;
import br.com.delfos.view.table.QuestionarioPerguntas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

/**
 * @author 00685193209
 *
 */
@Controller
public class QuestionarioController implements Initializable {

	@Autowired
	private QuestionarioDAO daoQuestionario;

	@FXML
	private DatePicker dtVencimento;

	@FXML
	private Label lblStatus;

	@FXML
	private Button btnSalvar;

	@FXML
	private DatePicker dtInicio;

	@FXML
	private CheckBox cbAutenticavel;

	@FXML
	private Button btnPesquisa;

	@FXML
	private TableView<QuestionarioPerguntas> tbPerguntas;

	@FXML
	private AnchorPane anchorPaneEndereco;

	@FXML
	private Tab tbEndereco;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtCod;

	@FXML
	private TextArea txtDesc;

	@FXML
	private Button btnExcluir;

	@FXML
	private Button btnNovo;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private Label lblDuracao;

	@FXML
	private TableColumn<QuestionarioPerguntas, String> nome;

	@FXML
	private TableColumn<QuestionarioPerguntas, Object> tipoPergunta;

	@FXML
	private ObservableList<QuestionarioPerguntas> dadosTabela = FXCollections
			.observableArrayList(new QuestionarioPerguntas("Qual � o seu nome?", new Object()));

	private Callback<DatePicker, DateCell> factoryDeVencimento = param -> new DateCell() {
		@Override
		public void updateItem(LocalDate item, boolean empty) {
			super.updateItem(item, empty);

			if (item.isBefore(QuestionarioController.this.dtInicio.getValue().plusDays(1))) {
				this.setDisable(true);
				this.setStyle("-fx-background-color: #ffc0cb;");
			}

			long p = QuestionarioController.this.getTotalDeDias(item);
			this.setTooltip(new Tooltip(String.format("Seu question�rio durar� %d dia(s).", p)));
		};
	};

	@FXML
	private void handleButtonNovo(ActionEvent event) {
		ManipuladorDeTelas.limpaCampos(this.rootPane);
		this.lblDuracao.setVisible(false);
		this.dtInicio.setValue(LocalDate.now());
	}

	@FXML
	private void handleButtonExcluir(ActionEvent event) {
		this.dtInicio.setValue(LocalDate.now());
	}

	@FXML
	private void handleButtonSalvar(ActionEvent event) {
		if (ManipuladorDeComponentes.validaCampos(this.rootPane)) {
			Optional<Questionario> save = this.daoQuestionario.save(this.montaRegistro());

			save.ifPresent(questionario -> {
				this.txtCod.setText(String.valueOf(questionario.getId()));
				AlertBuilder.information("Salvo com sucesso");
			});
		}

	}

	private Questionario montaRegistro() {
		Questionario q = new Questionario();
		q.setId(this.txtCod.getText().isEmpty() ? null : Long.parseLong(this.txtCod.getText()));

		q.setNome(this.txtNome.getText());
		q.setDescricao(this.txtDesc.getText());
		q.setDataInicio(this.dtInicio.getValue());
		q.setDataFinalizacao(this.dtVencimento.getValue());
		q.setActive(QuestionarioController.this.cbAutenticavel.isSelected());
		// this.lblStatus.setText((q.isActive() ? "Ativo" : "Inativo"));
		// this.lblStatus.setStyle("-fx-text-fill: " + (q.isActive() ? "#33ff77"
		// : "#ff5c33"));
		return q;
	}

	@FXML
	private void pesquisa() {
		this.dtInicio.setEditable(false);
		this.dtInicio.disarm();
	}

	@FXML
	void printa(ActionEvent event) {
		this.setDias(this.getTotalDeDias(this.dtVencimento.getValue()));
	}

	private void setDias(Long a) {
		if (a == 0) {
			this.lblDuracao.setVisible(false);
		} else {
			this.lblDuracao.setText(String.format("Dura��o: %d dia(s)", a));
			this.lblDuracao.setVisible(true);

		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.dtInicio.setEditable(false);
		this.dtInicio.disarm();
		this.dtInicio.setValue(LocalDate.now());

		this.dtVencimento.setDayCellFactory(this.factoryDeVencimento);

		this.nome.setCellValueFactory(new PropertyValueFactory<QuestionarioPerguntas, String>("nome"));
		this.tipoPergunta.setCellFactory(ComboBoxTableCell.forTableColumn("A", "B", "C"));
		this.tbPerguntas.setItems(this.dadosTabela);

	}

	private long getTotalDeDias(LocalDate item) {
		return ChronoUnit.DAYS.between(QuestionarioController.this.dtInicio.getValue(), item);
	}

}
