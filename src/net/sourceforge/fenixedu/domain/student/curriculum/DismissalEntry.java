package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;

public class DismissalEntry extends CurriculumEntry {

    private final DomainReference<Dismissal> dismissal;

    public DismissalEntry(final Dismissal dismissal) {
	this.dismissal = new DomainReference<Dismissal>(dismissal);
    }
    
    public Dismissal getDismissal() {
	return (this.dismissal != null) ? this.dismissal.getObject() : null;
    }
    
    public boolean hasDismissal() {
	return getDismissal() != null;
    }
    
    public CurricularCourse getCurricularCourse() {
	return hasDismissal() ? getDismissal().getCurricularCourse() : null;
    }
    
    public CurriculumGroup getCurriculumGroup() {
	return hasDismissal() ? getDismissal().getCurriculumGroup() : null;
    }

    public BigDecimal getEctsCreditsForCurriculum() {
	return hasDismissal() ? BigDecimal.valueOf(getDismissal().getEctsCredits()) : BigDecimal.ZERO;
    }
    
    @Override
    public boolean isDismissalEntry() {
        return true;
    }

    public BigDecimal getWeigthForCurriculum() {
	throw new RuntimeException();
    }

    @Override
    public BigDecimal getWeigthTimesGrade() {
	return null;
    }

    @Override
    public Grade getGrade() {
	return null;
    }

}
