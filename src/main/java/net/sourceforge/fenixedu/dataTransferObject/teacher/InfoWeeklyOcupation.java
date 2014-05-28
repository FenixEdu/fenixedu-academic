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
 * Created on 7/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoWeeklyOcupation extends InfoObject implements ISiteComponent {

    private InfoTeacher infoTeacher;

    private Integer research;

    private Integer management;

    private Integer lecture;

    private Integer support;

    private Integer other;

    private Date lastModificationDate;

    public InfoWeeklyOcupation() {
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @return Returns the other.
     */
    public Integer getOther() {
        return other;
    }

    /**
     * @param other
     *            The other to set.
     */
    public void setOther(Integer other) {
        this.other = other;
    }

    /**
     * @return Returns the research.
     */
    public Integer getResearch() {
        return research;
    }

    /**
     * @param research
     *            The research to set.
     */
    public void setResearch(Integer research) {
        this.research = research;
    }

    /**
     * @return Returns the management.
     */
    public Integer getManagement() {
        return management;
    }

    /**
     * @param management
     *            The management to set.
     */
    public void setManagement(Integer management) {
        this.management = management;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoWeeklyOcupation) {
            resultado = getInfoTeacher().equals(((InfoWeeklyOcupation) obj).getInfoTeacher());
        }
        return resultado;
    }

    /**
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate
     *            The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    /**
     * @return Returns the lecture.
     */
    public Integer getLecture() {
        return lecture;
    }

    /**
     * @param lecture
     *            The lecture to set.
     */
    public void setLecture(Integer lecture) {
        this.lecture = lecture;
    }

    /**
     * @return Returns the support.
     */
    public Integer getSupport() {
        return support;
    }

    /**
     * @param support
     *            The support to set.
     */
    public void setSupport(Integer support) {
        this.support = support;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    public void copyFromDomain(WeeklyOcupation weeklyOcupation) {
        super.copyFromDomain(weeklyOcupation);
        if (weeklyOcupation != null) {
            setLecture(weeklyOcupation.getLecture());
            setManagement(weeklyOcupation.getManagement());
            setOther(weeklyOcupation.getOther());
            setResearch(weeklyOcupation.getResearch());
            setSupport(weeklyOcupation.getSupport());
            setLastModificationDate(weeklyOcupation.getLastModificationDate());
        }
    }

    public static InfoWeeklyOcupation newInfoFromDomain(WeeklyOcupation weeklyOcupation) {
        InfoWeeklyOcupation infoWeeklyOcupation = null;
        if (weeklyOcupation != null) {
            infoWeeklyOcupation = new InfoWeeklyOcupation();
            infoWeeklyOcupation.copyFromDomain(weeklyOcupation);
        }

        return infoWeeklyOcupation;
    }
}