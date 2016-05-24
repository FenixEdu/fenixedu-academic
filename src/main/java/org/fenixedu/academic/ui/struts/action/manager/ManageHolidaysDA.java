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
package org.fenixedu.academic.ui.struts.action.manager;

import java.io.Serializable;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Holiday;
import org.fenixedu.academic.domain.Locality;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.DeleteHoliday;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerPeopleApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ManagerPeopleApp.class, path = "manage-holidays", titleKey = "label.manage.holidays",
        accessGroup = "#managers")
@Mapping(module = "manager", path = "/manageHolidays")
@Forwards(@Forward(name = "showHolidays", path = "/manager/showHolidays.jsp"))
@Deprecated
public class ManageHolidaysDA extends FenixDispatchAction {

    public static class HolidayFactoryCreator implements Serializable, FactoryExecutor {
        private Integer year;
        private Integer monthOfYear;
        private Integer dayOfMonth;
        private Locality localityReference;

        public Integer getDayOfMonth() {
            return dayOfMonth;
        }

        public void setDayOfMonth(Integer dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
        }

        public Integer getMonthOfYear() {
            return monthOfYear;
        }

        public void setMonthOfYear(Integer monthOfYear) {
            this.monthOfYear = monthOfYear;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Locality getLocality() {
            return localityReference;
        }

        public void setLocality(Locality locality) {
            if (locality != null) {
                this.localityReference = locality;
            }
        }

        @Override
        public Holiday execute() {
            Partial date = new Partial();
            if (getYear() != null) {
                date = date.with(DateTimeFieldType.year(), getYear());
            }
            if (getMonthOfYear() != null) {
                date = date.with(DateTimeFieldType.monthOfYear(), getMonthOfYear());
            }
            if (getDayOfMonth() != null) {
                date = date.with(DateTimeFieldType.dayOfMonth(), getDayOfMonth());
            }
            return new Holiday(date, getLocality());
        }
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final Set<Holiday> holidays = rootDomainObject.getHolidaysSet();
        request.setAttribute("holidays", holidays);
        return mapping.findForward("showHolidays");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        executeFactoryMethod();
        return prepare(mapping, form, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        final String holidayIDString = request.getParameter("holidayID");
        if (holidayIDString != null && StringUtils.isNumeric(holidayIDString)) {
            final Holiday holiday = FenixFramework.getDomainObject(holidayIDString);

            DeleteHoliday.run(holiday);
        }
        return prepare(mapping, form, request, response);
    }

}