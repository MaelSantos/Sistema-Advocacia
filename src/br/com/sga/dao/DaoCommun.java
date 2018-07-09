package br.com.sga.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.sga.entidade.enums.Tabela;
import java.util.ArrayList;

import br.com.sga.entidade.Despesa;
import br.com.sga.entidade.Endereco;
import br.com.sga.entidade.Parcela;
import br.com.sga.entidade.Parte;
import br.com.sga.entidade.Receita;
import br.com.sga.entidade.Telefone;
import br.com.sga.entidade.Testemunha;
import br.com.sga.exceptions.DaoException;
import br.com.sga.interfaces.IDaoCommun;
import br.com.sga.sql.SQLConnection;
import br.com.sga.sql.SQLUtil;

public class DaoCommun implements IDaoCommun{
	Connection  connection ;
	ResultSet resultSet;
	PreparedStatement statement;
	
	@Override
	public int getCurrentValorTabela(Tabela tabela) throws DaoException {
		  try {
	            this.connection = SQLConnection.getConnectionInstance(SQLConnection.NOME_BD_CONNECTION_POSTGRESS);
	            this.statement = connection.prepareStatement("select id from " + tabela.toString() + " order by id desc limit 1");

	            resultSet = this.statement.executeQuery();
	            int id;
	            if (resultSet.next()) {
	                id = resultSet.getInt(1);
	            } else {
	                throw new DaoException("Não há registro nessa tabela");
	            }
	            this.connection.close();
	            return id;

	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            throw new DaoException("PROBLEMA AO CONSULTAR " + tabela.toString() + " - Contate o ADM");
	        }
	}
	public void salvarTestemunha(Testemunha entidade,Integer consulta_id) throws DaoException {
		try {
			salvarEndereco(entidade.getEndereco());
			int endereco_id = getCurrentValorTabela(Tabela.ENDERECO);
			
			this.connection = SQLConnection.getConnectionInstance(SQLConnection.NOME_BD_CONNECTION_POSTGRESS);
			this.statement = connection.prepareStatement(SQLUtil.Testemunha.INSERT_ALL);
			statement.setString(1,entidade.getNome());
			statement.setInt(2,endereco_id);
			statement.setInt(3, consulta_id);
			statement.execute();
			int testemunha_id = getCurrentValorTabela(Tabela.TESTEMUNHA);
			this.connection.close();
			salvarContato(entidade.getTelefone(),testemunha_id,Tabela.TESTEMUNHA);

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DaoException("PROBLEMA AO SALVAR TESTEMUNHA - CONTATE O ADM");
		}
	}
	@Override
	public void salvarVinculoFuncionario(Integer notificacao_id, Integer funcionario_id)  throws DaoException {
		try {
			connection = SQLConnection.getConnectionInstance(SQLConnection.NOME_BD_CONNECTION_POSTGRESS);
			statement  = connection.prepareStatement(SQLUtil.VinculoFuncionario.INSERT_ALL);
			statement.setInt(1,notificacao_id);
			statement.setInt(2,funcionario_id);
			statement.execute();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
	        throw new DaoException("PROBLEMA AO SALVAR VINCULO FUNCIONARIO/NOTIFICA��O CONTATE ADM");
		}
	}

    @Override
    public void salvarEndereco(Endereco endereco) throws DaoException {
        try {
            this.connection = SQLConnection.getConnectionInstance(SQLConnection.NOME_BD_CONNECTION_POSTGRESS);
            this.statement = connection.prepareStatement(SQLUtil.Endereco.INSERT_ALL);
            //rua, numero, bairro, cidade, cep, pais, estado, complemento
            statement.setString(1, endereco.getRua());
            statement.setString(2, endereco.getNumero());
            statement.setString(3, endereco.getBairro());
            statement.setString(4, endereco.getCidade());
            statement.setString(5, endereco.getCep());
            statement.setString(6, endereco.getPais());
            statement.setString(7, endereco.getEstado());
            statement.setString(8, endereco.getComplemento());
            statement.execute();
            this.connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("PROBLEMA AO SALVAR ENDERECO - Contate o ADM");
        }
    }

    @Override
    public void salvarContato(Telefone telefone, int id,Tabela tabela) throws DaoException {

        try {
            this.connection = SQLConnection.getConnectionInstance(SQLConnection.NOME_BD_CONNECTION_POSTGRESS);
            if(tabela == Tabela.CLIENTE)
            	this.statement = connection.prepareStatement(SQLUtil.Telefone.INSERT_ALL_PARA_CLIENTE);
            else
            	this.statement = connection.prepareStatement(SQLUtil.Telefone.INSERT_ALL_PARA_TESTEMUNHA);
            statement.setInt(1, telefone.getNumero());
            statement.setInt(2, telefone.getPrefixo());
            statement.setInt(3, id);
            statement.execute();
            this.connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("PROBLEMA AO SALVAR ENDERECO - Contate o ADM");
        }

    }

    @Override
    public List<Telefone> getContatos(Integer id) throws DaoException {

       try {
            this.connection = SQLConnection.getConnectionInstance(SQLConnection.NOME_BD_CONNECTION_POSTGRESS);
            this.statement = connection.prepareStatement(SQLUtil.Telefone.SELECT_TELEFONE_CLIENTE);
            this.statement.setInt(1, id);

            resultSet = this.statement.executeQuery();
            List<Telefone> contatos = new ArrayList<>();
            Telefone telefone;
            while (resultSet.next()) {
//            	id ,cliente_id, numero, prefixo
                telefone = new Telefone();
                telefone.setId(resultSet.getInt(1));
                telefone.setPrefixo(resultSet.getInt(2));
                telefone.setNumero(resultSet.getInt(3));
                contatos.add(telefone);
            }
            this.connection.close();

            return contatos;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("PROBLEMA AO CONSULTAR CONTATOS - Contate o ADM");
        }

    }
	@Override
	public void salvarParte(Parte parte, Integer contrato_id) throws DaoException {
		try {
            this.connection = SQLConnection.getConnectionInstance(SQLConnection.NOME_BD_CONNECTION_POSTGRESS);
            this.statement = connection.prepareStatement(SQLUtil.Parte.INSERT_ALL);
           //nome,tipo_parte,tipo_participacao,contrato_id
            statement.setString(2, parte.getTipo_parte().toString());
            statement.setString(3, parte.getTipo_participacao().toString());
            statement.setString(1, parte.getNome());
            statement.setInt(4, contrato_id);
            statement.execute();
            this.connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("PROBLEMA AO SALVAR PARTE - Contate o ADM");
        }

	}
	@Override
	public void salvarReceita(Receita receita, Integer financeiro_id) throws DaoException {
		try {
			this.connection = SQLConnection.getConnectionInstance(SQLConnection.NOME_BD_CONNECTION_POSTGRESS);
            this.statement = connection.prepareStatement(SQLUtil.Receita.INSERT_ALL);
           //data_entrada,centro_custo,decricao,valor,status,tipo_pagamento,vencimento,financeiro_id
            statement.setDate(1,new Date(receita.getData_entrada().getTime()));
            statement.setString(2,receita.getCentro_custo());
            statement.setString(3,receita.getDescricao());
            statement.setFloat(4,receita.getValor());
            statement.setBoolean(5,receita.getStatus());
            statement.setString(6,receita.getTipo_pagamento());
            statement.setDate(7,new Date(receita.getVencimento().getTime()));
            statement.setInt(8,financeiro_id);
            statement.execute();
            this.connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("PROBLEMA AO SALVAR RECEITA - Contate o ADM");
        }

		
	}
	@Override
	public void salvarDespesa(Despesa despesa, Integer financeiro_id) throws DaoException {
		try {
			this.connection = SQLConnection.getConnectionInstance(SQLConnection.NOME_BD_CONNECTION_POSTGRESS);
            this.statement = connection.prepareStatement(SQLUtil.Despesa.INSERT_ALL);
            
            statement.setDate(1,new Date(despesa.getData_retirada().getTime()));
            statement.setString(2,despesa.getCentro_custo());
            statement.setString(3,despesa.getDescricao());
            statement.setFloat(4,despesa.getValor());
            statement.setBoolean(5,despesa.getStatus());
            statement.setString(6,despesa.getTipo_gasto());
            statement.setDate(7,new Date(despesa.getVencimento().getTime()));
            statement.setInt(8,financeiro_id);
            statement.execute();
            this.connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("PROBLEMA AO SALVAR DESPESA - Contate o ADM");
        }

		
	}
	@Override
	public void salvarParcela(Parcela parcela, Integer contrato_id) throws DaoException {
		try {
			this.connection = SQLConnection.getConnectionInstance(SQLConnection.NOME_BD_CONNECTION_POSTGRESS);
            this.statement = connection.prepareStatement(SQLUtil.Parcela.INSERT_ALL);
           //valor,tipo,estado,juros,multa,vencimento,contrato_id
            statement.setFloat(1,parcela.getValor());
            statement.setString(2,parcela.getTipo());
            statement.setString(3,parcela.getEstado());
            statement.setFloat(4,parcela.getJuros());
            statement.setFloat(5,parcela.getMulta());
            statement.setDate(6,new Date(parcela.getVencimento().getTime()));
            statement.setInt(7,contrato_id);
            statement.execute();
            this.connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("PROBLEMA AO SALVAR DESPESA - Contate o ADM");
        }
	}

}