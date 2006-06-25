package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.utl.ist.fenix.tools.util.StringAppender;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;

public class GenerateAnnoucementsRSS extends RSSAction{
	
 	@Override
	protected ChannelIF getRSSChannel(final HttpServletRequest request) throws Exception {
		final String id = request.getParameter("id");
		final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(Integer.valueOf(id));

		final String executionCourseName = executionCourse.getNome();
		ChannelBuilder builder = new ChannelBuilder();
    	ChannelIF channel = builder.createChannel(executionCourseName);
    	channel.setDescription("Anúncios da disciplina " + executionCourseName);

		final String appContext = PropertiesManager.getProperty("app.context");
        final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
        final String commonLocalUrl = StringAppender.append(context,
        		"/publico/viewSite.do?method=announcements&objectCode=",
        		executionCourse.getSite().getIdInternal().toString(),
        		"&executionPeriodOID=",
        		executionCourse.getExecutionPeriod().getIdInternal().toString(),
        		"#");

    	for (final Announcement announcement : executionCourse.getSite().getAssociatedAnnouncements()) {
			final ItemIF item = new Item();
			item.setTitle(announcement.getTitle());
			item.setDate(announcement.getLastModifiedDate());
			item.setDescription(announcement.getInformation());
			final String localUrl = commonLocalUrl + announcement.getIdInternal().toString();
			item.setLink(new URL(request.getScheme(), request.getServerName(), request.getServerPort(), localUrl));
			channel.addItem(item);
		}

    	return channel;
	}

}
