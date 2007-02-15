package net.sourceforge.fenixedu.domain.enrolment;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public interface IDegreeModuleToEvaluate {

    public CurriculumGroup getCurriculumGroup();
    public Context getContext();
    public DegreeModule getDegreeModule();

    public boolean isLeaf();
    public boolean isOptional();
    public boolean isEnroled();
    
    public boolean canCollectRules();
    
    public Double getEctsCredits(final ExecutionPeriod executionPeriod);
}
