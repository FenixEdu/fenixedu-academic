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
import javax.servlet.http.HttpSession;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;

/**
 * @author jpvl
 */
public abstract class RequestUtils {

	public static final InfoExecutionCourse getExecutionCourseBySigla(
		HttpServletRequest request,
		String infoExecutionCourseInitials)
		throws Exception {


		List executionCourseList =
			SessionUtils.getExecutionCourses(request);
			
		HttpSession session = request.getSession();
		
		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);	
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		
		
		infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
		infoExecutionCourse.setSigla(infoExecutionCourseInitials);
		System.out.println("*******************************************");
		System.out.println(infoExecutionPeriod.getName());
		System.out.println(infoExecutionCourseInitials);
		int indexOf =
			executionCourseList.indexOf(infoExecutionCourse);

		System.out.println("INDEX OF "+ indexOf+" *****************************");
		System.out.println("*******************************************");
		if (indexOf != -1)
			return (InfoExecutionCourse) executionCourseList.get(indexOf);
		else
			return null;
	}
}
