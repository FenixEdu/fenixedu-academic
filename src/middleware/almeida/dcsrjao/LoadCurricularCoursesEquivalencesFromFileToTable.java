package middleware.almeida.dcsrjao;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import middleware.almeida.PersistentObjectOJBReader;
import Dominio.CurricularCourseEquivalence;
import Dominio.CurricularCourseEquivalenceRestriction;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseEquivalence;
import Dominio.ICurricularCourseEquivalenceRestriction;
import Dominio.IDegreeCurricularPlan;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class LoadCurricularCoursesEquivalencesFromFileToTable extends LoadAlmeidaDataToTable {

	private static LoadCurricularCoursesEquivalencesFromFileToTable loader = null;
	private static String logString = "";
//	private static final String ONE_SPACE = " ";
	private IDegreeCurricularPlan oldLEQDegreeCurricularPlan = null;
	private IDegreeCurricularPlan oldLQDegreeCurricularPlan = null;
//	private IDegreeCurricularPlan oldLEBLDegreeCurricularPlan = null;
	private IDegreeCurricularPlan newLEQDegreeCurricularPlan = null;
	private String inputFilename = "equivalenciasLEQAfter1997.txt";
//	private boolean before1977 = true;
	private final String NAME_OF_OLD_LEQ_DEGREE_CURRICULAR_PLAN = "LEQ";
	private final String NAME_OF_OLD_LQ_DEGREE_CURRICULAR_PLAN = "LQ";
//	private final String NAME_OF_OLD_LEBL_DEGREE_CURRICULAR_PLAN = "LEBL";
	private final String NAME_OF_NEW_DEGREE_CURRICULAR_PLAN = "LEQ2003/2004";

	public LoadCurricularCoursesEquivalencesFromFileToTable() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadCurricularCoursesEquivalencesFromFileToTable();
		}

		//		logString = "";
		//		loader.before1977 = flag;
		//		if (loader.before1977 == true) {
		//			loader.inputFilename = "equivalenciasLEQBefore1997.txt";
		//		} else {
		//			loader.inputFilename = "equivalenciasLEQAfter1997.txt";
		//		}
		//		ISuportePersistente suportePersistente = null;
		//		try {
		//			suportePersistente = SuportePersistenteOJB.getInstance();
		//			suportePersistente.iniciarTransaccao();
		//			IPersistentCurricularCourseEquivalence persistentCurricularCourseEquivalence = suportePersistente.getIPersistentCurricularCourseEquivalence();
		////			IPersistentCurricularCourseEquivalenceRestriction persistentCurricularCourseEquivalenceRestriction =
		////				suportePersistente.getIPersistentCurricularCourseEquivalenceRestriction();
		//			persistentCurricularCourseEquivalence.deleteAll();
		////			persistentCurricularCourseEquivalenceRestriction.deleteAll();
		//			suportePersistente.confirmarTransaccao();
		//		} catch (ExcepcaoPersistencia e) {
		//			try {
		//				suportePersistente.cancelarTransaccao();
		//			} catch (ExcepcaoPersistencia e1) {
		//				logString = "Erro ao apagar as tabelas CurricularCourseEquivalence e CurricularCourseEquivalenceRestriction";
		//				logString = loader.report(logString);
		//				loader.writeToFile(logString);
		//				return;
		//			}
		//			logString = "Erro ao apagar as tabelas CurricularCourseEquivalence e CurricularCourseEquivalenceRestriction";
		//			logString = loader.report(logString);
		//			loader.writeToFile(logString);
		//			return;
		//		}

		loader.persistentObjectOJB = new PersistentObjectOJBReader();
		loader.setupDAO();
		loader.oldLEQDegreeCurricularPlan = loader.persistentObjectOJB.readDegreeCurricularPlanByName(loader.NAME_OF_OLD_LEQ_DEGREE_CURRICULAR_PLAN);
		if (loader.oldLEQDegreeCurricularPlan == null) {
			logString = "o plano curricular " + loader.NAME_OF_OLD_LEQ_DEGREE_CURRICULAR_PLAN + " não existe!";
			logString = loader.report(logString);
			loader.writeToFile(logString);
			loader.shutdownDAO();
			return;
		}

		loader.oldLQDegreeCurricularPlan = loader.persistentObjectOJB.readDegreeCurricularPlanByName(loader.NAME_OF_OLD_LQ_DEGREE_CURRICULAR_PLAN);
		if (loader.oldLEQDegreeCurricularPlan == null) {
			logString = "o plano curricular " + loader.NAME_OF_OLD_LQ_DEGREE_CURRICULAR_PLAN + " não existe!";
			logString = loader.report(logString);
			loader.writeToFile(logString);
			loader.shutdownDAO();
			return;
		}

//		loader.oldLEBLDegreeCurricularPlan = loader.persistentObjectOJB.readDegreeCurricularPlanByName(loader.NAME_OF_OLD_LEBL_DEGREE_CURRICULAR_PLAN);
//		if (loader.oldLEQDegreeCurricularPlan == null) {
//			logString = "o plano curricular " + loader.NAME_OF_OLD_LEBL_DEGREE_CURRICULAR_PLAN + " não existe!";
//			logString = loader.report(logString);
//			loader.writeToFile(logString);
//			loader.shutdownDAO();
//			return;
//		}

		loader.newLEQDegreeCurricularPlan = loader.persistentObjectOJB.readDegreeCurricularPlanByName(loader.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN);
		if (loader.newLEQDegreeCurricularPlan == null) {
			logString = "o plano curricular " + loader.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN + " não existe!";
			logString = loader.report(logString);
			loader.writeToFile(logString);
			loader.shutdownDAO();
			return;
		}
		loader.shutdownDAO();

		System.out.println("\nMigrating " + loader.getClassName());
		loader.load();
		logString = loader.report(logString);
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {
		loader.setupDAO();
		loader.printLine(getClassName());
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		int numberTokens = stringTokenizer.countTokens();

		String oldCourseName1ToGiveEquivalence = stringTokenizer.nextToken();
		String yearOfEquivalence = "";
		String newCourseNameToGetEquivalence = "";
		String oldCourseName2ToGiveEquivalence = "";

		if ((numberTokens > 2) && (!oldCourseName1ToGiveEquivalence.startsWith("+"))) {
			newCourseNameToGetEquivalence = stringTokenizer.nextToken();
			yearOfEquivalence = stringTokenizer.nextToken();
		}

		if ((numberTokens > 2) && (oldCourseName1ToGiveEquivalence.startsWith("+"))) {
			oldCourseName2ToGiveEquivalence = stringTokenizer.nextToken();
			oldCourseName1ToGiveEquivalence = oldCourseName1ToGiveEquivalence.substring(1);
			newCourseNameToGetEquivalence = stringTokenizer.nextToken();
		}

		if (numberTokens == 2) {
			newCourseNameToGetEquivalence = stringTokenizer.nextToken();
		}

		ICurricularCourse newCurricularCourse =
			persistentObjectOJB.readCurricularCourseByNameAndDegreeCurricularPlan(newCourseNameToGetEquivalence, this.newLEQDegreeCurricularPlan);
		if (newCurricularCourse == null) {
			loader.numberUntreatableElements++;
			logString += "\nERRO: Na linha ["
				+ (numberLinesProcessed + 1)
				+ "] o curricular course "
				+ newCourseNameToGetEquivalence
				+ " do plano "
				+ this.newLEQDegreeCurricularPlan.getName()
				+ " não existe!";
			loader.shutdownDAO();
			return;
		}

		List oldCurricularCourse1ListForLEQ =
			readListOfCurricularCoursesByDegreeCurricularPlan(oldCourseName1ToGiveEquivalence, this.oldLEQDegreeCurricularPlan);

		List oldCurricularCourse1ListForLQ = readListOfCurricularCoursesByDegreeCurricularPlan(oldCourseName1ToGiveEquivalence, this.oldLQDegreeCurricularPlan);

//		List oldCurricularCourse1ListForLEBL =
//			readListOfCurricularCoursesByDegreeCurricularPlan(oldCourseName1ToGiveEquivalence, this.oldLEBLDegreeCurricularPlan);

		List oldCurricularCourse2ListForLEQ = null;
		List oldCurricularCourse2ListForLQ = null;
//		List oldCurricularCourse2ListForLEBL = null;

		if (!oldCourseName2ToGiveEquivalence.equals("")) {
			oldCurricularCourse2ListForLEQ =
				readListOfCurricularCoursesByDegreeCurricularPlan(oldCourseName2ToGiveEquivalence, this.oldLEQDegreeCurricularPlan);

			oldCurricularCourse2ListForLQ = readListOfCurricularCoursesByDegreeCurricularPlan(oldCourseName2ToGiveEquivalence, this.oldLQDegreeCurricularPlan);

//			oldCurricularCourse2ListForLEBL =
//				readListOfCurricularCoursesByDegreeCurricularPlan(oldCourseName2ToGiveEquivalence, this.oldLEBLDegreeCurricularPlan);

		}

		if (oldCurricularCourse1ListForLEQ != null) {

			processCurricularCoursesEquivalencesForDegreeCurricularPlan(
				oldCurricularCourse1ListForLEQ,
				newCurricularCourse,
				yearOfEquivalence,
				oldCurricularCourse2ListForLEQ);
		}
		if (oldCurricularCourse1ListForLQ != null) {
			processCurricularCoursesEquivalencesForDegreeCurricularPlan(
				oldCurricularCourse1ListForLQ,
				newCurricularCourse,
				yearOfEquivalence,
				oldCurricularCourse2ListForLQ);
		}
		if (oldCurricularCourse1ListForLQ != null) {
			processCurricularCoursesEquivalencesForDegreeCurricularPlan(
				oldCurricularCourse1ListForLQ,
				newCurricularCourse,
				yearOfEquivalence,
				oldCurricularCourse2ListForLQ);
		}
		
		loader.shutdownDAO();
	}

	private List readListOfCurricularCoursesByDegreeCurricularPlan(String oldCourseName1ToGiveEquivalence, IDegreeCurricularPlan plan) {
		List oldCurricularCourse1List = persistentObjectOJB.readListOfCurricularCoursesByNameAndDegreCurricularPlan(oldCourseName1ToGiveEquivalence, plan);
		if ((oldCurricularCourse1List == null) && (plan.equals(this.newLEQDegreeCurricularPlan))) {
			loader.numberUntreatableElements++;
			logString += "\nERRO: Na linha ["
				+ (numberLinesProcessed + 1)
				+ "] o curricular course "
				+ oldCourseName1ToGiveEquivalence
				+ " do plano "
				+ plan.getName()
				+ " não existe!";
		}
		return oldCurricularCourse1List;
	}

	private void processCurricularCoursesEquivalencesForDegreeCurricularPlan(
		List oldCurricularCourse1List,
		ICurricularCourse newCurricularCourse,
		String yearOfEquivalence,
		List oldCurricularCourse2List) {
		Iterator iterator1 = oldCurricularCourse1List.iterator();
		ICurricularCourse oldCurricularCourse1 = null;
		ICurricularCourseEquivalence curricularCourseEquivalence = null;
		while (iterator1.hasNext()) {
			curricularCourseEquivalence = new CurricularCourseEquivalence();
			curricularCourseEquivalence.setCurricularCourse(newCurricularCourse);
			writeElement(curricularCourseEquivalence);
			oldCurricularCourse1 = (ICurricularCourse) iterator1.next();
			ICurricularCourseEquivalenceRestriction curricularCourseEquivalenceRestricition1 =
				persistentObjectOJB.readCurricularCourseEquivalenceRestrictionByUnique(oldCurricularCourse1, curricularCourseEquivalence, yearOfEquivalence);
			if (curricularCourseEquivalenceRestricition1 == null) {
				curricularCourseEquivalenceRestricition1 = new CurricularCourseEquivalenceRestriction();
				curricularCourseEquivalenceRestricition1.setCurricularCourseEquivalence(curricularCourseEquivalence);
				curricularCourseEquivalenceRestricition1.setEquivalentCurricularCourse(oldCurricularCourse1);
				curricularCourseEquivalenceRestricition1.setYearOfEquivalence(yearOfEquivalence);
				writeElement(curricularCourseEquivalenceRestricition1);
				numberElementsWritten--;
			} else {
				logString += "\nERRO: Na linha ["
					+ (numberLinesProcessed + 1)
					+ "] já existe uma equivalencia entre "
					+ oldCurricularCourse1.getName()
					+ " e "
					+ curricularCourseEquivalence.getCurricularCourse().getName();
			}

			if (oldCurricularCourse2List != null) {
				Iterator iterator2 = oldCurricularCourse2List.iterator();
				ICurricularCourse oldCurricularCourse2 = null;
				while (iterator2.hasNext()) {
					oldCurricularCourse2 = (ICurricularCourse) iterator2.next();
					ICurricularCourseEquivalenceRestriction curricularCourseEquivalenceRestricition2 =
						persistentObjectOJB.readCurricularCourseEquivalenceRestrictionByUnique(
							oldCurricularCourse2,
							curricularCourseEquivalence,
							yearOfEquivalence);
					if (curricularCourseEquivalenceRestricition2 == null) {
						curricularCourseEquivalenceRestricition2 = new CurricularCourseEquivalenceRestriction();
						curricularCourseEquivalenceRestricition2.setCurricularCourseEquivalence(curricularCourseEquivalence);
						curricularCourseEquivalenceRestricition2.setEquivalentCurricularCourse(oldCurricularCourse2);
						curricularCourseEquivalenceRestricition2.setYearOfEquivalence(yearOfEquivalence);
						writeElement(curricularCourseEquivalenceRestricition2);
						numberElementsWritten--;
					} else {
						logString += "\nERRO: Na linha ["
							+ (numberLinesProcessed + 1)
							+ "] já existe uma equivalencia entre "
							+ oldCurricularCourse2.getName()
							+ " e "
							+ curricularCourseEquivalence.getCurricularCourse().getName();
					}

					if (iterator2.hasNext()) {
						curricularCourseEquivalence = new CurricularCourseEquivalence();
						curricularCourseEquivalence.setCurricularCourse(newCurricularCourse);
						writeElement(curricularCourseEquivalence);
						curricularCourseEquivalenceRestricition1 = new CurricularCourseEquivalenceRestriction();
						curricularCourseEquivalenceRestricition1.setCurricularCourseEquivalence(curricularCourseEquivalence);
						curricularCourseEquivalenceRestricition1.setEquivalentCurricularCourse(oldCurricularCourse1);
						curricularCourseEquivalenceRestricition1.setYearOfEquivalence(yearOfEquivalence);
						writeElement(curricularCourseEquivalenceRestricition1);
						numberElementsWritten--;
					}
				}
			}
		}

	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaLEQData/" + this.inputFilename;
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadCurricularCourses" + this.inputFilename;
	}

	protected String getClassName() {
		return "LoadCurricularCoursesEquivalencesFromFileToTable";
	}
}