package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewAssiduousnessDispatchAction extends FenixDispatchAction {

    public ActionForward showSchedules(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        List<WorkScheduleType> workScheduleList = new ArrayList<WorkScheduleType>();
        for (WorkScheduleType workScheduleType : rootDomainObject.getWorkScheduleTypes()) {
            workScheduleList.add(workScheduleType);
        }
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("ojbConcreteClass"));
        comparatorChain.addComparator(new BeanComparator("acronym"));
        Collections.sort(workScheduleList, comparatorChain);
        request.setAttribute("workScheduleList", workScheduleList);
        return mapping.findForward("show-all-schedules");
    }

    public ActionForward showJustificationMotives(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        List<JustificationMotive> justificationMotives = new ArrayList<JustificationMotive>();
        for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
            if (justificationMotive.getJustificationType() != null) {
                justificationMotives.add(justificationMotive);
            }
        }
        Collections.sort(justificationMotives, new BeanComparator("justificationType"));
        request.setAttribute("justificationMotives", justificationMotives);
        return mapping.findForward("show-justification-motives");
    }

    public ActionForward showRegularizationMotives(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        List<JustificationMotive> regularizationMotives = new ArrayList<JustificationMotive>();
        for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
            if (justificationMotive.getJustificationType() == null) {
                regularizationMotives.add(justificationMotive);
            }
        }
        Collections.sort(regularizationMotives, new BeanComparator("acronym"));
        request.setAttribute("regularizationMotives", regularizationMotives);
        return mapping.findForward("show-regularization-motives");
    }

}