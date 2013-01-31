package net.sourceforge.fenixedu.domain.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class SearchStudentsWithEnrolmentsByDepartment implements Serializable {

	private Department departmentDomainReference;
	private Set<Degree> degreeDomainReferences;
	private ExecutionYear executionYearDomainReference;

	public SearchStudentsWithEnrolmentsByDepartment(final Department department) {
		departmentDomainReference = department;
	}

	public Department getDepartment() {
		return departmentDomainReference;
	}

	public List<Degree> getDegrees() {
		final List<Degree> degrees = new ArrayList<Degree>();
		if (degreeDomainReferences != null) {
			for (final Degree degreeDomainReference : degreeDomainReferences) {
				degrees.add(degreeDomainReference);
			}
		}
		Collections.sort(degrees, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
		return degrees;
	}

	public void setDegrees(List<Degree> degrees) {
		if (degrees != null) {
			degreeDomainReferences = new HashSet<Degree>();
			for (final Degree degree : degrees) {
				degreeDomainReferences.add(degree);
			}
		} else {
			degreeDomainReferences = null;
		}
	}

	public ExecutionYear getExecutionYear() {
		return executionYearDomainReference;
	}

	public void setExecutionYear(final ExecutionYear executionYear) {
		executionYearDomainReference = executionYear;
	}

	public Set<StudentCurricularPlan> search() {
		final ExecutionYear executionYear = getExecutionYear();
		final Set<StudentCurricularPlan> studentCurricularPlans = new HashSet<StudentCurricularPlan>();
		if (degreeDomainReferences != null) {
			for (final Degree degreeDomainReference : degreeDomainReferences) {
				final Degree degree = degreeDomainReference;
				for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
					if (degreeCurricularPlan.isActive()) {
						for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan
								.getStudentCurricularPlansSet()) {
							if (studentCurricularPlan.hasEnrolments(executionYear)) {
								studentCurricularPlans.add(studentCurricularPlan);
							}
						}
					}
				}
			}
		}
		return studentCurricularPlans;
	}

	public Set<StudentCurricularPlan> getSearch() {
		return search();
	}
}
