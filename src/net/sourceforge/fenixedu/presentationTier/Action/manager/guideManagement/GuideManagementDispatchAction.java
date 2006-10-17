/*
 * Created on Jan 18, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.guideManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoPaymentTransaction;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class GuideManagementDispatchAction extends FenixDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward("firstPage");
    }

    public ActionForward prepareChooseGuide(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	return mapping.findForward("chooseGuide");
    }

    public ActionForward chooseGuide(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);

	DynaActionForm guideForm = (DynaActionForm) actionForm;
	Integer number = (Integer) guideForm.get("number");
	Integer year = (Integer) guideForm.get("year");
	Integer version = (Integer) guideForm.get("version");

	// read guide
	InfoGuide guide = null;
	try {

	    if (version.intValue() == 0) {
		Object[] args = { number, year };
		List guidesList = (List) ServiceUtils.executeService(userView, "ChooseGuide", args);
		guide = (InfoGuide) guidesList.get(0);
	    } else {
		Object[] args = { number, year, version };
		guide = (InfoGuide) ServiceUtils.executeService(userView, "ChooseGuide", args);
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
	for (Iterator iter = guide.getInfoGuideEntries().iterator(); iter.hasNext();) {
	    InfoGuideEntry guideEntry = (InfoGuideEntry) iter.next();
	    InfoPaymentTransaction paymentTransaction = null;

	    Object[] argsPaymentTransactions = { guideEntry.getIdInternal() };
	    try {
		paymentTransaction = (InfoPaymentTransaction) ServiceUtils.executeService(userView,
			"ReadPaymentTransactionByGuideEntryID", argsPaymentTransactions);
	    } catch (FenixServiceException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }

	    paymentTransactions.add(paymentTransaction);
	}

	List executionYears = null;
	Object[] argsEmpty = {};
	try {
	    executionYears = (List) ServiceUtils.executeService(userView, "ReadExecutionYears",
		    argsEmpty);
	} catch (FenixServiceException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	List degreeCurricularPlans = null;
	try {
	    degreeCurricularPlans = (List) ServiceUtils.executeService(userView,
		    "ReadDegreeCurricularPlans", argsEmpty);
	} catch (FenixServiceException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	Collection degreeCurricularPlansInLabelValueBeanList = CollectionUtils.collect(
		degreeCurricularPlans, new Transformer() {

		    public Object transform(Object arg0) {
			InfoDegreeCurricularPlan degreeCurricularPlan = (InfoDegreeCurricularPlan) arg0;
			return new LabelValueBean(degreeCurricularPlan.getName(), degreeCurricularPlan
				.getIdInternal().toString());
		    }

		});

	guideForm.set("guideID", guide.getIdInternal());
	if (guide.getInfoExecutionDegree() != null) {
	    guideForm.set("newExecutionYear", guide.getInfoExecutionDegree().getInfoExecutionYear()
		    .getYear());
	    guideForm.set("newDegreeCurricularPlanID", guide.getInfoExecutionDegree()
		    .getInfoDegreeCurricularPlan().getIdInternal());
	}
	
	guideForm.set("newPaymentType", (guide.getPaymentType() != null) ? guide.getPaymentType().name()
		: null);
	request.setAttribute("paymentTransactions", paymentTransactions);
	request.setAttribute("degreeCurricularPlans", degreeCurricularPlansInLabelValueBeanList);
	request.setAttribute("executionYears", executionYears);
	request.setAttribute("guide", guide);
	request.setAttribute("days", Data.getMonthDays());
	request.setAttribute("months", Data.getMonths());
	request.setAttribute("years", Data.getYears());

	return mapping.findForward("editGuide");
    }

    public ActionForward addGuideEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);

	DynaActionForm guideForm = (DynaActionForm) actionForm;
	Integer guideID = (Integer) guideForm.get("guideID");
	String newEntryDescription = (String) guideForm.get("newEntryDescription");
	Integer newEntryQuantity = (Integer) guideForm.get("newEntryQuantity");
	Double newEntryPrice = (Double) guideForm.get("newEntryPrice");
	String newEntryDocumentType = (String) guideForm.get("newEntryDocumentType");

	Object[] args = { guideID, GraduationType.MASTER_DEGREE,
		DocumentType.valueOf(newEntryDocumentType), newEntryDescription, newEntryPrice,
		newEntryQuantity };
	try {
	    ServiceUtils.executeService(userView, "CreateGuideEntry", args);
	} catch (FenixServiceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	guideForm.set("newEntryDescription", null);
	guideForm.set("newEntryQuantity", null);
	guideForm.set("newEntryPrice", null);
	guideForm.set("newEntryDocumentType", null);

	return chooseGuide(mapping, actionForm, request, response);
    }

    public ActionForward addGuideSituation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);

	DynaActionForm guideForm = (DynaActionForm) actionForm;

	/** ***************** */

	Integer guideID = (Integer) guideForm.get("guideID");
	String newSituationRemarks = (String) guideForm.get("newSituationRemarks");
	Integer newSituationDay = (Integer) guideForm.get("newSituationDay");
	Integer newSituationMonth = (Integer) guideForm.get("newSituationMonth");
	Integer newSituationYear = (Integer) guideForm.get("newSituationYear");
	String newSituationType = (String) guideForm.get("newSituationType");

	Date date = (new GregorianCalendar(newSituationYear.intValue(), newSituationMonth.intValue(),
		newSituationDay.intValue())).getTime();

	Object[] args = { guideID, newSituationRemarks, GuideState.valueOf(newSituationType), date };

	try {
	    ServiceUtils.executeService(userView, "CreateGuideSituation", args);
	} catch (FenixServiceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	guideForm.set("newSituationRemarks", null);
	guideForm.set("newSituationDay", null);
	guideForm.set("newSituationMonth", null);
	guideForm.set("newSituationYear", null);
	guideForm.set("newSituationType", null);

	return chooseGuide(mapping, actionForm, request, response);
    }

    public ActionForward createPaymentTransaction(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);

	DynaActionForm guideForm = (DynaActionForm) actionForm;

	String selectedGuideEntryDocumentType = (String) guideForm.get("selectedGuideEntryDocumentType");
	Integer selectedGuideEntryID = (Integer) guideForm.get("selectedGuideEntryID");

	Object[] args = { selectedGuideEntryID, userView };

	try {

	    if (selectedGuideEntryDocumentType.equals(DocumentType.GRATUITY.name())) {
		ServiceUtils.executeService(userView, "CreateGratuityTransaction", args);
	    } else if (selectedGuideEntryDocumentType.equals(DocumentType.INSURANCE.name())) {
		ServiceUtils.executeService(userView, "CreateInsuranceTransaction", args);
	    }

	} catch (FenixServiceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward editExecutionDegree(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);

	DynaActionForm guideForm = (DynaActionForm) actionForm;

	Integer newDegreeCurricularPlanID = (Integer) guideForm.get("newDegreeCurricularPlanID");
	String newExecutionYear = (String) guideForm.get("newExecutionYear");
	Integer guideID = (Integer) guideForm.get("guideID");
	String newPaymentType = (String) guideForm.get("newPaymentType");

	Object[] args = { guideID, newDegreeCurricularPlanID, newExecutionYear, newPaymentType };
	try {
	    ServiceUtils.executeService(userView, "EditGuideInformationInManager", args);
	} catch (FenixServiceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward deleteGuideSituation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);

	DynaActionForm guideForm = (DynaActionForm) actionForm;
	Integer guideSituationID = (Integer) guideForm.get("guideSituationID");

	Object[] args = { guideSituationID };
	try {
	    ServiceUtils.executeService(userView, "DeleteGuideSituationInManager", args);
	} catch (FenixServiceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward deleteGuideEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);

	DynaActionForm guideForm = (DynaActionForm) actionForm;
	Integer selectedGuideEntryID = (Integer) guideForm.get("selectedGuideEntryID");

	Object[] args = { selectedGuideEntryID };
	try {
	    ServiceUtils
		    .executeService(userView, "DeleteGuideEntryAndPaymentTransactionInManager", args);
	} catch (FenixServiceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return chooseGuide(mapping, actionForm, request, response);

    }

    public ActionForward deleteGuide(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);

	DynaActionForm guideForm = (DynaActionForm) actionForm;
	Integer guideID = (Integer) guideForm.get("guideID");

	Object[] args = { guideID };
	try {
	    ServiceUtils.executeService(userView, "DeleteGuideVersionInManager", args);
	} catch (FenixServiceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return chooseGuide(mapping, actionForm, request, response);
	}

	return mapping.findForward("firstPage");

    }

}
