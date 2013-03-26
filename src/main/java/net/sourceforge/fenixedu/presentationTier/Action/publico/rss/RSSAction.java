package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import pt.ist.fenixframework.DomainObject;

import com.sun.syndication.feed.synd.SyndEntryImpl;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemGuidIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;

public abstract class RSSAction extends InformaRSSAction {

    // private static Pattern sanitizeInputForXml = Pattern
    // .compile("[^\\u0009\

    private static Pattern sanitizeInputForXml = Pattern
            .compile("[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\uD800\uDC00-\uDBFF\uDFFF]");

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

    private String sanitizeStringForXml(final String xml) {
        return sanitizeInputForXml.matcher(xml).replaceAll("");
    }

    @Override
    protected ChannelIF getRSSChannel(final HttpServletRequest request) throws Exception {
        final ChannelBuilder builder = new ChannelBuilder();

        final ChannelIF channel = builder.createChannel(getFeedTitle(request));
        channel.setDescription(getFeedDescription(request));
        channel.setLocation(new URL(getFeedLink(request)));
        String siteLocation = getSiteLocation(request);
        if (siteLocation != null) {
            channel.setSite(new URL(siteLocation));
        }

        for (final SyndEntryFenixImpl syndEntry : getFeedEntries(request)) {
            final ItemIF item = new Item();
            item.setTitle(syndEntry.getTitle());
            item.setDescription(sanitizeStringForXml(syndEntry.getDescription().getValue()));
            item.setCreator(syndEntry.getAuthor());
            item.setDate(syndEntry.getUpdatedDate());
            item.setFound(syndEntry.getPublishedDate());

            if (syndEntry.getLink() != null) {
                item.setLink(new URL(syndEntry.getLink()));
            }

            item.setGuid(syndEntry.getItemGuidIF(item));
            channel.addItem(item);
        }

        return channel;
    }

    protected abstract List<SyndEntryFenixImpl> getFeedEntries(HttpServletRequest request) throws Exception;

    protected abstract String getFeedTitle(HttpServletRequest request) throws Exception;

    protected abstract String getFeedDescription(HttpServletRequest request) throws Exception;

    protected abstract String getFeedLink(HttpServletRequest request) throws Exception;

    protected abstract String getSiteLocation(HttpServletRequest request) throws Exception;

}
