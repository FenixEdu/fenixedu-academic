package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public interface IDegreeModuleToEvaluate {

    public static final Comparator<IDegreeModuleToEvaluate> COMPARATOR_BY_EXECUTION_PERIOD = new Comparator<IDegreeModuleToEvaluate>() {

	@Override
	public int compare(IDegreeModuleToEvaluate o1, IDegreeModuleToEvaluate o2) {
	    return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
	}

    };

    public static final Comparator<IDegreeModuleToEvaluate> COMPARATOR_BY_CONTEXT = new Comparator<IDegreeModuleToEvaluate>() {

	@Override
	public int compare(IDegreeModuleToEvaluate o1, IDegreeModuleToEvaluate o2) {
	    return o1.getContext().compareTo(o2.getContext());
	}

    };

    public CurriculumGroup getCurriculumGroup();

    public Context getContext();

    public DegreeModule getDegreeModule();

    public boolean isFor(final DegreeModule degreeModule);

    public ExecutionSemester getExecutionPeriod();

    public boolean isLeaf();

    public boolean isOptional();

    public boolean isEnroled();

    public boolean isEnroling();

    public boolean isDissertation();

    public boolean canCollectRules();

    public String getName();

    public String getYearFullLabel();

    public boolean isOptionalCurricularCourse();

    public String getKey();

    public Double getEctsCredits();

    public Double getEctsCredits(final ExecutionSemester executionSemester);

    public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester);

    public List<CurricularRule> getCurricularRulesFromDegreeModule(final ExecutionSemester executionSemester);

    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(final ExecutionSemester executionSemester);

    public boolean isAnnualCurricularCourse(final ExecutionYear executionYear);

}
