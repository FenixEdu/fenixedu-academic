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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 13, 2006,4:59:49 PM
 * 
 */
public class MasterDegreeCoordinatorsGroup extends Group {
    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();
	final Collection<ExecutionYear> executionYears = RootDomainObject.getInstance()
		.getExecutionYears();
	for (final ExecutionYear executionYear : executionYears) {
	    if (executionYear.getState().equals(PeriodState.CURRENT)) {
		for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
		    final DegreeCurricularPlan degreeCurricularPlan = executionDegree
			    .getDegreeCurricularPlan();
		    final Degree degree = degreeCurricularPlan.getDegree();
		    if (degree.getTipoCurso() == DegreeType.MASTER_DEGREE) {
			for (final Coordinator coordinator : executionDegree.getCoordinatorsList()) {
			    final Teacher teacher = coordinator.getTeacher();
			    final Person person = teacher.getPerson();
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
		&& person.getTeacher().isMasterDegreeOrBolonhaMasterDegreeCoordinatorFor(
			ExecutionYear.readCurrentExecutionYear());
    }
}
