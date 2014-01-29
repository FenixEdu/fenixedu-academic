package pt.utl.ist.scripts.process.exportData.santanderCardGeneration;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatch;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderEntry;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderProblem;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class SantanderBatchFillerWorker {
    private static String recordEnd = "*";
    private static String lineEnd = "\r\n";
    private static String alamedaOid = "2465311230081";
    private static String tagusparkOid = "2465311230082";
    private static String itnOid = "2465311261622";
    private static String alamedaAddr = "Avenida Rovisco Pais, 1";
    private static String alamedaZip = "1049-001";
    private static String alamedaTown = "Lisboa";
    private static String tagusAddr = "Av. Prof. Doutor Aníbal Cavaco Silva";
    private static String tagusZip = "2744-016";
    private static String tagusTown = "Porto Salvo";
    private static String itnAddr = "Estrada Nacional 10 (ao Km 139,7)";
    private static String itnZip = "2695-066";
    private static String itnTown = "Bobadela";
    private static String IST_FULL_NAME = "Instituto Superior Técnico";

    protected final Logger logger;

    public SantanderBatchFillerWorker(final Logger logger) {
        this.logger = logger;
    }

    public void run() {
        logger.info("[" + (new DateTime()).toString("yyyy-MM-dd HH:mm") + "] Looking for open batches to populate...\n");
        for (SantanderBatch batch : Bennu.getInstance().getSantanderBatchesSet()) {
            if (batch.getGenerated() != null) {
                continue;
            }
            final Set<Object[]> lines = new HashSet<Object[]>();
            final Role personRole = Role.getRoleByRoleType(RoleType.PERSON);
            for (final Person person : personRole.getAssociatedPersonsSet()) {
                if (person.getIstUsername() != null
                        && person.namesCorrectlyPartitioned()) {
                    generateLine(lines, batch, person);
                }
            }
            fillBatch(batch, lines);
        }
        logger.info("[" + (new DateTime()).toString("yyyy-MM-dd HH:mm") + "] Work finished. :)");
    }

    @Atomic
    private void fillBatch(final SantanderBatch batch, final Set<Object[]> lines) {
        for (final Object[] o : lines) {
            final Person person = (Person) o[0];
            final String line = (String) o[1];
            new SantanderEntry(batch, person, line);
        }
        batch.setGenerated(new DateTime());
        logger.info("Processed batch #" + batch.getExternalId());
        logger.info("Total number of records: " + batch.getSantanderEntriesSet().size() + "\n");
    }

    private void generateLine(final Set<Object[]> lines, SantanderBatch batch, Person person) {
        /*
         * 1. Teacher
         * 2. Researcher
         * 3. Employee
         * 4. GrantOwner
         * 5. Student
         */
        String line = null;
        if (treatAsTeacher(person)) {
            line = createLine(batch, person, RoleType.TEACHER);
        } else if (treatAsResearcher(person)) {
            line = createLine(batch, person, RoleType.RESEARCHER);
        } else if (treatAsEmployee(person)) {
            line = createLine(batch, person, RoleType.EMPLOYEE);
        } else if (treatAsGrantOwner(person)) {
            line = createLine(batch, person, RoleType.GRANT_OWNER);
        } else if (treatAsStudent(person, batch.getExecutionYear())) {
            line = createLine(batch, person, RoleType.STUDENT);
        } else {
            return;
        }
        if (line != null) {
            lines.add(new Object[] { person, line });
            //new SantanderEntry(batch, person, line);
        }
    }

    private boolean treatAsTeacher(Person person) {
        if (person.hasRole(RoleType.TEACHER)) {
            final Teacher teacher = person.getTeacher();
            return (teacher != null && teacher.getCurrentWorkingUnit() != null && person.getPersonProfessionalData() != null && person
                    .getPersonProfessionalData().getGiafProfessionalDataByCategoryType(CategoryType.TEACHER) != null);
        }
        return false;
    }

    private boolean treatAsResearcher(Person person) {
        return (person.hasRole(RoleType.RESEARCHER) && person.hasRole(RoleType.EMPLOYEE) && person.getEmployee() != null && person
                .getEmployee().getCurrentWorkingPlace() != null);
    }

    private boolean treatAsEmployee(Person person) {
        return (person.hasRole(RoleType.EMPLOYEE) && person.hasPersonProfessionalData());
    }

    private boolean treatAsGrantOwner(Person person) {
        return (isGrantOwner(person))
                || (person.hasRole(RoleType.GRANT_OWNER) && person.hasEmployee() && !person.hasRole(RoleType.EMPLOYEE) && person
                        .hasPersonProfessionalData());
    }

    private boolean treatAsStudent(Person person, ExecutionYear executionYear) {
        if (person.hasStudent()) {
            final List<Registration> activeRegistrations = person.getStudent().getActiveRegistrations();
            for (final Registration registration : activeRegistrations) {
                if (registration.isBolonha()) {
                    return true;
                }
            }
            final InsuranceEvent event = person.getInsuranceEventFor(executionYear);
            final PhdIndividualProgramProcess phdIndividualProgramProcess =
                    event != null && event.isClosed() ? find(person.getPhdIndividualProgramProcesses()) : null;
            return (phdIndividualProgramProcess != null);
        }
        return false;
    }

    private boolean isGrantOwner(final Person person) {
        if (person.hasRole(RoleType.GRANT_OWNER)) {
            final PersonContractSituation currentGrantOwnerContractSituation =
                    person.getPersonProfessionalData() != null ? person.getPersonProfessionalData()
                            .getCurrentPersonContractSituationByCategoryType(CategoryType.GRANT_OWNER) : null;
            if (currentGrantOwnerContractSituation != null
                    && currentGrantOwnerContractSituation.getProfessionalCategory() != null && person.hasEmployee()
                    && person.getEmployee().getCurrentWorkingPlace() != null) {
                return true;
            }
        }
        return false;
    }

    private PhdIndividualProgramProcess find(final Set<PhdIndividualProgramProcess> phdIndividualProgramProcesses) {
        PhdIndividualProgramProcess result = null;
        for (final PhdIndividualProgramProcess process : phdIndividualProgramProcesses) {
            if (process.getActiveState() == PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
                if (result != null) {
                    return null;
                }
                result = process;
            }
        }
        return result;
    }

    private String makeStringBlock(String content, int size) {
        int fillerLength = size - content.length();
        if (fillerLength < 0) {
            throw new DomainException("Content is bigger than string block.");
        }
        StringBuilder blockBuilder = new StringBuilder(size);
        blockBuilder.append(content);

        for (int i = 0; i < fillerLength; i++) {
            blockBuilder.append(" ");
        }

        return blockBuilder.toString();
    }

    private String makeZeroPaddedNumber(int number, int size) {
        if (String.valueOf(number).length() > size) {
            throw new DomainException("Number has more digits than allocated room.");
        }
        String format = "%0" + size + "d";
        return String.format(format, number);
    }

    private String purgeString(final String name) {
        if (!StringUtils.isAlphaSpace(name)) {
            final char[] ca = new char[name.length()];
            int j = 0;
            for (int i = 0; i < name.length(); i++) {
                final char c = name.charAt(i);
                if (Character.isLetter(c) || c == ' ') {
                    ca[j++] = c;
                }
            }
            return new String(ca);
        }
        return name;
    }

    private String[] harvestNames(String name) {
        String[] result = new String[3];
        String purgedName = purgeString(name);
        String cleanedName = StringUtils.trimToEmpty(purgedName);
        String[] names = cleanedName.split(" ");
        result[0] = names[0].length() > 15 ? names[0].substring(0, 15) : names[0];
        result[1] = names[names.length - 1].length() > 15 ? names[names.length - 1].substring(0, 15) : names[names.length - 1];
        String midNames = names.length > 2 ? names[1] : "";
        for (int i = 2; i < (names.length - 1); i++) {
            if (midNames.length() + names[i].length() + 1 > 40) {
                break;
            }
            midNames += " ";
            midNames += names[i];
        }
        result[2] = midNames;
        return result;
    }

    private String getHomeCountry(Person person) {
        String countryName = person.getCountryOfBirth().getName();
        if (countryName.length() > 10) {
            return person.getCountryOfBirth().getThreeLetterCode();
        }
        return countryName;
    }

    private String getExpireDate(ExecutionYear year) {
        String result = "";
        int beginYear = year.getBeginCivilYear();
        int endYear = beginYear + 3;
        result = beginYear + "/" + endYear;
        return result;
    }

    private String getDegreeCode(SantanderBatch batch, Person person) {
        final PhdIndividualProgramProcess process = getPhdProcess(person);
        if (process != null) {
            System.out.println("phdProcess: " + process.getExternalId());
            return process.getPhdProgram().getAcronym();
        }
        final Degree degree = getDegree(batch, person);
        return degree == null ? null : degree.getSigla();
    }

    private Degree getDegree(SantanderBatch batch, Person person) {
        final Student student = person.getStudent();
        if (student == null) {
            return null;
        }

        final DateTime begin = batch.getExecutionYear().getBeginDateYearMonthDay().toDateTimeAtMidnight();
        final DateTime end = batch.getExecutionYear().getEndDateYearMonthDay().toDateTimeAtMidnight();
        final Set<StudentCurricularPlan> studentCurricularPlans = new HashSet<StudentCurricularPlan>();
        StudentCurricularPlan pickedSCP;

        for (final Registration registration : student.getRegistrationsSet()) {
            if (!registration.isActive()) {
                continue;
            }
            final RegistrationAgreement registrationAgreement = registration.getRegistrationAgreement();
            if (registrationAgreement != RegistrationAgreement.NORMAL && registrationAgreement != RegistrationAgreement.TOTAL
                    && registrationAgreement != RegistrationAgreement.ANGOLA_TELECOM
                    && registrationAgreement != RegistrationAgreement.MITP) {
                continue;
            }
            final DegreeType degreeType = registration.getDegreeType();
            if (!degreeType.isBolonhaType()) {
                continue;
            }
            for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                if (studentCurricularPlan.isActive()) {
                    if (degreeType == DegreeType.BOLONHA_DEGREE || degreeType == DegreeType.BOLONHA_MASTER_DEGREE
                            || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE
                            || degreeType == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) {
                        studentCurricularPlans.add(studentCurricularPlan);
                    } else {
                        final RegistrationState registrationState = registration.getActiveState();
                        if (registrationState != null) {
                            final DateTime dateTime = registrationState.getStateDate();
                            if (!dateTime.isBefore(begin) && !dateTime.isAfter(end)) {
                                studentCurricularPlans.add(studentCurricularPlan);
                            }
                        }
                    }
                }
            }
        }
        if (studentCurricularPlans.isEmpty()) {
            return null;
        }
        pickedSCP = Collections.max(studentCurricularPlans, new Comparator<StudentCurricularPlan>() {

            @Override
            public int compare(final StudentCurricularPlan o1, final StudentCurricularPlan o2) {
                final DegreeType degreeType1 = o1.getDegreeType();
                final DegreeType degreeType2 = o2.getDegreeType();
                if (degreeType1 == degreeType2) {
                    final YearMonthDay yearMonthDay1 = o1.getStartDateYearMonthDay();
                    final YearMonthDay yearMonthDay2 = o2.getStartDateYearMonthDay();
                    final int c = yearMonthDay1.compareTo(yearMonthDay2);
                    return c == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : c;
                } else {
                    return degreeType1.compareTo(degreeType2);
                }
            }

        });
        return pickedSCP.getRegistration().getDegree();
//        final String degreeNameForIdCard = degree.getIdCardName();
//        if (degreeNameForIdCard == null || degreeNameForIdCard.isEmpty()) {
//            throw new Error("No degree name for id card specified.");
//        }
//        if (degreeNameForIdCard.length() > 50) {
//            throw new Error("Length of degree name for id card to long: " + degreeNameForIdCard + " has more than 50 characters.");
//        }
//        return degreeNameForIdCard;
        //return pickedSCP.getRegistration().getDegree().getSigla();
    }

    private CampusAddress getCampusAddress(Person person, RoleType role) {
        Campus campus = null;
        Map<String, CampusAddress> campi = getCampi();
        switch (role) {
        case STUDENT:
            try {
                campus = person.getStudent().getLastActiveRegistration().getCampus();
            } catch (NullPointerException npe) {
                return null;
            }
            break;
        case EMPLOYEE:
            try {
                campus = person.getEmployee().getCurrentCampus();
            } catch (NullPointerException npe) {
                return null;
            }
            break;
        case TEACHER:
            try {
                campus =
                        person.getPersonProfessionalData().getGiafProfessionalDataByCategoryType(CategoryType.TEACHER)
                                .getCampus();
            } catch (NullPointerException npe) {
                return null;
            }
            break;
        case RESEARCHER:
            try {
                campus =
                        person.getPersonProfessionalData().getGiafProfessionalDataByCategoryType(CategoryType.RESEARCHER)
                                .getCampus();
            } catch (NullPointerException npe) {
                return null;
            }
            break;
        case GRANT_OWNER:
            try {
                campus =
                        person.getPersonProfessionalData().getGiafProfessionalDataByCategoryType(CategoryType.GRANT_OWNER)
                                .getCampus();
            } catch (NullPointerException npe) {
                return null;
            }
            break;
        default:
            break;
        }
        if (campus != null && campus.getExternalId().equals(alamedaOid)) {
            return campi.get("alameda");
        } else if (campus != null && campus.getExternalId().equals(tagusparkOid)) {
            return campi.get("tagus");
        } else if (campus != null && campus.getExternalId().equals(itnOid)) {
            return campi.get("itn");
        } else {
            return null;
        }
    }

    private String createLine(SantanderBatch batch, Person person, RoleType role) {
//        if (SantanderPhotoEntry.getOrCreatePhotoEntryForPerson(person) == null) {
//            return null;
//        }
        StringBuilder strBuilder = new StringBuilder(1505);
        String recordType = "2";

        String idNumber = makeStringBlock(person.getIstUsername(), 10);

        String[] names = harvestNames(person.getName());
        String name = makeStringBlock(names[0], 15);
        String surname = makeStringBlock(names[1], 15);
        String middleNames = makeStringBlock(names[2], 40);

        String degreeCode = makeStringBlock(getDegreeDescription(batch, person, role), 16);
        if (role == RoleType.STUDENT && degreeCode.startsWith(" ")) {
            return null;
        }

        CampusAddress campusAddr = getCampusAddress(person, role);
        if (campusAddr == null) {
            return null;
        }
        String address1 = makeStringBlock(campusAddr.getAddress(), 50);
        String address2 = makeStringBlock((IST_FULL_NAME + (degreeCode == null ? "" : " " + degreeCode)).trim(), 50); //makeStringBlock(getDegreeDescription(batch, person, role), 50);
        String zipCode = campusAddr.getZip();
        String town = makeStringBlock(campusAddr.getTown(), 30);

        String homeCountry = makeStringBlock("", 10);

        String residenceCountry = makeStringBlock(person.getIstUsername(), 10); // As stipulated this field will carry the istId instead.

        String expireDate = getExpireDate(batch.getExecutionYear());

//        if (role == RoleType.STUDENT) {
//            degreeCode = getDegreeCode(batch, person);
//            if (degreeCode == null) {
//                return null;
//            }
//            degreeCode = makeStringBlock(degreeCode, 16);
//        } else if (role == RoleType.GRANT_OWNER) {
//            degreeCode = getDegreeCode(batch, person);
//            if (degreeCode == null) {
//                degreeCode = makeStringBlock("", 16);
//            } else {
//                degreeCode = makeStringBlock(degreeCode, 16);
//            }
//        } else {
//            degreeCode = makeStringBlock("", 16);
//        }

        String backNumber = makeZeroPaddedNumber(Integer.parseInt(person.getIstUsername().substring(3)), 10);

        String chip1 = buildChip1Block(batch, person, role);

        String chip2 = makeStringBlock("", 180);

        String chip3 = makeStringBlock("", 180);

        String chip4 = makeStringBlock("", 180);

        String chip5 = makeStringBlock("", 180);

        String chip6 = makeStringBlock("", 180);

        String filler = makeStringBlock("", 145);

        strBuilder.append(recordType);
        strBuilder.append(idNumber);
        strBuilder.append(name);
        strBuilder.append(surname);
        strBuilder.append(middleNames);
        strBuilder.append(address1);
        strBuilder.append(address2);
        strBuilder.append(zipCode);
        strBuilder.append(town);
        strBuilder.append(homeCountry);
        strBuilder.append(residenceCountry);
        strBuilder.append(expireDate);
        strBuilder.append(degreeCode);
        strBuilder.append(backNumber);
        strBuilder.append(chip1);
        strBuilder.append(chip2);
        strBuilder.append(chip3);
        strBuilder.append(chip4);
        strBuilder.append(chip5);
        strBuilder.append(chip6);
        strBuilder.append(filler);
        strBuilder.append(recordEnd);
        strBuilder.append(lineEnd);

        return strBuilder.toString();
    }

    private String getDegreeDescription(final SantanderBatch batch, final Person person, RoleType roleType) {
        if (roleType == RoleType.STUDENT || roleType == RoleType.GRANT_OWNER) {
            final PhdIndividualProgramProcess process = getPhdProcess(person);
            if (process != null) {
                System.out.println("phdProcess: " + process.getExternalId());
                return process.getPhdProgram().getAcronym();
            }
            final Degree degree = //getDegree(person.getStudent());
                    getDegree(batch, person);
            if (degree != null) {
                return degree.getSigla();
//              final String degreeNameForIdCard = degree.getIdCardName();
//              if (degreeNameForIdCard == null || degreeNameForIdCard.isEmpty()) {
//                  throw new Error("No degree name for id card specified.");
//              }
//              if (degreeNameForIdCard.length() > 50) {
//                  throw new Error("Length of degree name for id card to long: " + degreeNameForIdCard + " has more than 50 characters.");
//              }
//              return degreeNameForIdCard;
                //return pickedSCP.getRegistration().getDegree().getSigla();
            }
        }
        if (roleType == RoleType.TEACHER) {
            final Teacher teacher = person.getTeacher();
            final Department department = teacher.getCurrentWorkingDepartment();
            if (department != null) {
                //return department.getDepartmentUnit().getIdentificationCardLabel();
                return department.getAcronym();
            }
        }
        //return "Técnico Lisboa";
        return "";
    }

    private PhdIndividualProgramProcess getPhdProcess(final Person person) {
        final InsuranceEvent event = person.getInsuranceEventFor(ExecutionYear.readCurrentExecutionYear());
        return event != null && event.isClosed() ? find(person.getPhdIndividualProgramProcesses()) : null;
    }

    private Degree getDegree(final Student student) {
        Degree degree = null;
        for (final Registration registration : student.getRegistrationsSet()) {
            if (registration.isActive()
                    && (degree == null || degree.getDegreeType().ordinal() < registration.getDegreeType().ordinal())) {
                degree = registration.getDegree();
            }
        }
        return degree;
    }

    private String buildChip1Block(SantanderBatch batch, Person person, RoleType role) {
        StringBuilder chip1String = new StringBuilder(185);

        String idNumber = makeZeroPaddedNumber(Integer.parseInt(person.getIstUsername().substring(3)), 10);

        String roleCode = "7";

        /*switch (role) {
        case STUDENT:
            roleCode = "1";
            break;

        case TEACHER:
            roleCode = "2";
            break;

        case EMPLOYEE:
            roleCode = "3";
            break;

        default:
            roleCode = "7";
            break;
        }*/

        String curricularYear = "00";
        String executionYear = "00000000";

        String unit = makeStringBlock("", 11);
        if (role == RoleType.TEACHER) {
            unit = makeStringBlock(person.getTeacher().getCurrentWorkingDepartment().getAcronym(), 11);
        }

        String activeCard = "0";
        String personPin = "0000";
        String institutionPin = "0000";
        String accessContrl = makeStringBlock("", 10);

        String altRoleKey = makeStringBlock("", 5);
        String altRoleCode = " ";
        String altRoleTemplate = "  ";
        String altRoleDescription = makeStringBlock("", 20);
        if (roleCode.equals("7")) {
            switch (role) {
            case STUDENT:
                altRoleKey = "CATEG";
                altRoleCode = "1";
                altRoleTemplate = "02";
                altRoleDescription = makeStringBlock("Estudante/Student", 20);
                break;

            case TEACHER:
                altRoleKey = "CATEG";
                altRoleCode = "2";
                altRoleTemplate = "02";
                altRoleDescription = makeStringBlock("Docente/Faculty", 20);
                break;

            case EMPLOYEE:
                altRoleKey = "CATEG";
                altRoleCode = "3";
                altRoleTemplate = "02";
                altRoleDescription = makeStringBlock("Funcionario/Staff", 20);
                break;

            case RESEARCHER:
                altRoleKey = "CATEG";
                altRoleCode = "8";
                altRoleTemplate = "02";
                altRoleDescription = makeStringBlock("Invest./Researcher", 20);
                break;

            case GRANT_OWNER:
                altRoleKey = "CATEG";
                altRoleCode = "9";
                altRoleTemplate = "02";
                altRoleDescription = makeStringBlock("Bolseiro/Grant Owner", 20);
                break;

            default:
                break;
            }
        }

        String filler = makeStringBlock("", 101);

        chip1String.append(idNumber);
        chip1String.append(roleCode);
        chip1String.append(curricularYear);
        chip1String.append(executionYear);
        chip1String.append(unit);
        chip1String.append(activeCard);
        chip1String.append(personPin);
        chip1String.append(institutionPin);
        chip1String.append(accessContrl);
        chip1String.append(altRoleKey);
        chip1String.append(altRoleCode);
        chip1String.append(altRoleTemplate);
        chip1String.append(altRoleDescription);
        chip1String.append(filler);

        return chip1String.toString();
    }

    private void generateProblem(SantanderBatch batch, Person person, String cause) {
        new SantanderProblem(batch, person, cause);
    }

    private class CampusAddress {
        private final String address;
        private final String zip;
        private final String town;

        private CampusAddress(String address, String zip, String town) {
            this.address = address;
            this.zip = zip;
            this.town = town;
        }

        public String getAddress() {
            return address;
        }

        public String getZip() {
            return zip;
        }

        public String getTown() {
            return town;
        }
    }

    private Map<String, CampusAddress> getCampi() {
        Map<String, CampusAddress> exports = new HashMap<String, CampusAddress>();
        exports.put("alameda", new CampusAddress(alamedaAddr, alamedaZip, alamedaTown));
        exports.put("tagus", new CampusAddress(tagusAddr, tagusZip, tagusTown));
        exports.put("itn", new CampusAddress(itnAddr, itnZip, itnTown));
        return exports;
    }
}
