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
package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import java.util.Locale;

/**
 * The {@link AdministrativeOfficeType} was used as a selector for the {@link AdministrativeOffice} from a given context (e.g. a
 * Degree, an
 * AcademicServiceRequest, etc). Offices now have a direct link for the {@link AcademicProgram}s they manage, rendering this class
 * useless. To select
 * the office of your context you must traverse to the associated {@link AcademicProgram}.
 */
@Deprecated
public enum AdministrativeOfficeType {

    DEGREE, MASTER_DEGREE;

    @Deprecated
    public String getName() {
        return name();
    }

    @Deprecated
    public String getQualifiedName() {
        return AdministrativeOfficeType.class.getSimpleName() + "." + name();
    }

    @Deprecated
    public String getFullyQualifiedName() {
        return AdministrativeOfficeType.class.getName() + "." + name();
    }

    @Deprecated
    public String getDescription() {
        return ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale()).getString(getQualifiedName());
    }

    @Deprecated
    public AdministrativeOffice getAdministrativeOffice() {
        return AdministrativeOffice.readByAdministrativeOfficeType(this);
    }

}
