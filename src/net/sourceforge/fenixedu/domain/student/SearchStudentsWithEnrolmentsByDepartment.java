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
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class SearchStudentsWithEnrolmentsByDepartment implements Serializable {

    private DomainReference<Department> departmentDomainReference; 
    private Set<DomainReference<Degree>> degreeDomainReferences;
    private DomainReference<ExecutionYear> executionYearDomainReference;

    public SearchStudentsWithEnrolmentsByDepartment(final Department department) {
        departmentDomainReference = department == null ? null : new DomainReference<Department>(department);
    }

    public Department getDepartment() {
        return departmentDomainReference == null ? null : departmentDomainReference.getObject();
    }

    public List<Degree> getDegrees() {
        final List<Degree> degrees = new ArrayList<Degree>();
        if (degreeDomainReferences != null) {
            for (final DomainReference<Degree> degreeDomainReference : degreeDomainReferences) {
                degrees.add(degreeDomainReference.getObject());
            }
        }
        Collections.sort(degrees, Degree.DEGREE_COMPARATOR_BY_NAME_AND_DEGREE_TYPE);
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        if (degrees != null) {
            degreeDomainReferences = new HashSet<DomainReference<Degree>>();
            for (final Degree degree : degrees) {
                degreeDomainReferences.add(new DomainReference<Degree>(degree));
            }
        } else {
            degreeDomainReferences = null;
        }
    }

    public ExecutionYear getExecutionYear() {
        return executionYearDomainReference == null ? null : executionYearDomainReference.getObject();
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
        executionYearDomainReference = executionYear == null ? null : new DomainReference<ExecutionYear>(executionYear);
    }

    public Set<StudentCurricularPlan> search() {
	final ExecutionYear executionYear = getExecutionYear();
	final Set<StudentCurricularPlan> studentCurricularPlans = new HashSet<StudentCurricularPlan>();
	if (degreeDomainReferences != null) {
	    for (final DomainReference<Degree> degreeDomainReference : degreeDomainReferences) {
		final Degree degree = degreeDomainReference.getObject();
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (degreeCurricularPlan.isActive()) {
			for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
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
