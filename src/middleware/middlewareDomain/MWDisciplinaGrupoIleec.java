package middleware.middlewareDomain;

public class MWDisciplinaGrupoIleec
{

	private Integer idDisciplina;
	private Integer idGrupo;

	public String toString()
	{
		return " [idDisciplina] " + idDisciplina + " [idGrupo] " + idGrupo;

	}

	public Integer getIdDisciplina()
	{
		return idDisciplina;
	}

	public void setIdDisciplina(Integer idDisciplina)
	{
		this.idDisciplina = idDisciplina;
	}

	public Integer getIdGrupo()
	{
		return idGrupo;
	}

	public void setIdGrupo(Integer idGrupo)
	{
		this.idGrupo = idGrupo;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof MWDisciplinaGrupoIleec) {
			MWDisciplinaGrupoIleec mwDisciplinaGrupoIleec = (MWDisciplinaGrupoIleec) obj;
			result = this.getIdDisciplina().equals(mwDisciplinaGrupoIleec.getIdDisciplina()) && this.getIdGrupo().equals(mwDisciplinaGrupoIleec.getIdGrupo());
		}
		return result;
	}
}