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
 * Created on Oct 14, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 */
public class InfoInstitution extends InfoObject {
    private String name;

    public InfoInstitution() {
    }

    public InfoInstitution(String name) {
        setName(name);
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param institution
     * @return
     */
    public static InfoInstitution newInfoFromDomain(Unit institution) {
        InfoInstitution infoInstitution = null;
        if (institution != null) {
            infoInstitution = new InfoInstitution();
            infoInstitution.copyFromDomain(institution);
        }

        return infoInstitution;
    }

    public void copyFromDomain(Unit institution) {
        super.copyFromDomain(institution);
        if (institution != null) {
            setName(institution.getName());
        }
    }
}