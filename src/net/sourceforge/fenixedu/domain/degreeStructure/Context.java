package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Context extends Context_Base {

    protected Context() {
        super();
    }

    public Context(CourseGroup courseGroup, DegreeModule degreeModule,
            CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod,
            ExecutionPeriod endExecutionPeriod) {
        super();
        if (courseGroup == null || degreeModule == null || beginExecutionPeriod == null) {
            throw new DomainException("error.incorrectContextValues");
        }
        setCourseGroup(courseGroup);
        setDegreeModule(degreeModule);
        setCurricularPeriod(curricularPeriod);
        setBeginExecutionPeriod(beginExecutionPeriod);
        setEndExecutionPeriod(endExecutionPeriod);
    }
    
    public void edit(CourseGroup courseGroup, DegreeModule degreeModule, CurricularPeriod curricularPeriod) {
        setCourseGroup(courseGroup);
        setDegreeModule(degreeModule);
        setCurricularPeriod(curricularPeriod);
    }

    public void delete() {
        removeCurricularPeriod();
        setDegreeModule(null);
        setCourseGroup(null);
        setBeginExecutionPeriod(null);
        setEndExecutionPeriod(null);
        super.deleteDomainObject();
    }
}
