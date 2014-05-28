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
package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.erasmus;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.NationalIdCardAvoidanceQuestion;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class NationalIdCardAvoidanceProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
        return Arrays.asList(new NationalIdCardAvoidanceQuestion[] {
                NationalIdCardAvoidanceQuestion.COUNTRY_NOT_LISTED_IN_FENIX_AUTHENTICATION,
                NationalIdCardAvoidanceQuestion.ELECTRONIC_ID_CARD_CODES_UNKNOWN,
                NationalIdCardAvoidanceQuestion.ELECTRONIC_ID_CARD_SUBMISSION_AVAILABILITY_UNKNOWN,
                NationalIdCardAvoidanceQuestion.ELECTRONIC_ID_CARD_SUBMISSION_TRUST_LACK,
                NationalIdCardAvoidanceQuestion.NOT_OWNER_ELECTRONIC_ID_CARD, NationalIdCardAvoidanceQuestion.OTHER_REASON });
    }

}
