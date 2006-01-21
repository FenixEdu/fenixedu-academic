package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

public class UpdateDepartmentsCompetenceCourseManagementGroup extends Service {

    public void run(Department department, Integer[] add, Integer[] remove) throws ExcepcaoPersistencia {
        List<Person> toAdd = materializePersons(persistentSupport, add);
        List<Person> toRemove = materializePersons(persistentSupport, remove);
        List<Person> finalList = new ArrayList<Person>();
        
        Role bolonhaRole = persistentSupport.getIPersistentRole().readByRoleType(RoleType.BOLONHA_MANAGER); 
        
        Group group = department.getCompetenceCourseMembersGroup();
        if (group == null) {
            group = new FixedSetGroup();
        }
        
        Iterator<Person> iterator = group.getElementsIterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            
            if (! toRemove.contains(person)) {
                finalList.add(person);
                addBolonhaRole(person, bolonhaRole);
            } else if (person.hasRole(RoleType.BOLONHA_MANAGER) && !belongsToOtherGroupsWithSameRole(persistentSupport, department, person)) {
                person.removeRoleByType(RoleType.BOLONHA_MANAGER);
            }
        }
        
        for (Person person : toAdd) {
            if (! finalList.contains(person)) {
                finalList.add(person);
                addBolonhaRole(person, bolonhaRole);
            }
        }
        
        department.setCompetenceCourseMembersGroup(new FixedSetGroup(finalList));
    }

    private List<Person> materializePersons(ISuportePersistente persistentSupport, Integer[] personsIDs) throws ExcepcaoPersistencia {
        if (personsIDs != null) {
            List<Person> result = new ArrayList<Person>();
            
            for (Integer personID : personsIDs) {
                result.add((Person) persistentObject.readByOID(Person.class, personID));
            }
            
            return result;
        } else {
            return new ArrayList<Person>();    
        }
    }

    private void addBolonhaRole(Person person, Role bolonhaRole) throws ExcepcaoPersistencia {
        if (!person.hasRole(RoleType.BOLONHA_MANAGER)) {
            person.addPersonRoles(bolonhaRole);    
        }
    }

    private boolean belongsToOtherGroupsWithSameRole(ISuportePersistente persistentSupport, Department departmentWhoAsks, Person person) throws ExcepcaoPersistencia {
        List<Department> departments = (List<Department>) persistentObject.readAll(Department.class);
        for (Department department : departments) {
            if (department != departmentWhoAsks) {
                Group group = department.getCompetenceCourseMembersGroup();
                if (group != null && group.isMember(person)) {
                    return true;
                }
            }
        }

        List<DegreeCurricularPlan> dcps = (List<DegreeCurricularPlan>) persistentObject.readAll(DegreeCurricularPlan.class);
        for (DegreeCurricularPlan dcp : dcps) {
            Group group = dcp.getCurricularPlanMembersGroup();
            if (group != null && group.isMember(person)) {
                return true;
            }
        }

        return false;
    }

}
