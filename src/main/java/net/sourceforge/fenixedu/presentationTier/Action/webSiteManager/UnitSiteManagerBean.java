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
package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.io.Serializable;

public class UnitSiteManagerBean implements Serializable {

    /**
     * Serial Version id.
     */
    private static final long serialVersionUID = 1L;

    private String alias;
    private String idNumber;

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();

        if (this.alias.length() == 0) {
            this.alias = null;
        }
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();

        if (this.idNumber.length() == 0) {
            this.idNumber = null;
        }
    }

}