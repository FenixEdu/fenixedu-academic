/*
 * Created on 31/Jul/2003, 16:04:48
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.Seminaries;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 31/Jul/2003, 16:04:48
 * 
 */
public class ListAllSeminaries extends FenixAction
{
	public void setCurrentCandidaciesInfo(ActionMapping mapping, HttpServletRequest request, IUserView userView) throws FenixActionException
	{
        List currentCandidacies = null;
        InfoStudent student = null;
		try
		{
            Object[] argsReadStudent= { userView.getUtilizador()};
            student= (InfoStudent) ServiceManagerServiceFactory.executeService(userView, "ReadStudentByUsername", argsReadStudent);
            Object[] argsReadCandidacies =  {student.getIdInternal()};
            currentCandidacies= (List) ServiceManagerServiceFactory.executeService(userView, "Seminaries.GetCandidaciesByStudentID",argsReadCandidacies);
		}
		catch (Exception e)
		{
            throw new FenixActionException();
		}
        request.setAttribute("currentCandidacies", currentCandidacies);
        
	}
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session= this.getSession(request);
		IUserView userView= (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		List seminaries= null;
		ActionForward destiny= null;
		try
		{
			seminaries= (List) ServiceManagerServiceFactory.executeService(userView, "Seminaries.GetAllSeminaries", new Object[0]);
		}
		catch (Exception e)
		{
            throw new FenixActionException();
		}
        this.setCurrentCandidaciesInfo(mapping,request,userView);
		destiny= mapping.findForward("listSeminaries");
		request.setAttribute("seminaries", seminaries);
		return destiny;
	}
}
