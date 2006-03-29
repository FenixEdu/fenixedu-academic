package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
    	final SortedSet<Homepage> others = new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME);
    	homepages.put("?", others);
    	for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
    		homepages.put("" + ((char) i), new TreeSet<Homepage>(Homepage.HOMEPAGE_COMPARATOR_BY_NAME));
    	}
    	for (final Homepage homepage : rootDomainObject.getHomepagesSet()) {
    		final String key = homepage.getName().substring(0, 1);
    		final SortedSet<Homepage> sortedSet;
    		if (homepages.containsKey(key)) {
    			sortedSet = homepages.get(key);
    		} else {
    			sortedSet = others;
    		}
    		sortedSet.add(homepage);
    	}
    	request.setAttribute("homepages", homepages);
        return mapping.findForward("list-homepages");
    }

}