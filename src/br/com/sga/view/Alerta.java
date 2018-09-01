package br.com.sga.view;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alerta extends Alert {

	private static Alerta alert;
	
	private Alerta(AlertType alertType, String titulo, String header, String content){
		super(alertType);
		setTitle(titulo);
		setHeaderText(header);
		setContentText(content);
		
		((Stage) this.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Icon.png")));
		
		initModality(Modality.APPLICATION_MODAL);
	}

	public static Alerta getInstance()
	{
		if(alert == null)
			alert = new Alerta(AlertType.INFORMATION, "Erro!!!", "Erro!!!", "Erro!!!");
		return alert;
	}
	
	public void showMensagem(AlertType alertType, String titulo, String header, String content)
	{
		setAlertType(alertType);
		setTitle(titulo);
		setHeaderText(header);
		setContentText(content);
		this.show();
		
	}
	
}
