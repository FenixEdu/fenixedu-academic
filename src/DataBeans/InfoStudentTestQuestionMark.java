/*
 * Created on 28/Ago/2003
 *
 */
package DataBeans;

import Dominio.IStudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestionMark extends InfoObject {
	private String studentName;

	private Integer studentNumber;

	private Integer studentIdInternal;

	private Double testQuestionMark;

	public InfoStudentTestQuestionMark() {
	}

	public Integer getStudentIdInternal() {
		return studentIdInternal;
	}

	public String getStudentName() {
		return studentName;
	}

	public Integer getStudentNumber() {
		return studentNumber;
	}

	public Double getTestQuestionMark() {
		return testQuestionMark;
	}

	public void setStudentIdInternal(Integer integer) {
		studentIdInternal = integer;
	}

	public void setStudentName(String string) {
		studentName = string;
	}

	public void setStudentNumber(Integer integer) {
		studentNumber = integer;
	}

	public void setTestQuestionMark(Double double1) {
		testQuestionMark = double1;
	}

	public boolean equals(Object o) {
		InfoStudentTestQuestionMark infoStudentTestQuestionMark = (InfoStudentTestQuestionMark) o;
		return (getTestQuestionMark().equals(infoStudentTestQuestionMark
				.getTestQuestionMark()))
				&& (getStudentIdInternal().equals(infoStudentTestQuestionMark
						.getStudentIdInternal()))
				&& (getStudentName().equals(infoStudentTestQuestionMark
						.getStudentName()))
				&& (getStudentNumber().equals(infoStudentTestQuestionMark
						.getStudentNumber()));
	}

	public void copyFromDomain(IStudentTestQuestion studentTestQuestion) {
		super.copyFromDomain(studentTestQuestion);
		if (studentTestQuestion != null) {
			if (studentTestQuestion.getStudent() != null) {
				setStudentIdInternal(studentTestQuestion.getStudent()
						.getIdInternal());
				setStudentNumber(studentTestQuestion.getStudent().getNumber());
				if (studentTestQuestion.getStudent().getPerson() != null)
					setStudentName(studentTestQuestion.getStudent().getPerson()
							.getNome());
			}
			setTestQuestionMark(studentTestQuestion.getTestQuestionMark());
		}
	}

	public static InfoStudentTestQuestionMark newInfoFromDomain(
			IStudentTestQuestion studentTestQuestion) {
		InfoStudentTestQuestionMark infoStudentTestQuestionMark = null;
		if (studentTestQuestion != null) {
			infoStudentTestQuestionMark = new InfoStudentTestQuestionMark();
			infoStudentTestQuestionMark.copyFromDomain(studentTestQuestion);
		}
		return infoStudentTestQuestionMark;
	}

}