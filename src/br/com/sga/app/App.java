package br.com.sga.app;

import java.util.ArrayList;

import br.com.sga.interfaces.Ouvinte;
import br.com.sga.entidade.enums.Tela;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application{
	
	static Stage stage;
	
	static Scene loginScene;
	static Scene menuScene;
	
	static Pane login, home, cadastro, menu, informacoes, editarPerfil, perfil, pesquisa, configuracoes,
	clientes, cadastroCliente, cadastroContrato, processo, cadastrarProcesso, detalhesProcesso,
	buscarContrato, cadastrarAudiencia, financeiro,cadastroConsulta, cadastroReceitaDespesa, agenda,
	consulta,detalhesConsulta,detalhesContrato,documentos, historico,estatistica;
	
	@SuppressWarnings("static-access")
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		stage.centerOnScreen();
		Parent parent = FXMLLoader.load(getClass().getResource("../view/Carregar.fxml"));
		Scene scene = new Scene(parent);
		stage.setScene(scene);
		stage.show();
		stage.setTitle("SGA - Sistema De Gerenciamento Advocativo");
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(("Icon.png"))));
	}
	
	public static void changeStage(Tela tela)
	{
		switch (tela) {
		case login:
			stage.setScene(loginScene);
			stage.centerOnScreen();
			
			break;
		case menu:
			stage.setScene(menuScene);
			stage.setMaximized(true);
			stage.centerOnScreen(); 
			break;
		default:
			break;
		}
	}
	
	public static Pane changePane(Tela tela)
	{
		switch (tela) {
		case home:
			return home;
		case informacoes:
			return informacoes;
		case cadastro:
			return cadastro;
		case perfil:
			return perfil;
		case editar_perfil:
			return editarPerfil;
		case configuracoes:
			return configuracoes;
		case clientes:
			return clientes;
		case cadastro_cliente:
			return cadastroCliente;
		case cadastro_contrato:
			return cadastroContrato;
		case processos:
			return processo;
		case cadastro_processo:
			return cadastrarProcesso;
		case detalhes_processo:
			return detalhesProcesso;
		case buscar_contrato:
			return buscarContrato;
		case cadastro_audiencia:
			return cadastrarAudiencia;
		case financeiro:
			return financeiro;
		case cadastro_consulta:
			return cadastroConsulta;
		case Cadastro_Receita_Despesa:
			return cadastroReceitaDespesa;
		case agenda:
			return agenda;
		case Consulta:
			return consulta;
		case Detalhes_consulta:
			return detalhesConsulta;
		case Detalhes_contrato:
			return detalhesContrato;
		case documentos:
			return documentos;
		case historico:
			return historico;
		case Estatistica:
			return estatistica;
		default:
			break;
		}
		
		return new Pane();
	}
	
	private static ArrayList<Ouvinte> ouvintes = new ArrayList<>();

	public static void notificarOuvintes(Tela tela, Object object) {
		for(Ouvinte ouvinte : ouvintes)
			ouvinte.atualizar(tela, object);
	}
	
	public static void notificarOuvintes(Tela tela) {
			notificarOuvintes(tela, null);
	}
	
	public static void addOuvinte(Ouvinte ouvinte) {
		ouvintes.add(ouvinte);
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}

}
