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
package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.util.StringFormatter;
import pt.ist.fenixWebFramework.renderers.StringRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;

/**
 * Presents a string considering that it's a person's name. So it allows you to
 * choose if only the first and last name is shown and if the middle names
 * appear as inicials.
 * 
 * @author cfgi
 */
public class PersonNameRenderer extends StringRenderer {

    private boolean firstLastOnly;
    private boolean middleNamesInicials;
    private String normalizedName;

    public boolean isFirstLastOnly() {
        return this.firstLastOnly;
    }

    public void setFirstLastOnly(boolean firstLastOnly) {
        this.firstLastOnly = firstLastOnly;
    }

    public boolean isMiddleNamesInicials() {
        return this.middleNamesInicials;
    }

    public void setMiddleNamesInicials(boolean middleNamesInicials) {
        this.middleNamesInicials = middleNamesInicials;
    }

    @Override
    public HtmlComponent render(Object object, Class type) {
        if (object != null) {
            String name = parseName((String) object);
            HtmlComponent component = super.render(name, type);

            if (isFirstLastOnly() || isMiddleNamesInicials()) {
                component.setTitle(this.normalizedName);
            }

            return component;
        } else {
            return super.render(object, type);
        }
    }

    private String parseName(String name) {
        String finalName;
        this.normalizedName = capitalize(name.toLowerCase());

        String[] allNames = this.normalizedName.split("\\p{Space}+");
        if (allNames.length == 0) {
            return "";
        }

        if (allNames.length == 1) {
            return allNames[0];
        }

        finalName = allNames[0];

        if (!isFirstLastOnly()) {
            for (int i = 1; i < allNames.length - 1; i++) {
                if (isMiddleNamesInicials()) {
                    if (Character.isUpperCase(allNames[i].charAt(0))) {
                        finalName += " " + allNames[i].substring(0, 1) + ".";
                    }
                } else {
                    finalName += " " + allNames[i];
                }
            }
        }

        finalName += " " + allNames[allNames.length - 1];

        return finalName;
    }

    private String capitalize(String text) {
        StringBuilder buffer = new StringBuilder();
        for (String part : text.split("\\p{Space}+")) {
            if (part.length() == 0) {
                continue;
            }

            if (buffer.length() > 0) {
                buffer.append(" ");
            }

            buffer.append(StringFormatter.capitalizeWord(part, false));
        }

        return buffer.toString();
    }

}
