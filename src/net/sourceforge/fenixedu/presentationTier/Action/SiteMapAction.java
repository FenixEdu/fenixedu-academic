package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
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

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

        final List degrees = (List) ServiceManagerServiceFactory.executeService(null, "ReadDegrees", null);
        Collections.sort(degrees, degreesComparatorChain);
        request.setAttribute("degrees", degrees);

        return mapping.findForward("site-map");
    }

}