/*
 * Created on 17/Set/2003, 15:19:24
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.teacher;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import DataBeans.InfoSiteStudents;
import DataBeans.InfoStudent;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 17/Set/2003, 15:19:24
 * 
 */
public class SendMailToAllStudents extends FenixDispatchAction
{
	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session= this.getSession(request);
		UserView userView= (UserView) session.getAttribute("UserView");
		GestorServicos gestor= GestorServicos.manager();
		TeacherAdministrationSiteView siteView= null;
		Integer objectCode= null;
		Integer shiftID= null;
		try
		{
			shiftID= new Integer((String) request.getParameter("shiftID"));
		}
		catch (NumberFormatException ex)
		{
			//ok, we don't want to view a shift's student list
		}
		String objectCodeString= request.getParameter("objectCode");
		if (objectCodeString == null)
		{
			objectCodeString= (String) request.getAttribute("objectCode");
		}
		objectCode= new Integer(objectCodeString);
		Object argsReadSiteView[]= { objectCode, null };
		Object argsReadExecutionCourse[]= { objectCode };
		InfoExecutionCourse infoExecutionCourse= null;
		InfoSite infoSite= null;
		try
		{
			siteView=
				(TeacherAdministrationSiteView) gestor.executar(
					userView,
					"ReadStudentsByCurricularCourse",
					argsReadSiteView);
			infoExecutionCourse=
				(InfoExecutionCourse) gestor.executar(
					userView,
					"ReadExecutionCourseByOID",
					argsReadExecutionCourse);
			Object argsReadSite[]= { infoExecutionCourse };
			infoSite= (InfoSite) gestor.executar(userView, "ReadSite", argsReadSite);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		DynaActionForm sendMailForm= (DynaActionForm) form;
		sendMailForm.set("from", infoSite.getMail());
		sendMailForm.set("fromName", infoSite.getInfoExecutionCourse().getNome());
		sendMailForm.set("text", "");
		request.setAttribute("siteView", siteView);
		return mapping.findForward("showEmailForm");
	}
	public ActionForward send(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session= this.getSession(request);
		UserView userView= (UserView) session.getAttribute("UserView");
		Integer objectCode= null;
		Integer shiftID= null;
		Integer groupCode= null;
		String from= request.getParameter("from");
		String fromName= request.getParameter("fromName");
		String text= request.getParameter("text");
		String subject= request.getParameter("subject");
		try
		{
			groupCode= new Integer((String) request.getParameter("studentGroupCode"));
		}
		catch (NumberFormatException ex)
		{
			//ok, we don't want to view a group's student list
		}
		try
		{
			shiftID= new Integer((String) request.getParameter("shiftCode"));
		}
		catch (NumberFormatException ex)
		{
			//ok, we don't want to view a shift's student list
		}
		String objectCodeString= request.getParameter("objectCode");
		if (objectCodeString == null)
		{
			objectCodeString= (String) request.getAttribute("objectCode");
		}
		objectCode= new Integer(objectCodeString);
		Object args[]= { objectCode, null };
		GestorServicos gestor= GestorServicos.manager();
		TeacherAdministrationSiteView siteView= null;
		InfoSiteStudents infoSiteStudents= null;
		List groupStudents= null;
		List shiftStudents= null;
		List failedEmails= null;
		try
		{
			siteView=
				(TeacherAdministrationSiteView) gestor.executar(userView, "ReadStudentsByCurricularCourse", args);
			infoSiteStudents= (InfoSiteStudents) siteView.getComponent();
			if (shiftID != null)
			{
				//the objectCode is needed by the filter...doing this is awfull !!!
				//please read http://www.dcc.unicamp.br/~oliva/fun/prog/resign-patterns
				Object[] argsReadShiftStudents= { objectCode, shiftID };
				shiftStudents=
					(List) gestor.executar(userView, "teacher.ReadStudentsByShiftID", argsReadShiftStudents);
				infoSiteStudents.setStudents(shiftStudents);
			}
			if (groupCode != null)
			{
				Object[] argsReadGroupStudents= { objectCode, groupCode };
				groupStudents=
					(List) gestor.executar(
						userView,
						"teacher.ReadStudentsByStudentGroupID",
						argsReadGroupStudents);
				infoSiteStudents.setStudents(groupStudents);
			}
			Collections.sort(infoSiteStudents.getStudents(), new BeanComparator("number"));
			//
			// and finnaly, let us send the emaaaaaaaails !
			//
			List toList= new LinkedList();
			for (Iterator iter= infoSiteStudents.getStudents().iterator(); iter.hasNext();)
			{
				InfoStudent infoStudent= (InfoStudent) iter.next();
				toList.add(infoStudent.getInfoPerson().getEmail());
			}
			Object[] argsSendMails= { toList, fromName, from, subject, text };
			failedEmails= (List) gestor.executar(userView, "commons.SendMail", argsSendMails);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		ActionErrors actionErrors= new ActionErrors();
		for (Iterator iter= failedEmails.iterator(); iter.hasNext();)
		{
			String to= (String) iter.next();
			ActionError actionError= new ActionError("error.email.notSend", to);
			actionErrors.add("error.seminaries.candidaciesLimitReached", actionError);
		}
		saveErrors(request, actionErrors);
		return mapping.findForward("mailSent");
	}
}
