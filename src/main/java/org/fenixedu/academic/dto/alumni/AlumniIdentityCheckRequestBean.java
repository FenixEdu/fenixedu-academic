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

import org.fenixedu.academic.domain.AlumniRequestType;

public class AlumniIdentityCheckRequestBean extends AlumniErrorSendingMailBean {

    private String districtOfBirth;
    private String districtSubdivisionOfBirth;
    private String parishOfBirth;
    private AlumniRequestType requestType;

    public AlumniIdentityCheckRequestBean(AlumniRequestType requestType) {
        setRequestType(requestType);
    }

    public String getDistrictOfBirth() {
        return districtOfBirth;
    }

    public void setDistrictOfBirth(String districtOfBirth) {
        this.districtOfBirth = districtOfBirth;
    }

    public String getDistrictSubdivisionOfBirth() {
        return districtSubdivisionOfBirth;
    }

    public void setDistrictSubdivisionOfBirth(String districtSubdivisionOfBirth) {
        this.districtSubdivisionOfBirth = districtSubdivisionOfBirth;
    }

    public void setParishOfBirth(String parishOfBirth) {
        this.parishOfBirth = parishOfBirth;
    }

    public String getParishOfBirth() {
        return parishOfBirth;
    }

    public AlumniRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(AlumniRequestType requestType) {
        this.requestType = requestType;
    }
}
