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
package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Summary;
import pt.ist.fenixframework.DomainObject;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;
import de.nava.informa.core.ItemIF;

public class GenerateSummariesRSS extends GenerateExecutionCourseRSS {

    @Override
    public String getDescriptionPrefix() {
        return "Sumários";
    }

    @Override
    public String getMethodName() {
        return "summaries";
    }

    @Override
    public Set getObjects(final ExecutionCourse executionCourse) {
        return executionCourse.getAssociatedSummariesSet();
    }

    @Override
    public void fillItem(final ItemIF item, final DomainObject domainObject) {
        final Summary summary = (Summary) domainObject;
        item.setTitle(summary.getTitle().getContent(MultiLanguageString.pt));
        item.setDate(summary.getLastModifiedDate());
        item.setDescription(summary.getSummaryText().getContent(MultiLanguageString.pt));
    }

    @Override
    public String getIdPrefix() {
        return "s";
    }

}
