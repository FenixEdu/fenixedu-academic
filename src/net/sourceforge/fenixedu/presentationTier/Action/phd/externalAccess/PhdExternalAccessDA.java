package net.sourceforge.fenixedu.presentationTier.Action.phd.externalAccess;

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

/**
 * Serves as entry point to external phd programs access. To add new operations
 * define new types in PhdProcessAccessType enum and define methods here in the
 * following format: prepare<Descriptor> . Each new method will handle an
 * operation in this page
 */

@Mapping(path = "/phdExternalAccess", module = "publico")
@Forwards(tileProperties = @Tile(extend = "definition.phd.external.access"), value = {

@Forward(name = "showOperations", path = "/phd/externalAccess/showOperations.jsp")

})
public class PhdExternalAccessDA extends PhdProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("dont-cache-pages-in-search-engines", Boolean.TRUE);

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("participant", getPhdParticipant(request));

	return mapping.findForward("showOperations");
    }

    private PhdParticipant getPhdParticipant(HttpServletRequest request) {
	return PhdParticipant.readByAccessHashCode((String) getFromRequest(request, "hash"));

    }
}
