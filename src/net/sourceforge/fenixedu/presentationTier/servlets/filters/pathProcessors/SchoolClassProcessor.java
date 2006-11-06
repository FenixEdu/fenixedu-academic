package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ScheduleProcessor.ScheduleContext;

public class SchoolClassProcessor extends PathProcessor {

    public SchoolClassProcessor(String forwardURI) {
        super(forwardURI);
    }

    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new SchoolClassContext(parentContext);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String current = provider.current();
        
        SchoolClassContext ownContext = (SchoolClassContext) context;
        ownContext.setSchoolClassName(current);
        
        return ownContext.getSchoolClass() != null;
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider)
            throws IOException, ServletException {
        if (provider.hasNext()) {
            return false;
        }
        else {
            SchoolClassContext ownContext = (SchoolClassContext) context;
            SchoolClass schoolClass = ownContext.getSchoolClass();
            
            doForward(context,
                    schoolClass.getIdInternal(),
                    schoolClass.getNome(),
                    schoolClass.getExecutionDegree().getDegreeCurricularPlan().getIdInternal(),
                    schoolClass.getExecutionDegree().getDegreeCurricularPlan().getName(),
                    schoolClass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getIdInternal(),
                    schoolClass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla());
            return true;
        }
    }

    public static class SchoolClassContext extends ProcessingContext {

        private String schoolClassName;
        private SchoolClass schoolClass;
        
        public SchoolClassContext(ProcessingContext parent) {
            super(parent);
        }

        @Override
        public ScheduleContext getParent() {
            return (ScheduleContext) super.getParent();
        }

        public String getSchoolClassName() {
            return this.schoolClassName;
        }

        public void setSchoolClassName(String schoolClassName) {
            this.schoolClassName = schoolClassName;
        }
        
        public SchoolClass getSchoolClass() {
            if (this.schoolClass != null) {
                return this.schoolClass;
            }
            
            String schoolClassName = getSchoolClassName();
            
            if (schoolClassName == null) {
                return null;
            }
            
            ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
            for (SchoolClass schoolClass : executionPeriod.getSchoolClasses()) {
                if (schoolClass.getNome().equalsIgnoreCase(schoolClassName)) {
                    return this.schoolClass = schoolClass;
                }
            }
            
            return null;
        }
        
    }
}
