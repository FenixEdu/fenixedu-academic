/*
 * Created on May 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.thor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import middleware.LoadDataFile;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class LoadExecutionCoursesAndAssociations extends LoadDataFile {

	protected static int numberElementsRead = 0;
	protected static int numberElementsWritten = 0;
	protected static int numberUntreatableElements = 0;

	private static LoadExecutionCoursesAndAssociations loader = null;

	protected PersistentObjectOJBReader persistentObjectOJB = null;

	static String bufferToWrite = new String();

	HashMap hashMap = new HashMap();

	private LoadExecutionCoursesAndAssociations() {
		super();
		persistentObjectOJB = new PersistentObjectOJBReader();
	}

	public static void main(String[] args) {
		loader = new LoadExecutionCoursesAndAssociations();
		loader.startTime = Calendar.getInstance();
		loader.setupDAO();

		System.out.println("Processing Execution Course Data.");

		loader.processData();

		loader.shutdownDAO();
		loader.endTime = Calendar.getInstance();

		loader.report();
		loader.writeToFile(bufferToWrite);
	}

	protected void processData() {

		List allRooms = persistentObjectOJB.readThorRooms();
		int numAulas = 0;

		IExecutionPeriod executionPeriod =
			persistentObjectOJB.readExecutionPeriod();

		for (int s = 0; s < allRooms.size(); s++) {
			System.out.println("sala= " + allRooms.get(s));
			for (int hora = 1; hora < 31; hora++) {
				for (int dia = 1; dia < 7; dia++) {

					String sala = ((String) allRooms.get(s));
					List aulas =
						persistentObjectOJB.readThorAulas(dia, hora, sala);
					for (int a = 0; a < aulas.size(); a++) {
						numAulas++;
						Aulas aula = ((Aulas) aulas.get(a));
						Integer degreeID =
							new Integer(aula.getTurma().substring(0, 2));
						Disciplinas disciplina =
							persistentObjectOJB.readDisciplina(
								aula.getDisciplina());

						IDisciplinaExecucao disciplinaExecucao =
							persistentObjectOJB.readExecutionCourse(disciplina);
						if (disciplinaExecucao == null) {
							disciplinaExecucao = new DisciplinaExecucao();
							disciplinaExecucao.setNome(disciplina.getNome());
							disciplinaExecucao.setSigla(disciplina.getSigla());
							disciplinaExecucao.setAssociatedExams(null);
							disciplinaExecucao.setComment(null);
							ICurricularCourse curricularCourseToAdd =
								getAssociated(
									disciplinaExecucao,
									aula,
									disciplina);
							if (curricularCourseToAdd != null
								&& disciplinaExecucao
									.getAssociatedCurricularCourses()
									!= null) {
								disciplinaExecucao
									.getAssociatedCurricularCourses()
									.add(
									curricularCourseToAdd);
							}
							disciplinaExecucao.setExecutionPeriod(
								executionPeriod);
							disciplinaExecucao.setTheoreticalHours(null);
							disciplinaExecucao.setPraticalHours(null);
							disciplinaExecucao.setTheoPratHours(null);
							disciplinaExecucao.setLabHours(null);
							disciplinaExecucao.setComment(" ");

							if (curricularCourseToAdd != null) {
								numberElementsWritten++;
								writeElement(disciplinaExecucao);
							} else {
							}

						}

						//Ler Curricular correspondente e se ja existir na BD criar associacao
						ICurricularCourse curricularCourse =
							getAssociated(
								disciplinaExecucao,
								aula,
								disciplina);
								
						if (curricularCourse != null) {
							List associatedCurricularCourses =
								(List) hashMap.get(
									disciplinaExecucao.getIdInternal());
							if (associatedCurricularCourses == null) {
								associatedCurricularCourses = new ArrayList();
								hashMap.put(
									disciplinaExecucao.getIdInternal(),
									associatedCurricularCourses);
							}

							if (!associatedCurricularCourses
								.contains(curricularCourse.getIdInternal())) {
								bufferToWrite
									+= "insert into CURRICULAR_COURSE_EXECUTION_COURSE values (null, "
									+ curricularCourse.getIdInternal()
									+ ", "
									+ disciplinaExecucao.getIdInternal()
									+ ");\n";
								associatedCurricularCourses.add(curricularCourse.getIdInternal());
							} else {
								
							}
						} else {

						}
					}
				}
			}
		}
		System.out.println("Numero de aulas processadas = " + numAulas);
	}

	/**
	 * @param disciplinaExecucao
	 * @param aula
	 * @param disciplina
	 * @return
	 */
	private ICurricularCourse getAssociated(
		IDisciplinaExecucao disciplinaExecucao,
		Aulas aula,
		Disciplinas disciplina) {

		IDegreeCurricularPlan degreeCurricularPlan =
			persistentObjectOJB.readDegreeCurricularPlanNextSemester(
				aula.getTurma().substring(0, 2));

		if (degreeCurricularPlan != null) {
			ICurricularCourse curricularCourse =
				persistentObjectOJB.getCurricularCourse(
					degreeCurricularPlan,
					disciplina);
			if (curricularCourse != null) {
				curricularCourse.getAssociatedExecutionCourses().add(
					disciplinaExecucao);

				return curricularCourse;
			} else {
				numberUntreatableElements++;
			}

		} else {
			// It's not untreatable, we just don't have the curricular plan 
			//numberUntreatableElements++;
		}

		return null;
	}

	protected void processLine(String line) {
	}

	protected String getFilename() {
		return null;
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "CurricularCourseExecutionCourse.txt";
	}

	protected void report() {
		long duration =
			(endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
		long durationHour = duration / 3600;
		long durationMin = (duration % 3600) / 60;
		long durationSec = (duration % 3600) % 60;

		System.out.println(
			"----------------------------------------------------------------");
		System.out.println("   Report for loading of file: " + getFilename());
		System.out.println(
			"      Number of elements read: " + numberElementsRead);
		System.out.println(
			"      Number of elements added: " + numberElementsWritten);
		System.out.println(
			"      Number of untreatable elements: "
				+ numberUntreatableElements);
		System.out.println(
			"      Total processing time: "
				+ durationHour
				+ "h "
				+ durationMin
				+ "m "
				+ durationSec
				+ "s");
		System.out.println(
			"----------------------------------------------------------------");
	}

}