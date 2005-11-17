package net.sourceforge.fenixedu.applicationTier.Servico.manager.semesterInitialization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IBibliographicReference;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
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
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
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

        final IExecutionPeriod executionPeriod = persistentExecutionPeriod.readBySemesterAndExecutionYear(semester, executionYear);
        final IExecutionPeriod newExecutionPeriod = executionPeriod.getNextExecutionPeriod().getNextExecutionPeriod();

        logger.info(StringAppender.append("Initializing period [semester ", newExecutionPeriod
                .getSemester().toString(), ", ", newExecutionPeriod.getExecutionYear().getYear(),
                "] from period [semester ", executionPeriod.getSemester().toString(), ", ",
                executionPeriod.getExecutionYear().getYear(), "]."));

        final Map<IExecutionDegree, IExecutionDegree> executionDegreeMap = constructExecutionDegreeCorrespondenceMap(executionPeriod, newExecutionPeriod);
        final Map<ISchoolClass, ISchoolClass> schoolClassMap = createSchoolClasses(executionDegreeMap, newExecutionPeriod);
        createExecutionCourses(curricularCourseCodes, schoolClassMap, executionPeriod, newExecutionPeriod);
    }

    private static void createExecutionCourses(final Set<String> curricularCourseCodes, final Map<ISchoolClass, ISchoolClass> schoolClassMap,
    		final IExecutionPeriod executionPeriod, final IExecutionPeriod newExecutionPeriod) {
    	for (final IExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
    		if (keepAggregate(curricularCourseCodes, executionCourse.getAssociatedCurricularCourses())) {
                final IExecutionCourse newExecutionCourse = createExecutionCourse(newExecutionPeriod, executionCourse);
                final List<ICurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
                for (final ICurricularCourse curricularCourse : curricularCourses) {
                    addCurricularCourse(newExecutionCourse, curricularCourse);
                }
                if (!newExecutionCourse.getAssociatedCurricularCourses().isEmpty()) {
                    createSchedule(schoolClassMap, newExecutionCourse, executionCourse);
                }
            } else {
                final List<ICurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
                int i = 0;
                for (final ICurricularCourse curricularCourse : curricularCourses) {
                    final IExecutionCourse newExecutionCourse = createExecutionCourse(newExecutionPeriod, executionCourse);
                    newExecutionCourse.setSigla(executionCourse.getSigla() + "_" + i++);
                    addCurricularCourse(newExecutionCourse, curricularCourse);
                }
    		}
    	}
	}

	private static Map<ISchoolClass, ISchoolClass> createSchoolClasses(final Map<IExecutionDegree, IExecutionDegree> executionDegreeMap,
    		final IExecutionPeriod newEecutionPeriod) {
    	final Map<ISchoolClass, ISchoolClass> schoolClassMap = new HashMap<ISchoolClass, ISchoolClass>();
    	for (final Entry<IExecutionDegree, IExecutionDegree> entry : executionDegreeMap.entrySet()) {
    		final IExecutionDegree executionDegree = entry.getKey();
    		final IDegree degree = executionDegree.getDegreeCurricularPlan().getDegree();
    		if (DEGREE_TYPES_TO_CREATE.contains(degree.getTipoCurso())) {
    			for (final ISchoolClass schoolClass : executionDegree.getSchoolClasses()) {
    				if (schoolClass.getExecutionPeriod().getSemester().equals(newEecutionPeriod.getSemester())) {
    					final ISchoolClass newSchoolClass = new SchoolClass();
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

	private static Map<IExecutionDegree, IExecutionDegree> constructExecutionDegreeCorrespondenceMap(
    		final IExecutionPeriod executionPeriod, final IExecutionPeriod newExecutionPeriod) {
    	final Map<IExecutionDegree, IExecutionDegree> executionDegreeMap = new HashMap<IExecutionDegree, IExecutionDegree>();
    	for (final IExecutionDegree executionDegree : executionPeriod.getExecutionYear().getExecutionDegrees()) {
    		final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
    		final IExecutionDegree correspondingExecutionDegree = findExecutionDegree(
    				newExecutionPeriod.getExecutionYear().getExecutionDegrees(), degreeCurricularPlan);
    		if (correspondingExecutionDegree != null) {
    			executionDegreeMap.put(executionDegree, correspondingExecutionDegree);
    		}
    	}
		return executionDegreeMap;
	}

	private static IExecutionDegree findExecutionDegree(
			final List<IExecutionDegree> executionDegrees, final IDegreeCurricularPlan degreeCurricularPlan) {
		for (final IExecutionDegree executionDegree : executionDegrees) {
			if (executionDegree.getDegreeCurricularPlan() == degreeCurricularPlan) {
				return executionDegree;
			}
		}
		return null;
	}

    private static boolean keepAggregate(final Set<String> curricularCourseCodes, final List<ICurricularCourse> curricularCourses) {
        for (final ICurricularCourse curricularCourse : curricularCourses) {
            if (curricularCourseCodes.contains(curricularCourse.getCode())) {
                return false;
            }
        }
        return true;
    }

    private static IExecutionCourse createExecutionCourse(final IExecutionPeriod newExecutionPeriod,
            final IExecutionCourse executionCourse) {
        final IExecutionCourse newExecutionCourse = new ExecutionCourse();

        for (final IBibliographicReference bibliographicReference : executionCourse.getAssociatedBibliographicReferences()) {
        	final IBibliographicReference newBibliographicReference = new BibliographicReference();
        	newBibliographicReference.setAuthors(bibliographicReference.getAuthors());
        	newBibliographicReference.setExecutionCourse(newExecutionCourse);
        	newBibliographicReference.setOptional(bibliographicReference.getOptional());
        	newBibliographicReference.setReference(bibliographicReference.getReference());
        	newBibliographicReference.setTitle(bibliographicReference.getTitle());
        	newBibliographicReference.setYear(bibliographicReference.getYear());
        }

        newExecutionCourse.setComment(new String());
        newExecutionCourse.setCourseReport(null);

        final IEvaluationMethod evaluationMethod = executionCourse.getEvaluationMethod();
        if (evaluationMethod != null) {
        	final IEvaluationMethod newEvaluationMethod = new EvaluationMethod();
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

    private static void addCurricularCourse(final IExecutionCourse executionCourse, final ICurricularCourse curricularCourse) {
    	final IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
    	final IDegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
    	final IDegree degree = degreeCurricularPlan.getDegree();
        if (DEGREE_TYPES_TO_CREATE.contains(degree.getTipoCurso())
        		&& !curricularCourse.getActiveScopesInExecutionPeriodAndSemester(executionPeriod).isEmpty()) {
        	curricularCourse.getAssociatedExecutionCourses().add(executionCourse);
        }
    }

    private static void createSchedule(final Map<ISchoolClass, ISchoolClass> schoolClassMap, 
    		final IExecutionCourse newExecutionCourse, final IExecutionCourse executionCourse) {
        for (final IShift shift : executionCourse.getAssociatedShifts()) {
            createShift(schoolClassMap, shift, newExecutionCourse);
        }
    }

    private static void createShift(final Map<ISchoolClass, ISchoolClass> schoolClassMap, 
    		final IShift shift, final IExecutionCourse executionCourse) {
        final IShift newShift = new Shift();

        newShift.setAvailabilityFinal(shift.getLotacao());
        newShift.setDisciplinaExecucao(executionCourse);
        newShift.setLotacao(shift.getLotacao());
        newShift.setNome(shift.getNome());
        newShift.setTipo(shift.getTipo());

        for (final ISchoolClass schoolClass : shift.getAssociatedClasses()) {
            final ISchoolClass newSchoolClass = schoolClassMap.get(schoolClass);
            addSchoolClass(newShift, newSchoolClass, schoolClass);
        }

        if (!newShift.getAssociatedClasses().isEmpty()) {
            for (final ILesson lesson : shift.getAssociatedLessons()) {
                createLesson(lesson, newShift);
            }
        }
    }

    private static void addSchoolClass(final IShift shift, final ISchoolClass newSchoolClass, final ISchoolClass schoolClass) {
        if (newSchoolClass != null) {
            newSchoolClass.getAssociatedShifts().add(shift);
        } else {
        	final IExecutionDegree executionDegree = schoolClass.getExecutionDegree();
        	final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        	logger.warn("No new class found for: " + schoolClass.getNome() + " year: " + schoolClass.getAnoCurricular()
        			+ " degree: " + degreeCurricularPlan.getName());
        }
    }

    private static void createLesson(final ILesson lesson, final IShift newShift) {
    	final IExecutionCourse executionCourse = newShift.getDisciplinaExecucao();
    	final IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
    	final IRoom room = lesson.getSala();

        final ILesson newLesson = new Lesson();
        newLesson.setDiaSemana(lesson.getDiaSemana());;
        newLesson.setExecutionPeriod(executionPeriod);
        newLesson.setFim(lesson.getFim());
        newLesson.setInicio(lesson.getInicio());
        newLesson.setSala(room);
        newLesson.setShift(newShift);
        newLesson.setTipo(lesson.getTipo());

        createRoomOccupation(lesson.getRoomOccupation(), newLesson);
    }

    private static IRoomOccupation createRoomOccupation(final IRoomOccupation roomOccupation, final ILesson lesson) {
    	final IRoom room = roomOccupation.getRoom();
    	final IPeriod period = getPeriod(lesson);

        final IRoomOccupation newRoomOccupation = new RoomOccupation();
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

    private static IPeriod getPeriod(final ILesson lesson) {
        final ISchoolClass schoolClass = lesson.getShift().getAssociatedClasses().get(0);
        final IExecutionDegree executionDegree = schoolClass.getExecutionDegree();

        if (schoolClass.getExecutionPeriod().getSemester().intValue() == 1) {
            return executionDegree.getPeriodLessonsFirstSemester();
        }
        if (schoolClass.getExecutionPeriod().getSemester().intValue() == 2) {
            return executionDegree.getPeriodLessonsSecondSemester();
        }
        return null;
    }

}
