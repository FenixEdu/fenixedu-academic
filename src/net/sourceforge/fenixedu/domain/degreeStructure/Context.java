package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Context extends Context_Base {

    protected Context() {
        super();
    }

    public Context(ICourseGroup courseGroup, IDegreeModule degreeModule,
            ICurricularSemester curricularSemester, IExecutionPeriod beginExecutionPeriod,
            IExecutionPeriod endExecutionPeriod) {
        super();
        if (courseGroup == null || degreeModule == null || beginExecutionPeriod == null) {
            throw new DomainException("error.incorrectContextValues");
        }
        setCourseGroup(courseGroup);
        setDegreeModule(degreeModule);
        setCurricularSemester(curricularSemester);
        setBeginExecutionPeriod(beginExecutionPeriod);
        setEndExecutionPeriod(endExecutionPeriod);
    }
    
    public void edit(ICourseGroup courseGroup, IDegreeModule degreeModule, ICurricularSemester curricularSemester) {
        setCourseGroup(courseGroup);
        setDegreeModule(degreeModule);
        setCurricularSemester(curricularSemester);
    }

    public void delete() {
        setCurricularSemester(null);
        setDegreeModule(null);
        setCourseGroup(null);
        setBeginExecutionPeriod(null);
        setEndExecutionPeriod(null);
        super.deleteDomainObject();
    }
}
