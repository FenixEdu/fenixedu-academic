package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum GratuityExemptionJustificationType {

    // Teachers,Researchers,Employees and Agents from institution
    INSTITUTION,

    // Institution or Institution Partners Grant Owners
    INSTITUTION_GRANT_OWNER,

    // Teachers,Researcher and Employees from Institution Partners
    OTHER_INSTITUTION,

    // PALOP Teachers
    PALOP_TEACHER,

    SON_OF_DECORATED_MILITARY,

    SOCIAL_SHARE_GRANT_OWNER,

    // Students teaching classes
    STUDENT_TEACH,

    // Directive council authorization dispatch
    DIRECTIVE_COUNCIL_AUTHORIZATION,

    // Used to mark exemptions created by separation cycles
    SEPARATION_CYCLES_AUTHORIZATION;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return GratuityExemptionJustificationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return GratuityExemptionJustificationType.class.getName() + "." + name();
    }

    public static List<GratuityExemptionJustificationType> getTypesFor(final DegreeType degreeType) {
	switch (degreeType) {
	case BOLONHA_MASTER_DEGREE:
	case BOLONHA_INTEGRATED_MASTER_DEGREE:
	case BOLONHA_DEGREE:
	case DEGREE:
	    return Arrays.asList(new GratuityExemptionJustificationType[] { SON_OF_DECORATED_MILITARY, SOCIAL_SHARE_GRANT_OWNER,
		    DIRECTIVE_COUNCIL_AUTHORIZATION, SEPARATION_CYCLES_AUTHORIZATION });
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    return Arrays.asList(new GratuityExemptionJustificationType[] { INSTITUTION, INSTITUTION_GRANT_OWNER,
		    OTHER_INSTITUTION, PALOP_TEACHER, STUDENT_TEACH, DIRECTIVE_COUNCIL_AUTHORIZATION });
	case BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA:
	    return Arrays.asList(new GratuityExemptionJustificationType[] { INSTITUTION, OTHER_INSTITUTION, PALOP_TEACHER });
	default:
	    throw new RuntimeException("Unknown degree type");
	}

    }

    public String localizedName(Locale locale) {
	return ResourceBundle.getBundle(LabelFormatter.ENUMERATION_RESOURCES, locale).getString(getQualifiedName());
    }

    public String getLocalizedName() {
	return localizedName(Language.getLocale());
    }

}
