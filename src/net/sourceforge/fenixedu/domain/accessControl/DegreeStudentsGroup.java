package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class DegreeStudentsGroup extends DegreeGroup {

    /**
         * 
         */
    private static final long serialVersionUID = -2591751862204681731L;

    public DegreeStudentsGroup(Degree object) {
	super(object);
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> elements = super.buildSet();

	for (DegreeCurricularPlan degreeCurricularPlan : getDegree().getActiveDegreeCurricularPlans()) {
	    for (StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan
		    .getStudentCurricularPlans()) {
		if (studentCurricularPlan.isActive()) {
		    elements.add(studentCurricularPlan.getRegistration().getPerson());
		}
	    }
	}

	return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
	if (person != null && person.hasStudent()) {
	    for (final Registration registration : person.getStudent().getRegistrationsSet()) {
		if (registration.isActive()
			&& registration.getActiveStudentCurricularPlan().getDegree() == getDegree()) {
		    return true;
		}
	    }
	}

	return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] {
                new IdOperator(getObject())
        };
    }

    @Override
    public String getName() {
		return RenderUtils.getFormatedResourceString("GROUP_NAME_RESOURCES",
				"label.name." + getClass().getSimpleName(), getObject().getPresentationName()); 
    }
    
    public static class Builder implements GroupBuilder {

        public Group build(Object[] arguments) {
            try {
                return new DegreeStudentsGroup((Degree) arguments[0]);
            }
            catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.degreeGroup.notDegree", arguments[0].toString());
            }
        }

        public int getMinArguments() {
            return 0;
        }

        public int getMaxArguments() {
            return 1;
        }
        
    }
}
