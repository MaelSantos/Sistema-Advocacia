package br.com.sga.controle;

import java.net.BindException;
import java.net.URL;
import java.util.ResourceBundle;

import br.com.sga.app.App;
import br.com.sga.dao.DaoUsuario;
import br.com.sga.entidade.Funcionario;
import br.com.sga.entidade.Tela;
import br.com.sga.exceptions.BusinessException;
import br.com.sga.exceptions.DaoException;
import br.com.sga.fachada.Fachada;
import br.com.sga.view.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class ControleLogin implements Initializable{
	
	@FXML
    private TextField tfdLogin;

    @FXML
    private Button btnEntrar;
    
    @FXML
    private Button btnSair;

    @FXML
    private PasswordField tfdSenha;
	
    private Fachada fachada = Fachada.getInstance();
   
	@FXML
	private void actionButton(ActionEvent e)
	{		
		if(e.getSource() == btnEntrar)
		{
			try {
				Funcionario funcionario = fachada.buscarPorLogin(tfdLogin.getText(), tfdSenha.getText());
				
				System.out.println(funcionario);
				App.changeStage(Tela.menu);
				App.notificarOuvintes(Tela.menu,funcionario);
				System.out.println("Logado");
			}catch (BusinessException e1) {
				Alerta.getInstance().showMensagem("Dados Incorretos", "Login/Email Ou Senha Incorretos",e1.getMessage());
				e1.printStackTrace();
			}
			/*if(DaoUsuario.getInstance().entrarSistema(tfdLogin.getText(), tfdSenha.getText()))
			{
				App.changeStage(Tela.menu);
				App.notificarOuvintes(Tela.menu, DaoUsuario.getInstance().getUsuarioLogado());
			}
			else
			{
				Alerta.getInstance().showMensagem("Dados Incorretos", "Login/Email Ou Senha Incorretos", "Verifique Os Dados Informados E Tente Novamente");
			}*/ 
		}
		if(e.getSource() == btnSair)
		{
			System.exit(0);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Stub de m�todo gerado automaticamente
		
	}

}
