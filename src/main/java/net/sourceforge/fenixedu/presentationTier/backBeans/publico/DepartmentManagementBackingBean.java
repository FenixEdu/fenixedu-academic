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
package net.sourceforge.fenixedu.presentationTier.backBeans.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.FenixFramework;

public class DepartmentManagementBackingBean extends FenixBackingBean {

    public static final Comparator<DepartmentUnit> COMPARATOR_BY_REAL_NAME = new Comparator<DepartmentUnit>() {

        @Override
        public int compare(DepartmentUnit o1, DepartmentUnit o2) {
            return o1.getDepartment().getRealName().compareTo(o2.getDepartment().getRealName());
        }

    };

    private final Collection<ProfessionalCategory> sortedDepartmentCategories = new TreeSet<ProfessionalCategory>();

    private Map<ProfessionalCategory, List<Teacher>> teachersByCategory;

    public List<DepartmentUnit> getDepartmentUnits() {
        final List<DepartmentUnit> result = new ArrayList<DepartmentUnit>(UnitUtils.readAllDepartmentUnits());
        removeUnitsWithoutDepartment(result);
        Collections.sort(result, COMPARATOR_BY_REAL_NAME);
        return result;
    }

    private void removeUnitsWithoutDepartment(final List<DepartmentUnit> result) {
        final Iterator<DepartmentUnit> iterator = result.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().hasDepartment()) {
                iterator.remove();
            }
        }
    }

    public Department getDepartment() {
        String selectedDepartmentUnitID = getAndHoldStringParameter("selectedDepartmentUnitID");
        if (selectedDepartmentUnitID != null) {
            Unit departmentUnit = (Unit) FenixFramework.getDomainObject(selectedDepartmentUnitID);
            return departmentUnit.getDepartment();
        } else {
            return null;
        }
    }

    private List<Teacher> getDepartmentTeachers() {
        final SortedSet<Teacher> result = new TreeSet<Teacher>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);

        Department department = getDepartment();
        if (department != null) {
            YearMonthDay today = new YearMonthDay();
            YearMonthDay tomorrow = today.plusDays(1);
            result.addAll(department.getAllTeachers(today, tomorrow));
        }

        return new ArrayList<Teacher>(result);
    }

    private void initializeStructures() {
        teachersByCategory = new TreeMap<ProfessionalCategory, List<Teacher>>();

        for (final Teacher teacher : getDepartmentTeachers()) {
            ProfessionalCategory category = teacher.getCategory();

            if (!teachersByCategory.containsKey(category)) {
                final List<Teacher> categoryTeachers = new ArrayList<Teacher>();
                categoryTeachers.add(teacher);

                teachersByCategory.put(category, categoryTeachers);
                sortedDepartmentCategories.add(category);
            } else {
                final List<Teacher> categoryTeachers = teachersByCategory.get(category);
                categoryTeachers.add(teacher);
            }
        }
    }

    public Map<ProfessionalCategory, List<Teacher>> getTeachersByCategory() {
        if (teachersByCategory == null) {
            initializeStructures();
        }

        return teachersByCategory;
    }

    public List<ProfessionalCategory> getSortedDepartmentCategories() {
        if (sortedDepartmentCategories.isEmpty()) {
            initializeStructures();
        }

        return new ArrayList<ProfessionalCategory>(sortedDepartmentCategories);
    }

}
