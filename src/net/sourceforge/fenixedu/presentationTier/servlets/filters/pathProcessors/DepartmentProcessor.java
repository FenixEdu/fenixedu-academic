package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class DepartmentProcessor extends PathProcessor {

    public DepartmentProcessor(String forwardURI) {
        super(forwardURI);
    }

    public DepartmentProcessor add(SectionProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new DepartmentContext(parentContext, getForwardURI());
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String acronym = provider.current();
        
        DepartmentContext ownContext = (DepartmentContext) context;
        ownContext.setAcronym(acronym);
        
        return ownContext.getDepartment() != null;
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
        if (provider.hasNext()) {
            return false;
        }
        else {
            DepartmentContext ownContext = (DepartmentContext) context;
            return doForward(context, new Object[] { "presentation", ownContext.getUnit().getIdInternal() });
        }
    }

    public static class DepartmentContext extends ProcessingContext implements SiteContext {

        private String contextURI;
        private String acronym;
        private Department department;
        
        public DepartmentContext(ProcessingContext parent, String contextURI) {
            super(parent);
            
            this.contextURI = contextURI;
        }

        public void setAcronym(String acronym) {
            this.acronym = acronym;
        }

        public String getAcronym() {
            return this.acronym;
        }

        public Department getDepartment() {
            if (this.department != null) {
                return this.department;
            }
            
            if (this.acronym == null) {
                return null;
            }
            
            for (Department department : RootDomainObject.getInstance().getDepartments()) {
                if (department.getAcronym().equalsIgnoreCase(getAcronym())) {
                    Unit departmentUnit = department.getDepartmentUnit();
                    
                    if (departmentUnit != null) {
                        if (departmentUnit.hasSite()) {
                            return this.department = department;
                        }
                    }
                }
            }
            
            return null;
        }
        
        public Unit getUnit() {
            Department department = getDepartment();
            if (department == null) {
                return null;
            }
            else {
                return department.getDepartmentUnit();
            }
        }
        
        public Site getSite() {
            Department department = getDepartment();
            if (department == null) {
                return null;
            }
            
            Unit unit = department.getDepartmentUnit();
            if (unit == null) {
                return null;
            }
            
            return unit.getSite();
        }

        public String getSiteBasePath() {
            return String.format(this.contextURI, "%s", getUnit().getIdInternal());
        }
        
    }
    
    public static String getDepartmentPath(Department department) {
        return "/" + DepartmentsProcessor.PREFIX2 + "/" + department.getAcronym(); 
    }
}
