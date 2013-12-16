/*
 * Created on Jan 18, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.guideManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadDegreeCurricularPlans;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions.ReadPaymentTransactionByGuideEntryID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.ChooseGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.CreateGratuityTransaction;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.CreateGuideEntry;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.CreateGuideSituation;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.CreateInsuranceTransaction;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.DeleteGuideEntryAndPaymentTransactionInManager;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.DeleteGuideSituationInManager;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.DeleteGuideVersionInManager;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.EditGuideInformationInManager;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoPaymentTransaction;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Data;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
@Mapping(module = "manager", path = "/guideManagement", attribute = "editGuideForm", formBean = "editGuideForm",
        scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "editGuide", path = "/manager/guideManagement/editGuide.jsp"),
        @Forward(name = "firstPage", path = "/manager/guideManagement/welcomeScreen.jsp"),
        @Forward(name = "chooseGuide", path = "/manager/guideManagement/chooseGuide.jsp") })
public class GuideManagementDispatchAction extends FenixDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("firstPage");
    }

    public ActionForward prepareChooseGuide(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("chooseGuide");
    }

    public ActionForward chooseGuide(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        DynaActionForm guideForm = (DynaActionForm) actionForm;
        Integer number = (Integer) guideForm.get("number");
        Integer year = (Integer) guideForm.get("year");
        Integer version = (Integer) guideForm.get("version");

        // read guide
        InfoGuide guide = null;
        try {

            if (version.intValue() == 0) {
                List guidesList = ChooseGuide.runChooseGuide(number, year);
                guide = (InfoGuide) guidesList.iterator().next();
            } else {
                guide = ChooseGuide.runChooseGuide(number, year, version);
            }

        } catch (NonExistingServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // read transactions
        List paymentTransactions = new ArrayList();
        for (Object element : guide.getInfoGuideEntries()) {
            InfoGuideEntry guideEntry = (InfoGuideEntry) element;
            InfoPaymentTransaction paymentTransaction = null;

            try {
                paymentTransaction = ReadPaymentTransactionByGuideEntryID.run(guideEntry.getExternalId());
            } catch (FenixServiceException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            paymentTransactions.add(paymentTransaction);
        }

        List executionYears = null;
        try {
            executionYears = ReadExecutionYears.run();
        } catch (FenixServiceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        List degreeCurricularPlans = null;
        degreeCurricularPlans = ReadDegreeCurricularPlans.run();

        Collection degreeCurricularPlansInLabelValueBeanList = CollectionUtils.collect(degreeCurricularPlans, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                InfoDegreeCurricularPlan degreeCurricularPlan = (InfoDegreeCurricularPlan) arg0;
                return new LabelValueBean(degreeCurricularPlan.getName(), degreeCurricularPlan.getExternalId().toString());
            }

        });

        guideForm.set("guideID", guide.getExternalId());
        if (guide.getInfoExecutionDegree() != null) {
            guideForm.set("newExecutionYear", guide.getInfoExecutionDegree().getInfoExecutionYear().getYear());
            guideForm.set("newDegreeCurricularPlanID", guide.getInfoExecutionDegree().getInfoDegreeCurricularPlan()
                    .getExternalId());
        }

        guideForm.set("newPaymentType", (guide.getPaymentType() != null) ? guide.getPaymentType().name() : null);
        request.setAttribute("paymentTransactions", paymentTransactions);
        request.setAttribute("degreeCurricularPlans", degreeCurricularPlansInLabelValueBeanList);
        request.setAttribute("executionYears", executionYears);
        request.setAttribute("guide", guide);
        request.setAttribute("days", Data.getMonthDays());
        request.setAttribute("months", Data.getMonths());
        request.setAttribute("years", Data.getYears());

        return mapping.findForward("editGuide");
    }

    public ActionForward addGuideEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        DynaActionForm guideForm = (DynaActionForm) actionForm;
        String guideID = (String) guideForm.get("guideID");
        String newEntryDescription = (String) guideForm.get("newEntryDescription");
        Integer newEntryQuantity = (Integer) guideForm.get("newEntryQuantity");
        Double newEntryPrice = (Double) guideForm.get("newEntryPrice");
        String newEntryDocumentType = (String) guideForm.get("newEntryDocumentType");

        CreateGuideEntry.run(guideID, GraduationType.MASTER_DEGREE, DocumentType.valueOf(newEntryDocumentType),
                newEntryDescription, newEntryPrice, newEntryQuantity);

        guideForm.set("newEntryDescription", null);
        guideForm.set("newEntryQuantity", null);
        guideForm.set("newEntryPrice", null);
        guideForm.set("newEntryDocumentType", null);

        return chooseGuide(mapping, actionForm, request, response);
    }

    public ActionForward addGuideSituation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        DynaActionForm guideForm = (DynaActionForm) actionForm;

        /** ***************** */

        String guideID = (String) guideForm.get("guideID");
        String newSituationRemarks = (String) guideForm.get("newSituationRemarks");
        Integer newSituationDay = (Integer) guideForm.get("newSituationDay");
        Integer newSituationMonth = (Integer) guideForm.get("newSituationMonth");
        Integer newSituationYear = (Integer) guideForm.get("newSituationYear");
        String newSituationType = (String) guideForm.get("newSituationType");

        Date date =
                (new GregorianCalendar(newSituationYear.intValue(), newSituationMonth.intValue(), newSituationDay.intValue()))
                        .getTime();

        CreateGuideSituation.run(guideID, newSituationRemarks, GuideState.valueOf(newSituationType), date);

        guideForm.set("newSituationRemarks", null);
        guideForm.set("newSituationDay", null);
        guideForm.set("newSituationMonth", null);
        guideForm.set("newSituationYear", null);
        guideForm.set("newSituationType", null);

        return chooseGuide(mapping, actionForm, request, response);
    }

    public ActionForward createPaymentTransaction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        DynaActionForm guideForm = (DynaActionForm) actionForm;

        String selectedGuideEntryDocumentType = (String) guideForm.get("selectedGuideEntryDocumentType");
        String selectedGuideEntryID = (String) guideForm.get("selectedGuideEntryID");

        try {

            if (selectedGuideEntryDocumentType.equals(DocumentType.GRATUITY.name())) {
                CreateGratuityTransaction.run(selectedGuideEntryID, userView);
            } else if (selectedGuideEntryDocumentType.equals(DocumentType.INSURANCE.name())) {
                CreateInsuranceTransaction.run(selectedGuideEntryID, userView);
            }

        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward editExecutionDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        DynaActionForm guideForm = (DynaActionForm) actionForm;

        String newDegreeCurricularPlanID = (String) guideForm.get("newDegreeCurricularPlanID");
        String newExecutionYear = (String) guideForm.get("newExecutionYear");
        String guideID = (String) guideForm.get("guideID");
        String newPaymentType = (String) guideForm.get("newPaymentType");

        EditGuideInformationInManager.run(guideID, newDegreeCurricularPlanID, newExecutionYear, newPaymentType);

        return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward deleteGuideSituation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        DynaActionForm guideForm = (DynaActionForm) actionForm;
        String guideSituationID = (String) guideForm.get("guideSituationID");

        DeleteGuideSituationInManager.run(guideSituationID);

        return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward deleteGuideEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        DynaActionForm guideForm = (DynaActionForm) actionForm;
        String selectedGuideEntryID = (String) guideForm.get("selectedGuideEntryID");

        try {
            DeleteGuideEntryAndPaymentTransactionInManager.run(selectedGuideEntryID);
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward deleteGuide(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        DynaActionForm guideForm = (DynaActionForm) actionForm;
        String guideID = (String) guideForm.get("guideID");

        try {
            DeleteGuideVersionInManager.run(guideID);
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return chooseGuide(mapping, actionForm, request, response);
        }

        return mapping.findForward("firstPage");

    }

}