package DataBeans;

/**
 *
 * @author  Ricardo Nortadas
 */

import Util.TipoCurso;

public class InfoCourse {
	protected String _sigla;
	protected String _nome;
	protected TipoCurso _degreeType;
	

	public InfoCourse() {}

	public InfoCourse(String sigla,String nome,TipoCurso degreeType) {
	  setSigla(sigla);
	  setName(nome);
	  setDegreeType(degreeType);
	}

	
	 public String getSigla() {
	  return _sigla;
	 }

	 public void setSigla(String sigla) {
	  _sigla = sigla;
	 }

	 public String getName() {
		 return _nome;
	 }

	  public void setName(String name) {
		 _nome = name;
	 }

	public TipoCurso getDegreeType() {
	 return _degreeType;
	}

	public void setDegreeType(TipoCurso degreeType) {
	 _degreeType = degreeType;
	}

  public boolean equals(Object obj) {
	boolean resultado = false;
	if (obj instanceof InfoCourse) {
	  InfoCourse infoCurso = (InfoCourse)obj;
	  resultado = getName().equals(infoCurso.getName());
	}
	return resultado;
  }

  public String toString() {
	String result = "[InfoCourse";
	result += ", sigla=" + _sigla;
	result += ", nome=" + _nome;
	result += ", degreeType=" + _degreeType;
	result += "]";
	return result;
  }

}