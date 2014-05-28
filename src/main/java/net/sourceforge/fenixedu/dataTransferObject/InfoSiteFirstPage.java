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
 * Created on 5/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * 
 * 
 */
public class InfoSiteFirstPage extends DataTranferObject implements ISiteComponent {

    private String alternativeSite;

    private String initialStatement;

    private String introduction;

    private List lastFiveAnnouncements;

    private List responsibleTeachers;

    private List lecturingTeachers;

    private String siteExternalId;

    /**
     * @return
     */
    public String getAlternativeSite() {
        return alternativeSite;
    }

    /**
     * @return
     */
    public String getInitialStatement() {
        return initialStatement;
    }

    /**
     * @return
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * @return
     */
    public List getLecturingTeachers() {
        return lecturingTeachers;
    }

    /**
     * @return
     */
    public List getResponsibleTeachers() {
        return responsibleTeachers;
    }

    /**
     * @param string
     */
    public void setAlternativeSite(String string) {
        alternativeSite = string;
    }

    /**
     * @param string
     */
    public void setInitialStatement(String string) {
        initialStatement = string;
    }

    /**
     * @param string
     */
    public void setIntroduction(String string) {
        introduction = string;
    }

    /**
     * @param list
     */
    public void setLecturingTeachers(List list) {
        lecturingTeachers = list;
    }

    /**
     * @param list
     */
    public void setResponsibleTeachers(List list) {
        responsibleTeachers = list;
    }

    /**
     * @return
     */
    public String getSiteExternalId() {
        return siteExternalId;
    }

    /**
     * @param integer
     */
    public void setSiteExternalId(String integer) {
        siteExternalId = integer;
    }

    /**
     * @param infoAnnouncements
     */
    public void setLastFiveAnnouncements(List infoAnnouncements) {
        lastFiveAnnouncements = infoAnnouncements;
    }

    public List getLastFiveAnnouncements() {
        return lastFiveAnnouncements;
    }

}