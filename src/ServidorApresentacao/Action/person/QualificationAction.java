/*
 * Created on Jan 29, 2004
 */
package ServidorApresentacao.Action.person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoCountry;
import DataBeans.InfoObject;
import DataBeans.person.InfoQualification;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.framework.CRUDActionByOID;
import ServidorApresentacao.mapping.framework.CRUDMapping;

/**
 * @author pica
 * @author barbosa
 */
public class QualificationAction extends CRUDActionByOID {
    private static String format = "dd/MM/yyyy";

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateFormFromInfoObject(org.apache.struts.action.ActionMapping,
     *      DataBeans.InfoObject, org.apache.struts.action.ActionForm,
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
     * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateInfoObjectFromForm(org.apache.struts.action.ActionForm,
     *      ServidorApresentacao.mapping.framework.CRUDMapping)
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
                InfoCountry infoCountry = new InfoCountry();
                infoCountry.setIdInternal((Integer) dynaForm.get("countryIdInternal"));
                infoQualification.setInfoCountry(infoCountry);
            }
            return infoQualification;
        } catch (ParseException e) {
            throw new FenixActionException(e.getMessage());
        }
    }

}