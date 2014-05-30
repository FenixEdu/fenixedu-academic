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

        String feedTitle = getFeedTitle(request);
        if (feedTitle == null) {
            return null;
        }

        final ChannelIF channel = builder.createChannel(feedTitle);
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
