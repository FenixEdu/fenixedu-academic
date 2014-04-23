/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 13, 2006,4:59:49 PM
 */
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

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jul 13, 2006,4:59:49 PM
 * 
 */
public class MasterDegreeCoordinatorsGroup extends Group {

    private static final long serialVersionUID = 5928255128153111582L;

    @Override
    public Set<Person> getElements() {
        final Set<Person> elements = super.buildSet();
        final Collection<ExecutionYear> executionYears = Bennu.getInstance().getExecutionYearsSet();
        for (final ExecutionYear executionYear : executionYears) {
            if (executionYear.isCurrent()) {
                for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
                    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                    final Degree degree = degreeCurricularPlan.getDegree();
                    if (degree.getDegreeType() == DegreeType.MASTER_DEGREE) {
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
        return person != null && person.hasTeacher()
                && person.isMasterDegreeOrBolonhaMasterDegreeCoordinatorFor(ExecutionYear.readCurrentExecutionYear());
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[0];
    }

    @Override
    public PersistentCoordinatorGroup convert() {
        return PersistentCoordinatorGroup.getInstance(DegreeType.MASTER_DEGREE);
    }
}
