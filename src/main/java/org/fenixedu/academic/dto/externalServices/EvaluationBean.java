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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.util.Date;

import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;

public class EvaluationBean {

    private String name;
    private String evaluationType;
    private Date begin;
    private Date end;

    public EvaluationBean(final WrittenEvaluation evaluation) {
        setName(evaluation.getName());
        setEvaluationType(evaluation.getEvaluationType().toString());
        setBegin(evaluation.getBeginningDateTime().toDate());
        setEnd(evaluation.getEndDateTime().toDate());
    }

    public EvaluationBean(final Project evaluation) {
        setName(evaluation.getName());
        setEvaluationType(evaluation.getEvaluationType().toString());
        setBegin(evaluation.getProjectBeginDateTime().toDate());
        setEnd(evaluation.getProjectEndDateTime().toDate());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }

    public String getEvaluationType() {
        return evaluationType;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getBegin() {
        return begin;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getEnd() {
        return end;
    }
}
