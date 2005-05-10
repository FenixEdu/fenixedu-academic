/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits.serviceExemption;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoServiceExemptionCreditLine;
import net.sourceforge.fenixedu.domain.credits.ServiceExemptionType;
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
public class CRUDServiceExemptionAction extends CRUDActionByOID {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateFormFromInfoObject(org.apache.struts.action.ActionMapping,
     \*      net.sourceforge.fenixedu.dataTransferObject.InfoObject, org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected void populateFormFromInfoObject(ActionMapping mapping, InfoObject infoObject,
            ActionForm form, HttpServletRequest request) {
        DynaActionForm serviceExemptionForm = (DynaActionForm) form;
        InfoServiceExemptionCreditLine infoServiceExemptionCreditLine = (InfoServiceExemptionCreditLine) infoObject;

        serviceExemptionForm.set("idInternal", infoServiceExemptionCreditLine.getIdInternal());
        serviceExemptionForm.set("teacherId", infoServiceExemptionCreditLine.getInfoTeacher()
                .getIdInternal());

        serviceExemptionForm.set("type", infoServiceExemptionCreditLine.getType().toString());

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        serviceExemptionForm.set("start", df.format(infoServiceExemptionCreditLine.getStart()));
        serviceExemptionForm.set("end", df.format(infoServiceExemptionCreditLine.getEnd()));

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateInfoObjectFromForm(org.apache.struts.action.ActionForm,
     *      ServidorApresentacao.mapping.framework.CRUDMapping)
     */
    protected InfoObject populateInfoObjectFromForm(ActionForm form, CRUDMapping mapping) {
        DynaActionForm serviceExemptionForm = (DynaActionForm) form;
        InfoServiceExemptionCreditLine infoServiceExemptionCreditLine = new InfoServiceExemptionCreditLine();

        String type = (String) serviceExemptionForm.get("type");
        String start = (String) serviceExemptionForm.get("start");
        String end = (String) serviceExemptionForm.get("end");

        Integer teacherId = (Integer) serviceExemptionForm.get("teacherId");
        Integer idInternal = (Integer) serviceExemptionForm.get("idInternal");

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        infoServiceExemptionCreditLine.setIdInternal(idInternal);
        infoServiceExemptionCreditLine.setInfoTeacher(new InfoTeacher(teacherId));
        infoServiceExemptionCreditLine.setType(ServiceExemptionType.valueOf(type));

        try {
            infoServiceExemptionCreditLine.setStart(df.parse(start));
        } catch (ParseException e) {
            throw new RuntimeException("Problems parsing end date!" + start);
        }
        try {
            infoServiceExemptionCreditLine.setEnd(df.parse(end));
        } catch (ParseException e) {
            throw new RuntimeException("Problems parsing end date!" + end);
        }

        return infoServiceExemptionCreditLine;
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

        //request.setAttribute("serviceExemptionTypes", ServiceExemptionType.getEnumList());
    }
    
   

}