package net.sourceforge.fenixedu.dataTransferObject.academicAdministration;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class DegreeByExecutionYearBean implements Serializable, Comparable<DegreeByExecutionYearBean> {

    private Degree degree;
    private DegreeType degreeType;
    private ExecutionYear executionYear;
    private Set<DegreeType> administratedDegreeTypes;
    private Set<Degree> administratedDegrees;

    public DegreeByExecutionYearBean() {
    }

    public DegreeByExecutionYearBean(final Degree degree, final ExecutionYear executionYear) {
        setDegree(degree);
        setDegreeType(degree.getDegreeType());
        setExecutionYear(executionYear);
    }

    public DegreeByExecutionYearBean(Set<DegreeType> administratedDegreeTypes, Set<Degree> administratedDegrees) {
        this.administratedDegreeTypes = administratedDegreeTypes;
        this.administratedDegrees = administratedDegrees;
    }

    public Set<DegreeType> getAdministratedDegreeTypes() {
        return administratedDegreeTypes;
    }

    public SortedSet<Degree> getAdministratedDegrees() {
        final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

        for (final Degree degree : administratedDegrees) {
            if (executionYear != null && degree.getExecutionDegreesForExecutionYear(executionYear).isEmpty()) {
                continue;
            }
            if (degreeType != null && !degreeType.equals(degree.getDegreeType())) {
                continue;
            }
            result.add(degree);
        }

        return result;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public String getDegreeName() {
        return getDegree().getNameFor(getExecutionYear().getAcademicInterval()).getContent();
    }

    public String getDegreePresentationName() {
        return getDegree().getPresentationName(getExecutionYear());
    }

    public String getKey() {
        return getDegree().getOID() + ":" + getExecutionYear().getOID();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DegreeByExecutionYearBean) ? equals((DegreeByExecutionYearBean) obj) : false;
    }

    public boolean equals(DegreeByExecutionYearBean bean) {
        return getDegree() == bean.getDegree();
    }

    @Override
    public int hashCode() {
        Degree degree = getDegree();
        if (degree != null) {
            return getDegree().hashCode();
        } else {
            return 0;
        }
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlans() {
        return getDegree().getDegreeCurricularPlans();
    }

    @Override
    public int compareTo(final DegreeByExecutionYearBean other) {
        return (other == null) ? 1 : Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID.compare(getDegree(), other.getDegree());
    }
}
