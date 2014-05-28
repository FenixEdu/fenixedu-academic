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
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.GuiderType;

public class GuiderDTO extends DataTranferObject {

    private GuiderType guiderType;

    private String guiderName;

    private String guiderId;

    private String institutionName;

    public GuiderDTO(GuiderType type, String name, String id, String institutionName) {
        this.guiderType = type;
        this.guiderName = name;
        this.guiderId = id;
        this.institutionName = institutionName;
    }

    public String getGuiderName() {
        return guiderName;
    }

    public void setGuiderName(String guiderName) {
        this.guiderName = guiderName;
    }

    public String getGuiderNumber() {
        return guiderId;
    }

    public void setGuiderNumber(String guiderId) {
        this.guiderId = guiderId;
    }

    public GuiderType getGuiderType() {
        return guiderType;
    }

    public void setGuiderType(GuiderType guiderType) {
        this.guiderType = guiderType;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

}
