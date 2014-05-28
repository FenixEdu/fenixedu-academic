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
 * Created on 16/Nov/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.util.ProviderRegimeType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoServiceProviderRegime extends InfoObject {
    private ProviderRegimeType providerRegimeType;

    private InfoTeacher infoTeacher;

    private Date lastModificationDate;

    public InfoServiceProviderRegime() {
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
     * @return Returns the providerRegimeType.
     */
    public ProviderRegimeType getProviderRegimeType() {
        return providerRegimeType;
    }

    /**
     * @param providerRegimeType
     *            The providerRegimeType to set.
     */
    public void setProviderRegimeType(ProviderRegimeType providerRegimeType) {
        this.providerRegimeType = providerRegimeType;
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
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    public void copyFromDomain(ServiceProviderRegime serviceProviderRegime) {
        super.copyFromDomain(serviceProviderRegime);
        if (serviceProviderRegime != null) {
            setLastModificationDate(serviceProviderRegime.getLastModificationDate());
            setProviderRegimeType(serviceProviderRegime.getProviderRegimeType());
        }
    }

    public static InfoServiceProviderRegime newInfoFromDomain(ServiceProviderRegime serviceProviderRegime) {
        InfoServiceProviderRegime infoServiceProviderRegime = null;
        if (serviceProviderRegime != null) {
            infoServiceProviderRegime = new InfoServiceProviderRegime();
            infoServiceProviderRegime.copyFromDomain(serviceProviderRegime);
        }
        return infoServiceProviderRegime;
    }
}