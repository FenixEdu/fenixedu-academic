package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
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

    @Override
    public double getEctsCredits() {
	return hasDismissal() ? getDismissal().getEctsCredits().doubleValue() : 0d;
    }
    
    @Override
    public boolean isDismissalEntry() {
        return true;
    }

    @Override
    public double getWeigth() {
	throw new RuntimeException();
    }

    @Override
    public Double getWeigthTimesClassification() {
	return null;
    }

}
