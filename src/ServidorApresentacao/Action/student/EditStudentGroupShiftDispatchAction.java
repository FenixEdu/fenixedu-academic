/*
 * Created on 27/Ago/2003
 *
 */
package ServidorApresentacao.Action.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoShift;
import DataBeans.InfoSiteShifts;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidStudentNumberServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author asnr and scpo
 * 
 */
public class EditStudentGroupShiftDispatchAction extends FenixDispatchAction
{

    public ActionForward prepareEdit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        HttpSession session = request.getSession(false);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String shiftCodeString = request.getParameter("shiftCode");
        Integer shiftCode = new Integer(shiftCodeString);
		Integer studentGroupCode = new Integer(studentGroupCodeString);
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        Object[] args1 = { groupPropertiesCode, null, studentGroupCode, userView.getUtilizador(), new Integer(4)};
        try
        {
            ServiceUtils.executeService(userView, "VerifyStudentGroupAtributes", args1);
            
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
		}catch (InvalidSituationServiceException e)
        {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("errors.editStudentGroupShift.notEnroled");
            actionErrors1.add("errors.editStudentGroupShift.notEnroled", error1);
            saveErrors(request, actionErrors1);
            return mapping.findForward("viewStudentGroupInformation");
            
        }catch (InvalidChangeServiceException e)
        {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("error.GroupPropertiesShiftTypeChanged");
            actionErrors2.add("error.GroupPropertiesShiftTypeChanged", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewShiftsAndGroups");
        }catch (FenixServiceException e)
        {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("error.noGroup");
            actionErrors2.add("error.noGroup", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewShiftsAndGroups");
        }

        InfoSiteShifts infoSiteShifts = null;
        Object[] args2 = { groupPropertiesCode, studentGroupCode};
        try
        {
        	infoSiteShifts = (InfoSiteShifts) ServiceUtils.executeService(userView, "ReadGroupPropertiesShifts", args2);

        }catch (ExistingServiceException e){
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noProject");
			actionErrors.add("error.noProject", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewExecutionCourseProjects");
		}catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewShiftsAndGroups");
		}catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        
        List shifts = infoSiteShifts.getShifts();
        
        if (shifts.size() == 0)
        {
            ActionErrors actionErrors3 = new ActionErrors();
            ActionError error3 = null;
            // Create an ACTION_ERROR 
            error3 = new ActionError("errors.editStudentGroupShift.allShiftsFull");
            actionErrors3.add("errors.editStudentGroupShift.allShiftsFull", error3);
            saveErrors(request, actionErrors3);
            request.setAttribute("groupPropertiesCode", groupPropertiesCode);
            return mapping.findForward("viewStudentGroupInformation");
        } 

            ArrayList shiftsList = new ArrayList();
            InfoShift oldInfoShift = infoSiteShifts.getOldShift();
            if (shifts.size() != 0)
            {
                shiftsList.add(new LabelValueBean("(escolher)", ""));
                InfoShift infoShift;
                Iterator iter = shifts.iterator();
                String label, value;
                List shiftValues = new ArrayList();
                while (iter.hasNext())
                {
                    infoShift = (InfoShift) iter.next();
                    value = infoShift.getIdInternal().toString();
                    shiftValues.add(value);
                    label = infoShift.getNome();
                    shiftsList.add(new LabelValueBean(label, value));
                }
                if (shiftsList.size() == 1 && shiftValues.contains(oldInfoShift.getIdInternal().toString()))
                {
                    ActionErrors actionErrors4 = new ActionErrors();
                    ActionError error4 = null;
                    error4 = new ActionError("errors.editStudentGroupShift.allShiftsFull");
                    actionErrors4.add("errors.editStudentGroupShift.allShiftsFull", error4);
                    saveErrors(request, actionErrors4);
                    return mapping.findForward("viewStudentGroupInformation");
                }
                request.setAttribute("shiftsList", shiftsList);
            }
            
            request.setAttribute("shift", oldInfoShift);
        		
            return mapping.findForward("sucess");
        

    }

    public ActionForward edit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        HttpSession session = request.getSession(false);

        DynaActionForm editStudentGroupForm = (DynaActionForm) form;

        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
		Integer studentGroupCode = new Integer(studentGroupCodeString);
        String newShiftString = (String) editStudentGroupForm.get("shift");

        if (newShiftString.equals(""))
        {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.invalid.shift.groupEnrolment");
            actionErrors.add("errors.invalid.shift.groupEnrolment", error);
            saveErrors(request, actionErrors);
            return prepareEdit(mapping, form, request, response);

        } 
            Integer newShiftCode = new Integer(newShiftString);
            Object args[] = { studentGroupCode, groupPropertiesCode,newShiftCode, userView.getUtilizador()};

            try
            {
                ServiceUtils.executeService(userView, "EditGroupShift", args);
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
                error = new ActionError("errors.editStudentGroupShift.notEnroled");
                actionErrors.add("errors.editStudentGroupShift.notEnroled", error);
                saveErrors(request, actionErrors);
                return mapping.findForward("viewStudentGroupInformation");

            } catch (InvalidChangeServiceException e)
            {
                ActionErrors actionErrors3 = new ActionErrors();
                ActionError error3 = null;
                // Create an ACTION_ERROR 
                error3 = new ActionError("errors.editStudentGroupShift.shiftFull");
                actionErrors3.add("errors.editStudentGroupShift.shiftFull", error3);
                saveErrors(request, actionErrors3);
                return mapping.findForward("viewStudentGroupInformation");

            }catch (InvalidStudentNumberServiceException e)
            {
                ActionErrors actionErrors3 = new ActionErrors();
                ActionError error3 = null;
                error3 = new ActionError("error.GroupPropertiesShiftTypeChanged");
                actionErrors3.add("error.GroupPropertiesShiftTypeChanged", error3);
                saveErrors(request, actionErrors3);
                return mapping.findForward("viewShiftsAndGroups");

            }catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }
            return mapping.findForward("viewShiftsAndGroups");
    }
}