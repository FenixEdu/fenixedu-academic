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
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;
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
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = PedagogicalControlApp.class, path = "view-quc-teacher-status",
        titleKey = "title.inquiries.teachers.status", bundle = "InquiriesResources")
@Mapping(path = "/qucTeachersStatus", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewQucTeachersState", path = "/pedagogicalCouncil/inquiries/viewQucTeachersStatus.jsp",
        tileProperties = @Tile(title = "private.pedagogiccouncil.control.teachersstatusresponse")) })
public class ViewQucTeacherStatus extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final TeacherInquiryTemplate teacherInquiryTemplate =
                TeacherInquiryTemplate.getTemplateByExecutionPeriod(ExecutionSemester.readActualExecutionSemester()
                        .getPreviousExecutionPeriod());
        if (teacherInquiryTemplate != null) {
            request.setAttribute("teacherInquiryOID", teacherInquiryTemplate.getExternalId());
        }
        return mapping.findForward("viewQucTeachersState");
    }

    public ActionForward dowloadReport(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final TeacherInquiryTemplate teacherInquiryTemplate =
                FenixFramework.getDomainObject(getFromRequest(request, "teacherInquiryOID").toString());

        final ExecutionSemester executionPeriod = teacherInquiryTemplate.getExecutionPeriod();

        final Map<Person, TeacherBean> teachersMap = new HashMap<Person, TeacherBean>();
        for (Professorship professorship : Bennu.getInstance().getProfessorshipsSet()) {
            if (professorship.getExecutionCourse().getExecutionPeriod() == executionPeriod) {
                Person person = professorship.getPerson();
                boolean isToAnswer = person.hasToAnswerTeacherInquiry(professorship);
                if (isToAnswer) {
                    boolean hasMandatoryCommentsToMake = professorship.hasMandatoryCommentsToMake();
                    boolean inquiryToAnswer =
                            !professorship.hasInquiryTeacherAnswer()
                                    || professorship.getInquiryTeacherAnswer().hasRequiredQuestionsToAnswer(
                                            teacherInquiryTemplate);
                    if (inquiryToAnswer || hasMandatoryCommentsToMake) {
                        TeacherBean teacherBean = teachersMap.get(person);
                        if (teacherBean == null) {
                            Department department = null;
                            if (person.getEmployee() != null) {
                                department =
                                        person.getEmployee().getLastDepartmentWorkingPlace(
                                                teacherInquiryTemplate.getExecutionPeriod().getBeginDateYearMonthDay(),
                                                teacherInquiryTemplate.getExecutionPeriod().getEndDateYearMonthDay());
                            }
                            teacherBean = new TeacherBean(department, person, new ArrayList<ExecutionCourse>());
                            teacherBean.setCommentsToMake(hasMandatoryCommentsToMake);
                            teacherBean.setInquiryToAnswer(inquiryToAnswer);
                            teachersMap.put(person, teacherBean);
                        } else {
                            teacherBean.setCommentsToMake(hasMandatoryCommentsToMake || teacherBean.isCommentsToMake());
                            teacherBean.setInquiryToAnswer(inquiryToAnswer || teacherBean.isInquiryToAnswer());
                        }
                        List<ExecutionCourse> executionCourseList = teachersMap.get(person).getCoursesToComment();
                        executionCourseList.add(professorship.getExecutionCourse());
                    }
                }
            }
        }

        Spreadsheet spreadsheet = createReport(teachersMap.values());
        StringBuilder filename = new StringBuilder("Docentes_em_falta_");
        filename.append(new DateTime().toString("yyyy_MM_dd_HH_mm"));

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

        OutputStream outputStream = response.getOutputStream();
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        outputStream.close();
        return null;
    }

    private Spreadsheet createReport(Collection<TeacherBean> teachersList) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet("Docentes em falta");
        spreadsheet.setHeader("Departamento");
        spreadsheet.setHeader("Docente");
        spreadsheet.setHeader("Nº Mec");
        spreadsheet.setHeader("Telefone");
        spreadsheet.setHeader("Email");
        spreadsheet.setHeader("Comentários obrigatórios por fazer");
        spreadsheet.setHeader("Inquérito por responder");
        spreadsheet.setHeader("Disciplinas");
        spreadsheet.setHeader("Disciplinas sujeitas auditoria");

        for (TeacherBean teacherBean : teachersList) {
            Row row = spreadsheet.addRow();
            row.setCell(teacherBean.getDepartment() != null ? teacherBean.getDepartment().getName() : "-");
            row.setCell(teacherBean.getTeacher().getName());
            row.setCell(teacherBean.getTeacher().getUsername());
            row.setCell(teacherBean.getTeacher().getDefaultMobilePhoneNumber());
            row.setCell(teacherBean.getTeacher().getDefaultEmailAddressValue());
            row.setCell(teacherBean.isCommentsToMake() ? "Sim" : "Não");
            row.setCell(teacherBean.isInquiryToAnswer() ? "Sim" : "Não");
            StringBuilder sb = new StringBuilder();
            StringBuilder sbAudit = new StringBuilder();
            for (ExecutionCourse executionCourse : teacherBean.getOrderedCoursesToComment()) {
                if (executionCourse.canBeSubjectToQucAudit()) {
                    sbAudit.append(executionCourse.getName()).append(", ");
                } else {
                    sb.append(executionCourse.getName()).append(", ");
                }
            }
            row.setCell(sb.toString());
            row.setCell(sbAudit.toString());
        }

        return spreadsheet;
    }

    class TeacherBean {
        private Department department;
        private Person teacher;
        private List<ExecutionCourse> coursesToComment;
        private boolean commentsToMake;
        private boolean inquiryToAnswer;

        public TeacherBean(Department department, Person teacher, List<ExecutionCourse> coursesToComment) {
            setDepartment(department);
            setTeacher(teacher);
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

        public void setTeacher(Person teacher) {
            this.teacher = teacher;
        }

        public Person getTeacher() {
            return teacher;
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
