/*
 * Created on May 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.thor;

import java.util.Calendar;
import java.util.List;

import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;

import middleware.LoadDataFile;

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

	private LoadExecutionCoursesAndAssociations() {
		super();
		persistentObjectOJB = new PersistentObjectOJBReader();
	}

	public static void main(String[] args) {
		loader = new LoadExecutionCoursesAndAssociations();
		loader.startTime = Calendar.getInstance();
		loader.setupDAO();

		loader.processData();

		loader.shutdownDAO();
		loader.endTime = Calendar.getInstance();

		loader.report();
	}

	protected void processData() {

		List allRooms = persistentObjectOJB.readThorRooms();
		int numAulas = 0;

		for (int dia = 1; dia < 7; dia++) {
			for (int hora = 1; hora < 31; hora++) {
				for (int s = 0; s < allRooms.size(); s++) {
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


						IDisciplinaExecucao disciplinaExecucao = persistentObjectOJB.readExecutionCourse();
						if (disciplinaExecucao == null) {
								disciplinaExecucao = new DisciplinaExecucao();
								disciplinaExecucao.setNome(disciplina.getNome());
								disciplinaExecucao.setSigla(disciplina.getSigla());
								disciplinaExecucao.setAssociatedExams(null);
								disciplinaExecucao.setComment(null);
//								disciplinaExecucao.setAssociatedCurricularCourses(null);
//								disciplinaExecucao.setExecutionPeriod(null);
//								disciplinaExecucao.setTheoreticalHours(disciplina.get);
//								disciplinaExecucao.setPraticalHours(null);
//								disciplinaExecucao.setTheoPratHours(null);
//								disciplinaExecucao.setLabHours(null);
//								writeElement(disciplinaExecucao);                      
						}

						//Ler Curricular correspondente e se ja existir na BD criar associacao
						ICurricularCourse curricularCourse =
							persistentObjectOJB.readCurricularCourse(
								degreeID,
								disciplina.getSigla());
						if (curricularCourse != null) {
							
						}
						else {
							
						}
					}
				}
			}
		}
		System.out.println("Numero de aulas processadas = " + numAulas);
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
		return null;
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