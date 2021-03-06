package br.com.delfos.control.pessoal.resposta;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import br.com.delfos.model.pesquisa.Questionario;
import br.com.delfos.model.pesquisa.pergunta.MultiplaEscolha;
import br.com.delfos.model.pesquisa.pergunta.Pergunta;
import br.com.delfos.model.pesquisa.resposta.Resposta;
import br.com.delfos.model.pesquisa.resposta.RespostaMultiplaEscolha;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RespostaMultiplaEscolhaController
		implements RespostaControllerImpl<MultiplaEscolha, String>, Initializable {

	@FXML
	private VBox boxItems;

	@FXML
	private AnchorPane rootElement;

	@FXML
	private Text txtNome;

	@FXML
	private Text txtDescricao;

	@FXML
	private CheckBox cbNaoResponder;

	private ToggleGroup groupItems;

	private Optional<Pergunta<MultiplaEscolha>> option;

	@Override
	public void setOption(Optional<Pergunta<MultiplaEscolha>> optionalAlternativa) {
		optionalAlternativa.ifPresent(pergunta -> {
			txtNome.setText(pergunta.getNome());
			txtDescricao.setText(pergunta.getDescricao());

			MultiplaEscolha alternativa = pergunta.getAlternativa();
			this.configGroupItems(alternativa);
		});

		this.option = optionalAlternativa;
	}

	private void configGroupItems(MultiplaEscolha alternativa) {
		initGroupItem();
		alternativa.getEscolhas().forEach(item -> {
			RadioButton radio = new RadioButton(item);

			groupItems.getToggles().add(radio);

			VBox.setMargin(radio, new Insets(0, 5, 5, 5));

			boxItems.getChildren().add(radio);
		});
	}

	private void initGroupItem() {
		if (this.groupItems == null) {
			groupItems = new ToggleGroup();
		} else {
			groupItems.getToggles().clear();
		}
	}

	@Override
	public Optional<Pergunta<MultiplaEscolha>> getOption() {
		return option;
	}

	@Override
	public String getSelected() {
		RadioButton radio = (RadioButton) this.groupItems.getSelectedToggle();
		return radio.getText();
	}

	@Override
	public void clearSelected() {
		this.groupItems.selectToggle(null);
	}

	@Override
	public boolean isSelected() {
		return this.groupItems.getSelectedToggle() != null;
	}

	@Override
	public Resposta<?> getResposta(Questionario questionario) {
		if (this.getOption().isPresent()) {
			RespostaMultiplaEscolha resposta = new RespostaMultiplaEscolha();
			resposta.setQuestionario(questionario);
			resposta.setPergunta(this.getOption().get());
			if (!cbNaoResponder.isSelected())
				resposta.setEscolha(getSelected());
			return resposta;
		} else {
			throw new RuntimeException("Não foi informado uma pergunta válida ou pertencente ao questionario.");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.cbNaoResponder.selectedProperty().addListener((obs, oldValue, newValue) -> {
			this.groupItems.getToggles().forEach(toggle -> {
				((RadioButton) toggle).setDisable(newValue);
			});
		});
	}
	
	@Override
	public boolean isIgnored() {
		return cbNaoResponder.isSelected();
		
	}

}
