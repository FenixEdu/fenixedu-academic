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
package net.sourceforge.fenixedu.presentationTier.Action.student.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType;

import org.joda.time.DateTime;

public class DeclarationBean implements Serializable {

    /**
     * Serial version is.
     */
    private static final long serialVersionUID = 1L;

    private ThesisVisibilityType visibility;
    private DateTime availableAfter;

    public DeclarationBean(Thesis thesis) {
        super();

        final ThesisVisibilityType visibility = thesis.getVisibility();
        setVisibility(visibility == null ? ThesisVisibilityType.PUBLIC : visibility);
        setAvailableAfter(thesis.getDocumentsAvailableAfter());
    }

    public ThesisVisibilityType getVisibility() {
        return this.visibility;
    }

    public void setVisibility(ThesisVisibilityType visibility) {
        this.visibility = visibility;
    }

    public DateTime getAvailableAfter() {
        return this.availableAfter;
    }

    public void setAvailableAfter(DateTime availableAfter) {
        this.availableAfter = availableAfter;
    }

}
