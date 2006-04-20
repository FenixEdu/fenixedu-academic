package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author Luis Cruz
 */
public class SiteMapAction extends FenixAction {

	private static final ComparatorChain degreesComparatorChain = new ComparatorChain();

	static {
		degreesComparatorChain.addComparator(new BeanComparator("tipoCurso"));
		degreesComparatorChain.addComparator(new BeanComparator("nome"));
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws FenixActionException, FenixFilterException,
			FenixServiceException {
		final TreeSet<Degree> oldDegrees = new TreeSet<Degree>(
				degreesComparatorChain);
		final TreeSet<Degree> bolonhaDegrees = new TreeSet<Degree>(
				degreesComparatorChain);
		for (final Degree d : RootDomainObject.getInstance().getDegreesSet()) {
			if (d.isBolonhaDegree())
				bolonhaDegrees.add(d);
			else
				oldDegrees.add(d);
		}
		request.setAttribute("oldDegrees", oldDegrees);
		request.setAttribute("bolonhaDegrees", bolonhaDegrees);
		return mapping.findForward("site-map");
	}
}