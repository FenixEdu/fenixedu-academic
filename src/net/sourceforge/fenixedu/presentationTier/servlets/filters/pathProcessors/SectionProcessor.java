package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Section;

public class SectionProcessor extends SiteElementPathProcessor {

    public SectionProcessor(String forwardURI) {
        super(forwardURI);
    }

    public SectionProcessor add(ItemProcessor processor) {
        addChild(processor);
        return this;
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new SectionContext(parentContext);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        SectionContext ownContext = (SectionContext) context;

        int i;
        for (i = 0; ; i++) {
            String current = provider.peek(i);
            
            if (! ownContext.addSection(current)) {
                break;
            }
        }

        while (i > 1) {
            provider.next();
            i--;
        }
        
        return !ownContext.getSections().isEmpty();
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider)
            throws IOException, ServletException {
        if (provider.hasNext()) {
            return false;
        }
        else {
            SectionContext ownContext = (SectionContext) context;
            Section section = ownContext.getLastSection();
            
            if (section == null) {
                return false;
            }
            
            ExecutionCourse executionCourse = ownContext.getExecutionCourse();
            return doForward(context, executionCourse.getIdInternal(), section.getIdInternal());
        }
    }

    public static class SectionContext extends ProcessingContext {

        private List<Section> sections;
        
        public SectionContext(ProcessingContext parent) {
            super(parent);
            
            this.sections = new ArrayList<Section>();
        }

        public ExecutionCourse getExecutionCourse() {
            return ((ExecutionCourseContext) super.getParent()).getExecutionCourse();
        }
        
        public boolean addSection(String name) {
            for (Section section : getCurrentPossibilities()) {
                String pathName = getElementPathName(section);
                
                if (pathName == null) {
                    continue;
                }
                
                if (pathName.equalsIgnoreCase(name)) {
                    addSection(section);
                    return true;
                }
            }
            
            return false;
        }
        
        private List<Section> getCurrentPossibilities() {
            Section section = getLastSection();
            
            if (section == null) {
                ExecutionCourse executionCourse = getExecutionCourse();
                if (executionCourse == null) {
                    return Collections.emptyList();
                }
                
                ExecutionCourseSite site = executionCourse.getSite();
                return site.getAssociatedSections(null);
            }
            else {
                return section.getAssociatedSections();
            }
        }

        public Section getLastSection() {
            List<Section> sections = getSections();
            
            if (sections.isEmpty()) {
                return null;
            }
            else {
                return sections.get(sections.size() - 1);
            }
        }

        public void addSection(Section section) {
            this.sections.add(section);
        }

        public List<Section> getSections() {
            return this.sections;
        }
        
    }
    
    public static String getSectionPath(Section section) {
        if (section == null) {
            return "";
        }
        else {
            return getSectionPath(section.getSuperiorSection()) + "/" + getElementPathName(section);
        }
    }
    
    public static String getSectionAbsolutePath(ExecutionCourse executionCourse, Section section) {
        return ExecutionCourseProcessor.getExecutionCourseAbsolutePath(executionCourse) + getSectionPath(section);
    }

}
