/*
 * Created on 29/Fev/2004
 */
package ServidorApresentacao.Action.credits;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import DataBeans.credits.InfoOtherTypeCreditLine;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.framework.CRUDActionByOID;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.mapping.framework.CRUDMapping;

/**
 * @author jpvl
 */
public class CRUDOtherTypeCreditLineAction extends CRUDActionByOID
{

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateFormFromInfoObject(org.apache.struts.action.ActionMapping,
     *      DataBeans.InfoObject, org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected void populateFormFromInfoObject(
        ActionMapping mapping,
        InfoObject infoObject,
        ActionForm form,
        HttpServletRequest request)
        throws FenixActionException
    {
        DynaActionForm otherTypeCreditLineForm = (DynaActionForm) form;
        InfoOtherTypeCreditLine infoOtherTypeCreditLine = (InfoOtherTypeCreditLine) infoObject;

        otherTypeCreditLineForm.set("idInternal", infoOtherTypeCreditLine.getIdInternal());
        otherTypeCreditLineForm.set(
            "executionPeriodId",
            infoOtherTypeCreditLine.getInfoExecutionPeriod().getIdInternal());
        otherTypeCreditLineForm.set(
            "teacherId",
            infoOtherTypeCreditLine.getInfoTeacher().getIdInternal());
        otherTypeCreditLineForm.set("reason", infoOtherTypeCreditLine.getReason());
        otherTypeCreditLineForm.set("credits", infoOtherTypeCreditLine.getCredits().toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateInfoObjectFromForm(org.apache.struts.action.ActionForm,
     *      ServidorApresentacao.mapping.framework.CRUDMapping)
     */
    protected InfoObject populateInfoObjectFromForm(ActionForm form, CRUDMapping mapping)
        throws FenixActionException
    {
        DynaActionForm otherTypeCreditLineForm = (DynaActionForm) form;
        InfoOtherTypeCreditLine infoOtherTypeCreditLine = new InfoOtherTypeCreditLine();

        Double credits = Double.valueOf((String) otherTypeCreditLineForm.get("credits"));
        String reason = (String) otherTypeCreditLineForm.get("reason");
        Integer teacherId = (Integer) otherTypeCreditLineForm.get("teacherId");
        Integer executionPeriodId = (Integer) otherTypeCreditLineForm.get("executionPeriodId");
        Integer idInternal = (Integer) otherTypeCreditLineForm.get("idInternal");

        infoOtherTypeCreditLine.setIdInternal(idInternal);
        infoOtherTypeCreditLine.setInfoTeacher(new InfoTeacher(teacherId));
        infoOtherTypeCreditLine.setInfoExecutionPeriod(new InfoExecutionPeriod(executionPeriodId));
        infoOtherTypeCreditLine.setReason(reason);
        infoOtherTypeCreditLine.setCredits(credits);

        return infoOtherTypeCreditLine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#prepareFormConstants(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected void prepareFormConstants(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request)
        throws FenixServiceException
    {
        DynaActionForm otherTypeCreditLineForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);
        Integer executionPeriodId = (Integer) otherTypeCreditLineForm.get("executionPeriodId");
        Integer teacherId = (Integer) otherTypeCreditLineForm.get("teacherId");
        Object args[] = { executionPeriodId };

        InfoExecutionPeriod infoExecutionPeriod =
            (InfoExecutionPeriod) ServiceUtils.executeService(
                userView,
                "ReadExecutionPeriodByOID",
                args);

        request.setAttribute("infoExecutionPeriod", infoExecutionPeriod);

        args[0] = teacherId;
        InfoTeacher infoTeacher =
            (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByOID", args);
        request.setAttribute("infoTeacher", infoTeacher);
    }

}
