/*
 * ConversorSOP.java
 *
 * Created on 1 de Setembro de 2002, 11:16
 */

package ServidorPersistente.middleware;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.odmg.QueryException;
import org.odmg.QueryInvalidException;

import Dominio.CurricularCourse;
import Dominio.CurricularCourseLoad;
import Dominio.ExecutionCourse;
import Dominio.IAula;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionYear;
import Dominio.ISala;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Dominio.SOPAtribuicaoDisciplinas;
import Dominio.SOPAulas;
import Dominio.SOPDisciplinas;
import Dominio.Sala;
import Dominio.Turma;
import Dominio.TurmaTurno;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import ServidorPersistente.middleware.Utils.ClassUtils;
import ServidorPersistente.middleware.Utils.PrintUtil;
import ServidorPersistente.middleware.comparators.migrationExecutionCourse.ComparatorExecutionCourseByInitials;
import ServidorPersistente.middleware.comparators.sopLesson.SOPHalfHourComparator;
import ServidorPersistente.middleware.constants.Constants;
import ServidorPersistente.middleware.predicates.migrationExecutionCourseList.PredicateExecutionCourseByDegreeInitialsAndCourseInitial;
import ServidorPersistente.middleware.predicates.migrationExecutionCourseList.PredicateExecutionCourseWithDanglingCurricularCourse;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateConsistentLesson;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonWithType;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonsFromClass;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateLessonsFromMigrationExecutionCourse;
import ServidorPersistente.middleware.predicates.migrationLessonList.PredicateMigrationLessonThatContainsHalfHour;
import ServidorPersistente.middleware.transformers.TransformerMigrationExecutionCourse2ExecutionCourse;
import ServidorPersistente.middleware.transformers.TransformerMigrationLessonToIAula;
import ServidorPersistente.middleware.transformers.TransformerMigrationShift2Shift;
import Util.TipoSala;

/**
 *
 * @author  ars
 */

public class ConversorSOP {

	public class AcessDatabase extends ObjectFenixOJB {
		public List read(Class clazz) {
			List list = null;
			try {
				String strQuery = "select all from " + clazz.getName();
				query.create(strQuery);
				list = (List) query.execute();
			} catch (QueryInvalidException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (QueryException e) {
				e.printStackTrace();
				System.exit(1);
			}

			return list;

		}

		/**
		 * @param courseName
		 * @param degreeInitials
		 * @param curricularCourse
		 * @return ICurricularCourse
		 */
		public ICurricularCourse readCurricularCourseByNameAndDegreeInitialsAndCurricularYear(
			String courseName,
			String degreeInitials,
			Integer curricularYear) {
			ICurricularCourse curricularCourse = null;
			try {

				String strQuery =
					"select all from "
						+ CurricularCourse.class.getName()
						+ " where name = $1 and "
						+ " curricularYear = $2 and "
						+ " degreeCurricularPlan.degree.sigla = $3 ";
				query.create(strQuery);
				query.bind(courseName);
				query.bind(curricularYear);
				query.bind(degreeInitials);
				List list = (List) query.execute();
				lockRead(list);

				if (!list.isEmpty()) {
					if (list.size() > 1)
						System.out.println(
							"Obtive mais que uma disciplina Curricular!DEGREE="
								+ degreeInitials
								+ ",COURSE="
								+ courseName
								+ ",CURRICULAR_YEAR="
								+ curricularYear);
					curricularCourse = (ICurricularCourse) list.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}

			return curricularCourse;
		}

	}

	static ISuportePersistente sp;
	static {
		try {
			sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
//		ConversorSOP conversor = new ConversorSOP();
		System.out.println("Findou!!!");
	}

	private List classList = new ArrayList();

	ITurmaPersistente classDAO = sp.getITurmaPersistente();
	ITurmaTurnoPersistente classShiftDAO = sp.getITurmaTurnoPersistente();
	IPersistentExecutionCourse executionCourseDAO =
		sp.getIDisciplinaExecucaoPersistente();

	ICursoExecucaoPersistente executionDegreeDAO =
		sp.getICursoExecucaoPersistente();

	IPersistentExecutionPeriod executionPeriodDAO =
		sp.getIPersistentExecutionPeriod();

	IExecutionYear executionYear;
	IAulaPersistente lessonDAO = sp.getIAulaPersistente();

	List licenciaturas = null;

	// HashMap degreeInitials --> ICursoExecucao
	HashMap licenciaturasExecucao = null;

	private List migrationLessonsList = new ArrayList();
	int notOk = 0;

	AcessDatabase readDatabase = new AcessDatabase();
	ISalaPersistente roomDAO = sp.getISalaPersistente();

	// HashMap roomName --> ISala
	HashMap roomHashMap = null;
	ITurnoPersistente shiftDAO = sp.getITurnoPersistente();
	ITurnoAulaPersistente shiftLessonDAO = sp.getITurnoAulaPersistente();

	HashMap shiftsCounter = new HashMap();

	List sopAtribuicaoDisciplinas = null;
	List sopAulas = null;
	HashMap sopDisciplinas = null;

	/* curricularCourseName + degreeCode --> CurricularCourseLoad */
	HashMap curricularLoads = new HashMap();
	/** Creates a new instance of ConversorSOP */
	public ConversorSOP() {

		try {
			sp.iniciarTransaccao();

			classDAO.deleteAll();
			shiftDAO.deleteAll();
			shiftLessonDAO.deleteAll();
			classShiftDAO.deleteAll();
			lessonDAO.deleteAll();
			readDatabase.deleteAll(
				"select all from " + ExecutionCourse.class.getName());

			Constants.executionPeriod =
				executionPeriodDAO.readActualExecutionPeriod();

			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			System.exit(1);
		}
		executionYear = Constants.executionPeriod.getExecutionYear();

		curricularLoads = createCurricularLoadHashMap();

		getExecutionDegreeList();

		/*		sopAtribuicaoDisciplinas = getSOPAtribuicaoDisciplinas();
		
				System.out.println(
					+sopAtribuicaoDisciplinas.size() + " course-class from sop!");
		*/
		sopDisciplinas = getSOPDisciplinas();

		System.out.println(sopDisciplinas.size() + " courses from sop!");

		sopAulas = getSOPAulas();

		System.out.println(sopAulas.size() + " lessons from sop!");

		//must be by this list not by sopAtribuicaoDisciplinas because have more classes
		createClassList(sopAulas);

		/* this will run only once */
		insertSalas();

		createMigrationLessonList(sopAulas);

		System.out.println(
			"Got " + migrationLessonsList.size() + " migration lessons");

		/* for debugging */
		PrintUtil.printMigrationLessonList(migrationLessonsList);
		/* ************** */

		/* Remove possibly inconsistents lessons */
		List consistentMigrationLessonList =
			(List) CollectionUtils.select(
				migrationLessonsList,
				new PredicateConsistentLesson());

		System.out.println(
			(migrationLessonsList.size()
				- consistentMigrationLessonList.size())
				+ " lessons not treated.");

		// gets room hash map from database
		roomHashMap = getRoomHashMap();

		Collection migrationExecutionCourseCollection =
			createMigrationExecutionCourseCollection(consistentMigrationLessonList);
		Collections.sort(
			(List) migrationExecutionCourseCollection,
			new ComparatorExecutionCourseByInitials());

		
		System.out.println(
			migrationExecutionCourseCollection.size()
				+ " executionCourse created.");

		PrintUtil.printList(
			migrationExecutionCourseCollection,
			"c:\\middleware\\disciplinasExecucao.txt");

		//***********************************************************************************

		/*		Collection lessonList =
					CollectionUtils.collect(
						consistentMigrationLessonList,
						new TransformerMigrationLessonToIAula(roomHashMap));*/
		/* **** */

		associateCurricularCoursesToMigrationExecutionCourse(migrationExecutionCourseCollection);

		PrintUtil.printList(
			migrationExecutionCourseCollection,
			"c:\\middleware\\disciplinasExecucaoEDisciplinasCurriculares.txt");
		PrintUtil.printList(
			migrationExecutionCourseCollection,
			"c:\\middleware\\disciplinasExecucaoComDisciplinasCurricularesSoltas.txt",
			new PredicateExecutionCourseWithDanglingCurricularCourse());


//		List migrationShiftList =
//			createShiftList(classList, consistentMigrationLessonList);
/*
		saveExecutionCourse(migrationExecutionCourseCollection);
		saveClasses(classList);
		saveLessons(consistentMigrationLessonList);
*/
	}

	/**
	 * @return HashMap
	 */
	private HashMap createCurricularLoadHashMap() {
		
		HashMap ret = new HashMap();
		try {
			sp.iniciarTransaccao();
			List curricularCourseLoadList =
				readDatabase.read(CurricularCourseLoad.class);
			sp.confirmarTransaccao();
			Iterator loadIterator = curricularCourseLoadList.iterator();
			while (loadIterator.hasNext()) {
				CurricularCourseLoad load =
					(CurricularCourseLoad) loadIterator.next();
				ret.put(load.getCourseName().toUpperCase() + load.getDegreeCode(), load);
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			System.exit(1);
		}
		return ret;
	}

	private void addHalfHourToMigrationLessonList(SOPAulas halfHour) {
		MigrationLesson migrationLesson =
			getHalfHourOwnerMigrationLesson(halfHour);
		if (migrationLesson == null) {
			migrationLesson = new MigrationLesson(halfHour);
			migrationLessonsList.add(migrationLesson);
		} else {
			migrationLesson.addHalfHour(halfHour);
		}

	}

	/**
	 * @param migrationExecutionCourseCollection
	 */
	private void associateCurricularCoursesToMigrationExecutionCourse(Collection migrationExecutionCourseCollection) {
		Iterator migrationExecutionCourseIterator =
			migrationExecutionCourseCollection.iterator();

		FileOutputStream fout = null;
		try {
			fout =
				new FileOutputStream("c:\\middleware\\disciplinasExecucaoSemCurricular.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintWriter out = new PrintWriter(fout, true);
		while (migrationExecutionCourseIterator.hasNext()) {
			MigrationExecutionCourse migrationExecutionCourse =
				(MigrationExecutionCourse) migrationExecutionCourseIterator
					.next();
			HashMap degreeToSopCourse =
				migrationExecutionCourse.getDegreeToSopCourse();

			Iterator degreeToSopCourseIterator =
				degreeToSopCourse.keySet().iterator();
			while (degreeToSopCourseIterator.hasNext()) {
				String degreeInitials =
					(String) degreeToSopCourseIterator.next();
				List pairList = (List) degreeToSopCourse.get(degreeInitials);
				Iterator pairListIterator = pairList.iterator();

				while (pairListIterator.hasNext()) {
					CourseCurricularYearPair pair =
						(CourseCurricularYearPair) pairListIterator.next();
					String courseName =
						((SOPDisciplinas) sopDisciplinas
							.get(pair.getCourseInitials()))
							.getNome();
					Integer curricularYear = pair.getCurricularYear();
					ICurricularCourse curricularCourse = null;
					try {
						
						
						if (migrationExecutionCourse
							.getName()
							.equalsIgnoreCase("Introdução à Programação")
							&& degreeInitials.equals("LEEC")
							&& curricularYear.intValue() == 1) {
							courseName = "PROGRAMAÇÃO";
						}
						if (migrationExecutionCourse
							.getName()
							.equalsIgnoreCase("Física I")
							&& (degreeInitials.equals("LERCI") || degreeInitials.equals("LESIM"))
							&& curricularYear.intValue() == 1) {
							courseName = "FÍSICA I - CURSO INFORMÁTICA";
						}
						
						if (migrationExecutionCourse
							.getName()
							.equalsIgnoreCase("Química dos Alimentos")
							&& (degreeInitials.equals("LEBL") || degreeInitials.equals("LQ"))
							&& curricularYear.intValue() == 4) {
							courseName = "QUÍMICA DE ALIMENTOS";
						}
						if (migrationExecutionCourse
							.getName()
							.equalsIgnoreCase("Monitorização e Controlo de Bio Processos")
							&& degreeInitials.equals("LEBL")
							&& curricularYear.intValue() == 5) {
							courseName = "MONITORIZAÇÃO E CONTROLO DE BIOPROCESSOS";
						}
						if (migrationExecutionCourse
							.getName()
							.equalsIgnoreCase("Probabilidades e Estatística")
							&& degreeInitials.equals("LMAC")
							&& curricularYear.intValue() == 2) {
							courseName = "PROBABILIDADES E ESTATÍSTICA I";
						}
						if (migrationExecutionCourse
							.getName()
							.equalsIgnoreCase("Engenharia de Reacções I")
							&& degreeInitials.equals("LEQ")
							&& curricularYear.intValue() == 3) {
							courseName = "ENGENHARIA DAS REACÇÕES I";
						}
						if (migrationExecutionCourse
							.getName()
							.equalsIgnoreCase("EQUAÇÕES DIFERENCIAIS PARCIAIS II")
							&& degreeInitials.equals("LMAC")
							&& curricularYear.intValue() == 4) {
							courseName = "EQUAÇÕES DIFERENCIAIS PARCIAIS II(M)";
						}
						
						sp.iniciarTransaccao();

						curricularCourse =
							readDatabase
								.readCurricularCourseByNameAndDegreeInitialsAndCurricularYear(
								courseName,
								degreeInitials,
								curricularYear);
						sp.confirmarTransaccao();
					} catch (ExcepcaoPersistencia e) {
						e.printStackTrace();
						System.exit(1);
					}

					if (curricularCourse == null) {
						out.println(
							"(Curso="
								+ degreeInitials
								+ ",CurricularYear="
								+ curricularYear.toString()
								+ ",COURSE = "
								+ courseName
								+ ", SIGLA = "
								+ pair.getCourseInitials()
								+ ")");
					} else {
						migrationExecutionCourse.addCurricularCourse(
							curricularCourse);
						CurricularCourseLoad load =
							(CurricularCourseLoad) curricularLoads.get(
								curricularCourse.getName().toUpperCase()
									+ curricularCourse
										.getDegreeCurricularPlan()
										.getDegree()
										.getSigla());
						if (load != null) {

							migrationExecutionCourse.setTheoreticalHours(
								load.getTheoHours() == null
									? 0.0
									: load.getTheoHours().doubleValue());
							migrationExecutionCourse.setPracticalHours(
								load.getPratHours() == null
									? 0.0
									: load.getPratHours().doubleValue());
							migrationExecutionCourse
								.setTheoreticalPraticalHours(
								load.getTheoPratHours() == null
									? 0.0
									: load.getTheoPratHours().doubleValue());
							migrationExecutionCourse.setLaboratoryHours(
								load.getLabHours() == null
									? 0.0
									: load.getLabHours().doubleValue());
						}
					}
				}
			}
		}
		out.close();

	}

	/**
	 * @param executionCourse
	 * @param migrationLesson
	 * @param initialsAtributed
	 */
	private void associateInitialsToExecutionCourse(
		MigrationExecutionCourse executionCourse,
		MigrationLesson migrationLesson,
		HashMap initialsAtributed) {

		List courseInitialsList = migrationLesson.getCourseInitialsList();

		String courseInitialsToBeAttributed = new String("");
		Iterator courseInitialsIterator = courseInitialsList.iterator();
		String name = new String("");

		while (courseInitialsIterator.hasNext()) {
			String courseInitial = (String) courseInitialsIterator.next();
			if (!courseInitialsToBeAttributed.equals(""))
				courseInitialsToBeAttributed += "-";
			courseInitialsToBeAttributed += courseInitial;
			SOPDisciplinas sopCourse =
				(SOPDisciplinas) sopDisciplinas.get(courseInitial);
			if (sopCourse == null) {
				throw new IllegalStateException(
					"Can't find sop course:" + courseInitial);
			}
			if (!name.equals("")) {
				name += "/";
			}
			name += sopCourse.getNome();
		}
		Integer timesAttributed =
			(Integer) initialsAtributed.get(courseInitialsToBeAttributed);
		executionCourse.setAbreviation(courseInitialsToBeAttributed);
		executionCourse.setName(name);
		if (timesAttributed == null)
			initialsAtributed.put(courseInitialsToBeAttributed, new Integer(1));
		else {
			initialsAtributed.put(
				new String(courseInitialsToBeAttributed),
				new Integer(timesAttributed.intValue() + 1));
			courseInitialsToBeAttributed += "-" + timesAttributed.intValue();
		}
		executionCourse.setInitials(courseInitialsToBeAttributed);
	}
	/**
	 * @param lessonsFromTypeList
	 * @param lessonType
	 * @return Integer
	 */
	private Integer calculateShiftCapacity(
		Collection lessonsFromTypeList,
		String lessonType) {
		int capacity = 0;
		MigrationLesson migrationLesson = null;
		Iterator iter = lessonsFromTypeList.iterator();
		while (iter.hasNext()) {
			migrationLesson = (MigrationLesson) iter.next();
			capacity =
				Math.max(
					capacity,
					((ISala) roomHashMap.get(migrationLesson.getRoom()))
						.getCapacidadeNormal()
						.intValue());
		}

		if (capacity != 0) {
			Float f = new Float(capacity * 1.2);
			capacity = f.intValue();
		} else if (lessonType.equals("Lab"))
			capacity = 30;
		else
			capacity = 100;
		return new Integer(capacity);
	}
	/**
	 * @param executionCourse
	 * @param lessonType
	 * @param lessonsFromTypeList
	 * @return List
	 */
	private List calculateShiftList(
		ITurma clazz,
		MigrationExecutionCourse executionCourse,
		String lessonType,
		Collection lessonsFromTypeList,
		PrintWriter out) {
		List migrationShiftList = new ArrayList();
		//		if (lessonsFromTypeList.isEmpty()){
		////			out.println("------------------------------------------");			
		////			out.print("Coloquei a 0 ");
		//			if (lessonType.equals("Teo")){
		////				out.print("Teo ");
		//				executionCourse.setTheoreticalHours(0);
		//			}else if (lessonType.equals("Pra")){
		////				out.print("Pra ");
		//				executionCourse.setPracticalHours(0);
		//			}else if (lessonType.equals("TP")){
		////				out.print("TP ");				
		//				executionCourse.setTheoreticalPraticalHours(0);
		//			}else if (lessonType.equals("Lab")){
		////				out.print("Lab ");
		//				executionCourse.setLaboratoryHours(0);
		//			}
		////			out.print("na disciplina:");
		////			out.println(executionCourse.toString());
		////			out.println("------------------------------------------");
		//			return migrationShiftList;
		//		}

		double numberOfHours =
			extractNumberOfHoursFromLessonList(lessonsFromTypeList);

		double executionLessonTypeHoursLoad =
			executionCourse.getHoursFromType(lessonType);

		if (executionLessonTypeHoursLoad == numberOfHours) {
			// create shift with all lessons
			String shiftName = createShiftName(executionCourse, lessonType);
			Integer capacity =
				calculateShiftCapacity(lessonsFromTypeList, lessonType);
			MigrationShift migrationShift =
				getMigrationShiftFromLessonShift(lessonsFromTypeList);
			if (migrationShift == null) {
				migrationShift = new MigrationShift();
				migrationShift.setShiftName(shiftName);
				migrationShift.setType(lessonType);
				migrationShift.setCapacity(capacity);
				migrationShift.setMigrationExecutionCourse(executionCourse);
				migrationShiftList.add(migrationShift);

			}
			migrationShift.setClass(clazz);
			Iterator lessonsIterator = lessonsFromTypeList.iterator();
			while (lessonsIterator.hasNext()) {
				MigrationLesson migrationLesson =
					(MigrationLesson) lessonsIterator.next();
				if (migrationLesson.getMigrationShift() == null) {
					migrationLesson.setMigrationShift(migrationShift);
				}
			}
		} else if (
			lessonsOfSameSize(
				lessonsFromTypeList,
				executionLessonTypeHoursLoad)) {
			//				create n shifts with one lesson each

			Integer capacity = null;
			//calculateShiftCapacity(lessonsFromTypeList, lessonType);
			String shiftName = null;
			// createShiftName(executionCourse, lessonType);			
			Iterator lessonIterator = lessonsFromTypeList.iterator();
			Collection aux = null;
			MigrationShift migrationShift = null;
			while (lessonIterator.hasNext()) {
				aux = new ArrayList();
				MigrationLesson migrationLesson =
					(MigrationLesson) lessonIterator.next();
				shiftName = createShiftName(executionCourse, lessonType);
				aux.add(migrationLesson);
				capacity = calculateShiftCapacity(aux, lessonType);
				migrationShift = migrationLesson.getMigrationShift();
				if (migrationShift == null) {
					migrationShift = new MigrationShift();
					migrationShift.setShiftName(shiftName);
					migrationShift.setType(lessonType);
					migrationShift.setCapacity(capacity);
					migrationShift.setMigrationExecutionCourse(executionCourse);
					migrationShiftList.add(migrationShift);
					migrationLesson.setMigrationShift(migrationShift);
				}
				migrationShift.setClass(clazz);
			}

		} else {
			out.println("TipoAula:" + lessonType);
			out.println("Turma:" + clazz.getNome());
			out.println(executionCourse.toString());

			Iterator iter = lessonsFromTypeList.iterator();
			while (iter.hasNext()) {
				MigrationLesson element = (MigrationLesson) iter.next();
				out.println(element.toString());
			}
			out.println("\tCurriculum:" + executionLessonTypeHoursLoad);
			out.println("\tRealidade:" + numberOfHours);
			out.println("----------------------------------------------");
			notOk++;
		}

		return migrationShiftList;
	}

	/**
	 * @param sopAtribuicaoDisciplinas
	 */
	private void createClassList(List pSopLessonList) {
		ITurma clazz = null;

		Iterator iterator = pSopLessonList.iterator();
		while (iterator.hasNext()) {
			SOPAulas sopLesson = (SOPAulas) iterator.next();
			String className = sopLesson.getTurma();
			Integer curricularYear =
				new Integer(ClassUtils.getCurricularYearByClassName(className));
			ICursoExecucao executionDegree =
				ClassUtils.getExecutionDegreeByClassName(
					className,
					Constants.siglasLicenciaturas,
					licenciaturasExecucao);
			clazz = new Turma();

			clazz.setAnoCurricular(curricularYear);
			clazz.setExecutionDegree(executionDegree);
			clazz.setExecutionPeriod(Constants.executionPeriod);
			clazz.setNome(className);
			if (!classList.contains(clazz))
				classList.add(clazz);
		}

		System.out.println(classList.size() + " classes obtained");
	}

	/**
	 * @param consistentMigrationLessonList
	 * @return List
	 */
	private Collection createMigrationExecutionCourseCollection(List consistentMigrationLessonList) {

		Collection executionCourseCollection = new ArrayList();
		Iterator migrationLessonIterator =
			consistentMigrationLessonList.iterator();

		/* courseInitials --> number of times attributed */
		HashMap initialsAtributed = new HashMap();
		while (migrationLessonIterator.hasNext()) {
			MigrationLesson migrationLesson =
				(MigrationLesson) migrationLessonIterator.next();

			HashMap courseHashMap = migrationLesson.getCourseToClassHashMap();
			MigrationExecutionCourse executionCourse = null;

			Iterator courseIterator = courseHashMap.keySet().iterator();
			while ((courseIterator.hasNext()) && (executionCourse == null)) {
				String courseInitials = (String) courseIterator.next();

				List classList = (List) courseHashMap.get(courseInitials);
				Iterator classIterator = classList.iterator();
				while (classIterator.hasNext() && executionCourse == null) {
					String className = (String) classIterator.next();

					int curricularYear =
						ClassUtils.getCurricularYearByClassName(className);
					String degreeInitials =
						ClassUtils.getDegreeInitials(className);
					executionCourse =
						(MigrationExecutionCourse) CollectionUtils.find(
							executionCourseCollection,
							new PredicateExecutionCourseByDegreeInitialsAndCourseInitial(
								degreeInitials,
								courseInitials,
								curricularYear));
				}
			}

			if (executionCourse == null) {
				executionCourse = new MigrationExecutionCourse();
				executionCourseCollection.add(executionCourse);
				associateInitialsToExecutionCourse(
					executionCourse,
					migrationLesson,
					initialsAtributed);
			}

			Iterator courseIterator2 = courseHashMap.keySet().iterator();
			while ((courseIterator2.hasNext())) {
				String courseInitials = (String) courseIterator2.next();

				List classList = (List) courseHashMap.get(courseInitials);

				Iterator classIterator = classList.iterator();
				while (classIterator.hasNext()) {

					String className = (String) classIterator.next();

					int curricularYear =
						ClassUtils.getCurricularYearByClassName(className);
					String degreeInitials =
						ClassUtils.getDegreeInitials(className);
					executionCourse.addExecutionDegreeSopCourseRelation(
						degreeInitials,
						courseInitials,
						new Integer(curricularYear));
				}
			}
			migrationLesson.setMigrationExecutionCourse(executionCourse);
		}

		return executionCourseCollection;
	}

	private void createMigrationLessonList(List pSopLessonList) {
		migrationLessonsList = new ArrayList();

		Iterator iterator = pSopLessonList.iterator();
		while (iterator.hasNext()) {
			SOPAulas sopLessonHalfHour = (SOPAulas) iterator.next();
			// ignore lessons without room
			if (sopLessonHalfHour.getSala() != null)
				addHalfHourToMigrationLessonList(sopLessonHalfHour);
		}

	}
	/**
	 * @param classList
	 * @param consistentMigrationLessonList
	 */
	private List createShiftList(
		List classList,
		List consistentMigrationLessonList) {
		// TODO Not completed
		Iterator classIterator = classList.iterator();
		PrintWriter out = null;
		try {
			out =
				new PrintWriter(
					new FileOutputStream("c:\\middleware\\output.txt"),
					true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		List migrationShiftList = new ArrayList();

		while (classIterator.hasNext()) {

			ITurma clazz = (ITurma) classIterator.next();

			Collection classLessons =
				CollectionUtils.select(
					consistentMigrationLessonList,
					new PredicateLessonsFromClass(clazz));

			List executionCourseList =
				extractExecutionCourseFromLessonCollection(classLessons);
			Iterator executionCourseIterator = executionCourseList.iterator();

			while (executionCourseIterator.hasNext()) {
				MigrationExecutionCourse executionCourse =
					(MigrationExecutionCourse) executionCourseIterator.next();

				Collection lessonsExecutionCourseList =
					CollectionUtils.select(
						classLessons,
						new PredicateLessonsFromMigrationExecutionCourse(executionCourse));

				List lessonTypes =
					extractLessonTypesFromMigrationExecutionCourse(executionCourse);

				Iterator lessonTypeIterator = lessonTypes.iterator();
				while (lessonTypeIterator.hasNext()) {
					String lessonType = (String) lessonTypeIterator.next();
					Collection lessonsFromTypeList =
						CollectionUtils.select(
							lessonsExecutionCourseList,
							new PredicateLessonWithType(lessonType));
					migrationShiftList.addAll(
						calculateShiftList(
							clazz,
							executionCourse,
							lessonType,
							lessonsFromTypeList,
							out));

				}
			}

		}
		out.println("Not ok " + notOk);
		out.println("Shifts Created:" + migrationShiftList.size());
		out.close();
		return migrationShiftList;
	}
	/**
	 * @param executionCourse
	 * @param lessonType
	 * @return String
	 */
	private String createShiftName(
		MigrationExecutionCourse executionCourse,
		String lessonType) {
		String shiftKey = executionCourse.getInitials() + "-" + lessonType;

		if (!shiftsCounter.containsKey(shiftKey))
			shiftsCounter.put(shiftKey, new Integer(1));

		Integer i = (Integer) shiftsCounter.get(shiftKey);
		String shiftName = shiftKey + "-" + i.toString();
		shiftsCounter.put(shiftKey, new Integer(i.intValue() + 1));

		return shiftName;
	}
	/**
	 * @param classLessons
	 * @return List
	 */
	private List extractExecutionCourseFromLessonCollection(Collection classLessons) {
		Iterator lessonIterator = classLessons.iterator();
		List executionCourseList = new ArrayList();
		while (lessonIterator.hasNext()) {
			MigrationLesson lesson = (MigrationLesson) lessonIterator.next();
			if (!executionCourseList
				.contains(lesson.getMigrationExecutionCourse()))
				executionCourseList.add(lesson.getMigrationExecutionCourse());
		}
		return executionCourseList;
	}
	/**
	 * @param executionCourse
	 * @return List
	 */
	private List extractLessonTypesFromMigrationExecutionCourse(MigrationExecutionCourse executionCourse) {

		List lessonTypes = new ArrayList();
		lessonTypes.add("Teo");
		lessonTypes.add("Lab");
		lessonTypes.add("Pra");
		lessonTypes.add("TP");
		//		if (executionCourse.getTheoreticalHours() > 0) {
		//		
		//		}
		//		if (executionCourse.getLaboratoryHours() > 0) {
		//			
		//		}
		//		if (executionCourse.getPracticalHours() > 0) {
		//			
		//		}
		//		if (executionCourse.getTheoreticalPraticalHours() > 0) {
		//			
		//		}

		return lessonTypes;
	}
	/**
	 * @param lessonsFromTypeList
	 * @return double
	 */
	private double extractNumberOfHoursFromLessonList(Collection lessonsFromTypeList) {

		Iterator lessonIterator = lessonsFromTypeList.iterator();
		double numberOfHours = 0;
		while (lessonIterator.hasNext()) {
			MigrationLesson migrationLesson =
				(MigrationLesson) lessonIterator.next();
			numberOfHours
				+= (double) (migrationLesson.getEndIndex()
					- migrationLesson.getStartIndex()
					+ 1)
				/ 2;
		}
		return numberOfHours;
	}

	private void getExecutionDegreeList() {
		try {
			sp.iniciarTransaccao();
			List executionDegreeList =
				executionDegreeDAO.readByExecutionYear(executionYear);

			licenciaturasExecucao = new HashMap(executionDegreeList.size());

			Iterator iterator = executionDegreeList.iterator();

			while (iterator.hasNext()) {
				ICursoExecucao executionDegree =
					(ICursoExecucao) iterator.next();
				licenciaturasExecucao.put(
					executionDegree.getCurricularPlan().getDegree().getSigla(),
					executionDegree);
			}
			sp.confirmarTransaccao();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * @param halfHour
	 * @return MigrationLesson
	 */
	private MigrationLesson getHalfHourOwnerMigrationLesson(SOPAulas halfHour) {

		List list =
			(List) CollectionUtils.select(
				migrationLessonsList,
				new PredicateMigrationLessonThatContainsHalfHour(halfHour));

		if (list.size() > 0) {
			if (list.size() > 1) {
				System.out.println(halfHour);
				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {
					MigrationLesson migrationLesson =
						(MigrationLesson) iterator.next();
					System.out.println(migrationLesson);
				}
				System.exit(1);
			}
			return (MigrationLesson) list.get(0);
		} else {
			// get migration lesson that is the start of this half hour.
			Iterator iterator = migrationLessonsList.iterator();
			while (iterator.hasNext()) {
				MigrationLesson migrationLesson =
					(MigrationLesson) iterator.next();

				if ((migrationLesson.getEndIndex() + 1 == halfHour.getHora())
					&& (migrationLesson
						.getClassList()
						.contains(halfHour.getTurma()))
					&& (migrationLesson.getShiftNumber()
						== halfHour.getNumeroTurno())
					&& (migrationLesson.getDay() == halfHour.getDia())
					&& (migrationLesson
						.getRoom()
						.equals(
							halfHour.getSala() == null
								? ""
								: halfHour.getSala()))
					&& (migrationLesson
						.getLessonType()
						.equals(halfHour.getTipoAula()))
					&& (migrationLesson
						.getCourseInitialsList()
						.contains(halfHour.getDisciplina()))) {
					return migrationLesson;
				}
			}
		}

		return null;
	}
	/**
	 * @param lessonsFromTypeList
	 * @return MigrationShift
	 */
	private MigrationShift getMigrationShiftFromLessonShift(Collection lessonsFromTypeList) {
		Iterator lessonsIterator = lessonsFromTypeList.iterator();
		while (lessonsIterator.hasNext()) {
			MigrationLesson migrationLesson =
				(MigrationLesson) lessonsIterator.next();
			if (migrationLesson.getMigrationShift() != null)
				return migrationLesson.getMigrationShift();
		}
		return null;
	}

	/**
	 * @return HashMap
	 */
	private HashMap getRoomHashMap() {
		HashMap roomHashMap = new HashMap();
		try {
			sp.iniciarTransaccao();

			List list = roomDAO.readAll();

			sp.confirmarTransaccao();

			Iterator listIterator = list.iterator();
			while (listIterator.hasNext()) {
				ISala room = (ISala) listIterator.next();
				roomHashMap.put(room.getNome(), room);
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			System.exit(1);
		}
		return roomHashMap;
	}

	private List getSOPAtribuicaoDisciplinas() {
		try {
			sp.iniciarTransaccao();
			List list = readDatabase.read(SOPAtribuicaoDisciplinas.class);
			sp.confirmarTransaccao();
			return list;
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	/**
	 * 
	 * @return List ordered list by hour...
	 */
	private List getSOPAulas() {
		try {
			sp.iniciarTransaccao();
			List list = readDatabase.read(SOPAulas.class);
			sp.confirmarTransaccao();

			Collections.sort(list, new SOPHalfHourComparator());
			return list;
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	private HashMap getSOPDisciplinas() {
		try {
			sp.iniciarTransaccao();
			List list = readDatabase.read(SOPDisciplinas.class);
			sp.confirmarTransaccao();
			Iterator iterator = list.iterator();
			HashMap sopCourseHashMap = new HashMap();
			while (iterator.hasNext()) {
				SOPDisciplinas sopCourse = (SOPDisciplinas) iterator.next();
				sopCourseHashMap.put(sopCourse.getSigla(), sopCourse);
			}
			return sopCourseHashMap;
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	// insere apenas as salas que não estão na base de dados
	private void insertSalas() {
		Iterator iter = sopAulas.iterator();
		int i = 0;
		while (iter.hasNext()) {
			SOPAulas sa = (SOPAulas) iter.next();
			String sigla = sa.getSala();
			if (sigla == null) {
				sigla = "";
			}

			ISala room = null;

			try {
				sp.iniciarTransaccao();
				room = roomDAO.readByName(sigla);
				sp.confirmarTransaccao();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}

			if (room == null) {
				try {
					sp.iniciarTransaccao();

					room =
						new Sala(
							sigla,
							"Localização Indefinida",
							new Integer(0),
							new TipoSala(TipoSala.LABORATORIO),
							new Integer(30),
							new Integer(0));
					roomDAO.lockWrite(room);
					i++;
					sp.confirmarTransaccao();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}
		System.out.println(i + " new rooms created!");
	}
	/**
	 * @param lessonsFromTypeList
	 * @param executionLessonTypeHours
	 * @return boolean
	 */
	private boolean lessonsOfSameSize(
		Collection lessonsFromTypeList,
		double executionLessonTypeHours) {
		Iterator iter = lessonsFromTypeList.iterator();

		while (iter.hasNext()) {
			MigrationLesson element = (MigrationLesson) iter.next();
			if ((double) (element.getEndIndex() - element.getStartIndex() + 1)
				/ 2
				!= executionLessonTypeHours)
				return false;
		}
		return true;
	}
	/**
	 * @param classList
	 */
	private void saveClasses(List classList) {
		Iterator classesIterator = classList.iterator();
		while (classesIterator.hasNext()) {
			ITurma clazz = (ITurma) classesIterator.next();

			try {
				sp.iniciarTransaccao();
				classDAO.lockWrite(clazz);
				sp.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * @param migrationExecutionCourseCollection
	 */
	private void saveExecutionCourse(Collection migrationExecutionCourseCollection) {

		Iterator iter = migrationExecutionCourseCollection.iterator();
		TransformerMigrationExecutionCourse2ExecutionCourse t =
			new TransformerMigrationExecutionCourse2ExecutionCourse();

		while (iter.hasNext()) {
			MigrationExecutionCourse migrationExecutionCourse =
				(MigrationExecutionCourse) iter.next();
			IExecutionCourse executionCourse =
				(IExecutionCourse) t.transform(migrationExecutionCourse);
			// TODO not saving relation with curricular course?????
			try {
				sp.iniciarTransaccao();
				executionCourseDAO.lockWrite(executionCourse);
				sp.confirmarTransaccao();
				migrationExecutionCourse.setMigrated(true);
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
			}

		}

	}
	/**
	 * @param consistentMigrationLessonList
	 */
	private void saveLessons(List migrationLessonList) {
		Iterator lessonsIterator = migrationLessonList.iterator();
		TransformerMigrationLessonToIAula transfLesson =
			new TransformerMigrationLessonToIAula(roomHashMap);
		TransformerMigrationShift2Shift transfShift =
			new TransformerMigrationShift2Shift();
		List classShiftAuxList = new ArrayList();
		while (lessonsIterator.hasNext()) {
			MigrationLesson lesson = (MigrationLesson) lessonsIterator.next();

			try {
				sp.iniciarTransaccao();
				sp.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
			}

			if (!lesson.isMigrated()) {
				IAula materializedLesson =
					(IAula) transfLesson.transform(lesson);
				lesson.setLesson(materializedLesson);

				try {
					sp.iniciarTransaccao();
					lessonDAO.lockWrite(materializedLesson);
					sp.confirmarTransaccao();
					lesson.setMigrated(true);
				} catch (ExcepcaoPersistencia e) {
					System.out.println(lesson.toString());
					try {
						sp.cancelarTransaccao();
					} catch (Exception ex) {
						//ignored
						ex.printStackTrace();
					}
					e.printStackTrace();
					//System.exit(1);
				}

				MigrationShift migrationShift = lesson.getMigrationShift();
				if (migrationShift != null) {
					ITurno shift =
						(ITurno) transfShift.transform(migrationShift);
					migrationShift.setShift(shift);

					if (!migrationShift.isMigrated()) {
						try {
							sp.iniciarTransaccao();
							shiftDAO.lockWrite(shift);
							sp.confirmarTransaccao();
							migrationShift.setMigrated(true);
						} catch (ExistingPersistentException e) {
							try {
								sp.cancelarTransaccao();
							} catch (ExcepcaoPersistencia ex) {
								//ignored
							}
						} catch (ExcepcaoPersistencia e) {
							try {
								sp.cancelarTransaccao();
							} catch (ExcepcaoPersistencia ex) {
								//ignored
							}

							e.printStackTrace();
							System.exit(1);
						}
					}

					ITurnoAula shiftLesson =
						new TurnoAula(shift, materializedLesson);
					try {
						sp.iniciarTransaccao();
						shiftLessonDAO.lockWrite(shiftLesson);
						sp.confirmarTransaccao();
					} catch (Exception e) {

						e.printStackTrace();
						System.exit(1);
					}

					Iterator classIterator =
						migrationShift.getClassList().iterator();
					while (classIterator.hasNext()) {
						ITurma clazz = (ITurma) classIterator.next();
						if (!classShiftAuxList
							.contains(clazz.toString() + shift.toString())) {

							ITurmaTurno classShift =
								new TurmaTurno(clazz, shift);

							try {
								sp.iniciarTransaccao();
								classShiftDAO.lockWrite(classShift);
								sp.confirmarTransaccao();
								classShiftAuxList.add(
									clazz.toString() + shift.toString());
							} catch (ExistingPersistentException e) {
								try {
									sp.cancelarTransaccao();
								} catch (ExcepcaoPersistencia ex) {
									//ignored
								}

							} catch (ExcepcaoPersistencia e) {
								try {
									sp.cancelarTransaccao();
								} catch (ExcepcaoPersistencia ex) {
									//ignored
								}
								System.out.println(classShift.toString());
								e.printStackTrace();
								System.exit(1);
							}
						}
					}
				} //shift
			}
		}
	}
}
