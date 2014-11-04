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
/*
 * Created on 22/Jul/2003
 *
 */
package org.fenixedu.academic.dto;

import java.util.Calendar;
import java.util.List;

import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.util.EnrolmentGroupPolicyType;

/**
 * @author asnr and scpo
 * @author joaosa & rmalo 12/Aug/2004
 */
public class InfoGrouping extends InfoObject {

    private Integer maximumCapacity;

    private Integer minimumCapacity;

    private Integer idealCapacity;

    private EnrolmentGroupPolicyType enrolmentPolicy;

    private Integer groupMaximumNumber;

    private String name;

    private List infoExportGroupings;

    private ShiftType shiftType;

    private Calendar enrolmentBeginDay;

    private Calendar enrolmentEndDay;

    private String projectDescription;

    private List infoAttends;

    private Boolean automaticEnrolment;

    private Boolean differentiatedCapacity;

    private List<InfoShift> infoShifts;

    /**
     * Construtor
     */
    public InfoGrouping() {

    }

    /**
     * Construtor
     */
    public InfoGrouping(Integer maximumCapacity, Integer minimumCapacity, Integer idealCapacity,
            EnrolmentGroupPolicyType enrolmentPolicy, Integer groupMaximumNumber, List infoExportGroupings, String name,
            ShiftType shiftType, Calendar enrolmentBeginDay, Calendar enrolmentEndDay, String projectDescription,
            Boolean automaticEnrolment, Boolean differenciatedCapacity, List infoShifts) {

        this.maximumCapacity = maximumCapacity;
        this.minimumCapacity = minimumCapacity;
        this.idealCapacity = idealCapacity;
        this.enrolmentPolicy = enrolmentPolicy;
        this.groupMaximumNumber = groupMaximumNumber;
        this.infoExportGroupings = infoExportGroupings;
        this.name = name;
        this.shiftType = shiftType;
        this.enrolmentBeginDay = enrolmentBeginDay;
        this.enrolmentEndDay = enrolmentEndDay;
        this.projectDescription = projectDescription;
        this.automaticEnrolment = automaticEnrolment;
        this.differentiatedCapacity = differenciatedCapacity;
        this.infoShifts = infoShifts;
    }

    @Override
    public String toString() {

        String result = "[INFO_GROUP_PROPERTIES";
        result += ", maximumCapacity=" + getMaximumCapacity();
        result += ", minimumCapacity=" + getMinimumCapacity();
        result += ", idealCapacity=" + getIdealCapacity();
        result += ", enrolmentPolicy=" + getEnrolmentPolicy();
        result += ", groupMaximumNumber=" + getGroupMaximumNumber();
        result += ", name=" + getName();
        result += ", shiftType=" + getShiftType();
        // result += ", enrolmentBeginDay=" + getEnrolmentBeginDay();
        // result += ", enrolmentEndDay=" + getEnrolmentEndDay();
        result += ", projectDescription=" + getProjectDescription();
        result += "]";
        return result;
    }

    public Integer getMaximumCapacity() {

        return maximumCapacity;
    }

    public Integer getMinimumCapacity() {

        return minimumCapacity;
    }

    public Integer getIdealCapacity() {

        return idealCapacity;
    }

    public EnrolmentGroupPolicyType getEnrolmentPolicy() {

        return enrolmentPolicy;
    }

    public Integer getGroupMaximumNumber() {

        return groupMaximumNumber;
    }

    public String getName() {

        return name;
    }

    public String getProjectDescription() {

        return projectDescription;
    }

    public ShiftType getShiftType() {

        return shiftType;
    }

    public Calendar getEnrolmentBeginDay() {

        return enrolmentBeginDay;
    }

    public Calendar getEnrolmentEndDay() {

        return enrolmentEndDay;
    }

    public void setMaximumCapacity(Integer maximumCapacity) {

        this.maximumCapacity = maximumCapacity;
    }

    public void setMinimumCapacity(Integer minimumCapacity) {

        this.minimumCapacity = minimumCapacity;
    }

    public void setIdealCapacity(Integer idealCapacity) {

        this.idealCapacity = idealCapacity;
    }

    public void setEnrolmentPolicy(EnrolmentGroupPolicyType enrolmentPolicy) {

        this.enrolmentPolicy = enrolmentPolicy;
    }

    public void setGroupMaximumNumber(Integer groupMaximumNumber) {

        this.groupMaximumNumber = groupMaximumNumber;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setProjectDescription(String projectDescription) {

        this.projectDescription = projectDescription;
    }

    public void setShiftType(ShiftType shiftType) {

        this.shiftType = shiftType;
    }

    public void setEnrolmentBeginDay(Calendar enrolmentBeginDay) {

        this.enrolmentBeginDay = enrolmentBeginDay;
    }

    public void setEnrolmentEndDay(Calendar enrolmentEndDay) {

        this.enrolmentEndDay = enrolmentEndDay;
    }

    public String getShiftTypeFormatted() {
        return shiftType.toString();
    }

    public String getEnrolmentBeginDayFormatted() {

        String result = "";
        Calendar date = getEnrolmentBeginDay();
        if (date == null) {
            return result;
        }
        if (date.get(Calendar.DAY_OF_MONTH) < 10) {
            result = result.concat("0");
        }
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        if ((date.get(Calendar.MONTH) + 1) < 10) {
            result = result.concat("0");
        }
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        return result;
    }

    public String getEnrolmentBeginHourFormatted() {

        String result = "";
        Calendar date = getEnrolmentBeginDay();
        if (date == null) {
            return result;
        }
        if (date.get(Calendar.HOUR_OF_DAY) < 10) {
            result = result.concat("0");
        }
        result += date.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (date.get(Calendar.MINUTE) < 10) {
            result = result.concat("0");
        }
        result += date.get(Calendar.MINUTE);
        return result;
    }

    public String getEnrolmentEndDayFormatted() {

        String result = "";
        Calendar date = getEnrolmentEndDay();
        if (date == null) {
            return result;
        }
        if (date.get(Calendar.DAY_OF_MONTH) < 10) {
            result = result.concat("0");
        }
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        if ((date.get(Calendar.MONTH) + 1) < 10) {
            result = result.concat("0");
        }
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        return result;
    }

    public String getEnrolmentEndHourFormatted() {

        String result = "";
        Calendar date = getEnrolmentEndDay();
        if (date == null) {
            return result;
        }
        if (date.get(Calendar.HOUR_OF_DAY) < 10) {
            result = result.concat("0");
        }
        result += date.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (date.get(Calendar.MINUTE) < 10) {
            result = result.concat("0");
        }
        result += date.get(Calendar.MINUTE);
        return result;
    }

    public void copyFromDomain(Grouping groupProperties) {
        super.copyFromDomain(groupProperties);
        if (groupProperties != null) {
            setName(groupProperties.getName());
            // setInfoAttendsSet(__________);
            setProjectDescription(groupProperties.getProjectDescription());
            setShiftType(groupProperties.getShiftType());
            setEnrolmentPolicy(groupProperties.getEnrolmentPolicy());
            setGroupMaximumNumber(groupProperties.getGroupMaximumNumber());
            setIdealCapacity(groupProperties.getIdealCapacity());
            setMaximumCapacity(groupProperties.getMaximumCapacity());
            setMinimumCapacity(groupProperties.getMinimumCapacity());
            setEnrolmentBeginDay(groupProperties.getEnrolmentBeginDay());
            setEnrolmentEndDay(groupProperties.getEnrolmentEndDay());
            setAutomaticEnrolment(groupProperties.getAutomaticEnrolment());
            setDifferentiatedCapacity(groupProperties.getDifferentiatedCapacity());
        }
    }

    public static InfoGrouping newInfoFromDomain(Grouping groupProperties) {
        InfoGrouping infoGroupProperties = null;
        if (groupProperties != null) {
            infoGroupProperties = new InfoGrouping();
            infoGroupProperties.copyFromDomain(groupProperties);
        }

        return infoGroupProperties;
    }

    public List getInfoExportGroupings() {
        return infoExportGroupings;
    }

    public void setInfoExportGroupings(List infoExportGroupings) {
        this.infoExportGroupings = infoExportGroupings;
    }

    public List getInfoAttends() {
        return infoAttends;
    }

    public void setInfoAttends(List infoAttends) {
        this.infoAttends = infoAttends;
    }

    public List<InfoShift> getInfoShifts() {
        return infoShifts;
    }

    public void setInfoShifts(List<InfoShift> infoShifts) {
        this.infoShifts = infoShifts;
    }

    public Boolean getAutomaticEnrolment() {
        return automaticEnrolment;
    }

    public void setAutomaticEnrolment(Boolean automaticEnrolment) {
        this.automaticEnrolment = automaticEnrolment;
    }

    public Boolean getDifferentiatedCapacity() {
        return differentiatedCapacity;
    }

    public void setDifferentiatedCapacity(Boolean differentiatedCapacity) {
        this.differentiatedCapacity = differentiatedCapacity;
    }
}
