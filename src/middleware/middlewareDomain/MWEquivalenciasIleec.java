package middleware.middlewareDomain;

public class MWEquivalenciasIleec
{
	private String codigoDisciplinaCurriculoActual;
	private String codigoDisciplinaCurriculoAntigo;
	private Integer idAreaCientifica;
	private Integer idAreaEspeciaplizacao;
	private Integer idAreaSecundaria;
	private Integer idEquivalencia;
	private Integer tipoEquivalencia;

	public String toString()
	{
		return " [codigoDisciplinaCurriculoActual] "
			+ codigoDisciplinaCurriculoActual
			+ " [codigoDisciplinaCurriculoAntigo] "
			+ codigoDisciplinaCurriculoAntigo
			+ " [idAreaCientifica] "
			+ idAreaCientifica
			+ " [idAreaEspeciaplizacao] "
			+ idAreaEspeciaplizacao
			+ " [idAreaSecundaria] "
			+ idAreaSecundaria
			+ " [idEquivalencia] "
			+ idEquivalencia
			+ " [tipoEquivalencia] "
			+ tipoEquivalencia;

	}

	public String getCodigoDisciplinaCurriculoActual()
	{
		return codigoDisciplinaCurriculoActual;
	}

	public void setCodigoDisciplinaCurriculoActual(String codigoDisciplinaCurriculoActual)
	{
		this.codigoDisciplinaCurriculoActual = codigoDisciplinaCurriculoActual;
	}

	public String getCodigoDisciplinaCurriculoAntigo()
	{
		return codigoDisciplinaCurriculoAntigo;
	}

	public void setCodigoDisciplinaCurriculoAntigo(String codigoDisciplinaCurriculoAntigo)
	{
		this.codigoDisciplinaCurriculoAntigo = codigoDisciplinaCurriculoAntigo;
	}

	public Integer getIdAreaCientifica()
	{
		return idAreaCientifica;
	}

	public void setIdAreaCientifica(Integer idAreaCientifica)
	{
		this.idAreaCientifica = idAreaCientifica;
	}

	public Integer getIdAreaEspeciaplizacao()
	{
		return idAreaEspeciaplizacao;
	}

	public void setIdAreaEspeciaplizacao(Integer idAreaEspeciaplizacao)
	{
		this.idAreaEspeciaplizacao = idAreaEspeciaplizacao;
	}

	public Integer getIdAreaSecundaria()
	{
		return idAreaSecundaria;
	}

	public void setIdAreaSecundaria(Integer idAreaSecundaria)
	{
		this.idAreaSecundaria = idAreaSecundaria;
	}

	public Integer getIdEquivalencia()
	{
		return idEquivalencia;
	}

	public void setIdEquivalencia(Integer idEquivalencia)
	{
		this.idEquivalencia = idEquivalencia;
	}

	public Integer getTipoEquivalencia()
	{
		return tipoEquivalencia;
	}

	public void setTipoEquivalencia(Integer tipoEquivalencia)
	{
		this.tipoEquivalencia = tipoEquivalencia;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof MWEquivalenciasIleec) {
			MWEquivalenciasIleec mwEquivalenciasIleec = (MWEquivalenciasIleec) obj;
			result = this.getCodigoDisciplinaCurriculoActual().equals(mwEquivalenciasIleec.getCodigoDisciplinaCurriculoActual()) &&
					this.getCodigoDisciplinaCurriculoAntigo().equals(mwEquivalenciasIleec.getCodigoDisciplinaCurriculoAntigo()) &&
					this.getIdAreaCientifica().equals(mwEquivalenciasIleec.getIdAreaCientifica()) &&
					this.getIdAreaEspeciaplizacao().equals(mwEquivalenciasIleec.getIdAreaEspeciaplizacao()) &&
					this.getIdAreaSecundaria().equals(mwEquivalenciasIleec.getIdAreaSecundaria()) &&
					this.getTipoEquivalencia().equals(mwEquivalenciasIleec.getTipoEquivalencia());
		}
		return result;
	}
}