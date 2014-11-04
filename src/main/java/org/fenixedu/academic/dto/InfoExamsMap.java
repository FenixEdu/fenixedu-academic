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
 * Created on Apr 2, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class InfoExamsMap extends InfoObject implements Serializable {

    List<InfoExecutionCourse> executionCourses;

    List curricularYears;

    Calendar startSeason1;

    Calendar endSeason1;

    Calendar startSeason2;

    Calendar endSeason2;

    InfoExecutionDegree infoExecutionDegree;

    InfoExecutionPeriod infoExecutionPeriod;

    public InfoExamsMap() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InfoExamsMap) {
            InfoExamsMap infoExamsMap = (InfoExamsMap) obj;
            return this.getExecutionCourses().size() == infoExamsMap.getExecutionCourses().size()
                    && this.getCurricularYears().size() == infoExamsMap.getCurricularYears().size();
        }

        return false;
    }

    /**
     * @return
     */
    public List getCurricularYears() {
        return curricularYears;
    }

    /**
     * @return
     */
    public Calendar getEndSeason1() {
        return endSeason1;
    }

    /**
     * @return
     */
    public Calendar getEndSeason2() {
        return endSeason2;
    }

    /**
     * @return
     */
    public List<InfoExecutionCourse> getExecutionCourses() {
        return executionCourses;
    }

    /**
     * @return
     */
    public Calendar getStartSeason1() {
        return startSeason1;
    }

    /**
     * @return
     */
    public Calendar getStartSeason2() {
        return startSeason2;
    }

    /**
     * @param list
     */
    public void setCurricularYears(List list) {
        curricularYears = list;
    }

    /**
     * @param calendar
     */
    public void setEndSeason1(Calendar calendar) {
        endSeason1 = calendar;
    }

    /**
     * @param calendar
     */
    public void setEndSeason2(Calendar calendar) {
        endSeason2 = calendar;
    }

    /**
     * @param list
     */
    public void setExecutionCourses(List<InfoExecutionCourse> list) {
        executionCourses = list;
    }

    /**
     * @param calendar
     */
    public void setStartSeason1(Calendar calendar) {
        startSeason1 = calendar;
    }

    /**
     * @param calendar
     */
    public void setStartSeason2(Calendar calendar) {
        startSeason2 = calendar;
    }

    /**
     * @return Returns the infoExecutionDegree.
     */
    public InfoExecutionDegree getInfoExecutionDegree() {
        return infoExecutionDegree;
    }

    /**
     * @return Returns the infoExecutionDegree.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }

    /**
     * @param infoExecutionDegree
     *            The infoExecutionDegree to set.
     */
    public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
        this.infoExecutionDegree = infoExecutionDegree;
    }

    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }
}