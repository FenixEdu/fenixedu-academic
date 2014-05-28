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
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class InsertCurriculum {

    @Atomic
    public static Boolean run(String curricularCourseId, String program, String programEn, String operacionalObjectives,
            String operacionalObjectivesEn, String generalObjectives, String generalObjectivesEn, DateTime lastModification,
            Boolean basic) throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseId);

        if (curricularCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Curriculum curriculumFromDB = curricularCourse.findLatestCurriculum();

        if (curriculumFromDB != null) {
            throw new InvalidArgumentsServiceException();
        }

        if (curricularCourse.getBasic().equals(basic)) {

            curricularCourse.insertCurriculum(program, programEn, operacionalObjectives, operacionalObjectivesEn,
                    generalObjectives, generalObjectivesEn, lastModification);

            return new Boolean(true);
        }

        return new Boolean(false);
    }
}