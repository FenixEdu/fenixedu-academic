/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package net.sourceforge.fenixedu.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends ExecutionDegree_Base {

    public static final Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME;
    static {
    	EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME = new ComparatorChain();
    	((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME).addComparator(new BeanComparator("degreeCurricularPlan.degree.tipoCurso"));
    	((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME).addComparator(new BeanComparator("degreeCurricularPlan.degree.nome"));
    }

    public ExecutionDegree() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public boolean isFirstYear() {

        List<ExecutionDegree> executionDegrees = this.getDegreeCurricularPlan().getExecutionDegrees();

        ExecutionDegree firstExecutionDegree = (ExecutionDegree) Collections.min(executionDegrees,
                new BeanComparator("executionYear.year"));

        if (firstExecutionDegree.equals(this)) {
            return true;
        }

        return false;
    }

	public Set<Shift> findAvailableShifts(final CurricularYear curricularYear, final ExecutionPeriod executionPeriod) {
		final Set<Shift> shifts = new HashSet<Shift>();
		for (final CurricularCourse curricularCourse : getDegreeCurricularPlan().getCurricularCourses()) {
			if (curricularCourse.hasScopeForCurricularYear(curricularYear.getYear())) {
				for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
					if (executionCourse.getExecutionPeriod() == executionPeriod) {
						shifts.addAll(executionCourse.getAssociatedShifts());
					}
				}
			}
		}
		return shifts;
	}

	public Set<SchoolClass> findSchoolClassesByExecutionPeriod(final ExecutionPeriod executionPeriod) {
		final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
		for (final SchoolClass schoolClass : getSchoolClasses()) {
			if (schoolClass.getExecutionPeriod() == executionPeriod) {
				schoolClasses.add(schoolClass);
			}
		}
		return schoolClasses;
	}

	public Set<SchoolClass> findSchoolClassesByExecutionPeriodAndCurricularYear(final ExecutionPeriod executionPeriod, final Integer curricularYear) {
		final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
		for (final SchoolClass schoolClass : getSchoolClasses()) {
			if (schoolClass.getExecutionPeriod() == executionPeriod && schoolClass.getAnoCurricular().equals(curricularYear)) {
				schoolClasses.add(schoolClass);
			}
		}
		return schoolClasses;
	}

	public SchoolClass findSchoolClassesByExecutionPeriodAndName(final ExecutionPeriod executionPeriod, final String name) {
		final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
		for (final SchoolClass schoolClass : getSchoolClasses()) {
			if (schoolClass.getExecutionPeriod() == executionPeriod && schoolClass.getNome().equals(name)) {
				return schoolClass;
			}
		}
		return null;
	}
	
}
