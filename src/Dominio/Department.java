/*
 * Department.java
 *
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package Dominio;

import java.util.Set;

public class Department extends DomainObject implements IDepartment {
    
    private String name;
    private String code;
    private Set disciplinasAssociadas;
        
    public Department() {
    }
    
    public Department(String nome, String sigla) {
        setName(nome);
        setCode(sigla);
    }
    
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof Department ) {
            Department d = (Department)obj;
            resultado = getCode().equals(d.getCode());
        }
        return resultado;
    }
    
    
  public String toString() {
    String result = "[DEPARTAMENT";
    result += ", codInt=" + getIdInternal();
    result += ", sigla=" + code;
    result += ", nome=" + name;
    //result += ", disciplinasAssociadas=" + disciplinasAssociadas;
    result += "]";
    return result;
  }    
    

	/**
	 * Returns the disciplinasAssociadas.
	 * @return Set
	 */
	public Set getDisciplinasAssociadas() {
		return disciplinasAssociadas;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	/**
	 * Sets the disciplinasAssociadas.
	 * @param disciplinasAssociadas The disciplinasAssociadas to set
	 */
	public void setDisciplinasAssociadas(Set disciplinasAssociadas) {
		this.disciplinasAssociadas = disciplinasAssociadas;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
