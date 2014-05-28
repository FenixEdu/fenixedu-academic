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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class QuestionHeader implements Serializable {

    private String title;

    private String[] scaleHeaders;

    private String toolTip;

    public QuestionHeader(String title) {
        this.title = title;
    }

    public QuestionHeader(String title, String... scaleHeaders) {
        this(title);
        this.scaleHeaders = scaleHeaders;
    }

    public String getTitle() {
        return title;
    }

    public String[] getScaleHeaders() {
        return scaleHeaders;
    }

    public boolean hasScaleHeaders() {
        return scaleHeaders != null;
    }

    public int getScaleHeadersCount() {
        return scaleHeaders != null ? scaleHeaders.length : 1;
    }

    public String getToolTip() {
        return toolTip;
    }

    public QuestionHeader setToolTip(String toolTip) {
        this.toolTip = toolTip;
        return this;
    }

}
