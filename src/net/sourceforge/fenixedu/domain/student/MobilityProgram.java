package net.sourceforge.fenixedu.domain.student;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum MobilityProgram {

	SOCRATES,

	ERASMUS,

	MINERVA,

	COVENANT_WITH_AZORES,

	COVENANT_WITH_INSTITUTION {

		@Override
		protected String getSpecificDescription(final Locale locale) {
			return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(getQualifiedName())
					+ UniversityUnit.getInstitutionsUniversityUnit().getName();
		}

	};

	public String getQualifiedName() {
		Class<?> enumClass = this.getClass();
		if (!enumClass.isEnum() && Enum.class.isAssignableFrom(enumClass)) {
			enumClass = enumClass.getEnclosingClass();
		}

		return enumClass.getSimpleName() + "." + name();
	}

	protected String getSpecificDescription(final Locale locale) {
		return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
	}

	public String getDescription() {
		return getSpecificDescription(Language.getLocale());
	}

	public String getDescription(final Locale locale) {
		return getSpecificDescription(locale);
	}

}
