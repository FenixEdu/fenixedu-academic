package net.sourceforge.fenixedu.presentationTier.Action.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedPublicExecutionPeriodsByExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadPublicExecutionDegreeByDCPID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public abstract class FenixContextDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ContextUtils.setContextSelectionBean(request, getRenderedObject());

        ContextUtils.setExecutionPeriodContext(request);

        ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        return actionForward;
    }

    public static String getFromRequest(String parameter, HttpServletRequest request) {
        if (request.getParameter(parameter) != null) {
            return request.getParameter(parameter);
        } else if (request.getAttribute(parameter) != null) {
            if (request.getAttribute(parameter) instanceof String) {
                return (String) request.getAttribute(parameter);
            }
        }
        return null;
    }

    public static Boolean getFromRequestBoolean(String parameter, HttpServletRequest request) {
        return (request.getParameter(parameter) != null) ? Boolean.valueOf(request.getParameter(parameter)) : (Boolean) request
                .getAttribute(parameter);
    }

    protected List<LabelValueBean> buildExecutionPeriodsLabelValueList(String degreeCurricularPlanId) throws FenixActionException {
        List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
        try {

            infoExecutionDegreeList = ReadPublicExecutionDegreeByDCPID.run(degreeCurricularPlanId);
        } catch (Exception e) {
            throw new FenixActionException(e);
        }

        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        for (InfoExecutionDegree infoExecutionDegree : infoExecutionDegreeList) {

            try {
                List<InfoExecutionPeriod> infoExecutionPeriodsList =
                        ReadNotClosedPublicExecutionPeriodsByExecutionYear.run(infoExecutionDegree.getInfoExecutionYear());

                for (InfoExecutionPeriod infoExecutionPeriodIter : infoExecutionPeriodsList) {
                    result.add(new LabelValueBean(infoExecutionPeriodIter.getName() + " - "
                            + infoExecutionPeriodIter.getInfoExecutionYear().getYear(), infoExecutionPeriodIter.getExternalId()
                            .toString()));
                }
            } catch (Exception e) {
                throw new FenixActionException(e);
            }
        }

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("value"));
        Collections.sort(result, comparatorChain);
        Collections.reverse(result);

        return result;
    }

}