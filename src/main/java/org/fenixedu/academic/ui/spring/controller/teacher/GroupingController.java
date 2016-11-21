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

import java.util.*;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExportGrouping;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA;
import org.fenixedu.academic.util.ProposalState;
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

import pt.ist.fenixframework.FenixFramework;

@Controller
@RequestMapping("/teacher/{executionCourse}/student-groups/")
public class GroupingController extends ExecutionCourseController {

    @Autowired
    StudentGroupService studentGroupService;

    @Override
    protected Class<?> getFunctionalityType() {
        return ManageExecutionCourseDA.class;
    }

    @Override
    Boolean getPermission(Professorship prof) {
        return prof.getPermissions().getGroups();
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public TeacherView showStudentGroups(Model model) {
        return new TeacherView("executionCourse/groupings/viewProjectsAndLink");
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public TeacherView create(Model model) {
        model.addAttribute("projectGroup", new ProjectGroupBean(this.executionCourse));
        return new TeacherView("executionCourse/groupings/insertGroupProperties");
    }

    @RequestMapping(value = "/edit/{grouping}", method = RequestMethod.GET)
    public TeacherView edit(Model model, Grouping grouping) {
        model.addAttribute("projectGroup", new ProjectGroupBean(grouping, this.executionCourse));
        return new TeacherView("executionCourse/groupings/insertGroupProperties");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public AbstractUrlBasedView create(Model model, @ModelAttribute("projectGroup") ProjectGroupBean projectGroup,
            @PathVariable ExecutionCourse executionCourse, BindingResult bindingResult) {

        ArrayList<String> errors = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            errors.add("error.groupProperties.errorsDetected");
        }

        if (projectGroup.getName().isEmpty()) {
            errors.add("error.groupProperties.missingName");
        }

        Grouping groupingWithSameName = executionCourse.getGroupingByName(projectGroup.getName());

        if (groupingWithSameName != null && !groupingWithSameName.getExternalId().equals(projectGroup.getExternalId())) {
            errors.add("error.exception.existing.groupProperties");
        }

        ShiftType shiftType =
                projectGroup.getShiftType() == null || projectGroup.getShiftType().isEmpty() ? null : ShiftType
                        .valueOf(projectGroup.getShiftType());

        if (projectGroup.getDifferentiatedCapacity()
                && projectGroup.getMaximumGroupCapacity() != null
                && projectGroup
                        .getDifferentiatedCapacityShifts()
                        .entrySet()
                        .stream()
                        .anyMatch(
                                entry -> ((Shift) FenixFramework.getDomainObject(entry.getKey())).getTypes().contains(shiftType)
                                        && entry.getValue() != null
                                        && ((Shift) FenixFramework.getDomainObject(entry.getKey())).getLotacao() != 0 /* it means it was locked from students enrolment only*/
                                        && entry.getValue() * projectGroup.getMaximumGroupCapacity() > ((Shift) FenixFramework
                                                .getDomainObject(entry.getKey())).getLotacao())) {
            errors.add("error.groupProperties.capacityOverflow");
        }

        if (projectGroup.getMaximumGroupCapacity() == null || projectGroup.getMaximumGroupCapacity() < 0) {
            errors.add("error.groupProperties.capacity.negative");
        }

        if (projectGroup.getMinimumGroupCapacity() == null || projectGroup.getMinimumGroupCapacity() < 0) {
            errors.add("error.groupProperties.capacity.negative");
        }

        if (projectGroup.getMaxGroupNumber() == null || projectGroup.getMaxGroupNumber() < 0) {
            errors.add("error.groupProperties.capacity.negative");
        }

        if (projectGroup.getIdealGroupCapacity() != null && projectGroup.getIdealGroupCapacity() < 0) {
            errors.add("error.groupProperties.capacity.negative");
        }

        if (smallerThan(projectGroup.getMaximumGroupCapacity(), projectGroup.getMinimumGroupCapacity())) {
            errors.add("error.groupProperties.minimum");
        }

        if (smallerThan(projectGroup.getMaximumGroupCapacity(), projectGroup.getIdealGroupCapacity())) {
            errors.add("error.groupProperties.ideal.maximum");
        }

        if(groupingWithSameName != null && !groupingWithSameName.getStudentGroupsSet().isEmpty()) {
            Integer maxStudentGroupsOnShift = groupingWithSameName.getStudentGroupsSet().stream()
                .filter(studentGroup -> !studentGroup.wasDeleted())
                .filter(studentGroup -> studentGroup.getShift() != null)
                .collect(Collectors.groupingBy(studentGroup -> studentGroup.getShift(), Collectors.counting()))
                .values().stream().max(Comparator.naturalOrder()).orElseGet(()->new Long(0)).intValue();

            if( smallerThan(projectGroup.getMaxGroupNumber(), maxStudentGroupsOnShift)){
                errors.add("error.groupProperties.many.shift.groups");
            }
        }

        if (smallerThan(projectGroup.getIdealGroupCapacity(), projectGroup.getMinimumGroupCapacity())) {
            errors.add("error.groupProperties.ideal.minimum");
        }

        if (projectGroup.getEnrolmentBeginDay() == null) {
            errors.add("error.groupProperties.missingEnrolmentBeginDay");
        }

        if (projectGroup.getEnrolmentEndDay() == null) {
            errors.add("error.groupProperties.missingEnrolmentEndDay");
        }

        if (projectGroup.getEnrolmentEndDay() != null && projectGroup.getEnrolmentEndDay() != null
                && projectGroup.getEnrolmentEndDay().isBefore(projectGroup.getEnrolmentBeginDay())) {
            errors.add("error.manager.wrongDates");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return new TeacherView("executionCourse/groupings/insertGroupProperties");
        }

        Grouping grouping = studentGroupService.createOrEditGrouping(projectGroup, executionCourse);
        return new RedirectView("/teacher/" + executionCourse.getExternalId() + "/student-groups/view/"
                + grouping.getExternalId(), true);
    }

    private static Boolean smallerThan(Integer a, Integer b) {
        return a != null && b != null && a < b;
    }

    @RequestMapping(value = "/view/{grouping}", method = RequestMethod.GET)
    public TeacherView viewGrouping(Model model, @PathVariable Grouping grouping) {
        List<Shift> shiftList = new ArrayList<Shift>();

        if (grouping.getShiftType() != null) {
            shiftList =
                    grouping.getExportGroupingsSet().stream().map(ExportGrouping::getExecutionCourse)
                            .flatMap(ec -> ec.getAssociatedShifts().stream()).sorted(Shift.SHIFT_COMPARATOR_BY_NAME)
                            .filter(shift -> shift.containsType(grouping.getShiftType())).collect(Collectors.toList());
        }

        HashMap<Shift, TreeSet<StudentGroup>> studentGroupsByShift = new HashMap<Shift, TreeSet<StudentGroup>>();

        for (Shift shift : shiftList) {
            TreeSet<StudentGroup> tree = new TreeSet<StudentGroup>(StudentGroup.COMPARATOR_BY_GROUP_NUMBER);
            tree.addAll(shift.getAssociatedStudentGroups(grouping));
            studentGroupsByShift.put(shift, tree);
        }

        if (shiftList.isEmpty()) {
            TreeSet<StudentGroup> studentGroups = new TreeSet<StudentGroup>(StudentGroup.COMPARATOR_BY_GROUP_NUMBER);
            studentGroups.addAll(grouping.getStudentGroupsSet());
            model.addAttribute("studentGroups", studentGroups);
        }

        model.addAttribute("studentGroupsByShift", studentGroupsByShift);
        model.addAttribute("grouping", grouping);
        model.addAttribute("shifts", shiftList);
        return new TeacherView("executionCourse/groupings/viewShiftsAndGroups");
    }

    @RequestMapping(value = "/viewAttends/{grouping}", method = RequestMethod.GET)
    public TeacherView viewAttends(Model model, @PathVariable Grouping grouping) {
        model.addAttribute("grouping", grouping);

        ArrayList<Registration> studentsNotAttending = new ArrayList<Registration>();
        for (final ExportGrouping exportGrouping : grouping.getExportGroupingsSet()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                exportGrouping.getExecutionCourse().getAttendsSet().stream()
                        .filter(attend -> !grouping.getAttendsSet().contains(attend))
                        .forEach(attend -> studentsNotAttending.add(attend.getRegistration()));

            }
        }

        model.addAttribute("studentsNotAttending", studentsNotAttending);
        return new TeacherView("executionCourse/groupings/viewAttendsSet");

    }

    @RequestMapping(value = "/editAttends/{grouping}", method = RequestMethod.POST)
    public TeacherView editAttends(Model model, @PathVariable Grouping grouping,
            @ModelAttribute("attends") @Validated AttendsBean attendsBean, @PathVariable ExecutionCourse executionCourse,
            BindingResult bindingResult) {

        Map<String, Boolean> studentsToRemove = attendsBean.getRemoveStudent();
        Map<String, Boolean> studentsToAdd = attendsBean.getAddStudent();
        if (bindingResult.hasErrors()) {
            model.addAttribute("removeStudent", studentsToRemove);
            model.addAttribute("addStudent", studentsToAdd);
            model.addAttribute("errors", "binding error " + bindingResult.getAllErrors());
            return viewAttends(model, grouping);
        }

        studentGroupService.updateGroupingAttends(grouping, studentsToRemove, studentsToAdd);

        return viewAttends(model, grouping);

    }

    @RequestMapping(value = "/viewAllStudentsAndGroups/{grouping}", method = RequestMethod.GET)
    public TeacherView viewAllStudentsAndGroups(Model model, @PathVariable Grouping grouping) {
        model.addAttribute("grouping", grouping);
        model.addAttribute("studentsInStudentGroupsSize",
                grouping.getStudentGroupsSet().stream().mapToInt(sg -> sg.getAttendsSet().size()).sum());
        return new TeacherView("executionCourse/groupings/viewAllStudentsAndGroups");

    }

    @RequestMapping(value = "/viewStudentsAndGroupsByShift/{grouping}", method = RequestMethod.GET)
    public TeacherView viewStudentsAndGroupsByShift(Model model, @PathVariable Grouping grouping) {
        model.addAttribute("grouping", grouping);

        return new TeacherView("executionCourse/groupings/viewStudentsAndGroupsByShift");
    }

    @RequestMapping(value = "/viewStudentsAndGroupsByShift/{grouping}/shift/{shift}", method = RequestMethod.GET)
    public TeacherView viewStudentsAndGroupsByShift(Model model, @PathVariable Grouping grouping, @PathVariable Shift shift) {
        model.addAttribute("shift", shift);
        model.addAttribute("grouping", grouping);
        TreeSet<StudentGroup> studentsByGroup = new TreeSet<StudentGroup>(StudentGroup.COMPARATOR_BY_GROUP_NUMBER);
        studentsByGroup.addAll(shift.getAssociatedStudentGroups(grouping));
        model.addAttribute("studentsByGroup", studentsByGroup);

        return new TeacherView("executionCourse/groupings/viewStudentsAndGroupsByShift");
    }

    @RequestMapping(value = "/deleteGrouping/{grouping}", method = RequestMethod.POST)
    public RedirectView deleteGrouping(Model model, @PathVariable Grouping grouping) {
        studentGroupService.deleteGrouping(grouping);
        return new RedirectView("/teacher/" + executionCourse.getExternalId() + "/student-groups/show", true);
    }

}