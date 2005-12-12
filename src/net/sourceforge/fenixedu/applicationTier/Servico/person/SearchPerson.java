package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.StringNormalizer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import sun.text.Normalizer;

public class SearchPerson implements IService {

    public static class SearchParameters {

        private String email, username, documentIdNumber;

        private String[] nameWords;

        private IRole role;

        private IDegree degree;

        private IDepartment department;

        private DegreeType degreeType;

        public SearchParameters(String name, String email, String username, String documentIdNumber,
                String roleType, String degreeTypeString, Integer degreeId, Integer departmentId)
                throws ExcepcaoPersistencia {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            this.nameWords = (name != null && !name.equals("")) ? getNameWords(name) : null;
            this.email = (email != null && !email.equals("")) ? normalize(email.trim()) : null;
            this.username = (username != null && !username.equals("")) ? normalize(username.trim())
                    : null;
            this.documentIdNumber = (documentIdNumber != null && !documentIdNumber.equals("")) ? normalize(documentIdNumber
                    .trim())
                    : null;

            if (roleType != null && roleType.length() > 0) {
                role = (IRole) sp.getIPersistentRole().readByRoleType(RoleType.valueOf(roleType));
            }

            if (degreeId != null) {
                degree = (IDegree) sp.getICursoPersistente().readByOID(Degree.class, degreeId);
            }

            if (degreeTypeString != null && degreeTypeString.length() > 0) {
                degreeType = DegreeType.valueOf(degreeTypeString);
            }

            if (departmentId != null) {
                department = (IDepartment) sp.getIDepartamentoPersistente().readByOID(Department.class,
                        departmentId);
            }
        }

        private static String[] getNameWords(String name) {
            String[] nameWords = null;
            if (name != null && !name.trim().equals("")) {
                nameWords = name.trim().split(" ");
                normalizeName(nameWords);
            }
            return nameWords;
        }

        public static List<InfoPerson> getIntervalPersons(Integer start, Integer end,
                List<InfoPerson> allPersons) {

            return (end >= allPersons.size()) ? allPersons.subList(start, allPersons.size()) : allPersons
                    .subList(start, end);
        }

        public IDegree getDegree() {
            return degree;
        }

        public DegreeType getDegreeType() {
            return degreeType;
        }

        public IDepartment getDepartment() {
            return department;
        }

        public String getDocumentIdNumber() {
            return documentIdNumber;
        }

        public String getEmail() {
            return email;
        }

        public String[] getNameWords() {
            return nameWords;
        }

        public IRole getRole() {
            return role;
        }

        public String getUsername() {
            return username;
        }
    }

    public SearchPersonResults run(SearchParameters searchParameters, Predicate predicate)
            throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final List<IPerson> persons;
        List<IPerson> allValidPersons = new ArrayList<IPerson>();
        List<ITeacher> teachers = new ArrayList<ITeacher>();
        int totalPersons;

        IRole roleBd = searchParameters.getRole();
        if (roleBd == null) {
            roleBd = sp.getIPersistentRole().readByRoleType(RoleType.PERSON);
        }

        if ((roleBd.getRoleType() == RoleType.TEACHER) && (searchParameters.getDepartment() != null)) {
            persons = new ArrayList<IPerson>();
            teachers = searchParameters.getDepartment().getTeachers();
            for (ITeacher teacher : teachers) {
                persons.add(teacher.getPerson());
            }
        } else if (roleBd.getRoleType() == RoleType.EMPLOYEE) {
            persons = new ArrayList<IPerson>();
            for (IPerson person : roleBd.getAssociatedPersons()) {
                if (person.getTeacher() == null) {
                    persons.add(person);
                }
            }
        } else {
            persons = roleBd.getAssociatedPersons();
        }

        allValidPersons = (List<IPerson>) CollectionUtils.select(persons, predicate);
        totalPersons = allValidPersons.size();

        if (totalPersons > SessionConstants.LIMIT_FINDED_PERSONS_TOTAL) {
            throw new FenixServiceException("error.search.person");
        }

        Collections.sort(allValidPersons, new BeanComparator("nome"));
        List<InfoPerson> infoPersons = new ArrayList<InfoPerson>();

        for (IPerson person : (List<IPerson>) allValidPersons) {
            infoPersons.add(InfoPerson.newInfoFromDomain(person));
        }

        return new SearchPersonResults(infoPersons, totalPersons);
    }

    private static void normalizeName(String[] nameWords) {
        for (int i = 0; i < nameWords.length; i++) {
            nameWords[i] = normalize(nameWords[i]);
        }
    }

    private static String normalize(String string) {
        return StringNormalizer.normalize(string);
    }

    // --------------- Search Person Predicate
    // -------------------------------------------

    public static class SearchPersonPredicate implements Predicate {

        SearchParameters searchParameters;

        public SearchPersonPredicate(SearchParameters searchParameters) {
            this.searchParameters = searchParameters;
        }

        public boolean evaluate(Object arg0) {
            IPerson person = (IPerson) arg0;

            return verifyParameter(person.getEmail(), searchParameters.getEmail())
                    && verifyParameter(person.getUsername(), searchParameters.getUsername())
                    && verifyParameter(person.getNumeroDocumentoIdentificacao(), searchParameters
                            .getDocumentIdNumber())
                    && verifyNameEquality(searchParameters.getNameWords(), person)
                    && verifyDegreeType(searchParameters.getDegree(), searchParameters.getDegreeType(),
                            person);
        }

        private boolean verifyDegreeType(final IDegree degree, final DegreeType degreeType,
                final IPerson person) {
            return degreeType == null || verifyDegreeType(degree, person.getStudentByType(degreeType));
        }

        private boolean verifyDegreeType(final IDegree degree, final IStudent studentByType) {
            return studentByType != null && (degree == null || verifyDegree(degree, studentByType));
        }

        private boolean verifyDegree(final IDegree degree, final IStudent studentByType) {
            final IStudentCurricularPlan studentCurricularPlan = studentByType
                    .getActiveStudentCurricularPlan();
            return (studentCurricularPlan != null && degree == studentCurricularPlan
                    .getDegreeCurricularPlan().getDegree());
        }

        private boolean verifyParameter(String parameter, String searchParameter) {
            return (searchParameter == null)
                    || (parameter != null && normalizeAndCompare(parameter, searchParameter));
        }

        private boolean normalizeAndCompare(String parameter, String searchParameter) {
            String personParameter = normalize(parameter.trim());
            return (personParameter.indexOf(searchParameter) == -1) ? false : true;
        }

        private static boolean verifyNameEquality(String[] nameWords, IPerson person) {

            if (nameWords == null) {
                return true;
            }

            if (person.getNome() != null) {
                String[] personNameWords = person.getNome().trim().split(" ");
                normalizeName(personNameWords);
                int j, i;
                for (i = 0; i < nameWords.length; i++) {
                    if (!nameWords[i].equals("")) {
                        for (j = 0; j < personNameWords.length; j++) {
                            if (personNameWords[j].equals(nameWords[i])) {
                                break;
                            }
                        }
                        if (j == personNameWords.length) {
                            return false;
                        }
                    }
                }
                if (i == nameWords.length) {
                    return true;
                }
            }
            return false;
        }
    }

    // ------------ SearchPerson Results Class -------------
    public static class SearchPersonResults {

        List<InfoPerson> validPersons;

        int totalPersons;

        public SearchPersonResults(List<InfoPerson> validPersons, int totalPersons) {
            this.totalPersons = totalPersons;
            this.validPersons = validPersons;
        }

        public int getTotalPersons() {
            return totalPersons;
        }

        public List<InfoPerson> getValidPersons() {
            return validPersons;
        }
    }
}
