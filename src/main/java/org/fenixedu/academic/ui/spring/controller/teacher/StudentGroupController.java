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
package org.fenixedu.academic.ui.spring.controller.teacher;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.UriBuilder;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExportGrouping;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.email.ExecutionCourseSender;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.RedirectView;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

@Controller
@RequestMapping("/teacher/{executionCourse}/student-groups/{grouping}")
public class StudentGroupController extends ExecutionCourseController {

    private final StudentGroupService studentGroupService;

    @Autowired
    public StudentGroupController(StudentGroupService studentGroupService) {
        this.studentGroupService = studentGroupService;
    }

    @Override
    protected Class<?> getFunctionalityType() {
        return ManageExecutionCourseDA.class;
    }

    @Override
    Boolean getPermission(Professorship prof) {
        return prof.getPermissions().getGroups();
    }

    @RequestMapping(value = "/createStudentGroup", method = RequestMethod.POST)
    public AbstractUrlBasedView createStudentGroup(Model model, @PathVariable Grouping grouping,
            @ModelAttribute("addStudent") @Validated AttendsBean addStudents, BindingResult bindingResult) {
        return createStudentGroup(model, grouping, null, addStudents, bindingResult);
    }

    @RequestMapping(value = "/shift/{shift}/createStudentGroup", method = RequestMethod.POST)
    public AbstractUrlBasedView createStudentGroup(Model model, @PathVariable Grouping grouping, @PathVariable Shift shift,
            @ModelAttribute("addStudent") @Validated AttendsBean addStudents, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new RedirectView("/teacher/" + executionCourse.getExternalId() + "/student-groups/view/"
                    + grouping.getExternalId(), true);
        }

        StudentGroup studentgroup = studentGroupService.createStudentGroup(grouping, shift);
        return new RedirectView("/teacher/" + executionCourse.getExternalId() + "/student-groups/" + grouping.getExternalId()
                + "/viewStudentGroup/" + studentgroup.getExternalId(), true);
    }

    @RequestMapping(value = "/viewStudentGroup/{studentGroup}", method = RequestMethod.GET)
    public TeacherView viewStudentGroup(Model model, @PathVariable Grouping grouping, @PathVariable StudentGroup studentGroup) {

        model.addAttribute("studentGroup", studentGroup);

        ArrayList<Shift> shiftList = new ArrayList<Shift>();

        if (grouping.getShiftType() != null) {
            for (final ExportGrouping exportGrouping : grouping.getExportGroupingsSet()) {
                final ExecutionCourse executionCourse = exportGrouping.getExecutionCourse();
                for (final Shift shf : executionCourse.getAssociatedShifts()) {
                    if (shf.containsType(grouping.getShiftType())) {
                        shiftList.add(shf);
                    }
                }
            }
        }
        ArrayList<Attends> studentsWithoutStudentGroup = new ArrayList<Attends>();
        studentsWithoutStudentGroup.addAll(grouping
                .getAttendsSet()
                .stream()
                .filter(attends -> grouping.getStudentGroupsSet().stream()
                        .noneMatch(sg -> sg.getAttendsSet().stream().anyMatch(at -> at.equals(attends))))
                .collect(Collectors.toList()));

        model.addAttribute("newShift", studentGroup.getShift());
        model.addAttribute("shifts", shiftList);
        model.addAttribute("studentsWithoutStudentGroup", studentsWithoutStudentGroup);
        return new TeacherView("executionCourse/groupings/viewStudentGroupInformation");
    }

    @RequestMapping(value = "/editStudentGroupAttends/{studentGroup}", method = RequestMethod.POST)
    public AbstractUrlBasedView editStudentGroupAttends(Model model, @PathVariable Grouping grouping,
            @ModelAttribute("attends") @Validated AttendsBean attendsBean, @PathVariable StudentGroup studentGroup,
            BindingResult bindingResult) {

        Map<String, Boolean> studentsToRemove = attendsBean.getRemoveStudent();
        Map<String, Boolean> studentsToAdd = attendsBean.getAddStudent();
        if (bindingResult.hasErrors()) {
            model.addAttribute("removeStudent", studentsToRemove);
            model.addAttribute("addStudent", studentsToAdd);
            model.addAttribute("errors", "binding error " + bindingResult.getAllErrors());
            return viewStudentGroup(model, grouping, studentGroup);
        }

        studentGroupService.updateStudentGroupMembers(studentGroup, studentsToRemove, studentsToAdd);
        return new RedirectView("/teacher/" + executionCourse.getExternalId() + "/student-groups/" + grouping.getExternalId()
                + "/viewStudentGroup/" + studentGroup.getExternalId(), true);
    }

    @RequestMapping(value = "/editStudentGroupShift/{studentGroup}", method = RequestMethod.POST)
    public AbstractUrlBasedView editStudentGroupShift(@PathVariable Grouping grouping, @PathVariable StudentGroup studentGroup,
            @ModelAttribute("newShift") @Validated Shift newShift, BindingResult bindingResult) {
        studentGroupService.updateStudentGroupShift(studentGroup, newShift);
        return new RedirectView("/teacher/" + executionCourse.getExternalId() + "/student-groups/" + grouping.getExternalId()
                + "/viewStudentGroup/" + studentGroup.getExternalId(), true);

    }

    @RequestMapping(value = "/sendEmail/{studentGroup}", method = RequestMethod.GET)
    public RedirectView sendEmail(Model model, HttpServletRequest request, HttpSession session,
            @PathVariable StudentGroup studentGroup) {
        String label =
                studentGroup.getGrouping().getName() + "-" + BundleUtil.getString(Bundle.APPLICATION, "label.group")
                        + studentGroup.getGroupNumber();

        ArrayList<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(Recipient.newInstance(
                label,
                UserGroup.of(studentGroup.getAttendsSet().stream().map(Attends::getRegistration).map(Registration::getPerson)
                        .map(Person::getUser).collect(Collectors.toSet()))));
        String sendEmailUrl =
                UriBuilder
                        .fromUri("/messaging/emails.do")
                        .queryParam("method", "newEmail")
                        .queryParam("sender", ExecutionCourseSender.newInstance(executionCourse).getExternalId())
                        .queryParam("recipient", recipients.stream().filter(r -> r != null).map(r -> r.getExternalId()).toArray())
                        .build().toString();
        String sendEmailWithChecksumUrl =
                GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), sendEmailUrl, session);
        return new RedirectView(sendEmailWithChecksumUrl, true);

    }

    @RequestMapping(value = "/deleteStudentGroup/{studentGroup}", method = RequestMethod.POST)
    public AbstractUrlBasedView deleteStudentGroup(Model model, @PathVariable Grouping grouping,
            @PathVariable StudentGroup studentGroup) {
        if (!studentGroup.getAttendsSet().isEmpty()) {
            model.addAttribute("errors", "errors.invalid.delete.not.empty.studentGroup");
            return viewStudentGroup(model, grouping, studentGroup);
        }
        studentGroupService.deleteStudentGroup(studentGroup);
        return new RedirectView("/teacher/" + executionCourse.getExternalId() + "/student-groups/view/"
                + grouping.getExternalId(), true);
    }
}