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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CoordinatorInquiryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryBlockDTO;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.inquiries.CoordinatorInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryRegentAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTeacherAnswer;
import net.sourceforge.fenixedu.domain.inquiries.RegentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/viewQUCInquiryAnswers", module = "publico")
public class ViewQUCInquiryAnswers extends FenixDispatchAction {

    public ActionForward showCoordinatorInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionDegree executionDegree =
                FenixFramework.getDomainObject(getFromRequest(request, "executionDegreeOID").toString());
        ExecutionSemester executionSemester =
                FenixFramework.getDomainObject(getFromRequest(request, "executionPeriodOID").toString());
        CoordinatorInquiryTemplate coordinatorInquiryTemplate =
                CoordinatorInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
        Coordinator coordinator = FenixFramework.getDomainObject(getFromRequest(request, "coordinatorOID").toString());
        InquiryCoordinatorAnswer inquiryCoordinatorAnswer = null;
        if (coordinatorInquiryTemplate.getShared()) {
            inquiryCoordinatorAnswer = executionDegree.getInquiryCoordinationAnswers(executionSemester);
        } else {
            inquiryCoordinatorAnswer = coordinator.getInquiryCoordinatorAnswer(executionSemester);
            request.setAttribute("person", coordinator.getPerson());
        }

        CoordinatorInquiryBean coordinatorInquiryBean =
                new CoordinatorInquiryBean(coordinatorInquiryTemplate, coordinator, inquiryCoordinatorAnswer, executionSemester,
                        executionDegree);

        Set<InquiryBlockDTO> coordinatorInquiryBlocks =
                new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
        for (InquiryBlock inquiryBlock : coordinatorInquiryTemplate.getInquiryBlocks()) {
            coordinatorInquiryBlocks.add(new InquiryBlockDTO(inquiryCoordinatorAnswer, inquiryBlock));
        }

        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("executionDegree", executionDegree);
        request.setAttribute("coordinatorInquiryBlocks", coordinatorInquiryBlocks);

        return new ActionForward(null, "/inquiries/showCoordinatorInquiry.jsp", false, "/coordinator");
    }

    public ActionForward showRegentInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Professorship professorship = FenixFramework.getDomainObject(getFromRequest(request, "professorshipOID").toString());

        RegentInquiryTemplate regentInquiryTemplate =
                RegentInquiryTemplate.getTemplateByExecutionPeriod(professorship.getExecutionCourse().getExecutionPeriod());
        InquiryRegentAnswer inquiryRegentAnswer = professorship.getInquiryRegentAnswer();

        Set<InquiryBlockDTO> regentInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
        for (InquiryBlock inquiryBlock : regentInquiryTemplate.getInquiryBlocks()) {
            regentInquiryBlocks.add(new InquiryBlockDTO(inquiryRegentAnswer, inquiryBlock));
        }

        request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
        request.setAttribute("executionCourse", professorship.getExecutionCourse());
        request.setAttribute("person", professorship.getPerson());
        request.setAttribute("regentInquiryBlocks", regentInquiryBlocks);

        return new ActionForward(null, "/inquiries/showRegentInquiry.jsp", false, "/teacher");
    }

    public ActionForward showTeacherInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Professorship professorship = FenixFramework.getDomainObject(getFromRequest(request, "professorshipOID").toString());

        TeacherInquiryTemplate teacherInquiryTemplate =
                TeacherInquiryTemplate.getTemplateByExecutionPeriod(professorship.getExecutionCourse().getExecutionPeriod());
        InquiryTeacherAnswer inquiryTeacherAnswer = professorship.getInquiryTeacherAnswer();

        Set<InquiryBlockDTO> teacherInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
        for (InquiryBlock inquiryBlock : teacherInquiryTemplate.getInquiryBlocks()) {
            teacherInquiryBlocks.add(new InquiryBlockDTO(inquiryTeacherAnswer, inquiryBlock));
        }

        request.setAttribute("executionPeriod", professorship.getExecutionCourse().getExecutionPeriod());
        request.setAttribute("executionCourse", professorship.getExecutionCourse());
        request.setAttribute("person", professorship.getPerson());
        request.setAttribute("teacherInquiryBlocks", teacherInquiryBlocks);

        return new ActionForward(null, "/inquiries/showTeacherInquiry.jsp", false, "/teacher");
    }

    public ActionForward showDelegateInquiry(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionCourse executionCourse =
                FenixFramework.getDomainObject(getFromRequest(request, "executionCourseOID").toString());
        ExecutionDegree executionDegree =
                FenixFramework.getDomainObject(getFromRequest(request, "executionDegreeOID").toString());

        DelegateInquiryTemplate delegateInquiryTemplate =
                DelegateInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
        InquiryDelegateAnswer inquiryDelegateAnswer = null;
        for (InquiryDelegateAnswer delegateAnswer : executionCourse.getInquiryDelegatesAnswers()) {
            if (delegateAnswer.getExecutionDegree() == executionDegree) {
                inquiryDelegateAnswer = delegateAnswer;
                break;
            }
        }

        Set<InquiryBlockDTO> delegateInquiryBlocks = new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder"));
        for (InquiryBlock inquiryBlock : delegateInquiryTemplate.getInquiryBlocks()) {
            delegateInquiryBlocks.add(new InquiryBlockDTO(inquiryDelegateAnswer, inquiryBlock));
        }

        Integer year = inquiryDelegateAnswer != null ? inquiryDelegateAnswer.getDelegate().getCurricularYear().getYear() : null;
        request.setAttribute("year", year);
        request.setAttribute("executionPeriod", executionCourse.getExecutionPeriod());
        request.setAttribute("executionCourse", executionCourse);
        request.setAttribute("executionDegree", executionDegree);
        request.setAttribute("delegateInquiryBlocks", delegateInquiryBlocks);

        return new ActionForward(null, "/inquiries/showDelegateInquiry.jsp", false, "/delegate");
    }
}
