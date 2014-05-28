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
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

public enum TypeOfProgramme {
    PROJECT, THESIS, LAB, SEMINAR, COURSES;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return AcademicServiceRequestType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return AcademicServiceRequestType.class.getName() + "." + name();
    }

    public String getDescription() {
        ResourceBundle ENUMERATION_RESOURCES = ResourceBundle.getBundle("resources/EnumerationResources", Locale.getDefault());
        return ENUMERATION_RESOURCES.getString(getFullyQualifiedName());
    }
}
