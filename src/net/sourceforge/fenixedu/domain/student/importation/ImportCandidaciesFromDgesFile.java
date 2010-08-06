package net.sourceforge.fenixedu.domain.student.importation;



/**
 * 
 * @author naat
 * 
 */
public class ImportCandidaciesFromDgesFile {

    // private static final PatternLayout layout = new
    // PatternLayout("%d{dd-MM-yyyy HH:mm:ss.SSS} %m%n");
    //
    // private static final File logsDir = new File(IMPORT_DIR_PATH +
    // "/FirstYearCandidates_logs");
    //
    // private static Integer ALAMEDA_CAMPUS_ID =
    // Campus.readActiveCampusByName(campusName)
    //
    // private static Integer TAGUS_CAMPUS_ID = Integer.valueOf(2178);
    //
    // private static final String ALAMEDA_UNIVERSITY = "A";
    //
    // private static final String TAGUS_UNIVERSITY = "T";
    //
    // private final String alamedaDgesFilename;
    //
    // private final String tagusDgesFilename;
    //
    // private final String executionYearName;
    //
    // private final String entryPhase;
    //
    // private int personsCreated = 0;
    //
    // private FileAppender fileAppender;
    //
    // public static void main(String[] args) {
    // processWriteTransaction(new ImportCandidaciesFromDgesFile(args[0],
    // args[1], args[2], args[3]));
    // System.exit(0);
    // }
    //
    // public ImportCandidaciesFromDgesFile(String alamedaDgesFile, String
    // tagusDgesFilename, String executionYearName,
    // String entryPhase) {
    // this.alamedaDgesFilename = alamedaDgesFile;
    // this.tagusDgesFilename = tagusDgesFilename;
    // this.executionYearName = executionYearName;
    // this.entryPhase = entryPhase;
    // }
    //
    // @Override
    // protected void run() throws Exception {
    //
    // initLoggers();
    //
    // importCandidates(this.alamedaDgesFilename, ALAMEDA_UNIVERSITY,
    // ALAMEDA_CAMPUS_ID);
    // importCandidates(this.tagusDgesFilename, TAGUS_UNIVERSITY,
    // TAGUS_CAMPUS_ID);
    //
    // logger.info("Persons created :" + personsCreated + "\n");
    //
    // destroyLoggers();
    //
    // }
    //
    // private void destroyLoggers() {
    // logger.removeAppender(fileAppender);
    // fileAppender.close();
    // }
    //
    // private void initLoggers() throws IOException {
    // initializeDirs(logsDir);
    // fileAppender = new FileAppender(layout, logsDir.getAbsolutePath() +
    // "/firstYearStudents_"
    // + executionYearName.replace('/', '_') + new
    // DateTime().toString("-dd.MM.yyyy_HH.mm") + ".log");
    // logger.addAppender(fileAppender);
    // }
    //
    // private void importCandidates(final String dgesFilename, final String
    // university, final Integer campusId) {
    //
    // final List<DegreeCandidateDTO> degreeCandidateDTOs =
    // parseDgesFile(dgesFilename, university, this.entryPhase);
    //
    // final Employee employee = Employee.readByNumber(4581);
    // final Campus campus = (Campus)
    // rootDomainObject.readResourceByOID(campusId);
    // final ExecutionYear executionYear =
    // ExecutionYear.readExecutionYearByName(this.executionYearName);
    //
    // logger.info("DGES Entries for " + campus.getName() + ":" +
    // degreeCandidateDTOs.size());
    //
    // createDegreeCandidacies(employee, campus, executionYear,
    // degreeCandidateDTOs);
    // }
    //
    // private void createDegreeCandidacies(final Employee employee, final
    // Campus campus, final ExecutionYear executionYear,
    // final List<DegreeCandidateDTO> degreeCandidateDTOs) {
    // int processed = 0;
    // for (final DegreeCandidateDTO degreeCandidateDTO : degreeCandidateDTOs) {
    //
    // if (processed % 150 == 0) {
    // logger.info("Processed :" + processed);
    // }
    //
    // // logger.info("Processing : \n" + degreeCandidateDTO.toString());
    //
    // final ExecutionDegree executionDegree =
    // getExecutionDegree(degreeCandidateDTO, campus, executionYear);
    // final Person person = initializePerson(degreeCandidateDTO,
    // executionDegree);
    //
    // if (person == null || (person.hasStudent() &&
    // person.getStudent().hasAnyRegistrations()) || person.hasTeacher()) {
    // logger.info("Discarding\t - ID: "
    // + degreeCandidateDTO.getDocumentIdNumber()
    // + " - "
    // + (person == null ? "ERROR MATCHING PERSON, " : "")
    // + (person != null && person.hasStudent() ? "STUDENT: " +
    // person.getStudent().getNumber() + " "
    // + person.getName() + ", " : "")
    // + (person != null && person.hasTeacher() ? "TEACHER: " +
    // person.getTeacher().getTeacherNumber() + " "
    // + person.getName() + ", " : "") + " @ " +
    // executionDegree.getDegreeCurricularPlan().getName()
    // + " (" + campus.getName() + ")");
    //
    // processed++;
    // continue;
    // }
    //
    // if (person.hasRole(RoleType.EMPLOYEE) || person.hasEmployee()) {
    // logger.info("\nLoading employee: " +
    // person.getEmployee().getEmployeeNumber());
    // }
    //
    // person.addPersonRoleByRoleType(RoleType.CANDIDATE);
    //
    // if (!person.hasStudent()) {
    // new Student(person);
    // person.setIstUsername();
    // }
    //
    // final StudentCandidacy studentCandidacy = getCandidacy(employee,
    // degreeCandidateDTO, executionDegree, person);
    // new StandByCandidacySituation(studentCandidacy, employee.getPerson());
    //
    // // logger.info("Created\t - ID: " +
    // // degreeCandidateDTO.getDocumentIdNumber() + " - STUDENT: "
    // // + person.getStudent().getNumber() + " @ " +
    // // executionDegree.getDegreeCurricularPlan().getName());
    //
    // processed++;
    // }
    // }
    //
    // private StudentCandidacy getCandidacy(final Employee employee, final
    // DegreeCandidateDTO degreeCandidateDTO,
    // final ExecutionDegree executionDegree, final Person person) {
    //
    // final StudentCandidacy candidacy = createCandidacy(employee,
    // degreeCandidateDTO, executionDegree, person);
    // candidacy.setHighSchoolType(degreeCandidateDTO.getHighSchoolType());
    // candidacy.setFirstTimeCandidacy(true);
    // createPrecedentDegreeInformation(candidacy, degreeCandidateDTO);
    //
    // return candidacy;
    //
    // }
    //
    // private StudentCandidacy createCandidacy(final Employee employee, final
    // DegreeCandidateDTO degreeCandidateDTO,
    // final ExecutionDegree executionDegree, final Person person) {
    //
    // if (executionDegree.getDegree().getDegreeType() ==
    // DegreeType.BOLONHA_DEGREE) {
    // return new DegreeCandidacy(person, executionDegree, employee.getPerson(),
    // degreeCandidateDTO.getEntryGrade(),
    // degreeCandidateDTO.getContigent(), degreeCandidateDTO.getIngression(),
    // degreeCandidateDTO.getEntryPhase(),
    // degreeCandidateDTO.getPlacingOption());
    //
    // } else if (executionDegree.getDegree().getDegreeType() ==
    // DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
    // return new IMDCandidacy(person, executionDegree, employee.getPerson(),
    // degreeCandidateDTO.getEntryGrade(),
    // degreeCandidateDTO.getContigent(), degreeCandidateDTO.getIngression(),
    // degreeCandidateDTO.getEntryPhase(),
    // degreeCandidateDTO.getPlacingOption());
    //
    // }
    //
    // throw new RuntimeException("Unexpected degree type from DGES file");
    //
    // }
    //
    // private void createPrecedentDegreeInformation(final StudentCandidacy
    // studentCandidacy,
    // final DegreeCandidateDTO degreeCandidateDTO) {
    //
    // final PrecedentDegreeInformation precedentDegreeInformation = new
    // PrecedentDegreeInformation(studentCandidacy);
    //
    // precedentDegreeInformation.setConclusionGrade(degreeCandidateDTO.getHighSchoolFinalGrade());
    // precedentDegreeInformation.setDegreeDesignation(degreeCandidateDTO.getHighSchoolDegreeDesignation());
    //
    // precedentDegreeInformation.setInstitution(UnitUtils.readExternalInstitutionUnitByName(degreeCandidateDTO
    // .getHighSchoolName()));
    // }
    //
    // private ExecutionDegree getExecutionDegree(final DegreeCandidateDTO
    // degreeCandidateDTO, final Campus campus,
    // final ExecutionYear executionYear) {
    // return
    // ExecutionDegree.readByDegreeCodeAndExecutionYearAndCampus(degreeCandidateDTO.getDegreeCode(),
    // executionYear,
    // campus);
    // }
    //
    // private Person initializePerson(final DegreeCandidateDTO
    // degreeCandidateDTO, ExecutionDegree executionDegree) {
    // Collection<Person> persons =
    // Person.readByDocumentIdNumber(degreeCandidateDTO.getDocumentIdNumber());
    // if (persons.isEmpty()) {
    // personsCreated++;
    // return createPerson(degreeCandidateDTO);
    // } else if (persons.size() == 1) {
    // final Person person = persons.iterator().next();
    // if ((person.getDateOfBirthYearMonthDay() != null &&
    // person.getDateOfBirthYearMonthDay().equals(
    // degreeCandidateDTO.getDateOfBirth()))
    // || (person.getName().equals(degreeCandidateDTO.getName()))) {
    //
    // if (!person.hasTeacher() && !person.hasStudent() &&
    // person.hasAnyCandidacies()) {
    // checkCandidacies(executionDegree, person);
    // }
    //
    // return person;
    // }
    // }
    //
    // return null;
    // }
    //
    // private void checkCandidacies(ExecutionDegree executionDegree, final
    // Person person) {
    // for (Candidacy candidacy : person.getCandidacies()) {
    // if (candidacy instanceof StudentCandidacy) {
    // StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
    // if (studentCandidacy.getExecutionDegree().getExecutionYear() ==
    // executionDegree.getExecutionYear()
    // && !studentCandidacy.isConcluded()) {
    // studentCandidacy.cancelCandidacy();
    // }
    // }
    // }
    // }
    //
    // private Person createPerson(DegreeCandidateDTO degreeCandidateDTO) {
    //
    // final Person person = new Person(degreeCandidateDTO.getName(),
    // degreeCandidateDTO.getGender(), degreeCandidateDTO
    // .getDocumentIdNumber(), IDDocumentType.IDENTITY_CARD);
    //
    // person.setPassword(PasswordEncryptor.encryptPassword(GeneratePassword.getInstance().generatePassword(person)));
    // person.setMaritalStatus(MaritalStatus.SINGLE);
    // person.setDateOfBirthYearMonthDay(degreeCandidateDTO.getDateOfBirth());
    //
    // PhysicalAddress.createPhysicalAddress(person, new
    // PhysicalAddressData(degreeCandidateDTO.getAddress(), degreeCandidateDTO
    // .getAreaCode(), degreeCandidateDTO.getAreaOfAreaCode(), null),
    // PartyContactType.PERSONAL, true);
    //
    // Phone.createPhone(person, degreeCandidateDTO.getPhoneNumber(),
    // PartyContactType.PERSONAL, true);
    //
    // return person;
    // }
    //
    // private void initializeDirs(File dir) {
    // if (!dir.exists()) {
    // dir.mkdirs();
    // }
    // }

}