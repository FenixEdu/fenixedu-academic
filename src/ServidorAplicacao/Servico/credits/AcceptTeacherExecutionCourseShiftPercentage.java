/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package ServidorAplicacao.Servico.credits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoTeacherShiftPercentage;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.ITeacherShiftPercentage;
import Dominio.ITurno;
import Dominio.Teacher;
import Dominio.TeacherShiftPercentage;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentTeacherShiftPercentage;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class AcceptTeacherExecutionCourseShiftPercentage implements IServico {
	private static AcceptTeacherExecutionCourseShiftPercentage service = new AcceptTeacherExecutionCourseShiftPercentage();

	/**
	 * The singleton access method of this class.
	 */
	public static AcceptTeacherExecutionCourseShiftPercentage getService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "AcceptTeacherExecutionCourseShiftPercentage";
	}

	/**
	 *  
	 * @param infoTeacherShiftPercentageList list of shifts and percentages that teacher needs...
	 * @return 
	 * @throws FenixServiceException
	 */
	public List run(InfoTeacher infoTeacher, InfoExecutionCourse infoExecutionCourse, List infoTeacherShiftPercentageList)
		throws FenixServiceException {
		List shiftWithErrors = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurnoPersistente shiftDAO = sp.getITurnoPersistente();
			IPersistentTeacherShiftPercentage teacherShiftPercentageDAO = sp.getIPersistentTeacherShiftPercentage();
			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			IDisciplinaExecucaoPersistente executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();
			IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();

			//read execution course
			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			executionCourse.setIdInternal(infoExecutionCourse.getIdInternal());
			executionCourse = (IDisciplinaExecucao) executionCourseDAO.readByOId(executionCourse, false);

			//read teacher
			ITeacher teacherParam = new Teacher(infoTeacher.getIdInternal());
			ITeacher teacher = (ITeacher) teacherDAO.readByOId(teacherParam, false);

			//read professorship
			IProfessorship professorship = professorshipDAO.readByTeacherAndExecutionCourse(teacher, executionCourse);

			//List teacherShiftPercentageAdded = new ArrayList();

			Iterator iterator = infoTeacherShiftPercentageList.iterator();
			while (iterator.hasNext()) {
				InfoTeacherShiftPercentage infoTeacherShiftPercentage = (InfoTeacherShiftPercentage) iterator.next();

				ITurno shift = getIShift(shiftDAO, infoTeacherShiftPercentage);

				ITeacherShiftPercentage teacherShiftPercentage = new TeacherShiftPercentage();
				teacherShiftPercentage.setProfessorShip(professorship);
				teacherShiftPercentage.setShift(shift);
				teacherShiftPercentage.setPercentage(infoTeacherShiftPercentage.getPercentage());

				boolean ok = validate(shift, infoTeacherShiftPercentage.getPercentage(), teacher);

				if (ok) {
					if (infoTeacherShiftPercentage.getPercentage().floatValue() != 0) {
						lockPercentages(teacherShiftPercentageDAO, /*teacherShiftPercentageAdded,*/ shift, teacherShiftPercentage);
					} else {
						//delete because is zero
						teacherShiftPercentage = teacherShiftPercentageDAO.readByUnique(teacherShiftPercentage);
						
						if(teacherShiftPercentage != null) {
							teacherShiftPercentageDAO.delete(teacherShiftPercentage);
						} 
					}
				} else {
					shiftWithErrors.add(Cloner.copyIShift2InfoShift(shift));
				}
			}

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			throw new FenixServiceException(e);
		}

		return shiftWithErrors; //retorna a lista com os turnos que causaram erros! 
	}

	private ITurno getIShift(ITurnoPersistente shiftDAO, InfoTeacherShiftPercentage infoTeacherShiftPercentage)
		throws ExcepcaoPersistencia {
		ITurno shiftParam = new Turno();
		shiftParam.setIdInternal(infoTeacherShiftPercentage.getInfoShift().getIdInternal());
		ITurno shift = (ITurno) shiftDAO.readByOId(shiftParam, false);
		return shift;
	}

	private void lockPercentages(
		IPersistentTeacherShiftPercentage teacherShiftPercentageDAO,
		//List teacherShiftPercentageAdded,
		ITurno shift,
		ITeacherShiftPercentage teacherShiftPercentage)
		throws ExcepcaoPersistencia {

		List associatedTeacherProfessorShipPercentage = shift.getAssociatedTeacherProfessorShipPercentage();
		int indexOf = associatedTeacherProfessorShipPercentage.indexOf(teacherShiftPercentage);

		ITeacherShiftPercentage teacherShiftPercentageToWrite = null;
		if (indexOf != -1) {
			teacherShiftPercentageToWrite = (ITeacherShiftPercentage) associatedTeacherProfessorShipPercentage.get(indexOf);
		} else {
			teacherShiftPercentageToWrite = teacherShiftPercentage;
		}

		teacherShiftPercentageDAO.simpleLockWrite(teacherShiftPercentageToWrite);
		teacherShiftPercentageToWrite.setPercentage(teacherShiftPercentage.getPercentage());
		//teacherShiftPercentageAdded.add(teacherShiftPercentageToWrite);
	}

	private boolean validate(ITurno shift, Double percentage, ITeacher teacher) {
		double percentageAvailable = 100;

		List teacherProfessorShipPercentageList = shift.getAssociatedTeacherProfessorShipPercentage();
		Iterator iterator = teacherProfessorShipPercentageList.iterator();
		while (iterator.hasNext()) {
			ITeacherShiftPercentage teacherShiftPercentageBD = (ITeacherShiftPercentage) iterator.next();

			if (!teacherShiftPercentageBD.getProfessorShip().getTeacher().equals(teacher)) {
				percentageAvailable -= teacherShiftPercentageBD.getPercentage().doubleValue();
			}
		}

		if (percentageAvailable < percentage.doubleValue()) {
			// 100% exceeded
			return false;
		}
		return true;
	}
}
