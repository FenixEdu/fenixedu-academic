/*
 * InfoStudent.java
 *
 * Created on 13 de Dezembro de 2002, 16:04
 */

package DataBeans;

import Util.TipoCurso;

/**
 *
 * @author  tfc130
 */

public class InfoStudent {
	protected Integer _number;
	protected Integer _state=new Integer(1);
	private InfoPerson _infoPerson;
	protected TipoCurso _degreeType;
	
	public InfoStudent() {}

	public InfoStudent(Integer numero,Integer estado, InfoPerson pessoa, TipoCurso degreeType) {
	  setNumber(numero);
	  setState(estado);
	  setInfoPerson(pessoa);
	  setDegreeType(degreeType);
	}

	public InfoPerson getInfoPerson() {
	  return _infoPerson;
	}

	public void setInfoPerson(InfoPerson pessoa) {
		_infoPerson = pessoa;
	  }

	 public Integer getNumber() {
	  return _number;
	 }

	 public void setNumber(Integer numero) {
	  _number = numero;
	 }

	 public Integer getState() {
		 return _state;
	 }

	  public void setState(Integer estado) {
		 _state = estado;
	 }

	public TipoCurso getDegreeType() {
	 return _degreeType;
	}

	public void setDegreeType(TipoCurso degreeType) {
	 _degreeType = degreeType;
	}

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof InfoStudent) {
      InfoStudent infoAluno = (InfoStudent)obj;
      resultado = getNumber().equals(infoAluno.getNumber());
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[InfoStudent";
    result += ", numero=" + _number;
    result += ", degreeType=" + _degreeType;
    result += ", estado=" + _state;
    if (_infoPerson != null)
    	result += ", pessoa" + _infoPerson.toString();
    result += "]";
    return result;
  }
  
}