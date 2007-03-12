package net.sourceforge.fenixedu.domain.enrolment;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public interface IDegreeModuleToEvaluate {

    public CurriculumGroup getCurriculumGroup();

    public Context getContext();

    public DegreeModule getDegreeModule();
    
    public ExecutionPeriod getExecutionPeriod();

    public boolean isLeaf();

    public boolean isOptional();

    public boolean isEnroled();

    public boolean canCollectRules();
    
    public String getName();
    
    public String getYearFullLabel();
    
    public boolean isOptionalCurricularCourse();
    
    public String getKey();
    
    public Double getEctsCredits();

    public Double getEctsCredits(final ExecutionPeriod executionPeriod);

    public double getAccumulatedEctsCredits(final ExecutionPeriod executionPeriod);
    
    public List<CurricularRule> getCurricularRulesFromDegreeModule(final ExecutionPeriod executionPeriod);

    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(final ExecutionPeriod executionPeriod);
    
}
