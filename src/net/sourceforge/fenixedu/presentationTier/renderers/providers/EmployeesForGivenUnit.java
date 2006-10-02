package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

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

        VigilantGroup group = bean.getSelectedVigilantGroup();
        if(group!=null) {
        	List<Vigilant> vigilants = group.getVigilants();
        	for(Vigilant vigilant : vigilants) {
        		Employee employee = vigilant.getPerson().getEmployee();
        		if(employee!=null) {
        			employees.remove(employee);
        		}
        	}
        }
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(new CategoryComparator());
        chain.addComparator(new BeanComparator("person.name"));
        chain.addComparator(new BeanComparator("person.username"));
        Collections.sort(employees, chain);
        return employees;

    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

    private class CategoryComparator implements Comparator {

		public int compare(Object o1, Object o2) {
				Employee e1 = (Employee) o1;
				Employee e2 = (Employee) o2;
				
				Category c1 = e1.getCategory();
				Category c2 = e2.getCategory();
				
				if(c1==null) return -1;
				if(c2==null) return 1;
				
				return -c1.compareTo(c2);
		}
    	
    }
}
