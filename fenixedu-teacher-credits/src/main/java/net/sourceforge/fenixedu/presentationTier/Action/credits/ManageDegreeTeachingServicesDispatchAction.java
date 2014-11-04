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
/**
 * Nov 14, 2005
 */
package org.fenixedu.academic.ui.struts.action.credits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.teacher.services.UpdateDegreeTeachingServices;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.teacher.DegreeTeachingService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageDegreeTeachingServicesDispatchAction extends FenixDispatchAction {

    public static final Comparator<TeachingServicePercentage> TEACHING_SERVICE_PERCENTAGE_COMPARATOR_BY_SHIFT =
            new Comparator<TeachingServicePercentage>() {
                @Override
                public int compare(TeachingServicePercentage teachingServicePercentage1,
                        TeachingServicePercentage teachingServicePercentage2) {
                    return Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS.compare(teachingServicePercentage1.getShift(),
                            teachingServicePercentage2.getShift());
                }
            };

    protected void teachingServiceDetailsProcess(Professorship professorship, HttpServletRequest request, DynaActionForm dynaForm)
            throws NumberFormatException, FenixServiceException {

        List<TeachingServicePercentage> teachingServicePercentages = new ArrayList<TeachingServicePercentage>();
        HashMap<String, Double> teacherPercentageMap = new HashMap<String, Double>();

        for (Shift shift : professorship.getExecutionCourse().getAssociatedShifts()) {
            Double availablePercentage = TeacherService.getAvailableShiftPercentage(shift, professorship);
            teachingServicePercentages.add(new TeachingServicePercentage(shift, availablePercentage));
            for (DegreeTeachingService degreeTeachingService : shift.getDegreeTeachingServicesSet()) {
                if (professorship == degreeTeachingService.getProfessorship()) {
                    teacherPercentageMap.put(shift.getExternalId().toString(), round(degreeTeachingService.getPercentage()));
                    break;
                }
            }
        }

        Iterator<TeachingServicePercentage> teachingServicePercentagesIterator =
                new OrderedIterator<TeachingServicePercentage>(teachingServicePercentages.iterator(),
                        TEACHING_SERVICE_PERCENTAGE_COMPARATOR_BY_SHIFT);

        request.setAttribute("professorship", professorship);
        request.setAttribute("teachingServicePercentages", teachingServicePercentagesIterator);
        dynaForm.set("teacherPercentageMap", teacherPercentageMap);
    }

    protected ActionForward updateTeachingServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            RoleType roleType) throws NumberFormatException, FenixServiceException {

        DynaActionForm teachingServiceForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();
        HashMap<String, String> teacherPercentageMap = (HashMap<String, String>) teachingServiceForm.get("teacherPercentageMap");

        String professorshipID = (String) teachingServiceForm.get("professorshipID");

        List<ShiftIDTeachingPercentage> shiftIDPercentages = new ArrayList<ShiftIDTeachingPercentage>();
        Iterator<Map.Entry<String, String>> entryInterator = teacherPercentageMap.entrySet().iterator();
        while (entryInterator.hasNext()) {
            Map.Entry<String, String> entry = entryInterator.next();
            String percentage = entry.getValue();
            if ((percentage != null) && (percentage.length() != 0)) {
                percentage = percentage.replace(',', '.');
                String shiftID = entry.getKey();
                ShiftIDTeachingPercentage shiftIDPercentage = new ShiftIDTeachingPercentage(shiftID, Double.valueOf(percentage));
                shiftIDPercentages.add(shiftIDPercentage);
            }
        }

        try {
            UpdateDegreeTeachingServices.runUpdateDegreeTeachingServices(professorshipID, shiftIDPercentages, roleType);
        } catch (DomainException domainException) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error", new ActionMessage(domainException.getMessage(), domainException.getArgs()));
            saveMessages(request, actionMessages);
            Professorship professorship = FenixFramework.getDomainObject(professorshipID);
            teachingServiceDetailsProcess(professorship, request, teachingServiceForm);
            return mapping.findForward("show-teaching-service-percentages");
        }

        return mapping.findForward("sucessfull-edit");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws NumberFormatException, FenixServiceException {

        return mapping.findForward("sucessfull-edit");
    }

    public class ShiftIDTeachingPercentage {

        String shiftID;

        Double percentage;

        public ShiftIDTeachingPercentage(String shiftID, Double percentage) {
            setShiftID(shiftID);
            setPercentage(percentage);
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }

        public String getShiftID() {
            return shiftID;
        }

        public void setShiftID(String shiftID) {
            this.shiftID = shiftID;
        }

    }

    public class TeachingServicePercentage {

        Shift shift;

        Double availablePercentage;

        public TeachingServicePercentage(Shift shift, Double availablePercentage) {
            setShift(shift);
            setAvailablePercentage(availablePercentage);
        }

        public Double getAvailablePercentage() {
            return availablePercentage;
        }

        public void setAvailablePercentage(Double availablePercentage) {
            this.availablePercentage = availablePercentage;
        }

        public Shift getShift() {
            return shift;
        }

        public void setShift(Shift shift) {
            this.shift = shift;
        }
    }

    private Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }
}
