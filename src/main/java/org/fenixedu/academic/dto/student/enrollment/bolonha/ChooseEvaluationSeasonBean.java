package org.fenixedu.academic.dto.student.enrollment.bolonha;

import java.io.Serializable;
import java.util.Collection;

import org.fenixedu.academic.domain.EvaluationSeason;
import org.joda.time.LocalDate;

@SuppressWarnings("serial")
public abstract class ChooseEvaluationSeasonBean implements Serializable {

    protected EvaluationSeason evaluationSeason;
    protected LocalDate evaluationDate;

    public EvaluationSeason getEvaluationSeason() {
        return evaluationSeason;
    }

    public void setEvaluationSeason(EvaluationSeason evaluationSeason) {
        this.evaluationSeason = evaluationSeason;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public abstract Collection<EvaluationSeason> getActiveEvaluationSeasons();

    public abstract String getFuncionalityTitle();

}
