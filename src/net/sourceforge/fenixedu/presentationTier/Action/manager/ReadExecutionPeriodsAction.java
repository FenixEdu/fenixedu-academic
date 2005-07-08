/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lmac1
 * @author Fernanda Quitério 17/Dez/2003
 */
public class ReadExecutionPeriodsAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        try {
            List infoExecutionPeriods = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionPeriods", null);

            if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {

                //				Collections.sort(infoExecutionPeriods, new
                // ExecutionPeriodComparator());
                ComparatorChain comparator = new ComparatorChain();
                comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
                comparator.addComparator(new BeanComparator("name"), true);
                Collections.sort(infoExecutionPeriods, comparator);

                if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
                    request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, infoExecutionPeriods);
                }

            }
        } catch (FenixServiceException ex) {
            throw new FenixActionException("Problemas de comunicação com a base de dados.", ex);
        }

        return mapping.findForward("readExecutionPeriods");

    }
}