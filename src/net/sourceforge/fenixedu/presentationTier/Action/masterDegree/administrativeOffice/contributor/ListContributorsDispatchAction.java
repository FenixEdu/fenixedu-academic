/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.contributor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class ListContributorsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	DynaActionForm createContributorForm = (DynaActionForm) form;

	// Clean the form
	createContributorForm.set("contributorNumber", null);

	String action = request.getParameter("action");

	if (action.equals("visualize")) {
	    request.setAttribute(PresentationConstants.CONTRIBUTOR_ACTION, "label.action.contributor.visualize");
	} else if (action.equals("edit")) {
	    request.setAttribute(PresentationConstants.CONTRIBUTOR_ACTION, "label.action.contributor.edit");
	}

	return mapping.findForward("PrepareReady");

    }

    public ActionForward getContributors(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {

	final Integer contributorNumber = getIntegerFromRequestOrForm(request, (DynaActionForm) form, "contributorNumber");
	if (contributorNumber == null) {
	    return prepare(mapping, form, request, response);
	}
	final InfoContributor infoContributor = InfoContributor.newInfoFromDomain(Party.readByContributorNumber(contributorNumber
		.toString()));

	if (infoContributor == null) {
	    addActionMessage(request, "error.contributor.not.found", contributorNumber.toString());
	    return prepare(mapping, form, request, response);
	}

	request.setAttribute(PresentationConstants.CONTRIBUTOR, infoContributor);
	return mapping.findForward("ActionReady");

    }

    public ActionForward chooseContributor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	List contributorList = (List) request.getAttribute(PresentationConstants.CONTRIBUTOR_LIST);

	Integer choosenContributorPosition = Integer.valueOf(request.getParameter("contributorPosition"));

	InfoContributor infoContributor = (InfoContributor) contributorList.get(choosenContributorPosition.intValue());

	request.setAttribute(PresentationConstants.CONTRIBUTOR, infoContributor);
	return mapping.findForward("ActionReady");

    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm editContributorForm = (DynaActionForm) form;

	InfoContributor infoContributor = getInfoContributor(request, editContributorForm);

	editContributorForm.set("contributorNumber", String.valueOf(infoContributor.getContributorNumber()));
	editContributorForm.set("contributorName", infoContributor.getContributorName());
	editContributorForm.set("contributorAddress", infoContributor.getContributorAddress());
	editContributorForm.set("areaCode", infoContributor.getAreaCode());
	editContributorForm.set("areaOfAreaCode", infoContributor.getAreaOfAreaCode());
	editContributorForm.set("area", infoContributor.getArea());
	editContributorForm.set("parishOfResidence", infoContributor.getParishOfResidence());
	editContributorForm.set("districtSubdivisionOfResidence", infoContributor.getDistrictSubdivisionOfResidence());
	editContributorForm.set("districtOfResidence", infoContributor.getDistrictOfResidence());
	editContributorForm.set("contributorId", infoContributor.getIdInternal());

	return mapping.findForward("EditReady");

    }

    private InfoContributor getInfoContributor(HttpServletRequest request, DynaActionForm form) {
	final InfoContributor infoContributor = (InfoContributor) request.getAttribute(PresentationConstants.CONTRIBUTOR);
	return infoContributor != null ? infoContributor : InfoContributor.newInfoFromDomain((Party) readDomainObject(request,
		Party.class, getIntegerFromRequestOrForm(request, form, "contributorId")));
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	DynaActionForm editContributorForm = (DynaActionForm) form;

	InfoContributor infoContributor = getInfoContributor(request, editContributorForm);

	// Get the Information
	String contributorNumberString = (String) editContributorForm.get("contributorNumber");
	Integer contributorNumber = Integer.valueOf(contributorNumberString);
	String contributorName = (String) editContributorForm.get("contributorName");
	String contributorAddress = (String) editContributorForm.get("contributorAddress");
	String areaCode = (String) editContributorForm.get("areaCode");
	String areaOfAreaCode = (String) editContributorForm.get("areaOfAreaCode");
	String area = (String) editContributorForm.get("area");
	String parishOfResidence = (String) editContributorForm.get("parishOfResidence");
	String districtSubdivisionOfResidence = (String) editContributorForm.get("districtSubdivisionOfResidence");
	String districtOfResidence = (String) editContributorForm.get("districtOfResidence");

	Object args[] = { infoContributor, contributorNumber, contributorName, contributorAddress, areaCode, areaOfAreaCode,
		area, parishOfResidence, districtSubdivisionOfResidence, districtOfResidence };
	InfoContributor newInfoContributor = null;
	try {
	    newInfoContributor = (InfoContributor) ServiceManagerServiceFactory.executeService("EditContributor", args);
	} catch (ExistingServiceException e) {
	    throw new ExistingActionException("O Contribuinte", e);
	}

	request.setAttribute(PresentationConstants.CONTRIBUTOR, newInfoContributor);
	return mapping.findForward("EditSuccess");

    }

}