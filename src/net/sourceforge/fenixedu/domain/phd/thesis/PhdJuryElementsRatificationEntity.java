package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdJuryElementsRatificationEntity {
	BY_COORDINATOR {

		@Override
		public String getRatificationEntityMessage(final PhdThesisProcess process, final Locale locale) {
			ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", locale);

			String message = bundle.getString("message.phd.thesis.ratification.entity." + getName());

			String phdProgramName = process.getIndividualProgramProcess().getPhdProgram().getName().getContent();
			String whenJuryDesignated = process.getWhenJuryDesignated().toString("dd/MM/yyyy");
			String personName = process.getPerson().getName();

			return String.format(message, phdProgramName, whenJuryDesignated, personName);
		}
	},

	BY_SCIENTIC_COUNCIL {

		@Override
		public String getRatificationEntityMessage(PhdThesisProcess process, final Locale locale) {
			ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", locale);

			String message = bundle.getString("message.phd.thesis.ratification.entity." + getName());

			String phdProgramName = process.getIndividualProgramProcess().getPhdProgram().getName().getContent();
			String whenJuryDesignated = process.getWhenJuryDesignated().toString("dd/MM/yyyy");
			String personName = process.getPerson().getName();

			return String.format(message, phdProgramName, whenJuryDesignated, personName);
		}
	},

	BY_RECTORATE {

		@Override
		public String getRatificationEntityMessage(PhdThesisProcess process, final Locale locale) {
			ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", locale);

			String message = bundle.getString("message.phd.thesis.ratification.entity." + getName());

			String phdProgramName = process.getIndividualProgramProcess().getPhdProgram().getName().getContent();
			String whenJuryDesignated = process.getWhenJuryDesignated().toString("dd/MM/yyyy");
			String personName = process.getPerson().getName();
			String whenJuryValidated = process.getWhenJuryValidated().toString("dd/MM/yyyy");

			return String.format(message, phdProgramName, whenJuryDesignated, personName, whenJuryValidated);
		}
	},

	CUSTOM {

		@Override
		public String getRatificationEntityMessage(PhdThesisProcess process, Locale locale) {
			return process.getRatificationEntityCustomMessage();
		}
	};

	public String getName() {
		return name();
	}

	public String getLocalizedName() {
		return getLocalizedName(Language.getLocale());
	}

	public String getLocalizedName(final Locale locale) {
		return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
	}

	private String getQualifiedName() {
		return PhdJuryElementsRatificationEntity.class.getSimpleName() + "." + name();
	}

	public abstract String getRatificationEntityMessage(final PhdThesisProcess process, Locale locale);
}
