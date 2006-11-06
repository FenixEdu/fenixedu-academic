package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.SectionProcessor.SectionContext;

public class ItemProcessor extends SiteElementPathProcessor {

    public ItemProcessor(String forwardURI) {
        super(forwardURI);
    }
    
    @Override
    public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
        return new ItemContext(parentContext);
    }

    @Override
    protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
        String current = provider.current();
        
        ItemContext ownContext = (ItemContext) context;
        ownContext.setItemName(current);
        
        return ownContext.getItem() != null;
    }

    @Override
    protected boolean forward(ProcessingContext context, PathElementsProvider provider)
            throws IOException, ServletException {
        if (provider.hasNext()) {
            return false;
        }
        else {
            ItemContext ownContext = (ItemContext) context;
            
            Item item = ownContext.getItem();
            if (item == null) {
                return false;
            }
            
            return doForward(context, 
                    ownContext.getExecutionCourse().getIdInternal(), 
                    ownContext.getSection().getIdInternal(), 
                    item.getIdInternal());
        }
    }

    public static class ItemContext extends ProcessingContext {

        private String itemName;
        private Item item;

        public ItemContext(ProcessingContext parent) {
            super(parent);
        }

        public String getItemName() {
            return this.itemName;
        }

        public void setItemName(String name) {
            this.itemName = name;
        }

        @Override
        public SectionContext getParent() {
            return (SectionContext) super.getParent();
        }

        public ExecutionCourse getExecutionCourse() {
            return getParent().getExecutionCourse();
        }
        
        public Section getSection() {
            return getParent().getLastSection();
        }

        public Item getItem() {
            if (this.item != null) {
                return this.item;
            }
            
            String name = getItemName();
            if (name == null) {
                return null;
            }
            
            Section section = getSection();
            if (section == null) {
                return null;
            }
            
            for (Item item : section.getAssociatedItems()) {
                String pathName = getElementPathName(item);

                if (pathName == null) {
                    continue;
                }
                
                if (pathName.equalsIgnoreCase(name)) {
                    return this.item = item;
                }
            }
            
            return null;
        }
        
    }
    
    public static String getItemPath(Item item) {
        return SectionProcessor.getSectionPath(item.getSection()) + "/" + getElementPathName(item);
    }
    
    public static String getItemAbsolutePath(ExecutionCourse executionCourse, Item item) {
        return SectionProcessor.getSectionAbsolutePath(executionCourse, item.getSection()) + "/" + getElementPathName(item);
    }
    
}
