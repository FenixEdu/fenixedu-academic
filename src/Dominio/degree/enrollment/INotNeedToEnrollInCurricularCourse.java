package Dominio.degree.enrollment;

import Dominio.ICurricularCourse;
import Dominio.IStudentCurricularPlan;

/**
 * @author David Santos in Jun 17, 2004
 */

public interface INotNeedToEnrollInCurricularCourse
{
	public ICurricularCourse getCurricularCourse();
	public void setCurricularCourse(ICurricularCourse curricularCourse);

	public IStudentCurricularPlan getStudentCurricularPlan();
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
}