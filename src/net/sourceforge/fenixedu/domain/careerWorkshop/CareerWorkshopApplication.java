package net.sourceforge.fenixedu.domain.careerWorkshop;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class CareerWorkshopApplication extends CareerWorkshopApplication_Base {

	public CareerWorkshopApplication(Student student, CareerWorkshopApplicationEvent event) {
		super();
		if (student == null) {
			throw new DomainException("error.careerWorkshop.creatingNewApplication: Student cannot be a null value.");
		}
		setStudent(student);
		if (event == null) {
			throw new DomainException("error.careerWorkshop.creatingNewApplication: Event cannot be a null value.");
		}
		setCareerWorkshopApplicationEvent(event);
	}

	public boolean isApplicationEventOpened() {
		DateTime today = new DateTime();
		return (today.isBefore(getCareerWorkshopApplicationEvent().getBeginDate()) || today
				.isAfter(getCareerWorkshopApplicationEvent().getEndDate())) ? false : true;
	}

	public int getSession(final CareerWorkshopSessions careerWorkshopSessions) {
		final String sessions = getSessions();
		if (sessions != null) {
			final String optionName = careerWorkshopSessions.name();
			int optionIndex = sessions.indexOf(optionName);
			if (optionIndex >= 0) {
				int offset = optionIndex + optionName.length() + 1;
				int optionEnd = sessions.indexOf(';', offset);
				return Integer.parseInt(sessions.substring(offset, optionEnd));
			}
		}
		return CareerWorkshopSessions.values().length - 1;
	}

	public int getTheme(final CareerWorkshopThemes careerWorkshopThemes) {
		final String themes = getThemes();
		if (themes != null) {
			final String optionName = careerWorkshopThemes.name();
			int optionIndex = themes.indexOf(optionName);
			if (optionIndex >= 0) {
				int offset = optionIndex + optionName.length() + 1;
				int optionEnd = themes.indexOf(';', offset);
				return Integer.parseInt(themes.substring(offset, optionEnd));
			}
		}
		return CareerWorkshopThemes.values().length - 1;
	}

	@Service
	public void setSessionPreferences(String[] preferences) {
		final StringBuilder stringBuilder = new StringBuilder();
		for (final CareerWorkshopSessions careerWorkshopSessions : CareerWorkshopSessions.values()) {
			stringBuilder.append(careerWorkshopSessions.name());
			stringBuilder.append(":");
			stringBuilder.append(preferences[careerWorkshopSessions.ordinal()]);
			stringBuilder.append(";");
		}
		setSessions(stringBuilder.toString());
	}

	@Service
	public void setThemePreferences(String[] preferences) {
		final StringBuilder stringBuilder = new StringBuilder();
		for (final CareerWorkshopThemes careerWorkshopThemes : CareerWorkshopThemes.values()) {
			stringBuilder.append(careerWorkshopThemes.name());
			stringBuilder.append(":");
			stringBuilder.append(preferences[careerWorkshopThemes.ordinal()]);
			stringBuilder.append(";");
		}
		setThemes(stringBuilder.toString());
	}

	public int[] getSessionPreferences() {
		int[] preferences = new int[CareerWorkshopSessions.values().length];
		int i = 0;
		if (getSessions() != null) {
			final String[] parts = getSessions().split(";");
			for (; i < parts.length; i++) {
				final int seperatorPos = parts[i].indexOf(':');
				final CareerWorkshopSessions careerWorkshopSessions =
						CareerWorkshopSessions.valueOf(parts[i].substring(0, seperatorPos));
				preferences[careerWorkshopSessions.ordinal()] = Integer.parseInt(parts[i].substring(seperatorPos + 1));
			}
		}
		for (; i < CareerWorkshopSessions.values().length; i++) {
			preferences[i] = CareerWorkshopSessions.values().length - 1;
		}
		return preferences;
	}

	public CareerWorkshopSessions[] getSortedSessionPreferences() {
		final CareerWorkshopSessions[] result = new CareerWorkshopSessions[CareerWorkshopSessions.values().length];

		int[] preferences = getSessionPreferences();

		int index = 0;
		for (int x = 0; x < CareerWorkshopSessions.values().length; x++) {
			for (int y = 0; y < CareerWorkshopSessions.values().length; y++) {
				if (preferences[y] == x) {
					result[index] = CareerWorkshopSessions.values()[y];
					index++;
				}
			}
		}

		return result;
	}

	public int[] getThemePreferences() {
		int[] preferences = new int[CareerWorkshopThemes.values().length];
		int i = 0;
		if (getThemes() != null) {
			final String[] parts = getThemes().split(";");
			for (; i < parts.length; i++) {
				final int seperatorPos = parts[i].indexOf(':');
				final CareerWorkshopThemes careerWorkshopThemes =
						CareerWorkshopThemes.valueOf(parts[i].substring(0, seperatorPos));
				preferences[careerWorkshopThemes.ordinal()] = Integer.parseInt(parts[i].substring(seperatorPos + 1));
			}
		}
		for (; i < CareerWorkshopThemes.values().length; i++) {
			preferences[i] = CareerWorkshopThemes.values().length - 1;
		}
		return preferences;
	}

	public CareerWorkshopThemes[] getSortedThemePreferences() {
		final CareerWorkshopThemes[] result = new CareerWorkshopThemes[CareerWorkshopThemes.values().length];

		int[] preferences = getThemePreferences();

		int index = 0;
		for (int x = 0; x < CareerWorkshopThemes.values().length; x++) {
			for (int y = 0; y < CareerWorkshopThemes.values().length; y++) {
				if (preferences[y] == x) {
					result[index] = CareerWorkshopThemes.values()[y];
					index++;
				}
			}
		}

		return result;
	}

	@Service
	public void sealApplication() {
		DateTime timestamp = new DateTime();
		setSealStamp(timestamp);
		getCareerWorkshopApplicationEvent().setLastUpdate(timestamp);
	}

	public void delete() {
		removeCareerWorkshopConfirmation();
		removeCareerWorkshopApplicationEvent();
		removeStudent();
		removeRootDomainObject();
		deleteDomainObject();
	}

}
