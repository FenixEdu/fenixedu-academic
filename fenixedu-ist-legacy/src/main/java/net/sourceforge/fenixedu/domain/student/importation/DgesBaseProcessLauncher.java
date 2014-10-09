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
package net.sourceforge.fenixedu.domain.student.importation;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.Atomic;

public class DgesBaseProcessLauncher {

    @Atomic
    public static DgesBaseProcess launchImportation(final ExecutionYear executionYear, final Space campus,
            final EntryPhase phase, DgesStudentImportationFile file) {
        return new DgesStudentImportationProcess(executionYear, campus, phase, file);
    }

    @Atomic
    public static ExportDegreeCandidaciesByDegreeForPasswordGeneration launchExportationCandidaciesForPasswordGeneration(
            final ExecutionYear executionYear, final EntryPhase entryPhase) {
        return new ExportDegreeCandidaciesByDegreeForPasswordGeneration(executionYear, entryPhase);
    }
}
