/*
 * Created on May 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.thor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.LoadDataFile;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
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

	HashMap hashMapCode = new HashMap();

	HashMap hashMapLessons = new HashMap();

	List executionCourses = new ArrayList();

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

		List allRooms =
		/*new ArrayList();
				allRooms.add("VA4");
				allRooms.add("V109");
				allRooms.add("VA3");
				allRooms.add("V004");
				allRooms.add("QA02.1");
				allRooms.add("V124");
				allRooms.add("GA1");
				allRooms.add("V133");
				allRooms.add("V123");
				allRooms.add("V131");
				allRooms.add("GA5");
				allRooms.add("QA02.2");
				allRooms.add("C22");
				allRooms.add("QA02.4");
				allRooms.add("Q4.4");
				allRooms.add("Q4.6");
				allRooms.add("Q4.1");
				allRooms.add("V116");
				allRooms.add("VA5");
				allRooms.add("V135");
				allRooms.add("E3");
				allRooms.add("GA3");
				allRooms.add("PA1");
				allRooms.add("V126");
				allRooms.add("GA2");
				allRooms.add("C9");
				allRooms.add("V138");
				allRooms.add("F3");
				allRooms.add("C11");
				allRooms.add("V111");
				allRooms.add("GA4");
				allRooms.add("EA4");
				allRooms.add("0.2.20");
				allRooms.add("0.2.18");
				allRooms.add("0.2.1");
				allRooms.add("Q4.5");
				allRooms.add("V110");
				allRooms.add("Q5.2");
				allRooms.add("V108");
				allRooms.add("V112");
				allRooms.add("Q5.1");
				allRooms.add("P12");
				allRooms.add("C12");
				allRooms.add("F4");
				allRooms.add("C23");
				allRooms.add("EA5");
				allRooms.add("EA1");
				allRooms.add("0.2.11");
				allRooms.add("0.2.4"); */
		persistentObjectOJB.readThorRooms();
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
						//if (aula.getDisciplina().equals("AL")) {

						Integer degreeID =
							new Integer(aula.getTurma().substring(0, 2));
						Disciplinas disciplina =
							persistentObjectOJB.readDisciplina(
								aula.getDisciplina());

						CourseDegreesPair courseDegreesPair =
							(CourseDegreesPair) hashMapLessons.get(
								aula.getHashKey());
						if (courseDegreesPair == null) {
							courseDegreesPair =
								new CourseDegreesPair(
									disciplina,
									new ArrayList(),
									new ArrayList(),
									new ArrayList());
							courseDegreesPair.getNames().add(
								disciplina.getNome());
							courseDegreesPair.getCodes().add(
								disciplina.getSigla());
							courseDegreesPair.getDegrees().add(degreeID);
							hashMapLessons.put(
								aula.getHashKey(),
								courseDegreesPair);
						} else {
							if (!courseDegreesPair
								.getDegrees()
								.contains(degreeID)) {
								courseDegreesPair.getDegrees().add(degreeID);
								if (courseDegreesPair.getCourse().getNome()
									!= disciplina.getNome()) {
									courseDegreesPair.getNames().add(
										disciplina.getNome());
									courseDegreesPair.getCodes().add(
										disciplina.getSigla());
									System.out.println(
										"Lesson with different names: "
											+ courseDegreesPair
												.getCourse()
												.getNome()
											+ " != "
											+ disciplina.getNome());
								}
							}
						}
					}
				}
				//}
			}
		}

		System.out.println("hasMap size = " + hashMapLessons.size());

		Collection lessonsHM = hashMapLessons.values();
		Iterator iterator = lessonsHM.iterator();
		while (iterator.hasNext()) {
			final CourseDegreesPair courseDegreesPair =
				(CourseDegreesPair) iterator.next();

			List matchingExecutionCourses =
				(
					List) CollectionUtils
						.select(executionCourses, new Predicate() {
				public boolean evaluate(Object obj) {
					return courseDegreesPair.equals(obj);
				}
			});

			//int pos = executionCourses.indexOf(courseDegreesPair);

			if (matchingExecutionCourses != null
				&& !matchingExecutionCourses.isEmpty() /*  pos > -1*/
				) {
				executionCourses.removeAll(matchingExecutionCourses);

				for (int k = 0; k < matchingExecutionCourses.size(); k++) {

					//					System.out.println("### Before Merge, pos > -1");
					//					System.out.println("name = " + courseDegreesPair.getNome());
					//					for (int j = 0;
					//						j < courseDegreesPair.getDegrees().size();
					//						j++) {
					//						System.out.println(
					//							"degree["
					//								+ j
					//								+ "] = "
					//								+ courseDegreesPair.getDegrees().get(j));
					//					}

					((CourseDegreesPair) matchingExecutionCourses.get(k))
						.getDegrees()
						.removeAll(
						courseDegreesPair.getDegrees());
					courseDegreesPair.getDegrees().addAll(
						((CourseDegreesPair) matchingExecutionCourses.get(k))
							.getDegrees());

					((CourseDegreesPair) matchingExecutionCourses.get(k))
						.getNames()
						.removeAll(
						courseDegreesPair.getNames());
					courseDegreesPair.getNames().addAll(
						((CourseDegreesPair) matchingExecutionCourses.get(k))
							.getNames());

					((CourseDegreesPair) matchingExecutionCourses.get(k))
						.getCodes()
						.removeAll(
						courseDegreesPair.getCodes());
					courseDegreesPair.getCodes().addAll(
						((CourseDegreesPair) matchingExecutionCourses.get(k))
							.getCodes());

				}

				executionCourses.add(courseDegreesPair);

			} else {
				//				System.out.println("### No Merge, pos = -1");
				//				System.out.println("name = " + courseDegreesPair.getNome());
				//				for (int j = 0;
				//					j < courseDegreesPair.getDegrees().size();
				//					j++) {
				//					System.out.println(
				//						"degree["
				//							+ j
				//							+ "] = "
				//							+ courseDegreesPair.getDegrees().get(j));
				//				}
				executionCourses.add(courseDegreesPair);
			}
		}

		for (int i = 0; i < executionCourses.size(); i++) {
			CourseDegreesPair courseDegreesPair =
				(CourseDegreesPair) executionCourses.get(i);

			System.out.println("name = " + courseDegreesPair.getNome());
			for (int j = 0; j < courseDegreesPair.getDegrees().size(); j++) {
				System.out.println(
					"degree["
						+ j
						+ "] = "
						+ courseDegreesPair.getDegrees().get(j));
			}

			//Disciplinas disciplina = courseDegreesPair.getCourse();

			Integer numElements =
				(Integer) hashMapCode.get(courseDegreesPair.getSigla());
			String siglaDE = courseDegreesPair.getSigla();

			if (numElements != null) {
				siglaDE += "-" + numElements.intValue() + 1;
			}

			IDisciplinaExecucao disciplinaExecucao = null;
			//				persistentObjectOJB.readExecutionCourse(siglaDE);

			for (int j = 0; j < courseDegreesPair.degrees.size(); j++) {
				ICurricularCourse curricularCourse =
					getAssociated(
						(Integer) courseDegreesPair.degrees.get(j),
						courseDegreesPair);

				if (curricularCourse != null
					&& curricularCourse.getAssociatedExecutionCourses() != null
					&& !curricularCourse
						.getAssociatedExecutionCourses()
						.isEmpty()) {
					disciplinaExecucao =
						(IDisciplinaExecucao) curricularCourse
							.getAssociatedExecutionCourses()
							.get(
							0);
				}

			}

			if (disciplinaExecucao == null) {

				disciplinaExecucao = new DisciplinaExecucao();
				disciplinaExecucao.setSigla(siglaDE);
				disciplinaExecucao.setNome(courseDegreesPair.getNome());
				disciplinaExecucao.setAssociatedExams(null);
				disciplinaExecucao.setExecutionPeriod(executionPeriod);
				disciplinaExecucao.setTheoreticalHours(new Double(0));
				disciplinaExecucao.setPraticalHours(new Double(0));
				disciplinaExecucao.setTheoPratHours(new Double(0));
				disciplinaExecucao.setLabHours(new Double(0));
				disciplinaExecucao.setComment(" ");
				disciplinaExecucao.setAssociatedCurricularCourses(
					new ArrayList());
			} else {
			}

			//boolean writeExecutionCourse = false;
			for (int j = 0; j < courseDegreesPair.getDegrees().size(); j++) {
				Integer degree =
					(Integer) courseDegreesPair.getDegrees().get(j);

				ICurricularCourse curricularCourse =
					getAssociated(degree, courseDegreesPair);
				if (curricularCourse != null) {
					disciplinaExecucao.getAssociatedCurricularCourses().add(
						curricularCourse);
				}
			}

			if (disciplinaExecucao.getAssociatedCurricularCourses().size()
				> 0) {

				ICurricularCourse curricularCourse =
					(ICurricularCourse) disciplinaExecucao
						.getAssociatedCurricularCourses()
						.get(
						0);

				ICurricularCourseScope curricularCourseScope =
					(ICurricularCourseScope) curricularCourse.getScopes().get(
						0);

				disciplinaExecucao.setTheoreticalHours(
					curricularCourseScope.getTheoreticalHours());
				disciplinaExecucao.setPraticalHours(
					curricularCourseScope.getPraticalHours());
				disciplinaExecucao.setTheoPratHours(
					curricularCourseScope.getTheoPratHours());
				disciplinaExecucao.setLabHours(
					curricularCourseScope.getLabHours());

				if (numElements == null) {
					hashMapCode.put(
						courseDegreesPair.getSigla(),
						new Integer(0));
				} else {
					hashMapCode.remove(courseDegreesPair.getSigla());
					hashMapCode.put(
						courseDegreesPair.getSigla(),
						new Integer(numElements.intValue() + 1));
				}

				writeElement(disciplinaExecucao);
			}

		}

	}

	/**
	 * @param disciplinaExecucao
	 * @param aula
	 * @param disciplina
	 * @return
	 */
	private ICurricularCourse getAssociated(
		Integer degree,
		CourseDegreesPair pair) {

		IDegreeCurricularPlan degreeCurricularPlan =
			persistentObjectOJB.readDegreeCurricularPlanNextSemester(
				degree.toString());

		if (degreeCurricularPlan != null) {

			ICurricularCourse curricularCourse = null;
			Iterator iterator = pair.names.iterator();
			while (curricularCourse == null && iterator.hasNext()) {
				String nomeDisciplina = (String) iterator.next();

				ICurricularCourse curricularCourseTemp =
					persistentObjectOJB.getCurricularCourse(
						degreeCurricularPlan,
						nomeDisciplina);

				if (curricularCourseTemp != null)
					curricularCourse = curricularCourseTemp;
			}

			if (curricularCourse != null) {
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

	private class CourseDegreesPair {
		private Disciplinas course;
		List names;
		private List codes;
		List degrees;

		public int hashCode() {
			return 2003;
		}

		public boolean equals(Object obj) {
			boolean resultado = false;
			if (obj instanceof CourseDegreesPair) {
				CourseDegreesPair courseDegreesPair = (CourseDegreesPair) obj;

				resultado =
					CollectionUtils.containsAny(
						this.getNames(),
						courseDegreesPair.getNames())
						&& CollectionUtils.containsAny(
							this.getDegrees(),
							courseDegreesPair.getDegrees());
			}

			return resultado;
		}

		public String getSigla() {
			Collections.sort(codes);

			String sigla = new String();

			for (int i = 0; i < codes.size(); i++) {
				if (i > 0) {
					sigla += "/";
				}
				sigla += codes.get(i);
			}

			return sigla;
		}

		public String getNome() {
			Collections.sort(names);

			String nome = new String();

			for (int i = 0; i < names.size(); i++) {
				if (i > 0) {
					nome += "/";
				}
				nome += names.get(i);
			}

			return nome;
		}

		/**
		 * 
		 */
		public CourseDegreesPair() {
		}

		/**
		 * 
		 */
		public CourseDegreesPair(
			Disciplinas course,
			List degrees,
			List names,
			List codes) {
			setCourse(course);
			setDegrees(degrees);
			setNames(names);
			setCodes(codes);
		}

		/**
		 * @return
		 */
		public Disciplinas getCourse() {
			return course;
		}

		/**
		 * @return
		 */
		public List getDegrees() {
			return degrees;
		}

		/**
		 * @param course
		 */
		public void setCourse(Disciplinas course) {
			this.course = course;
		}

		/**
		 * @param degrees
		 */
		public void setDegrees(List degrees) {
			this.degrees = degrees;
		}

		/**
		 * @return
		 */
		public List getNames() {
			return names;
		}

		/**
		 * @param names
		 */
		public void setNames(List names) {
			this.names = names;
		}

		/**
		 * @return
		 */
		public List getCodes() {
			return codes;
		}

		/**
		 * @param codes
		 */
		public void setCodes(List codes) {
			this.codes = codes;
		}

	}

}