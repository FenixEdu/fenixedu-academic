package middleware;

public class AlunoDisciplinaCurricular {
    private Integer numeroAluno;
    private String siglaDisciplinaCurricular;
    private String nomeDisciplinaCurricular;
//    private int chaveDisciplinaCurricular; 

	public AlunoDisciplinaCurricular(Integer numeroAluno,
									 String siglaDisciplinaCurricular,
									 String nomeDisciplinaCurricular) {
		setNomeDisciplinaCurricular(nomeDisciplinaCurricular);
		setSiglaDisciplinaCurricular(siglaDisciplinaCurricular);
		setNumeroAluno(numeroAluno);		
	}

	/**
	 * Returns the nomeDisciplinaCurricular.
	 * @return String
	 */
	public String getNomeDisciplinaCurricular() {
		return nomeDisciplinaCurricular;
	}

	/**
	 * Returns the numeroAluno.
	 * @return Integer
	 */
	public Integer getNumeroAluno() {
		return numeroAluno;
	}

	/**
	 * Returns the siglaDisciplinaCurricular.
	 * @return String
	 */
	public String getSiglaDisciplinaCurricular() {
		return siglaDisciplinaCurricular;
	}

	/**
	 * Sets the nomeDisciplinaCurricular.
	 * @param nomeDisciplinaCurricular The nomeDisciplinaCurricular to set
	 */
	public void setNomeDisciplinaCurricular(String nomeDisciplinaCurricular) {
		this.nomeDisciplinaCurricular = nomeDisciplinaCurricular;
	}

	/**
	 * Sets the numeroAluno.
	 * @param numeroAluno The numeroAluno to set
	 */
	public void setNumeroAluno(Integer numeroAluno) {
		this.numeroAluno = numeroAluno;
	}

	/**
	 * Sets the siglaDisciplinaCurricular.
	 * @param siglaDisciplinaCurricular The siglaDisciplinaCurricular to set
	 */
	public void setSiglaDisciplinaCurricular(String siglaDisciplinaCurricular) {
		this.siglaDisciplinaCurricular = siglaDisciplinaCurricular;
	}

}
