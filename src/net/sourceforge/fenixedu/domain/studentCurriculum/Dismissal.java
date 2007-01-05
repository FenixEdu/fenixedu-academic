package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Dismissal extends Dismissal_Base {
    
    protected  Dismissal() {
        super();
    }
    
    protected  Dismissal(Credits credits, CurriculumGroup curriculumGroup) {
        super();
        if(credits == null || curriculumGroup == null) {
            throw new DomainException("error.equivalence.wrong.arguments");
        }
        setCredits(credits);
        setCurriculumGroup(curriculumGroup);
    }
    
    protected  Dismissal(Credits credits, CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        this(credits, curriculumGroup);
        if( curricularCourse == null) {
            throw new DomainException("error.equivalence.wrong.arguments");
        }
        setCurricularCourse(curricularCourse);
    }

    @Override
    public StringBuilder print(String tabs) {
	return null;
    }
    
    @Override
    public boolean isAproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
        if(getCurricularCourse() != null) {
            return getCurricularCourse().isEquivalent(curricularCourse);
        } else {
            return false;
        }
    }
    
}
