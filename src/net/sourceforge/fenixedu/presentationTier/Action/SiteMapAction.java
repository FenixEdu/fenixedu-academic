package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SiteMapAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws FenixActionException, FenixFilterException,
			FenixServiceException {
		final TreeSet<Degree> degrees = new TreeSet<Degree>(Degree.DEGREE_COMPARATOR_BY_NAME_AND_DEGREE_TYPE);
		for (final Degree degree : RootDomainObject.getInstance().getDegreesSet()) {
		    degrees.add(degree);
		}
		request.setAttribute("degrees", degrees);
		return mapping.findForward("site-map");
	}
    
}
