package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ExecutionPeriodDA extends FenixContextDispatchAction {

    private static final Comparator<ExecutionDegree> executionDegreeComparator = new Comparator<ExecutionDegree>() {
        public int compare(ExecutionDegree executionDegree1, ExecutionDegree executionDegree2) {
            final Degree degree1 = executionDegree1.getDegreeCurricularPlan().getDegree();
            final Degree degree2 = executionDegree2.getDegreeCurricularPlan().getDegree();

            int degreeTypeComparison = degree1.getTipoCurso().compareTo(degree2.getTipoCurso());
            return (degreeTypeComparison != 0) ? degreeTypeComparison : degree1.getNome().compareTo(degree2.getNome());
        }};

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        IUserView userView = getUserView(request);

        InfoExecutionPeriod selectedExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object argsReadExecutionPeriods[] = {};
        List executionPeriods = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                "ReadNotClosedExecutionPeriods", argsReadExecutionPeriods);
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(executionPeriods, chainComparator);

        // if executionPeriod was previously selected,form has that
        // value as default
        if (selectedExecutionPeriod != null) {
            DynaActionForm indexForm = (DynaActionForm) form;
            indexForm.set("index",
            //new Integer(executionPeriods.indexOf(selectedExecutionPeriod))
                    selectedExecutionPeriod.getIdInternal());
        }
        //----------------------------------------------

        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), ""
                    + infoExecutionPeriod.getIdInternal()));
        }

        request.setAttribute(SessionConstants.LIST_INFOEXECUTIONPERIOD, executionPeriods);

        request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);

        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(selectedExecutionPeriod.getIdInternal());
        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(executionPeriod.getExecutionYear().getExecutionDegrees());
        Collections.sort(executionDegrees, executionDegreeComparator);
        request.setAttribute("executionPeriodDomainObject", executionPeriod);
        request.setAttribute("executionDegrees", executionDegrees);

        return mapping.findForward("showForm");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm indexForm = (DynaActionForm) form;

        //Integer index = (Integer) indexForm.get("index");
        Integer executionPeriodOid = (Integer) indexForm.get("index");
        if (executionPeriodOid != null) {
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOid.toString());
            ContextUtils.setExecutionPeriodContext(request);
        }

        return mapping.findForward("choose");
    }

    public ActionForward chooseForViewRoom(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        DynaActionForm indexForm = (DynaActionForm) form;

        IUserView userView = getUserView(request);

        // get selected execution period
        InfoExecutionPeriod selectedInfoExecutionPeriod = null;
        Integer executionPeriodOid = (Integer) indexForm.get("index");
        if (executionPeriodOid != null) {
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOid.toString());
            ContextUtils.setExecutionPeriodContext(request);
            selectedInfoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
        }

        Object argsReadExecutionPeriods[] = {};
        List executionPeriods = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                "ReadNotClosedExecutionPeriods", argsReadExecutionPeriods);
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"));
        chainComparator.addComparator(new BeanComparator("semester"));
        Collections.sort(executionPeriods, chainComparator);

        // if executionPeriod was previously selected,form has that
        // value as default
        if (selectedInfoExecutionPeriod != null) {
            indexForm.set("index",
            //new Integer(executionPeriods.indexOf(selectedExecutionPeriod))
                    selectedInfoExecutionPeriod.getIdInternal());
        }
        //----------------------------------------------

        List executionPeriodsLabelValueList = new ArrayList();
        for (int i = 0; i < executionPeriods.size(); i++) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear(), ""
                    + infoExecutionPeriod.getIdInternal()));
        }

        // read lessons of (previously selected) room in selected
        // executionPeriod
        InfoRoom infoRoom = null;
        Object argsReadRoomByOID[] = new Object[1];
        if (request.getParameter(SessionConstants.ROOM_OID) != null) {
            argsReadRoomByOID[0] = new Integer(request.getParameter(SessionConstants.ROOM_OID));
        } else {
        }

        try {
            infoRoom = (InfoRoom) ServiceUtils.executeService(null, "ReadRoomByOID", argsReadRoomByOID);
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }
        request.setAttribute(SessionConstants.ROOM, infoRoom);
        request.setAttribute(SessionConstants.ROOM_OID, infoRoom.getIdInternal());

        Object argsReadLessons[] = { selectedInfoExecutionPeriod, infoRoom, null };

        try {
            List lessons;
            lessons = (List) ServiceUtils.executeService(null, "LerAulasDeSalaEmSemestre",
                    argsReadLessons);

            if (lessons != null) {
                request.setAttribute(SessionConstants.LESSON_LIST_ATT, lessons);
            }

        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }
        return mapping.findForward("viewRoom");
    }

}