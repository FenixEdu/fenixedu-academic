/*
 * InfoDegree.java
 * 
 * Created on 25 de Novembro de 2002, 1:07
 */

package DataBeans;

import java.util.List;

import Util.TipoCurso;

/**
 * @author tfc130
 */
public class InfoDegree extends InfoObject implements Comparable {
	protected String sigla;
	protected String nome;
	protected TipoCurso tipoCurso;

	//FIXME : Esta variavel e para sair
	protected String degreeTypeString;

	private List infoDegreeCurricularPlans;
	private List infoDegreeInfos; //added by Tânia Pousão
	private InfoCampus infoCampus; //added by Tânia Pousão

	public InfoDegree() {
	}

	public InfoDegree(String sigla, String nome) {
		setSigla(sigla);
		setNome(nome);
	}

	/**
	 * @deprecated
	 */
	public InfoDegree(String sigla, String nome, String degreeType) {
		setSigla(sigla);
		setNome(nome);
		setDegreeTypeString(degreeType);
	}

	public InfoDegree(String sigla, String nome, TipoCurso degreeType) {
		setSigla(sigla);
		setNome(nome);
		setTipoCurso(degreeType);
	}

	public String getSigla() {
		return this.sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoDegree) {
			InfoDegree iL = (InfoDegree) obj;
			resultado = (getSigla().equals(iL.getSigla()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[INFOCURSO";
		result += ", sigla=" + this.sigla;
		result += ", nome=" + this.nome;
		result += ", tipoCurso=" + this.tipoCurso;
		result += "]";
		return result;
	}

	/**
	 * @return TipoCurso
	 */
	public TipoCurso getTipoCurso() {
		return tipoCurso;
	}

	/**
	 * Sets the tipoCurso.
	 * 
	 * @param degreeType
	 *          The degreeType to set
	 */
	public void setTipoCurso(TipoCurso tipoCurso) {
		this.tipoCurso = tipoCurso;
	}

	/**
	 * @return List
	 */
	public List getInfoDegreeCurricularPlans() {
		return infoDegreeCurricularPlans;
	}

	/**
	 * Sets the infoDegreeCurricularPlans.
	 * 
	 * @param infoDegreeCurricularPlans
	 *          The infoDegreeCurricularPlans to set
	 */
	public void setInfoDegreeCurricularPlans(List infoDegreeCurricularPlans) {
		this.infoDegreeCurricularPlans = infoDegreeCurricularPlans;
	}

	/**
	 * @return String
	 * @deprecated
	 */
	public String getDegreeTypeString() {
		return degreeTypeString;
	}

	/**
	 * Sets the degreeTypeString.
	 * 
	 * @param degreeTypeString
	 *          The degreeTypeString to set
	 * @deprecated
	 */
	public void setDegreeTypeString(String degreeTypeString) {
		this.degreeTypeString = degreeTypeString;
	}

	//alphabetic order
	public int compareTo(Object arg0) {

		InfoDegree degree = (InfoDegree) arg0;
		return this.getNome().compareTo(degree.getNome());
	}

	public List getInfoDegreeInfos() {
		return infoDegreeInfos;
	}

	public void setInfoDegreeInfos(List infoDegreeInfos) {
		this.infoDegreeInfos = infoDegreeInfos;
	}

	public InfoCampus getInfoCampus() {
		return infoCampus;
	}

	public void setInfoCampus(InfoCampus campus) {
		this.infoCampus = campus;
	}

}
