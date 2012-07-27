package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherCredits;
import net.sourceforge.fenixedu.domain.credits.AnnualTeachingCredits;

public class TeacherCreditsBean implements Serializable {
    private Teacher teacher;
    private Set<AnnualTeachingCreditsBean> annualTeachingCredits;
    private Set<CreditLineDTO> pastTeachingCredits;

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

    public void prepareAnnualTeachingCredits() {
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
	    this.annualTeachingCredits.add(new AnnualTeachingCreditsBean(annualTeachingCredits));
	    if (annualTeachingCredits.getAnnualCreditsState().getExecutionYear().equals(currentExecutionYear)) {
		hasCurrentYear = true;
	    }
	}
	if (!hasCurrentYear) {
	    this.annualTeachingCredits.add(new AnnualTeachingCreditsBean(currentExecutionYear, teacher));
	}
    }

    public void preparePastTeachingCredits() {
	pastTeachingCredits = new TreeSet<CreditLineDTO>(new Comparator<CreditLineDTO>() {

	    @Override
	    public int compare(CreditLineDTO creditLineDTO1, CreditLineDTO creditLineDTO2) {
		return creditLineDTO1.getExecutionPeriod().compareTo(creditLineDTO2.getExecutionPeriod());
	    }
	});
	for (TeacherCredits teacherCredits : teacher.getTeacherCredits()) {
	    pastTeachingCredits.add(new CreditLineDTO(teacherCredits.getTeacherCreditsState().getExecutionSemester(),
		    teacherCredits));
	}
    }

}
