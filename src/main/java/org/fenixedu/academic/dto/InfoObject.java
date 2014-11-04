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
 * Created on 4/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import pt.ist.fenixframework.DomainObject;

/**
 * @author jpvl
 */
@Deprecated
public abstract class InfoObject extends DataTranferObject {
    private String externalId;

    public InfoObject() {
    }

    public InfoObject(String externalId) {
        setExternalId(externalId);
    }

    /**
     * @return
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * @param integer
     */
    public void setExternalId(String integer) {
        externalId = integer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InfoObject) {
            InfoObject infoObject = (InfoObject) obj;
            return this.getExternalId().equals(infoObject.getExternalId());
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (this.externalId != null) {
            return this.externalId.hashCode();
        }

        return 0;
    }

    public void copyFromDomain(DomainObject domainObject) {
        if (domainObject != null) {
            setExternalId(domainObject.getExternalId());
        }
    }
}
