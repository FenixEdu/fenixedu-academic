package net.sourceforge.fenixedu.presentationTier.backBeans.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class DepartmentManagementBackingBean extends FenixBackingBean {

    public Department getDepartment() {
        Integer selectedDepartmentUnitID = getAndHoldIntegerParameter("selectedDepartmentUnitID");
        if (selectedDepartmentUnitID != null) {
            Unit departmentUnit = (Unit) rootDomainObject.readPartyByOID(selectedDepartmentUnitID); 
            return departmentUnit.getDepartment();
        } else {
            return null;
        }
    }
    
    public List<Teacher> getDepartmentTeachers() throws FenixFilterException, FenixServiceException {
        final SortedSet<Teacher> result = new TreeSet<Teacher>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);

        Department department = getDepartment();
        if (department != null) {
            result.addAll(department.getTeachersHistoric());
        }
        
        return new ArrayList<Teacher>(result);
    }

}
