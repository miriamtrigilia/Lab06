package it.polito.tdp.meteo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Integer> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;
	
	private Model model;
	private List<Citta> citta;
	
	public void setModel(Model model) {
		this.model = model;
		this.boxMese.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12);
	}

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		this.citta = this.model.calcolaSequenza(this.boxMese.getValue());
		this.txtResult.appendText("\nSequenza:\n");
		for(Citta c : this.citta) {
			this.txtResult.appendText(c.getNome()+"\n");
		}

	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		this.txtResult.clear();
		this.txtResult.setText(this.model.getUmiditaMedia(this.boxMese.getValue()));
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
	}

}
