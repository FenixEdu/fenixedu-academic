package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.CreateUnavailablePeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.DeleteUnavailablePeriodByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.EditUnavailablePeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class UnavailablePeriodManagement extends FenixDispatchAction {

    public ActionForward addUnavailablePeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String vid = request.getParameter("vid");
        VigilantWrapper vigilantWrapper = AbstractDomainObject.fromExternalId(vid);
        String gid = request.getParameter("gid");
        VigilantGroup group = AbstractDomainObject.fromExternalId(gid);

        UnavailablePeriodBean bean = new UnavailablePeriodBean();
        bean.setVigilantWrapper(group.getVigilantWrapperFor(vigilantWrapper.getPerson()));
        bean.setSelectedVigilantGroup(group);

        request.setAttribute("bean", bean);
        return mapping.findForward("addUnavailablePeriod");

    }

    public ActionForward createUnavailablePeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        UnavailablePeriodBean periodBean =
                (UnavailablePeriodBean) RenderUtils.getViewState("createUnavailablePeriod").getMetaObject().getObject();
        VigilantWrapper vigilantWrapper = periodBean.getVigilantWrapper();

        try {

            CreateUnavailablePeriod.run(vigilantWrapper.getPerson(), periodBean.getBeginDate(), periodBean.getEndDate(),
                    periodBean.getJustification());
        } catch (DomainException e) {
            request.setAttribute("bean", periodBean);
            addActionMessage(request, e.getMessage());
            return mapping.findForward("addUnavailablePeriod");
        }

        putRequestVigilantManagementCompliant(request, vigilantWrapper);

        return mapping.findForward("addedUnavailablePeriod");

    }

    public ActionForward editUnavailablePeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareForEdit(request);
        return mapping.findForward("editUnavailablePeriod");
    }

    public ActionForward deleteUnavailablePeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String id = request.getParameter("oid");
        UnavailablePeriod unavailablePeriod = (UnavailablePeriod) AbstractDomainObject.fromExternalId(id);

        VigilantWrapper vigilant = unavailablePeriod.getPerson().getVigilantWrappers().get(0);
        deletePeriod(request);
        putRequestVigilantManagementCompliant(request, vigilant);
        return mapping.findForward("deleteUnavailablePeriod");
    }

    public ActionForward changeUnavailablePeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        UnavailablePeriodBean periodBean = (UnavailablePeriodBean) RenderUtils.getViewState().getMetaObject().getObject();
        VigilantWrapper vigilant = periodBean.getVigilantWrapper();
        try {
            applyChangesToUnavailablePeriod(request, periodBean.getUnavailablePeriod(), periodBean.getBeginDate(),
                    periodBean.getEndDate(), periodBean.getJustification());
        } catch (DomainException e) {
            request.setAttribute("bean", periodBean);
            addActionMessage(request, e.getMessage());
            return mapping.findForward("editUnavailablePeriod");
        }
        putRequestVigilantManagementCompliant(request, vigilant);
        return mapping.findForward("addedUnavailablePeriod");
    }

    public ActionForward changeUnavailablePeriodForVigilant(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        UnavailablePeriodBean periodBean = (UnavailablePeriodBean) RenderUtils.getViewState().getMetaObject().getObject();
        try {
            applyChangesToUnavailablePeriod(request, periodBean.getUnavailablePeriod(), periodBean.getBeginDate(),
                    periodBean.getEndDate(), periodBean.getJustification());
        } catch (DomainException e) {
            String gid = request.getParameter("gid");
            request.setAttribute("gid", gid);
            request.setAttribute("bean", periodBean);
            addActionMessage(request, e.getMessage());
            return mapping.findForward("editPeriodOfVigilant");
        }
        String gid = request.getParameter("gid");
        VigilantGroup group = (VigilantGroup) AbstractDomainObject.fromExternalId(gid);
        VigilantGroupBean bean = new VigilantGroupBean();
        ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();
        bean.setExamCoordinator(coordinator);
        bean.setSelectedVigilantGroup(group);
        putUnavailablePeriodsOnRequest(request, group);
        request.setAttribute("bean", bean);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");
    }

    public ActionForward prepareManageUnavailablePeriodsOfVigilants(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = new VigilantGroupBean();
        ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();
        bean.setExamCoordinator(coordinator);
        request.setAttribute("bean", bean);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");

    }

    public ActionForward manageUnavailablePeriodsOfVigilants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("selectVigilantGroup").getMetaObject().getObject();
        VigilantGroup group = bean.getSelectedVigilantGroup();
        if (group != null) {
            putUnavailablePeriodsOnRequest(request, group);
        }
        request.setAttribute("bean", bean);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");

    }

    public ActionForward editUnavailablePeriodOfVigilant(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareForEdit(request);
        String gid = request.getParameter("gid");
        request.setAttribute("gid", gid);
        return mapping.findForward("editPeriodOfVigilant");
    }

    public ActionForward deleteUnavailablePeriodOfVigilant(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        deletePeriod(request);
        VigilantGroupBean bean = new VigilantGroupBean();
        ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();
        bean.setExamCoordinator(coordinator);
        String gid = request.getParameter("gid");
        VigilantGroup group = (VigilantGroup) AbstractDomainObject.fromExternalId(gid);
        bean.setSelectedVigilantGroup(group);
        putUnavailablePeriodsOnRequest(request, group);
        request.setAttribute("bean", bean);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");

    }

    public ActionForward prepareAddPeriodToVigilant(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String gid = request.getParameter("gid");
        VigilantGroup group = (VigilantGroup) AbstractDomainObject.fromExternalId(gid);

        UnavailablePeriodBean bean = new UnavailablePeriodBean();
        Person person = getLoggedPerson(request);
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        ExamCoordinator coordinator = person.getExamCoordinatorForGivenExecutionYear(executionYear);
        bean.setCoordinator(coordinator);
        bean.setSelectedVigilantGroup(group);

        request.setAttribute("gid", gid);
        request.setAttribute("bean", bean);
        return mapping.findForward("prepareAddPeriodToVigilant");

    }

    public ActionForward addUnavailablePeriodToVigilant(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        UnavailablePeriodBean bean =
                (UnavailablePeriodBean) RenderUtils.getViewState("periodCreation").getMetaObject().getObject();

        try {

            CreateUnavailablePeriod.run(bean.getVigilantWrapper().getPerson(), bean.getBeginDate(), bean.getEndDate(),
                    bean.getJustification());
        } catch (DomainException e) {
            String gid = request.getParameter("gid");
            request.setAttribute("gid", gid);
            request.setAttribute("bean", bean);
            addActionMessage(request, e.getMessage());
            return mapping.findForward("prepareAddPeriodToVigilant");
        }

        VigilantGroupBean beanToPutOnRequest = new VigilantGroupBean();
        String gid = request.getParameter("gid");

        VigilantGroup group = (VigilantGroup) AbstractDomainObject.fromExternalId(gid);
        ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();
        beanToPutOnRequest.setExamCoordinator(coordinator);
        beanToPutOnRequest.setSelectedVigilantGroup(group);
        putUnavailablePeriodsOnRequest(request, group);
        request.setAttribute("bean", beanToPutOnRequest);

        return mapping.findForward("manageUnavailablePeriodsOfVigilants");
    }

    private void prepareForEdit(HttpServletRequest request) {
        String id = request.getParameter("oid");

        UnavailablePeriod unavailablePeriod = (UnavailablePeriod) AbstractDomainObject.fromExternalId(id);
        UnavailablePeriodBean bean = new UnavailablePeriodBean();
        bean.setUnavailablePeriod(unavailablePeriod);
        bean.setBeginDate(unavailablePeriod.getBeginDate());
        bean.setEndDate(unavailablePeriod.getEndDate());
        bean.setJustification(unavailablePeriod.getJustification());
        bean.setVigilantWrapper(unavailablePeriod.getPerson().getVigilantWrappers().get(0));

        request.setAttribute("bean", bean);
    }

    private void deletePeriod(HttpServletRequest request) throws FenixServiceException {
        String id = request.getParameter("oid");

        try {
            DeleteUnavailablePeriodByOID.run(id);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }
    }

    private void putUnavailablePeriodsOnRequest(HttpServletRequest request, VigilantGroup group) throws Exception {
        request.setAttribute("unavailablePeriods", group.getUnavailablePeriodsOfVigilantsInGroup());
    }

    private void applyChangesToUnavailablePeriod(HttpServletRequest request, UnavailablePeriod unavailablePeriod, DateTime begin,
            DateTime end, String justification) throws Exception {

        EditUnavailablePeriod.run(unavailablePeriod, begin, end, justification);
    }

    private void putRequestVigilantManagementCompliant(HttpServletRequest request, VigilantWrapper vigilantWrapper) {
        VigilantBean bean = new VigilantBean();
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        bean.setExecutionYear(executionYear);

        VigilantGroup group = getGroupInRequestOrFirstGroupFromVigilant(request, vigilantWrapper);
        bean.setSelectedVigilantGroup(group);
        List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
        List<VigilantWrapper> vigilantWrappersFromPerson = vigilantWrapper.getPerson().getVigilantWrappers();
        for (VigilantWrapper vigilantWrapperFromPerson : vigilantWrappersFromPerson) {
            groups.add(vigilantWrapperFromPerson.getVigilantGroup());
        }
        bean.setVigilantGroups(groups);
        bean.setShowUnavailables(true);
        request.setAttribute("bean", bean);
        request.setAttribute("vigilant", vigilantWrapper);
    }

    private VigilantGroup getGroupInRequestOrFirstGroupFromVigilant(HttpServletRequest request, VigilantWrapper vigilant) {
        String groupId = request.getParameter("gid");
        return (groupId == null) ? vigilant.getVigilantGroup() : (VigilantGroup) AbstractDomainObject.fromExternalId(groupId);
    }

}