/*
 * Created on 10/Set/2003, 18:36:21
 * changed on 4/Jan/2004, 19:45:11 (generalize for any execution course)
 * changed on 13/Out/2004 20:12:33 (changed servlet output, giving more information)
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */


package net.sourceforge.fenixedu.presentationTier.Action.publico;


import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 10/Set/2003, 18:36:21
 * 
 */

public class ShowStudentGroupInfo extends Action
{
	public String buildInfo(Integer executionCourseID, String username, String password,
			String requestURL)
	{
		String result = new String();
		if (username == null) return new String();

		try
		{
			Object argsAutenticacao[] =
			{ username, password, "", requestURL };
			IUserView userView = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsAutenticacao);

			IExecutionCourse executionCourse = null;
			Collection<IGrouping> groupings = null;
			try
			{
				executionCourse = (IExecutionCourse) ServiceUtils.executeService(userView, "ReadDomainObject", new Object[]
				{ ExecutionCourse.class, executionCourseID });
				groupings = executionCourse.getGroupings();
			}
			catch (FenixServiceException e)
			{
				e.printStackTrace();
				throw new FenixActionException(e);
			}

			if (executionCourse == null || groupings == null) return new String("-1");

			for (Iterator iterGroupings = groupings.iterator(); iterGroupings.hasNext();)
			{
				IGrouping grouping = (IGrouping) iterGroupings.next();

				if (grouping != null)
				{

					for (IStudentGroup studentGroup : grouping.getStudentGroups())
					{
						for (IAttends searchingAttendsSet : studentGroup.getAttends())
						{
							if (username.equalsIgnoreCase(searchingAttendsSet.getAluno().getPerson().getUsername()))
							{
								result = executionCourse.getNome() + "(";
								Collection<ICurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
								boolean firstCurricularCourse = true;
								for (ICurricularCourse course : curricularCourses)
								{
									if (!firstCurricularCourse) result += ",";
									result += course.getDegreeCurricularPlan().getName();
								}
								result += ")\n";
								result += grouping.getName() + "\n";
								int remainingStudents = grouping.getMaximumCapacity().intValue();
								for (IAttends attends : studentGroup.getAttends())
								{
									result += attends.getAluno().getNumber();
									result += "-";
									result += attends.getAluno().getActiveStudentCurricularPlan().getDegreeCurricularPlan().getName()
											+ "\n";
									remainingStudents--;
								}
								while (remainingStudents > 0)
								{
									remainingStudents--;
									result += "N/A\n";
								}

								result += studentGroup.getGroupNumber() + "\n"
										+ studentGroup.getShift().getNome() + "\n";
								for (ILesson lesson : studentGroup.getShift().getAssociatedLessons())
								{
									result += lesson.getDiaSemana().toString() + " "
											+ lesson.getInicio().get(Calendar.HOUR_OF_DAY);
									int minute = lesson.getInicio().get(Calendar.MINUTE);
									result += ":";
									if (minute < 10) result += "0";
									result += minute + "\n\n";
								}

							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = "-1";
		}

		return result;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException
	{

		String result = new String();

		if (!request.getScheme().equalsIgnoreCase("https"))
		{
			result = "-1";
		}
		else
		{

			String studentUsername = request.getParameter("username");
			String studentPassword = request.getParameter("password");
			String idsString = request.getParameter("ids");
			final String requestURL = request.getRequestURL().toString();

			Integer[] coursesIds =
			{ new Integer(34811), new Integer(34661), new Integer(34950) };
			if (idsString != null)
			{
				String[] ids = idsString.split(",");
				coursesIds = new Integer[ids.length];
				for (int i = 0; i < ids.length; i++)
					coursesIds[i] = new Integer(ids[i]);

			}

			for (int i = 0; i < coursesIds.length; i++)
			{

				result += this.buildInfo(coursesIds[i], studentUsername, studentPassword, requestURL);
			}
			if (result.equals("")) result = "-1";
		}
		try
		{
			ServletOutputStream writer = response.getOutputStream();
			response.setContentType("text/plain");
			writer.print(result);
			writer.flush();
			response.flushBuffer();
		}
		catch (IOException ex)
		{
			throw new FenixActionException();
		}
		return null;
	}
}