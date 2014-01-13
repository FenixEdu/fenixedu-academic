package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.DomainObject;
import de.nava.informa.core.ChannelExporterIF;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemGuidIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.exporters.RSS_2_0_Exporter;
import de.nava.informa.impl.basic.ItemGuid;

public abstract class InformaRSSAction extends FenixAction {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final String encoding = System.getProperty("file.encoding", Charset.defaultCharset().name());
        final ChannelIF channel = getRSSChannel(request);
        if (channel != null) {
            response.setContentType("text/xml; charset=" + encoding);
            final PrintWriter writer = response.getWriter();
            final ChannelExporterIF exporter = new RSS_2_0_Exporter(writer, encoding);
            exporter.write(channel);
            response.flushBuffer();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_NOT_FOUND));
            response.getWriter().close();
        }
        return null;
    }

    protected abstract ChannelIF getRSSChannel(HttpServletRequest request) throws Exception;

    protected static ItemGuidIF getItemGuidIF(final ItemIF itemIF, final DomainObject domainObject) {
        final ItemGuidIF itemGuidIF = new ItemGuid(itemIF);
        itemGuidIF.setLocation(domainObject.getClass().getName() + domainObject.getExternalId());
        itemGuidIF.setPermaLink(false);
        return itemGuidIF;
    }

}
