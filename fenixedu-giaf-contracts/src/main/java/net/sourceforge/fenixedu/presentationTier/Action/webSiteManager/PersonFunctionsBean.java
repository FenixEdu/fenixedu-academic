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
package org.fenixedu.academic.ui.struts.action.webSiteManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;

public class PersonFunctionsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Person person;
    private Unit unit;
    private List<Function> functions;

    public PersonFunctionsBean(Person person, Unit unit) {
        super();

        this.person = person;
        this.unit = unit;

        initFunctions(person);
    }

    private void initFunctions(Person person) {
        this.functions = new ArrayList<Function>();
        for (PersonFunction pf : PersonFunction.getAllActivePersonFunctions(person, getUnit())) {
            this.functions.add(pf.getFunction());
        }

        Collections.sort(this.functions, FUNCTION_COMPARATOR);
    }

    public Person getPerson() {
        return this.person;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public List<Function> getFunctions() {
        return this.functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    private static Comparator<Function> FUNCTION_COMPARATOR = new Comparator<Function>() {

        @Override
        public int compare(Function f1, Function f2) {
            Integer level1 = getUnitLevel(f1.getUnit());
            Integer level2 = getUnitLevel(f2.getUnit());

            int comparison = level1.compareTo(level2);
            if (comparison != 0) {
                return comparison;
            } else {
                Integer order1 = getOrder(f1);
                Integer order2 = getOrder(f2);

                comparison = order1.compareTo(order2);
                if (comparison != 0) {
                    return comparison;
                } else {
                    return f1.getExternalId().compareTo(f2.getExternalId());
                }
            }
        }

        private Integer getUnitLevel(Unit unit) {
            return unit.getUnitDepth();
        }

        private Integer getOrder(Function f1) {
            Integer order = f1.getFunctionOrder();
            return order == null ? Integer.MAX_VALUE : order;
        }
    };

}
