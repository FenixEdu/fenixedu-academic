package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class UnitEmployeesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        VigilantGroupBean bean = (VigilantGroupBean) source;
        Unit unit = bean.getUnit();
        List<Employee> employees;

        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        employees = unit.getAllWorkingEmployees(executionYear.getBeginDateYearMonthDay(), executionYear
                .getEndDateYearMonthDay());

        return employees;

    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
