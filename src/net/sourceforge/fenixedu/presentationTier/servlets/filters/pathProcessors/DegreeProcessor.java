package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Site;

public class DegreeProcessor extends PathProcessor {

    private String degreeSiteURI;
    
    public DegreeProcessor(String forwardURI, String degreeSiteURI) {
        super(forwardURI);
        
        this.degreeSiteURI = degreeSiteURI;
    }

    public DegreeProcessor add(SectionProcessor processor) {
        addChild(processor);
        return this;
    }

    public DegreeProcessor add(ExecutionCoursesProcessor processor) {
        addChild(processor);
        return this;
    }

    public DegreeProcessor add(ScheduleProcessor processor) {
        addChild(processor);
        return this;
    }
    
    public DegreeProcessor add(ExamProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parent) {
        return new DegreeContext(parent, this.degreeSiteURI);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String current = provider.current();
        Degree degree = Degree.readBySigla(current);
        
        if (degree == null) {
            return false;
        }
        else {
            DegreeContext ownContext = (DegreeContext) context;
            ownContext.setDegree(degree);
            
            return true;
        }
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
        if (! provider.hasNext()) {
            DegreeContext ownContext = (DegreeContext) context;
            return doForward(context, 
                    ExecutionPeriod.readActualExecutionPeriod().getIdInternal(),
                    ownContext.getDegree().getIdInternal());
        }
        else {
            return false;
        }
    }

    public static class DegreeContext extends ProcessingContext implements SiteContext {
        
        private String contextURI;
        
        public DegreeContext(ProcessingContext parent, String contextURI) {
            super(parent);
            
            this.contextURI = contextURI;
        }

        private Degree degree;

        public Degree getDegree() {
            return this.degree;
        }

        public void setDegree(Degree degree) {
            this.degree = degree;
        }

        public Site getSite() {
            return getDegree().getSite();
        }

        public String getSiteBasePath() {
            return String.format(this.contextURI, "%s", getDegree().getIdInternal()) ;
        }
        
    }

}
