/*
 * Created on Apr 16, 2004
 */
package middleware.mathResponsibles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import pt.utl.ist.berserk.storage.exceptions.StorageException;
import Dominio.Curso;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IPessoa;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.Professorship;
import Dominio.ResponsibleFor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;
import Util.PeriodState;
import Util.TipoCurso;
/**
 * @author João Mota
 */
public class UpdateMathResponsibles {
	public static void main(String[] args) {
		try {
			SuportePersistenteOJB persistentSuport = SuportePersistenteOJB
					.getInstance();
			persistentSuport.iniciarTransaccao();
			PersistenceBroker broker = persistentSuport.currentBroker();
			Criteria crit1 = new Criteria();
			crit1.addEqualTo("Semestre", "2");
			Query query1 = new QueryByCriteria(Math_RESPONSAVEIS.class, crit1);
			List mathResponsibles = (List) broker.getCollectionByQuery(query1);
			persistentSuport.commitTransaction();
			Iterator iter = mathResponsibles.iterator();
			List responsiblesWithoutExecutionCourse = new ArrayList();
			List nonExistingTeachers = new ArrayList();
			List executionCourses = new ArrayList();
			Map responsibles = new HashMap();
			while (iter.hasNext()) {
				persistentSuport.iniciarTransaccao();
				Math_RESPONSAVEIS mathResponsible = (Math_RESPONSAVEIS) iter
						.next();
				IExecutionCourse executionCourse = findExecutionCourse(
						mathResponsible, persistentSuport, broker);
				if (executionCourse == null) {
					responsiblesWithoutExecutionCourse.add(mathResponsible);
				} else {
					List responsiblesToAdd = (List) responsibles
							.get(executionCourse.getIdInternal());
					if (responsiblesToAdd == null) {
						responsiblesToAdd = new ArrayList();
					}
					ITeacher teacher = findTeacher(mathResponsible,
							persistentSuport);
					if (teacher == null) {
						nonExistingTeachers.add(mathResponsible);
					} else {
						IResponsibleFor responsibleFor = findResponsibleFor(
								executionCourse, teacher, responsiblesToAdd);
						if (responsibleFor == null) {
							responsiblesToAdd.add(createResponsibleFor(
									executionCourse, teacher));
						}
						responsibles.put(executionCourse.getIdInternal(),
								responsiblesToAdd);
						if (!executionCourses.contains(executionCourse)) {
							executionCourses.add(executionCourse);
						}
					}
				}
				persistentSuport.confirmarTransaccao();
			}
			Iterator iter2 = executionCourses.iterator();
			while (iter2.hasNext()) {
				IExecutionCourse executionCourse = (IExecutionCourse) iter2
						.next();
				persistentSuport.iniciarTransaccao();
				deleteResponsibleFors(executionCourse, persistentSuport);
				persistentSuport.confirmarTransaccao();
				persistentSuport.iniciarTransaccao();
				List responsiblesToWrite = (List) responsibles
						.get(executionCourse.getIdInternal());
				writeResponsibles(responsiblesToWrite, persistentSuport);
				persistentSuport.confirmarTransaccao();
			}
			System.out.println("Ocorreram os seguintes erros:");
			System.out.println("disciplinas sem execução:");
			printList(responsiblesWithoutExecutionCourse);
			System.out
					.println("###########################################################");
			System.out
					.println("###########################################################");
			System.out.println("docentes que não existem:");
			printList(nonExistingTeachers);
			System.out.println("The End");
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param list
	 */
	private static void printList(List list) {
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Math_RESPONSAVEIS math = (Math_RESPONSAVEIS) iter.next();
			System.out.println("id: " + math.getIdInternal());
			System.out.println("Abreviatura_do_Curso: "
					+ math.getAbreviatura_do_Curso());
			System.out.println("Ano: " + math.getAno());
			System.out.println("codigo disc: "
					+ math.getCodigo_da_disciplina_no_IST());
			System.out.println("nome disc: " + math.getNome_da_disciplina());
			System.out.println("semestre: " + math.getSemestre());
			System.out.println("nome resp: " + math.getNome_do_responsavel());
			System.out.println("--------------------------");
		}
	}
	/**
	 * @param responsiblesToWrite
	 */
	private static void writeResponsibles(List responsiblesToWrite,
			SuportePersistenteOJB persistentSuport) throws ExcepcaoPersistencia {
		IPersistentResponsibleFor persistentResponsibleFor = persistentSuport
				.getIPersistentResponsibleFor();
		IPersistentProfessorship persistentProfessorship = persistentSuport
				.getIPersistentProfessorship();
		Iterator iter = responsiblesToWrite.iterator();
		while (iter.hasNext()) {
			IResponsibleFor responsibleFor2write = (IResponsibleFor) iter
					.next();
			IResponsibleFor newResponsibleFor = new ResponsibleFor();
			persistentResponsibleFor.simpleLockWrite(newResponsibleFor);
			newResponsibleFor.setTeacher(responsibleFor2write.getTeacher());
			newResponsibleFor.setExecutionCourse(responsibleFor2write
					.getExecutionCourse());
			IProfessorship professorship = persistentProfessorship
					.readByTeacherAndExecutionCourse(responsibleFor2write
							.getTeacher(), responsibleFor2write
							.getExecutionCourse());
			if (professorship == null) {
				professorship = new Professorship();
				persistentProfessorship.simpleLockWrite(professorship);
				professorship.setTeacher(responsibleFor2write.getTeacher());
				professorship.setExecutionCourse(responsibleFor2write
						.getExecutionCourse());
			}
		}
	}
	/**
	 * @param executionCourse
	 * @param persistentSuport
	 */
	private static void deleteResponsibleFors(IExecutionCourse executionCourse,
			SuportePersistenteOJB persistentSuport) throws ExcepcaoPersistencia {
		IPersistentResponsibleFor persistentResponsibleFor = persistentSuport
				.getIPersistentResponsibleFor();
		List responsibles = persistentResponsibleFor
				.readByExecutionCourse(executionCourse);
		Iterator iter = responsibles.iterator();
		while (iter.hasNext()) {
			IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
			persistentResponsibleFor.deleteByOID(ResponsibleFor.class,
					responsibleFor.getIdInternal());
		}
	}
	/**
	 * @param executionCourse
	 * @param teacher
	 * @return
	 */
	private static IResponsibleFor createResponsibleFor(
			IExecutionCourse executionCourse, ITeacher teacher) {
		IResponsibleFor responsibleFor = new ResponsibleFor();
		responsibleFor.setExecutionCourse(executionCourse);
		responsibleFor.setTeacher(teacher);
		return responsibleFor;
	}
	/**
	 * @param executionCourse
	 * @param teacher
	 * @param persistentSuport
	 * @return
	 */
	private static IResponsibleFor findResponsibleFor(
			final IExecutionCourse executionCourse, final ITeacher teacher,
			List responsiblesToAdd) {
		IResponsibleFor responsibleFor = (IResponsibleFor) CollectionUtils
				.find(responsiblesToAdd, new Predicate() {
					public boolean evaluate(Object arg0) {
						IResponsibleFor responsibleFor = (IResponsibleFor) arg0;
						if (responsibleFor.getExecutionCourse().getIdInternal()
								.equals(executionCourse.getIdInternal())
								&& responsibleFor.getTeacher().getIdInternal()
										.equals(teacher.getIdInternal())) {
							return true;
						} else {
							return false;
						}
					}
				});
		return responsibleFor;
	}
	/**
	 * @param mathResponsible
	 * @param persistentSuport
	 * @return
	 */
	private static ITeacher findTeacher(Math_RESPONSAVEIS mathResponsible,
			SuportePersistenteOJB persistentSuport) throws ExcepcaoPersistencia {
		IPersistentTeacher persistentTeacher = persistentSuport
				.getIPersistentTeacher();
		IPessoaPersistente persistentPerson = persistentSuport
				.getIPessoaPersistente();
		List persons = persistentPerson.findPersonByName(mathResponsible
				.getNome_do_responsavel().replaceAll(" ", "%"));
		if (persons == null || persons.isEmpty()) {
			return null;
		} else {
			IPessoa person = (IPessoa) persons.get(0);
			ITeacher teacher = persistentTeacher.readTeacherByUsername(person
					.getUsername());
			return teacher;
		}
	}
	/**
	 * @param mathResponsible
	 * @param persistentSuport
	 * @param broker
	 * @return
	 */
	private static IExecutionCourse findExecutionCourse(
			Math_RESPONSAVEIS mathResponsible,
			SuportePersistenteOJB persistentSuport, PersistenceBroker broker)
			throws ExcepcaoPersistencia {
		IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
				.getIPersistentCurricularCourse();
		ICursoPersistente persistentDegree = persistentSuport
				.getICursoPersistente();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
				.getIPersistentDegreeCurricularPlan();
		Integer degreeId = new Integer(mathResponsible.getCodigo_do_Curso());
		if (degreeId.intValue() == 109) {
			degreeId = new Integer(10);
		}
		if (degreeId.intValue() == 0) {
			degreeId = new Integer(27);
		}
		if (degreeId.intValue() == 98) {
			degreeId = new Integer(43);
		}
		if (degreeId.intValue() == 24) {
			degreeId = new Integer(51);
		}
		ICurso degree = (ICurso) persistentDegree.readByOID(Curso.class,
				degreeId);
		List curricularPlans = persistentDegreeCurricularPlan
				.readByDegreeAndState(degree, new DegreeCurricularPlanState(
						DegreeCurricularPlanState.ACTIVE));
		Iterator iter = curricularPlans.iterator();
		List degreeCurricularPlans = new ArrayList();
		while (iter.hasNext()) {
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iter
					.next();
			if (degreeCurricularPlan.getName().endsWith("2003/2004")
					|| degreeCurricularPlan.getName().endsWith(
							"LEIC - Currículo Antigo")
					|| degreeCurricularPlan.getName().endsWith("LEIC 2003")
					|| degreeCurricularPlan.getName().endsWith("03/05")
                    || degreeCurricularPlan.getName().endsWith("02/04")) {
				degreeCurricularPlans.add(degreeCurricularPlan);
			}
		}
		iter = degreeCurricularPlans.iterator();
		List curricularCourses = new ArrayList();
		while (iter.hasNext()) {
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iter
					.next();
			List courses;
            
			if (degreeCurricularPlan.getDegree().getTipoCurso().equals(TipoCurso.MESTRADO_OBJ)||
                    mathResponsible.getAbreviatura_do_Curso().equals("Mestrado")) {
				courses = persistentCurricularCourse
						.readbyCourseNameAndDegreeCurricularPlan(
								mathResponsible.getNome_da_disciplina(),
								degreeCurricularPlan);
                
			} else {
				courses = persistentCurricularCourse
						.readbyCourseCodeAndDegreeCurricularPlan(
								mathResponsible
										.getCodigo_da_disciplina_no_IST(),
								degreeCurricularPlan);
				if (mathResponsible.getNome_da_disciplina_no_IST()
						.indexOf(" I") != -1
						|| mathResponsible.getNome_da_disciplina().equals(
								"Álgebra Linear")) {
					curricularCourses.addAll(persistentCurricularCourse
							.readbyCourseNameAndDegreeCurricularPlan(
									mathResponsible
											.getNome_da_disciplina()
											+ " A", degreeCurricularPlan));
				}
			}
			curricularCourses.addAll(courses);
		}
		iter = curricularCourses.iterator();
		List executionCoursesOfCurricularCourses = new ArrayList();
		while (iter.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iter
					.next();
			List executionCourses = curricularCourse
					.getAssociatedExecutionCourses();
			IExecutionCourse executionCourse = (IExecutionCourse) CollectionUtils
					.find(executionCourses, new Predicate() {
						public boolean evaluate(Object arg0) {
							IExecutionCourse executionCourse = (IExecutionCourse) arg0;
							if (executionCourse.getExecutionPeriod().getState()
									.equals(PeriodState.CURRENT)) {
								return true;
							} else {
								return false;
							}
						}
					});
			if (executionCourse != null
					&& !executionCoursesOfCurricularCourses
							.contains(executionCourse)) {
				executionCoursesOfCurricularCourses.add(executionCourse);
			}
		}
		if (executionCoursesOfCurricularCourses.size() > 0) {
			return (IExecutionCourse) executionCoursesOfCurricularCourses
					.get(0);
		} else {
			return null;
		}
	}
}