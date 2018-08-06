package br.com.sga.business;

import java.util.Date;
import java.util.List;

import br.com.sga.dao.DaoCommun;
import br.com.sga.dao.DaoFinanceiro;
import br.com.sga.entidade.Financeiro;
import br.com.sga.entidade.Notificacao;
import br.com.sga.entidade.adapter.ReceitaAdapter;
import br.com.sga.exceptions.BusinessException;
import br.com.sga.exceptions.DaoException;
import br.com.sga.interfaces.IBusinessFinanceiro;
import br.com.sga.interfaces.IDaoCommun;
import br.com.sga.interfaces.IDaoFinanceiro;

public class BusinessFinanceiro implements IBusinessFinanceiro {
	private IDaoFinanceiro daoFinanceiro;
	private IDaoCommun daoCommun;
	public BusinessFinanceiro() {
		daoFinanceiro = new DaoFinanceiro();
		daoCommun = DaoCommun.getInstance();
	}

	@Override
	public Financeiro buscarPorAno(Integer ano) throws BusinessException {
		try {
			return daoFinanceiro.buscarPorAno(ano);
		} catch (DaoException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void salvarEditar(Financeiro financeiro) throws BusinessException {
		try {
			if(financeiro.getId() == null)
				daoFinanceiro.salvar(financeiro);
			else
				daoFinanceiro.editar(financeiro);
		} catch (DaoException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Financeiro buscarPorId(int id) throws BusinessException {
		// TODO Stub de m�todo gerado automaticamente
		return null;
	}

	@Override
	public List<Financeiro> buscarPorBusca(String busca) throws BusinessException {
		// TODO Stub de m�todo gerado automaticamente
		return null;
	}

	@Override
	public Financeiro buscarPorIntervalo(Date de, Date ate) throws BusinessException {
		try {
			return daoFinanceiro.buscarPorIntervalo(de, ate);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public List<ReceitaAdapter> buscarReceitasTotalMesPorIntervalo(Date de, Date ate) throws BusinessException {
		try {
			return daoCommun.getReceitaTotalMesPorIntervalo(de, ate);
		} catch (DaoException e) {
			throw new BusinessException(e.getMessage());
		}
	}

}
