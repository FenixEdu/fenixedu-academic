package net.sourceforge.fenixedu.applicationTier.Servico.manager.semesterInitialization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.StringAppender;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class InitializeExecutionPeriod {

    private static final Logger logger = Logger.getLogger(InitializeExecutionPeriod.class);

    private static final Set DEGREE_TYPES_TO_CREATE = new HashSet(1);

    static {
        logger.addAppender(new ConsoleAppender(new PatternLayout("%m%n")));
        logger.setAdditivity(false);

        DEGREE_TYPES_TO_CREATE.add(DegreeType.DEGREE);
    }

    final ISuportePersistente persistentSupport;

    final IPersistentObject persistentObject;

    final IPersistentExecutionPeriod persistentExecutionPeriod;

    final IPersistentExecutionCourse persistentExecutionCourse;

    final ITurnoPersistente persistentShift;

    final IAulaPersistente persistentLesson;

    final ITurmaPersistente persistentSchoolClass;

    final Set<String> curricularCourseCodes;

    final Map<String, ISchoolClass> schoolClassMap = new HashMap<String, ISchoolClass>();

    int schoolClassCounter = 0;

    int processedschoolClass = 0;

    int executionCourseCounter = 0;

    int processedExecutionCourses = 0;

    int shiftCounter = 0;

    int processedShifts = 0;

    int lessonCounter = 0;

    int roomOccupationCounter = 0;

    private InitializeExecutionPeriod(final ISuportePersistente persistentSupport,
            final Set<String> curricularCourseCodes) {
        super();
        this.persistentSupport = persistentSupport;
        this.persistentObject = persistentSupport.getIPersistentObject();
        this.persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        this.persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        this.persistentShift = persistentSupport.getITurnoPersistente();
        this.persistentLesson = persistentSupport.getIAulaPersistente();
        this.persistentSchoolClass = persistentSupport.getITurmaPersistente();
        this.curricularCourseCodes = curricularCourseCodes;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        logger.info("Only the following DegreeTypes will be considered: "
                + DEGREE_TYPES_TO_CREATE.toString());

        verifyArguments(args);

        final Integer semester = new Integer(args[0]);
        final String executionYear = args[1];

        final Set<String> curricularCourseCodes = new HashSet<String>(args.length);
        for (int i = 2; i < args.length; i++) {
            curricularCourseCodes.add(args[i]);
        }

        try {
            final ISuportePersistente persistentSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();
            persistentSupport.iniciarTransaccao();
            final InitializeExecutionPeriod instance = new InitializeExecutionPeriod(persistentSupport,
                    curricularCourseCodes);
            instance.initializeNextExecutionPeriod(semester, executionYear);
            persistentSupport.confirmarTransaccao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long end = System.currentTimeMillis();

        logger.info("Process terminated in: " + (end - start) + "ms.");
        System.exit(0);
    }

    private static void verifyArguments(final String[] args) {
        if (args.length < 2) {
            logger
                    .fatal("Usage: <Semester> <ExecutionYear> [<CurricularCourse Code> ... <CurricularCourse Code>]");
            throw new IllegalArgumentException();
        }
        if (!StringUtils.isNumeric(args[0])) {
            logger
                    .fatal("Usage: <Semester> <ExecutionYear> [<CurricularCourse Code> ... <CurricularCourse Code>]");
            logger.fatal("       <Semester> must be a number.");
            throw new IllegalArgumentException();
        }
        if (!StringUtils.isNumeric(args[1].substring(0, 4))
                || !StringUtils.isNumeric(args[1].substring(5, 9)) || args[1].charAt(4) != '/') {
            logger
                    .fatal("Usage: <Semester> <ExecutionYear> [<CurricularCourse Code> ... <CurricularCourse Code>]");
            logger.fatal("       <ExecutionYear> has invalid format");
            throw new IllegalArgumentException();
        }
    }

    private void persist(final IDomainObject domainObject) throws ExcepcaoPersistencia {
        // persistentObject.simpleLockWrite(domainObject);
    }

    private void initializeNextExecutionPeriod(final Integer semester, String executionYear)
            throws ExcepcaoPersistencia {
        final IExecutionPeriod executionPeriod = persistentExecutionPeriod
                .readBySemesterAndExecutionYear(semester, executionYear);
        final IExecutionPeriod newExecutionPeriod = executionPeriod.getNextExecutionPeriod()
                .getNextExecutionPeriod();

        logger.info(StringAppender.append("Initializing period [semester ", newExecutionPeriod
                .getSemester().toString(), ", ", newExecutionPeriod.getExecutionYear().getYear(),
                "] from period [semester ", executionPeriod.getSemester().toString(), ", ",
                executionPeriod.getExecutionYear().getYear(), "]."));

        createSchoolClasses(newExecutionPeriod, executionPeriod);

        createExecutionCourses(newExecutionPeriod, executionPeriod);

        persistSchoolClasses();

        report();
    }

    private void createSchoolClasses(final IExecutionPeriod newExecutionPeriod,
            final IExecutionPeriod executionPeriod) {
        final List<ISchoolClass> schoolClasses = executionPeriod.getSchoolClasses();
        Collections.sort(schoolClasses, new BeanComparator("nome"));
        for (final ISchoolClass schoolClass : schoolClasses) {
            final IExecutionDegree executionDegree = schoolClass.getExecutionDegree();
            final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final IDegree degree = degreeCurricularPlan.getDegree();

            if (DEGREE_TYPES_TO_CREATE.contains(degree.getTipoCurso())) {
                final IExecutionDegree newExecutionDegree = findExecutionDegree(degreeCurricularPlan,
                        newExecutionPeriod.getExecutionYear());

                if (newExecutionDegree != null) {
                    processedschoolClass++;

                    final ISchoolClass newSchoolClass = new SchoolClass();
                    newSchoolClass.setAnoCurricular(schoolClass.getAnoCurricular());
                    newSchoolClass.setAssociatedShifts(new ArrayList());
                    newSchoolClass.setExecutionDegree(newExecutionDegree);
                    newExecutionDegree.getSchoolClasses().add(newSchoolClass);
                    newSchoolClass.setExecutionPeriod(newExecutionPeriod);
                    newExecutionPeriod.getSchoolClasses().add(newSchoolClass);
                    newSchoolClass.setNome(schoolClass.getNome());
                    newSchoolClass.setSchoolClassShifts(new ArrayList());

                    schoolClassMap.put(newSchoolClass.getNome(), newSchoolClass);
                }
            }
        }
    }

    private void persistSchoolClasses() throws ExcepcaoPersistencia {
        for (final ISchoolClass schoolClass : schoolClassMap.values()) {
            if (!schoolClass.getAssociatedShifts().isEmpty()) {
                schoolClassCounter++;
                persist(schoolClass);
            }
        }
    }

    private IExecutionDegree findExecutionDegree(final IDegreeCurricularPlan degreeCurricularPlan,
            final IExecutionYear executionYear) {
        for (final IExecutionDegree executionDegree : (List<IExecutionDegree>) degreeCurricularPlan
                .getExecutionDegrees()) {
            if (executionYear.getIdInternal().equals(executionDegree.getExecutionYear().getIdInternal())) {
                return executionDegree;
            }
        }
        logger.error(StringAppender.append("No execution degree found for: ", degreeCurricularPlan
                .getName(), " ", executionYear.getYear()));
        return null;
    }

    private void createExecutionCourses(final IExecutionPeriod newExecutionPeriod,
            final IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        final List<IExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        for (final IExecutionCourse executionCourse : executionCourses) {
            processedExecutionCourses++;
            createExecutionCourse(newExecutionPeriod, executionPeriod, executionCourse);
        }
    }

    private void createExecutionCourse(final IExecutionPeriod newExecutionPeriod,
            final IExecutionPeriod executionPeriod, final IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        if (keepAggregate(executionCourse.getAssociatedCurricularCourses())) {
            final IExecutionCourse newExecutionCourse = createExecutionCourse(newExecutionPeriod,
                    executionCourse);
            executionCourseCounter++;
            final List<ICurricularCourse> curricularCourses = executionCourse
                    .getAssociatedCurricularCourses();
            for (final ICurricularCourse curricularCourse : curricularCourses) {
                addCurricularCourse(newExecutionCourse, curricularCourse);
            }
            if (!newExecutionCourse.getAssociatedCurricularCourses().isEmpty()) {
                persist(newExecutionCourse);
                createSchedule(newExecutionCourse, executionCourse);
            }
        } else {
            final List<ICurricularCourse> curricularCourses = executionCourse
                    .getAssociatedCurricularCourses();
            int i = 0;
            for (final ICurricularCourse curricularCourse : curricularCourses) {
                final IExecutionCourse newExecutionCourse = createExecutionCourse(newExecutionPeriod,
                        executionCourse);
                executionCourseCounter++;
                newExecutionCourse.setSigla(executionCourse.getSigla() + "_" + i++);
                addCurricularCourse(newExecutionCourse, curricularCourse);
                if (!newExecutionCourse.getAssociatedCurricularCourses().isEmpty()) {
                    persist(newExecutionCourse);
                }
            }
        }
    }

    private boolean keepAggregate(final List<ICurricularCourse> curricularCourses) {
        for (final ICurricularCourse curricularCourse : curricularCourses) {
            if (curricularCourseCodes.contains(curricularCourse.getCode())) {
                return false;
            }
        }
        return true;
    }

    private IExecutionCourse createExecutionCourse(final IExecutionPeriod newExecutionPeriod,
            final IExecutionCourse executionCourse) {
        final IExecutionCourse newExecutionCourse = new ExecutionCourse();

        newExecutionCourse.setAssociatedBibliographicReferences(new ArrayList(executionCourse
                .getAssociatedBibliographicReferences()));
        newExecutionCourse.setAssociatedCurricularCourses(new ArrayList());
        newExecutionCourse.setAssociatedEvaluations(new ArrayList(0));
        newExecutionCourse.setAssociatedExams(new ArrayList(0));
        newExecutionCourse.setAssociatedShifts(new ArrayList(0));
        newExecutionCourse.setAssociatedSummaries(new ArrayList(0));
        newExecutionCourse.setAttendingStudents(new ArrayList(0));
        newExecutionCourse.setAttends(new ArrayList(0));
        newExecutionCourse.setComment(new String());
        newExecutionCourse.setCourseReport(null);
        newExecutionCourse.setEvaluationExecutionCourses(new ArrayList(0));
        newExecutionCourse.setEvaluationMethod(null);
        newExecutionCourse.setExecutionCourseProperties(new ArrayList(0));
        newExecutionCourse.setExecutionPeriod(newExecutionPeriod);
        newExecutionPeriod.getAssociatedExecutionCourses().add(newExecutionCourse);
        newExecutionCourse.setGroupPropertiesExecutionCourse(new ArrayList(0));
        newExecutionCourse.setGroupPropertiesSenderExecutionCourse(new ArrayList(0));
        newExecutionCourse.setLabHours(executionCourse.getLabHours());
        newExecutionCourse.setNome(executionCourse.getNome());
        newExecutionCourse.setNonAffiliatedTeachers(new ArrayList(0));
        newExecutionCourse.setPraticalHours(executionCourse.getPraticalHours());
        newExecutionCourse.setProfessorships(new ArrayList(0));
        newExecutionCourse.setResponsibleTeachers(new ArrayList(0));
        newExecutionCourse.setSigla(executionCourse.getSigla());
        newExecutionCourse.setSite(executionCourse.getSite());
        newExecutionCourse.setTheoPratHours(executionCourse.getTheoPratHours());
        newExecutionCourse.setTheoreticalHours(executionCourse.getTheoreticalHours());

        return newExecutionCourse;
    }

    private void addCurricularCourse(final IExecutionCourse executionCourse,
            final ICurricularCourse curricularCourse) {
        if (DEGREE_TYPES_TO_CREATE.contains(curricularCourse.getDegreeCurricularPlan().getDegree()
                .getTipoCurso())
                && hasOpenScope(executionCourse.getExecutionPeriod(), curricularCourse)) {
            executionCourse.getAssociatedCurricularCourses().add(curricularCourse);
            curricularCourse.getAssociatedExecutionCourses().add(executionCourse);
        }
    }

    private boolean hasOpenScope(final IExecutionPeriod executionPeriod,
            final ICurricularCourse curricularCourse) {
        for (final ICurricularCourseScope curricularCourseScope : ((List<ICurricularCourseScope>) curricularCourse
                .getScopes())) {
            if (curricularCourseScope.getCurricularSemester().getSemester().equals(
                    executionPeriod.getSemester())
                    && (executionPeriod.getEndDate().getTime() > curricularCourseScope.getBeginDate()
                            .getTimeInMillis())
                    && (curricularCourseScope.getEndDate() == null || executionPeriod.getBeginDate()
                            .getTime() < curricularCourseScope.getEndDate().getTimeInMillis())) {
                return true;
            }
        }
        return false;
    }

    private void createSchedule(final IExecutionCourse newExecutionCourse,
            final IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        for (final IShift shift : (List<IShift>) executionCourse.getAssociatedShifts()) {
            createShift(shift, newExecutionCourse);
        }
    }

    private void createShift(final IShift shift, final IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        final IShift newShift = new Shift();
        newShift.setAssociatedClasses(new ArrayList());
        newShift.setAssociatedLessons(new ArrayList());
        newShift.setAssociatedShiftProfessorship(new ArrayList());
        newShift.setAssociatedStudentGroups(new ArrayList());
        newShift.setAssociatedSummaries(new ArrayList());
        newShift.setAvailabilityFinal(shift.getLotacao());
        newShift.setDisciplinaExecucao(executionCourse);
        newShift.setLotacao(shift.getLotacao());
        newShift.setNome(shift.getNome());
        newShift.setOcupation(Integer.valueOf(0));
        newShift.setPercentage(Double.valueOf(0));
        // newShift.setSchoolClassShifts(new ArrayList());
        newShift.setStudentShifts(new ArrayList());
        newShift.setTipo(shift.getTipo());

        processedShifts++;

        for (final ISchoolClass schoolClass : (List<ISchoolClass>) shift.getAssociatedClasses()) {
            final ISchoolClass newSchoolClass = schoolClassMap.get(schoolClass.getNome());
            addSchoolClass(newShift, newSchoolClass);
        }

        if (!newShift.getAssociatedClasses().isEmpty()) {
            shiftCounter++;

            for (final ILesson lesson : (List<ILesson>) shift.getAssociatedLessons()) {
                createLesson(lesson, newShift);
            }

            executionCourse.getAssociatedShifts().add(newShift);
            persist(newShift);
        }

    }

    private void addSchoolClass(final IShift shift, final ISchoolClass schoolClass) {
        if (schoolClass != null) {
            shift.getAssociatedClasses().add(schoolClass);
            schoolClass.getAssociatedShifts().add(shift);
        }
    }

    private void createLesson(final ILesson lesson, final IShift newShift) throws ExcepcaoPersistencia {
        final ILesson newLesson = new Lesson();
        newLesson.setDiaSemana(lesson.getDiaSemana());
        final IExecutionPeriod executionPeriod = newShift.getDisciplinaExecucao().getExecutionPeriod();
        newLesson.setExecutionPeriod(executionPeriod);
        executionPeriod.getLessons().add(newLesson);
        newLesson.setFim(lesson.getFim());
        newLesson.setInicio(lesson.getInicio());
        final IRoom room = lesson.getSala();
        newLesson.setSala(room);
        room.getAssociatedLessons().add(newLesson);
        newLesson.setShift(newShift);
        newShift.getAssociatedLessons().add(newLesson);
        newLesson.setTipo(lesson.getTipo());
        createRoomOccupation(lesson.getRoomOccupation(), newLesson);

        lessonCounter++;

        persist(newLesson);
    }

    private IRoomOccupation createRoomOccupation(final IRoomOccupation roomOccupation,
            final ILesson lesson) throws ExcepcaoPersistencia {
        final IRoomOccupation newRoomOccupation = new RoomOccupation();
        newRoomOccupation.setDayOfWeek(roomOccupation.getDayOfWeek());
        newRoomOccupation.setEndTime(roomOccupation.getEndTime());
        newRoomOccupation.setFrequency(roomOccupation.getFrequency());
        newRoomOccupation.setLesson(lesson);
        lesson.setRoomOccupation(roomOccupation);

        final IPeriod period = getPeriod(lesson);
        newRoomOccupation.setPeriod(period);
        period.getRoomOccupations().add(roomOccupation);

        final IRoom room = roomOccupation.getRoom();
        newRoomOccupation.setRoom(room);
        room.getRoomOccupations().add(roomOccupation);
        newRoomOccupation.setStartTime(roomOccupation.getStartTime());
        newRoomOccupation.setWeekOfQuinzenalStart(roomOccupation.getWeekOfQuinzenalStart());
        newRoomOccupation.setWrittenEvaluation(null);

        roomOccupationCounter++;

        persist(newRoomOccupation);

        return newRoomOccupation;
    }

    private IPeriod getPeriod(final ILesson lesson) {
        final ISchoolClass schoolClass = ((ISchoolClass) lesson.getShift().getAssociatedClasses().get(0));
        final IExecutionDegree executionDegree = schoolClass.getExecutionDegree();

        if (schoolClass.getExecutionPeriod().getSemester().intValue() == 1) {
            return executionDegree.getPeriodLessonsFirstSemester();
        }
        if (schoolClass.getExecutionPeriod().getSemester().intValue() == 2) {
            return executionDegree.getPeriodLessonsSecondSemester();
        }
        return null;
    }

    private void report() {
        StringBuilder stringBuilder = new StringBuilder();

        // int schoolClassCounter = 0;
        // int executionCourseCounter = 0;
        // int shiftCounter = 0;
        // int lessonCounter = 0;
        // int roomOccupationCounter = 0;

        stringBuilder.append("Processed:");
        stringBuilder.append("\n\tSchoolClasses: ");
        stringBuilder.append(processedschoolClass);
        stringBuilder.append("\n\tExecutionCourses: ");
        stringBuilder.append(processedExecutionCourses);
        stringBuilder.append("\n\tShifts: ");
        stringBuilder.append(processedShifts);
        stringBuilder.append("\n\nCreated:");
        stringBuilder.append("\n\tSchoolClasses: ");
        stringBuilder.append(schoolClassCounter);
        stringBuilder.append("\n\tExecutionCourses: ");
        stringBuilder.append(executionCourseCounter);
        stringBuilder.append("\n\tShifts: ");
        stringBuilder.append(shiftCounter);
        stringBuilder.append("\n\tLessons: ");
        stringBuilder.append(lessonCounter);
        stringBuilder.append("\n\tRoomOccupations: ");
        stringBuilder.append(roomOccupationCounter);

        logger.info(stringBuilder.toString());
    }

}