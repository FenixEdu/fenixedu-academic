package middleware.middlewareDomain;

public class MWDisciplinaIleec
{
	private String codigoDisciplina;
	private Integer funciona;
	private Integer anoCurricular;
	private Integer idAreaCientifica;
	private Integer idDisciplina;
	private Integer semestre;
	private Integer inscObrigatoria;
	private String nome;
	private Integer numeroCreditos;
	private Integer obrigatoria;
	private Integer tipoPrecedencia;

	public String toString()
	{
		return " [codigoDisciplina] "
			+ codigoDisciplina
			+ " [funciona] "
			+ funciona
			+ " [anoCurricular] "
			+ anoCurricular
			+ " [idAreaCientifica] "
			+ idAreaCientifica
			+ " [idDisciplina] "
			+ idDisciplina
			+ " [semestre] "
			+ semestre
			+ " [inscObrigatoria] "
			+ inscObrigatoria
			+ " [nome] "
			+ nome
			+ " [numeroCreditos] "
			+ numeroCreditos
			+ " [obrigatoria] "
			+ obrigatoria
			+ " [tipoPrecedencia] "
			+ tipoPrecedencia;
	}

	public Integer getAnoCurricular()
	{
		return anoCurricular;
	}

	public void setAnoCurricular(Integer anoCurricular)
	{
		this.anoCurricular = anoCurricular;
	}

	public String getCodigoDisciplina()
	{
		return codigoDisciplina;
	}

	public void setCodigoDisciplina(String codigoDisciplina)
	{
		this.codigoDisciplina = codigoDisciplina;
	}

	public Integer getFunciona()
	{
		return funciona;
	}

	public void setFunciona(Integer funciona)
	{
		this.funciona = funciona;
	}

	public Integer getIdAreaCientifica()
	{
		return idAreaCientifica;
	}

	public void setIdAreaCientifica(Integer idAreaCientifica)
	{
		this.idAreaCientifica = idAreaCientifica;
	}

	public Integer getIdDisciplina()
	{
		return idDisciplina;
	}

	public void setIdDisciplina(Integer idDisciplina)
	{
		this.idDisciplina = idDisciplina;
	}

	public Integer getInscObrigatoria()
	{
		return inscObrigatoria;
	}

	public void setInscObrigatoria(Integer inscObrigatoria)
	{
		this.inscObrigatoria = inscObrigatoria;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public Integer getNumeroCreditos()
	{
		return numeroCreditos;
	}

	public void setNumeroCreditos(Integer numeroCreditos)
	{
		this.numeroCreditos = numeroCreditos;
	}

	public Integer getObrigatoria()
	{
		return obrigatoria;
	}

	public void setObrigatoria(Integer obrigatoria)
	{
		this.obrigatoria = obrigatoria;
	}

	public Integer getSemestre()
	{
		return semestre;
	}

	public void setSemestre(Integer semestre)
	{
		this.semestre = semestre;
	}

	public Integer getTipoPrecedencia()
	{
		return tipoPrecedencia;
	}

	public void setTipoPrecedencia(Integer tipoPrecedencia)
	{
		this.tipoPrecedencia = tipoPrecedencia;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof MWDisciplinaIleec) {
			MWDisciplinaIleec mwDisciplinasIleec = (MWDisciplinaIleec) obj;
			result = this.getCodigoDisciplina().equals(mwDisciplinasIleec.getCodigoDisciplina());
		}
		return result;
	}
}