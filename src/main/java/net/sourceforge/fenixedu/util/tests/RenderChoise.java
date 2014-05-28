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
package net.sourceforge.fenixedu.util.tests;

public class RenderChoise extends Render {

    public static final boolean YES = true;

    public static final boolean NO = false;

    private static final String YES_STRING = "Yes";

    private static final String NO_STRING = "No";

    private boolean shuffle;

    public RenderChoise() {
        super();
    }

    public void setShuffle(String shuffle) {
        this.shuffle = getShuffleValue(shuffle);
    }

    private boolean getShuffleValue(String shuffle) {
        if (shuffle.equalsIgnoreCase(YES_STRING)) {
            return YES;
        } else if (shuffle.equalsIgnoreCase(NO_STRING)) {
            return NO;
        }
        return NO;
    }

    public static String getShuffleString(boolean shuffle) {
        if (shuffle == YES) {
            return YES_STRING;
        } else if (shuffle == NO) {
            return NO_STRING;
        }
        return NO_STRING;
    }

    private String getShuffleString() {
        if (shuffle == YES) {
            return YES_STRING;
        } else if (shuffle == NO) {
            return NO_STRING;
        }
        return NO_STRING;
    }

    @Override
    public String toXML(String inside) {
        return "<render_choice shuffle=\"" + getShuffleString() + "\">\n" + inside + "</render_choice>\n";
    }

}