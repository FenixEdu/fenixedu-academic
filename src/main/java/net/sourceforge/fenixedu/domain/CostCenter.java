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
 * Created on 1/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author T�nia Pous�o
 * 
 */
public class CostCenter extends CostCenter_Base {

    public CostCenter() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CostCenter(String code, String departament, String section1, String section2) {
        this();
        setCode(code);
        setDepartament(departament);
        setSection1(section1);
        setSection2(section2);
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    public static CostCenter readByCode(final String code) {
        for (final CostCenter costCenter : Bennu.getInstance().getCostCentersSet()) {
            if (costCenter.getCode().equals(code)) {
                return costCenter;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EmployeeHistoric> getSalaryCostCenterAssociatedEmployeeHistorics() {
        return getSalaryCostCenterAssociatedEmployeeHistoricsSet();
    }

    @Deprecated
    public boolean hasAnySalaryCostCenterAssociatedEmployeeHistorics() {
        return !getSalaryCostCenterAssociatedEmployeeHistoricsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EmployeeHistoric> getMailingCostCenterAssociatedEmployeeHistorics() {
        return getMailingCostCenterAssociatedEmployeeHistoricsSet();
    }

    @Deprecated
    public boolean hasAnyMailingCostCenterAssociatedEmployeeHistorics() {
        return !getMailingCostCenterAssociatedEmployeeHistoricsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EmployeeHistoric> getWorkingPlaceCostCenterAssociatedEmployeeHistorics() {
        return getWorkingPlaceCostCenterAssociatedEmployeeHistoricsSet();
    }

    @Deprecated
    public boolean hasAnyWorkingPlaceCostCenterAssociatedEmployeeHistorics() {
        return !getWorkingPlaceCostCenterAssociatedEmployeeHistoricsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDepartament() {
        return getDepartament() != null;
    }

    @Deprecated
    public boolean hasSection2() {
        return getSection2() != null;
    }

    @Deprecated
    public boolean hasSection1() {
        return getSection1() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

}
