package net.sourceforge.fenixedu.presentationTier.backBeans.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class DepartmentManagementBackingBean extends FenixBackingBean {

    private Collection<Category> sortedDepartmentCategories = new TreeSet<Category>();

    private Map<Category, List<Teacher>> teachersByCategory;

    public List<Unit> getDepartmentUnits() {
        List<Unit> result = UnitUtils.readAllDepartmentUnits(); 
        
        Collections.sort(result, new BeanComparator("department.realName"));
        
        return result;
    }
    
    public Department getDepartment() {
        Integer selectedDepartmentUnitID = getAndHoldIntegerParameter("selectedDepartmentUnitID");
        if (selectedDepartmentUnitID != null) {
            Unit departmentUnit = (Unit) rootDomainObject.readPartyByOID(selectedDepartmentUnitID);
            return departmentUnit.getDepartment();
        } else {
            return null;
        }
    }

    private List<Teacher> getDepartmentTeachers() {
        final SortedSet<Teacher> result = new TreeSet<Teacher>(
                Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);

        Department department = getDepartment();
        if (department != null) {
            YearMonthDay today = new YearMonthDay();
            YearMonthDay tomorrow = today.plusDays(1);
            result.addAll(department.getAllTeachers(today, tomorrow));
        }

        return new ArrayList<Teacher>(result);
    }

    private void initializeStructures() {
        teachersByCategory = new TreeMap<Category, List<Teacher>>();

        for (final Teacher teacher : getDepartmentTeachers()) {
            Category category = teacher.getCategory();

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

    public Map<Category, List<Teacher>> getTeachersByCategory() {
        if (teachersByCategory == null) {
            initializeStructures();
        }

        return teachersByCategory;
    }

    public List<Category> getSortedDepartmentCategories() {
        if (sortedDepartmentCategories.isEmpty()) {
            initializeStructures();
        }

        return new ArrayList<Category>(sortedDepartmentCategories);
    }

}
