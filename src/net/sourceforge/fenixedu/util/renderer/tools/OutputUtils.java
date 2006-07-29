/*
 StatCvs - CVS statistics generation 
 Copyright (C) 2002  Lukasz Pekacki <lukasz@pekacki.de>
 http://statcvs.sf.net/

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 $RCSfile$ 
 Created on $Date$ 
 */
package net.sourceforge.fenixedu.util.renderer.tools;

import java.awt.Color;

/**
 * Utility class for output related stuff.
 * 
 * @author Richard Cyganiak
 * @version $Id$
 */
public class OutputUtils {

    private static final String MAGIC_SEED_1 = "0 Ax-!";

    private static final String MAGIC_SEED_2 = "!Z x5";

    /**
     * Returns a distinct <code>Color</code> for a <code>String</code>
     * argument. The algorithm tries to provide different colors for similar
     * strings, and will return equal colors for equal strings. The colors will
     * all have similar brightness and maximum intensity. Useful for chart
     * coloring.
     * 
     * @param s
     *            a <code>String</code> to get a color for
     * @return a distinct <code>Color</code> for a <code>String</code>
     *         argument. The algorithm tries to provide different colors for
     *         similar strings, and will return equal colors for equal strings.
     *         The colors will all have similar brightness and maximum
     *         intensity. Useful for chart coloring.
     */
    public static Color getStringColor(String s) {
        double d = (MAGIC_SEED_1 + s + MAGIC_SEED_2).hashCode();
        d -= Integer.MIN_VALUE;
        d /= ((double) Integer.MAX_VALUE - (double) Integer.MIN_VALUE);
        d *= 3;
        if (d < 1) {
            int i = (int) (d * 256);
            return new Color(255 - i, i, 0);
        } else if (d < 2) {
            int i = (int) ((d - 1) * 256);
            return new Color(0, 255 - i, i);
        } else {
            int i = (int) ((d - 2) * 256);
            return new Color(i, 0, 255 - i);
        }
    }

    /**
     * Escapes HTML meta characters "&", " <", ">" and turns "\n" line breaks
     * into HTML line breaks ("<br/>
     * ");
     * 
     * @param text
     *            some string, for example "x > 0 && y < 100"
     * @return HTML-escaped string, for example "x &gt; 0 &amp;&amp; y &lt; 100"
     */
    public static String escapeHtml(String text) {
        String result = text.replaceAll("&", "&amp;");
        result = result.replaceAll("<", "&lt;");
        result = result.replaceAll(">", "&gt;");
        result = result.replaceAll("\n", "<br/>\n");
        return result;
    }
}