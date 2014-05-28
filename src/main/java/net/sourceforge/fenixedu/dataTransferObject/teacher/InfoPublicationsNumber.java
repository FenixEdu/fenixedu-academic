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
 * Created on 21/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.util.PublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoPublicationsNumber extends InfoObject {
    private PublicationType publicationType;

    private Date lastModificationDate;

    private Integer national;

    private Integer international;

    private InfoTeacher infoTeacher;

    /**
     *  
     */
    public InfoPublicationsNumber() {
        super();

    }

    /**
     * @return Returns the international.
     */
    public Integer getInternational() {
        return international;
    }

    /**
     * @param international
     *            The international to set.
     */
    public void setInternational(Integer international) {
        this.international = international;
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
     * @return Returns the national.
     */
    public Integer getNational() {
        return national;
    }

    /**
     * @param national
     *            The national to set.
     */
    public void setNational(Integer national) {
        this.national = national;
    }

    /**
     * @return Returns the publicationType.
     */
    public PublicationType getPublicationType() {
        return publicationType;
    }

    /**
     * @param publicationType
     *            The publicationType to set.
     */
    public void setPublicationType(PublicationType publicationType) {
        this.publicationType = publicationType;
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    public void copyFromDomain(PublicationsNumber publicationsNumber) {
        super.copyFromDomain(publicationsNumber);
        if (publicationsNumber != null) {
            setInternational(publicationsNumber.getInternational());
            setNational(publicationsNumber.getNational());
            setPublicationType(publicationsNumber.getPublicationType());
            setLastModificationDate(publicationsNumber.getLastModificationDate());
        }
    }

    public static InfoPublicationsNumber newInfoFromDomain(PublicationsNumber publicationsNumber) {
        InfoPublicationsNumber infoPublicationsNumber = null;
        if (publicationsNumber != null) {
            infoPublicationsNumber = new InfoPublicationsNumber();
            infoPublicationsNumber.copyFromDomain(publicationsNumber);
        }
        return infoPublicationsNumber;
    }
}