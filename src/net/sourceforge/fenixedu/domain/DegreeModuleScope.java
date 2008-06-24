package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public abstract class DegreeModuleScope {

    static final protected Comparator<DegreeModuleScope> COMPARATOR_BY_ID = new Comparator<DegreeModuleScope>() {
	public int compare(DegreeModuleScope o1, DegreeModuleScope o2) {
	    return o1.getIdInternal().compareTo(o2.getIdInternal());
	}
    };

    private static final String KEY_SEPARATOR = ":";
    public static final Comparator<DegreeModuleScope> COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME = new ComparatorChain();
    public static final Comparator<DegreeModuleScope> COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME_AND_BRANCH = new ComparatorChain();
    public static final Comparator<DegreeModuleScope> COMPARATOR_BY_NAME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME)
		.addComparator(new BeanComparator("curricularYear"));
	((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME)
		.addComparator(new BeanComparator("curricularSemester"));
	((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME)
		.addComparator(new BeanComparator("curricularCourse.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME)
		.addComparator(DegreeModuleScope.COMPARATOR_BY_ID);

	((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME_AND_BRANCH)
		.addComparator(new BeanComparator("curricularYear"));
	((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME_AND_BRANCH)
		.addComparator(new BeanComparator("curricularSemester"));
	((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME_AND_BRANCH)
		.addComparator(new BeanComparator("curricularCourse.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME_AND_BRANCH)
		.addComparator(new BeanComparator("branch", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME_AND_BRANCH)
		.addComparator(DegreeModuleScope.COMPARATOR_BY_ID);

	((ComparatorChain) COMPARATOR_BY_NAME).addComparator(new BeanComparator("curricularCourse.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_NAME).addComparator(DegreeModuleScope.COMPARATOR_BY_ID);
    }

    public abstract String getClassName();

    public abstract Integer getIdInternal();

    public abstract Integer getCurricularSemester();

    public abstract Integer getCurricularYear();

    public abstract String getBranch();

    public abstract String getAnotation();

    public abstract CurricularCourse getCurricularCourse();

    public abstract boolean isActiveForExecutionPeriod(ExecutionSemester executionSemester);

    public static List<DegreeModuleScope> getDegreeModuleScopes(WrittenEvaluation writtenEvaluation) {
	return getDegreeModuleScopes(writtenEvaluation.getAssociatedCurricularCourseScope(), writtenEvaluation
		.getAssociatedContexts());
    }

    public static List<DegreeModuleScope> getDegreeModuleScopes(CurricularCourse curricularCourse) {
	return getDegreeModuleScopes(curricularCourse.getScopes(), curricularCourse.getParentContexts());
    }

    private static List<DegreeModuleScope> getDegreeModuleScopes(List<CurricularCourseScope> curricularCourseScopes,
	    List<Context> contexts) {
	List<DegreeModuleScope> degreeModuleScopes = new ArrayList<DegreeModuleScope>();
	for (CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
	    degreeModuleScopes.add(curricularCourseScope.getDegreeModuleScopeCurricularCourseScope());
	}
	for (Context context : contexts) {
	    degreeModuleScopes.add(context.getDegreeModuleScopeContext());
	}
	return degreeModuleScopes;
    }

    public boolean isActiveForExecutionYear(ExecutionYear executionYear) {
	for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
	    if (isActiveForExecutionPeriod(executionSemester)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isActive() {
	return isActiveForExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
    }

    public boolean isActive(int year, int semester) {
	return getCurricularYear().intValue() == year && getCurricularSemester().intValue() == semester;
    }

    public boolean isFirstSemester() {
	return (this.getCurricularSemester().intValue() == 1);
    }

    public boolean isSecondSemester() {
	return (this.getCurricularSemester().intValue() == 2);
    }

    public String getKey() {
	return getIdInternal() + KEY_SEPARATOR + getClassName();
    }

    public static String getKey(Integer idInternal, String className) {
	return idInternal + KEY_SEPARATOR + className;
    }

    public static DegreeModuleScope getDegreeModuleScopeByKey(String key) {
	String[] split = key.split(KEY_SEPARATOR);
	if (split.length == 2) {
	    String idInternal = split[0];
	    String className = split[1];
	    try {
		Class clazz = Class.forName(className);
		DomainObject domainObject = RootDomainObject.getInstance().readDomainObjectByOID(clazz,
			Integer.valueOf(idInternal));
		if (domainObject != null && domainObject instanceof CurricularCourseScope) {
		    return ((CurricularCourseScope) domainObject).getDegreeModuleScopeCurricularCourseScope();
		}
		if (domainObject != null && domainObject instanceof Context) {
		    return ((Context) domainObject).getDegreeModuleScopeContext();
		}
	    } catch (ClassNotFoundException e) {
		return null;
	    } catch (NumberFormatException exception) {
		return null;
	    }
	}
	return null;
    }

    public LabelFormatter getDescription() {
	return new LabelFormatter(getCurricularYear().toString()).appendLabel("º ").appendLabel("label.curricular.year",
		LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ").appendLabel(getCurricularSemester().toString())
		.appendLabel("º ").appendLabel("label.semester.short", LabelFormatter.APPLICATION_RESOURCES);

    }
}
