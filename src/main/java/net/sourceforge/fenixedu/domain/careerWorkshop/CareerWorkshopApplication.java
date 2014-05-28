/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.careerWorkshop;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

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

    @Atomic
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

    @Atomic
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

    @Atomic
    public void sealApplication() {
        DateTime timestamp = new DateTime();
        setSealStamp(timestamp);
        getCareerWorkshopApplicationEvent().setLastUpdate(timestamp);
    }

    public void delete() {
        setCareerWorkshopConfirmation(null);
        setCareerWorkshopApplicationEvent(null);
        setStudent(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasSessions() {
        return getSessions() != null;
    }

    @Deprecated
    public boolean hasThemes() {
        return getThemes() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCareerWorkshopApplicationEvent() {
        return getCareerWorkshopApplicationEvent() != null;
    }

    @Deprecated
    public boolean hasSealStamp() {
        return getSealStamp() != null;
    }

    @Deprecated
    public boolean hasCareerWorkshopConfirmation() {
        return getCareerWorkshopConfirmation() != null;
    }

}
