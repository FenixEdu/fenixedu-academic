package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.Util;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class PrepareConsultCurricularPlanDispatchAction extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixServiceException {

        request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);

        Integer degreeCurricularPlanId = (Integer) request.getAttribute("degreeCurricularPlanID");
        if (degreeCurricularPlanId == null) {
            degreeCurricularPlanId = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
        }
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Integer degreeId = (Integer) request.getAttribute("degreeID");
        if (degreeId == null) {
            degreeId = Integer.valueOf(request.getParameter("degreeID"));
        }
        request.setAttribute("degreeID", degreeId);

        DynaActionForm indexForm = (DynaActionForm) form;

        Integer index = (Integer) indexForm.get("index");
        request.setAttribute("index", index);
        indexForm.set("index", index);

        request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);

        try {
            final Object argsLerLicenciaturas[] = { degreeCurricularPlanId };
            final List<InfoExecutionDegree> infoExecutionDegreeList = (List<InfoExecutionDegree>) ServiceUtils.executeService(null, "ReadPublicExecutionDegreeByDCPID", argsLerLicenciaturas);

            if (!infoExecutionDegreeList.isEmpty()) {
                List<LabelValueBean> executionPeriodsLabelValueList = new ArrayList<LabelValueBean>();
                InfoExecutionDegree infoExecutionDegree1 = infoExecutionDegreeList.get(0);
                executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionDegree1.getInfoExecutionYear().getYear(), "" + infoExecutionDegree1.getInfoExecutionYear().getIdInternal()));

                for (int i = 1; i < infoExecutionDegreeList.size(); i++) {
                    final InfoExecutionDegree infoExecutionDegree = infoExecutionDegreeList.get(i);

                    if (infoExecutionDegree.getInfoExecutionYear().getYear() != infoExecutionDegree1.getInfoExecutionYear().getYear()) {
                        executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionDegree.getInfoExecutionYear().getYear(), "" + infoExecutionDegree.getInfoExecutionYear().getIdInternal()));
                        infoExecutionDegree1 = infoExecutionDegreeList.get(i);
                    }
                }

                if (executionPeriodsLabelValueList.size() > 1) {
                    request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);
                } else {
                    request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);
                }
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List<LabelValueBean> anosCurriculares = new ArrayList<LabelValueBean>();
        anosCurriculares.add(new LabelValueBean("---------", ""));
        anosCurriculares.add(new LabelValueBean("1", "1"));
        anosCurriculares.add(new LabelValueBean("2", "2"));
        anosCurriculares.add(new LabelValueBean("3", "3"));
        anosCurriculares.add(new LabelValueBean("4", "4"));
        anosCurriculares.add(new LabelValueBean("5", "5"));
        request.setAttribute("curricularYearList", anosCurriculares);
        
        // If executionPeriod was previously selected,form has that value as default
        InfoExecutionPeriod selectedExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
        if (selectedExecutionPeriod != null) {
            indexForm.set("indice", selectedExecutionPeriod.getInfoExecutionYear().getIdInternal());
            indexForm.set("curYear", Integer.valueOf(anosCurriculares.indexOf(anosCurriculares.get(0))));
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, selectedExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, selectedExecutionPeriod.getIdInternal().toString());
        }

        try {
            final Object arg[] = { degreeCurricularPlanId, (Integer) indexForm.get("indice") };
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(null, "ReadPublicExecutionDegreeByDCPID", arg);
            if (infoExecutionDegree == null) {
                try {
                    final Object arg1[] = { degreeCurricularPlanId };
                    List<InfoExecutionDegree> infoExecutionDegrees = (List<InfoExecutionDegree>) ServiceUtils.executeService(null, "ReadPublicExecutionDegreeByDCPID", arg1);
                    if (infoExecutionDegrees.size() >= 1) {
                        infoExecutionDegree = infoExecutionDegrees.get(infoExecutionDegrees.size() - 1);
                        indexForm.set("indice", infoExecutionDegree.getInfoExecutionYear().getIdInternal());
                    }
                } catch (FenixServiceException e1) {
                    return mapping.findForward("Sucess");
                }
            }
            RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        // TODO: No futuro, os edificios devem ser lidos da BD
        List buildings = Util.readExistingBuldings("*", null);
        request.setAttribute("publico.buildings", buildings);

        // TODO: No futuro, os tipos de salas devem ser lidos da BD
        List<LabelValueBean> types = new ArrayList<LabelValueBean>();
        types.add(new LabelValueBean("*", null));
        types.add(new LabelValueBean("Anfiteatro", (Integer.valueOf(TipoSala.ANFITEATRO)).toString()));
        types.add(new LabelValueBean("Laborat�rio", (Integer.valueOf(TipoSala.LABORATORIO)).toString()));
        types.add(new LabelValueBean("Plana", (Integer.valueOf(TipoSala.PLANA)).toString()));
        request.setAttribute("publico.types", types);

        return mapping.findForward("Sucess");
    }

    public ActionForward select(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm escolherContextoForm = (DynaActionForm) form;
        HttpSession session = request.getSession(true);
        SessionUtils.removeAttributtes(session, SessionConstants.CONTEXT_PREFIX);

        Integer executionYear = (Integer) escolherContextoForm.get("indice");

        Integer degreeCurricularPlanId = (Integer) request.getAttribute("degreeCurricularPlanID");
        if (degreeCurricularPlanId == null)
            degreeCurricularPlanId = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Integer degreeId = (Integer) request.getAttribute("degreeID");
        if (degreeId == null)
            degreeId = Integer.valueOf(request.getParameter("degreeID"));

        request.setAttribute("degreeID", degreeId);

        Integer index = (Integer) escolherContextoForm.get("index");
        Integer curricularYear = (Integer) escolherContextoForm.get("curYear");
        request.setAttribute("index", index);
        request.setAttribute("curYear", curricularYear);
        escolherContextoForm.set("index", index);

        Object args[] = { executionYear };
        List infoExecutionPeriodList;
        try {
            infoExecutionPeriodList = (List) ServiceUtils.executeService(null,
                    "ReadExecutionPeriodsByExecutionYear", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriodList.get(0));
        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID,
                ((InfoExecutionPeriod) infoExecutionPeriodList.get(0)).getIdInternal().toString());
        RequestUtils.setExecutionPeriodToRequest(request, (InfoExecutionPeriod) infoExecutionPeriodList
                .get(0));

        // ----------------------------------------------------------
        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        Object arg[] = { degreeCurricularPlanId, executionYear };

        try {
            infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(null,
                    "ReadPublicExecutionDegreeByDCPID", arg);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        // request.setAttribute("windowLocation",FenixCacheFilter.getPageURL(request));
        RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
        return mapping.findForward("Sucess");

    }

}
