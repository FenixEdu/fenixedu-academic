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
 * Created on 22/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoOldPublication extends InfoObject {
    private OldPublicationType oldPublicationType;

    private String oldPublicationTypeString;

    private Date lastModificationDate;

    private String publication;

    private InfoTeacher infoTeacher;

    /**
     *  
     */
    public InfoOldPublication() {
        super();
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
     * @return Returns the oldPublicationType.
     */
    public OldPublicationType getOldPublicationType() {
        return oldPublicationType;
    }

    /**
     * @param oldPublicationType
     *            The oldPublicationType to set.
     */
    public void setOldPublicationType(OldPublicationType oldPublicationType) {
        this.oldPublicationType = oldPublicationType;
        this.oldPublicationTypeString = oldPublicationType.getName();
    }

    /**
     * @return Returns the publication.
     */
    public String getPublication() {
        return publication;
    }

    /**
     * @param publication
     *            The publication to set.
     */
    public void setPublication(String publication) {
        this.publication = publication;
    }

    /**
     * @return Returns the oldPublicationTypeString.
     */
    public String getOldPublicationTypeString() {
        return oldPublicationTypeString;
    }

    /**
     * @param oldPublicationTypeString
     *            The oldPublicationTypeString to set.
     */
    public void setOldPublicationTypeString(String oldPublicationTypeString) {
        this.oldPublicationTypeString = oldPublicationTypeString;
        this.oldPublicationType = OldPublicationType.getEnum(oldPublicationTypeString);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    public void copyFromDomain(OldPublication oldPublication) {
        super.copyFromDomain(oldPublication);

        if (oldPublication != null) {
            setLastModificationDate(oldPublication.getLastModificationDate());
            setOldPublicationType(oldPublication.getOldPublicationType());
            setOldPublicationTypeString(oldPublication.getOldPublicationType().toString());
            setPublication(oldPublication.getPublication());
        }
    }

    public static InfoOldPublication newInfoFromDomain(OldPublication oldPublication) {
        InfoOldPublication infoOldPublication = null;
        if (oldPublication != null) {
            infoOldPublication = new InfoOldPublication();
            infoOldPublication.copyFromDomain(oldPublication);
        }
        return infoOldPublication;
    }
}