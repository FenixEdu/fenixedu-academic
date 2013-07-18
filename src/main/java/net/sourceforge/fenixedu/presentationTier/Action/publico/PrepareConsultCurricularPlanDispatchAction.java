package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodsByExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadPublicExecutionDegreeByDCPID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.Util;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/prepareConsultCurricularPlanNew", attribute = "chooseContextDegreeForm",
        formBean = "chooseContextDegreeForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "Sucess", path = "/showDegreeCurricularPlanNew.do?method=showCurricularPlan&page=0"),
        @Forward(name = "choose", path = "/prepareSelectExecutionCourseActionNew.do") })
public class PrepareConsultCurricularPlanDispatchAction extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException,  FenixServiceException {

        request.removeAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD);

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

        request.removeAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD);

        try {

            final List<InfoExecutionDegree> infoExecutionDegreeList =
                    ReadPublicExecutionDegreeByDCPID.run(degreeCurricularPlanId);

            if (!infoExecutionDegreeList.isEmpty()) {
                List<LabelValueBean> executionPeriodsLabelValueList = new ArrayList<LabelValueBean>();
                InfoExecutionDegree infoExecutionDegree1 = infoExecutionDegreeList.get(0);
                executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionDegree1.getInfoExecutionYear().getYear(), ""
                        + infoExecutionDegree1.getInfoExecutionYear().getIdInternal()));

                for (int i = 1; i < infoExecutionDegreeList.size(); i++) {
                    final InfoExecutionDegree infoExecutionDegree = infoExecutionDegreeList.get(i);

                    if (infoExecutionDegree.getInfoExecutionYear().getYear() != infoExecutionDegree1.getInfoExecutionYear()
                            .getYear()) {
                        executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionDegree.getInfoExecutionYear()
                                .getYear(), "" + infoExecutionDegree.getInfoExecutionYear().getIdInternal()));
                        infoExecutionDegree1 = infoExecutionDegreeList.get(i);
                    }
                }

                if (executionPeriodsLabelValueList.size() > 1) {
                    request.setAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);
                } else {
                    request.removeAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD);
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

        // If executionPeriod was previously selected,form has that value as
        // default
        InfoExecutionPeriod selectedExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        if (selectedExecutionPeriod != null) {
            indexForm.set("indice", selectedExecutionPeriod.getInfoExecutionYear().getIdInternal());
            indexForm.set("curYear", Integer.valueOf(anosCurriculares.indexOf(anosCurriculares.get(0))));
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD, selectedExecutionPeriod);
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, selectedExecutionPeriod.getIdInternal().toString());
        }

        InfoExecutionDegree infoExecutionDegree =
                ReadPublicExecutionDegreeByDCPID.run(degreeCurricularPlanId, (Integer) indexForm.get("indice"));
        if (infoExecutionDegree == null) {
            try {

                List<InfoExecutionDegree> infoExecutionDegrees = ReadPublicExecutionDegreeByDCPID.run(degreeCurricularPlanId);
                if (infoExecutionDegrees.size() >= 1) {
                    infoExecutionDegree = infoExecutionDegrees.get(infoExecutionDegrees.size() - 1);
                    indexForm.set("indice", infoExecutionDegree.getInfoExecutionYear().getIdInternal());
                }
            } catch (FenixServiceException e1) {
                return mapping.findForward("Sucess");
            }
        }
        RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);

        // TODO: No futuro, os edificios devem ser lidos da BD
        List buildings = Util.readExistingBuldings("*", null);
        request.setAttribute("publico.buildings", buildings);

        // TODO: No futuro, os tipos de salas devem ser lidos da BD
        List<LabelValueBean> types = Util.readTypesOfRooms("*", null);
        request.setAttribute("publico.types", types);

        return mapping.findForward("Sucess");
    }

    public ActionForward select(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm escolherContextoForm = (DynaActionForm) form;

        Integer executionYear = (Integer) escolherContextoForm.get("indice");

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

        Integer index = (Integer) escolherContextoForm.get("index");
        Integer curricularYear = (Integer) escolherContextoForm.get("curYear");
        request.setAttribute("index", index);
        request.setAttribute("curYear", curricularYear);
        escolherContextoForm.set("index", index);

        List infoExecutionPeriodList = ReadExecutionPeriodsByExecutionYear.run(executionYear);

        request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriodList.get(0));
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, ((InfoExecutionPeriod) infoExecutionPeriodList.get(0))
                .getIdInternal().toString());
        RequestUtils.setExecutionPeriodToRequest(request, (InfoExecutionPeriod) infoExecutionPeriodList.get(0));

        // ----------------------------------------------------------

        InfoExecutionDegree infoExecutionDegree = ReadPublicExecutionDegreeByDCPID.run(degreeCurricularPlanId, executionYear);

        // request.setAttribute("windowLocation",FenixCacheFilter.getPageURL(
        // request));
        RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
        return mapping.findForward("Sucess");

    }

}