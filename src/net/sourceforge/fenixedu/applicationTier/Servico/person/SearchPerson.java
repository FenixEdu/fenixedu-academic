package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
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

            IRole role = persistentRole.readByRoleType(RoleType.PERSON);
            persons = role.getAssociatedPersons();

            if (startIndex == null || startIndex.equals("")) {
                allValidPersons = filterPersons(persons, username, email, documentIdNumber, nameWords);
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
                    IRole role = persistentRole.readByRoleType(RoleType.TEACHER);
                    persons = role.getAssociatedPersons();
                }
            }

            else if (roleBd.getRoleType().equals(RoleType.EMPLOYEE)) {
                IRole role = persistentRole.readByRoleType(RoleType.EMPLOYEE);
                List<IPerson> personsAux = role.getAssociatedPersons();
                for (IPerson person : personsAux) {
                    if (person.getTeacher() == null) {
                        persons.add(person);
                    }
                }
            }

            else if (roleBd.getRoleType().equals(RoleType.STUDENT)) {

                IRole role = persistentRole.readByRoleType(RoleType.STUDENT);
                persons = role.getAssociatedPersons();

                if (degreetype != null) {
                    persons = getValidDegreeTypePersons(degree, persons, degreetype);
                }
            }

            else if (roleBd.getRoleType().equals(RoleType.GRANT_OWNER)) {
                IRole role = persistentRole.readByRoleType(RoleType.GRANT_OWNER);
                persons = role.getAssociatedPersons();
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
            String documentIdNumber, String[] nameWords) {

        List<IPerson> validPersons = new ArrayList<IPerson>();

        for (IPerson person : persons) {

            boolean found = true, entry = false;

            if (found && email != null && !email.trim().equals("")) {
                if (person.getEmail() != null) {
                    email = normalize(email.trim());
                    String personEmail = normalize(person.getEmail().trim());
                    if (personEmail.indexOf(email) == -1) {
                        found = false;
                    }
                } else {
                    found = false;
                }
                entry = true;
            }
            if (found && username != null && !username.trim().equals("")) {
                if (person.getUsername() != null) {
                    username = normalize(username.trim());
                    String personUserName = normalize(person.getUsername().trim());
                    if (personUserName.indexOf(username) == -1) {
                        found = false;
                    }
                } else {
                    found = false;
                }
                entry = true;
            }
            if (found && documentIdNumber != null && !documentIdNumber.trim().equals("")) {
                if (person.getNumeroDocumentoIdentificacao() != null) {
                    documentIdNumber = normalize(documentIdNumber.trim());
                    String personBI = normalize(person.getNumeroDocumentoIdentificacao().trim());
                    if (personBI.indexOf(documentIdNumber) == -1) {
                        found = false;
                    }
                } else {
                    found = false;
                }
                entry = true;
            }
            if (found && nameWords != null && nameWords.length > 0) {
                if (person.getNome() != null) {
                    int whiteSpaces = getWhiteSpaces(nameWords);
                    found = verifyNameEquality(nameWords, whiteSpaces, person);
                } else {
                    found = false;
                }
                entry = true;
            }

            if (found && entry) {
                validPersons.add(person);
            }
        }
        return validPersons;
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

    private List<IPerson> getValidDegreeTypePersons(IDegree degree, List<IPerson> persons,
            DegreeType degreeType) {

        List<IPerson> validPersons = new ArrayList<IPerson>();

        if (degree != null) {
            for (IDegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
                for (IStudentCurricularPlan studentCurricularPlan : degreeCurricularPlan
                        .getStudentCurricularPlans()) {
                    if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.ACTIVE) {
                        validPersons.add(studentCurricularPlan.getStudent().getPerson());
                    }
                }
            }
        } else {
            for (IPerson person : persons) {
                IStudent student = person.getStudentByType(degreeType);
                if(student != null){
                    validPersons.add(person);
                }
            }
        }
        return validPersons;
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

    private List<IPerson> getValidPersons(String[] nameWords, List<IPerson> allPersons) {

        List<IPerson> persons = new ArrayList();

        int whiteSpaces = getWhiteSpaces(nameWords);
        for (IPerson person : allPersons) {
            if (verifyNameEquality(nameWords, whiteSpaces, person)) {
                persons.add(person);
            }
        }
        return persons;
    }

    private int getWhiteSpaces(String[] nameWords) {
        int whiteSpaces = 0;
        for (int i = 0; i < nameWords.length; i++) {
            if (nameWords[i].trim().equals("")) {
                whiteSpaces++;
            }
        }
        return whiteSpaces;
    }

    private boolean verifyNameEquality(String[] nameWords, int whiteSpaces, IPerson person) {
        String personName = person.getNome();
        if (personName != null) {
            String[] personNameWords = personName.split(" ");
            normalizeName(personNameWords);
            int count = 0;
            for (int i = 0; i < nameWords.length; i++) {
                for (int j = 0; j < personNameWords.length; j++) {
                    if (!personNameWords[j].trim().equals("") && !nameWords[i].trim().equals("")
                            && personNameWords[j].trim().equals(nameWords[i].trim())) {
                        count++;
                        break;
                    }
                }
            }
            if (count == (nameWords.length - whiteSpaces)) {
                return true;
            }
        }
        return false;
    }
}