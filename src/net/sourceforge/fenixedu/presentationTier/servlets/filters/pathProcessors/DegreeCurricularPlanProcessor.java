package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ExecutionCoursesProcessor.ExecutionCoursesContext;

public class DegreeCurricularPlanProcessor extends PathProcessor {

    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new Context(parentContext);
    }

    public DegreeCurricularPlanProcessor add(ExecutionCourseProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String current = provider.current();

        Context ownContext = (Context) context;
        ownContext.setCurricularPlanYear(current);
        
        return ownContext.getDegreeCurricularPlan() != null;
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider)
            throws IOException {
        return false;
    }

    public static class Context extends ProcessingContext implements DegreeCurricularPlanContext {

        private String curricularPlanYear;
        private DegreeCurricularPlan curricularPlan;
        
        public Context(ProcessingContext parent) {
            super(parent);
        }

        @Override
        public ExecutionCoursesContext getParent() {
            return (ExecutionCoursesContext) super.getParent();
        }

        public String getCurricularPlanYear() {
            return this.curricularPlanYear;
        }

        public void setCurricularPlanYear(String curricularPlanYear) {
            this.curricularPlanYear = curricularPlanYear;
        }

        public Degree getDegree() {
            return getParent().getDegree();
        }
        
        public DegreeCurricularPlan getDegreeCurricularPlan() {
            if (this.curricularPlan != null) {
                return this.curricularPlan;
            }
            
            String year = getCurricularPlanYear();
            if (year == null) {
                return null;
            }
            
            if (! year.matches("\\p{Digit}{4}(-\\p{Digit}{4})?")) {
                return null;
            }
            
            String[] parts = year.split("-");
            Integer initialYear = new Integer(parts[0]);
            
            List<DegreeCurricularPlan> plans = new ArrayList<DegreeCurricularPlan>();
            for (DegreeCurricularPlan curricularPlan : getDegree().getDegreeCurricularPlans()) {
                if (curricularPlan.getInitialDateYearMonthDay().getYear() == initialYear) {
                    if (!curricularPlan.getState().equals(DegreeCurricularPlanState.CONCLUDED)
                            && !curricularPlan.getState().equals(DegreeCurricularPlanState.PAST)) {
                        plans.add(curricularPlan);
                    }
                }
            }

            if (plans.isEmpty()) {
                return null;
            }
            
            return this.curricularPlan = plans.get(0);
        }
        
    }
}
