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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Arrays;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.domain.EntryPhase;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EntryPhaseProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) source;
        if (ingressionInformationBean.getIngression() != null && ingressionInformationBean.getIngression().hasEntryPhase()) {
            return Arrays.asList(EntryPhase.values());
        }
        return new ArrayList<EntryPhase>();
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
