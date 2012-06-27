package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.DegreeSpecializationArea;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.FenixActionForward;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans.OfficialPublicationBean.SpecializationName;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/curricularPlans/editOfficialPublication", module = "scientificCouncil", formBeanClass = FenixActionForm.class)
@Forwards({
	@Forward(name = "prepare", path = "/scientificCouncil/curricularPlans/editOfficialPublication.jsp"),
	@Forward(name = "prepareSpecializationArea", path = "/scientificCouncil/curricularPlans/editDegreeSpecializationArea.jsp"),
	@Forward(name = "editDegree", path = "/scientificCouncil/curricularPlans/editDegree.jsp"),
	@Forward(name = "deletePublication", path = "/scientificCouncil/curricularPlans/deleteDegreeOfficialPublication.jsp")
})
public class EditDegreeOfficialPublicationDA extends FenixDispatchAction {

    /**
     * Prepare a DegreeOfficialPublication
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward preparePubs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DegreeOfficialPublication degreeOfficialPublication = (DegreeOfficialPublication) request.getAttribute("officialPub");

	if (degreeOfficialPublication == null) {
	    degreeOfficialPublication = DegreeOfficialPublication.fromExternalId(request.getParameter("officialPubId"));
	}
	if (degreeOfficialPublication == null) {
	    degreeOfficialPublication = (DegreeOfficialPublication) getRenderedObject("officialPub");
	}

	OfficialPublicationBean bean = new OfficialPublicationBean(degreeOfficialPublication);

	request.setAttribute("officialPub", degreeOfficialPublication);
	request.setAttribute("pubBean", new OfficialPublicationBean(degreeOfficialPublication));
	request.setAttribute("referenceBean", new OfficialPublicationBean(degreeOfficialPublication));

	// TODO: remove..
	// request.setAttribute("context",
	// request.getParameter("contentContextPath_PATH"));
	return mapping.findForward("prepare");
    }

    public ActionForward updateOfficialPub(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException {

	boolean error = false;
	OfficialPublicationBean bean = (OfficialPublicationBean) getRenderedObject("referenceBean");

	if (bean.getNewReference().compareTo("") == 0) {
	    addErrorMessage(request, "error", "confirm.error.edit.name.specializationArea");
	    error = true;
	}

	if (!error) {
	    bean.getDegreeOfficialPublication().changeOfficialreference(bean.getNewReference(), bean.getPublication());
	    addActionMessage("success", request, "label.masterDegree.administrativeOffice.changeSuccess");
	}

	request.setAttribute("officialPub", bean.getDegreeOfficialPublication());
	return preparePubs(mapping, form, request, response);
    }

    public ActionForward updatePubs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	RenderUtils.invalidateViewState("pubBean");
	return mapping.findForward("prepare");
    }

    public ActionForward updateArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SpecializationName name = getRenderedObject("specializationArea");

	if (name.getEnName().compareTo("") == 0 || name.getPtName().compareTo("") == 0) {
	    addErrorMessage(request, "error", "confirm.error.edit.name.specializationArea");
	} else {
	    // update whether the names have been changed

	    name.update();
	}
	request.setAttribute("officialPub", name.getSpecializationArea().getOfficialPublication());
	return preparePubs(mapping, form, request, response);
    }

    public ActionForward removeSpecializationArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DegreeSpecializationArea specializationArea = AbstractDomainObject.fromExternalId(request
		.getParameter("specializationId"));

	DegreeOfficialPublication degreeOfficialPublication = specializationArea.getOfficialPublication();
	OfficialPublicationBean bean = new OfficialPublicationBean(degreeOfficialPublication);

	bean.removeSpecializationArea(specializationArea);

	request.setAttribute("officialPub", degreeOfficialPublication);
	return preparePubs(mapping, form, request, response);
    }

    public ActionForward editSpecializationArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DegreeSpecializationArea area = AbstractDomainObject.fromExternalId(request.getParameter("specializationId"));
	request.setAttribute("specializationArea",
		new OfficialPublicationBean(area.getOfficialPublication()).new SpecializationName(area));
	return mapping.findForward("prepareSpecializationArea");
    }

    public ActionForward createNewSpecializationArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	OfficialPublicationBean bean = getRenderedObject("pubBean");

	DegreeOfficialPublication degreeOfficialPublication = bean.getDegreeOfficialPublication();

	if (bean.getNewNameEn().compareTo("") == 0 || bean.getNewNamePt().compareTo("") == 0) {
	    addErrorMessage(request, "error", "confirm.error.edit.name.specializationArea");
	    request.setAttribute("officialPub", degreeOfficialPublication);
	    return preparePubs(mapping, form, request, response);
	}

	degreeOfficialPublication.createSpecializationArea(bean.getNewNameEn(), bean.getNewNamePt());
	request.setAttribute("officialPub", degreeOfficialPublication);
	return preparePubs(mapping, form, request, response);
    }

    public ActionForward prepareDeleteDegreeOfficialPublication(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	String degreeId = request.getParameter("degreeId");
	String officialPubId = request.getParameter("officialPubId");

	DegreeOfficialPublication publication = DegreeOfficialPublication.fromExternalId(officialPubId);
	
	request.setAttribute("publication", publication);
	request.setAttribute("degreeId", degreeId);
	
	return mapping.findForward("deletePublication");
    }

    public ActionForward goToEditDegree(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	String degreeId = request.getParameter("degreeId");

	return new FenixActionForward(request, new ActionForward("/curricularPlans/editDegree.faces?degreeId=" + degreeId, false));
    }

    public ActionForward deleteDegreeOfficialPublication(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) {
	String degreeId = request.getParameter("degreeId");
	String officialPubId = request.getParameter("officialPubId");

	DegreeOfficialPublication publication = DegreeOfficialPublication.fromExternalId(officialPubId);
	
	publication.delete();

	return new FenixActionForward(request, new ActionForward("/curricularPlans/editDegree.faces?degreeId=" + degreeId, false));
    }
}
