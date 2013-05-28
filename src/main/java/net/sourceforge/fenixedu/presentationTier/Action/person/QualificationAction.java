/*
 * Created on Jan 29, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.qualification.DeleteQualification;
import net.sourceforge.fenixedu.applicationTier.Servico.person.qualification.EditQualification;
import net.sourceforge.fenixedu.applicationTier.Servico.person.qualification.ReadQualification;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountryEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.framework.CRUDActionByOID;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author pica
 * @author barbosa
 */
@Mapping(module = "person", path = "/qualificationForm", input = "/qualificationForm.do?method=prepareEdit&page=0",
        attribute = "qualificationForm", formBean = "qualificationForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "successfull-delete", path = "/readQualifications.do?page=0"),
        @Forward(name = "successfull-edit", path = "/readQualifications.do?page=0"),
        @Forward(name = "successfull-read", path = "/readQualifications.do?page=0"),
        @Forward(name = "show-form", path = "edit-qualification") })
public class QualificationAction extends CRUDActionByOID {
    private static String format = "yyyy";

    /*
     * (non-Javadoc)
     * 
     * @see
     * presentationTier.Action.framework.CRUDActionByOID#populateFormFromInfoObject
     * (org.apache.struts.action.ActionMapping,
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject,
     * org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected void populateFormFromInfoObject(ActionMapping mapping, InfoObject infoObject, ActionForm form,
            HttpServletRequest request) throws FenixActionException {
        DynaActionForm dynaForm = (DynaActionForm) form;
        super.populateFormFromInfoObject(mapping, infoObject, form, request);

        InfoQualification infoQualification = (InfoQualification) infoObject;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        dynaForm.set("tempDate", sdf.format(infoQualification.getDate()));
        if (infoQualification.getEquivalenceDate() != null) {
            dynaForm.set("tempEquivalenceDate", sdf.format(infoQualification.getEquivalenceDate()));
        }
        if (infoQualification.getInfoCountry() != null) {
            dynaForm.set("countryExternalId", infoQualification.getInfoCountry().getExternalId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * presentationTier.Action.framework.CRUDActionByOID#populateInfoObjectFromForm
     * (org.apache.struts.action.ActionForm,
     * presentationTier.mapping.framework.CRUDMapping)
     */
    @Override
    protected InfoObject populateInfoObjectFromForm(ActionForm form) throws FenixActionException {
        try {
            InfoQualification infoQualification = (InfoQualification) super.populateInfoObjectFromForm(form);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            DynaActionForm dynaForm = (DynaActionForm) form;

            Date date = sdf.parse((String) dynaForm.get("tempDate"));
            infoQualification.setDate(date);
            if (dynaForm.get("tempEquivalenceDate") != null && !dynaForm.get("tempEquivalenceDate").equals("")) {
                Date equivalenceDate = sdf.parse((String) dynaForm.get("tempEquivalenceDate"));
                infoQualification.setEquivalenceDate(equivalenceDate);
            }
            if (dynaForm.get("countryExternalId") != null && !dynaForm.get("countryExternalId").equals("")) {
                InfoCountryEditor infoCountry = new InfoCountryEditor();
                infoCountry.setExternalId((Integer) dynaForm.get("countryExternalId"));
                infoQualification.setInfoCountry(infoCountry);
            }
            return infoQualification;
        } catch (ParseException e) {
            throw new FenixActionException(e.getMessage());
        }
    }

    @Override
    protected InfoObject readIt(Integer externalId) throws NotAuthorizedException {
        return ReadQualification.runReadQualification(externalId);
    }

    @Override
    protected String getRequestAttribute() {
        return "infoQualification";
    }

    @Override
    protected void deleteIt(Integer externalId) throws NotAuthorizedException {
        DeleteQualification.runDeleteQualification(externalId);
    }

    @Override
    protected void editIt(Integer externalId, InfoObject object) throws NotAuthorizedException, FenixServiceException {
        EditQualification.runEditQualification(externalId, (InfoQualification) object);
    }

    @Override
    protected String getInfoObjectClassName() {
        return "infoObjectClassName";
    }

}