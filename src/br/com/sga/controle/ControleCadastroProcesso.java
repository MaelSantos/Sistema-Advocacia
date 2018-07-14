package br.com.sga.controle;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import br.com.sga.app.App;
import br.com.sga.entidade.Funcionario;
import br.com.sga.entidade.Processo;
import br.com.sga.entidade.enums.Tela;
import br.com.sga.entidade.enums.TipoParticipacao;
import br.com.sga.entidade.enums.TipoProcesso;
import br.com.sga.exceptions.BusinessException;
import br.com.sga.fachada.Fachada;
import br.com.sga.fachada.IFachada;
import br.com.sga.view.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ControleCadastroProcesso extends Controle{
	
	@FXML
    private TextField tfdContrato;
	
	@FXML
	private ComboBox<TipoProcesso> cbxTipoProcesso;

	@FXML
	private ComboBox<TipoParticipacao> cbxParticipacao;

	@FXML
	private TextField tfdNumero;

	@FXML
	private TextField tfdComarca;

	@FXML
	private DatePicker tfdData;

	@FXML
	private TextField tfdDescricao;

	@FXML
	private TextField tfdFase;

	@FXML
	private TextField tfdClasse;

	@FXML
	private TextField tfdOrgao;

	@FXML
	private Button btnVoltar;

	@FXML
	private Button btnCadastrar;
	
	private IFachada fachada;
	private Processo processo;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		
		fachada = Fachada.getInstance();
		
		cbxParticipacao.getItems().addAll(TipoParticipacao.values());
		cbxTipoProcesso.getItems().addAll(TipoProcesso.values());
		
	}

	@Override
	public void actionButton(ActionEvent event) {
		
		Object obj = event.getSource();
		
		if(obj == btnCadastrar)
		{
			try {
				processo = criarProcesso();
				fachada.salvarEditarProcesso(processo);
			} catch (BusinessException e) {
				e.printStackTrace();
				Alerta.getInstance().showMensagem("Erro Ao Salvar", "Erro ao Salvar Processo", e.getMessage());
			}
		}
		if(obj == btnVoltar)
			App.notificarOuvintes(Tela.processos);

	}

	@Override
	public void atualizar(Tela tela, Object object) {
		
//		try {
//			TextFields.bindAutoCompletion(tfdContrato, fachada.buscarContratoPorBusca(""));
//		} catch (BusinessException e) {
//			e.printStackTrace();
//		}

	}

	@SuppressWarnings("finally")
	private Processo criarProcesso()
	{
		processo = new Processo();
		try {
			processo.setClasse_judicial(tfdClasse.getText().trim());
			processo.setComarca(tfdComarca.getText().trim());
			
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date data = df.parse(tfdData.getEditor().getText());
			processo.setData_atuacao(data);
			
			processo.setDescricao(tfdDescricao.getText().trim());
			processo.setFase(tfdFase.getText().trim());
			processo.setNumero(tfdNumero.getText().trim());
			processo.setOrgao_julgador(tfdOrgao.getText().trim());
			processo.setTipo_participacao(cbxParticipacao.getValue());
			processo.setTipo_processo(cbxTipoProcesso.getValue());
			
			processo.setContrato(fachada.buscarContratoPorCodigo(tfdContrato.getText().trim()));

		} catch (Exception e) {

		}
		finally {			
			return processo;
		}
		
	}

}
