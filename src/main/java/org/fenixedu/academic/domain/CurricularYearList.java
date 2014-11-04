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

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class CurricularYearList {

    private final List<Integer> curricularYears;

    public CurricularYearList(Iterable<Integer> curricularYears) {
        super();
        if (curricularYears == null) {
            throw new IllegalArgumentException("exception.null.values");
        }
        this.curricularYears = Lists.newArrayList(curricularYears);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        for (Integer year : curricularYears) {
            if (buffer.length() > 0) {
                buffer.append(',');
            }
            buffer.append(year);
        }

        return buffer.toString();
    }

    private static Splitter SPLITTER = Splitter.on(',');

    public static CurricularYearList internalize(String data) {

        Iterable<Integer> years = Iterables.transform(SPLITTER.split(data), new Function<String, Integer>() {

            @Override
            public Integer apply(String str) {
                return Integer.parseInt(str);
            }

        });

        return new CurricularYearList(years);

    }

    public List<Integer> getYears() {
        return curricularYears;
    }

    /*
     * The value -1 in the year list represents all the years of the selected
     * degree
     */
    public boolean hasAll() {
        return curricularYears.contains(-1);
    }

}
