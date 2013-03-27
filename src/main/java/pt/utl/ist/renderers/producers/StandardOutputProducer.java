package pt.utl.ist.renderers.producers;

import pt.utl.ist.renderers.ConfigurationNode;
import pt.utl.ist.renderers.RendererNode;
import pt.utl.ist.renderers.RenderersParser;

import com.sun.javadoc.MethodDoc;

public class StandardOutputProducer implements DocumentationProducer {

    public void produce(RenderersParser parser) {
        System.out.println("Types");
        System.out.println("-----------------------------------");

        for (String type : parser.getTypes().keySet()) {
            System.out.println("Type: " + type);
            
            for (ConfigurationNode configuration : parser.getTypes().get(type)) {
                System.out.println("      Mode: " + configuration.mode);
                System.out.println("    Layout: " + configuration.layout);
                System.out.println("  Renderer: " + configuration.renderer);
                System.out.println();
            }
        }
        
        System.out.println("Layouts");
        System.out.println("-----------------------------------");

        for (String layout : parser.getLayouts().keySet()) {
            System.out.println("Layout: " + layout);
            
            for (ConfigurationNode configuration : parser.getLayouts().get(layout)) {
                System.out.println("      Mode: " + configuration.mode);
                System.out.println("      Type: " + configuration.type);
                System.out.println("  Renderer: " + configuration.renderer);
                System.out.println("  Defaults: ");
                
                for (Object key : configuration.properties.keySet()) {
                    System.out.println("      " + key + "=" + configuration.properties.getProperty((String) key));
                }
                
                System.out.println("   Comment: ");
                System.out.println("       " + configuration.comment);
                System.out.println();
            }
        }

        System.out.println("Renderers");
        System.out.println("-----------------------------------");

        for (String renderer : parser.getRenderers().keySet()) {
            System.out.println("Renderer: " + renderer);
            
            RendererNode node = parser.getRenderers().get(renderer);
            
            System.out.println("Properties: ");
            
            for (MethodDoc doc : node.properties) {
                System.out.println("     " + doc.name() + doc.flatSignature());
            }
            
            System.out.println("   Comment: ");
            System.out.println("       " + node.doc.commentText());
            System.out.println();
        }
    }
}
