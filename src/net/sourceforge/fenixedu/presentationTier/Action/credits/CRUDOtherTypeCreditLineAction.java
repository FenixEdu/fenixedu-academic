/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.framework.CRUDActionByOID;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.mapping.framework.CRUDMapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author jpvl
 */
public class CRUDOtherTypeCreditLineAction extends CRUDActionByOID {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateFormFromInfoObject(org.apache.struts.action.ActionMapping,
     *      net.sourceforge.fenixedu.dataTransferObject.InfoObject, org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected void populateFormFromInfoObject(ActionMapping mapping, InfoObject infoObject,
            ActionForm form, HttpServletRequest request) throws FenixActionException {
        DynaActionForm otherTypeCreditLineForm = (DynaActionForm) form;
        InfoOtherTypeCreditLine infoOtherTypeCreditLine = (InfoOtherTypeCreditLine) infoObject;

        otherTypeCreditLineForm.set("idInternal", infoOtherTypeCreditLine.getIdInternal());
        otherTypeCreditLineForm.set("executionPeriodId", infoOtherTypeCreditLine
                .getInfoExecutionPeriod().getIdInternal());
        otherTypeCreditLineForm.set("teacherId", infoOtherTypeCreditLine.getInfoTeacher()
                .getIdInternal());
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
            throws FenixActionException {
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
    protected void prepareFormConstants(ActionMapping mapping, ActionForm form,
            HttpServletRequest request) throws FenixServiceException, FenixFilterException {
        DynaActionForm otherTypeCreditLineForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);
        Integer executionPeriodId = (Integer) otherTypeCreditLineForm.get("executionPeriodId");
        Integer teacherId = (Integer) otherTypeCreditLineForm.get("teacherId");
        Object args[] = { executionPeriodId };

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(
                userView, "ReadExecutionPeriodByOID", args);

        request.setAttribute("infoExecutionPeriod", infoExecutionPeriod);

        args[0] = teacherId;
        InfoTeacher infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView,
                "ReadTeacherByOID", args);
        request.setAttribute("infoTeacher", infoTeacher);
    }

}