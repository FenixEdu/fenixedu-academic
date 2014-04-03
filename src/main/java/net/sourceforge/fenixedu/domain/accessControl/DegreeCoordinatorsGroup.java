package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.fenixedu.bennu.core.domain.Bennu;

public class DegreeCoordinatorsGroup extends Group {

    private static final long serialVersionUID = -7559780015187749338L;

    @Override
    public String getPresentationNameBundle() {
        return "resources.SiteResources";
    }

    @Override
    public String getPresentationNameKey() {
        return "label." + getClass().getName();
    }

    @Override
    public Set<Person> getElements() {
        final Set<Person> elements = super.buildSet();
        final Collection<ExecutionYear> executionYears = Bennu.getInstance().getExecutionYearsSet();
        for (final ExecutionYear executionYear : executionYears) {
            if (executionYear.isCurrent()) {
                for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
                    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                    final Degree degree = degreeCurricularPlan.getDegree();
                    if (degree.getDegreeType() == DegreeType.DEGREE) {
                        for (final Coordinator coordinator : executionDegree.getCoordinatorsList()) {
                            final Person person = coordinator.getPerson();
                            elements.add(person);
                        }
                    }
                }
                break;
            }
        }
        return elements;
    }

    @Override
    public boolean isMember(Person person) {
        return person != null
                && person.hasTeacher()
                && person.isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegreeCoordinatorFor(ExecutionYear
                        .readCurrentExecutionYear());
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[0];
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DegreeCoordinatorsGroup;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public PersistentCoordinatorGroup convert() {
        return PersistentCoordinatorGroup.getInstance(DegreeType.DEGREE);
    }
}