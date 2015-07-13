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
package org.fenixedu.academic.domain.serviceRequests;

import java.util.Locale;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.joda.time.DateTime;

/**
 * Declares API for service request
 *
 */
public interface IAcademicServiceRequest {
    
    public Person getPerson();

    public AcademicProgram getAcademicProgram();
    
    public CycleType getRequestedCycle();
    
    /* request variations */
    
    public boolean isRequestedWithCycle();
    
    public boolean isDetailed();
    
    public boolean isUrgentRequest();
    
    public Locale getLanguage();
    
    public DateTime getRequestDate();
    
    public Integer getNumberOfUnits();
    
    public Integer getNumberOfPages();
    
    public String getDescription();

    public boolean isRequestForRegistration();

    public boolean isRequestForPhd();

    public RegistryCode getRegistryCode();

    public boolean hasRegistryCode();
}
