package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

public class UnavailablePeriodManagement extends FenixDispatchAction {

    private Vigilant getVigilantForCurrentYear(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        Person person = getLoggedPerson(request);
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        Vigilant vigilant = person.getVigilantForGivenExecutionYear(executionYear);
        return vigilant;
    }

    public ActionForward addUnavailablePeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Vigilant vigilant = getVigilantForCurrentYear(request);
        UnavailablePeriodBean bean = new UnavailablePeriodBean();
        bean.setVigilant(vigilant);

        request.setAttribute("bean", bean);
        return mapping.findForward("addUnavailablePeriod");

    }

    public ActionForward createUnavailablePeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnavailablePeriodBean periodBean = (UnavailablePeriodBean) RenderUtils.getViewState(
                "createUnavailablePeriod").getMetaObject().getObject();
        Vigilant vigilant = periodBean.getVigilant();

        try {
            Object[] args = { vigilant, periodBean.getBeginDate(), periodBean.getEndDate(),
                    periodBean.getJustification() };
            executeService(request, "CreateUnavailablePeriod", args);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), null);
        }

        putRequestVigilantManagementCompliant(request, vigilant);
        return mapping.findForward("addedUnavailablePeriod");

    }

    public ActionForward editUnavailablePeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        prepareForEdit(request);
        return mapping.findForward("editUnavailablePeriod");

    }

    public ActionForward deleteUnavailablePeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Vigilant vigilant = getVigilantForCurrentYear(request);

        deletePeriod(request);
        putRequestVigilantManagementCompliant(request, vigilant);

        return mapping.findForward("deleteUnavailablePeriod");
    }

    public ActionForward changeUnavailablePeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnavailablePeriodBean periodBean = (UnavailablePeriodBean) RenderUtils.getViewState()
                .getMetaObject().getObject();
        Vigilant vigilant = periodBean.getVigilant();
        applyChangesToUnavailablePeriod(request, periodBean.getIdInternal(), periodBean.getBeginDate(),
                periodBean.getEndDate(), periodBean.getJustification());

        putRequestVigilantManagementCompliant(request, vigilant);
        return mapping.findForward("addedUnavailablePeriod");

    }

    private void putRequestVigilantManagementCompliant(HttpServletRequest request, Vigilant vigilant) {
        VigilantBean bean = new VigilantBean();
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        bean.setExecutionYear(executionYear);

        VigilantGroup group = getGroupInRequestOrFirstGroupFromVigilant(request, vigilant);
        bean.setSelectedVigilantGroup(group);
        bean.setVigilantGroups(vigilant.getVigilantGroups());
        request.setAttribute("bean", bean);
        request.setAttribute("vigilant", vigilant);
    }

    private VigilantGroup getGroupInRequestOrFirstGroupFromVigilant(HttpServletRequest request,
            Vigilant vigilant) {
        String groupId = request.getParameter("gid");
        return (groupId == null) ? vigilant.getVigilantGroups().get(0)
                : (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, Integer
                        .valueOf(groupId));
    }

    public ActionForward changeUnavailablePeriodForVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnavailablePeriodBean periodBean = (UnavailablePeriodBean) RenderUtils.getViewState()
                .getMetaObject().getObject();
        applyChangesToUnavailablePeriod(request, periodBean.getIdInternal(), periodBean.getBeginDate(),
                periodBean.getEndDate(), periodBean.getJustification());

        putUnavailablePeriodsOnRequest(request);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");
    }

    public ActionForward manageUnavailablePeriodsOfVigilants(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        putUnavailablePeriodsOnRequest(request);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");

    }

    public ActionForward editUnavailablePeriodOfVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareForEdit(request);
        return mapping.findForward("editPeriodOfVigilant");
    }

    public ActionForward deleteUnavailablePeriodOfVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        deletePeriod(request);
        putUnavailablePeriodsOnRequest(request);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");

    }

    public ActionForward prepareAddPeriodToVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnavailablePeriodBean bean = new UnavailablePeriodBean();
        Person person = getLoggedPerson(request);
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        ExamCoordinator coordinator = person.getExamCoordinatorForGivenExecutionYear(executionYear);
        bean.setCoordinator(coordinator);
        request.setAttribute("bean", bean);
        return mapping.findForward("prepareAddPeriodToVigilant");

    }

    public ActionForward addUnavailablePeriodToVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnavailablePeriodBean bean = (UnavailablePeriodBean) RenderUtils.getViewState("periodCreation")
                .getMetaObject().getObject();

        Object[] args = { bean.getVigilant(), bean.getBeginDate(), bean.getEndDate(),
                bean.getJustification() };
        executeService(request, "CreateUnavailablePeriod", args);
        putUnavailablePeriodsOnRequest(request);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");
    }

    private void prepareForEdit(HttpServletRequest request) {
        String id = request.getParameter("oid");
        int idInternal = Integer.valueOf(id);

        UnavailablePeriod unavailablePeriod = (UnavailablePeriod) RootDomainObject
                .readDomainObjectByOID(UnavailablePeriod.class, idInternal);
        UnavailablePeriodBean bean = new UnavailablePeriodBean();
        bean.setIdInternal(idInternal);
        bean.setBeginDate(unavailablePeriod.getBeginDate());
        bean.setEndDate(unavailablePeriod.getEndDate());
        bean.setJustification(unavailablePeriod.getJustification());
        bean.setVigilant(unavailablePeriod.getVigilant());

        request.setAttribute("bean", bean);
    }

    private void deletePeriod(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        String id = request.getParameter("oid");
        Integer idInternal = Integer.valueOf(id);

        try {
            Object[] args = { idInternal };
            executeService(request, "DeleteUnavailablePeriodByOID", args);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), null);
        }
    }

    private void putUnavailablePeriodsOnRequest(HttpServletRequest request) throws Exception {
        Person person = getLoggedPerson(request);
        ExamCoordinator coordinator = person.getExamCoordinatorForGivenExecutionYear(ExecutionYear
                .readCurrentExecutionYear());

        if (coordinator != null) {
            List<UnavailablePeriod> unavailablePeriods = coordinator
                    .getUnavailablePeriodsThatCanManage();
            request.setAttribute("unavailablePeriods", unavailablePeriods);
        } else {
            request.setAttribute("unavailablePeriods", new ArrayList<UnavailablePeriod>());
        }

    }

    private void applyChangesToUnavailablePeriod(HttpServletRequest request, Integer idInternal,
            DateTime begin, DateTime end, String justification) throws Exception {
        try {
            Object[] args = { idInternal, begin, end, justification };
            executeService(request, "EditUnavailablePeriod", args);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), null);
        }
    }

}