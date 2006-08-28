package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewAllClassesSchedulesDA extends FenixContextDispatchAction {

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            //GestorServicos gestor = GestorServicos.manager();
            IUserView userView = getUserView(request);

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
            //				setExecutionContext(request);
            /* Cria o form bean com as licenciaturas em execucao. */
            Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

            List executionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

            Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
            MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
            executionDegreeList = InfoExecutionDegree.buildLabelValueBeansForList(executionDegreeList, messageResources);

            request.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_LIST, executionDegreeList);

            return mapping.findForward("choose");
        }
        throw new Exception();

    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);
            DynaActionForm chooseViewAllClassesSchedulesContextForm = (DynaActionForm) form;

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
            //				setExecutionContext(request);

            Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };
            List infoExecutionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);
            Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());

            Boolean selectAllDegrees = (Boolean) chooseViewAllClassesSchedulesContextForm
                    .get("selectAllDegrees");
            List selectedInfoExecutionDegrees = null;
            if (selectAllDegrees != null && selectAllDegrees.booleanValue()) {
                selectedInfoExecutionDegrees = infoExecutionDegreeList;
            } else {
                String[] selectedDegreesIndexes = (String[]) chooseViewAllClassesSchedulesContextForm
                        .get("selectedDegrees");
                selectedInfoExecutionDegrees = new ArrayList();

                for (int i = 0; i < selectedDegreesIndexes.length; i++) {
                    Integer index = new Integer("" + selectedDegreesIndexes[i]);
                    InfoExecutionDegree infoEd = (InfoExecutionDegree) infoExecutionDegreeList.get(index
                            .intValue());

                    selectedInfoExecutionDegrees.add(infoEd);
                }
            }

            Object[] args = { selectedInfoExecutionDegrees, infoExecutionPeriod };
            List infoViewClassScheduleList = (List) ServiceManagerServiceFactory.executeService(
                    userView, "ReadDegreesClassesLessons", args);

            if (infoViewClassScheduleList != null && infoViewClassScheduleList.isEmpty()) {
                request.removeAttribute(SessionConstants.ALL_INFO_VIEW_CLASS_SCHEDULE);
            } else {
                Collections.sort(infoViewClassScheduleList, new BeanComparator("infoClass.nome"));
                request.setAttribute(SessionConstants.ALL_INFO_VIEW_CLASS_SCHEDULE,
                        infoViewClassScheduleList);
            }

            return mapping.findForward("list");
        }
        throw new Exception();

    }
}