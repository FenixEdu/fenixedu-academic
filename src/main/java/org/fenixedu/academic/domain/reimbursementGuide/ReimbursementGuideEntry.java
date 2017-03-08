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
 * Created on 18/Mar/2004
 *  
 */

package org.fenixedu.academic.domain.reimbursementGuide;

import java.math.BigDecimal;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * This class contains all the information regarding a Reimbursement Guide
 * Entry. <br>
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */

public class ReimbursementGuideEntry extends ReimbursementGuideEntry_Base {

    public ReimbursementGuideEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setGuideEntry(null);
        setReimbursementGuide(null);
        setReimbursementTransaction(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public Double getValue() {
        return getValueBigDecimal().doubleValue();
    }

    @Deprecated
    public void setValue(Double value) {
        setValueBigDecimal(BigDecimal.valueOf(value));
    }

}
