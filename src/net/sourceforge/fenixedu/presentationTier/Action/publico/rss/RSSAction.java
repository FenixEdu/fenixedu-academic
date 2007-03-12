package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.DomainObject;

import com.sun.syndication.feed.synd.SyndEntryImpl;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemGuidIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;

public abstract class RSSAction extends InformaRSSAction {

    public static class SyndEntryFenixImpl extends SyndEntryImpl {

	private final DomainObject domainObject;

	public SyndEntryFenixImpl(final DomainObject domainObject) {
	    super();
	    this.domainObject = domainObject;
	}

	public ItemGuidIF getItemGuidIF(final ItemIF itemIF) {
	    return InformaRSSAction.getItemGuidIF(itemIF, domainObject);
	}
    }

    @Override
    protected ChannelIF getRSSChannel(final HttpServletRequest request) throws Exception {
        final ChannelBuilder builder = new ChannelBuilder();
        
        final ChannelIF channel = builder.createChannel(getFeedTitle(request));
        channel.setDescription(getFeedDescription(request));
        channel.setLocation(new URL(getFeedLink(request)));

        for (final SyndEntryFenixImpl syndEntry : getFeedEntries(request)) {
            final ItemIF item = new Item();
            item.setTitle(syndEntry.getTitle());
            item.setDescription(syndEntry.getDescription().getValue());
            item.setCreator(syndEntry.getAuthor());
            item.setDate(syndEntry.getUpdatedDate());
            item.setFound(syndEntry.getPublishedDate());
            item.setGuid(syndEntry.getItemGuidIF(item));
            channel.addItem(item);
        }

        return channel;
    }

    protected abstract List<SyndEntryFenixImpl> getFeedEntries(HttpServletRequest request) throws Exception;

    protected abstract String getFeedTitle(HttpServletRequest request) throws Exception;

    protected abstract String getFeedDescription(HttpServletRequest request) throws Exception;

    protected abstract String getFeedLink(HttpServletRequest request) throws Exception;

}
