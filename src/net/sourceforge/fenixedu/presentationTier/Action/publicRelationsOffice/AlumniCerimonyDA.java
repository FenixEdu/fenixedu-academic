package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry;
import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryAnswer;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.FileUtils;

@Mapping(path = "/alumniCerimony", module = "publicRelations")
@Forwards( {
    @Forward(name = "manageAlumniCerimony", path = "publicRelationsOffice-manageAlumniCerimony"),
    @Forward(name = "viewAlumniCerimonyInquiry", path = "publicRelationsOffice-viewAlumniCerimonyInquiry"),
    @Forward(name = "editAlumniCerimonyInquiry", path = "publicRelationsOffice-editAlumniCerimonyInquiry"),
    @Forward(name = "editAlumniCerimonyInquiryAnswer", path = "publicRelationsOffice-editAlumniCerimonyInquiryAnswer"),
    @Forward(name = "viewAlumniCerimonyInquiryAnswer", path = "publicRelationsOffice-viewAlumniCerimonyInquiryAnswer"),
    @Forward(name = "addPeopleToCerimonyInquiry", path = "publicRelationsOffice-addPeopleToCerimonyInquiry"),
    @Forward(name = "viewInquiryPeople", path = "publicRelationsOffice-viewInquiryPeople")
})
public class AlumniCerimonyDA extends FenixDispatchAction {

    public ActionForward manage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	final Set<CerimonyInquiry> cerimonyInquirySet = rootDomainObject.getCerimonyInquirySet();
	request.setAttribute("cerimonyInquirySet", new TreeSet<CerimonyInquiry>(cerimonyInquirySet));
	return mapping.findForward("manageAlumniCerimony");
    }

    public ActionForward deleteInquiry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
	final CerimonyInquiry cerimonyInquiry = getDomainObject(request, "cerimonyInquiryId");
	cerimonyInquiry.delete();
	return manage(mapping, form, request, response);
    }

    public ActionForward forwardToInquiry(final ActionMapping mapping, final HttpServletRequest request, final String forward)
		throws Exception {
	final CerimonyInquiry cerimonyInquiry = getDomainObject(request, "cerimonyInquiryId");
	return forwardToInquiry(mapping, request, forward, cerimonyInquiry);
    }

    public ActionForward forwardToInquiry(final ActionMapping mapping, final HttpServletRequest request, final String forward, final CerimonyInquiry cerimonyInquiry)
		throws Exception {
	request.setAttribute("cerimonyInquiry", cerimonyInquiry);
	return mapping.findForward(forward);
    }

    public ActionForward viewInquiry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	return forwardToInquiry(mapping, request, "viewAlumniCerimonyInquiry");
    }

    public ActionForward createNewInquiry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
	final CerimonyInquiry cerimonyInquiry = CerimonyInquiry.createNew();
	return forwardToInquiry(mapping, request, "editAlumniCerimonyInquiry", cerimonyInquiry);
    }

    public ActionForward editInquiry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	return forwardToInquiry(mapping, request, "editAlumniCerimonyInquiry");
    }

    public ActionForward addInquiryAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	final CerimonyInquiry cerimonyInquiry = getDomainObject(request, "cerimonyInquiryId");
	final CerimonyInquiryAnswer cerimonyInquiryAnswer = cerimonyInquiry.createNewAnswer();
	return editInquiryAnswer(mapping, request, cerimonyInquiryAnswer);
    }

    public ActionForward editInquiryAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
	final CerimonyInquiryAnswer cerimonyInquiryAnswer = getDomainObject(request, "cerimonyInquiryAnswerId");
	return editInquiryAnswer(mapping, request, cerimonyInquiryAnswer);
    }

    public ActionForward editInquiryAnswer(ActionMapping mapping, HttpServletRequest request, CerimonyInquiryAnswer cerimonyInquiryAnswer)
		throws Exception {
	request.setAttribute("cerimonyInquiryAnswer", cerimonyInquiryAnswer);
	return forwardToInquiry(mapping, request, "editAlumniCerimonyInquiryAnswer");
    }
    
    public ActionForward deleteInquiryAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	final CerimonyInquiryAnswer cerimonyInquiryAnswer = getDomainObject(request, "cerimonyInquiryAnswerId");
	final CerimonyInquiry cerimonyInquiry = cerimonyInquiryAnswer.getCerimonyInquiry();
	cerimonyInquiryAnswer.delete();
	RenderUtils.invalidateViewState();
	return forwardToInquiry(mapping, request, "viewAlumniCerimonyInquiry", cerimonyInquiry);
    }

    public ActionForward viewInquiryAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	final CerimonyInquiryAnswer cerimonyInquiryAnswer = getDomainObject(request, "cerimonyInquiryAnswerId");
	request.setAttribute("cerimonyInquiryAnswer", cerimonyInquiryAnswer);
	return forwardToInquiry(mapping, request, "viewAlumniCerimonyInquiryAnswer");
    }

    public ActionForward prepareAddPeople(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
	final CerimonyInquiry cerimonyInquiry = getDomainObject(request, "cerimonyInquiryId");
	request.setAttribute("cerimonyInquiry", cerimonyInquiry);
	final UsernameFileBean usernameFileBean = new UsernameFileBean();
	request.setAttribute("usernameFileBean", usernameFileBean);
	return mapping.findForward("addPeopleToCerimonyInquiry");
    }

    public ActionForward addPeople(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	final CerimonyInquiry cerimonyInquiry = getDomainObject(request, "cerimonyInquiryId");
	final UsernameFileBean usernameFileBean = (UsernameFileBean) getRenderedObject();
	final String contents = FileUtils.readFile(usernameFileBean.getInputStream());
	final Set<String> usernames = findUsernames(contents);
	cerimonyInquiry.addPeople(usernames);
	return forwardToInquiry(mapping, request, "viewAlumniCerimonyInquiry", cerimonyInquiry);
    }

    private Set<String> findUsernames(final String contents) {
	final Set<String> result = new HashSet<String>();
	for (final String string : contents.split("\n")) {
	    result.add(string.trim());
	}
	return result;
    }

    public ActionForward viewInquiryPeople(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	final CerimonyInquiry cerimonyInquiry = getDomainObject(request, "cerimonyInquiryId");
	request.setAttribute("cerimonyInquiry", cerimonyInquiry);
	return mapping.findForward("viewInquiryPeople");
    }

}
