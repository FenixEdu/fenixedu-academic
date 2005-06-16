/*
 * Created on 08/Mar/2005
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * @author joaosa & rmalo
 * 
 */
public class GroupProperties extends GroupProperties_Base {

    /**
     * @return Calendar
     */
    public Calendar getEnrolmentBeginDay() {
        if (this.getEnrolmentBeginDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrolmentBeginDayDate());
            return result;
        }
        return null;
    }

    /**
     * Sets the enrolmentBeginDay.
     * 
     * @param enrolmentBeginDay
     *            The enrolmentBeginDay to set
     */
    public void setEnrolmentBeginDay(Calendar enrolmentBeginDay) {
        if (enrolmentBeginDay.getTime() != null) {
            this.setEnrolmentBeginDayDate(enrolmentBeginDay.getTime());
        } else {
            this.setEnrolmentBeginDayDate(null);
        }
    }

    /**
     * @return Calendar
     */
    public Calendar getEnrolmentEndDay() {
        if (this.getEnrolmentEndDayDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrolmentEndDayDate());
            return result;
        }
        return null;
    }

    /**
     * Sets the enrolmentEndDay.
     * 
     * @param enrolmentEndDay
     *            The enrolmentEndDay to set
     */
    public void setEnrolmentEndDay(Calendar enrolmentEndDay) {
        if (enrolmentEndDay.getTime() != null) {
            this.setEnrolmentEndDayDate(enrolmentEndDay.getTime());
        } else {
            this.setEnrolmentEndDayDate(null);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[GROUP";
        result += ", maximumCapacity=" + getMaximumCapacity();
        result += ", minimumCapacity=" + getMinimumCapacity();
        result += ", idealCapacity=" + getIdealCapacity();
        result += ", enrolmentPolicy=" + getEnrolmentPolicy();
        result += ", groupMaximumNumber=" + getGroupMaximumNumber();
        result += ", name=" + getName();
        result += ", attendsSet=" + getAttendsSet();
        result += ", shiftType=" + getShiftType();
        result += ", projectDescription=" + getProjectDescription();
        result += "]";
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof IGroupProperties) {
            result = (getAttendsSet().equals(((IGroupProperties) arg0).getAttendsSet()))
                    && (getName().equals(((IGroupProperties) arg0).getName()));
        }
        return result;
    }

    /**
     * @return List
     */
    public List getExecutionCourses() {
        List executionCourses = new ArrayList();
        Iterator iterGroupPropertiesExecutionCourse = getGroupPropertiesExecutionCourse().iterator();
        IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = null;
        while (iterGroupPropertiesExecutionCourse.hasNext()) {
            groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse) iterGroupPropertiesExecutionCourse
                    .next();
            if (groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 1
                    || groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 2) {
                executionCourses.add(groupPropertiesExecutionCourse.getExecutionCourse());
            }
        }
        return executionCourses;
    }

    public void addGroupPropertiesExecutionCourse(
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {

        if (super.getGroupPropertiesExecutionCourse() == null) {
            super.setGroupPropertiesExecutionCourse(new ArrayList());
            super.getGroupPropertiesExecutionCourse().add(groupPropertiesExecutionCourse);
        } else {
            super.getGroupPropertiesExecutionCourse().add(groupPropertiesExecutionCourse);
        }

    }

    public void removeGroupPropertiesExecutionCourse(
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        super.getGroupPropertiesExecutionCourse().remove(groupPropertiesExecutionCourse);
    }

}
