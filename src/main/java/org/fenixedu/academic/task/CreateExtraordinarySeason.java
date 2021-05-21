package org.fenixedu.academic.task;

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.commons.i18n.LocalizedString;

import java.util.Locale;

public class CreateExtraordinarySeason extends CustomTask {

    public void runTask() {
        final LocalizedString acronym = new LocalizedString.Builder().with(Locale.forLanguageTag("pt-PT"), "EX")
                .with(Locale.forLanguageTag("en-GB"), "ES").build();
        new EvaluationSeason(acronym, BundleUtil.getLocalizedString(Bundle.ENUMERATION, "EXTRAORDINARY_SEASON"), false, false,
                false, false, true);
    }

}

