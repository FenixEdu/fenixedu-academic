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
    private String name;

        
    public PlanoCurricularCurso() { }
    
    public PlanoCurricularCurso(String nome, ICurso licenciatura) {
        setName(nome);
        setCurso(licenciatura);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof PlanoCurricularCurso ) {
            PlanoCurricularCurso p = (PlanoCurricularCurso)obj;
            resultado = ( getName().equals(p.getName()) && getCurso().equals(curso));
        }
        return resultado;
    }
    
  public String toString() {
    String result = "[PLANO_CURRICULAR_CURSO";
    result += ", codInt=" + codigoInterno;
    result += ", name=" + name;
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
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
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
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String nome) {
		this.name = nome;
	}


}
