package br.com.sga.business;

import java.util.Calendar;
import java.util.List;

import br.com.sga.dao.DaoContrato;
import br.com.sga.entidade.Contrato;
import br.com.sga.entidade.Parcela;
import br.com.sga.exceptions.BusinessException;
import br.com.sga.exceptions.DaoException;
import br.com.sga.exceptions.ValidacaoException;
import br.com.sga.interfaces.IBusinessContrato;
import br.com.sga.interfaces.IDaoContrato;
import br.com.sga.view.Alerta;

public class BusinessContrato implements IBusinessContrato {

	private IDaoContrato daoContrato;
	
	public BusinessContrato() {
		daoContrato = new DaoContrato();
	}

	@Override
	public void salvarEditar(Contrato entidade) throws BusinessException {
		
		try {
			validar(entidade);
			if(entidade.getId() == null) {
				gerarDadasDeParcelas(entidade);
				daoContrato.salvar(entidade);
			}else
				daoContrato.editar(entidade);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	private void gerarDadasDeParcelas(Contrato contrato) {
		Calendar c = Calendar.getInstance();
		c.setTime(contrato.getData_contrato());
		for(Parcela e : contrato.getParcelas()) {
			c.set(Calendar.DAY_OF_MONTH,e.dia_pagamento);
			e.setVencimento(c.getTime());
			c.set(Calendar.MONTH,Calendar.MONTH+1);
		}
	}
	
	@Override
	public Contrato buscarPorId(int id) throws BusinessException {
		try {
			return daoContrato.buscarPorId(id);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Contrato buscarPorCodigo(String codigo) throws BusinessException {
		try {
			return daoContrato.buscarPorCodigo(codigo);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public List<Contrato> buscarPorBusca(String busca) throws BusinessException {
		try {
			return daoContrato.buscarPorBusca(busca);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}
	
	private void validar(Contrato entidade) throws ValidacaoException{
		
	}

}
