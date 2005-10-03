package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.grant.owner.IGrantOwner;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class SearchPerson implements IService {

    /*
     * This service return a list with 2 elements. The first is a Integer with
     * the number of elements returned by the main search, The second is a list
     * with the elemts returned by the limited search.
     */
    public List run(HashMap searchParameters) throws ExcepcaoPersistencia, FenixServiceException {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        IPersistentRole persistentRole = sp.getIPersistentRole();
        ICursoPersistente persistentDegree = sp.getICursoPersistente();
        IPersistentDepartment persistentDepartment = sp.getIDepartamentoPersistente();

        String name = (String) searchParameters.get(new String("name"));
        Integer startIndex = (Integer) searchParameters.get(new String("startIndex"));
        String roleType = (String) searchParameters.get(new String("roleType"));
        String degreeType = (String) searchParameters.get(new String("degreeType"));
        Integer degreeId = (Integer) searchParameters.get(new String("degreeId"));
        Integer departmentId = (Integer) searchParameters.get(new String("departmentId"));
        IRole roleBd = null;
        IDegree degree = null;
        IDepartment department = null;
        DegreeType degreetype = null;
        List<IPerson> persons = new ArrayList<IPerson>();
        List<IPerson> allValidPersons = new ArrayList<IPerson>();
        List<ITeacher> teachers = new ArrayList<ITeacher>();
        List<Object> objects = new ArrayList<Object>();
        Integer totalPersons = null;

        if (roleType != null && roleType.length() > 0) {
            roleBd = persistentRole.readByRoleType(RoleType.valueOf(roleType));
        }

        if (degreeId != null) {
            degree = (IDegree) persistentDegree.readByOID(Degree.class, degreeId);
        }
        if (degreeType != null && degreeType.length() > 0) {
            degreetype = DegreeType.valueOf(degreeType);
        }

        if (departmentId != null) {
            department = (IDepartment) persistentDepartment.readByOID(Department.class, departmentId);
        }

        String[] nameWords = name.split(" ");

        if (roleBd == null) {
            persons = (List<IPerson>) persistentPerson.readAll(Person.class);
        
        } else {

            if (roleBd.getRoleType().equals(RoleType.TEACHER)) {

                if (department != null) {
                    teachers = department.getTeachers();
                    for (ITeacher teacher : teachers) {
                        persons.add(teacher.getPerson());
                    }
                } else {
                    teachers = (List<ITeacher>) sp.getIPersistentTeacher().readAll(Teacher.class);
                    for (ITeacher teacher : teachers) {
                        persons.add(teacher.getPerson());
                    }
                }
            }

            else if (roleBd.getRoleType().equals(RoleType.EMPLOYEE)) {
                List<IEmployee> employees = (List<IEmployee>) sp.getIPersistentEmployee().readAll(
                        Employee.class);
                for (IEmployee employee : employees) {
                    persons.add(employee.getPerson());
                }
            }

            else if (roleBd.getRoleType().equals(RoleType.STUDENT)) {

                if (degreetype == null) {

                    List<IStudent> students = (List<IStudent>) sp.getIPersistentStudent().readAll(
                            Student.class);
                    for (IStudent student : students) {
                        persons.add(student.getPerson());
                    }
                } else {
                    
                    List<IStudent> students = (List<IStudent>) sp.getIPersistentStudent().readAll(
                            Student.class);

                    persons = getValidDegreeTypePersons(degree, students, degreetype);
                }
            }

            else if (roleBd.getRoleType().equals(RoleType.GRANT_OWNER)) {
                List<IGrantOwner> grantOwners = (List<IGrantOwner>) sp.getIPersistentGrantOwner()
                        .readAll(GrantOwner.class);
                for (IGrantOwner grantOwner : grantOwners) {
                    persons.add(grantOwner.getPerson());
                }
            }

        }       
        
        allValidPersons = getValidPersons(nameWords, persons);
        totalPersons = allValidPersons.size();
        
        if(totalPersons.intValue() > 100){
            throw new FenixServiceException("error.search.person");
        }
       
        Collections.sort(allValidPersons, new BeanComparator("nome"));        
        objects = getIntervalPersons(startIndex, allValidPersons);

        List<InfoPerson> infoPersons = new ArrayList<InfoPerson>();

        for (IPerson person : (List<IPerson>) objects.get(0)) {
            infoPersons.add(InfoPerson.newInfoFromDomain(person));
        }

        List<Object> result = new ArrayList<Object>(4);
        result.add(0, totalPersons);
        result.add(1, infoPersons);
        result.add(2, objects.get(1));
        result.add(3, objects.get(2));

        return result;
    }

    private List<IPerson> getValidDegreeTypePersons(IDegree degree, List<IStudent> students,
            DegreeType degreeType) {

        List<IPerson> allValidPersons = new ArrayList<IPerson>();

        for (IStudent student : students) {            
            if (student.getDegreeType().equals(degreeType)) {
                if (degree != null) {
                    List<IStudentCurricularPlan> studentsCurrPlans = student.getStudentCurricularPlans();
                    for (IStudentCurricularPlan studentCurricularPlan : studentsCurrPlans) {
                        if (studentCurricularPlan.getCurrentState().equals(
                                StudentCurricularPlanState.ACTIVE)
                                && studentCurricularPlan.getDegreeCurricularPlan().getDegree().equals(
                                        degree)) {

                            allValidPersons.add(student.getPerson());
                            break;
                        }
                    }
                } else {
                    allValidPersons.add(student.getPerson());                   
                }
            }
        }
        return allValidPersons;
    }

    private List<Object> getIntervalPersons(Integer startIndex, List<IPerson> persons) {

        List<Object> objects = new ArrayList<Object>(3);
        List<IPerson> persons_ = new ArrayList<IPerson>();
        Integer startIndexBefore = startIndex;
        int index = 0;

        for (int i = startIndex; index < SessionConstants.LIMIT_FINDED_PERSONS; i++) {
            try {
                IPerson person = persons.get(i);
                persons_.add(person);
                startIndex++;
                index++;
            } catch (IndexOutOfBoundsException boundsException) {
                break;
            }
        }
        objects.add(0, persons_);
        objects.add(1, startIndex);
        objects.add(2, startIndexBefore);
        return objects;
    }

    private List<IPerson> getValidPersons(String[] nameWords, List<IPerson> persons) {

        List<IPerson> persons_ = new ArrayList();

        for (IPerson person : persons) {
            if (person.getNome() != null) {
                String personName = person.getNome().toLowerCase();
                int count = 0;
                for (int i = 0; i < nameWords.length; i++) {
                    String name_ = nameWords[i].toLowerCase();
                    if (personName.indexOf(name_) != -1) {
                        count++;
                    }
                }
                if (count == nameWords.length) {
                    persons_.add(person);
                }
            }
        }
        return persons_;
    }
}