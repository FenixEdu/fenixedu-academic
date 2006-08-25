package net.sourceforge.fenixedu.presentationTier.Action.publication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublicationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class SearchAuthorPublicationAction extends FenixDispatchAction {

    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpSession session = request.getSession(false);

	DynaActionForm dynaForm = (DynaActionForm) actionForm;

	IUserView userView = SessionUtils.getUserView(request);

	Integer publicationTypeId = new Integer(request.getParameter("infoPublicationTypeId"));

	String typePublication = (String) dynaForm.get("typePublication");

	Integer idTeacher = (Integer) dynaForm.get("teacherId");

	String[] list = (String[]) dynaForm.get("authorsIds");
	List newList = Arrays.asList(list);
	List authorsIds = new ArrayList();
	authorsIds.addAll(newList);

	ActionForward actionForward = null;

	Object args[] = { publicationTypeId };
	InfoPublicationType publicationType = (InfoPublicationType) ServiceUtils.executeService(
		userView, "ReadPublicationType", args);

	if (session != null) {

	    List infoAuthors = readInfoAuthors(authorsIds, userView);

	    request.setAttribute("infoAuthorsList", infoAuthors);
	    dynaForm.set("infoPublicationTypeId", publicationTypeId);
	    dynaForm.set("typePublication", typePublication);
	    dynaForm.set("teacherId", idTeacher);

	    if (publicationType.getPublicationType().equalsIgnoreCase("Unstructured"))
		actionForward = mapping.findForward("show-attributes");
	    else
		actionForward = mapping.findForward("show-search-author-form");
	}

	return actionForward;
    }

    private List<InfoPerson> readInfoAuthors(List<String> authorsIds, IUserView userView)
	    throws FenixFilterException, FenixServiceException {
	
	final List<Integer> newAuthorsIds = new ArrayList<Integer>(authorsIds.size());
	for (final String authorId : authorsIds) {
	    newAuthorsIds.add(Integer.valueOf(authorId));
	}
	return (List<InfoPerson>) ServiceUtils.executeService(userView, "ReadPersonsByIDs",
		new Object[] { newAuthorsIds });
    }

}