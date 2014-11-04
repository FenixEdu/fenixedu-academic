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
/*
 * Created on Nov 4, 2005
 *	by mrsp
 */
package org.fenixedu.academic.ui.faces.bean.manager.personManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.fenixedu.academic.service.services.departmentAdmOffice.AssociateNewFunctionToPerson;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.ui.faces.bean.departmentAdmOffice.FunctionsManagementBackingBean;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.YearMonthDay;

public class ManagerFunctionsManagementBackingBean extends FunctionsManagementBackingBean {

    public ManagerFunctionsManagementBackingBean() {
    }

    @Override
    public String associateNewFunction() throws FenixServiceException, ParseException {

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Double credits = Double.valueOf(this.getCredits());

        Date beginDate_ = null, endDate_ = null;
        try {
            if (this.getBeginDate() != null) {
                beginDate_ = format.parse(this.getBeginDate());
            } else {
                setErrorMessage("error.notBeginDate");
                return "";
            }
            if (this.getEndDate() != null) {
                endDate_ = format.parse(this.getEndDate());
            } else {
                setErrorMessage("error.notEndDate");
                return "";
            }

            AssociateNewFunctionToPerson.runAssociateNewFunctionToPerson(this.getFunctionID(), this.getPersonID(), credits,
                    YearMonthDay.fromDateFields(beginDate_), YearMonthDay.fromDateFields(endDate_));
            setErrorMessage("message.success");
            return "success";

        } catch (ParseException e) {
            setErrorMessage("error.date1.format");
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        }

        return "";
    }

    @Override
    public String getUnits() throws FenixServiceException {
        StringBuilder buffer = new StringBuilder();
        YearMonthDay currentDate = new YearMonthDay();
        getUnitTree(buffer, UnitUtils.readInstitutionUnit(), currentDate);
        return buffer.toString();
    }

    protected void getUnitTree(StringBuilder buffer, Unit parentUnit, YearMonthDay currentDate) {
        buffer.append("<ul class='padding1 nobullet'>");
        getUnitsList(parentUnit, null, buffer, currentDate);
        buffer.append("</ul>");
    }

    protected void getUnitsList(Unit parentUnit, Unit parentUnitParent, StringBuilder buffer, YearMonthDay currentDate) {

        openLITag(buffer);

        List<Unit> subUnits = new ArrayList<Unit>(getSubUnits(parentUnit, currentDate));
        Collections.sort(subUnits, Unit.COMPARATOR_BY_NAME_AND_ID);

        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer, parentUnitParent);
        }

        buffer.append("<a href=\"").append(getContextPath())
                .append("/manager/functionsManagement/chooseFunction.faces?personID=").append(personID).append("&unitID=")
                .append(parentUnit.getExternalId()).append("\">").append(parentUnit.getPresentationName()).append("</a>")
                .append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer, parentUnitParent);
        }

        for (Unit subUnit : subUnits) {
            getUnitsList(subUnit, parentUnit, buffer, currentDate);
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }

    @Override
    public List<PersonFunction> getActiveFunctions() throws FenixServiceException {

        if (this.activeFunctions == null) {
            Person person = this.getPerson();
            List<PersonFunction> activeFunctions = PersonFunction.getPersonFunctions(person, null, false, true, null);
            Collections.sort(activeFunctions, new ReverseComparator(PersonFunction.COMPARATOR_BY_BEGIN_DATE));
            this.activeFunctions = activeFunctions;
        }
        return activeFunctions;
    }

    @Override
    public List<PersonFunction> getInactiveFunctions() throws FenixServiceException {

        if (this.inactiveFunctions == null) {
            Person person = this.getPerson();
            List<PersonFunction> inactiveFunctions = PersonFunction.getPersonFunctions(person, null, false, false, null);
            Collections.sort(inactiveFunctions, new ReverseComparator(PersonFunction.COMPARATOR_BY_BEGIN_DATE));
            this.inactiveFunctions = inactiveFunctions;
        }
        return inactiveFunctions;
    }

    @Override
    public List<Function> getInherentFunctions() throws FenixServiceException {
        if (this.inherentFunctions == null) {
            this.inherentFunctions = PersonFunction.getActiveInherentPersonFunctions(this.getPerson());
        }
        return inherentFunctions;
    }
}
