/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CategoryProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<String> result = new ArrayList<String>();

        result.add(hardcoded("Assistente Convidado", "Invited Lecturer"));
        result.add(hardcoded("Professor Associado", "Associate Professor"));
        result.add(hardcoded("Professor Auxiliar", "Assistant Professor"));
        result.add(hardcoded("Professor Catedratico ", "Full Professor"));
        result.add(hardcoded("Professor Catedratico Convidado", "Invited Full Professor"));
        result.add(hardcoded("Professor Associado Convidado", "Invited Associate Professor"));
        result.add(hardcoded("Professor Auxiliar Convidado", "Invited Assistant Professor"));

        result.add(hardcoded("Especialista", "Especialist"));
        result.add(hardcoded("Investigador Principal", "Main Researcher"));
        result.add(hardcoded("Investigador Auxiliar", "Auxiliar Researcher"));
        result.add(hardcoded("Investigador Coordenador", "Coordinator Researcher"));

        return result;
    }

    private String hardcoded(String pt, String en) {
        return new LocalizedString(org.fenixedu.academic.util.LocaleUtils.PT, pt).with(org.fenixedu.academic.util.LocaleUtils.EN, en).getContent();
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
