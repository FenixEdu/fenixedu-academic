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
 * Created on 8/Mai/2005 - 11:53:40
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ShiftType;
import pt.ist.fenixframework.DomainObject;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes extends InfoObject {

    private InfoPerson person;
    private InfoNonAffiliatedTeacher nonAffiliatedTeacher;
    private String teacherName;

    final private List<ShiftType> remainingClassTypes = new ArrayList<ShiftType>();
    private Boolean hasEvaluations = false;

    public InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes(InfoObject infoTeacherOrNonAffiliatedTeacher,
            InfoExecutionCourse infoExecutionCourse) {

        if (infoTeacherOrNonAffiliatedTeacher instanceof InfoPerson) {
            this.person = (InfoPerson) infoTeacherOrNonAffiliatedTeacher;
            this.nonAffiliatedTeacher = null;
            this.setExternalId(this.person.getExternalId());
            this.teacherName = this.person.getNome();

        } else if (infoTeacherOrNonAffiliatedTeacher instanceof InfoNonAffiliatedTeacher) {

            this.person = null;
            this.nonAffiliatedTeacher = (InfoNonAffiliatedTeacher) infoTeacherOrNonAffiliatedTeacher;
            this.setExternalId(this.nonAffiliatedTeacher.getExternalId());
            this.teacherName = this.nonAffiliatedTeacher.getName();
        }

        Map<ShiftType, CourseLoad> courseLoadsMap = infoExecutionCourse.getExecutionCourse().getCourseLoadsMap();

        if (courseLoadsMap.containsKey(ShiftType.TEORICA) && !courseLoadsMap.get(ShiftType.TEORICA).isEmpty()) {
            this.remainingClassTypes.add(ShiftType.TEORICA);
        }
        if (courseLoadsMap.containsKey(ShiftType.PRATICA) && !courseLoadsMap.get(ShiftType.PRATICA).isEmpty()) {
            this.remainingClassTypes.add(ShiftType.PRATICA);
        }
        if (courseLoadsMap.containsKey(ShiftType.LABORATORIAL) && !courseLoadsMap.get(ShiftType.LABORATORIAL).isEmpty()) {
            this.remainingClassTypes.add(ShiftType.LABORATORIAL);
        }
        if (courseLoadsMap.containsKey(ShiftType.TEORICO_PRATICA) && !courseLoadsMap.get(ShiftType.TEORICO_PRATICA).isEmpty()) {
            this.remainingClassTypes.add(ShiftType.TEORICO_PRATICA);
        }
        if (courseLoadsMap.containsKey(ShiftType.SEMINARY) && !courseLoadsMap.get(ShiftType.SEMINARY).isEmpty()) {
            this.remainingClassTypes.add(ShiftType.SEMINARY);
        }
        if (courseLoadsMap.containsKey(ShiftType.PROBLEMS) && !courseLoadsMap.get(ShiftType.PROBLEMS).isEmpty()) {
            this.remainingClassTypes.add(ShiftType.PROBLEMS);
        }
        if (courseLoadsMap.containsKey(ShiftType.FIELD_WORK) && !courseLoadsMap.get(ShiftType.FIELD_WORK).isEmpty()) {
            this.remainingClassTypes.add(ShiftType.FIELD_WORK);
        }
        if (courseLoadsMap.containsKey(ShiftType.TRAINING_PERIOD) && !courseLoadsMap.get(ShiftType.TRAINING_PERIOD).isEmpty()) {
            this.remainingClassTypes.add(ShiftType.TRAINING_PERIOD);
        }
        if (courseLoadsMap.containsKey(ShiftType.TUTORIAL_ORIENTATION)
                && !courseLoadsMap.get(ShiftType.TUTORIAL_ORIENTATION).isEmpty()) {
            this.remainingClassTypes.add(ShiftType.TUTORIAL_ORIENTATION);
        }
    }

    public InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes() {

    }

    /**
     * @return Returns the remainingClassTypes.
     */
    public List<ShiftType> getRemainingClassTypes() {
        return remainingClassTypes;
    }

    /**
     * @return Returns the nonAffiliatedTeacher.
     */
    public InfoNonAffiliatedTeacher getNonAffiliatedTeacher() {
        return nonAffiliatedTeacher;
    }

    /**
     * @param nonAffiliatedTeacher
     *            The nonAffiliatedTeacher to set.
     */
    public void setNonAffiliatedTeacher(InfoNonAffiliatedTeacher nonAffiliatedTeacher) {
        this.nonAffiliatedTeacher = nonAffiliatedTeacher;
    }

    /**
     * @return Returns the teacher.
     */
    public InfoPerson getPerson() {
        return person;
    }

    /**
     * @param teacher
     *            The teacher to set.
     */
    public void setPerson(InfoPerson person) {
        this.person = person;
    }

    /**
     * @return Returns the teacherName.
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * @param teacherName
     *            The teacherName to set.
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    /**
     * @return Returns the hasEvaluations.
     */
    public Boolean getHasEvaluations() {
        return hasEvaluations;
    }

    /**
     * @param hasEvaluations
     *            The hasEvaluations to set.
     */
    public void setHasEvaluations(Boolean hasEvaluations) {
        this.hasEvaluations = hasEvaluations;
    }

    @Override
    public String toString() {
        String result = "[INFOTEACHERORNONAFFILIATEDTEACHERWITHREMAININGCLASSTYPES";

        if (this.person != null) {
            result += ", " + this.person.toString();

        } else if (this.nonAffiliatedTeacher != null) {
            result += ", " + this.nonAffiliatedTeacher.toString();
        }

        result += this.remainingClassTypes.toString() + "]\n";
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (net.sourceforge.fenixedu.domain.DomainObject)
     */
    @Override
    public void copyFromDomain(DomainObject domainObject) {
        // TODO Auto-generated method stub
        super.copyFromDomain(domainObject);
    }

}
