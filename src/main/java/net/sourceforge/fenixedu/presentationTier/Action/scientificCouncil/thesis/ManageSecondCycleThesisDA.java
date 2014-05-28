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
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.Serializable;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis.ApproveThesisDiscussion;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis.ApproveThesisProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.ChangeThesisPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.CreateThesisAbstractFile;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.CreateThesisDissertationFile;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.MakeThesisDocumentsAvailable;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.MakeThesisDocumentsUnavailable;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication.ScientificDisserationsApp;
import net.sourceforge.fenixedu.presentationTier.Action.student.thesis.ThesisFileBean;
import net.sourceforge.fenixedu.presentationTier.docs.thesis.StudentThesisIdentificationDocument;
import net.sourceforge.fenixedu.presentationTier.docs.thesis.ThesisJuryReportDocument;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
        String unitName;

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        @Override
        public void addMember(final Thesis thesis) {
            // TODO Auto-generated method stub

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

    private ActionForward showThesisDetails(final ActionMapping mapping, final HttpServletRequest request, final Thesis thesis)
            throws Exception {
        if (!thesis.areThesisFilesReadable()) {
            final ThesisFile thesisFile = thesis.getDissertation();
            if (thesisFile != null) {
                final ThesisFileBean thesisDissertationFileBean = new ThesisFileBean();
                thesisDissertationFileBean.setTitle(thesisFile.getTitle());
                thesisDissertationFileBean.setSubTitle(thesisFile.getSubTitle());
                thesisDissertationFileBean.setLanguage(thesisFile.getLanguage());
                request.setAttribute("thesisDissertationFileBean", thesisDissertationFileBean);

                final ThesisFileBean thesisExtendendAbstractFileBean = new ThesisFileBean();
                request.setAttribute("thesisExtendendAbstractFileBean", thesisExtendendAbstractFileBean);
            }
        }
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
        MakeThesisDocumentsUnavailable.run(thesis);
        return showThesisDetails(mapping, request, thesis);
    }

    public ActionForward makeDocumentAvailable(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");
        MakeThesisDocumentsAvailable.run(thesis);
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

        if (bean != null && bean.getFile() != null) {
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
            byte[] data = ReportsUtils.exportToProcessedPdfAsByteArray(document);

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

            response.getOutputStream().write(data);

            return null;
        } catch (final JRException e) {
            addActionMessage("error", request, "student.thesis.generate.identification.failed");
            return showThesisDetails(mapping, request, thesis);
        }
    }

    public ActionForward downloadJuryReportSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Thesis thesis = getDomainObject(request, "thesisOid");

        try {
            ThesisJuryReportDocument document = new ThesisJuryReportDocument(thesis);
            byte[] data = ReportsUtils.exportToProcessedPdfAsByteArray(document);

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

            response.getOutputStream().write(data);

            return null;
        } catch (final JRException e) {
            addActionMessage("error", request, "student.thesis.generate.juryreport.failed");
            return showThesisDetails(mapping, request, thesis);
        }
    }

}
