package br.com.sga.interfaces;

import java.util.List;

import br.com.sga.entidade.Processo;
import br.com.sga.entidade.adapter.ProcessoAdapter;
import br.com.sga.exceptions.DaoException;

public interface IDaoProcesso extends IDao<Processo> {
	
	public List<ProcessoAdapter> buscarAllAdapter(String tipo)throws DaoException;
	
	public List<ProcessoAdapter> buscaPorClienteAdapter(int id_cliente) throws DaoException;
		
	public List<Processo> buscarPorBusca(String busca) throws DaoException;
	
	public List<ProcessoAdapter> buscarPorBusca(String[] busca) throws DaoException;
	
}
