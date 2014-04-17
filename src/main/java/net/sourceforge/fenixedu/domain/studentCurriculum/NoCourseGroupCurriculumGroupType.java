package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import java.util.Locale;

public enum NoCourseGroupCurriculumGroupType {

    PROPAEDEUTICS(CurricularRuleLevel.PROPAEUDEUTICS_ENROLMENT),

    EXTRA_CURRICULAR(CurricularRuleLevel.EXTRA_ENROLMENT),

    STANDALONE(CurricularRuleLevel.STANDALONE_ENROLMENT),

    INTERNAL_CREDITS_SOURCE_GROUP(CurricularRuleLevel.NULL_LEVEL);

    private CurricularRuleLevel level;

    private NoCourseGroupCurriculumGroupType() {
        level = null;
    }

    private NoCourseGroupCurriculumGroupType(final CurricularRuleLevel level) {
        this.level = level;
    }

    public CurricularRuleLevel getCurricularRuleLevel() {
        return level;
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

}
