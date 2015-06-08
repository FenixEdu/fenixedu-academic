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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.thesis;

import java.io.Serializable;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.domain.thesis.ThesisFile;
import org.fenixedu.academic.domain.thesis.ThesisParticipationType;
import org.fenixedu.academic.domain.thesis.ThesisVisibilityType;
import org.fenixedu.academic.report.thesis.StudentThesisIdentificationDocument;
import org.fenixedu.academic.report.thesis.ThesisJuryReportDocument;
import org.fenixedu.academic.service.services.scientificCouncil.thesis.ApproveThesisDiscussion;
import org.fenixedu.academic.service.services.scientificCouncil.thesis.ApproveThesisProposal;
import org.fenixedu.academic.service.services.thesis.ChangeThesisPerson;
import org.fenixedu.academic.service.services.thesis.CreateThesisAbstractFile;
import org.fenixedu.academic.service.services.thesis.CreateThesisDissertationFile;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificDisserationsApp;
import org.fenixedu.academic.ui.struts.action.student.thesis.ThesisFileBean;
import org.fenixedu.academic.util.report.ReportsUtils;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.io.ByteStreams;

@StrutsFunctionality(app = ScientificDisserationsApp.class, path = "list-new", titleKey = "navigation.list.jury.proposals.new")
@Mapping(path = "/manageSecondCycleThesis", module = "scientificCouncil")
@Forwards({
        @Forward(name = "firstPage", path = "/scientificCouncil/thesis/firstPage.jsp"),
        @Forward(name = "showPersonThesisDetails", path = "/scientificCouncil/thesis/showPersonThesisDetails.jsp"),
        @Forward(name = "showThesisDetails", path = "/scientificCouncil/thesis/showThesisDetails.jsp"),
        @Forward(name = "editThesisEvaluationParticipant", path = "/scientificCouncil/thesis/editThesisEvaluationParticipant.jsp"),
        @Forward(name = "editThesisDetails", path = "/scientificCouncil/thesis/editThesisDetails.jsp"),
        @Forward(name = "addJuryMember", path = "/scientificCouncil/thesis/addJuryMember.jsp"),
        @Forward(name = "addExternalOrientationMember", path = "/scientificCouncil/thesis/addExternalOrientationMember.jsp"),
        @Forward(name = "addOrientationMember", path = "/scientificCouncil/thesis/addOrientationMember.jsp") })
public class ManageSecondCycleThesisDA extends FenixDispatchAction {

    public abstract static class EvaluationMemberBean implements Serializable {
        ThesisParticipationType thesisParticipationType;

        public ThesisParticipationType getThesisParticipationType() {
            return thesisParticipationType;
        }

        public void setThesisParticipationType(ThesisParticipationType thesisParticipationType) {
            this.thesisParticipationType = thesisParticipationType;
        }

        public abstract void addMember(final Thesis thesis);
    }

    public static class InternalEvaluationMemberBean extends EvaluationMemberBean {
        Person person;
        Unit unit;

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public Unit getUnit() {
            return unit;
        }

        public void setUnit(Unit unit) {
            this.unit = unit;
        }

        @Override
        public void addMember(final Thesis thesis) {
            ChangeThesisPerson.add(thesis, thesisParticipationType, person);
        }
    }

    public static class ExternalEvaluationMemberBean extends EvaluationMemberBean {
        String personName;
        String personEmail;

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public String getPersonEmail() {
            return personEmail;
        }

        public void setPersonEmail(String personEmail) {
            this.personEmail = personEmail;
        }

        @Override
        public void addMember(final Thesis thesis) {
            ChangeThesisPerson.addExternal(thesis, thesisParticipationType, getPersonName(), getPersonEmail());
        }
    }

    public static class RejectionCommentBean implements Serializable {
        String comment;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    @EntryPoint
    public ActionForward firstPage(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        return firstPage(new ManageSecondCycleThesisSearchBean(), mapping, request, response);
    }

    public ActionForward firstPage(final ManageSecondCycleThesisSearchBean filterSearchForm, final ActionMapping mapping,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final SortedSet<Enrolment> enrolments = filterSearchForm.findEnrolments();
        request.setAttribute("enrolments", enrolments);

        request.setAttribute("manageSecondCycleThesisSearchBean", filterSearchForm);
        return mapping.findForward("firstPage");
    }

    public ActionForward filterSearch(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final ManageSecondCycleThesisSearchBean filterSearchForm = getRenderedObject("filterSearchForm");
        return firstPage(filterSearchForm, mapping, request, response);
    }

    public ActionForward searchPerson(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final ManageSecondCycleThesisSearchBean searchPersonForm = getRenderedObject("searchPersonForm");

        final SortedSet<Person> people = searchPersonForm.findPersonBySearchString();
        if (people.size() == 1) {
            final Person person = people.first();
            return showPersonThesisDetails(mapping, request, person);
        }
        request.setAttribute("people", people);

        return firstPage(searchPersonForm, mapping, request, response);
    }

    public ActionForward showPersonThesisDetails(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personOid");
        return showPersonThesisDetails(mapping, request, person);
    }

    public ActionForward showPersonThesisDetails(final ActionMapping mapping, final HttpServletRequest request,
            final Person person) throws Exception {
        request.setAttribute("person", person);
        return mapping.findForward("showPersonThesisDetails");
    }

    public ActionForward showThesisDetails(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward rejectThesis(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        final RejectionCommentBean rejectionCommentBean = getRenderedObject();
        try {
            thesis.rejectProposal(rejectionCommentBean.getComment());
            addActionMessage("success", request, "message.thesis.reject.success");
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }
        RenderUtils.invalidateViewState();
        return showThesisDetails(mapping, request, thesis);
    }

    private ActionForward showThesisDetails(final ActionMapping mapping, final HttpServletRequest request, final Thesis thesis)
            throws Exception {
        if (!thesis.areThesisFilesReadable()) {
            final ThesisFile thesisFile = thesis.getDissertation();
            final ThesisFileBean thesisDissertationFileBean = new ThesisFileBean(thesis);
            if (thesisFile != null) {
                thesisDissertationFileBean.setTitle(thesisFile.getTitle());
                thesisDissertationFileBean.setSubTitle(thesisFile.getSubTitle());
                thesisDissertationFileBean.setLanguage(thesisFile.getLanguage());
            }
            request.setAttribute("thesisDissertationFileBean", thesisDissertationFileBean);

            final ThesisFileBean thesisExtendendAbstractFileBean = new ThesisFileBean(thesis);
            request.setAttribute("thesisExtendendAbstractFileBean", thesisExtendendAbstractFileBean);
        }
        request.setAttribute("rejectionCommentBean", new RejectionCommentBean());
        request.setAttribute("thesis", thesis);
        return mapping.findForward("showThesisDetails");
    }

    public ActionForward editThesisEvaluationParticipant(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final ThesisEvaluationParticipant thesisEvaluationParticipant =
                getDomainObject(request, "thesisEvaluationParticipantOid");
        request.setAttribute("thesisEvaluationParticipant", thesisEvaluationParticipant);
        return mapping.findForward("editThesisEvaluationParticipant");
    }

    public ActionForward removeThesisEvaluationParticipant(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final ThesisEvaluationParticipant thesisEvaluationParticipant =
                getDomainObject(request, "thesisEvaluationParticipantOid");
        final Thesis thesis = thesisEvaluationParticipant.getThesis();

        ChangeThesisPerson.remove(thesisEvaluationParticipant);

        return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward editThesisDetails(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        request.setAttribute("thesis", thesis);
        return mapping.findForward("editThesisDetails");
    }

    public ActionForward changeThesisFilesVisibility(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        thesis.swapFilesVisibility();
        return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward prepareAddJuryMember(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        request.setAttribute("thesis", thesis);
        EvaluationMemberBean evaluationMemberBean = getRenderedObject();
        if (evaluationMemberBean == null) {
            evaluationMemberBean =
                    request.getParameter("external") == null ? new InternalEvaluationMemberBean() : new ExternalEvaluationMemberBean();
        }
        request.setAttribute("evaluationMemberBean", evaluationMemberBean);
        return mapping.findForward("addJuryMember");
    }

    public ActionForward prepareAddOrientationMember(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        request.setAttribute("thesis", thesis);
        EvaluationMemberBean evaluationMemberBean = getRenderedObject();
        if (evaluationMemberBean == null) {
            evaluationMemberBean =
                    request.getParameter("external") == null ? new InternalEvaluationMemberBean() : new ExternalEvaluationMemberBean();
        }
        request.setAttribute("evaluationMemberBean", evaluationMemberBean);
        return mapping.findForward("addOrientationMember");
    }

    public ActionForward prepareAddExternalOrientationMember(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        request.setAttribute("thesis", thesis);
        EvaluationMemberBean evaluationMemberBean = getRenderedObject();
        if (evaluationMemberBean == null) {
            evaluationMemberBean = new ExternalEvaluationMemberBean();
        }
        request.setAttribute("evaluationMemberBean", evaluationMemberBean);
        return mapping.findForward("addExternalOrientationMember");
    }

    public ActionForward addEvaluationMember(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        final EvaluationMemberBean evaluationMemberBean = getRenderedObject();
        evaluationMemberBean.addMember(thesis);
        return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward makeDocumentUnavailable(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        FenixFramework.atomic(() -> thesis.setVisibility(null));
        return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward makeDocumentAvailable(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        FenixFramework.atomic(() -> thesis.setVisibility(ThesisVisibilityType.INTRANET));
        return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward substituteDissertation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return substituteDocument(mapping, request, true);
    }

    public ActionForward substituteExtendedAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return substituteDocument(mapping, request, false);
    }

    public ActionForward substituteDocument(final ActionMapping mapping, final HttpServletRequest request,
            final boolean dissertationFile) throws Exception {

        final Thesis thesis = getDomainObject(request, "thesisOid");
        ThesisFileBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();

        if (thesis.getVisibility() == null) {
            atomic(() -> thesis.setVisibility(ThesisVisibilityType.INTRANET));
        }

        if (bean != null && bean.getFile() != null) {
            if (thesis.getTitle() != null) {
                bean.setTitle(thesis.getTitle().toString());
            } else {
                throw new DomainException("thesis.files.dissertation.title.required");
            }
            byte[] bytes = ByteStreams.toByteArray(bean.getFile());
            if (dissertationFile) {
                CreateThesisDissertationFile.runCreateThesisDissertationFile(thesis, bytes, bean.getSimpleFileName(),
                        bean.getTitle(), bean.getSubTitle(), bean.getLanguage());
            } else {
                CreateThesisAbstractFile.runCreateThesisAbstractFile(thesis, bytes, bean.getSimpleFileName(), bean.getTitle(),
                        bean.getSubTitle(), bean.getLanguage());
            }
        }

        return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward approveThesis(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        try {
            ApproveThesisDiscussion.runApproveThesisDiscussion(thesis);
            addActionMessage("mail", request, "thesis.evaluated.mail.sent");
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }
        return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward approveProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        try {
            ApproveThesisProposal.runApproveThesisProposal(thesis);
            addActionMessage("mail", request, "thesis.approved.mail.sent");
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }
        return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward downloadIdentificationSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");

        try {
            StudentThesisIdentificationDocument document = new StudentThesisIdentificationDocument(thesis);
            byte[] data = ReportsUtils.generateReport(document).getData();

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

            response.getOutputStream().write(data);

            return null;
        } catch (final Exception e) {
            addActionMessage("error", request, "student.thesis.generate.identification.failed");
            return showThesisDetails(mapping, request, thesis);
        }
    }

    public ActionForward downloadJuryReportSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");

        try {
            ThesisJuryReportDocument document = new ThesisJuryReportDocument(thesis);
            byte[] data = ReportsUtils.generateReport(document).getData();

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

            response.getOutputStream().write(data);

            return null;
        } catch (final Exception e) {
            addActionMessage("error", request, "student.thesis.generate.juryreport.failed");
            return showThesisDetails(mapping, request, thesis);
        }
    }

}
