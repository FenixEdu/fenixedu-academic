package net.sourceforge.fenixedu.renderers.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu.renderers.exceptions.NoRendererException;

import org.apache.commons.collections.Predicate;

public class RendererRegistry {

    private ClassHierarchyTable<Map<String, RendererDescription>> renderersTable;
    
    public RendererRegistry() {
        super();

        this.renderersTable = new ClassHierarchyTable<Map<String, RendererDescription>>();
    }

    public void registerRenderer(Class type, String layout, Class renderer, Properties defaultProperties) {
        Map<String, RendererDescription> layoutsTable = this.renderersTable.getUnspecific(type);

        if (layoutsTable == null) {
            this.renderersTable.put(type, new HashMap<String, RendererDescription>());
            layoutsTable = this.renderersTable.getUnspecific(type);
        }

        layoutsTable.put(layout, new RendererDescription(renderer, defaultProperties));
    }

    public RendererDescription getRenderDescription(Class objectType, final String layout) {
        Map<String, RendererDescription> layoutsTable = renderersTable.get(objectType, new Predicate() {

            public boolean evaluate(Object table) {
                Map layoutsTable = (Map) table;
                
                return layoutsTable.get(layout) != null;
            }
            
        });
        
        if (layoutsTable == null) {
            throw new NoRendererException(objectType, layout);
        }
        
        return layoutsTable.get(layout);
    }
    
    public RendererDescription getExactRenderDescription(Class objectType, String layout) {
        Map<String, RendererDescription> layoutsTable = renderersTable.getUnspecific(objectType);
        
        if (layoutsTable == null) {
            throw new NoRendererException(objectType, layout);
        }
        
        return layoutsTable.get(layout);
    }
}
