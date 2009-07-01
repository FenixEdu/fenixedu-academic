package net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;

public class CurriculumLineLocationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DomainReference<CurriculumLine> curriculumLine;

    private DomainReference<CurriculumGroup> curriculumGroup;

    private boolean withRules = true;

    public CurriculumLineLocationBean() {

    }

    public CurriculumLineLocationBean(final CurriculumLine curriculumLine, final CurriculumGroup curriculumGroup,
	    final boolean withRules) {
	setCurriculumLine(curriculumLine);
	setCurriculumGroup(curriculumGroup);
	withRules(withRules);
    }

    public CurriculumLine getCurriculumLine() {
	return (this.curriculumLine != null) ? this.curriculumLine.getObject() : null;
    }

    public void setCurriculumLine(CurriculumLine curriculumLine) {
	this.curriculumLine = (curriculumLine != null) ? new DomainReference<CurriculumLine>(curriculumLine) : null;
    }

    public CurriculumGroup getCurriculumGroup() {
	return (this.curriculumGroup != null) ? this.curriculumGroup.getObject() : null;
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(curriculumGroup) : null;
    }

    public static CurriculumLineLocationBean buildFrom(final CurriculumLine curriculumLine, final boolean withRules) {
	return new CurriculumLineLocationBean(curriculumLine, curriculumLine.getCurriculumGroup(), withRules);
    }

    public Student getStudent() {
	return getCurriculumLine().getStudent();
    }

    public boolean isWithRules() {
	return withRules;
    }

    public void withRules(boolean value) {
	this.withRules = value;
    }
}
