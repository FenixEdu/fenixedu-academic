/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.publicRelationsOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Alumni;
import org.fenixedu.academic.domain.Formation;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.domain.QueueJob;
import org.fenixedu.academic.domain.alumni.AlumniReportFile;
import org.fenixedu.academic.domain.alumni.AlumniReportFileBean;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.email.EmailBean;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.Sender;
import org.fenixedu.academic.dto.alumni.AlumniInfoNotUpdatedBean;
import org.fenixedu.academic.dto.alumni.AlumniMailSendToBean;
import org.fenixedu.academic.dto.alumni.AlumniSearchBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.publicRelationsOffice.PublicRelationsApplication.PublicRelationsAlumniApp;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

@StrutsFunctionality(app = PublicRelationsAlumniApp.class, path = "search", titleKey = "link.search.alumni")
@Mapping(path = "/alumni", module = "publicRelations")
@Forwards({ @Forward(name = "alumni.showAlumniStatistics", path = "/gep/alumni/alumniStatistics.jsp"),
        @Forward(name = "alumni.showAlumniDetails", path = "/gep/alumni/alumniDetails.jsp") })
public class AlumniInformationAction extends FenixDispatchAction {

    private static final String GABINETE_ESTUDOS_PLANEAMENTO = "Gabinete de Estudos e Planeamento";
    private final static String NOT_AVAILABLE = "n/a";

    @StrutsFunctionality(app = PublicRelationsAlumniApp.class, path = "statistics", titleKey = "title.statistics")
    @Mapping(path = "/alumniStatistics", module = "publicRelations")
    public static class AlumniStatisticsAction extends AlumniInformationAction {
        @Override
        @EntryPoint
        public ActionForward showAlumniStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
                HttpServletResponse response) {
            return super.showAlumniStatistics(mapping, actionForm, request, response);
        }
    }

    public ActionForward showAlumniStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        Map<Long, Integer> registrationsByDay = new TreeMap<Long, Integer>();

        int totalAlumniCount = Bennu.getInstance().getAlumnisSet().size();

        int newAlumniCount = 0;
        int registeredAlumniCount = 0;
        for (Alumni alumni : Bennu.getInstance().getAlumnisSet()) {
            if (alumni.hasStartedPublicRegistry()) {
                newAlumniCount++;
            }
            if (alumni.hasFinishedPublicRegistry()) {
                registeredAlumniCount++;
            }
            DateTime whenRegistered = alumni.getRegisteredWhen();
            if (whenRegistered != null) {
                // long time =
                // whenRegistered.toLocalDate().toDateTimeAtStartOfDay().getMillis();
                long time = whenRegistered.toDateMidnight().getMillis();
                Integer count = registrationsByDay.get(time);
                registrationsByDay.put(time, count == null ? 1 : count + 1);
            }
        }

        int jobCount = Bennu.getInstance().getJobsSet().size();

        int formationCount = 0;
        for (Qualification q : Bennu.getInstance().getQualificationsSet()) {
            if (q.getClass().equals(Formation.class)) {
                formationCount++;
            }
        }

        request.setAttribute("chartData", createJsonArray(registrationsByDay));
        request.setAttribute("statistics1", RoleType.ALUMNI.actualGroup().getMembers().size());
        request.setAttribute("statistics2", totalAlumniCount);
        request.setAttribute("statistics3", newAlumniCount);
        request.setAttribute("statistics4", registeredAlumniCount);
        request.setAttribute("statistics5", jobCount);
        request.setAttribute("statistics6", formationCount);

        request.setAttribute("doneJobs", AlumniReportFile.readDoneJobs());
        request.setAttribute("undoneJobs", AlumniReportFile.readUndoneJobs());
        request.setAttribute("canRequestReport", AlumniReportFile.canRequestReport());

        return mapping.findForward("alumni.showAlumniStatistics");
    }

    private String createJsonArray(Map<Long, Integer> registrationsByDay) {
        JsonArray data = new JsonArray();
        for (Entry<Long, Integer> entry : registrationsByDay.entrySet()) {
            JsonArray dataEntry = new JsonArray();
            dataEntry.add(new JsonPrimitive(entry.getKey()));
            dataEntry.add(new JsonPrimitive(entry.getValue()));
            data.add(dataEntry);
        }
        return data.toString();
    }

    public ActionForward prepareAddRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("createRecipient", new AlumniMailSendToBean());
        request.setAttribute("notUpdatedInfoRecipient", new AlumniInfoNotUpdatedBean());
        return mapping.findForward("addRecipients");
    }

    public ActionForward prepareRemoveRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        EmailBean emailBean = new EmailBean();
        final Set<Sender> availableSenders = Sender.getAvailableSenders();
        for (Sender sender : availableSenders) {
            if (sender.getFromName().equals(GABINETE_ESTUDOS_PLANEAMENTO)) {
                emailBean.setSender(sender);
                break;
            }
        }
        request.setAttribute("emailBean", emailBean);
        return mapping.findForward("removeRecipients");
    }

    public ActionForward manageRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Set<Sender> availableSenders = Sender.getAvailableSenders();
        Sender gepSender = getGEPSender(availableSenders);
        List<Recipient> recipients = new ArrayList<Recipient>();
        recipients.addAll(gepSender.getRecipientsSet());
        Collections.sort(recipients, new BeanComparator("toName"));
        Collections.reverse(recipients);
        request.setAttribute("recipients", recipients);
        return mapping.findForward("manageRecipients");
    }

    private Sender getGEPSender(final Set<Sender> availableSenders) {
        Sender gepSender = null;
        for (Sender sender : availableSenders) {
            if (sender.getFromName().equals(GABINETE_ESTUDOS_PLANEAMENTO)) {
                gepSender = sender;
                break;
            }
        }
        return gepSender;
    }

    public ActionForward selectDegreeType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("notUpdatedInfoRecipient", new AlumniInfoNotUpdatedBean());
        request.setAttribute("createRecipient", getRenderedObject("createRecipient"));

        RenderUtils.invalidateViewState();
        return mapping.findForward("addRecipients");
    }

    public ActionForward addRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AlumniMailSendToBean alumniMailSendToBean = getRenderedObject("createRecipient");
        Sender gepSender = getGEPSender(Sender.getAvailableSenders());
        alumniMailSendToBean.createRecipientGroup(gepSender);

        return manageRecipients(mapping, actionForm, request, response);
    }

    public ActionForward addNotUpdatedInfoRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AlumniInfoNotUpdatedBean alumniInfoNotUpdatedBean = getRenderedObject("notUpdatedInfoRecipient");
        if (!alumniInfoNotUpdatedBean.getFormationInfo() && !alumniInfoNotUpdatedBean.getProfessionalInfo()
                && !alumniInfoNotUpdatedBean.getPersonalDataInfo()) {
            RenderUtils.invalidateViewState();
            addActionMessage(request, "label.alumni.choose.formationOrProfessionalOrPersonal");
            request.setAttribute("notUpdatedInfoRecipient", alumniInfoNotUpdatedBean);
            request.setAttribute("createRecipient", new AlumniMailSendToBean());
            return mapping.findForward("addRecipients");
        }
        Sender gepSender = getGEPSender(Sender.getAvailableSenders());
        alumniInfoNotUpdatedBean.createRecipientGroup(gepSender);

        return manageRecipients(mapping, actionForm, request, response);
    }

    public ActionForward removeRecipients(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        EmailBean emailBean = getRenderedObject("emailBean");
        emailBean.setRecipients(null);

        return manageRecipients(mapping, actionForm, request, response);
    }

    public ActionForward generateRegisteredAlumniPartialReport(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        AlumniReportFileBean.launchJob(false, true);
        return showAlumniStatistics(mapping, actionForm, request, response);
    }

    public ActionForward generateRegisteredAlumniFullReport(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        AlumniReportFileBean.launchJob(true, true);
        return showAlumniStatistics(mapping, actionForm, request, response);
    }

    public ActionForward generateAllAlumni(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        AlumniReportFileBean.launchJob(true, false);
        return showAlumniStatistics(mapping, actionForm, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        QueueJob job = getDomainObject(request, "jobId");
        job.cancel();

        return showAlumniStatistics(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward searchAlumni(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniSearchBean searchBean;
        final IViewState viewState = RenderUtils.getViewState("searchAlumniBean");
        if (viewState != null) {
            searchBean = (AlumniSearchBean) viewState.getMetaObject().getObject();
        } else {
            searchBean = new AlumniSearchBean();
        }

        List<Registration> registrations = Alumni.readRegistrations(searchBean);
        searchBean.setAlumni(new ArrayList<Registration>(registrations));

        java.util.List<AlumniSearchResultItemBean> alumniSearchResultItems =
                new java.util.ArrayList<AlumniSearchResultItemBean>();

        for (Registration registration : registrations) {
            alumniSearchResultItems.add(new AlumniSearchResultItemBean(registration));
        }

        RenderUtils.invalidateViewState();
        request.setAttribute("searchAlumniBean", searchBean);
        request.setAttribute("alumniResultItems", alumniSearchResultItems);
        return mapping.findForward("alumni.showAlumniDetails");
    }

    public ActionForward searchAlumniError(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("searchAlumniBean", getFromRequest(request, "searchAlumniBean"));
        return mapping.findForward("alumni.showAlumniDetails");
    }

    public static class AlumniSearchResultItemBean implements java.io.Serializable {
        private final Registration registration;

        public AlumniSearchResultItemBean(final Registration registration) {
            this.registration = registration;
        }

        public String getName() {
            return this.registration.getPerson().getName();
        }

        public String getDegree() {
            return registration.getDegree().getPresentationName();
        }

        public String getStartYear() {
            return registration.getStartExecutionYear().getYear();
        }

        public YearMonthDay getConclusionDateForBolonha() {
            return this.registration.getConclusionDateForBolonha();
        }

        public Boolean getActiveAlumni() {
            return this.registration.getStudent().getActiveAlumni();
        }

        public String getEmail() {
            if (registration.getPerson().getDefaultEmailAddress() == null) {
                return NOT_AVAILABLE;
            }

            return registration.getPerson().getDefaultEmailAddress().getValue();
        }

        public String getMobilePhone() {
            if (registration.getPerson().getDefaultMobilePhone() == null) {
                return NOT_AVAILABLE;
            }

            return registration.getPerson().getDefaultMobilePhone().getNumber();
        }
    }
}
