/*
 * InfoExecutionDegree.java
 *
 * Created on 24 de Novembro de 2002, 23:05
 */

package DataBeans;

import java.io.Serializable;

/**
 *
 * @author  tfc130
 */
public class InfoExecutionDegree implements Serializable {
	protected String _anoLectivo;
	protected InfoDegree _infoLicenciatura;
	private InfoExecutionYear infoExecutionYear;
	private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

	public InfoExecutionDegree() {
	}
	/**
	 * 
	 * @param anoLectivo
	 * @param infoLicenciatura
	 * @deprecated
	 */
	public InfoExecutionDegree(
		String anoLectivo,
		InfoDegree infoLicenciatura) {
		setAnoLectivo(anoLectivo);
		setInfoLicenciatura(infoLicenciatura);
	}
/**
 * 
 * @param anoLectivo
 * @param infoLicenciatura
 * @param infoExecutionYear
 */
	public InfoExecutionDegree(
			String anoLectivo,
			InfoDegree infoLicenciatura,InfoExecutionYear infoExecutionYear) {
			setAnoLectivo(anoLectivo);
			setInfoLicenciatura(infoLicenciatura);
			setInfoExecutionYear(infoExecutionYear);
		}
	public String getAnoLectivo() {
		return _anoLectivo;
	}

	public void setAnoLectivo(String anoLectivo) {
		_anoLectivo = anoLectivo;
	}

	public InfoDegree getInfoLicenciatura() {
		return _infoLicenciatura;
	}

	public void setInfoLicenciatura(InfoDegree infoLicenciatura) {
		_infoLicenciatura = infoLicenciatura;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoExecutionDegree) {
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) obj;
			result =
				getAnoLectivo().equals(infoExecutionDegree.getAnoLectivo())
					&& getInfoDegreeCurricularPlan().equals(infoExecutionDegree.getInfoDegreeCurricularPlan());
		}
		return result;
	}

	public String toString() {
		String result = "[INFOLICENCIATURAEXECUCAO";
		result += ", anoLectivo=" + _anoLectivo;
		result += ", infoLicenciatura=" + _infoLicenciatura;
		result += "]";
		return result;
	}

	/**
	 * Returns the infoExecutionYear.
	 * @return InfoExecutionYear
	 */
	public InfoExecutionYear getInfoExecutionYear() {
		return infoExecutionYear;
	}

	/**
	 * Sets the infoExecutionYear.
	 * @param infoExecutionYear The infoExecutionYear to set
	 */
	public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
		this.infoExecutionYear = infoExecutionYear;
	}

	/**
	 * Returns the infoDegreeCurricularPlan.
	 * @return InfoDegreeCurricularPlan
	 */
	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
		return infoDegreeCurricularPlan;
	}

	/**
	 * Sets the infoDegreeCurricularPlan.
	 * @param infoDegreeCurricularPlan The infoDegreeCurricularPlan to set
	 */
	public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
	}

}
