package net.sourceforge.fenixedu.applicationTier.Servico.manager.semesterInitialization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.StringAppender;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class InitializeExecutionPeriod {

    private static final Logger logger = Logger.getLogger(InitializeExecutionPeriod.class);

    private static final Set<DegreeType> DEGREE_TYPES_TO_CREATE = new HashSet<DegreeType>(1);

    static {
        logger.addAppender(new ConsoleAppender(new PatternLayout("%m%n")));
        logger.setAdditivity(false);

        DEGREE_TYPES_TO_CREATE.add(DegreeType.DEGREE);
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

        ISuportePersistente persistentSupport = null;
        try {
        	SuportePersistenteOJB.fixDescriptors();
            persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentSupport.iniciarTransaccao();
            initializeNextExecutionPeriod(semester, executionYear, curricularCourseCodes);
            persistentSupport.confirmarTransaccao();
        } catch (Exception e) {
            try {
                persistentSupport.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e1) {
                throw new RuntimeException(e1.getMessage(), e);
            }
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

    private static void initializeNextExecutionPeriod(final Integer semester, final String executionYear, final Set<String> curricularCourseCodes)
            throws ExcepcaoPersistencia {
    	final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
    	final IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();

        final ExecutionPeriod executionPeriod = persistentExecutionPeriod.readBySemesterAndExecutionYear(semester, executionYear);
        final ExecutionPeriod newExecutionPeriod = executionPeriod.getNextExecutionPeriod().getNextExecutionPeriod();

        logger.info(StringAppender.append("Initializing period [semester ", newExecutionPeriod
                .getSemester().toString(), ", ", newExecutionPeriod.getExecutionYear().getYear(),
                "] from period [semester ", executionPeriod.getSemester().toString(), ", ",
                executionPeriod.getExecutionYear().getYear(), "]."));

        final Map<ExecutionDegree, ExecutionDegree> executionDegreeMap = constructExecutionDegreeCorrespondenceMap(executionPeriod, newExecutionPeriod);
        final Map<SchoolClass, SchoolClass> schoolClassMap = createSchoolClasses(executionDegreeMap, newExecutionPeriod);
        createExecutionCourses(curricularCourseCodes, schoolClassMap, executionPeriod, newExecutionPeriod);
    }

    private static void createExecutionCourses(final Set<String> curricularCourseCodes, final Map<SchoolClass, SchoolClass> schoolClassMap,
    		final ExecutionPeriod executionPeriod, final ExecutionPeriod newExecutionPeriod) {
    	for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
    		if (keepAggregate(curricularCourseCodes, executionCourse.getAssociatedCurricularCourses())) {
                final ExecutionCourse newExecutionCourse = createExecutionCourse(newExecutionPeriod, executionCourse);
                final List<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
                for (final CurricularCourse curricularCourse : curricularCourses) {
                    addCurricularCourse(newExecutionCourse, curricularCourse);
                }
                if (!newExecutionCourse.getAssociatedCurricularCourses().isEmpty()) {
                    createSchedule(schoolClassMap, newExecutionCourse, executionCourse);
                }
            } else {
                final List<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
                int i = 0;
                for (final CurricularCourse curricularCourse : curricularCourses) {
                    final ExecutionCourse newExecutionCourse = createExecutionCourse(newExecutionPeriod, executionCourse);
                    newExecutionCourse.setSigla(executionCourse.getSigla() + "_" + i++);
                    addCurricularCourse(newExecutionCourse, curricularCourse);
                }
    		}
    	}
	}

	private static Map<SchoolClass, SchoolClass> createSchoolClasses(final Map<ExecutionDegree, ExecutionDegree> executionDegreeMap,
    		final ExecutionPeriod newEecutionPeriod) {
    	final Map<SchoolClass, SchoolClass> schoolClassMap = new HashMap<SchoolClass, SchoolClass>();
    	for (final Entry<ExecutionDegree, ExecutionDegree> entry : executionDegreeMap.entrySet()) {
    		final ExecutionDegree executionDegree = entry.getKey();
    		final Degree degree = executionDegree.getDegreeCurricularPlan().getDegree();
    		if (DEGREE_TYPES_TO_CREATE.contains(degree.getTipoCurso())) {
    			for (final SchoolClass schoolClass : executionDegree.getSchoolClasses()) {
    				if (schoolClass.getExecutionPeriod().getSemester().equals(newEecutionPeriod.getSemester())) {
    					final SchoolClass newSchoolClass = new SchoolClass();
    					newSchoolClass.setAnoCurricular(schoolClass.getAnoCurricular());
    					newSchoolClass.setExecutionDegree(entry.getValue());
    					newSchoolClass.setExecutionPeriod(newEecutionPeriod);
    					newSchoolClass.setNome(schoolClass.getNome());

    					schoolClassMap.put(schoolClass, newSchoolClass);
    				}
    			}
    		}
    	}
    	return schoolClassMap;
    }

	private static Map<ExecutionDegree, ExecutionDegree> constructExecutionDegreeCorrespondenceMap(
    		final ExecutionPeriod executionPeriod, final ExecutionPeriod newExecutionPeriod) {
    	final Map<ExecutionDegree, ExecutionDegree> executionDegreeMap = new HashMap<ExecutionDegree, ExecutionDegree>();
    	for (final ExecutionDegree executionDegree : executionPeriod.getExecutionYear().getExecutionDegrees()) {
    		final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
    		final ExecutionDegree correspondingExecutionDegree = findExecutionDegree(
    				newExecutionPeriod.getExecutionYear().getExecutionDegrees(), degreeCurricularPlan);
    		if (correspondingExecutionDegree != null) {
    			executionDegreeMap.put(executionDegree, correspondingExecutionDegree);
    		}
    	}
		return executionDegreeMap;
	}

	private static ExecutionDegree findExecutionDegree(
			final List<ExecutionDegree> executionDegrees, final DegreeCurricularPlan degreeCurricularPlan) {
		for (final ExecutionDegree executionDegree : executionDegrees) {
			if (executionDegree.getDegreeCurricularPlan() == degreeCurricularPlan) {
				return executionDegree;
			}
		}
		return null;
	}

    private static boolean keepAggregate(final Set<String> curricularCourseCodes, final List<CurricularCourse> curricularCourses) {
        for (final CurricularCourse curricularCourse : curricularCourses) {
            if (curricularCourseCodes.contains(curricularCourse.getCode())) {
                return false;
            }
        }
        return true;
    }

    private static ExecutionCourse createExecutionCourse(final ExecutionPeriod newExecutionPeriod,
            final ExecutionCourse executionCourse) {
        final ExecutionCourse newExecutionCourse = new ExecutionCourse();

        for (final BibliographicReference bibliographicReference : executionCourse.getAssociatedBibliographicReferences()) {
        	final BibliographicReference newBibliographicReference = new BibliographicReference();
        	newBibliographicReference.setAuthors(bibliographicReference.getAuthors());
        	newBibliographicReference.setExecutionCourse(newExecutionCourse);
        	newBibliographicReference.setOptional(bibliographicReference.getOptional());
        	newBibliographicReference.setReference(bibliographicReference.getReference());
        	newBibliographicReference.setTitle(bibliographicReference.getTitle());
        	newBibliographicReference.setYear(bibliographicReference.getYear());
        }

        newExecutionCourse.setComment(new String());
        newExecutionCourse.setCourseReport(null);

        final EvaluationMethod evaluationMethod = executionCourse.getEvaluationMethod();
        if (evaluationMethod != null) {
        	final EvaluationMethod newEvaluationMethod = new EvaluationMethod();
        	newEvaluationMethod.setEvaluationElements(evaluationMethod.getEvaluationElements());
        	newEvaluationMethod.setEvaluationElementsEn(evaluationMethod.getEvaluationElementsEn());
        	newEvaluationMethod.setExecutionCourse(newExecutionCourse);
        }

        newExecutionCourse.setExecutionPeriod(newExecutionPeriod);
        newExecutionCourse.setLabHours(executionCourse.getLabHours());
        newExecutionCourse.setNome(executionCourse.getNome());
        newExecutionCourse.setPraticalHours(executionCourse.getPraticalHours());
        newExecutionCourse.setSigla(executionCourse.getSigla());
        newExecutionCourse.setSite(new Site());
        newExecutionCourse.setTheoPratHours(executionCourse.getTheoPratHours());
        newExecutionCourse.setTheoreticalHours(executionCourse.getTheoreticalHours());

        return newExecutionCourse;
    }

    private static void addCurricularCourse(final ExecutionCourse executionCourse, final CurricularCourse curricularCourse) {
    	final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
    	final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
    	final Degree degree = degreeCurricularPlan.getDegree();
        if (DEGREE_TYPES_TO_CREATE.contains(degree.getTipoCurso())
        		&& !curricularCourse.getActiveScopesInExecutionPeriodAndSemester(executionPeriod).isEmpty()) {
        	curricularCourse.getAssociatedExecutionCourses().add(executionCourse);
        }
    }

    private static void createSchedule(final Map<SchoolClass, SchoolClass> schoolClassMap, 
    		final ExecutionCourse newExecutionCourse, final ExecutionCourse executionCourse) {
        for (final Shift shift : executionCourse.getAssociatedShifts()) {
            createShift(schoolClassMap, shift, newExecutionCourse);
        }
    }

    private static void createShift(final Map<SchoolClass, SchoolClass> schoolClassMap, 
    		final Shift shift, final ExecutionCourse executionCourse) {
        final Shift newShift = new Shift();

        newShift.setAvailabilityFinal(shift.getLotacao());
        newShift.setDisciplinaExecucao(executionCourse);
        newShift.setLotacao(shift.getLotacao());
        newShift.setNome(shift.getNome());
        newShift.setTipo(shift.getTipo());

        for (final SchoolClass schoolClass : shift.getAssociatedClasses()) {
            final SchoolClass newSchoolClass = schoolClassMap.get(schoolClass);
            addSchoolClass(newShift, newSchoolClass, schoolClass);
        }

        if (!newShift.getAssociatedClasses().isEmpty()) {
            for (final Lesson lesson : shift.getAssociatedLessons()) {
                createLesson(lesson, newShift);
            }
        }
    }

    private static void addSchoolClass(final Shift shift, final SchoolClass newSchoolClass, final SchoolClass schoolClass) {
        if (newSchoolClass != null) {
            newSchoolClass.getAssociatedShifts().add(shift);
        } else {
        	final ExecutionDegree executionDegree = schoolClass.getExecutionDegree();
        	final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        	logger.warn("No new class found for: " + schoolClass.getNome() + " year: " + schoolClass.getAnoCurricular()
        			+ " degree: " + degreeCurricularPlan.getName());
        }
    }

    private static void createLesson(final Lesson lesson, final Shift newShift) {
    	final ExecutionCourse executionCourse = newShift.getDisciplinaExecucao();
    	final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
    	final OldRoom room = lesson.getSala();

        final Lesson newLesson = new Lesson();
        newLesson.setDiaSemana(lesson.getDiaSemana());;
        newLesson.setExecutionPeriod(executionPeriod);
        newLesson.setFim(lesson.getFim());
        newLesson.setInicio(lesson.getInicio());
        newLesson.setSala(room);
        newLesson.setShift(newShift);
        newLesson.setTipo(lesson.getTipo());

        createRoomOccupation(lesson.getRoomOccupation(), newLesson);
    }

    private static RoomOccupation createRoomOccupation(final RoomOccupation roomOccupation, final Lesson lesson) {
    	final OldRoom room = roomOccupation.getRoom();
    	final OccupationPeriod period = getPeriod(lesson);

        final RoomOccupation newRoomOccupation = new RoomOccupation();
        newRoomOccupation.setDayOfWeek(roomOccupation.getDayOfWeek());
        newRoomOccupation.setEndTime(roomOccupation.getEndTime());
        newRoomOccupation.setFrequency(roomOccupation.getFrequency());
        newRoomOccupation.setLesson(lesson);
        newRoomOccupation.setPeriod(period);
        newRoomOccupation.setRoom(room);
        newRoomOccupation.setStartTime(roomOccupation.getStartTime());
        newRoomOccupation.setWeekOfQuinzenalStart(roomOccupation.getWeekOfQuinzenalStart());
        newRoomOccupation.setWrittenEvaluation(null);

        return newRoomOccupation;
    }

    private static OccupationPeriod getPeriod(final Lesson lesson) {
        final SchoolClass schoolClass = lesson.getShift().getAssociatedClasses().get(0);
        final ExecutionDegree executionDegree = schoolClass.getExecutionDegree();

        if (schoolClass.getExecutionPeriod().getSemester().intValue() == 1) {
            return executionDegree.getPeriodLessonsFirstSemester();
        }
        if (schoolClass.getExecutionPeriod().getSemester().intValue() == 2) {
            return executionDegree.getPeriodLessonsSecondSemester();
        }
        return null;
    }

}
