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
/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.student.ExternalRegistrationDataBean;
import net.sourceforge.fenixedu.domain.student.ExternalRegistrationData;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalRegistrationDataFactoryExecutor {

    @SuppressWarnings("serial")
    public static class ExternalRegistrationDataEditor extends ExternalRegistrationDataBean implements FactoryExecutor,
            Serializable {

        public ExternalRegistrationDataEditor(ExternalRegistrationData externalRegistrationData) {
            super(externalRegistrationData);
        }

        @Override
        public Object execute() {
            getExternalRegistrationData().edit(this);
            return null;
        }

    }

}
