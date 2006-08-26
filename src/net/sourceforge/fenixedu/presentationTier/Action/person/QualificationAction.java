/*
 * Created on Jan 29, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.dataTransferObject.InfoCountryEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.framework.CRUDActionByOID;
import net.sourceforge.fenixedu.presentationTier.mapping.framework.CRUDMapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author pica
 * @author barbosa
 */
public class QualificationAction extends CRUDActionByOID {
    private static String format = "yyyy";

    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.framework.CRUDActionByOID#populateFormFromInfoObject(org.apache.struts.action.ActionMapping,
     *      net.sourceforge.fenixedu.dataTransferObject.InfoObject, org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected void populateFormFromInfoObject(ActionMapping mapping, InfoObject infoObject,
            ActionForm form, HttpServletRequest request) throws FenixActionException {
        DynaActionForm dynaForm = (DynaActionForm) form;
        super.populateFormFromInfoObject(mapping, infoObject, form, request);

        InfoQualification infoQualification = (InfoQualification) infoObject;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        dynaForm.set("tempDate", sdf.format(infoQualification.getDate()));
        if (infoQualification.getEquivalenceDate() != null)
            dynaForm.set("tempEquivalenceDate", sdf.format(infoQualification.getEquivalenceDate()));
        if (infoQualification.getInfoCountry() != null)
            dynaForm.set("countryIdInternal", infoQualification.getInfoCountry().getIdInternal());
    }

    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.framework.CRUDActionByOID#populateInfoObjectFromForm(org.apache.struts.action.ActionForm,
     *      presentationTier.mapping.framework.CRUDMapping)
     */
    protected InfoObject populateInfoObjectFromForm(ActionForm form, CRUDMapping mapping)
            throws FenixActionException {
        try {
            InfoQualification infoQualification = (InfoQualification) super.populateInfoObjectFromForm(
                    form, mapping);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            DynaActionForm dynaForm = (DynaActionForm) form;

            Date date = sdf.parse((String) dynaForm.get("tempDate"));
            infoQualification.setDate(date);
            if (dynaForm.get("tempEquivalenceDate") != null
                    && !dynaForm.get("tempEquivalenceDate").equals("")) {
                Date equivalenceDate = sdf.parse((String) dynaForm.get("tempEquivalenceDate"));
                infoQualification.setEquivalenceDate(equivalenceDate);
            }
            if (dynaForm.get("countryIdInternal") != null
                    && !dynaForm.get("countryIdInternal").equals("")) {
                InfoCountryEditor infoCountry = new InfoCountryEditor();
                infoCountry.setIdInternal((Integer) dynaForm.get("countryIdInternal"));
                infoQualification.setInfoCountry(infoCountry);
            }
            return infoQualification;
        } catch (ParseException e) {
            throw new FenixActionException(e.getMessage());
        }
    }

}