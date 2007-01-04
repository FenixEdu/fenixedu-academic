package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sun.syndication.feed.synd.SyndEntry;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;

public abstract class RSSAction extends InformaRSSAction {

    @Override
    protected ChannelIF getRSSChannel(final HttpServletRequest request) throws Exception {
        final ChannelBuilder builder = new ChannelBuilder();
        
        final ChannelIF channel = builder.createChannel(getFeedTitle(request));
        channel.setDescription(getFeedDescription(request));
        channel.setLocation(new URL(getFeedLink(request)));

        for (final SyndEntry syndEntry : getFeedEntries(request)) {
            final ItemIF item = new Item();
            item.setTitle(syndEntry.getTitle());
            item.setDescription(syndEntry.getDescription().getValue());
            item.setCreator(syndEntry.getAuthor());
            item.setDate(syndEntry.getUpdatedDate());
            item.setFound(syndEntry.getPublishedDate());
            //item.setLink(new URL(syndEntry.getLink()));
            channel.addItem(item);
        }

        return channel;
    }

    protected abstract List<SyndEntry> getFeedEntries(HttpServletRequest request) throws Exception;

    protected abstract String getFeedTitle(HttpServletRequest request) throws Exception;

    protected abstract String getFeedDescription(HttpServletRequest request) throws Exception;

    protected abstract String getFeedLink(HttpServletRequest request) throws Exception;

}
