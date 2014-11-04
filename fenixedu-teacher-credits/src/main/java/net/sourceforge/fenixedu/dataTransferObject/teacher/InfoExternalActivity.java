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
package org.fenixedu.academic.dto.teacher;

import java.util.Date;

import org.fenixedu.academic.dto.InfoObject;
import org.fenixedu.academic.dto.InfoTeacher;
import org.fenixedu.academic.domain.teacher.ExternalActivity;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoExternalActivity extends InfoObject {

    private InfoTeacher infoTeacher;

    private String activity;

    private Date lastModificationDate;

    public InfoExternalActivity() {
    }

    /**
     * @return Returns the activity.
     */
    public String getActivity() {
        return activity;
    }

    /**
     * @param activity
     *            The activity to set.
     */
    public void setActivity(String activity) {
        this.activity = activity;
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

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoExternalActivity) {
            resultado = getInfoTeacher().equals(((InfoExternalActivity) obj).getInfoTeacher());
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.fenixedu.academic.dto.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    public void copyFromDomain(ExternalActivity externalActivity) {
        super.copyFromDomain(externalActivity);
        if (externalActivity != null) {
            setActivity(externalActivity.getActivity());
            setLastModificationDate(externalActivity.getLastModificationDate());
            setInfoTeacher(InfoTeacher.newInfoFromDomain(externalActivity.getTeacher()));
        }
    }

    public static InfoExternalActivity newInfoFromDomain(ExternalActivity externalActivity) {
        InfoExternalActivity infoExternalActivity = null;
        if (externalActivity != null) {
            infoExternalActivity = new InfoExternalActivity();
            infoExternalActivity.copyFromDomain(externalActivity);
        }
        return infoExternalActivity;
    }
}