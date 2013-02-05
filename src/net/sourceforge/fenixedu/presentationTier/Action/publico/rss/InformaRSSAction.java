package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
        final String encoding = System.getProperty("file.encoding", PropertiesManager.DEFAULT_CHARSET);
        final ChannelIF channel = getRSSChannel(request);
        response.setContentType("text/xml; charset=" + encoding);
        final PrintWriter writer = response.getWriter();
        final ChannelExporterIF exporter = new RSS_2_0_Exporter(writer, encoding);
        exporter.write(channel);
        response.flushBuffer();
        return null;
    }

    protected abstract ChannelIF getRSSChannel(HttpServletRequest request) throws Exception;

    protected static ItemGuidIF getItemGuidIF(final ItemIF itemIF, final DomainObject domainObject) {
        final ItemGuidIF itemGuidIF = new ItemGuid(itemIF);
        itemGuidIF.setLocation(domainObject.getClass().getName() + domainObject.getIdInternal());
        itemGuidIF.setPermaLink(false);
        return itemGuidIF;
    }

}
