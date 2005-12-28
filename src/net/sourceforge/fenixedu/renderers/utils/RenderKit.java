package net.sourceforge.fenixedu.renderers.utils;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu.renderers.Renderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.exceptions.NoRendererException;
import net.sourceforge.fenixedu.renderers.exceptions.NoSuchSchemaException;
import net.sourceforge.fenixedu.renderers.schemas.Schema;

public class RenderKit {

    private static RenderKit instance = new RenderKit();

    private Map<RenderMode, RendererRegistry> registryMap;
    
    private SchemaRegistry schemaRegistry;
    
    //
    // construct
    //
    
    private RenderKit() {
        registryMap = new Hashtable<RenderMode, RendererRegistry>();
        
        Iterator<RenderMode> iterator = RenderMode.getAllModes().iterator();
        while (iterator.hasNext()) {
            RenderMode mode = iterator.next();
            
            registryMap.put(mode, new RendererRegistry());
        }
        
        schemaRegistry = new SchemaRegistry();
    }

    public static RenderKit getInstance() {
        return instance;
    }

    //
    // register renderer and schema
    //
    
    public void registerRenderer(RenderMode mode, Class type, String layout, Class<? extends Renderer> renderer, Properties defaultProperties) {
        registryMap.get(mode).registerRenderer(type, layout, renderer, defaultProperties);
    }

    public void registerSchema(Schema schema) {
        schemaRegistry.registerSchema(schema);
    }

    //
    // find
    // 
    
    /**
     * @exception NoSuchSchemaException if the schema named <tt>schemaName</tt> could not be found
     */
    public Schema findSchema(String schemaName) {
        return schemaRegistry.getSchema(schemaName);
    }

    /**
     * @exception NoRendererException if no renderer description could be found
     */
    private RendererDescription getRendererDescription(RenderMode mode, Class type, String layout) {
        return registryMap.get(mode).getRenderDescription(type, layout);
    }
    
    /**
     * @exception NoRendererException if no specific renderer description could be found
     */
    private RendererDescription getSpecificRendererDescription(RenderMode mode, Class type, String layout) {
        try {
            return getRendererDescription(mode, type, layout);
        } catch (NoRendererException e) {
            return getRendererDescription(mode, type, null);
        }
    }
    
    private Renderer getRenderer(RenderMode mode, Class type, String layout) {
        RendererDescription rendererDescription = getSpecificRendererDescription(mode, type, layout);
        return rendererDescription.createRenderer();
    }

    public Renderer getRenderer(PresentationContext context, Class type, String layout) {
        return getRenderer(context.getRenderMode(), type, layout);
    }

    //
    // render
    //
    
    private void prepareRenderer(Renderer renderer, PresentationContext context) {
        renderer.setContext(context);
        
        Properties properties = context.getProperties();
        
        if (properties != null) {
            RenderUtils.setProperties(renderer, properties);
        }
    }

    /**
     * Renders the object given as <code>targetObject</code>.
     *
     * @exception NullPointerException if the targetObject is <code>null</code>
     * @exception NoRendererException if no suitable renderer could be found to present the 
     *                                <code>object</code> with the given context 
     */
    public HtmlComponent render(PresentationContext context, Object object) {
        return render(context, object, object.getClass());
    }
    
    /**
     * Usefull for presenting the <code>null</code> value of a specific type or to present an 
     * object as one of it's superclasses.
     */
    public HtmlComponent render(PresentationContext context, Object object, Class type) {
        Renderer renderer = getRenderer(context, type, context.getLayout());
        
        return renderUsing(renderer, context, object, type);
    }
    
    /**
     * Uses the given renderer to render the object. 
     */
    public HtmlComponent renderUsing(Renderer renderer, PresentationContext context, Object object, Class type) {
        prepareRenderer(renderer, context);
        
        return renderer.render(object, type);
    }
}
