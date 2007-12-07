package net.sourceforge.fenixedu.presentationTier.Action.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public abstract class FenixContextDispatchAction extends FenixDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ContextUtils.setExecutionPeriodContext(request);

        ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        return actionForward;
    }
    
    protected static Integer getFromRequest(String parameter, HttpServletRequest request) {
        if (request.getParameter(parameter) != null) {
            try {
                return Integer.valueOf(request.getParameter(parameter));    
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (request.getAttribute(parameter) != null) {
            if (request.getAttribute(parameter) instanceof String) {
                try {
                    return Integer.valueOf((String) request.getAttribute(parameter));    
                } catch (NumberFormatException e) {
                    return null;
                }
            } else if (request.getAttribute(parameter) instanceof Integer) {
                return (Integer) request.getAttribute(parameter);
            }
        }
        return null;
    }

    protected Boolean getFromRequestBoolean(String parameter, HttpServletRequest request) {
        return (request.getParameter(parameter) != null) ? Boolean.valueOf(request.getParameter(parameter)) : (Boolean) request.getAttribute(parameter);
    }

    protected List<LabelValueBean> buildExecutionPeriodsLabelValueList(Integer degreeCurricularPlanId) throws FenixActionException {
        List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
        try {
            final Object argsLerLicenciaturas[] = { degreeCurricularPlanId };
            infoExecutionDegreeList = (List<InfoExecutionDegree>) ServiceUtils.executeService(null, "ReadPublicExecutionDegreeByDCPID", argsLerLicenciaturas);
        } catch (Exception e) {
            throw new FenixActionException(e);
        } 

        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        for (InfoExecutionDegree infoExecutionDegree : infoExecutionDegreeList) {
            Object args[] = { infoExecutionDegree.getInfoExecutionYear() };
            try {
                List<InfoExecutionPeriod> infoExecutionPeriodsList = (List<InfoExecutionPeriod>) ServiceUtils.executeService(null, "ReadNotClosedPublicExecutionPeriodsByExecutionYear", args);

                for (InfoExecutionPeriod infoExecutionPeriodIter : infoExecutionPeriodsList) {
                    result.add(new LabelValueBean(infoExecutionPeriodIter.getName() + " - " + infoExecutionPeriodIter.getInfoExecutionYear().getYear(), 
                            infoExecutionPeriodIter.getIdInternal().toString()));
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
