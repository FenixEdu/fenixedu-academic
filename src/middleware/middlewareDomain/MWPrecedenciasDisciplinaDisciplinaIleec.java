package middleware.middlewareDomain;

public class MWPrecedenciasDisciplinaDisciplinaIleec
{

	private Integer idDisciplina;
	private Integer idPrecedente;

	public Integer getIdDisciplina()
	{
		return this.idDisciplina;
	}

	public void setIdDisciplina(Integer param)
	{
		this.idDisciplina = param;
	}

	public Integer getIdPrecedente()
	{
		return this.idPrecedente;
	}
	public void setIdPrecedente(Integer param)
	{
		this.idPrecedente = param;
	}

	public String toString()
	{
		return " [idDisciplina] " + idDisciplina + " [idPrecedente] " + idPrecedente;
	}

	public boolean equals(Object obj)
	{
		boolean result = false;
		if (obj instanceof MWPrecedenciasDisciplinaDisciplinaIleec)
		{
			MWPrecedenciasDisciplinaDisciplinaIleec mwpddi = (MWPrecedenciasDisciplinaDisciplinaIleec) obj;
			result = getIdDisciplina().equals(mwpddi.getIdDisciplina()) && getIdPrecedente().equals(mwpddi.getIdPrecedente());
		}
		return result;
	}
}