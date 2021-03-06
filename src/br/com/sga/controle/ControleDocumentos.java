package br.com.sga.controle;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.sga.dao.DaoCommun;
import br.com.sga.entidade.Consulta;
import br.com.sga.entidade.Contrato;
import br.com.sga.entidade.Funcionario;
import br.com.sga.entidade.Log;
import br.com.sga.entidade.adapter.ConsultaAdapter;
import br.com.sga.entidade.adapter.ContratoAdapter;
import br.com.sga.entidade.adapter.DocumentoContratoAdapter;
import br.com.sga.entidade.enums.EventoLog;
import br.com.sga.entidade.enums.Instancia;
import br.com.sga.entidade.enums.StatusLog;
import br.com.sga.entidade.enums.Tela;
import br.com.sga.entidade.enums.TipoDocumento;
import br.com.sga.entidade.enums.TipoPagamento;
import br.com.sga.exceptions.BusinessException;
import br.com.sga.exceptions.DaoException;
import br.com.sga.exceptions.ValidacaoException;
import br.com.sga.fachada.Fachada;
import br.com.sga.fachada.IFachada;
import br.com.sga.interfaces.IDaoCommun;
import br.com.sga.view.Alerta;
import br.com.sga.view.Dialogo;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ControleDocumentos extends Controle {

	@FXML
	private ComboBox<TipoDocumento> cbxTipo;

	@FXML
	private Button btnBuscar;

	@FXML
	private Label lblDados;

	@FXML
	private Button btnGerar;

	@FXML
	private FlowPane flowFinanceiro;

	@FXML
	private DatePicker tfdDe;

	@FXML
	private DatePicker tfdAte;

	@FXML
	private VBox flowBusca;

	@FXML
	private TextField tfdBusca;

	@FXML
	private Label lblContrato;

	@FXML
	private GridPane panelContrato;

	@FXML
	private TextField tfdComarca;

	@FXML
	private TextField tfdNumero;

	@FXML
	private ComboBox<Instancia> cbxInstancia;

	@FXML
	private ProgressIndicator pgiDados;

	private String arquivo;
	private IFachada fachada;
	private IDaoCommun daoCommun;
	private Dialogo dialogo;
	private List<? extends Object> list;

	private double porcentagem = 0;
	private Service<?> service;

	private Funcionario funcionario;
	private Consulta consulta;

	@Override
	public void atualizar(Tela tela, Object object) {

		if (object instanceof Funcionario) {
			funcionario = (Funcionario) object;
		}

		if (tela == Tela.CADASTRO_CONSULTA) {
			if (object instanceof Consulta) {
				Consulta consulta = (Consulta) object;

				List<Consulta> list = new ArrayList<Consulta>();
				list.add(consulta);

				Log log;
				try {
					gerarDocumento(list, "Ficha.jrxml");
					log = new Log(new Date(System.currentTimeMillis()), EventoLog.GERAR, funcionario.getNome(),
							"Gerar Documento: ", StatusLog.CONCLUIDO);
				} catch (FileNotFoundException | JRException e) {
					Alerta.getInstance().showMensagem(AlertType.ERROR, "Erro!", "Erro ao gerar ficha!!!",
							e.getMessage());
					log = new Log(new Date(System.currentTimeMillis()), EventoLog.GERAR, funcionario.getNome(),
							"Gerar Documento: ", StatusLog.ERRO);
					e.printStackTrace();
				}

				try {
					if (log != null)
						fachada.salvarEditarLog(log);
				} catch (BusinessException e) {

					e.printStackTrace();
				}

			}

		} 
//		else if (tela == Tela.DOCUMENTOS)
//		{
//			if (object instanceof Contrato) {
//				Contrato contrato = (Contrato) object;
//
//				list = new ArrayList<Contrato>();
//				
//				
//			}
//
//		}

	}

	@Override
	public void init() {

		fachada = Fachada.getInstance();
		daoCommun = DaoCommun.getInstance();
		dialogo = Dialogo.getInstance();

		cbxTipo.getItems().setAll(TipoDocumento.values());
		cbxInstancia.getItems().setAll(Instancia.values());

		cbxTipo.setButtonCell(new ListCell<TipoDocumento>() {
			@Override
			protected void updateItem(TipoDocumento item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText("Tipo de Documento");
				} else {
					setText(item.toString());
				}
			}
		});
		cbxInstancia.setButtonCell(new ListCell<Instancia>() {
			@Override
			protected void updateItem(Instancia item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText("Instância");
				} else {
					setText(item.toString());
				}
			}
		});

		service = new Service<Object>() {

			@Override
			protected Task<Object> createTask() {

				return new Task<Object>() {

					public void update() {
						updateMessage("Gerando Arquivo...");
						porcentagem += 16.666666667;
						updateProgress(porcentagem, 100);
					}

					@SuppressWarnings("deprecation")
					@Override
					protected Object call() throws Exception {
						updateTitle("Preparando Arquivo...");

						// gerando o jasper design
						InputStream inputStream = getClass().getClassLoader().getResourceAsStream(arquivo);
						update();
						System.out.println("Carregou arquivo");
						JasperDesign desenho = JRXmlLoader.load(inputStream);
						update();
						System.out.println("Carregou designer");
						// compila o relat�rio
						JasperReport relatorio = JasperCompileManager.compileReport(desenho);
						update();
						System.out.println("Compilou");
						/* Convert List to JRBeanCollectionDataSource */
						JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(list);
						update();
						System.out.println("Criou biblioteca para documento - bean");
						/* Map to hold Jasper report Parameters */
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("ItemDataSource", itemsJRBean);
						update();
						System.out.println("Criou map");
						/* Using compiled version(.jasper) of Jasper report to generate PDF */
						JasperPrint jasperPrint = JasperFillManager.fillReport(relatorio, parameters, itemsJRBean);
						update();
						System.out.println("Criou jasperPrint");
						JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
						jasperViewer.setZoomRatio(0.75F);
						jasperViewer.setLocationRelativeTo(null);
						jasperViewer.show();
						// JasperViewer.viewReport(jasperPrint);

						return null;
					}

					@Override
					protected void succeeded() {
						super.succeeded();
						porcentagem = 0;
						pgiDados.setVisible(false);
					}

					@Override
					protected void scheduled() {
						super.scheduled();
						pgiDados.setVisible(true);
					}

				};
			}
		};

		pgiDados.progressProperty().bind(service.progressProperty());
	}

	@Override
	public void actionButton(ActionEvent event) {

		Object obj = event.getSource();

		if (obj == btnGerar) {

			Log log = null;
			try {
				if (list != null && arquivo != null && !(list.isEmpty())) {
					service.restart();

					log = new Log(new Date(System.currentTimeMillis()), EventoLog.GERAR, funcionario.getNome(),
							"Gerar Documento: " + cbxTipo.getValue(), StatusLog.CONCLUIDO);

				} else {
					Alerta.getInstance().showMensagem(AlertType.ERROR, "Erro!", "Erro Ao Gerar Documento!!!",
							"Verifique Se Todos Os Dados Est�o Corretos");

					log = new Log(new Date(System.currentTimeMillis()), EventoLog.GERAR, funcionario.getNome(),
							"Gerar Documento: Sem Resultados", StatusLog.SEM_RESULTADOS);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log = new Log(new Date(System.currentTimeMillis()), EventoLog.GERAR, funcionario.getNome(),
						"Gerar Documento: " + cbxTipo.getValue(), StatusLog.ERRO);
				Alerta.getInstance().showMensagem(AlertType.ERROR, "Erro!", "Erro Ao Gerar Documento!!!",
						"Verifique Se Todos Os Dados Est�o Corretos");
			}

			try {
				if (log != null)
					fachada.salvarEditarLog(log);
			} catch (BusinessException e) {

				e.printStackTrace();
			}

		}
		if (obj == btnBuscar) {
			Log log = null;
			try {

				list = carregarLista(cbxTipo.getValue());

				if (!list.isEmpty()) {
					Alerta.getInstance().showMensagem(AlertType.INFORMATION, "Concluido!",
							"Dados Carregados Com Sucesso!!!", "Pronto Para Gerar Arquivo");

					lblDados.setText("Dados Carregados");
					lblDados.setTextFill(Paint.valueOf("#00FF00"));

					log = new Log(new Date(System.currentTimeMillis()), EventoLog.BUSCAR, funcionario.getNome(),
							"Buscar: " + cbxTipo.getValue(), StatusLog.CONCLUIDO);

				} else {
					Alerta.getInstance().showMensagem(AlertType.ERROR, "N�o Encontrado!", "Dados N�o Encontrados!!!",
							"Tente Novamente Procurando Por Outros Dados!!!");

					lblDados.setText("Dados N�o Encontrados");
					lblDados.setTextFill(Paint.valueOf("#FF0000"));

					log = new Log(new Date(System.currentTimeMillis()), EventoLog.BUSCAR, funcionario.getNome(),
							"Buscar: " + cbxTipo.getValue() + " - Sem Resultados", StatusLog.SEM_RESULTADOS);
				}
			} catch (ValidacaoException e) {
				e.printStackTrace();
				Alerta.getInstance().showMensagem(AlertType.ERROR, "Erro!", "Erro ao Carregar Arquivos!!!",
						e.getMessage());
				log = new Log(new Date(System.currentTimeMillis()), EventoLog.BUSCAR, funcionario.getNome(),
						"Buscar: " + cbxTipo.getValue() + " - Erro", StatusLog.ERRO);
			}

			try {
				if (log != null)
					fachada.salvarEditarLog(log);
			} catch (BusinessException e) {
				e.printStackTrace();
			}

		}
		if (obj == cbxTipo) {
			switch (cbxTipo.getValue()) {
			case DESPESA:
				arquivo = "Despesas.jrxml";
				flowFinanceiro.setVisible(true);
				flowBusca.setVisible(false);

				break;
			case RECEITA:
				arquivo = "Receitas.jrxml";
				flowFinanceiro.setVisible(true);
				flowBusca.setVisible(false);
				break;

			case FICHA:
				arquivo = "Ficha.jrxml";
				flowBusca.setVisible(true);
				flowFinanceiro.setVisible(false);
				panelContrato.setVisible(false);
				lblContrato.setVisible(false);
				break;

			case CONTRATO:
				arquivo = "ContratoBanco.jrxml";
				flowFinanceiro.setVisible(false);
				flowBusca.setVisible(true);
				panelContrato.setVisible(true);
				lblContrato.setVisible(true);
				break;

			default:
				break;
			}
		}

	}

	public List<? extends Object> carregarLista(TipoDocumento documento) throws ValidacaoException {
		try {
			switch (documento) {
			case DESPESA:
				return daoCommun.getDespesaPorIntervalo(
						Date.from(tfdDe.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
						Date.from(tfdAte.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			case RECEITA:
				return daoCommun.getReceitaPorIntervalo(
						Date.from(tfdDe.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
						Date.from(tfdAte.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			case FICHA:
				if (!tfdBusca.getText().isEmpty()) {
					List<Consulta> list = new ArrayList<Consulta>();
					ConsultaAdapter con = dialogo.selecionar(
							fachada.buscarConsultaPorClienteAdapter(new String[] { tfdBusca.getText().trim() }));
					consulta = fachada.buscarConsultaPorId(con.getId());
					consulta.setCliente(fachada.buscarClientePorIdConsulta(consulta.getId()));
					
					System.out.println("Cliente: "+consulta.getCliente());
					System.out.println("Endere�o: "+consulta.getCliente().getEndereco());
					
					consulta.setFuncionario(fachada.buscarUsuarioPorIdConsulta(consulta.getId()));
					list.add(consulta);
					return list;
				} else
					throw new ValidacaoException("INFORME SUA BUSCA");
			case CONTRATO:
				if (!tfdBusca.getText().isEmpty()) {
					List<DocumentoContratoAdapter> list = new ArrayList<>();
					ContratoAdapter adapter = dialogo
							.selecionar(fachada.buscarContratoPorClienteAdapter(tfdBusca.getText().trim()));

					if (adapter != null && adapter.getId() != null) {
						lblContrato.setText(adapter + "");

						if (!tfdComarca.getText().trim().isEmpty() && !tfdNumero.getText().trim().isEmpty()
								&& cbxInstancia.getSelectionModel().getSelectedItem() != null) {
							DocumentoContratoAdapter contratoAdapter = new DocumentoContratoAdapter();
							Contrato contrato = fachada.buscarContratoPorId(adapter.getId());
							
							Consulta consulta = fachada.buscarConsultaPorId(contrato.getConsulta().getId());
							consulta.setCliente(fachada.buscarClientePorId(consulta.getCliente().getId()));
							consulta.setFuncionario(fachada.buscarUsuarioPorId(consulta.getFuncionario().getId()));
							contrato.setConsulta(consulta);
							
							contratoAdapter.setContrato(contrato);
							contratoAdapter.setComarca(tfdComarca.getText().trim());
							contratoAdapter.setNumeroProcesso(tfdNumero.getText().trim());
							contratoAdapter.setTipo(cbxInstancia.getValue());

							if (contrato.getTipo_pagamento() == TipoPagamento.DEPOSITO_EM_CONTA)
								arquivo = "ContratoBanco.jrxml";
							else
								arquivo = "ContratoSem.jrxml";

							list.add(contratoAdapter);
							return list;
						} else
							throw new ValidacaoException("INFORME TODOS OS DADOS");

					} else
						return null;

				} else
					throw new ValidacaoException("INFORME SUA BUSCA");

			default:
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new ValidacaoException("Dados Incompletos!!! - Informe Todos os Dados Necessarios!!!");
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ValidacaoException(e.getMessage());
		} catch (BusinessException e) {
			e.printStackTrace();
			throw new ValidacaoException(e.getMessage());
		}

		throw new ValidacaoException("Dados N�o Encontrados!!! - Tente Procurar Informando Outro Dado");
	}

	@SuppressWarnings("deprecation")
	public void gerarDocumento(List<? extends Object> list, String layout) throws JRException, FileNotFoundException {

		// gerando o jasper design
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(layout);

		System.out.println(inputStream);
		JasperDesign desenho = JRXmlLoader.load(inputStream);

		// compila o relat�rio
		JasperReport relatorio = JasperCompileManager.compileReport(desenho);

		/* Convert List to JRBeanCollectionDataSource */
		JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(list);

		/* Map to hold Jasper report Parameters */
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ItemDataSource", itemsJRBean);

		/* Using compiled version(.jasper) of Jasper report to generate PDF */
		JasperPrint jasperPrint = JasperFillManager.fillReport(relatorio, parameters, itemsJRBean);

		JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
		jasperViewer.setZoomRatio(0.75F);
		jasperViewer.setLocationRelativeTo(null);
		jasperViewer.show();
		// JasperViewer.viewReport(jasperPrint);

	}

}
