package org.fenixedu.academic.dto.student.enrollment.bolonha;

import java.util.Collection;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

@SuppressWarnings("serial")
public class SpecialSeasonChooseEvaluationSeasonBean extends ChooseEvaluationSeasonBean {

    @Override
    public Collection<EvaluationSeason> getActiveEvaluationSeasons() {
        return EvaluationSeason.readSpecialSeasons().sorted().collect(Collectors.toSet());
    }

    @Override
    public String getFuncionalityTitle() {
        return BundleUtil.getString(Bundle.ACADEMIC, "label.special.season.enrolment");
    }

}
