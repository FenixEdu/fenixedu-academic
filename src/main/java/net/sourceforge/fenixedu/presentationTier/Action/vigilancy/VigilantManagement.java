package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.ConfirmConvoke;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.OtherCourseVigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class VigilantManagement extends FenixDispatchAction {

    public ActionForward prepareMap(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantBean bean = new VigilantBean();
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        prepareBean(bean, request, executionYear);

        return mapping.findForward("displayConvokeMap");
    }

    public ActionForward displayMap(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("selectGroup");
        if (viewState == null) {
            return prepareMap(mapping, form, request, response);
        }

        VigilantBean bean = (VigilantBean) viewState.getMetaObject().getObject();

        prepareBean(bean, request, bean.getExecutionYear());

        RenderUtils.invalidateViewState("selectGroup");
        return mapping.findForward("displayConvokeMap");
    }

    public ActionForward vigilantAcceptsConvoke(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String id = request.getParameter("oid");
        Integer idInternal = Integer.valueOf(id);
        OtherCourseVigilancy vigilancy =
                (OtherCourseVigilancy) RootDomainObject.readDomainObjectByOID(OtherCourseVigilancy.class, idInternal);

        ConfirmConvoke.run(vigilancy);

        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        VigilantGroup group = getGroupFromRequestOrVigilant(request, vigilancy.getVigilantWrapper());
        VigilantBean bean = new VigilantBean();
        bean.setExecutionYear(executionYear);
        bean.setSelectedVigilantGroup(group);

        bean.setVigilantGroups(vigilancy.getVigilantWrapper().getPerson().getVisibleVigilantGroups(executionYear));

        List<Vigilancy> activeOtherCourseVigilancies = new ArrayList<Vigilancy>();
        List<VigilantWrapper> vigilantWrappers = vigilancy.getVigilantWrapper().getPerson().getVigilantWrappers();
        for (VigilantWrapper vigilantWrapper : vigilantWrappers) {
            activeOtherCourseVigilancies.addAll(vigilantWrapper.getActiveOtherCourseVigilancies());
        }
        bean.setActiveOtherCourseVigilancies(activeOtherCourseVigilancies);

        request.setAttribute("vigilant", vigilancy.getVigilantWrapper());
        request.setAttribute("bean", bean);

        return mapping.findForward("displayConvokeMap");
    }

    public ActionForward showWrittenEvaluationReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String writtenEvaluationId = request.getParameter("writtenEvaluationId");
        WrittenEvaluation writtenEvaluation =
                (WrittenEvaluation) RootDomainObject.readDomainObjectByOID(WrittenEvaluation.class,
                        Integer.valueOf(writtenEvaluationId));

        request.setAttribute("writtenEvaluation", writtenEvaluation);
        return mapping.findForward("showReport");
    }

    private void prepareBean(VigilantBean bean, HttpServletRequest request, ExecutionYear executionYear)
            throws  FenixServiceException {
        List<VigilantWrapper> vigilantWrappers = getVigilantWrappersForPerson(request);

        bean.setExecutionYear(executionYear);
        VigilantGroup selectedVigilantGroup = bean.getSelectedVigilantGroup();

        VigilantWrapper vigilant = vigilantWrappers.get(0);
        if (selectedVigilantGroup != null) {
            for (VigilantWrapper vigilantWrapper : vigilantWrappers) {
                if (vigilantWrapper.getVigilantGroup().equals(selectedVigilantGroup)) {
                    vigilant = vigilantWrapper;
                }
            }
        }

        if (selectedVigilantGroup != null && !selectedVigilantGroup.getExecutionYear().equals(executionYear)) {
            bean.setSelectedVigilantGroup(null);
        }

        if (vigilant != null) {
            List<VigilantGroup> groups = vigilant.getPerson().getVisibleVigilantGroups(executionYear);

            bean.setVigilantGroups(groups);
            if (groups.size() == 1) {
                VigilantGroup group = groups.get(0);
                bean.setSelectedVigilantGroup(group);
            }
        } else {
            bean.setVigilantGroups(new ArrayList<VigilantGroup>());
        }

        List<Vigilancy> activeOtherCourseVigilancies = new ArrayList<Vigilancy>();
        for (VigilantWrapper vigilantWrapper : vigilantWrappers) {
            activeOtherCourseVigilancies.addAll(vigilantWrapper.getActiveOtherCourseVigilancies());
        }
        bean.setActiveOtherCourseVigilancies(activeOtherCourseVigilancies);

        request.setAttribute("vigilant", vigilant);
        request.setAttribute("bean", bean);
    }

    private List<VigilantWrapper> getVigilantWrappersForPerson(HttpServletRequest request) throws 
            FenixServiceException {
        Person person = getLoggedPerson(request);
        List<VigilantWrapper> vigilantWrappers = person.getVigilantWrappers();
        return vigilantWrappers;
    }

    private VigilantGroup getGroupFromRequestOrVigilant(HttpServletRequest request, VigilantWrapper vigilant) {
        String groupId = request.getParameter("gid");
        return (groupId == null) ? vigilant.getVigilantGroup() : (VigilantGroup) RootDomainObject.readDomainObjectByOID(
                VigilantGroup.class, Integer.valueOf(groupId));
    }

}