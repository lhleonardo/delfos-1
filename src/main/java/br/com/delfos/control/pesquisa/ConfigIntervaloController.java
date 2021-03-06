package br.com.delfos.control.pesquisa;

import java.net.URL;
import java.util.ResourceBundle;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Controller;

import br.com.delfos.control.dialog.EditDialog;
import br.com.delfos.model.pesquisa.pergunta.Intervalo;
import br.com.delfos.model.pesquisa.pergunta.Pergunta;
import br.com.delfos.util.view.FXValidator;
import br.com.delfos.util.view.MaskFieldUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@Controller
public class ConfigIntervaloController implements EditDialog<Pergunta<Intervalo>>, Initializable {

	@FXML
	private Button btnSalvar;

	@FXML
	@NotNull
	private TextField txtIntervalo;

	@FXML
	@NotNull
	private TextField txtValorFinal;

	@FXML
	private AnchorPane rootPane;

	@FXML
	@NotNull
	private TextField txtValorInicial;

	@FXML
	@NotNull
	private TextField txtNome;

	@FXML
	private TextArea txtDescricao;

	@FXML
	private Text lblTipo;

	private boolean okClicked = false;

	private Stage dialogStage;

	private Pergunta<Intervalo> value;

	@FXML
	private void handleButtonSalvar(ActionEvent event) {
		if (FXValidator.validate(this)) {
			this.value.setNome(txtNome.getText());
			this.value.setDescricao(txtDescricao.getText());
			this.value.getAlternativa().setIncremento(Integer.parseInt(txtIntervalo.getText()));
			this.value.getAlternativa().setValorInicial(Integer.parseInt(txtValorInicial.getText()));
			this.value.getAlternativa().setValorFinal(Integer.parseInt(txtValorFinal.getText()));

			this.okClicked = true;
			this.dialogStage.close();
		}
	}

	@Override
	public boolean isOkCliked() {
		return this.okClicked;
	}

	@Override
	public void setDialogStage(Stage stage) {
		this.dialogStage = stage;
	}

	@Override
	public void setValue(Pergunta<Intervalo> value) {
		this.value = value;

		// define as informações da pergunta
		this.txtNome.setText(value.getNome());
		this.txtDescricao.setText(value.getDescricao());
		this.lblTipo.setText("Intervalo");

		// define as informações do intervalo.
		this.txtIntervalo.setText(String.valueOf(value.getAlternativa().getIncremento()));
		this.txtValorInicial.setText(String.valueOf(value.getAlternativa().getValorInicial()));
		this.txtValorFinal.setText(String.valueOf(value.getAlternativa().getValorFinal()));

	}

	@Override
	public Pergunta<Intervalo> getValue() {
		return this.value;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MaskFieldUtil.numericFields(txtIntervalo, txtValorInicial, txtValorFinal);
	}

}
