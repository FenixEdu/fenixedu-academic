package middleware.curriculum;

import java.util.List;
import java.util.ListIterator;

import Dominio.EvaluationMethod;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluationMethod;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Created on 24/Out/2003
 */
/********************************************************************************************/
//this file is not used anymore
/********************************************************************************************/
public class migrationEvaluationElements {
	//This class migrates all evaluation elements in curriculum table
	//for a evalaution method table.
	//And more, at evaluation method table a key
	//already it is not for curricular course but for execution course

	public static void main(String[] args) throws Exception {
		System.out.println("Reading evaluation elements ....");
		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
		IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

		//read all curriculum in database
		sp.iniciarTransaccao();
		List curriculumsList = persistentCurriculum.readAll();
		sp.confirmarTransaccao();

		if (curriculumsList != null && curriculumsList.size() > 0) {
			System.out.println("Were readed " + curriculumsList.size() + " evaluation methods in curriculum....");
			System.out.println("Curriculum are " + curriculumsList.size());

			ListIterator iterator = curriculumsList.listIterator();
			int i = 0;
			while (iterator.hasNext()) {
				ICurriculum curriculum = (ICurriculum) iterator.next();

				i++;
				System.out.println("-->" + i + " Curriculum: " + curriculum.getIdInternal());

				//it´s necessary find all the execution courses of this curricular course
				//and create a evaluation method for each one execution course
				ICurricularCourse curricularCourse = curriculum.getCurricularCourse();
				if (curricularCourse != null) {
					List executionCoursesList = curriculum.getCurricularCourse().getAssociatedExecutionCourses();
					if (executionCoursesList != null && executionCoursesList.size() > 0) {
						System.out.println("ExecutionCourses " + executionCoursesList.size());

						IPersistentEvaluationMethod persistentEvaluationMethod =
							SuportePersistenteOJB.getInstance().getIPersistentEvaluationMethod();

						ListIterator iterator2 = executionCoursesList.listIterator();
						while (iterator2.hasNext()) {
							IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) iterator2.next();
							System.out.println("Writing EvaluationMethods of the execution course " + executionCourse.getIdInternal());

							//if (curriculum.getEvaluationElements() != null && curriculum.getEvaluationElements().length() > 0 &&
							//curriculum.getEvaluationElementsEn() != null && curriculum.getEvaluationElementsEn().length() > 0) {
								//put Evaluation Elements of the curriculum in Evaluation Method Objects
								IEvaluationMethod evaluationMethod = new EvaluationMethod();
//								evaluationMethod.setEvaluationElements(curriculum.getEvaluationElements());
//								evaluationMethod.setEvaluationElementsEn(curriculum.getEvaluationElementsEn());

								evaluationMethod.setExecutionCourse(executionCourse);
								evaluationMethod.setKeyExecutionCourse(executionCourse.getIdInternal());

								sp.iniciarTransaccao();
								persistentEvaluationMethod.lockWrite(evaluationMethod);
								sp.confirmarTransaccao();
							//}
						}
					} else {
						System.out.println("Don't exist execution course!!");
					}
				} else {
					System.out.println("Don't exist curricular course!!");
				}
			}
		}
	}
}
