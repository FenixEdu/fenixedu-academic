/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.groupManagement;


import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 12:49:20,26/Set/2005
 * @version $Id$
 */
public class ExecutionCourseStudentsGroupManagement extends ExecutionCourseUserGroupManagement
{
	public ActionForward viewExecutionCourseGroupElements(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException, FenixServiceException
	{
		IUserView userView = SessionUtils.getUserView(request);
		Collection<IPerson> students = new ArrayList<IPerson>();
		Integer executionCourseID = new Integer((String) request.getParameter("executionCourseID"));
		IExecutionCourse executionCourse = (IExecutionCourse) ServiceManagerServiceFactory.executeService(userView, "ReadDomainExecutionCourseByID", new Object[]
		{ executionCourseID });

		for (IAttends attend : executionCourse.getAttends())
			students.add(attend.getAluno().getPerson());
		request.setAttribute("elements", students);
		return mapping.findForward("showStudents");
	}

}
