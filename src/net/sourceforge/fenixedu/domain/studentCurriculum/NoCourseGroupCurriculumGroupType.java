package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

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
        return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
        return StringAppender.append(getClass().getSimpleName(), ".", name());
    }

}
