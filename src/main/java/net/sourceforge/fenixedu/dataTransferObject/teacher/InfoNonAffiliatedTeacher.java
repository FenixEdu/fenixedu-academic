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
 * Created on May 3, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InfoNonAffiliatedTeacher extends InfoObject {

    protected String name;

    protected Integer keyInstitution;

    protected InfoInstitution infoInstitution;

    public InfoNonAffiliatedTeacher() {
    }

    public void copyFromDomain(NonAffiliatedTeacher nonAffiliatedTeacher) {
        super.copyFromDomain(nonAffiliatedTeacher);
        if (nonAffiliatedTeacher != null) {
            if (nonAffiliatedTeacher.getInstitutionUnit() != null) {
                InfoInstitution infoInstitution = new InfoInstitution();
                infoInstitution.copyFromDomain(nonAffiliatedTeacher.getInstitutionUnit());
                setInfoInstitution(infoInstitution);
            }
            setName(nonAffiliatedTeacher.getName());
        }
    }

    public static InfoNonAffiliatedTeacher newInfoFromDomain(NonAffiliatedTeacher naTeacher) {
        InfoNonAffiliatedTeacher infoNaTeacher = null;
        if (naTeacher != null) {
            infoNaTeacher = new InfoNonAffiliatedTeacher();
            infoNaTeacher.copyFromDomain(naTeacher);
        }
        return infoNaTeacher;
    }

    public InfoInstitution getInfoInstitution() {
        return infoInstitution;
    }

    public void setInfoInstitution(InfoInstitution infoInstitution) {
        this.infoInstitution = infoInstitution;
    }

    public Integer getKeyInstitution() {
        return keyInstitution;
    }

    public void setKeyInstitution(Integer keyInstitution) {
        this.keyInstitution = keyInstitution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String result = "[INFONONAFFILIATEDTEACHER";
        result += ", nome=" + this.getName();
        result += "]";
        return result;
    }

}
