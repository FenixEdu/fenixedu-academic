package pt.utl.ist.renderers.producers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DocumentationProducerFactory {
    private static DocumentationProducerFactory instance = new DocumentationProducerFactory();
    
    private Map<String, DocumentationProducer> producers;
    
    protected DocumentationProducerFactory() {
        this.producers = new HashMap<String, DocumentationProducer>();
        
        this.producers.put("stdout", new StandardOutputProducer());
        this.producers.put("html", new HtmlProducer());
        //this.producers.put("wiki", new StandardOutputProducer());
    }
    
    public static DocumentationProducerFactory getInstance() {
        return DocumentationProducerFactory.instance;
    }
    
    public DocumentationProducer getProducer(String type) {
        return this.producers.get(type);
    }
    
    public Set<String> getAvailableTypes() {
        return this.producers.keySet();
    }
}
