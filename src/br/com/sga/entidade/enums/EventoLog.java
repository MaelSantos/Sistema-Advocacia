package br.com.sga.entidade.enums;

public enum EventoLog {

	LOGIN, ENCERRAR, EDITAR, SALVAR, CADASTRAR, APAGAR, BUSCAR, GERAR;

	public static EventoLog getEvento(String evento)
	{
		for(EventoLog e : values())
			if(e.name().equalsIgnoreCase(evento))
				return e;
		return null;
	}
	
}

