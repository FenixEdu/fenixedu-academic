/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author jmota
 * @author Fernanda Quitério
 * 
 */
public class InfoSiteProgram implements ISiteComponent {
	private List infoCurriculums;
	private String program;
	private String programEn;

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
		String result = "[INFOSITEPROGRAM";
		result += " program =" + getProgram();
		result += ", programEn =" + getProgramEn();
		result += "]";
		return result;
	}

	public boolean equals(Object objectToCompare) {
		boolean result = false;

		if (objectToCompare instanceof InfoSiteProgram
			&& (((((InfoSiteProgram) objectToCompare).getProgram() != null
				&& this.getProgram() != null
				&& ((InfoSiteProgram) objectToCompare).getProgram().equals(this.getProgram()))
				|| ((InfoSiteProgram) objectToCompare).getProgram() == null
				&& this.getProgram() == null))
				
			&& (((((InfoSiteProgram) objectToCompare).getProgramEn() != null
				&& this.getProgramEn() != null
				&& ((InfoSiteProgram) objectToCompare).getProgramEn().equals(this.getProgramEn()))
				|| ((InfoSiteProgram) objectToCompare).getProgramEn() == null
				&& this.getProgramEn() == null))) {
			result = true;
		}
		return result;
	}

	/**
	 * @return
	 */
	public String getProgram() {
		return program;
	}

	/**
	 * @param string
	 */
	public void setProgram(String string) {
		program = string;
	}

	/**
	 * @return
	 */
	public String getProgramEn() {
		return programEn;
	}

	/**
	 * @param string
	 */
	public void setProgramEn(String string) {
		programEn = string;
	}

}
