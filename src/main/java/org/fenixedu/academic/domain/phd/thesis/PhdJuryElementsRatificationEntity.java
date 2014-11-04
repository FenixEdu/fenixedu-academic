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
package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.Locale;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum PhdJuryElementsRatificationEntity {
    BY_COORDINATOR {

        @Override
        public String getRatificationEntityMessage(final PhdThesisProcess process, final Locale locale) {
            String message = BundleUtil.getString(Bundle.PHD, "message.phd.thesis.ratification.entity." + getName());

            String phdProgramName = process.getIndividualProgramProcess().getPhdProgram().getName().getContent();
            String whenJuryDesignated = process.getWhenJuryDesignated().toString("dd/MM/yyyy");
            String personName = process.getPerson().getName();

            return String.format(message, phdProgramName, whenJuryDesignated, personName);
        }
    },

    BY_SCIENTIC_COUNCIL {

        @Override
        public String getRatificationEntityMessage(PhdThesisProcess process, final Locale locale) {
            String message = BundleUtil.getString(Bundle.PHD, "message.phd.thesis.ratification.entity." + getName());

            String phdProgramName = process.getIndividualProgramProcess().getPhdProgram().getName().getContent();
            String whenJuryDesignated = process.getWhenJuryDesignated().toString("dd/MM/yyyy");
            String personName = process.getPerson().getName();

            return String.format(message, phdProgramName, whenJuryDesignated, personName);
        }
    },

    BY_RECTORATE {

        @Override
        public String getRatificationEntityMessage(PhdThesisProcess process, final Locale locale) {
            String message = BundleUtil.getString(Bundle.PHD, "message.phd.thesis.ratification.entity." + getName());

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
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return BundleUtil.getString(Bundle.PHD, locale, getQualifiedName());
    }

    private String getQualifiedName() {
        return PhdJuryElementsRatificationEntity.class.getSimpleName() + "." + name();
    }

    public abstract String getRatificationEntityMessage(final PhdThesisProcess process, Locale locale);
}
