package middleware.middlewareDomain;


public class MWAreaSecundariaIleec
{
	private Integer idAreaEspecializacao;
	private Integer idAreaSecundaria;
	private Integer maxCreditos;
	private String nome;

	public String toString()
	{
		return " [idAreaEspeciaplizacao] "
			+ idAreaEspecializacao
			+ " [idAreaSecundaria] "
			+ idAreaSecundaria
			+ " [maxCreditos] "
			+ maxCreditos
			+ " [nome] "
			+ nome;
	}

	public Integer getIdAreaEspecializacao()
	{
		return idAreaEspecializacao;
	}

	public void setIdAreaEspecializacao(Integer idAreaEspeciaplizacao)
	{
		this.idAreaEspecializacao = idAreaEspeciaplizacao;
	}

	public Integer getIdAreaSecundaria()
	{
		return idAreaSecundaria;
	}

	public void setIdAreaSecundaria(Integer idAreaSecundaria)
	{
		this.idAreaSecundaria = idAreaSecundaria;
	}

	public Integer getMaxCreditos()
	{
		return maxCreditos;
	}

	public void setMaxCreditos(Integer maxCreditos)
	{
		this.maxCreditos = maxCreditos;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof MWAreaSecundariaIleec) {
			MWAreaSecundariaIleec mwAreasSecundariasIleec = (MWAreaSecundariaIleec) obj;
			result =	this.getNome().equals(mwAreasSecundariasIleec.getNome()) &&
						this.getMaxCreditos().equals(mwAreasSecundariasIleec.getMaxCreditos()) &&
						this.getIdAreaEspecializacao().equals(mwAreasSecundariasIleec.getIdAreaEspecializacao()) &&
						this.getIdAreaSecundaria().equals(mwAreasSecundariasIleec.getIdAreaSecundaria());
		}
		return result;
	}
}