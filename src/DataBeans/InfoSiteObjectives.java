/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 *
 * 
 */
public class InfoSiteObjectives implements ISiteComponent {


	private List infoCurriculums;
	private String generalObjectives;
	private String operacionalObjectives;
	private String generalObjectivesEn;
	private String operacionalObjectivesEn;

	/**
	 * @return
	 */
	public List getInfoCurriculums() {
		return infoCurriculums;
	}

	/**
	 * @param infoCurriculums
	 */
	public void setInfoCurriculums(List infoCurriculums) {
		this.infoCurriculums = infoCurriculums;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFOSITEOBJECTIVES";
		result += " generalObjectives=" + getGeneralObjectives();
		result += ", generalObjectivesEn=" + getGeneralObjectivesEn();
		result += ", operacionalObjectives=" + getOperacionalObjectives();
		result += ", operacionalObjectivesEn =" + getOperacionalObjectivesEn();
		result += "]";
		return result;
	}

	public boolean equals(Object objectToCompare) {
		boolean result = false;
		
		if (objectToCompare instanceof InfoSiteObjectives
			&& (((((InfoSiteObjectives) objectToCompare).getGeneralObjectives() != null
				&& this.getGeneralObjectives() != null
				&& ((InfoSiteObjectives) objectToCompare).getGeneralObjectives().equals(this.getGeneralObjectives()))
				|| ((InfoSiteObjectives) objectToCompare).getGeneralObjectives() == null
				&& this.getGeneralObjectives() == null))
				
			&& (((((InfoSiteObjectives) objectToCompare).getGeneralObjectivesEn() != null
				&& this.getGeneralObjectivesEn() != null
				&& ((InfoSiteObjectives) objectToCompare).getGeneralObjectivesEn().equals(this.getGeneralObjectivesEn()))
				|| ((InfoSiteObjectives) objectToCompare).getGeneralObjectivesEn() == null
				&& this.getGeneralObjectivesEn() == null))
				
			&& (((((InfoSiteObjectives) objectToCompare).getOperacionalObjectives() != null
				&& this.getOperacionalObjectives() != null
				&& ((InfoSiteObjectives) objectToCompare).getOperacionalObjectives().equals(this.getOperacionalObjectives()))
				|| ((InfoSiteObjectives) objectToCompare).getOperacionalObjectives() == null
				&& this.getOperacionalObjectives() == null))
				
			&& (((((InfoSiteObjectives) objectToCompare).getOperacionalObjectivesEn() != null
				&& this.getOperacionalObjectivesEn() != null
				&& ((InfoSiteObjectives) objectToCompare).getOperacionalObjectivesEn().equals(this.getOperacionalObjectivesEn()))
				|| ((InfoSiteObjectives) objectToCompare).getOperacionalObjectivesEn() == null
				&& this.getOperacionalObjectivesEn() == null))) {
			result = true;
		}
		return result;
	}

	/**
	 * @return
	 */
	public String getGeneralObjectives() {
		return generalObjectives;
	}

	/**
	 * @return
	 */
	public String getOperacionalObjectives() {
		return operacionalObjectives;
	}

	/**
	 * @param string
	 */
	public void setGeneralObjectives(String string) {
		generalObjectives = string;
	}

	/**
	 * @param string
	 */
	public void setOperacionalObjectives(String string) {
		operacionalObjectives = string;
	}

	/**
	 * @return
	 */
	public String getGeneralObjectivesEn() {
		return generalObjectivesEn;
	}

	/**
	 * @return
	 */
	public String getOperacionalObjectivesEn() {
		return operacionalObjectivesEn;
	}

	/**
	 * @param string
	 */
	public void setGeneralObjectivesEn(String string) {
		generalObjectivesEn = string;
	}

	/**
	 * @param string
	 */
	public void setOperacionalObjectivesEn(String string) {
		operacionalObjectivesEn = string;
	}

}
