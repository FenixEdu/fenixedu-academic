package middleware.middlewareDomain;

public class MWPrecedenciasNumeroDisciplinasIleec
{
	private Integer idDisciplina;
	private Integer numeroDisciplinas;

	public Integer getIdDisciplina()
	{
		return this.idDisciplina;
	}

	public void setIdDisciplina(Integer param)
	{
		this.idDisciplina = param;
	}

	public Integer getNumeroDisciplinas()
	{
		return this.numeroDisciplinas;
	}
	
	public void setNumeroDisciplinas(Integer param)
	{
		this.numeroDisciplinas = param;
	}

	public String toString()
	{
		return " [idDisciplina] " + idDisciplina + " [numeroDisciplinas] " + numeroDisciplinas;

	}

	public boolean equals(Object obj)
	{
		boolean result = false;
		if (obj instanceof MWPrecedenciasNumeroDisciplinasIleec)
		{
			MWPrecedenciasNumeroDisciplinasIleec mwpnd = (MWPrecedenciasNumeroDisciplinasIleec) obj;
			result = getIdDisciplina().equals(mwpnd.getIdDisciplina()) && getNumeroDisciplinas().equals(mwpnd.getNumeroDisciplinas());
		}
		return result;
	}
}