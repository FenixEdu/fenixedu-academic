/**
 * Project Sop 
 * 
 * Package ServidorAplicacao.Servico.sop
 * 
 * Created on 18/Dez/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoClass;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.InfoShiftStudentEnrolment;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.Pessoa;
import Dominio.ShiftStudent;
import Dominio.Student;
import Dominio.TurmaTurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

/**
 * @author tdi-dev
 *
 * 
 */
public class ReadStudentShiftEnrolment implements IServico {
	/**
	 * <code>_service</code> is the instance of the service
	 *
	 */
	private static ReadStudentShiftEnrolment _service = new ReadStudentShiftEnrolment();

	/**
	 * Creates a new <code>ReadStudentShiftEnrolment</code>.
	 *
	 */
	private ReadStudentShiftEnrolment() {
	}

	/**
	 * Describe <code>getService</code> method here.
	 *
	 * @return a <code>ReadStudentShiftEnrolment</code> value
	 */
	public static ReadStudentShiftEnrolment getService() {
		return _service;
	}
	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public final String getNome() {
		return "ReadStudentShiftEnrolment";
	}

	/**
	 * Works with SHIFT_STUDENT table.
	 * Inserts and updates table.
	 * 
	 *
	 * @param infoShiftStudentEnrolment an <code>InfoShiftStudentEnrolment</code> value
	 * @return an <code>InfoShiftStudentEnrolment</code> value
	 * @exception FenixServiceException if an error occurs
	 * @exception ExcepcaoPersistencia if an error occurs
	 */
	public InfoShiftStudentEnrolment run(InfoShiftStudentEnrolment infoShiftStudentEnrolment) throws FenixServiceException, ExcepcaoPersistencia {
		if (infoShiftStudentEnrolment == null) {
			throw new IllegalArgumentException("InfoShiftStudentEnrolment must be not null!");
		}

		InfoStudent infoStudent = infoShiftStudentEnrolment.getInfoStudent();

		IStudent student = new Student();
		IPessoa person = new Pessoa();
		person.setUsername(infoStudent.getInfoPerson().getUsername());
		student.setPerson(person);
		student.setDegreeType(infoStudent.getDegreeType());
		student.setNumber(infoStudent.getNumber());

		//Get the OJB Persistent Support singleton
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();

		//*********************************************************************
		//Get the classes to wich the student is entitled to be in, 
		//  in the current execution year:
		List studentAllowedClasses = getStudentsAllowedClasses(student, sp);

		//Copy these Classes (ITurma objects) to the action
		ClassTransformer classTransformer = new ClassTransformer();

		List allowedClasses = (List) CollectionUtils.collect(studentAllowedClasses, classTransformer);

		// And the result is: shifts with vacancies, of the courses the student is enrolled with
		infoShiftStudentEnrolment.setAllowedClasses(allowedClasses);

		//************************************************
		//Code for filling the currentEnrolment list:

		//Open a OJB entrypoint for the ShiftStudent table
		ITurnoAlunoPersistente shiftStudentDAO = sp.getITurnoAlunoPersistente();

		//read all the shiftStudent for that student
		ITurnoAluno shiftStudentExample = new ShiftStudent();

		shiftStudentExample.setStudent(student);
		List infoShiftStudentEnrolmentTmp;
		infoShiftStudentEnrolmentTmp = shiftStudentDAO.readByCriteria(shiftStudentExample);

		//Copy the Shifts (ITurno objects) associated with the course in which 
		// the student is enrolled to InfoShifts
		ShiftStudentTransformer shiftStudentTransformer = new ShiftStudentTransformer();
		List infoShiftEnrolment = (List) CollectionUtils.collect(infoShiftStudentEnrolmentTmp, shiftStudentTransformer);

		// And the result is: shifts with vacancies, of the courses the student is enrolled with
		infoShiftStudentEnrolment.setCurrentEnrolment(infoShiftEnrolment);

		//************************************************
		//Code for filling the availlableShifts list
		ITurno shiftExample = new Turno();

		//Open a OJB entrypoint for the Shift table
		ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

		//read all the shifts associated with the course in which the student is enrolled
		IDisciplinaExecucao executionCourseExample = new DisciplinaExecucao();
		List associatedStudents = new ArrayList();
		associatedStudents.add(student);
		executionCourseExample.setAttendingStudents(associatedStudents);
		shiftExample.setDisciplinaExecucao(executionCourseExample);

		//TODO: tdi-dev -> The executionPeriod isn't handled yet!
		//IExecutionPeriod executionPeriod = null;
		//do something to the execution period, get the current period...
		//executionCourse.setExecutionPeriod(executionPeriod);

		//And the result is: shifts (ITurno's) associated with the course in which the student is enrolled
		List currentEnrolment = shiftDAO.readByCriteria(shiftExample);

		//Copy the Shifts associated with the course in which the student is enrolled
		ShiftTransformer shiftTransformer = new ShiftTransformer();
		List infoShiftEnrolmentTmp = (List) CollectionUtils.collect(currentEnrolment, shiftTransformer);

		//Remove the full shifts and the shifts that aren't theo. or prat.  
		List availableShifts = new ArrayList();
		CollectionUtils.select(infoShiftEnrolmentTmp, new Predicate() {
			public boolean evaluate(Object arg0) {
				InfoShift shift = (InfoShift) arg0;
				return (shift.getAvailabilityFinal().intValue() > 0 && (shift.getTipo().getTipo().intValue() == TipoAula.TEORICA || shift.getTipo().getTipo().intValue() == TipoAula.PRATICA));
			}
		}, availableShifts);

		// Remove the shifts in which the student is already enrolled
		List infoAvailableShifts = (List) CollectionUtils.subtract(availableShifts, infoShiftStudentEnrolment.getCurrentEnrolment());

		//Get the classes for each shift that is in  infoAvailableShifts,
		// using the list currentEnrollment to search in OJB
		List infoAvailableShiftsFiltered = new ArrayList();
		for (int i = 0; i < infoAvailableShifts.size(); i++) {
			InfoShift thisInfoShift = (InfoShift) infoAvailableShifts.get(i);

			//Get the appropriate OJB Shift from the currentEnrolment,
			// and get the classes associated to that shift
			List classList = getClassesAssociatedWithShift(thisInfoShift, currentEnrolment, sp);

			if (theClassIsAllowed(classList, infoShiftStudentEnrolment.getAllowedClasses())) {
				//the shift is OK to proceed:
				//Replace the arrayList element with an array
				//[InfoShift, ArrayListWithAssociatedClasses]  
				//	Object obj[] = new Object[2];
				//	obj[0] = (Object) thisInfoShift;
				//	obj[1] = (Object) classList;	
				InfoShiftWithAssociatedInfoClassesAndInfoLessons composedShift = new InfoShiftWithAssociatedInfoClassesAndInfoLessons();

				composedShift.setInfoClasses((List) CollectionUtils.intersection(allowedClasses, classList));
				composedShift.setInfoShift(thisInfoShift);
				composedShift.setInfoLessons(sp.getITurnoAulaPersistente().readByShift((ITurno) (sp.getITurnoPersistente().readByOId(Cloner.copyInfoShift2IShift(thisInfoShift), false))));
				//Copy the Shifts associated with the course in which the student is enrolled
				composedShift.setInfoLessons((List) CollectionUtils.collect(composedShift.getInfoLessons(), new LessonTransformer()));

				infoAvailableShiftsFiltered.add(composedShift);
			}

		}

		//Shifts with vacancies of the courses the student is enrolled with 
		infoShiftStudentEnrolment.setAvailableShift(infoAvailableShiftsFiltered);

		return infoShiftStudentEnrolment;
	}

	/**
	 * getStudentsAllowedClasses
	 * @param student
	 * @param sp Persistent Suport
	 * @return list of classes (ITurma) with the allowed classes for the student
	 * @throws ExcepcaoPersistencia
	 */
	private List getStudentsAllowedClasses(IStudent student, ISuportePersistente sp) throws ExcepcaoPersistencia {

		ITurno shiftExample = new Turno();
		IDisciplinaExecucao executionCourseExample = new DisciplinaExecucao();

		List associatedStudents = new ArrayList();
		associatedStudents.add(student);

		executionCourseExample.setAttendingStudents(associatedStudents);
		shiftExample.setDisciplinaExecucao(executionCourseExample);

		//ITurma classExample = new Turma();

		ITurmaTurno classShiftExample = new TurmaTurno();
		//classShiftExample.setTurma(classExample);
		classShiftExample.setTurno(shiftExample);

		List classShifts = sp.getITurmaTurnoPersistente().readByCriteria(classShiftExample);

		IStudentCurricularPlan currentSCP = sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
		ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();

		List executionDegrees = executionDegreeDAO.readByDegreeCurricularPlan(currentSCP.getDegreeCurricularPlan());

		return filterClassesFromListOfClassShifts(classShifts, executionDegrees);
	}

	/**
	 * This method do a distinct on classes and test if class belongs to the student degree
	 * @param classShifts
	 * @return List with distinct classes (IClass)
	 */
	private List filterClassesFromListOfClassShifts(List classShifts, List executionDegrees) {
		Iterator i = classShifts.iterator();
		List result = new ArrayList();
		while (i.hasNext()) {
			ITurma clazz = (ITurma) ((ITurmaTurno) i.next()).getTurma();
			if (executionDegrees.contains(clazz.getExecutionDegree()) && (!result.contains(clazz))) {
				result.add(clazz);
			}
			//			Iterator resultIterator = result.iterator();
			//			
			//			boolean containsOneOfThese = false;
			//			while (resultIterator.hasNext()) {
			//				ITurma resultShift = (ITurma) resultIterator.next();
			//				if (clazz.equals(resultShift)) {
			//					containsOneOfThese = true;
			//					break;
			//				}
			//			}
			//			if (!containsOneOfThese) {
			//				result.add(clazz);
			//			}
		}
		return result;
	}

	/**
	 * theClassIsAllowed returns true if the fist list (with
	 * the classes of one shift) intersects the second list
	 * (with the classes the student is allowed to be in).
	 *  
	 * @param list1 of classes
	 * @param list2 of classes
	 * @return true if list1 intersected with list2 is not null
	 */
	private boolean theClassIsAllowed(List list1, List list2) {
		Iterator iterator = list1.iterator();
		while (iterator.hasNext()) {
			InfoClass thisClass = (InfoClass) iterator.next();
			Iterator iterator2 = list2.iterator();
			while (iterator2.hasNext()) {
				InfoClass element = (InfoClass) iterator2.next();
				if ((element.getNome().equals(thisClass.getNome())) && (element.getInfoExecutionDegree().equals(thisClass.getInfoExecutionDegree()))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param thisInfoShift - Shift to be used in OJB usage
	 * @param currentEnrolment - list of shifts (ITurno)
	 * @return
	 */
	private List getClassesAssociatedWithShift(InfoShift thisInfoShift, List currentEnrolment, ISuportePersistente sp) {
		ITurmaTurnoPersistente shiftClassDAO = sp.getITurmaTurnoPersistente();
		Iterator iter = currentEnrolment.iterator();
		ITurno shift = null;
		while (iter.hasNext()) {
			shift = (ITurno) iter.next();
			if (shift.getIdInternal().equals(thisInfoShift.getIdInternal())) {
				break;
			}
			continue;
		}
		if (shift != null) {
			List classes;
			try {
				classes = shiftClassDAO.readClassesWithShift(shift);
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				return null;
			}

			//Return a list of InfoClass objects
			ShiftClassTransformer shiftClassTransformer = new ShiftClassTransformer();
			return (List) CollectionUtils.collect(classes, shiftClassTransformer);
		}
		return null;
	}

	/**
	 * @author tdi-dev
	 *
	 */
	private class ShiftClassTransformer implements Transformer {

		public Object transform(Object arg0) {
			ITurma thisClass = ((ITurmaTurno) arg0).getTurma();

			return Cloner.copyClass2InfoClass(thisClass);
		}
	}

	/**
	 * ReadStudentShiftEnrolment.java
	 *
	 *
	 * Created: Tue Jul 22 01:05:42 2003
	 *
	 * @author <a href="mailto:tfi-dev@tagus.ist.utl.pt">tdi-dev</a>
	 * @author <a href="mailto:edgar.gonçalves@tagus.ist.utl.pt">Edgar Gonçalves</a>
	 * @version 1.0
	 */
	public class LessonTransformer implements Transformer {
		public LessonTransformer() {

		} // LessonTransformer constructor

		// Implementation of org.apache.commons.collections.Transformer

		/**
		 * <code>transform</code> takes an Dominio.IAula and
		 * returns an InfoLesson
		 *
		 * @param object an <code>Object</code> value
		 * @return an <code>Object</code> value representing an InfoLesson
		 */
		public Object transform(Object object) {
			return Cloner.copyILesson2InfoLesson((Dominio.IAula) object);
		}

	} // LessonTransformer

	/**
	 * @author tdi-dev
	 *
	 */
	private class ClassTransformer implements Transformer {

		public Object transform(Object arg0) {

			ITurma thisClass = (ITurma) arg0;

			return Cloner.copyClass2InfoClass(thisClass);
		}
	}

	/**
	 * @author tdi-dev
	 *
	 */
	private class ShiftStudentTransformer implements Transformer {

		public Object transform(Object arg0) {
			ITurnoAluno shiftStudent = (ITurnoAluno) arg0;
			InfoShift infoShift = new InfoShift();
			infoShift.setAvailabilityFinal(shiftStudent.getShift().getAvailabilityFinal());
			InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(shiftStudent.getShift().getDisciplinaExecucao());

			ITurno shift = shiftStudent.getShift();
			List infoLessonsList = new ArrayList();
			Iterator iterator = shift.getAssociatedLessons().iterator();
			while (iterator.hasNext()) {
				IAula lesson = (IAula) iterator.next();
				infoLessonsList.add(Cloner.copyILesson2InfoLesson(lesson));
			}

			infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
			infoShift.setInfoLessons(infoLessonsList);
			infoShift.setLotacao(shiftStudent.getShift().getLotacao());
			infoShift.setNome(shiftStudent.getShift().getNome());
			infoShift.setTipo(shiftStudent.getShift().getTipo());
			infoShift.setIdInternal(shiftStudent.getShift().getIdInternal());
			return infoShift;
		}
	}

	/**
	 * @author tdi-dev
	 *
	 */
	private class ShiftTransformer implements Transformer {

		public Object transform(Object arg0) {
			ITurno shift = (ITurno) arg0;
			return Cloner.copyIShift2InfoShift(shift);
		}
	}

	/**
	 * @author tdi-dev
	 *
	 */
	private class ShiftTransformerInverse implements Transformer {

		public Object transform(Object arg0) {
			InfoShift shift = (InfoShift) arg0;
			return Cloner.copyInfoShift2IShift(shift);
		}
	}

}
