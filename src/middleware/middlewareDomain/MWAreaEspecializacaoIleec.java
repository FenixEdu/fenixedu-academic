package middleware.middlewareDomain;


public class MWAreaEspecializacaoIleec
{
	private Integer idAreaEspecializacao;
	private Integer maxCreditos;
	private String nome;

	public String toString()
	{
		return " [idAreaEspeciaplizacao] "
			+ idAreaEspecializacao
			+ " [maxCreditos] "
			+ maxCreditos
			+ " [nome] "
			+ nome;
	}

	public Integer getIdAreaEspecializacao()
	{
		return this.idAreaEspecializacao;
	}

	public void setIdAreaEspecializacao(Integer areaId)
	{
		this.idAreaEspecializacao = areaId;
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
		if (obj instanceof MWAreaEspecializacaoIleec) {
			MWAreaEspecializacaoIleec mwAreasEspecializacaoIleec = (MWAreaEspecializacaoIleec) obj;
			result = this.getNome().equals(mwAreasEspecializacaoIleec.getNome()) && this.getMaxCreditos().equals(mwAreasEspecializacaoIleec.getMaxCreditos());
		}
		return result;
	}
}