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
package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import org.joda.time.LocalDate;

public class FenixPeriod {

    String start;
    String end;

    public FenixPeriod() {
        this((String) null, (String) null);
    }

    public FenixPeriod(final String start, final String end) {
        this.start = start;
        this.end = end;
    }

    public FenixPeriod(final LocalDate start, final LocalDate end) {
        this.start = start == null ? null : start.toString("yyyy-MM-dd");
        this.end = end == null ? null : end.toString("yyyy-MM-dd");
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

}
