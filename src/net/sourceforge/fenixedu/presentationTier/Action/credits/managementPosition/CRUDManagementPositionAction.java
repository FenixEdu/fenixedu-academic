/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits.managementPosition;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
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
public class CRUDManagementPositionAction extends CRUDActionByOID {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateFormFromInfoObject(org.apache.struts.action.ActionMapping,
     *      net.sourceforge.fenixedu.dataTransferObject.InfoObject, org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected void populateFormFromInfoObject(ActionMapping mapping, InfoObject infoObject,
            ActionForm form, HttpServletRequest request) {
        DynaActionForm managementPositionForm = (DynaActionForm) form;
        InfoManagementPositionCreditLine infoManagementPositionCreditLine = (InfoManagementPositionCreditLine) infoObject;

        managementPositionForm.set("idInternal", infoManagementPositionCreditLine.getIdInternal());
        managementPositionForm.set("teacherId", infoManagementPositionCreditLine.getInfoTeacher()
                .getIdInternal());
        managementPositionForm.set("position", infoManagementPositionCreditLine.getPosition());
        managementPositionForm.set("credits", infoManagementPositionCreditLine.getCredits().toString());

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        managementPositionForm.set("start", df.format(infoManagementPositionCreditLine.getStart()));
        managementPositionForm.set("end", df.format(infoManagementPositionCreditLine.getEnd()));

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateInfoObjectFromForm(org.apache.struts.action.ActionForm,
     *      ServidorApresentacao.mapping.framework.CRUDMapping)
     */
    protected InfoObject populateInfoObjectFromForm(ActionForm form, CRUDMapping mapping) {
        DynaActionForm managementPositionForm = (DynaActionForm) form;
        InfoManagementPositionCreditLine infoManagementPositionCreditLine = new InfoManagementPositionCreditLine();

        String position = (String) managementPositionForm.get("position");
        String start = (String) managementPositionForm.get("start");
        String end = (String) managementPositionForm.get("end");
        String credits = (String) managementPositionForm.get("credits");

        Integer teacherId = (Integer) managementPositionForm.get("teacherId");
        Integer idInternal = (Integer) managementPositionForm.get("idInternal");

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        infoManagementPositionCreditLine.setIdInternal(idInternal);
        infoManagementPositionCreditLine.setInfoTeacher(new InfoTeacher(teacherId));
        infoManagementPositionCreditLine.setPosition(position);

        try {
            infoManagementPositionCreditLine.setStart(df.parse(start));
        } catch (ParseException e) {
            throw new RuntimeException("Problems parsing end date!" + start);
        }
        try {
            infoManagementPositionCreditLine.setEnd(df.parse(end));
        } catch (ParseException e) {
            throw new RuntimeException("Problems parsing end date!" + end);
        }
        infoManagementPositionCreditLine.setCredits(Double.valueOf(credits));

        return infoManagementPositionCreditLine;
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
        DynaActionForm managementPositionForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);
        Integer teacherId = (Integer) managementPositionForm.get("teacherId");

        Object args[] = { teacherId };
        InfoTeacher infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView,
                "ReadTeacherByOID", args);
        request.setAttribute("infoTeacher", infoTeacher);
    }

}