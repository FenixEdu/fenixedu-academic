package middleware.middlewareDomain;

public class MWEquivalenciaIleec
{
	private String codigoDisciplinaCurriculoActual;
	private String codigoDisciplinaCurriculoAntigo;
	private Integer idAreaCientifica;
	private Integer idAreaEspecializacao;
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
			+ idAreaEspecializacao
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
		if (obj instanceof MWEquivalenciaIleec) {
			MWEquivalenciaIleec mwEquivalenciasIleec = (MWEquivalenciaIleec) obj;
			result = this.getCodigoDisciplinaCurriculoActual().equals(mwEquivalenciasIleec.getCodigoDisciplinaCurriculoActual()) &&
					this.getCodigoDisciplinaCurriculoAntigo().equals(mwEquivalenciasIleec.getCodigoDisciplinaCurriculoAntigo()) &&
					this.getIdAreaCientifica().equals(mwEquivalenciasIleec.getIdAreaCientifica()) &&
					this.getIdAreaEspecializacao().equals(mwEquivalenciasIleec.getIdAreaEspecializacao()) &&
					this.getIdAreaSecundaria().equals(mwEquivalenciasIleec.getIdAreaSecundaria()) &&
					this.getTipoEquivalencia().equals(mwEquivalenciasIleec.getTipoEquivalencia());
		}
		return result;
	}
}