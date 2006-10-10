package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.Summary;
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
		item.setTitle(summary.getTitle().getContent(Language.pt));
		item.setDate(summary.getLastModifiedDate());
		item.setDescription(summary.getSummaryText().getContent(Language.pt));
	}

    @Override
    public String getIdPrefix() {
        return "s";
    }

}