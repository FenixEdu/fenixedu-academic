package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class MainPage extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

    	final Person person = getLoggedPerson(request);

    	final List<Advisory> advisories = new ArrayList(person.getAdvisories());
    	Collections.sort(advisories, new ReverseComparator(new BeanComparator("created")));
    	request.setAttribute(SessionConstants.LIST_ADVISORY, advisories);

    	if (!advisories.isEmpty()) {
    		final String selectedOidString = request.getParameter("activeAdvisory");
    		final Integer selectedOid = (selectedOidString != null) ?
    				Integer.valueOf(selectedOidString) : advisories.get(0).getIdInternal();
    		request.setAttribute("activeAdvisory", selectedOid);
    	}

        return mapping.findForward("ShowWelcomePage");
    }

}