/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop.utils
 * 
 * Created on 16/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoExecutionCourse;

/**
 * @author jpvl
 */
public abstract class RequestUtils {

	public static final InfoExecutionCourse getExecutionCourseBySigla(
		HttpServletRequest request,
		String infoExecutionCourseInitials)
		throws Exception {

		CurricularYearAndSemesterAndInfoExecutionDegree context =
			SessionUtils.getContext(request);

		List executionCourseList =
			SessionUtils.getExecutionCourses(request, context);

		int indexOf =
			executionCourseList.indexOf(
				new InfoExecutionCourse(
					null,
					infoExecutionCourseInitials,
					null,
					context.getInfoLicenciaturaExecucao(),
					null, null, null, null));

		if (indexOf != -1)
			return (InfoExecutionCourse) executionCourseList.get(indexOf);
		else
			return null;
	}
}
