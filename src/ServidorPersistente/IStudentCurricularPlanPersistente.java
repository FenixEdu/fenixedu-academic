/*
 * IStudentCurricularPlan.java
 *
 * Created on 21 of December de 2002, 16:57
 */

package ServidorPersistente;

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

import java.util.List;

import Dominio.IBranch;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Specialization;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

public interface IStudentCurricularPlanPersistente extends IPersistentObject {


    public IStudentCurricularPlan readActiveByStudentNumberAndDegreeType(Integer number, TipoCurso degreeType) throws ExcepcaoPersistencia;
	/**
	 * 
	 * @param studentNumber
	 * @param degreeType
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public IStudentCurricularPlan readActiveStudentCurricularPlan(Integer studentNumber, TipoCurso degreeType) throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param studentCurricularPlan
	 * @throws ExcepcaoPersistencia
	 * @throws ExistingPersistentException
	 */
	public void lockWrite(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia, ExistingPersistentException;

	/**
	 * 
	 * @param studentCurricularPlan
	 * @throws ExcepcaoPersistencia
	 */
	public void delete(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;

	

	/**
	 * 
	 * @param studentNumber
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readAllFromStudent(int studentNumber /*, StudentType studentType */
	) throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param studentNumber
	 * @param degreeType
	 * @param specialization
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public IStudentCurricularPlan readActiveStudentAndSpecializationCurricularPlan(Integer studentNumber, TipoCurso degreeType, Specialization specialization)
		throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param degreeCurricularPlan
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param username
	 * @return List with the Student's Curricular Plans
	 * @throws ExcepcaoPersistencia
	 */
	public List readByUsername(String username) throws ExcepcaoPersistencia;


	/**
	 * 
	 * @param number
	 * @param degreeType
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByStudentNumberAndDegreeType(Integer number, TipoCurso degreeType) throws ExcepcaoPersistencia;
	

	public List readAllByDegreeCurricularPlanAndState(IDegreeCurricularPlan degreeCurricularPlan, StudentCurricularPlanState state)
		throws ExcepcaoPersistencia;
		
	/**
	 * 
	 * @param branch
     * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByBranch(IBranch branch) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param branch
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
//	public List readByCurricularCourseScope(ICurricularCourseScope curricularCourseScope) throws ExcepcaoPersistencia;

	public List readAllByStudentAntState(IStudent student, StudentCurricularPlanState state) throws ExcepcaoPersistencia;
	public IStudentCurricularPlan readByStudentDegreeCurricularPlanAndState(IStudent student, IDegreeCurricularPlan degreeCurricularPlan, StudentCurricularPlanState state) throws ExcepcaoPersistencia;
	
	public List readAllActiveStudentCurricularPlan(Integer studentNumber) throws ExcepcaoPersistencia;
}