
/*
 * Student.java
 *
 * Created on 28 of December 2002, 17:04
 */

package Dominio;

import Util.TipoCurso;

/**
 *
 * @author Ricardo Nortadas
 */


public class Student implements IStudent {
  protected Integer number;
  protected Integer state;
  protected TipoCurso degreeType;
    
  // códigos internos da base de dados
  private Integer internalCode;
  private Integer personKey;
  private IPessoa person;
   
  /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
  public Student() {}
   
  public Student(Integer number,Integer state,IPessoa person, TipoCurso degreeType) {
    setNumber(number);
    setState(state);
    setPerson(person);
	setDegreeType(degreeType);
  }
 
  
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof IStudent) {
      IStudent student = (IStudent)obj;
      resultado = (getNumber() == student.getNumber()) &&
				  (getDegreeType().getTipoCurso().equals(student.getDegreeType().getTipoCurso()));
    } 
    return resultado;
  } 
  
  public String toString() {
    String result = "[STUDENT";
    result += ", Internal code =" + internalCode;
    result += ", Number =" + number;
    result += ", State =" + state;
    result += ", Degree Type =" + degreeType.toString();
    result += "]";
    return result;
  }
 
    
	/**
	 * Returns the degreeType.
	 * @return TipoCurso
	 */
	public TipoCurso getDegreeType() {
		return degreeType;
	}
	
	/**
	 * Returns the internalCode.
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}
	
	/**
	 * Returns the number.
	 * @return Integer
	 */
	public Integer getNumber() {
		return number;
	}
	
	/**
	 * Returns the person.
	 * @return IPessoa
	 */
	public IPessoa getPerson() {
		return person;
	}
	
	/**
	 * Returns the personKey.
	 * @return Integer
	 */
	public Integer getPersonKey() {
		return personKey;
	}
	
	/**
	 * Returns the state.
	 * @return Integer
	 */
	public Integer getState() {
		return state;
	}
	
	/**
	 * Sets the degreeType.
	 * @param degreeType The degreeType to set
	 */
	public void setDegreeType(TipoCurso degreeType) {
		this.degreeType = degreeType;
	}
	
	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}
	
	/**
	 * Sets the number.
	 * @param number The number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	/**
	 * Sets the person.
	 * @param person The person to set
	 */
	public void setPerson(IPessoa person) {
		this.person = person;
	}
	
	/**
	 * Sets the personKey.
	 * @param personKey The personKey to set
	 */
	public void setPersonKey(Integer personKey) {
		this.personKey = personKey;
	}
	
	/**
	 * Sets the state.
	 * @param state The state to set
	 */
	public void setState(Integer state) {
		this.state = state;
	}

}
