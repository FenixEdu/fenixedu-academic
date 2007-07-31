package net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;

public class CurriculumLineLocationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DomainReference<CurriculumLine> curriculumLine;

    private DomainReference<CurriculumGroup> curriculumGroup;

    public CurriculumLineLocationBean() {

    }

    public CurriculumLineLocationBean(final CurriculumLine curriculumLine, final CurriculumGroup curriculumGroup) {
	setCurriculumLine(curriculumLine);
	setCurriculumGroup(curriculumGroup);
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

    public static CurriculumLineLocationBean buildFrom(final CurriculumLine curriculumLine) {
	return new CurriculumLineLocationBean(curriculumLine, curriculumLine.getCurriculumGroup());
    }

}
