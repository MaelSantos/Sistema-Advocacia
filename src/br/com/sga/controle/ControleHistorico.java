package br.com.sga.controle;

import java.time.ZoneId;
import java.util.Date;

import br.com.sga.entidade.Funcionario;
import br.com.sga.entidade.Log;
import br.com.sga.entidade.enums.EventoLog;
import br.com.sga.entidade.enums.StatusLog;
import br.com.sga.entidade.enums.Tela;
import br.com.sga.exceptions.BusinessException;
import br.com.sga.fachada.Fachada;
import br.com.sga.fachada.IFachada;
import br.com.sga.view.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControleHistorico extends Controle {

	@FXML
	private Label lblData;

	@FXML
	private Button btnBuscar;

	@FXML
	private DatePicker tfdDe;

	@FXML
	private DatePicker tfdAte;

	@FXML
	private TableView<Log> tblLogs;

	@FXML
	private TableColumn<Log, Date> colData;

	@FXML
	private TableColumn<Log, String> colRemetente;

	@FXML
	private TableColumn<Log, EventoLog> colEvento;

	@FXML
	private TableColumn<Log, String> colDestinatario;

	@FXML
	private TableColumn<Log, StatusLog> colStatus;

	private IFachada fachada;
	private Funcionario funcionario;
	
	@Override
	public void atualizar(Tela tela, Object object) {
		
		if (object instanceof Log) {
			Log log = (Log) object;
			
			tblLogs.getItems().add(log);
			
		}
		
		if (object instanceof Funcionario) {
			Funcionario funcionario = (Funcionario) object;
			
			this.funcionario = funcionario;
			
		}

	}

	@Override
	public void init() {
		
		fachada = Fachada.getInstance();

		colData.setCellValueFactory(
				new PropertyValueFactory<>("data"));
		colDestinatario.setCellValueFactory(
				new PropertyValueFactory<>("destinatario"));
		colEvento.setCellValueFactory(
				new PropertyValueFactory<>("evento"));
		colRemetente.setCellValueFactory(
				new PropertyValueFactory<>("remetente"));
		colStatus.setCellValueFactory(
				new PropertyValueFactory<>("status"));
		
	}

	@Override
	public void actionButton(ActionEvent event) {
		
		Object obj = event.getSource();
		
		if(obj == btnBuscar)
		{
			try {
				tblLogs.getItems().setAll(fachada.buscarLogPorData(
						Date.from(tfdDe.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
						Date.from(tfdAte.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())));
				if(! (tblLogs.getItems().isEmpty()))
					lblData.setText("De: "+tfdDe.getEditor().getText().trim()+" - At�: "+tfdAte.getEditor().getText().trim());
				else
					lblData.setText("De: "+tfdDe.getEditor().getText().trim()+" - At�: "+tfdAte.getEditor().getText().trim()+" SEM RESULTADOS!!!");
				
				Alerta.getInstance().showMensagem("Cocluido", "Busca Concluida Com Sucesso","");
			} catch (BusinessException e) {
				e.printStackTrace();
				Alerta.getInstance().showMensagem("Erro!", "Erro Ao Buscar Historico!!!", e.getMessage());
				try {
					fachada.salvarEditarLog(new Log(new Date(System.currentTimeMillis()), EventoLog.BUSCAR, funcionario.getNome(), "HISTORICO", StatusLog.ERROR));
				} catch (BusinessException e1) {
					Alerta.getInstance().showMensagem("Erro!", "Erro Ao Buscar Historico!!!", e.getMessage());
					e1.printStackTrace();
				}
			}			
		}


	}
	
}
