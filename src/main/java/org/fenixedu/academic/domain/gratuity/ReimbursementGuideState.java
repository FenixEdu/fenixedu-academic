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
 * Created on 13/Nov/2003
 *
 */
package org.fenixedu.academic.domain.gratuity;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 13/Nov/2003
 * 
 */
public enum ReimbursementGuideState {

    ISSUED,

    APPROVED,

    PAYED,

    ANNULLED;

    public String getName() {
        return name();
    }

}