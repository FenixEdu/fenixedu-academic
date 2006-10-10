package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

public abstract class RSSAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        try {
            SyndFeed feed = new SyndFeedImpl();

            feed.setFeedType(this.getFeedType());

            feed.setTitle(this.getFeedTitle(request));
            feed.setDescription(this.getFeedDescription(request));
            feed.setLink(this.getFeedLink(request));
            feed.setDescription(this.getFeedDescription(request));
            feed.setEntries(this.getFeedEntries(request));

            final SyndFeedOutput output = new SyndFeedOutput();

            output.output(feed, response.getWriter());
            response.flushBuffer();
            response.getWriter().close();
        } catch (Exception e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    

    protected abstract List<SyndEntry> getFeedEntries(HttpServletRequest request) throws Exception;

    protected abstract String getFeedType();

    protected abstract String getFeedTitle(HttpServletRequest request) throws Exception;

    protected abstract String getFeedDescription(HttpServletRequest request) throws Exception;

    protected abstract String getFeedLink(HttpServletRequest request) throws Exception;

}
