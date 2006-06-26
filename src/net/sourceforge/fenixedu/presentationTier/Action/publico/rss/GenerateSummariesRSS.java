package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Summary;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;

public class GenerateSummariesRSS extends RSSAction{
	
 
	@Override
	protected ChannelIF getRSSChannel(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(Integer.valueOf(id));
		ChannelBuilder builder = new ChannelBuilder();
    	ChannelIF channel = builder.createChannel(executionCourse.getNome());
    	channel.setDescription("Sumários da disciplina " + executionCourse.getNome());
    	for (Summary summary : executionCourse.getAssociatedSummaries()) {
			ItemIF item = new Item();
			/*item.setTitle(announcement.getTitle());
			item.setDate(announcement.getLastModifiedDate());
			item.setDescription(announcement.getInformation());*/
			String appContext = PropertiesManager.getProperty("app.context");
            String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
			item.setLink(new URL(request.getScheme(), request.getServerName(), request.getServerPort(), context + "/publico/viewSite.do?method=announcements&objectCode=" + executionCourse.getSite().getIdInternal() + "&executionPeriodOID=" + executionCourse.getExecutionPeriod().getIdInternal() + "#" /*+ announcement.getIdInternal()*/));
			channel.addItem(item);
		}
    	return channel;
	}

}
