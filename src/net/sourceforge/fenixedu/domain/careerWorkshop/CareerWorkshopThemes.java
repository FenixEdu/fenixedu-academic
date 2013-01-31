package net.sourceforge.fenixedu.domain.careerWorkshop;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum CareerWorkshopThemes {

	RESUME(),

	LABOUR_MARKET_STRATEGIES(),

	RECRUITMENT_TECHNIQUES(),

	CAREER_MANAGEMENT();

	private CareerWorkshopThemes() {
	}

	public String getQualifiedName() {
		return getClass().getSimpleName() + "." + name();
	}

	public String getDescription() {
		return getDescription(Language.getLocale());
	}

	public String getDescription(final Locale locale) {
		return ResourceBundle.getBundle("resources.StudentResources", locale).getString(getQualifiedName());
	}

	static public Map<Integer, CareerWorkshopThemes> getEmptyRankings() {
		Map<Integer, CareerWorkshopThemes> rankings = new HashMap<Integer, CareerWorkshopThemes>();
		for (int i = 0; i < getTotalOptions(); i++) {
			rankings.put(i, null);
		}
		return rankings;
	}

	static public int getTotalOptions() {
		return CareerWorkshopThemes.values().length;
	}

}
