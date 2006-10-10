package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.nava.informa.core.ChannelExporterIF;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.exporters.RSS_2_0_Exporter;

public abstract class InformaRSSAction extends FenixAction {
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
    	final ChannelIF channel = getRSSChannel(request);
    	response.setContentType("text/xml");	    	
    	final ChannelExporterIF exporter = new RSS_2_0_Exporter(response.getWriter(), System.getProperty("file.encoding","iso-8859-1"));
    	exporter.write(channel);
    	response.flushBuffer();
    	return null;
    }
    
    protected abstract ChannelIF getRSSChannel(HttpServletRequest request) throws Exception;

}
