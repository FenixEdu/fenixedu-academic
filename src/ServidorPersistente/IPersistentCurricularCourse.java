package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularSemester;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public interface IPersistentCurricularCourse extends IPersistentObject {

	public ICurricularCourse readCurricularCourseByNameAndCode(String name, String code) throws ExcepcaoPersistencia;
	public ArrayList readCurricularCoursesByCurricularYear(Integer year) throws ExcepcaoPersistencia;
	public ArrayList readCurricularCoursesByCurricularSemester(Integer semester) throws ExcepcaoPersistencia;
//	public ArrayList readCurricularCoursesByCurricularSemesterAndCurricularYear(Integer semester, Integer year) throws ExcepcaoPersistencia;
	public ArrayList readCurricularCoursesByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;
	public ArrayList readAllCurricularCoursesByBranch(IBranch branch) throws ExcepcaoPersistencia;
	public ArrayList readAll() throws ExcepcaoPersistencia;
	public ArrayList readAllCurricularCoursesBySemester(ICurricularSemester curricularSemester, IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;
	public void lockWrite(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
}