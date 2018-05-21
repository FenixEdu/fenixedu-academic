/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.alumni;

import org.fenixedu.academic.domain.accessControl.NotUpdatedAlumniInfoForSpecificDaysGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.messaging.core.domain.Sender;
import pt.ist.fenixframework.Atomic;

import java.io.Serializable;

public class AlumniInfoNotUpdatedBean implements Serializable {

    private Integer daysNotUpdated;
    private Boolean professionalInfo;
    private Boolean formationInfo;
    private Boolean personalDataInfo;

    public AlumniInfoNotUpdatedBean(Integer daysNotUpdated, Boolean professionalInfo, Boolean formationInfo) {
        setDaysNotUpdated(daysNotUpdated);
        setProfessionalInfo(professionalInfo);
        setFormationInfo(formationInfo);
    }

    public AlumniInfoNotUpdatedBean() {
    }

    public Integer getDaysNotUpdated() {
        return daysNotUpdated;
    }

    public void setDaysNotUpdated(Integer daysNotUpdated) {
        this.daysNotUpdated = daysNotUpdated;
    }

    public Boolean getProfessionalInfo() {
        return professionalInfo;
    }

    public void setProfessionalInfo(Boolean professionalInfo) {
        this.professionalInfo = professionalInfo;
    }

    public Boolean getFormationInfo() {
        return formationInfo;
    }

    public void setFormationInfo(Boolean formationInfo) {
        this.formationInfo = formationInfo;
    }

    public Boolean getPersonalDataInfo() {
        return personalDataInfo;
    }

    public void setPersonalDataInfo(Boolean personalDataInfo) {
        this.personalDataInfo = personalDataInfo;
    }

    @Atomic
    public void createRecipientGroup(Sender sender) {
        Group recipientsGroup =
                NotUpdatedAlumniInfoForSpecificDaysGroup.get(getDaysNotUpdated(), getProfessionalInfo(), getFormationInfo(),
                        getPersonalDataInfo());
        sender.addRecipient(recipientsGroup);
    }
}
