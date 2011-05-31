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
