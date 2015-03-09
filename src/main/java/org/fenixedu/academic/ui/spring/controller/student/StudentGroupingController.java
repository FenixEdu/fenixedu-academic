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
package org.fenixedu.academic.ui.spring.controller.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExportGrouping;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.rest.JsonAwareResource;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SpringApplication(description = "link.student.group", path = "enroll-spring", group = "anyone", hint = "Student",
        title = "label.enroll")
@SpringFunctionality(app = StudentGroupingController.class, title = "label.groupEnrolment")
@RequestMapping("/student/groups")
public class StudentGroupingController extends JsonAwareResource {

    @Autowired
    StudentGroupingService studentGroupingService;

    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "student/groupings/showGroupings";
    }

    @RequestMapping(value = "/groupings", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getOpenEnrollmentGroupings() {
        return new ResponseEntity<String>(view(AccessControl
                .getPerson()
                .getStudent()
                .getRegistrationsSet()
                .stream()
                .flatMap(registration -> registration.getAssociatedAttendsSet().stream())
                .map(Attends::getExecutionCourse)
                .filter(executionCourse -> executionCourse.getExecutionPeriod() == ExecutionSemester
                        .readActualExecutionSemester())
                .flatMap(executionCourse -> executionCourse.getGroupings().stream())
                .filter(grouping -> grouping.getAttendsSet().stream()
                        .anyMatch(attends -> attends.getRegistration().getPerson() == AccessControl.getPerson()))
                .filter(grouping -> checkEnrolmentDate(grouping)).collect(Collectors.toList())), HttpStatus.OK);
    }

    @RequestMapping(value = "/grouping/{grouping}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getGrouping(Grouping grouping) {
        if (!groupingIsOpenForEnrollment(grouping)) {
            throw new DomainException("error.grouping.notOpenToEnrollment");
        }
        if (!personInGroupingAttends(grouping, AccessControl.getPerson())) {
            // throw new DomainException("error.grouping.notEnroled");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notEnroled")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<String>(view(grouping), HttpStatus.OK);
    }

    @RequestMapping(value = "/shift/{shift}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getShift(Shift shift) {
        if (shift.getAssociatedStudentGroupsSet().stream().map(StudentGroup::getGrouping)
                .noneMatch(grouping -> personInGroupingAttends(grouping, AccessControl.getPerson()))) {
//            throw new DomainException("error.grouping.notEnroled");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notEnroled")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);

        }

        return new ResponseEntity<String>(view(shift), HttpStatus.OK);
    }

    @RequestMapping(value = "/studentGroup/{studentGroup}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getStudentGroup(StudentGroup studentGroup) {
        if (!personInGroupingAttends(studentGroup.getGrouping(), AccessControl.getPerson())) {
//            throw new DomainException("error.grouping.notEnroled");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notEnroled")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);

        }
        if (!groupingIsOpenForEnrollment(studentGroup.getGrouping())) {
            throw new DomainException("error.grouping.notOpenToEnrollment");
        }
        return new ResponseEntity<String>(view(studentGroup), HttpStatus.OK);
    }

    @RequestMapping(value = "{grouping}/shifts", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getShitsForGrouping(@PathVariable Grouping grouping) {
        if (!groupingIsOpenForEnrollment(grouping)) {
            // throw new DomainException("error.grouping.notOpenToEnrollment");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notOpenToEnrollment")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);
        }

        if (grouping.getShiftType() == null) {
            return new ResponseEntity<String>("[]", HttpStatus.OK);
        }

        //because the existing domain might still be in use, otherwise we could just got grouping.getShiftGroupingProperties
        return new ResponseEntity<String>(view(grouping.getExportGroupingsSet().stream().map(ExportGrouping::getExecutionCourse)
                .flatMap(executionCourse -> executionCourse.getAssociatedShifts().stream())
                .filter(shift -> shift.getTypes().contains(grouping.getShiftType())).collect(Collectors.toList())), HttpStatus.OK);
    }

    @RequestMapping(value = "{grouping}/studentGroupsEnrolledByStudent")
    public @ResponseBody ResponseEntity<String> getStudentGroupsEnrolledByStudent(@PathVariable Grouping grouping) {
        if (!groupingIsOpenForEnrollment(grouping)) {
//            throw new DomainException("error.grouping.notOpenToEnrollment");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notOpenToEnrollment")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);
        }
        if (!personInGroupingAttends(grouping, AccessControl.getPerson())) {
//            throw new DomainException("error.grouping.notEnroled");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notEnroled")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<String>(view(grouping
                .getStudentGroupsSet()
                .stream()
                .filter(studentGroup -> studentGroup.getAttendsSet().stream()
                        .anyMatch(attends -> attends.getRegistration().getPerson() == AccessControl.getPerson()))), HttpStatus.OK);
    }

    @RequestMapping(value = "{grouping}/studentGroups")
    public @ResponseBody ResponseEntity<String> getStudentGroups(@PathVariable Grouping grouping) {
        if (!personInGroupingAttends(grouping, AccessControl.getPerson())) {
//            throw new DomainException("error.grouping.notEnroled");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notEnroled")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);
        }
        if (!groupingIsOpenForEnrollment(grouping)) {
//            throw new DomainException("error.grouping.notOpenToEnrollment");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notOpenToEnrollment")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<String>(view(grouping.getStudentGroupsSet()), HttpStatus.OK);
    }

    @RequestMapping(value = "{grouping}/studentsToEnroll")
    public @ResponseBody ResponseEntity<String> getStudentsToEnroll(@PathVariable Grouping grouping) {
        if (!personInGroupingAttends(grouping, AccessControl.getPerson())) {
//            throw new DomainException("error.grouping.notEnroled");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notEnroled")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);
        }
        if (!groupingIsOpenForEnrollment(grouping)) {
//            throw new DomainException("error.grouping.notOpenToEnrollment");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.studentGroupShift.notOpen")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<String>(view(grouping
                .getAttendsSet()
                .stream()
                .filter(attends -> grouping.getStudentGroupsSet().stream()
                        .noneMatch(sg -> sg.getAttendsSet().stream().anyMatch(at -> at.equals(attends))))
                .map(Attends::getRegistration).map(Registration::getPerson).collect(Collectors.toList())), HttpStatus.OK);
    }

    @RequestMapping(value = "/studentGroup/{studentGroup}/enrolled")
    public @ResponseBody ResponseEntity<String> getStudentsEnrolled(@PathVariable StudentGroup studentGroup) {
        if (!personInGroupingAttends(studentGroup.getGrouping(), AccessControl.getPerson())) {
//            throw new DomainException("error.grouping.notEnroled");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notEnroled")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);
        }
        if (!groupingIsOpenForEnrollment(studentGroup.getGrouping())) {
//            throw new DomainException("error.grouping.notOpenToEnrollment");
            return new ResponseEntity<String>(
                    createErrorJson((new DomainException("error.grouping.notOpenToEnrollment")).getLocalizedMessage()),
                    HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<String>(view(studentGroup.getAttendsSet().stream().map(Attends::getRegistration)
                .map(Registration::getPerson).collect(Collectors.toList())), HttpStatus.OK);
    }

    @RequestMapping(value = "/studentGroup/{studentGroup}/enroll", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> enroll(@PathVariable StudentGroup studentGroup) {
        try {
            studentGroupingService.enroll(studentGroup, AccessControl.getPerson());
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/studentGroup/{studentGroup}/unenroll", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> unenroll(@PathVariable StudentGroup studentGroup) {
        try {
            studentGroupingService.unenroll(studentGroup, AccessControl.getPerson());
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/studentGroup/{studentGroup}/changeShift", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> changeShift(@PathVariable StudentGroup studentGroup, @RequestBody String shiftJson) {
        try {

            Shift newShift =
                    FenixFramework.getDomainObject(new JsonParser().parse(shiftJson).getAsJsonObject().get("id").getAsString());
            studentGroupingService.changeShift(studentGroup, newShift);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/createStudentGroup/{grouping}/", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> createStudentGroup(@PathVariable Grouping grouping,
            @RequestBody String studentsToEnrollJson) {
        return createStudentGroup(grouping, null, studentsToEnrollJson);
    }

    @RequestMapping(value = "/createStudentGroup/{grouping}/{shift}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> createStudentGroup(@PathVariable Grouping grouping, @PathVariable Shift shift,
            @RequestBody String studentsToEnrollJson) {
        try {

            List<Person> studentsToEnroll = new ArrayList<Person>();

            for (JsonElement elem : new JsonParser().parse(studentsToEnrollJson).getAsJsonArray()) {
                JsonObject object = elem.getAsJsonObject();
                studentsToEnroll.add((Person) FenixFramework.getDomainObject(object.get("id").getAsString()));
            }

            studentGroupingService.createStudentGroup(grouping, shift, studentsToEnroll);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @Atomic
    private String createErrorJson(String message) {
        JsonObject object = new JsonObject();
        object.addProperty("message", message);
        return object.toString();
    }

    public Boolean groupingIsOpenForEnrollment(Grouping grouping) {
        return grouping.getEnrolmentBeginDayDateDateTime().isBeforeNow()
                && grouping.getEnrolmentEndDayDateDateTime().isAfterNow();
    }

    public Boolean personInGroupingAttends(Grouping grouping, Person person) {
        return grouping.getAttendsSet().stream().map(Attends::getRegistration).map(Registration::getPerson)
                .anyMatch(p -> p.equals(AccessControl.getPerson()));
    }

    public static boolean checkEnrolmentDate(Grouping grouping) {
        if (grouping.getEnrolmentBeginDay() == null || grouping.getEnrolmentBeginDay().before(Calendar.getInstance())) {
            if (grouping.getEnrolmentEndDay() == null || grouping.getEnrolmentEndDay().after(Calendar.getInstance())) {
                return true;
            }
            return false;
        }
        return false;
    }

}
