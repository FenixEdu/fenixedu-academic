package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniInfoNotUpdatedBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniMailSendToBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniSearchBean;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.ConclusionYearDegreesStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.NotUpdatedAlumniInfoForSpecificTimeGroup;
import net.sourceforge.fenixedu.domain.alumni.AlumniReportFile;
import net.sourceforge.fenixedu.domain.alumni.AlumniReportFileBean;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.EmailBean;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import org.json.JSONArray;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(path = "/alumni", module = "publicRelations")
@Forwards({ @Forward(name = "alumni.showAlumniStatistics", path = "/gep/alumni/alumniStatistics.jsp"),
        @Forward(name = "alumni.showAlumniDetails", path = "/gep/alumni/alumniDetails.jsp") })
public class AlumniInformationAction extends FenixDispatchAction {

    private static final String GABINETE_ESTUDOS_PLANEAMENTO = "Gabinete de Estudos e Planeamento";
    private final static String NOT_AVAILABLE = "n/a";

    public ActionForward showAlumniStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        Map<Long, Integer> registrationsByDay = new TreeMap<Long, Integer>();

        int totalAlumniCount = RootDomainObject.getInstance().getAlumnisSet().size();

        int newAlumniCount = 0;
        int registeredAlumniCount = 0;
        for (Alumni alumni : RootDomainObject.getInstance().getAlumnis()) {
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

        int jobCount = RootDomainObject.getInstance().getJobsSet().size();

        int formationCount = 0;
        for (Qualification q : RootDomainObject.getInstance().getQualifications()) {
            if (q.getClass().equals(Formation.class)) {
                formationCount++;
            }
        }

        request.setAttribute("chartData", createJsonArray(registrationsByDay));
        request.setAttribute("statistics1", Role.getRoleByRoleType(RoleType.ALUMNI).getAssociatedPersonsSet().size());
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
        JSONArray data = new JSONArray();
        for (Entry<Long, Integer> entry : registrationsByDay.entrySet()) {
            JSONArray dataEntry = new JSONArray();
            dataEntry.put(entry.getKey());
            dataEntry.put(entry.getValue());
            data.put(dataEntry);
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
        recipients.addAll(gepSender.getRecipients());
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

    public ActionForward exportGroupList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String recipientOID = (String) getFromRequest(request, "recipientOID");
        Recipient recipient = FenixFramework.getDomainObject(recipientOID);
        String fileName = recipient.getToName().replace(" ", "_");
        final Spreadsheet groupSheet;

        Group group = recipient.getMembers();
        ConclusionYearDegreesStudentsGroup yearDegreesStudentsGroup = null;
        NotUpdatedAlumniInfoForSpecificTimeGroup alumniInfoForSpecificTimeGroup = null;
        if (group instanceof ConclusionYearDegreesStudentsGroup) {
            fileName = fileName.split("_:")[0];
            groupSheet = new Spreadsheet(fileName);
            yearDegreesStudentsGroup = (ConclusionYearDegreesStudentsGroup) group;
            setYearDegreesStudentsSheet(recipient, groupSheet, yearDegreesStudentsGroup);
        } else {
            groupSheet = new Spreadsheet(fileName);
            alumniInfoForSpecificTimeGroup = (NotUpdatedAlumniInfoForSpecificTimeGroup) group;
            setAlumniInfoForSpecificTimeSheet(recipient, groupSheet);
        }

        groupSheet.setHeaders(new String[] { "NOME", "NUMERO_ALUNO", "CURSO", "INICIO", "CONCLUSAO", "EMAIL", "TELEMOVEL" });
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        final OutputStream outputStream = response.getOutputStream();
        groupSheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        response.flushBuffer();
        return null;
    }

    private void setYearDegreesStudentsSheet(Recipient recipient, final Spreadsheet groupSheet,
            ConclusionYearDegreesStudentsGroup yearDegreesStudentsGroup) {
        for (Person person : recipient.getMembers().getElements()) {
            Row row = groupSheet.addRow();
            row.setCell(person.getName());
            row.setCell(person.getStudent().getNumber());

            for (final Registration registration : person.getStudent().getRegistrationsSet()) {
                if (registration.isConcluded() && yearDegreesStudentsGroup.getDegrees().contains(registration.getDegree())) {
                    LocalDate conclusionDate = getConclusionDate(registration.getDegree(), registration);
                    if (conclusionDate != null
                            && (conclusionDate.getYear() == yearDegreesStudentsGroup.getRegistrationEnd().getEndCivilYear() || conclusionDate
                                    .getYear() == yearDegreesStudentsGroup.getRegistrationEnd().getBeginCivilYear())) {
                        row.setCell(registration.getDegree().getNameI18N().getContent());
                        row.setCell(registration.getStartDate().toString());
                        row.setCell(conclusionDate.toString());
                        break;
                    }
                }
            }
            row.setCell(person.getEmailForSendingEmails());
            row.setCell(person.getDefaultMobilePhoneNumber());
        }
    }

    private void setAlumniInfoForSpecificTimeSheet(Recipient recipient, final Spreadsheet groupSheet) {
        for (Person person : recipient.getMembers().getElements()) {
            Row row = groupSheet.addRow();
            row.setCell(person.getName());
            row.setCell(person.getStudent().getNumber());

            for (final Registration registration : person.getStudent().getRegistrationsSet()) {
                if (registration.isConcluded()) {
                    LocalDate conclusionDate = getConclusionDate(registration.getDegree(), registration);
                    if (conclusionDate != null) {
                        row.setCell(registration.getDegree().getNameI18N().getContent());
                        row.setCell(registration.getStartDate().toString());
                        row.setCell(conclusionDate.toString());
                        break;
                    }
                }
            }
            row.setCell(person.getEmailForSendingEmails());
            row.setCell(person.getDefaultMobilePhoneNumber());
        }
    }

    private LocalDate getConclusionDate(Degree degree, Registration registration) {
        for (StudentCurricularPlan scp : registration.getStudentCurricularPlansByDegree(degree)) {
            if (registration.isBolonha()) {
                if (scp.getLastConcludedCycleCurriculumGroup() != null) {
                    YearMonthDay conclusionDate =
                            registration.getConclusionDate(scp.getLastConcludedCycleCurriculumGroup().getCycleType());
                    if (conclusionDate != null) {
                        return conclusionDate.toLocalDate();
                    }
                }
                return null;
            } else {
                return registration.getConclusionDate() != null ? registration.getConclusionDate().toLocalDate() : null;
            }
        }
        return null;
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
