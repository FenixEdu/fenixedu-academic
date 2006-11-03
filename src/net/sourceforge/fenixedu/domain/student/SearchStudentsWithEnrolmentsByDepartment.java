package net.sourceforge.fenixedu.domain.student;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class SearchStudentsWithEnrolmentsByDepartment implements Serializable {

    private DomainReference<Department> departmentDomainReference; 
    private Set<DomainReference<Degree>> degreeDomainReferences;
    private DomainReference<ExecutionPeriod> executionPeriodDomainReference;

    public SearchStudentsWithEnrolmentsByDepartment(final Department department) {
        departmentDomainReference = department == null ? null : new DomainReference<Department>(department);
    }

    public Department getDepartment() {
        return departmentDomainReference == null ? null : departmentDomainReference.getObject();
    }

    public Set<Degree> getDegrees() {
        final Set<Degree> degrees = new HashSet<Degree>();
        if (degreeDomainReferences != null) {
            for (final DomainReference<Degree> degreeDomainReference : degreeDomainReferences) {
                degrees.add(degreeDomainReference.getObject());
            }
        }
        return degrees;
    }

    public void setDegrees(Set<Degree> degrees) {
        if (degrees != null) {
            degreeDomainReferences = new HashSet<DomainReference<Degree>>();
            for (final Degree degree : degrees) {
                degreeDomainReferences.add(new DomainReference<Degree>(degree));
            }
        }
    }

    public ExecutionPeriod getExecutionPeriod() {
        return executionPeriodDomainReference == null ? null : executionPeriodDomainReference.getObject();
    }

    public void setExecutionPeriod(final ExecutionPeriod executionPeriod) {
        executionPeriodDomainReference = executionPeriod == null ? null : new DomainReference<ExecutionPeriod>(executionPeriod);
    }

    public Set<StudentCurricularPlan> search() {
        return null;
    }

}
