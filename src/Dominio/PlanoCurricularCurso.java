/*
 * PlanoCurricular.java
 *
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package Dominio;


public class PlanoCurricularCurso implements IPlanoCurricularCurso{
    
    private Integer codigoInterno;
    private Integer chaveCurso;
    private ICurso curso;
    private String nome;
    private String sigla;
        
    public PlanoCurricularCurso() { }
    
    public PlanoCurricularCurso(String nome, String sigla, ICurso licenciatura) {
        setNome(nome);
        setSigla(sigla);
        setCurso(licenciatura);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof PlanoCurricularCurso ) {
            PlanoCurricularCurso p = (PlanoCurricularCurso)obj;
            resultado = ( getNome().equals(p.getNome()) && getSigla().equals(p.getSigla()) );
        }
        return resultado;
    }
    
  public String toString() {
    String result = "[PLANO_CURRICULAR_CURSO";
    result += ", codInt=" + codigoInterno;
    result += ", nome=" + nome;
    result += ", sigla=" + sigla;
    result += ", curso=" + curso;
    result += "]";
    return result;
  }
    
    
    
	/**
	 * Returns the chaveCurso.
	 * @return Integer
	 */
	public Integer getChaveCurso() {
		return chaveCurso;
	}

	/**
	 * Returns the codigoInterno.
	 * @return Integer
	 */
	public Integer getCodigoInterno() {
		return codigoInterno;
	}

	/**
	 * Returns the curso.
	 * @return ICurso
	 */
	public ICurso getCurso() {
		return curso;
	}

	/**
	 * Returns the nome.
	 * @return String
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Returns the sigla.
	 * @return String
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * Sets the chaveCurso.
	 * @param chaveCurso The chaveCurso to set
	 */
	public void setChaveCurso(Integer chaveCurso) {
		this.chaveCurso = chaveCurso;
	}

	/**
	 * Sets the codigoInterno.
	 * @param codigoInterno The codigoInterno to set
	 */
	public void setCodigoInterno(Integer codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	/**
	 * Sets the curso.
	 * @param curso The curso to set
	 */
	public void setCurso(ICurso curso) {
		this.curso = curso;
	}

	/**
	 * Sets the nome.
	 * @param nome The nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Sets the sigla.
	 * @param sigla The sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
