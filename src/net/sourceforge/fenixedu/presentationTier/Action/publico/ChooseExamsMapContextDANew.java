/**
 * Project sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoDegreeCurricularPlanAndExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamsMapContextDANew extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final HttpSession session = request.getSession(true);

        if (session != null) {
            Integer degreeId = getFromRequest("degreeID", request);
            request.setAttribute("degreeID", degreeId);
            request.setAttribute("degree", rootDomainObject.readDegreeByOID(degreeId));

            Integer executionDegreeId = getFromRequest("executionDegreeID", request);
            request.setAttribute("executionDegreeID", executionDegreeId);

            Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
            
            List<LabelValueBean> executionPeriodsLabelValueList = buildExecutionPeriodsLabelValueList(degreeCurricularPlanId);
            if (executionPeriodsLabelValueList.size() > 1) {
                request.setAttribute("lista", executionPeriodsLabelValueList);
            } else {
                request.removeAttribute("lista");
            }

            Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
            if (inEnglish == null) {
                inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
            }
            request.setAttribute("inEnglish", inEnglish);
            
            request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);

            return mapping.findForward("prepare");
        } else {
            throw new Exception();    
        }
    }

    private List<LabelValueBean> buildExecutionPeriodsLabelValueList(Integer degreeCurricularPlanId) throws Exception {
        List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
        try {
            final Object argsLerLicenciaturas[] = { degreeCurricularPlanId };
            infoExecutionDegreeList = (List<InfoExecutionDegree>) ServiceUtils.executeService(null, "ReadPublicExecutionDegreeByDCPID", argsLerLicenciaturas);
        } catch (FenixServiceException e) {
            throw new Exception(e);
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
            } catch (FenixServiceException e) {
                throw new Exception(e);
            }
        }

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("value"));
        Collections.sort(result, comparatorChain);
        Collections.reverse(result);
        
        return result;
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final HttpSession session = request.getSession(false);
        SessionUtils.removeAttributtes(session, SessionConstants.CONTEXT_PREFIX);

        if (session != null) {
            final ActionErrors errors = new ActionErrors();
            final DynaActionForm chooseExamContextoForm = (DynaActionForm) form;
            
            // index
            Integer indexValue = getFromRequest("index", request);
            request.setAttribute("index", indexValue);
            
            // curricularYearList
            final Boolean selectAllCurricularYears = (Boolean) chooseExamContextoForm.get("selectAllCurricularYears");
            final String[] selectedCurricularYears;
            if (selectAllCurricularYears != null && selectAllCurricularYears) {
                String[] allCurricularYears = { "1", "2", "3", "4", "5" };
                selectedCurricularYears = allCurricularYears;
            } else {
                selectedCurricularYears = (String[]) chooseExamContextoForm.get("selectedCurricularYears"); 
            }
            final List<Integer> curricularYears = new ArrayList<Integer>(selectedCurricularYears.length);
            for (String selectedCurricularYear : selectedCurricularYears) {
                curricularYears.add(Integer.valueOf(selectedCurricularYear));
            }
            request.setAttribute("curricularYearList", curricularYears);
            
            // degreeID
            Integer degreeId = (Integer) chooseExamContextoForm.get("degreeID");
            request.setAttribute("degreeID", degreeId);

            // degreeCurricularPlanID
            Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
            final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
            if (!degreeCurricularPlan.getDegree().getIdInternal().equals(degreeId)) {
                throw new FenixActionException();
            } else {
                request.setAttribute("degree", degreeCurricularPlan.getDegree());    
            }

            // lista
            List<LabelValueBean> executionPeriodsLabelValueList = buildExecutionPeriodsLabelValueList(degreeCurricularPlanId);
            if (executionPeriodsLabelValueList.size() > 1) {
                request.setAttribute("lista", executionPeriodsLabelValueList);
            } else {
                request.removeAttribute("lista");
            }
            
            // infoDegreeCurricularPlan
            List<InfoDegreeCurricularPlan> infoDegreeCurricularPlanList = null;
            try {
                final Object[] args1 = { degreeId };
                infoDegreeCurricularPlanList = (List<InfoDegreeCurricularPlan>) ServiceManagerServiceFactory.executeService(null, "ReadPublicDegreeCurricularPlansByDegree", args1);
            } catch (FenixServiceException e) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("state"));
            comparatorChain.addComparator(new BeanComparator("initialDate"), true);
            Collections.sort(infoDegreeCurricularPlanList, comparatorChain);
            if (degreeCurricularPlanId != null) {
                for (InfoDegreeCurricularPlan infoDegreeCurricularPlanElem : infoDegreeCurricularPlanList) {
                    if (infoDegreeCurricularPlanElem.getIdInternal().equals(degreeCurricularPlanId)) {
                        infoDegreeCurricularPlanElem.prepareEnglishPresentation(getLocale(request));
                        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlanElem);
                        break;
                    }
                }
            }

            // indice, SessionConstants.EXECUTION_PERIOD, SessionConstants.EXECUTION_PERIOD_OID
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
            Integer executionPeriodID = (Integer) chooseExamContextoForm.get("indice");
            if (executionPeriodID != null) {
                try {
                    final Object args[] = { executionPeriodID };
                    infoExecutionPeriod = (InfoExecutionPeriod) ServiceManagerServiceFactory.executeService(null, "ReadExecutionPeriodByOID", args);
                } catch (FenixServiceException e) {
                    errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
                    saveErrors(request, errors);
                    return new ActionForward(mapping.getInput());
                }
            }
            request.setAttribute("indice", infoExecutionPeriod.getIdInternal());
            chooseExamContextoForm.set("indice", infoExecutionPeriod.getIdInternal());
            RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());
            
            // SessionConstants.EXECUTION_DEGREE, executionDegreeID, infoExecutionDegree
            final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());
            
            ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionPeriod.getExecutionYear());
            if (executionDegree == null) {
                executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();
                
                infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear().readExecutionPeriodForSemester(1));
                request.setAttribute("indice", infoExecutionPeriod.getIdInternal());
                chooseExamContextoForm.set("indice", infoExecutionPeriod.getIdInternal());
                RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
                request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
                request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());
            }
            
            if (executionDegree != null) {
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoDegreeCurricularPlanAndExecutionYear.newInfoFromDomain(executionDegree);
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
                request.setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal().toString());
                RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
            } else {
                return mapping.findForward("viewExamsMap");
            }

            return mapping.findForward("showExamsMap");
        } else {
            throw new Exception();
        }
    }

}
