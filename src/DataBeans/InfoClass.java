/*
 * InfoClass.java
 *
 * Created on 31 de Outubro de 2002, 12:27
 */

package DataBeans;

import java.io.Serializable;

/**
 *
 * @author  tfc130
 */

public class InfoClass implements Serializable {
	protected String _nome;
	protected Integer _semestre;
	protected Integer _anoCurricular;
	protected InfoDegree _infoLicenciatura;

	private InfoExecutionDegree infoExecutionDegree;
	private InfoExecutionPeriod infoExecutionPeriod;

	public InfoClass() {
	}

	/**
	 * 
	 * @param nome
	 * @param semestre
	 * @param anoCurricular
	 * @param infoLicenciatura
	 * @deprecated
	 */
	public InfoClass(
		String nome,
		Integer semestre,
		Integer anoCurricular,
		InfoDegree infoLicenciatura) {
		setNome(nome);
		setSemestre(semestre);
		setAnoCurricular(anoCurricular);
		setInfoLicenciatura(infoLicenciatura);
	}

	public String getNome() {
		return _nome;
	}

	public void setNome(String nome) {
		_nome = nome;
	}

	public Integer getSemestre() {
		return _semestre;
	}

	public void setSemestre(Integer semestre) {
		_semestre = semestre;
	}

	public Integer getAnoCurricular() {
		return _anoCurricular;
	}

	public void setAnoCurricular(Integer anoCurricular) {
		_anoCurricular = anoCurricular;
	}

	public InfoDegree getInfoLicenciatura() {
		return _infoLicenciatura;
	}

	public void setInfoLicenciatura(InfoDegree infoLicenciatura) {
		_infoLicenciatura = infoLicenciatura;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoClass) {
			InfoClass infoTurma = (InfoClass) obj;
			resultado =
				getNome().equals(infoTurma.getNome())
					&& getInfoLicenciatura().equals(
						infoTurma.getInfoLicenciatura())
					&& getSemestre().equals(infoTurma.getSemestre());
		}
		return resultado;
	}

	public String toString() {
		String result = "[INFOTURMA";
		result += ", nome=" + _nome;
		result += ", semestre=" + _semestre;
		result += ", infoLicenciatura=" + _infoLicenciatura;
		result += "]";
		return result;
	}

	/**
	 * Returns the infoExecutionDegree.
	 * @return InfoExecutionDegree
	 */
	public InfoExecutionDegree getInfoExecutionDegree() {
		return infoExecutionDegree;
	}

	/**
	 * Sets the infoExecutionDegree.
	 * @param infoExecutionDegree The infoExecutionDegree to set
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
		this.infoExecutionDegree = infoExecutionDegree;
	}

	/**
	 * Returns the infoExecutionPeriod.
	 * @return InfoExecutionPeriod
	 */
	public InfoExecutionPeriod getInfoExecutionPeriod() {
		return infoExecutionPeriod;
	}

	/**
	 * Sets the infoExecutionPeriod.
	 * @param infoExecutionPeriod The infoExecutionPeriod to set
	 */
	public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
		this.infoExecutionPeriod = infoExecutionPeriod;
	}

}
