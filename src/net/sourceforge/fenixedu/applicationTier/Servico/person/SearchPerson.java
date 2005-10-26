package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
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
import sun.text.Normalizer;

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
        String email = (String) searchParameters.get(new String("email"));
        String username = (String) searchParameters.get(new String("username"));
        String documentIdNumber = (String) searchParameters.get(new String("documentIdNumber"));
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

        String[] nameWords = null;
        if (name != null && !name.trim().equals("")) {
            nameWords = name.split(" ");
            normalizeName(nameWords);
        }

        if (roleBd == null) {
            persons.addAll((List<IPerson>) persistentPerson.readAll(Person.class));

            if (startIndex == null || startIndex.equals("")) {
                allValidPersons = filterPersons(persons, username, email, name, documentIdNumber,
                        nameWords);
            } else {
                allValidPersons = getValidPersons(nameWords, persons);
            }

            totalPersons = allValidPersons.size();

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
                        if (teacher.getPerson() != null) {
                            persons.add(teacher.getPerson());
                        }
                    }
                }
            }

            else if (roleBd.getRoleType().equals(RoleType.EMPLOYEE)) {
                List<IEmployee> employees = (List<IEmployee>) sp.getIPersistentEmployee().readAll(
                        Employee.class);
                for (IEmployee employee : employees) {
                    if (employee.getPerson().getTeacher() == null) {
                        persons.add(employee.getPerson());
                    }
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

            allValidPersons = getValidPersons(nameWords, persons);
            totalPersons = allValidPersons.size();
        }

        if (startIndex != null && !startIndex.equals("")) {
            if (totalPersons.intValue() > SessionConstants.LIMIT_FINDED_PERSONS_TOTAL) {
                throw new FenixServiceException("error.search.person");
            }
        }

        Collections.sort(allValidPersons, new BeanComparator("nome"));
        List<InfoPerson> infoPersons = new ArrayList<InfoPerson>();

        if (startIndex != null && !startIndex.equals("")) {
            objects = getIntervalPersons(startIndex, allValidPersons);
            for (IPerson person : (List<IPerson>) objects.get(0)) {
                infoPersons.add(InfoPerson.newInfoFromDomain(person));
            }
        } else {
            for (IPerson person : allValidPersons) {
                infoPersons.add(InfoPerson.newInfoFromDomain(person));
            }
        }

        List<Object> result = new ArrayList<Object>(4);
        result.add(0, totalPersons);
        result.add(1, infoPersons);
        if (startIndex != null && !startIndex.equals("")) {
            result.add(2, objects.get(1));
            result.add(3, objects.get(2));
        }

        return result;
    }

    private List<IPerson> filterPersons(List<IPerson> persons, String username, String email,
            String name, String documentIdNumber, String[] nameWords) {

        List<IPerson> filterPersons = new ArrayList<IPerson>();

        List<IPerson> filterPersonsName = new ArrayList<IPerson>();
        List<IPerson> filterPersonsEmail = new ArrayList<IPerson>();
        List<IPerson> filterPersonsBI = new ArrayList<IPerson>();
        List<IPerson> filterPersonsUsername = new ArrayList<IPerson>();

        if (email != null && !email.trim().equals("")) {
            email = normalize(email.trim());
            for (IPerson person : persons) {
                if (person.getEmail() != null) {
                    String personEmail = normalize(person.getEmail().trim());
                    if (personEmail.indexOf(email) != -1) {
                        filterPersonsEmail.add(person);
                    }
                }
            }
            if (filterPersonsEmail.isEmpty()) {
                return filterPersonsEmail;
            }
            filterPersons.addAll(filterPersonsEmail);
        }
        if (username != null && !username.trim().equals("")) {
            username = normalize(username.trim());
            for (IPerson person : persons) {
                if (person.getUsername() != null) {
                    String personUserName = normalize(person.getUsername().trim());
                    if (personUserName.indexOf(username) != -1) {
                        filterPersonsUsername.add(person);
                    }
                }
            }
            if (filterPersonsUsername.isEmpty()) {
                return filterPersonsUsername;
            } else if (filterPersons.isEmpty()) {
                filterPersons.addAll(filterPersonsUsername);
            } else {
                filterPersons = (List<IPerson>) CollectionUtils.intersection(filterPersons,
                        filterPersonsUsername);
                if(filterPersons.isEmpty()){
                    return filterPersons; 
                }
            }
        }
        if (name != null && !name.equals("")) {
            filterPersonsName = getValidPersons(nameWords, persons);
            
            if (filterPersonsName.isEmpty()) {
                return filterPersonsName;
            } else if (filterPersons.isEmpty()) {
                filterPersons.addAll(filterPersonsName);
            } else {
                filterPersons = (List<IPerson>) CollectionUtils.intersection(filterPersons,
                        filterPersonsName);
                if(filterPersons.isEmpty()){
                    return filterPersons; 
                }
            }
        }
        if (documentIdNumber != null && !documentIdNumber.trim().equals("")) {
            documentIdNumber = normalize(documentIdNumber.trim());
            for (IPerson person : persons) {
                if (person.getNumeroDocumentoIdentificacao() != null && person.getNome() != null) {
                    String personBI = normalize(person.getNumeroDocumentoIdentificacao().trim());
                    if (personBI.trim().indexOf(documentIdNumber.trim()) != -1) {
                        filterPersonsBI.add(person);
                    }
                }
            }
            if(filterPersonsBI.isEmpty()){
                return filterPersonsBI;
            }
            else if (filterPersons.isEmpty()) {
                filterPersons.addAll(filterPersonsBI);
            } else {
                filterPersons = (List<IPerson>) CollectionUtils.intersection(filterPersons,
                        filterPersonsBI);
                if(filterPersons.isEmpty()){
                    return filterPersons; 
                }
            }
        }

        List<IPerson> totalPersons = removeINAPersons(filterPersons);

        return totalPersons;
    }

    private List<IPerson> removeINAPersons(List<IPerson> filterPersons) {
        List<IPerson> totalPersons = new ArrayList<IPerson>();
        for (IPerson person : filterPersons) {
            if (person.getUsername() != null && person.getUsername().indexOf("INA") == -1) {
                totalPersons.add(person);
            }
        }
        return totalPersons;
    }

    private void normalizeName(String[] nameWords) {
        for (int i = 0; i < nameWords.length; i++) {
            nameWords[i] = normalize(nameWords[i]);
        }
    }

    private String normalize(String string) {
        return Normalizer.normalize(string, Normalizer.DECOMP, Normalizer.DONE).replaceAll(
                "[^\\p{ASCII}]", "").toLowerCase();
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

        int whiteSpaces = 0;
        List<IPerson> persons_ = new ArrayList();

        for (int i = 0; i < nameWords.length; i++) {
            String string = nameWords[i];
            if (string.trim().equals("")) {
                whiteSpaces++;
            }
        }

        for (IPerson person : persons) {
            String personName = person.getNome();
            String userName = person.getUsername();
            if (personName != null && userName.indexOf("INA") == -1) {
                String[] personNameWords = personName.split(" ");
                normalizeName(personNameWords);
                int count = 0;
                for (int i = 0; i < nameWords.length; i++) {
                    String name_ = nameWords[i];
                    for (int j = 0; j < personNameWords.length; j++) {
                        if (!personNameWords[j].trim().equals("")
                                && !personNameWords[j].trim().equals("")
                                && personNameWords[j].trim().equals(name_.trim())) {
                            count++;
                            break;
                        }
                    }
                }
                if (count == (nameWords.length - whiteSpaces)) {
                    persons_.add(person);
                }
            }
        }
        return persons_;
    }
}