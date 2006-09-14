package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class EmployeesForGivenUnit implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        VigilantGroupBean bean = (VigilantGroupBean) source;
        ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        Unit unit = bean.getSelectedUnit();
        List<Employee> employees = null;

        if (unit != null) {
            employees = unit.getAllWorkingEmployees(currentYear.getBeginDateYearMonthDay(), currentYear
                    .getEndDateYearMonthDay());
        } else {
            Department department = bean.getSelectedDepartment();
            if (department != null) {
                employees = department.getAllWorkingEmployees(currentYear.getBeginDateYearMonthDay(),
                        currentYear.getEndDateYearMonthDay());
            }
        }

        Collections.sort(employees, new BeanComparator("person.name"));
        return employees;

    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
