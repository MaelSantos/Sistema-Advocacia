package br.com.sga.business;

import java.util.List;

import br.com.sga.dao.DaoUsuario;
import br.com.sga.entidade.Funcionario;
import br.com.sga.exceptions.BusinessException;
import br.com.sga.exceptions.DaoException;
import br.com.sga.exceptions.ValidacaoException;
import br.com.sga.interfaces.IBusinessUsuario;
import br.com.sga.interfaces.IDaoUsuario;

public class BusinessUsuario implements IBusinessUsuario {

	private IDaoUsuario daoUsuario;
	
	public BusinessUsuario() {
		daoUsuario = new DaoUsuario();
	}

	@Override
	public void salvar(Funcionario usuario) throws BusinessException{
		try {
			
			validarUsuario(usuario);
			if(usuario.getId() == null)
				daoUsuario.salvar(usuario);
			else
				daoUsuario.editar(usuario);
		}catch (ValidacaoException e) {
			throw new BusinessException(e.getMessage());
		} catch (DaoException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());	
		}		
	}
	
	@Override
	public Funcionario buscarPorId(int id) throws BusinessException {
		// TODO Stub de m�todo gerado automaticamente
		return null;
	}

	@Override
	public List<Funcionario> buscarPorBusca(String busca) throws BusinessException {
		// TODO Stub de m�todo gerado automaticamente
		return null;
	}

	private void validarUsuario(Funcionario usuario) throws ValidacaoException, DaoException{
		if(usuario.getLogin().trim().equals("") || usuario.getSenha().trim().equals(""))
			throw new ValidacaoException("DADOS EM BRANCO!!!");
		if(usuario.getSenha().trim().length() < 8)
			throw new ValidacaoException("SENHA MUITO CURTA!!!");
	}

	@Override
	public Funcionario buscarPorLogin(String login, String senha) throws BusinessException {
		try {
			return  daoUsuario.buscarPorLogin(login, senha);
		} catch (DaoException e) {
			throw new BusinessException(e.getMessage());
		}
	}

}
