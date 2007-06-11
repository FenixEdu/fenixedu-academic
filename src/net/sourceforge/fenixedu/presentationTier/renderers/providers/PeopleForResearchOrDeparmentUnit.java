package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentTeachersByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.MethodUtils;

public class PeopleForResearchOrDeparmentUnit implements DataProvider {

	public Converter getConverter() {
		return null;
	}

	public Object provide(Object source, Object currentValue) {
		Unit unit;
		if(source instanceof Unit) {
			unit = (Unit) source;
		}
		else {
			try {
			unit = (Unit) MethodUtils.invokeMethod(source, "getUnit", null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return getPeopleForUnit(unit);
	}

	private Collection<Person> getPeopleForUnit(Unit unit) {
		if(unit.isResearchUnit()) {
			return ((ResearchUnit)unit).getAssociatedPeople();
		}
		if(unit.isDepartmentUnit()) {
			ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
			Department department = ((DepartmentUnit)unit).getDepartment();
			Collection<Person> people = new HashSet<Person>();
			people.addAll(new DepartmentTeachersByExecutionYearGroup(currentYear,department).getElements());
			people.addAll(new DepartmentEmployeesByExecutionYearGroup(currentYear,department).getElements());
			return people;
		}
		return Collections.EMPTY_LIST;
	}

}
