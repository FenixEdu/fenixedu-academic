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
/*
 * Created on 2003/07/30
 * 
 *  
 */
package org.fenixedu.academic.service.services.resourceAllocationManager;

import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.dto.InfoShift;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadShiftByOID {

    @Atomic
    public static InfoShift run(final String oid) {
        final Shift shift = FenixFramework.getDomainObject(oid);
        return shift == null ? null : InfoShift.newInfoFromDomain(shift);
    }

}