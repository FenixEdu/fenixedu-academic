package middleware.middlewareDomain;

public class MWTipoEquivalenciaIleec extends MWDomainObject
{
	private String descricao;
	private Integer tipoEquivalencia;

	public String getDescricao()
	{
		return this.descricao;
	}

	public void setDescricao(String param)
	{
		this.descricao = param;
	}

	public Integer getTipoEquivalencia()
	{
		return this.tipoEquivalencia;
	}

	public void setTipoEquivalencia(Integer param)
	{
		this.tipoEquivalencia = param;
	}

	public String toString()
	{
		return " [descricao] " + descricao + " [tipoEquivalencia] " + tipoEquivalencia;
	}

	public boolean equals(Object obj)
	{
		boolean result = false;
		if (obj instanceof MWTipoEquivalenciaIleec)
		{
			MWTipoEquivalenciaIleec mwepi = (MWTipoEquivalenciaIleec) obj;
			result = getTipoEquivalencia().equals(mwepi.getTipoEquivalencia()) && getDescricao().equals(mwepi.getDescricao());
		}
		return result;
	}
}