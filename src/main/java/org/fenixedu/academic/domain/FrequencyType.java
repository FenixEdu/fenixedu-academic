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
package net.sourceforge.fenixedu.domain;

public enum FrequencyType {

    DAILY("D", 1), WEEKLY("S", 7), BIWEEKLY("Q", 14);

    private String abbreviation;

    private int numberOfDays;

    private FrequencyType(String abbreviation_, int ordinal) {
        setAbbreviation(abbreviation_);
        setNumberOfDays(ordinal);
    }

    public String getName() {
        return name();
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setNumberOfDays(int ordinal) {
        this.numberOfDays = ordinal;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }
}
