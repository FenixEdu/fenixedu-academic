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
package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(Attends.class)
public class AttendsJsonAdapter implements JsonViewer<Attends> {
    @Override
    public JsonElement view(Attends attends, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("externalId", attends.getExternalId());
        object.add("person", ctx.view(attends.getRegistration().getPerson()));
        object.addProperty("number", attends.getRegistration().getNumber());
        object.add("studentGroups", ctx.view(attends.getStudentGroupsSet()));
        object.add("curricularPlan", ctx.view(attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan()));
        if (attends.getEnrolment() != null) {
            object.addProperty("enrolmentsInThisCourse",
                    attends.getEnrolment().getNumberOfTotalEnrolmentsInThisCourse(attends.getEnrolment().getExecutionPeriod()));
        } else {
            object.addProperty("enrolmentsInThisCourse", "--");
        }
        RegistrationState registrationState = attends.getRegistration().getLastRegistrationState(attends.getExecutionYear());
        object.addProperty("registrationState", registrationState == null ? "" : registrationState.getStateType()
                .getDescription());

        object.addProperty("enrolmentType",
                BundleUtil.getString(Bundle.ENUMERATION, attends.getAttendsStateType().getQualifiedName()));

        object.addProperty("workingStudent",
                attends.getRegistration().getStudent().hasWorkingStudentStatuteInPeriod(attends.getExecutionPeriod()));

        JsonObject shiftsByType = new JsonObject();
        attends.getExecutionCourse().getShiftTypes().forEach(shiftType -> {
            Shift shift = attends.getRegistration().getShiftFor(attends.getExecutionCourse(), shiftType);
            if (shift != null) {
                shiftsByType.add(shiftType.getName(), ctx.view(shift, ShiftShortJsonAdapter.class));
            } else {
                shiftsByType.add(shiftType.getName(), new JsonObject());
            }

        });
        object.add("shifts", shiftsByType);

        return object;
    }
}
