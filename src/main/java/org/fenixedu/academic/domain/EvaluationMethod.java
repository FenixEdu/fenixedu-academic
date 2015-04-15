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
 * Created on 23/Abr/2003
 *
 * 
 */
package org.fenixedu.academic.domain;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Jo�o Mota
 * 
 * 
 */
public class EvaluationMethod extends EvaluationMethod_Base {

    public EvaluationMethod() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void edit(MultiLanguageString evaluationElements) {
        if (evaluationElements == null) {
            throw new NullPointerException();
        }

        setEvaluationElements(evaluationElements);
    }

    public void delete() {
        setExecutionCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    public void setEvaluationElements(MultiLanguageString evaluationElements) {
        ContentManagementLog.createLog(this.getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.curricular.evaluation.method", this.getExecutionCourse().getNome(), this
                        .getExecutionCourse().getDegreePresentationString());
        super.setEvaluationElements(evaluationElements);
    }

}
