/*
 * Created on 05/Nov/2003
 *  
 */
package DataBeans.person;

import DataBeans.InfoObject;
import DataBeans.InfoPerson;

/**
 * @author Barbosa
 * @author Pica
 */

public class InfoQualification extends InfoObject {

	private Integer year;
	private String mark;
	private String school;
	private String title;
	private InfoPerson personInfo;

	public InfoQualification() {
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && obj instanceof InfoQualification) {
			result =
				getQualificationMark().equals(
					((InfoQualification) obj).getQualificationMark())
					&& getQualificationSchool().equals(
						((InfoQualification) obj).getQualificationSchool())
					&& getQualificationTitle().equals(
						((InfoQualification) obj).getQualificationTitle())
					&& getQualificationYear().equals(
						((InfoQualification) obj).getQualificationYear())
					&& getPersonInfo().equals(
						((InfoQualification) obj).getPersonInfo());
		}
		return result;
	}

	/**
	 * @return InfoPerson
	 */
	public InfoPerson getPersonInfo() {
		return personInfo;
	}

	/**
	 * @return Integer
	 */
	public Integer getQualificationYear() {
		return year;
	}

	/**
	 * @return String
	 */
	public String getQualificationMark() {
		return mark;
	}

	/**
	 * @return String
	 */
	public String getQualificationSchool() {
		return school;
	}

	/**
	 * @return String
	 */
	public String getQualificationTitle() {
		return title;
	}

	/**
	 * Sets the personInfo.
	 * 
	 * @param infoPerson
	 *                    The personInfo to set
	 */
	public void setPersonInfo(InfoPerson infoPerson) {
		this.personInfo = infoPerson;
	}

	/**
	 * Sets the mark of the qualification
	 * 
	 * @param mark.
	 */
	public void setQualificationMark(String mark) {
		this.mark = mark;
	}

	/**
	 * Sets the qualification year
	 * 
	 * @param year
	 *                    The Year to set
	 */
	public void setQualificationYear(Integer year) {
		this.year = year;
	}

	/**
	 * Sets the school of qualification
	 * 
	 * @param school;
	 *                    The school to set
	 */
	public void setQualificationSchool(String school) {
		this.school = school;
	}

	/**
	 * Sets the title of qualification
	 * 
	 * @param title;
	 *                    The title to set
	 */
	public void setQualificationTitle(String title) {
		this.title = title;
	}

}