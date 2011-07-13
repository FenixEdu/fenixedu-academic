package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.jury.externalAccess;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdJuryExternalAccess", module = "publico")
@Forwards(tileProperties = @Tile(extend = "definition.phd.jury.external.access"), value = {

@Forward(name = "downloadDocuments", path = "/phd/thesis/jury/externalAccess/downloadDocuments.jsp"),

@Forward(name = "uploadReview", path = "/phd/thesis/jury/externalAccess/uploadReview.jsp")

})
public class PhdJuryExternalAccessDA extends PhdProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("dont-cache-pages-in-search-engines", Boolean.TRUE);

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareDownloadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdParticipant participant = getPhdParticipant(request);

	if (participant == null) {
	    return null;
	}

	request.setAttribute("process", participant.getIndividualProcess());

	return mapping.findForward("downloadDocuments");
    }

    public ActionForward downloadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("downloadDocuments");
    }

    public ActionForward prepareUploadReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("uploadReview");
    }

    public ActionForward uploadReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("uploadReview");
    }

    private PhdParticipant getPhdParticipant(HttpServletRequest request) {
	return getDomainObject(request, "hash");
    }
}
