/*
 * Created on 13/Nov/2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author joaosa and rmalo
 * 
 */
public class UnEnrollStudentGroupShiftDispatchAction extends FenixDispatchAction
{
	
    public ActionForward unEnrollStudentGroupShift(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException, FenixFilterException
    {

        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
		Integer studentGroupCode = new Integer(studentGroupCodeString);
		
		Object args[] = { studentGroupCode, groupPropertiesCode, userView.getUtilizador()};
		
		try
		{
                ServiceUtils.executeService(userView, "UnEnrollGroupShift", args);
            }catch (NotAuthorizedException e) {
    			ActionErrors actionErrors2 = new ActionErrors();
    			ActionError error2 = null;
    			error2 = new ActionError("errors.noStudentInAttendsSet");
    			actionErrors2.add("errors.noStudentInAttendsSet", error2);
    			saveErrors(request, actionErrors2);
    			return mapping.findForward("insucess");
    		}catch (ExistingServiceException e){
    			ActionErrors actionErrors = new ActionErrors();
    			ActionError error = null;
    			error = new ActionError("error.noProject");
    			actionErrors.add("error.noProject", error);
    			saveErrors(request, actionErrors);
    			return mapping.findForward("viewExecutionCourseProjects");
    		}catch (InvalidArgumentsServiceException e)
            {
                ActionErrors actionErrors = new ActionErrors();
                ActionError error = null;
                error = new ActionError("error.noGroup");
                actionErrors.add("error.noGroup", error);
                saveErrors(request, actionErrors);
                return mapping.findForward("viewShiftsAndGroups");

            } catch (InvalidSituationServiceException e)
            {
                ActionErrors actionErrors = new ActionErrors();
                ActionError error = null;
                error = new ActionError("errors.unEnrollStudentGroupShift.notEnroled");
                actionErrors.add("errors.unEnrollStudentGroupShift.notEnroled", error);
                saveErrors(request, actionErrors);
                return mapping.findForward("viewStudentGroupInformation");

            } catch (InvalidChangeServiceException e)
            {
                ActionErrors actionErrors3 = new ActionErrors();
                ActionError error3 = null;
                error3 = new ActionError("errors.unEnrollStudentGroupShift.shiftFull");
                actionErrors3.add("errors.unEnrollStudentGroupShift.shiftFull", error3);
                saveErrors(request, actionErrors3);
                return mapping.findForward("viewStudentGroupInformation");

            }catch (InvalidStudentNumberServiceException e)
            {
                ActionErrors actionErrors3 = new ActionErrors();
                ActionError error3 = null;
                error3 = new ActionError("error.UnEnrollStudentGroupShift");
                actionErrors3.add("error.UnEnrollStudentGroupShift", error3);
                saveErrors(request, actionErrors3);
                return mapping.findForward("viewShiftsAndGroups");

            }catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }
            return mapping.findForward("viewShiftsAndGroups");
    }
}