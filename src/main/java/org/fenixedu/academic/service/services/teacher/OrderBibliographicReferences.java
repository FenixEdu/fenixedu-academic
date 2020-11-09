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
package org.fenixedu.academic.service.services.teacher;

import java.util.List;

import org.fenixedu.academic.domain.BibliographicReference;
import org.fenixedu.academic.domain.ExecutionCourse;

import pt.ist.fenixframework.Atomic;

/**
 * Changes the presentation order of all the bibliographic references passed to
 * match the order they have in the given list.
 * 
 * @author cfgi
 */
public class OrderBibliographicReferences {

    @Atomic
    public static void run(ExecutionCourse executionCourse, List<BibliographicReference> references) {
        for (int i = 0; i < references.size(); i++) {
            references.get(i).setReferenceOrder(i);
        }
    }

}