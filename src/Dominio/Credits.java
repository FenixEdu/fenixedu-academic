package Dominio;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Alexandra Alves
 */
public class Credits extends DomainObject implements ICredits {

	private IExecutionPeriod executionPeriod;
	private Integer keyExecutionPeriod;

	private ITeacher teacher;
	private Integer keyTeacher;

	private Integer tfcStudentsNumber;
	private Double credits;
	private Double additionalCredits;
	private String additionalCreditsJustification;
	/**
	 * @return
	 */
	public Double getAdditionalCredits() {
		return this.additionalCredits;
	}

	/**
	 * @param additionalCredits
	 */
	public void setAdditionalCredits(Double additionalCredits) {
		this.additionalCredits = additionalCredits;
	}

	/**
	 * @return
	 */
	public String getAdditionalCreditsJustification() {
		return this.additionalCreditsJustification;
	}

	/**
	 * @param additionalCreditsJustification
	 */
	public void setAdditionalCreditsJustification(String additionalCreditsJustification) {
		this.additionalCreditsJustification = additionalCreditsJustification;
	}

	public Credits() {
	}

	public Double getCredits() {
		return credits;
	}

	public IExecutionPeriod getExecutionPeriod() {
		return executionPeriod;
	}

	public Integer getKeyExecutionPeriod() {
		return keyExecutionPeriod;
	}

	public Integer getKeyTeacher() {
		return keyTeacher;
	}

	public ITeacher getTeacher() {
		return teacher;
	}

	public Integer getTfcStudentsNumber() {
		return tfcStudentsNumber;
	}

	public void setCredits(Double double1) {
		credits = double1;
	}

	public void setExecutionPeriod(IExecutionPeriod period) {
		executionPeriod = period;
	}

	public void setKeyExecutionPeriod(Integer integer) {
		keyExecutionPeriod = integer;
	}

	public void setKeyTeacher(Integer integer) {
		keyTeacher = integer;
	}

	public void setTeacher(ITeacher teacher) {
		this.teacher = teacher;
	}

	public void setTfcStudentsNumber(Integer integer) {
		tfcStudentsNumber = integer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof ICredits) {
			ICredits credits = (ICredits) arg0;
			result =
				this.getExecutionPeriod().equals(credits.getExecutionPeriod())
					&& this.getTeacher().equals(credits.getTeacher());
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see Dominio.IDomainObject#getUniqueProperties()
	 */
	public List getUniqueProperties() {
		List list = new ArrayList();
		list.add("executionPeriod.semester");
		list.add("executionPeriod.name");
		list.add("executionPeriod.executionYear.year");
		list.add("teacher.teacherNumber");
		list.add("teacher.person.username");
		return list;
	}

}
