/*
 * Departamento.java
 *
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package Dominio;
import java.util.Set;

public class Departamento implements IDepartamento{
    
    private Integer codigoInterno;
    private String nome;
    private String sigla;
    private Set disciplinasAssociadas;
        
    public Departamento() {
        setCodigoInterno(null);
        setNome("");
        setSigla("");
    }
    
    public Departamento(String nome, String sigla) {
        setCodigoInterno(null);
        setNome(nome);
        setSigla(sigla);
    }
    
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof Departamento ) {
            Departamento d = (Departamento)obj;
            resultado = ( getNome().equals(d.getNome()) && getSigla().equals(d.getSigla()) );
        }
        return resultado;
    }
    
    
  public String toString() {
    String result = "[DEPARTAMENTO";
    result += ", codInt=" + codigoInterno;
    result += ", sigla=" + sigla;
    result += ", nome=" + nome;
    result += ", disciplinasAssociadas=" + disciplinasAssociadas;
    result += "]";
    return result;
  }    
    
	/**
	 * Returns the codigoInterno.
	 * @return Integer
	 */
	public Integer getCodigoInterno() {
		return codigoInterno;
	}

	/**
	 * Returns the disciplinasAssociadas.
	 * @return Set
	 */
	public Set getDisciplinasAssociadas() {
		return disciplinasAssociadas;
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
	 * Sets the codigoInterno.
	 * @param codigoInterno The codigoInterno to set
	 */
	public void setCodigoInterno(Integer codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	/**
	 * Sets the disciplinasAssociadas.
	 * @param disciplinasAssociadas The disciplinasAssociadas to set
	 */
	public void setDisciplinasAssociadas(Set disciplinasAssociadas) {
		this.disciplinasAssociadas = disciplinasAssociadas;
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
