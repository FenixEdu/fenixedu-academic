/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers.providers;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Employee;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.vigilancy.VigilantGroup;
import org.fenixedu.academic.domain.vigilancy.VigilantWrapper;
import org.fenixedu.academic.ui.struts.action.vigilancy.VigilantGroupBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EmployeesForGivenUnit implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        VigilantGroupBean bean = (VigilantGroupBean) source;
        ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        Unit unit = bean.getSelectedUnit();
        List<Employee> employees = null;

        if (unit != null) {
            employees =
                    Employee.getAllWorkingEmployees(unit, currentYear.getBeginDateYearMonthDay(),
                            currentYear.getEndDateYearMonthDay());
        } else {
            Department department = bean.getSelectedDepartment();
            if (department != null) {
                employees =
                        Employee.getAllWorkingEmployees(department.getDepartmentUnit(), currentYear.getBeginDateYearMonthDay(),
                                currentYear.getEndDateYearMonthDay());
            }
        }

        VigilantGroup group = bean.getSelectedVigilantGroup();
        if (group != null) {
            Collection<VigilantWrapper> vigilants = group.getVigilants();
            for (VigilantWrapper vigilant : vigilants) {
                Employee employee = vigilant.getPerson().getEmployee();
                if (employee != null) {
                    employees.remove(employee);
                }
            }
        }
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(new CategoryComparator());
        chain.addComparator(new ReverseComparator(new BeanComparator("person.username")));
        Collections.sort(employees, chain);
        return employees;

    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

    private class CategoryComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Employee e1 = (Employee) o1;
            Employee e2 = (Employee) o2;

            Teacher t1 = e1.getPerson().getTeacher();
            Teacher t2 = e2.getPerson().getTeacher();

            ProfessionalCategory c1 = (t1 != null) ? ProfessionalCategory.getCategory(t1) : null;
            ProfessionalCategory c2 = (t2 != null) ? ProfessionalCategory.getCategory(t2) : null;

            if (c1 == null && c2 == null) {
                return 0;
            }
            if (c1 == null) {
                return -1;
            }
            if (c2 == null) {
                return 1;
            }

            return -c1.compareTo(c2);
        }

    }
}
