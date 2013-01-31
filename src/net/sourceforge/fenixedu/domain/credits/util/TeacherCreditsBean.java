package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherCredits;
import net.sourceforge.fenixedu.domain.credits.AnnualTeachingCredits;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class TeacherCreditsBean implements Serializable {
	private Teacher teacher;
	private Set<AnnualTeachingCreditsBean> annualTeachingCredits;
	private Set<CreditLineDTO> pastTeachingCredits;
	private boolean hasAnyYearWithCreditsLimitation = false;
	private boolean hasAnyYearWithCorrections = false;
	private boolean canSeeCreditsReduction = false;

	public TeacherCreditsBean(Teacher teacher) {
		this.teacher = teacher;
	}

	public TeacherCreditsBean() {
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Set<CreditLineDTO> getPastTeachingCredits() {
		return pastTeachingCredits;
	}

	public Set<AnnualTeachingCreditsBean> getAnnualTeachingCredits() {
		return annualTeachingCredits;
	}

	public boolean getHasAnyYearWithCreditsLimitation() {
		return hasAnyYearWithCreditsLimitation;
	}

	public void setHasAnyYearWithCreditsLimitation(boolean hasAnyYearWithCreditsLimitation) {
		this.hasAnyYearWithCreditsLimitation = hasAnyYearWithCreditsLimitation;
	}

	public boolean isHasAnyYearWithCorrections() {
		return hasAnyYearWithCorrections;
	}

	public void setHasAnyYearWithCorrections(boolean hasAnyYearWithCorrections) {
		this.hasAnyYearWithCorrections = hasAnyYearWithCorrections;
	}

	public void prepareAnnualTeachingCredits(RoleType roleType) {
		annualTeachingCredits = new TreeSet<AnnualTeachingCreditsBean>(new Comparator<AnnualTeachingCreditsBean>() {

			@Override
			public int compare(AnnualTeachingCreditsBean annualTeachingCreditsBean1,
					AnnualTeachingCreditsBean annualTeachingCreditsBean2) {
				return annualTeachingCreditsBean1.getExecutionYear().compareTo(annualTeachingCreditsBean2.getExecutionYear());
			}
		});
		boolean hasCurrentYear = false;
		ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
		for (AnnualTeachingCredits annualTeachingCredits : teacher.getAnnualTeachingCredits()) {
			AnnualTeachingCreditsBean annualTeachingCreditsBean = new AnnualTeachingCreditsBean(annualTeachingCredits, roleType);
			this.annualTeachingCredits.add(annualTeachingCreditsBean);
			if (annualTeachingCredits.getAnnualCreditsState().getExecutionYear().equals(currentExecutionYear)) {
				hasCurrentYear = true;
			}
			if (annualTeachingCredits.getHasAnyLimitation()) {
				hasAnyYearWithCreditsLimitation = true;
			}
			if (annualTeachingCreditsBean.getCorrectionInYears().size() > 0) {
				hasAnyYearWithCorrections = true;
			}
		}
		if (!hasCurrentYear && isTeacherActiveForYear(currentExecutionYear)) {
			this.annualTeachingCredits.add(new AnnualTeachingCreditsBean(currentExecutionYear, teacher, roleType));
		}

		if (roleType.equals(RoleType.SCIENTIFIC_COUNCIL) || roleType.equals(RoleType.DEPARTMENT_MEMBER)) {
			setCanSeeCreditsReduction(true);
		}
	}

	private boolean isTeacherActiveForYear(ExecutionYear currentExecutionYear) {
		for (ExecutionSemester executionSemester : currentExecutionYear.getExecutionPeriods()) {
			if (teacher.isActiveForSemester(executionSemester) || teacher.getTeacherAuthorization(executionSemester) != null) {
				return true;
			}
		}
		return false;
	}

	public void preparePastTeachingCredits() {
		pastTeachingCredits = new TreeSet<CreditLineDTO>(new Comparator<CreditLineDTO>() {

			@Override
			public int compare(CreditLineDTO creditLineDTO1, CreditLineDTO creditLineDTO2) {
				return creditLineDTO1.getExecutionPeriod().compareTo(creditLineDTO2.getExecutionPeriod());
			}
		});
		for (TeacherCredits teacherCredits : teacher.getTeacherCredits()) {
			CreditLineDTO creditLineDTO =
					new CreditLineDTO(teacherCredits.getTeacherCreditsState().getExecutionSemester(), teacherCredits);
			pastTeachingCredits.add(creditLineDTO);
			if (creditLineDTO.getCorrectionInYears().size() > 0) {
				hasAnyYearWithCorrections = true;
			}
		}
	}

	public boolean getCanSeeCreditsReduction() {
		return canSeeCreditsReduction;
	}

	public void setCanSeeCreditsReduction(boolean canSeeCreditsReduction) {
		this.canSeeCreditsReduction = canSeeCreditsReduction;
	}

}
