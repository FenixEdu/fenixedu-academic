package net.sourceforge.fenixedu.applicationTier.Servico.manager.semesterInitialization;

import java.util.ArrayList;
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
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class CollectCurrentYearInformation {

    private static final Logger logger = Logger.getLogger(CollectCurrentYearInformation.class);

    static {
        logger.addAppender(new ConsoleAppender(new PatternLayout("%m%n")));
        logger.setAdditivity(false);
    }

    final ISuportePersistente persistentSupport;

    final Set<String> curricularCourseCodes;

    final IPersistentExecutionPeriod persistentExecutionPeriod;

    final IPersistentExecutionCourse persistentExecutionCourse;

    final ITurnoPersistente persistentShift;

    final IAulaPersistente persistentLesson;

    final ITurmaPersistente persistentSchoolClass;

    final Map<String, ISchoolClass> schoolClassMap = new HashMap<String, ISchoolClass>();

    private CollectCurrentYearInformation(final ISuportePersistente persistentSupport,
            final Set<String> curricularCourseCodes) {
        super();
        this.persistentSupport = persistentSupport;
        this.curricularCourseCodes = curricularCourseCodes;
        this.persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        this.persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        this.persistentShift = persistentSupport.getITurnoPersistente();
        this.persistentLesson = persistentSupport.getIAulaPersistente();
        this.persistentSchoolClass = persistentSupport.getITurmaPersistente();
    }

    private void initializeNextExecutionPeriod(Integer semester, String executionYear)
            throws ExcepcaoPersistencia {
        final IExecutionPeriod executionPeriod = persistentExecutionPeriod
                .readBySemesterAndExecutionYear(semester, executionYear);
        final IExecutionPeriod newExecutionPeriod = executionPeriod.getNextExecutionPeriod()
                .getNextExecutionPeriod();

        initializeSchoolClasses(newExecutionPeriod, executionPeriod);

        final List<IExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        for (final IExecutionCourse executionCourse : executionCourses) {
            if (scheduleIsToBeCreated(executionCourse)) {
                final IExecutionCourse newExecutionCourse = createExecutionCourse(newExecutionPeriod,
                        executionCourse);
                final List<ICurricularCourse> curricularCourses = executionCourse
                        .getAssociatedCurricularCourses();
                for (final ICurricularCourse curricularCourse : curricularCourses) {
                    addCurricularCourse(newExecutionCourse, curricularCourse);
                }
                if (!newExecutionCourse.getAssociatedCurricularCourses().isEmpty()) {
                    // persistentExecutionCourse.simpleLockWrite(newExecutionCourse);
                    createSchedule(newExecutionCourse, executionCourse);
                }
            } else {
                final List<ICurricularCourse> curricularCourses = executionCourse
                        .getAssociatedCurricularCourses();
                for (final ICurricularCourse curricularCourse : curricularCourses) {
                    final IExecutionCourse newExecutionCourse = createExecutionCourse(
                            newExecutionPeriod, executionCourse);
                    addCurricularCourse(newExecutionCourse, curricularCourse);
                    if (!newExecutionCourse.getAssociatedCurricularCourses().isEmpty()) {
                        // persistentExecutionCourse.simpleLockWrite(newExecutionCourse);
                    }
                }
            }
        }
    }

    private void initializeSchoolClasses(final IExecutionPeriod newExecutionPeriod,
            final IExecutionPeriod executionPeriod) {
        final List<ISchoolClass> schoolClasses = executionPeriod.getSchoolClasses();
        for (final ISchoolClass schoolClass : schoolClasses) {
            final IExecutionDegree executionDegree = schoolClass.getExecutionDegree();
            final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final IDegree degree = degreeCurricularPlan.getDegree();

            if (degree.getTipoCurso().equals(DegreeType.DEGREE)) {
                final IExecutionDegree newExecutionDegree = findExecutionDegree(degreeCurricularPlan,
                        newExecutionPeriod.getExecutionYear());

                final ISchoolClass newSchoolClass = new SchoolClass();
                newSchoolClass.setAnoCurricular(schoolClass.getAnoCurricular());
                newSchoolClass.setAssociatedShifts(new ArrayList());
                newSchoolClass.setExecutionDegree(newExecutionDegree);
                newSchoolClass.setExecutionPeriod(newExecutionPeriod);
                newSchoolClass.setNome(schoolClass.getNome());
                newSchoolClass.setSchoolClassShifts(new ArrayList());

                logger.info("Creating SchoolClass: " + schoolClass.getNome());

                schoolClassMap.put(newSchoolClass.getNome(), newSchoolClass);
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
        return null;
    }

    private void createSchedule(final IExecutionCourse newExecutionCourse,
            final IExecutionCourse executionCourse) {
        for (final IShift shift : (List<IShift>) executionCourse.getAssociatedShifts()) {
            for (final ISchoolClass schoolClass : (List<ISchoolClass>) shift.getAssociatedClasses()) {
            }
            for (final ILesson lesson : (List<ILesson>) shift.getAssociatedLessons()) {
            }
        }
    }

    private void addCurricularCourse(final IExecutionCourse executionCourse,
            final ICurricularCourse curricularCourse) {
        if (curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                DegreeType.DEGREE)
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

    private boolean scheduleIsToBeCreated(final IExecutionCourse executionCourse) {
        final List<ICurricularCourse> curricularCourses = executionCourse
                .getAssociatedCurricularCourses();
        for (final ICurricularCourse curricularCourse : curricularCourses) {
            if (curricularCourseCodes.contains(curricularCourse.getCode())) {
                return false;
            }
        }
        return true;
    }

    private IExecutionCourse createExecutionCourse(final IExecutionPeriod newExecutionPeriod,
            final IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
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

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        logger.info("Hello world.");

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
            final CollectCurrentYearInformation instance = new CollectCurrentYearInformation(
                    persistentSupport, curricularCourseCodes);
            instance.initializeNextExecutionPeriod(new Integer(1), "2004/2005");
            persistentSupport.confirmarTransaccao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long end = System.currentTimeMillis();

        logger.info("Process terminated in: " + (end - start) + "ms.");
        System.exit(0);
    }

}