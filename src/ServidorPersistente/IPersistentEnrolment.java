package ServidorPersistente;

import java.util.ArrayList;

import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IPersistentEnrolment extends IPersistentObject {

		public void deleteAll() throws ExcepcaoPersistencia;
		public void lockWrite(IEnrolment enrolmentToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
		public void delete(IEnrolment enrolment) throws ExcepcaoPersistencia;
		public IEnrolment readEnrolmentByStudentCurricularPlanAndCurricularCourse(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;
		public ArrayList readAll() throws ExcepcaoPersistencia;
}