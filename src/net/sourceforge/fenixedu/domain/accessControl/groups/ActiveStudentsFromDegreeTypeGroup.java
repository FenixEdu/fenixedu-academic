package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.BundleUtil;

public class ActiveStudentsFromDegreeTypeGroup extends Group {

    private final DegreeType degreeType;
    
    public ActiveStudentsFromDegreeTypeGroup(DegreeType degreeType) {
	this.degreeType = degreeType;
    }

    @Override
    public Set<Person> getElements() {
	List<Degree> degrees = Degree.readAllByDegreeType(degreeType);
	Set<Person> students = new HashSet<Person>();

	for (Degree degree : degrees) {
	    for (Registration registration : degree.getRegistrations()) {
		if(registration.isActive()) {
		    students.add(registration.getPerson());
		}
	    }
	}
	return students;
    }

    @Override
    public String getName() {
	return BundleUtil.getStringFromResourceBundle("resources.GroupNameResources",
		"label.name." + getClass().getSimpleName() + "." + degreeType.toString());
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new StaticArgument(degreeType.getName()) };
    }
    
    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    final DegreeType degreeType = DegreeType.valueOf((String) arguments[0]);
	    if (degreeType == null) {
		throw new VariableNotDefinedException("degreeType");
	    }
	    return new ActiveStudentsFromDegreeTypeGroup(degreeType);
	}

	public int getMaxArguments() {
	    return 1;
	}

	public int getMinArguments() {
	    return 1;
	}
    }
}
