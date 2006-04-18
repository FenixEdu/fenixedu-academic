package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;

public class GenerateAnnoucementsRSS extends RSSAction{
	
 
	@Override
	protected ChannelIF getRSSChannel(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		Object[] args = {ExecutionCourse.class, Integer.valueOf(id)};
		ExecutionCourse executionCourse = (ExecutionCourse) ServiceManagerServiceFactory.executeService(null, "ReadDomainObject", args);
		ChannelBuilder builder = new ChannelBuilder();
    	ChannelIF channel = builder.createChannel(executionCourse.getNome());
    	channel.setDescription("Anúncios da disciplina " + executionCourse.getNome());
    	for (Announcement announcement : executionCourse.getSite().getAssociatedAnnouncements()) {
			ItemIF item = new Item();
			item.setTitle(announcement.getTitle());
			item.setDate(announcement.getLastModifiedDate());
			item.setDescription(announcement.getInformation());
			String appContext = PropertiesManager.getProperty("app.context");
            String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
			item.setLink(new URL(request.getScheme(), request.getServerName(), request.getServerPort(), context + "/publico/viewSite.do?method=announcements&objectCode=" + executionCourse.getSite().getIdInternal() + "&executionPeriodOID=" + executionCourse.getExecutionPeriod().getIdInternal() + "#" + announcement.getIdInternal()));
			channel.addItem(item);
		}
    	return channel;
	}

}
