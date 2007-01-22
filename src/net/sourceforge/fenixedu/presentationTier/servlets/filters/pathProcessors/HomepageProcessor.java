package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.homepage.Homepage;

public class HomepageProcessor extends PathProcessor {

    public static final String PREFIX1 = "homepage";
    public static final String PREFIX2 = "pessoal";
    
    public HomepageProcessor(String forwardURI) {
        super(forwardURI);
    }

    public HomepageProcessor add(SectionProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new HomepageContext(parentContext, getForwardURI());
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String current = provider.current();
        
        if (!current.equalsIgnoreCase(PREFIX1) && !current.equalsIgnoreCase(PREFIX2)) {
            return false;
        }
        
        if (! provider.hasNext()) {
            return false;
        }
        
        provider.next();
        String userId = provider.current();
        
        HomepageContext ownContext = (HomepageContext) context;
        ownContext.setUserId(userId);
        
        return ownContext.getHomepage() != null;
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
        if (provider.hasNext()) {
            return false;
        }
        else {
            HomepageContext ownContext = (HomepageContext) context;
            return doForward(context, new Object[] { "show", ownContext.getHomepage().getIdInternal() });
        }
    }

    public static class HomepageContext extends ProcessingContext implements SiteContext {

        private String contextURI;
        private String userId;
        private Homepage homepage;
        
        public HomepageContext(ProcessingContext parent, String contextURI) {
            super(parent);
            
            this.contextURI = contextURI;
        }

        public void setUserId(String userId) {
            this.homepage = null;
            this.userId = userId;
        } 
        
        public Homepage getHomepage() {
            if (this.homepage != null) {
                return homepage;
            }
            
            for (User user : RootDomainObject.getInstance().getUsers()) {
                String userUId = user.getUserUId();
                if (userUId == null) {
                    continue;
                }
                
                if (userUId.equalsIgnoreCase(this.userId)) {
                    Homepage homepage = user.getPerson().getHomepage();
                    
                    if (homepage != null && homepage.getActivated() != null && homepage.getActivated()) {
                        return this.homepage = homepage;
                    }
                }
            }
            
            return null;
        }

        public Site getSite() {
            return getHomepage();
        }

        public String getSiteBasePath() {
            return String.format(this.contextURI, "%s", getHomepage().getIdInternal());
        }
    }
}
