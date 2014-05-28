/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.dataTransferObject.phd.YearMonth;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ExemptPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RequestPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.UploadGuidanceDocument;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdRegistrationConclusionBean;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

abstract public class CommonPhdIndividualProgramProcessDA extends PhdProcessDA {

    private static final int FIRST_YEAR_TO_SHOW_ARCHIVE_MESSAGES_FROM = 2002;
    private static final int NUMBER_OF_LAST_MESSAGES = 100;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final PhdIndividualProgramProcess process = getProcess(request);

        loadProcessAlertMessagesToNotify(request, process);

        return super.execute(mapping, actionForm, request, response);
    }

    private void loadProcessAlertMessagesToNotify(HttpServletRequest request, final PhdIndividualProgramProcess process) {
        if (process != null) {
            request.setAttribute("processAlertMessagesToNotify", process.getUnreadedAlertMessagesFor(getLoggedPerson(request)));
        }
    }

    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        SearchPhdIndividualProgramProcessBean searchBean = getOrCreateSearchBean(request);
        RenderUtils.invalidateViewState();

        return forwardToManageProcesses(mapping, request, searchBean);
    }

    private SearchPhdIndividualProgramProcessBean getOrCreateSearchBean(HttpServletRequest request) {
        SearchPhdIndividualProgramProcessBean searchBean =
                (SearchPhdIndividualProgramProcessBean) getObjectFromViewState("searchProcessBean");

        if (searchBean == null) {
            searchBean = initializeSearchBean(request);
        }
        return searchBean;
    }

    protected ActionForward forwardToManageProcesses(ActionMapping mapping, HttpServletRequest request,
            SearchPhdIndividualProgramProcessBean searchBean) {
        request.setAttribute("searchProcessBean", searchBean);
        request.setAttribute("candidacyCategory", getCandidacyCategory());
        request.setAttribute("seminarCategory", getSeminarCategory());
        request.setAttribute("thesisCategory", getThesisCategory());
        request.setAttribute("concludedThisYearContainer", getConcludedContainer());
        return mapping.findForward("manageProcesses");
    }

    abstract protected PhdInactivePredicateContainer getConcludedContainer();

    abstract protected List<PredicateContainer<?>> getThesisCategory();

    abstract protected List<PredicateContainer<?>> getSeminarCategory();

    abstract protected List<PredicateContainer<?>> getCandidacyCategory();

    abstract protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request);

    public ActionForward viewInactiveProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        SearchPhdIndividualProgramProcessBean searchBean = getOrCreateSearchBean(request);
        RenderUtils.invalidateViewState();

        request.setAttribute("searchProcessBean", searchBean);
        request.setAttribute("suspendedContainer", PhdInactivePredicateContainer.SUSPENDED);
        request.setAttribute("concludedContainer", PhdInactivePredicateContainer.CONCLUDED);
        request.setAttribute("abolishedContainer", PhdInactivePredicateContainer.ABOLISHED);
        return mapping.findForward("viewInactiveProcesses");
    }

    public ActionForward searchAllProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        SearchPhdIndividualProgramProcessBean searchBean = getOrCreateSearchBean(request);
        RenderUtils.invalidateViewState();

        List<PhdIndividualProgramProcess> processes;
        try {
            processes = PhdIndividualProgramProcess.search(searchBean.getPredicates());
        } catch (NumberFormatException ex) {
            addActionMessage("searchError", request, "error.invalidFormat");
            return forwardToManageProcesses(mapping, request, searchBean);
        }
        if (processes.isEmpty()) {
            addActionMessage("searchResults", request, "message.noResults");
            return forwardToManageProcesses(mapping, request, searchBean);
        }
        if (processes.size() == 1) {
            request.setAttribute("process", processes.iterator().next());
            loadProcessAlertMessagesToNotify(request, processes.iterator().next());
            Collection<PhdParticipant> guidingsList = processes.iterator().next().getGuidings();
            Collection<PhdParticipant> assistantGuidingsList = processes.iterator().next().getAssistantGuidings();
            request.setAttribute("guidingsList", guidingsList);
            request.setAttribute("assistantGuidingsList", assistantGuidingsList);
            return mapping.findForward("viewProcess");
        }
        request.setAttribute("searchProcessBean", searchBean);
        request.setAttribute("processes", processes);

        return mapping.findForward("searchResults");
    }

    @EntryPoint
    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();
        final PhdIndividualProgramProcess process = getProcess(request);
        if (process != null && process.hasRegistration()) {
            request.setAttribute("registrationConclusionBean", new PhdRegistrationConclusionBean(process.getRegistration()));
        }
        Collection<PhdParticipant> guidingsList = process.getGuidings();
        Collection<PhdParticipant> assistantGuidingsList = process.getAssistantGuidings();
        request.setAttribute("guidingsList", guidingsList);
        request.setAttribute("assistantGuidingsList", assistantGuidingsList);
        return forwardToViewProcess(mapping, request);
    }

    protected ActionForward forwardToViewProcess(ActionMapping mapping, HttpServletRequest request) {
        request.setAttribute("backMethod", getFromRequest(request, "backMethod"));
        return mapping.findForward("viewProcess");
    }

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
        return (PhdIndividualProgramProcess) super.getProcess(request);
    }

    protected DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        return getDomainObject(request, "degreeCurricularPlanID");
    }

    // Alerts Management
    public ActionForward viewAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        TreeSet<PhdAlertMessage> orderedMessages =
                new TreeSet<PhdAlertMessage>(Collections.reverseOrder(PhdAlertMessage.COMPARATOR_BY_WHEN_CREATED_AND_ID));
        orderedMessages.addAll(getLoggedPerson(request).getPhdAlertMessages());
        ArrayList<PhdAlertMessage> lastMessages = new ArrayList<PhdAlertMessage>();
        lastMessages.addAll(orderedMessages);

        request.setAttribute("unread", "false");
        request.setAttribute("alertMessages", lastMessages.subList(0, Math.min(lastMessages.size(), NUMBER_OF_LAST_MESSAGES)));
        request.setAttribute("tooManyMessages", (lastMessages.size() > NUMBER_OF_LAST_MESSAGES) ? "true" : "false");
        return mapping.findForward("viewAlertMessages");
    }

    public ActionForward viewUnreadAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        TreeSet<PhdAlertMessage> orderedMessages =
                new TreeSet<PhdAlertMessage>(Collections.reverseOrder(PhdAlertMessage.COMPARATOR_BY_WHEN_CREATED_AND_ID));
        orderedMessages.addAll(getLoggedPerson(request).getUnreadedPhdAlertMessages());

        request.setAttribute("unread", "true");
        request.setAttribute("alertMessages", orderedMessages);
        return mapping.findForward("viewAlertMessages");
    }

    public ActionForward viewAlertMessageArchive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        YearMonth yearMonthBean = getOrCreateBean(request);
        return forwardToAlertMessageArchive(mapping, request, yearMonthBean);
    }

    private ActionForward forwardToAlertMessageArchive(ActionMapping mapping, HttpServletRequest request, YearMonth yearMonthBean) {
        Integer year = yearMonthBean.getYear();
        if (year == null) {
            year = Integer.valueOf(ExecutionYear.readCurrentExecutionYear().getYear());
        }
        Month month = yearMonthBean.getMonth();

        TreeSet<PhdAlertMessage> orderedMessages =
                new TreeSet<PhdAlertMessage>(Collections.reverseOrder(PhdAlertMessage.COMPARATOR_BY_WHEN_CREATED_AND_ID));
        if (month == null) {
            for (PhdAlertMessage message : getLoggedPerson(request).getPhdAlertMessages()) {
                if (year == message.getWhenCreated().getYear()) {
                    orderedMessages.add(message);
                }
            }
        } else {
            for (PhdAlertMessage message : getLoggedPerson(request).getPhdAlertMessages()) {
                if ((year == message.getWhenCreated().getYear())
                        && (month.getNumberOfMonth() == message.getWhenCreated().getMonthOfYear())) {
                    orderedMessages.add(message);
                }
            }
        }

        request.setAttribute("yearMonthBean", yearMonthBean);
        request.setAttribute("alertMessages", orderedMessages);
        return mapping.findForward("viewAlertMessageArchive");
    }

    public YearMonth getOrCreateBean(HttpServletRequest request) {
        YearMonth yearMonthBean = getRenderedObject("yearMonthBean");
        RenderUtils.invalidateViewState();

        if (yearMonthBean == null) {
            yearMonthBean = createBeanFromRequest(request);
        }
        return yearMonthBean;
    }

    public YearMonth createBeanFromRequest(HttpServletRequest request) {
        String year = (String) getFromRequest(request, "year");
        String month = (String) getFromRequest(request, "month");
        YearMonth yearMonthBean;
        if (StringUtils.isEmpty(year)) {
            yearMonthBean = new YearMonth(Calendar.getInstance().get(Calendar.YEAR), null);
        } else if (StringUtils.isEmpty(month)) {
            yearMonthBean = new YearMonth(Integer.valueOf(year), null);
        } else {
            yearMonthBean = new YearMonth(Integer.valueOf(year), Integer.valueOf(month));
        }
        yearMonthBean.setFirstYear(FIRST_YEAR_TO_SHOW_ARCHIVE_MESSAGES_FROM);
        return yearMonthBean;
    }

    public ActionForward viewProcessAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        TreeSet<PhdAlertMessage> orderedMessages =
                new TreeSet<PhdAlertMessage>(Collections.reverseOrder(PhdAlertMessage.COMPARATOR_BY_WHEN_CREATED_AND_ID));
        orderedMessages.addAll(getProcess(request).getAlertMessagesForLoggedPerson());
        ArrayList<PhdAlertMessage> lastMessages = new ArrayList<PhdAlertMessage>();
        lastMessages.addAll(orderedMessages);

        request.setAttribute("unread", "false");
        request.setAttribute("alertMessages", lastMessages.subList(0, Math.min(lastMessages.size(), NUMBER_OF_LAST_MESSAGES)));
        request.setAttribute("tooManyMessages", (lastMessages.size() > NUMBER_OF_LAST_MESSAGES) ? "true" : "false");
        return mapping.findForward("viewProcessAlertMessages");
    }

    public ActionForward viewUnreadProcessAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        TreeSet<PhdAlertMessage> orderedMessages =
                new TreeSet<PhdAlertMessage>(Collections.reverseOrder(PhdAlertMessage.COMPARATOR_BY_WHEN_CREATED_AND_ID));
        orderedMessages.addAll(getProcess(request).getUnreadAlertMessagesForLoggedPerson());

        request.setAttribute("unread", "true");
        request.setAttribute("alertMessages", orderedMessages);
        return mapping.findForward("viewProcessAlertMessages");
    }

    public ActionForward viewProcessAlertMessageArchive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        YearMonth yearMonthBean = getOrCreateBean(request);
        return forwardToProcessAlertMessageArchive(mapping, request, yearMonthBean);
    }

    private ActionForward forwardToProcessAlertMessageArchive(ActionMapping mapping, HttpServletRequest request,
            YearMonth yearMonthBean) throws NumberFormatException {

        Integer year = yearMonthBean.getYear();
        if (year == null) {
            year = Integer.valueOf(ExecutionYear.readCurrentExecutionYear().getYear());
        }
        Month month = yearMonthBean.getMonth();

        TreeSet<PhdAlertMessage> orderedMessages =
                new TreeSet<PhdAlertMessage>(Collections.reverseOrder(PhdAlertMessage.COMPARATOR_BY_WHEN_CREATED_AND_ID));
        if (month == null) {
            for (PhdAlertMessage message : getProcess(request).getAlertMessagesForLoggedPerson()) {
                if (year == message.getWhenCreated().getYear()) {
                    orderedMessages.add(message);
                }
            }
        } else {
            for (PhdAlertMessage message : getProcess(request).getAlertMessagesForLoggedPerson()) {
                if ((year == message.getWhenCreated().getYear())
                        && (month.getNumberOfMonth() == message.getWhenCreated().getMonthOfYear())) {
                    orderedMessages.add(message);
                }
            }
        }

        request.setAttribute("yearMonthBean", yearMonthBean);
        request.setAttribute("alertMessages", orderedMessages);
        return mapping.findForward("viewProcessAlertMessageArchive");
    }

    public ActionForward readAlertMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PhdAlertMessage alertMessage = getAlertMessage(request);
        alertMessage.markAsReaded(getLoggedPerson(request));

        // back information
        String unread = (String) getFromRequest(request, "unread");
        String global = (String) getFromRequest(request, "global");
        String archive = (String) getFromRequest(request, "archive");
        String year = (String) getFromRequest(request, "year");
        String month = (String) getFromRequest(request, "month");
        request.setAttribute("unread", (unread != null) ? unread : "false");
        request.setAttribute("global", (global != null) ? global : "true");
        request.setAttribute("archive", (archive != null) ? archive : "false");
        request.setAttribute("year", (year != null) ? year : String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        request.setAttribute("month", (month != null) ? month : "");
        request.setAttribute("alertMessage", alertMessage);

        return mapping.findForward("viewAlertMessage");
    }

    public ActionForward markAlertMessageAsUnread(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        PhdAlertMessage alertMessage = getAlertMessage(request);
        alertMessage.markAsUnread();

        Person loggedPerson = AccessControl.getPerson();
        boolean global = Boolean.valueOf((String) getFromRequest(request, "global"));
        boolean unread = Boolean.valueOf((String) getFromRequest(request, "unread"));
        boolean archive = Boolean.valueOf((String) getFromRequest(request, "archive"));
        request.setAttribute("global", global);
        if (global) {
            request.setAttribute("alertMessagesToNotify", loggedPerson.getUnreadedPhdAlertMessages());
            if (unread) {
                return viewUnreadAlertMessages(mapping, form, request, response);
            }
            if (archive) {
                return forwardToAlertMessageArchive(mapping, request, createBeanFromRequest(request));
            }
            return viewAlertMessages(mapping, form, request, response);
        }
        request.setAttribute("processAlertMessagesToNotify",
                getProcess(request).getUnreadedAlertMessagesFor(getLoggedPerson(request)));
        if (unread) {
            return viewUnreadProcessAlertMessages(mapping, form, request, response);
        }
        if (archive) {
            return forwardToProcessAlertMessageArchive(mapping, request, createBeanFromRequest(request));
        }
        return viewProcessAlertMessages(mapping, form, request, response);
    }

    private PhdAlertMessage getAlertMessage(HttpServletRequest request) {
        return getDomainObject(request, "alertMessageId");
    }

    // End of Alerts Management

    // Request Public Presentation Seminar Comission

    public ActionForward prepareRequestPublicPresentationSeminarComission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        PhdIndividualProgramProcess process = getProcess(request);
        request.setAttribute("requestPublicPresentationSeminarComissionBean", new PublicPresentationSeminarProcessBean(process));

        return mapping.findForward("requestPublicPresentationSeminarComission");

    }

    public ActionForward prepareRequestPublicPresentationSeminarComissionPostback(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("requestPublicPresentationSeminarComissionBean",
                getRenderedObject("requestPublicPresentationSeminarComissionBean"));

        RenderUtils.invalidateViewState();
        return mapping.findForward("requestPublicPresentationSeminarComission");
    }

    public ActionForward prepareRequestPublicPresentationSeminarComissionInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("requestPublicPresentationSeminarComissionBean",
                getRenderedObject("requestPublicPresentationSeminarComissionBean"));

        return mapping.findForward("requestPublicPresentationSeminarComission");
    }

    public ActionForward requestPublicPresentationSeminarComission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final PublicPresentationSeminarProcessBean bean = getRenderedObject("requestPublicPresentationSeminarComissionBean");

        request.setAttribute("requestPublicPresentationSeminarComissionBean", bean);

        return executeActivity(RequestPublicPresentationSeminarComission.class, bean, request, mapping,
                "requestPublicPresentationSeminarComission", "viewProcess");
    }

    public ActionForward prepareExemptPublicPresentationSeminarComission(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("exemptPublicPresentationSeminarComission");
    }

    public ActionForward exemptPublicPresentationSeminarComission(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), ExemptPublicPresentationSeminarComission.class,
                    new PublicPresentationSeminarProcessBean());

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return prepareExemptPublicPresentationSeminarComission(mapping, actionForm, request, response);
        }

        return viewProcess(mapping, actionForm, request, response);
    }

    // End of Request Public Presentation Seminar Comission

    // View curriculum

    public ActionForward viewCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("curriculumFilter", new PhdCurriculumFilterOptions(getProcess(request).getRegistration()));
        return mapping.findForward("viewCurriculum");
    }

    public ActionForward changeViewCurriculumFilterOptions(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("curriculumFilter", getRenderedObject("curriculumFilter"));
        return mapping.findForward("viewCurriculum");
    }

    // End of view curriculum

    /* Phd guidance documents */
    public ActionForward manageGuidanceDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("manageGuidanceDocuments");
    }

    public ActionForward prepareUploadGuidanceDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramDocumentUploadBean bean = new PhdProgramDocumentUploadBean();

        request.setAttribute("documentBean", bean);
        return mapping.findForward("uploadGuidanceDocument");
    }

    public ActionForward uploadGuidanceDocumentInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramDocumentUploadBean bean = (PhdProgramDocumentUploadBean) getRenderedObject("documentBean");

        request.setAttribute("documentBean", bean);
        return mapping.findForward("uploadGuidanceDocument");
    }

    public ActionForward uploadGuidanceDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramDocumentUploadBean bean = (PhdProgramDocumentUploadBean) getRenderedObject("documentBean");

        ExecuteProcessActivity.run(getProcess(request), UploadGuidanceDocument.class, bean);
        return manageGuidanceDocuments(mapping, form, request, response);
    }

    public ActionForward downloadGuidanceDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        writeFile(response, getGuidanceDocumentsFilename(request), PhdDocumentsZip.ZIP_MIME_TYPE,
                createGuidanceDocumentsZip(request));
        return null;
    }

    protected String getGuidanceDocumentsFilename(HttpServletRequest request) {
        final PhdIndividualProgramProcess process = getProcess(request);
        return String.format("%s-%s.zip", process.getProcessNumber().replace("/", "-"),
                getMessageFromResource("label.phd.guidance.documents").replace(" ", "_"));
    }

    /* End of phd guidance documents */

}
