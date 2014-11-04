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
 * Created on Dec 19, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ResponsibleForValidator {
    /**
     * @author jpvl
     */
    public class InvalidCategory extends FenixServiceException {

    }

    /**
     * @author jpvl
     */
    public class MaxResponsibleForExceed extends FenixServiceException {
        private InfoExecutionCourse infoExecutionCourse;

        private List infoResponsiblefors;

        public MaxResponsibleForExceed(InfoExecutionCourse infoExecutionCourse, List infoResponsiblefors) {
            this.infoResponsiblefors = infoResponsiblefors;
            this.infoExecutionCourse = infoExecutionCourse;
        }

        /**
         * @return Returns the infoExecutionCourse.
         */
        public InfoExecutionCourse getInfoExecutionCourse() {
            return this.infoExecutionCourse;
        }

        /**
         * @return Returns the infoResponsiblefors.
         */
        public List getInfoResponsiblefors() {
            return this.infoResponsiblefors;
        }
    }

    private static ResponsibleForValidator validator = new ResponsibleForValidator();

    public static ResponsibleForValidator getInstance() {
        return validator;
    }

    private final int MAX_RESPONSIBLEFOR_BY_EXECUTION_COURSE = 3;

    private ResponsibleForValidator() {
    }

    public void validateResponsibleForList(Teacher teacher, ExecutionCourse executionCourse, Professorship responsibleForAdded)
            throws MaxResponsibleForExceed, InvalidCategory {

        List responsibleFors = executionCourse.responsibleFors();

        if ((!responsibleFors.contains(responsibleForAdded))
                && (responsibleFors.size() >= MAX_RESPONSIBLEFOR_BY_EXECUTION_COURSE)) {
            List infoResponsibleFors = (List) CollectionUtils.collect(responsibleFors, new Transformer() {

                @Override
                public Object transform(Object input) {
                    Professorship responsibleFor = (Professorship) input;
                    InfoProfessorship infoResponsibleFor = InfoProfessorship.newInfoFromDomain(responsibleFor);
                    return infoResponsibleFor;
                }
            });

            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
            throw new MaxResponsibleForExceed(infoExecutionCourse, infoResponsibleFors);
        }
    }
}