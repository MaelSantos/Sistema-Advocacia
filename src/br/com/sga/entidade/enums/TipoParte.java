package br.com.sga.entidade.enums;

public enum TipoParte {
	
	ATIVO,PASSIVO;
	public static TipoParte getTipoParte(String tipo)
	{
		for(TipoParte t : values())
			if(t.toString().equals(tipo))
				return t;
		return null;
		
	}
	
}
