package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.image.TextPngCreator;

public class ViewHomepageDA extends FenixContextDispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final String homepageIDString = request.getParameter("homepageID");
    	final Integer homepageID = Integer.valueOf(homepageIDString);
    	final Homepage homepage = (Homepage) readDomainObject(request, Homepage.class, homepageID);
    	request.setAttribute("homepage", homepage);
        return mapping.findForward("view-homepage");
    }

    public ActionForward notFound(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("not-found-homepage");
    }

    public ActionForward list(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final SortedMap<String, SortedSet<Homepage>> homepages = new TreeMap<String, SortedSet<Homepage>>();
    	for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
    		homepages.put("" + ((char) i), new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME));
    	}
    	for (final Homepage homepage : rootDomainObject.getHomepagesSet()) {
    		final String key = homepage.getName().substring(0, 1);
    		final SortedSet<Homepage> sortedSet;
    		if (homepages.containsKey(key)) {
    			sortedSet = homepages.get(key);
    			sortedSet.add(homepage);
    		}
    	}
    	request.setAttribute("homepages", homepages);

    	final String selectedPage = request.getParameter("selectedPage");
    	if (selectedPage != null) {
    		request.setAttribute("selectedPage", selectedPage);
    	} else {
    		request.setAttribute("selectedPage", "");
    	}

        return mapping.findForward("list-homepages");
    }

    public ActionForward emailPng(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String email = getEmailString(request);
        final byte[] pngFile = TextPngCreator.createPng("Sleek", 25, "000000", email);
        response.setContentType("image/png");
        response.getOutputStream().write(pngFile);
        response.getOutputStream().close();
        return null;
    }

    public ActionForward stats(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	request.setAttribute("homepages", rootDomainObject.getHomepages());
    	return mapping.findForward("homepage-stats");
    }

    private String getEmailString(final HttpServletRequest request) {
        final String personIDString = request.getParameter("personID");
        if (personIDString != null && personIDString.length() > 0) {
            final Person person = (Person) rootDomainObject.readPartyByOID(Integer.valueOf(personIDString));
            if (person != null && person.getHomepage() != null && person.getHomepage().getActivated().booleanValue() && person.getHomepage().getShowEmail().booleanValue()) {
                return person.getEmail();
            }
        }
        return "";
    }

    public ActionForward retrievePhoto(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String homepageIDString = request.getParameter("homepageID");
        final Integer homepageID = Integer.valueOf(homepageIDString);
        final Homepage homepage = (Homepage) readDomainObject(request, Homepage.class, homepageID);
        if (homepage != null && homepage.getShowPhoto() != null && homepage.getShowPhoto().booleanValue()) {
            final Person person = homepage.getPerson();
            final FileEntry personalPhoto = person.getPersonalPhoto();

            if (personalPhoto != null) {
                response.setContentType(personalPhoto.getContentType().getMimeType());
                DataOutputStream dos = new DataOutputStream(response.getOutputStream());
                dos.write(personalPhoto.getContents());
                dos.close();
            }
        }

        return null;
    }

}