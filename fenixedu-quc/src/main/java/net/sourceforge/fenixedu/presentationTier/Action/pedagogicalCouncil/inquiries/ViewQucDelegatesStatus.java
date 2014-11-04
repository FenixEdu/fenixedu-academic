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
package org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.inquiries;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.inquiries.DelegateInquiryTemplate;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.student.Delegate;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.YearDelegate;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalControlApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = PedagogicalControlApp.class, path = "view-quc-delegates-status",
        titleKey = "title.inquiries.delegates.status", bundle = "InquiriesResources")
@Mapping(path = "/qucDelegatesStatus", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewQucDelegatesState", path = "/pedagogicalCouncil/inquiries/viewQucDelegatesStatus.jsp") })
public class ViewQucDelegatesStatus extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DelegateInquiryTemplate delegateInquiryTemplate =
                DelegateInquiryTemplate.getTemplateByExecutionPeriod(ExecutionSemester.readActualExecutionSemester()
                        .getPreviousExecutionPeriod());
        if (delegateInquiryTemplate != null) {
            request.setAttribute("delegateInquiryOID", delegateInquiryTemplate.getExternalId());
        }
        return mapping.findForward("viewQucDelegatesState");
    }

    public ActionForward dowloadReport(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DelegateInquiryTemplate delegateInquiryTemplate =
                FenixFramework.getDomainObject(getFromRequest(request, "delegateInquiryOID").toString());

        Map<Degree, List<DelegateBean>> delegatesMap = new HashMap<Degree, List<DelegateBean>>();
        final ExecutionSemester executionPeriod = delegateInquiryTemplate.getExecutionPeriod();
        final List<Degree> degreeList =
                Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE,
                        DegreeType.BOLONHA_MASTER_DEGREE);

        for (Degree degree : degreeList) {
            Map<Integer, YearDelegate> yearDelegateByYear = new HashMap<Integer, YearDelegate>();
            for (Student student : Delegate.getAllDelegatesByExecutionYearAndFunctionType(degree,
                    executionPeriod.getExecutionYear(), FunctionType.DELEGATE_OF_YEAR)) {
                YearDelegate yearDelegate = getYearDelegate(student, executionPeriod);
                YearDelegate yearDelegateMap = yearDelegateByYear.get(yearDelegate.getCurricularYear().getYear());
                if (yearDelegateMap == null) {
                    yearDelegateByYear.put(yearDelegate.getCurricularYear().getYear(), yearDelegate);
                } else {
                    if (yearDelegate.isAfter(yearDelegateMap)) {
                        yearDelegateByYear.put(yearDelegate.getCurricularYear().getYear(), yearDelegate);
                    }
                }
            }
            for (YearDelegate yearDelegate : yearDelegateByYear.values()) {
                DelegateBean delegateBean = getCoursesToComment(degree, yearDelegate, executionPeriod);
                if (delegateBean != null) {
                    List<DelegateBean> delegatesList = delegatesMap.get(degree);
                    if (delegatesList == null) {
                        delegatesList = new ArrayList<DelegateBean>();
                        delegatesMap.put(degree, delegatesList);
                    }
                    delegatesList.add(delegateBean);
                }
            }
        }

        Spreadsheet spreadsheet = createReport(delegatesMap);
        StringBuilder filename = new StringBuilder("Delegados_em_falta_");
        filename.append(new DateTime().toString("yyyy_MM_dd_HH_mm"));

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

        OutputStream outputStream = response.getOutputStream();
        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.flush();
        outputStream.close();
        return null;
    }

    private Spreadsheet createReport(Map<Degree, List<DelegateBean>> delegatesMap) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet("Delegados em falta");
        spreadsheet.setHeader("Curso");
        spreadsheet.setHeader("Delegado");
        spreadsheet.setHeader("Ano");
        spreadsheet.setHeader("Telefone");
        spreadsheet.setHeader("Email");
        spreadsheet.setHeader("Disciplinas");

        for (Degree degree : delegatesMap.keySet()) {
            for (DelegateBean delegateBean : delegatesMap.get(degree)) {
                Row row = spreadsheet.addRow();
                row.setCell(degree.getDegreeType().getLocalizedName() + " - " + degree.getNameI18N().toString());
                row.setCell(delegateBean.getYearDelegate().getPerson().getName());
                row.setCell(delegateBean.getYearDelegate().getCurricularYear().getYear());
                row.setCell(delegateBean.getYearDelegate().getPerson().getDefaultMobilePhoneNumber());
                row.setCell(delegateBean.getYearDelegate().getPerson().getDefaultEmailAddressValue());
                StringBuilder sb = new StringBuilder();
                for (ExecutionCourse executionCourse : delegateBean.getOrderedCoursesToComment()) {
                    sb.append(executionCourse.getName()).append(", ");
                }
                row.setCell(sb.toString());
            }
        }

        return spreadsheet;
    }

    public DelegateBean getCoursesToComment(final Degree degree, final YearDelegate yearDelegate,
            final ExecutionSemester executionSemester) {
        List<ExecutionCourse> coursesToComment = new ArrayList<ExecutionCourse>();
        final ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(yearDelegate.getRegistration()
                        .getStudentCurricularPlan(executionSemester).getDegreeCurricularPlan(),
                        executionSemester.getExecutionYear());
        for (ExecutionCourse executionCourse : DelegateInquiryTemplate.getExecutionCoursesToInquiries(yearDelegate, executionSemester, executionDegree)) {
            if (DelegateInquiryTemplate.hasMandatoryCommentsToMake(yearDelegate, executionCourse, executionDegree)) {
                coursesToComment.add(executionCourse);
            }
        }
        if (!coursesToComment.isEmpty()) {
            return new DelegateBean(degree, yearDelegate, coursesToComment);
        }
        return null;
    }

    private YearDelegate getYearDelegate(Student student, ExecutionSemester executionPeriod) {
        YearDelegate yearDelegate = null;
        for (Delegate delegate : Delegate.getDelegates(student)) {
            if (delegate instanceof YearDelegate) {
                if (delegate.isActiveForFirstExecutionYear(executionPeriod.getExecutionYear())) {
                    if (yearDelegate == null
                            || delegate.getDelegateFunction().getEndDate()
                                    .isAfter(yearDelegate.getDelegateFunction().getEndDate())) {
                        yearDelegate = (YearDelegate) delegate;
                    }
                }
            }
        }
        return yearDelegate;
    }

    class DelegateBean {
        private Degree degree;
        private YearDelegate yearDelegate;
        private List<ExecutionCourse> coursesToComment;

        public DelegateBean(Degree degree, YearDelegate yearDelegate, List<ExecutionCourse> coursesToComment) {
            setDegree(degree);
            setYearDelegate(yearDelegate);
            setCoursesToComment(coursesToComment);
        }

        public List<ExecutionCourse> getOrderedCoursesToComment() {
            Collections.sort(getCoursesToComment(), new BeanComparator("name"));
            return getCoursesToComment();
        }

        public Degree getDegree() {
            return degree;
        }

        public void setDegree(Degree degree) {
            this.degree = degree;
        }

        public YearDelegate getYearDelegate() {
            return yearDelegate;
        }

        public void setYearDelegate(YearDelegate yearDelegate) {
            this.yearDelegate = yearDelegate;
        }

        public List<ExecutionCourse> getCoursesToComment() {
            return coursesToComment;
        }

        public void setCoursesToComment(List<ExecutionCourse> coursesToComment) {
            this.coursesToComment = coursesToComment;
        }
    }
}
