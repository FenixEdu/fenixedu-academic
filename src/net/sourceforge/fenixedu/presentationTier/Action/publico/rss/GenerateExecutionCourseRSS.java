package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.net.URL;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.utl.ist.fenix.tools.util.StringAppender;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;

public abstract class GenerateExecutionCourseRSS extends RSSAction{
	
 	@Override
	protected ChannelIF getRSSChannel(final HttpServletRequest request) throws Exception {
		final String id = request.getParameter("id");
		final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(Integer.valueOf(id));

		final String executionCourseName = executionCourse.getNome();
		final ChannelBuilder builder = new ChannelBuilder();
    	final ChannelIF channel = builder.createChannel(executionCourseName);
    	channel.setDescription(StringAppender.append(getDescriptionPrefix(), " da disciplina ", executionCourseName));

		final String appContext = PropertiesManager.getProperty("app.context");
        final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
        final String commonLocalUrl = StringAppender.append(context,
        		"/publico/executionCourse.do?method=",
        		getMethodName(),
        		"&executionCourseID=",
        		executionCourse.getIdInternal().toString(),
        		"#");

    	for (final DomainObject domainObject : ((Collection<DomainObject>) getObjects(executionCourse))) {
			final ItemIF item = new Item();
			fillItem(item, domainObject);
			final String localUrl = commonLocalUrl + getIdPrefix() + domainObject.getIdInternal().toString();
			item.setLink(new URL(request.getScheme(), request.getServerName(), request.getServerPort(), localUrl));
			channel.addItem(item);
		}

    	return channel;
	}

    public abstract String getDescriptionPrefix();

 	public abstract String getMethodName();

 	public abstract Set getObjects(final ExecutionCourse executionCourse);

	public abstract void fillItem(final ItemIF item, final DomainObject domainObject);

    public abstract String getIdPrefix();

}
