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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.inquiries;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.inquiries.RegentInquiryTemplate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalControlApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = PedagogicalControlApp.class, path = "view-quc-regents-status",
        titleKey = "title.inquiries.regents.status", bundle = "InquiriesResources")
@Mapping(path = "/qucRegentsStatus", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewQucRegentsState", path = "/pedagogicalCouncil/inquiries/viewQucRegentsStatus.jsp") })
public class ViewQucRegentsStatus extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final RegentInquiryTemplate regentInquiryTemplate =
                RegentInquiryTemplate.getTemplateByExecutionPeriod(ExecutionSemester.readActualExecutionSemester()
                        .getPreviousExecutionPeriod());
        if (regentInquiryTemplate != null) {
            request.setAttribute("regentInquiryOID", regentInquiryTemplate.getExternalId());
        }
        return mapping.findForward("viewQucRegentsState");
    }

    public ActionForward dowloadReport(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final RegentInquiryTemplate regentInquiryTemplate =
                FenixFramework.getDomainObject(getFromRequest(request, "regentInquiryOID").toString());

        final ExecutionSemester executionPeriod = regentInquiryTemplate.getExecutionPeriod();

        final Map<Person, RegentBean> regentsMap = new HashMap<Person, RegentBean>();
        for (Professorship professorship : Bennu.getInstance().getProfessorshipsSet()) {
            if (professorship.getExecutionCourse().getExecutionPeriod() == executionPeriod) {
                Person person = professorship.getPerson();
                boolean isToAnswer = person.hasToAnswerRegentInquiry(professorship);
                if (isToAnswer) {
                    boolean hasMandatoryCommentsToMake = professorship.hasMandatoryCommentsToMakeAsResponsible();
                    boolean inquiryToAnswer =
                            !professorship.hasInquiryRegentAnswer()
                                    || professorship.getInquiryRegentAnswer().hasRequiredQuestionsToAnswer(regentInquiryTemplate);
                    if (inquiryToAnswer || hasMandatoryCommentsToMake) {
                        RegentBean regentBean = regentsMap.get(person);
                        if (regentBean == null) {
                            Department department = null;
                            if (person.getEmployee() != null) {
                                department =
                                        person.getEmployee().getLastDepartmentWorkingPlace(
                                                regentInquiryTemplate.getExecutionPeriod().getBeginDateYearMonthDay(),
                                                regentInquiryTemplate.getExecutionPeriod().getEndDateYearMonthDay());
                            }
                            regentBean = new RegentBean(department, person, new ArrayList<ExecutionCourse>());
                            regentBean.setCommentsToMake(hasMandatoryCommentsToMake);
                            regentBean.setInquiryToAnswer(inquiryToAnswer);
                            regentsMap.put(person, regentBean);
                        } else {
                            regentBean.setCommentsToMake(hasMandatoryCommentsToMake || regentBean.isCommentsToMake());
                            regentBean.setInquiryToAnswer(inquiryToAnswer || regentBean.isInquiryToAnswer());
                        }
                        List<ExecutionCourse> executionCourseList = regentsMap.get(person).getCoursesToComment();
                        executionCourseList.add(professorship.getExecutionCourse());
                    }
                }
            }
        }

        Spreadsheet spreadsheet = createReport(regentsMap.values());
        StringBuilder filename = new StringBuilder("Regentes_em_falta_");
        filename.append(new DateTime().toString("yyyy_MM_dd_HH_mm"));

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

        OutputStream outputStream = response.getOutputStream();
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        outputStream.close();
        return null;
    }

    private Spreadsheet createReport(Collection<RegentBean> regentsList) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet("Regentes em falta");
        spreadsheet.setHeader("Departamento");
        spreadsheet.setHeader("Regente");
        spreadsheet.setHeader("Nº Mec");
        spreadsheet.setHeader("Telefone");
        spreadsheet.setHeader("Email");
        spreadsheet.setHeader("Comentários por fazer");
        spreadsheet.setHeader("Inquérito por responder");
        spreadsheet.setHeader("Disciplinas");

        for (RegentBean regentBean : regentsList) {
            Row row = spreadsheet.addRow();
            row.setCell(regentBean.getDepartment() != null ? regentBean.getDepartment().getName() : "-");
            row.setCell(regentBean.getRegent().getName());
            row.setCell(regentBean.getRegent().getTeacher() != null ? regentBean.getRegent().getTeacher().getTeacherId()
                    .toString() : regentBean.getRegent().getUsername());
            row.setCell(regentBean.getRegent().getDefaultMobilePhoneNumber());
            row.setCell(regentBean.getRegent().getDefaultEmailAddressValue());
            row.setCell(regentBean.isCommentsToMake() ? "Sim" : "Não");
            row.setCell(regentBean.isInquiryToAnswer() ? "Sim" : "Não");
            StringBuilder sb = new StringBuilder();
            for (ExecutionCourse executionCourse : regentBean.getOrderedCoursesToComment()) {
                sb.append(executionCourse.getName()).append(", ");
            }
            row.setCell(sb.toString());
        }

        return spreadsheet;
    }

    class RegentBean {
        private Department department;
        private Person regent;
        private List<ExecutionCourse> coursesToComment;
        private boolean commentsToMake;
        private boolean inquiryToAnswer;

        public RegentBean(Department department, Person regent, List<ExecutionCourse> coursesToComment) {
            setDepartment(department);
            setRegent(regent);
            setCoursesToComment(coursesToComment);
        }

        public List<ExecutionCourse> getOrderedCoursesToComment() {
            Collections.sort(getCoursesToComment(), new BeanComparator("name"));
            return getCoursesToComment();
        }

        public void setDepartment(Department department) {
            this.department = department;
        }

        public Department getDepartment() {
            return department;
        }

        public void setRegent(Person regent) {
            this.regent = regent;
        }

        public Person getRegent() {
            return regent;
        }

        public List<ExecutionCourse> getCoursesToComment() {
            return coursesToComment;
        }

        public void setCoursesToComment(List<ExecutionCourse> coursesToComment) {
            this.coursesToComment = coursesToComment;
        }

        public void setCommentsToMake(boolean commentsToMake) {
            this.commentsToMake = commentsToMake;
        }

        public boolean isCommentsToMake() {
            return commentsToMake;
        }

        public void setInquiryToAnswer(boolean inquiryToAnswer) {
            this.inquiryToAnswer = inquiryToAnswer;
        }

        public boolean isInquiryToAnswer() {
            return inquiryToAnswer;
        }
    }
}
