package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class AllStudentsByCampus extends Group {

    private DomainReference<Campus> campus;

    public AllStudentsByCampus(Campus campus) {
	this.campus = new DomainReference<Campus>(campus);
    }

    public Campus getCampus() {
	return campus.getObject();
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> people = new HashSet<Person>();
	Campus campus = getCampus();
	for (final ExecutionDegree executionDegree : ExecutionYear.readCurrentExecutionYear().getExecutionDegreesSet()) {
	    if (executionDegree.getCampus() == campus) {
		final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
		for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
		    final Registration registration = studentCurricularPlan.getRegistration();
		    if (registration != null && registration.isActive()) {
			people.add(registration.getPerson());
		    }
		}
	    }
	}
	return people;
    }

    @Override
    public String getName() {
	String name = RenderUtils.getResourceString("GROUP_NAME_RESOURCES", "label.name." + getClass().getSimpleName(),
		new Object[] { getCampus().getName() });
	return name != null ? name : super.getName();
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return null;
    }

}
