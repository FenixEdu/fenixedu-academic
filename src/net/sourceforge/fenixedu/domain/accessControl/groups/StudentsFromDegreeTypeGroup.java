package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class StudentsFromDegreeTypeGroup extends Group {

    private DegreeType degreeType;

    public StudentsFromDegreeTypeGroup(DegreeType degreeType) {
	this.degreeType = degreeType;
    }

    @Override
    public Set<Person> getElements() {
	List<Degree> degrees = Degree.readAllByDegreeType(degreeType);
	Set<Person> students = new HashSet<Person>();

	for (Degree degree : degrees) {
	    for (Registration registration : degree.getRegistrations()) {
		students.add(registration.getPerson());
	    }
	}

	return students;
    }

    @Override
    public String getName() {
	return RenderUtils.getResourceString("GROUP_NAME_RESOURCES", "label.name." + getClass().getSimpleName() + "." + degreeType.toString()); 
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new StaticArgument(degreeType.getName()) };
    }

}
