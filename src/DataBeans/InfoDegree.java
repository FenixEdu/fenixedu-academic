/*
 * InfoDegree.java
 *
 * Created on 25 de Novembro de 2002, 1:07
 */

package DataBeans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author  tfc130
 */
public class InfoDegree implements Serializable {
	protected String sigla;
	protected String nome;
	// FIXME : this should be a util!!! Why istn't it?
	protected String degreeType;
	private List infoDegreeCurricularPlans;

	public InfoDegree() {
	}

	public InfoDegree(String sigla, String nome) {
		setSigla(sigla);
		setNome(nome);
	}

	public InfoDegree(String sigla, String nome, String degreeType) {
		setSigla(sigla);
		setNome(nome);
		setDegreeType(degreeType);
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
		String result = "[INFOLICENCIATURA";
		result += ", sigla=" + this.sigla;
		result += ", nome=" + this.nome;
		result += ", degreeType=" + this.degreeType;
		result += "]";
		return result;
	}

	/**
	 * @return String
	 */
	public String getDegreeType() {
		return degreeType;
	}

	/**
	 * Sets the degreeType.
	 * @param degreeType The degreeType to set
	 */
	public void setDegreeType(String degreeType) {
		this.degreeType = degreeType;
	}

	/**
	 * @return List
	 */
	public List getInfoDegreeCurricularPlans() {
		return infoDegreeCurricularPlans;
	}

	/**
	 * Sets the infoDegreeCurricularPlans.
	 * @param infoDegreeCurricularPlans The infoDegreeCurricularPlans to set
	 */
	public void setInfoDegreeCurricularPlans(List infoDegreeCurricularPlans) {
		this.infoDegreeCurricularPlans = infoDegreeCurricularPlans;
	}

}
