package pt.utl.ist.renderers;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;

public class RenderersParser {
    private static final String RENDERER_CLASS_NAME = "net.sourceforge.fenixedu.renderers.Renderer";
    private static final String COMMENT_PREFIX = "@";

    private ParserConfiguration configuration;

    private Hashtable<String, RendererNode> renderers;
    private Hashtable<String, List<ConfigurationNode>> types;
    private Hashtable<String, List<ConfigurationNode>> layouts;
    
    private RootDoc rootDoc;
    
    public RenderersParser(ParserConfiguration configuration) {
        this.configuration = configuration;
        
        this.renderers = new Hashtable<String, RendererNode>();
        this.types     = new Hashtable<String, List<ConfigurationNode>>();
        this.layouts   = new Hashtable<String, List<ConfigurationNode>>();
    }
    
    public Hashtable<String, List<ConfigurationNode>> getTypes() {
        return this.types;
    }

    public Hashtable<String, List<ConfigurationNode>> getLayouts() {
        return this.layouts;
    }

    public Hashtable<String, RendererNode> getRenderers() {
        return this.renderers;
    }

    public ParserConfiguration getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(ParserConfiguration configuration) {
        this.configuration = configuration;
    }

    public RootDoc getRootDoc() {
        return this.rootDoc;
    }

    protected void setRootDoc(RootDoc rootDoc) {
        this.rootDoc = rootDoc;
    }

    public void parse(RootDoc root) {
        collectRenderers(root);
        collectLayoutsAndTypes(root);
    }

    private void collectRenderers(RootDoc root) {
        setRootDoc(root);
        ClassDoc rendererClassDoc = root.classNamed(RENDERER_CLASS_NAME);
        
        if (rendererClassDoc == null) {
            throw new RuntimeException("could not find base class: " + RENDERER_CLASS_NAME);
        }
        
        ClassDoc[] allClasses = root.classes();
        for (int i = 0; i < allClasses.length; i++) {
            ClassDoc classDoc = allClasses[i];
            
            if (classDoc.subclassOf(rendererClassDoc) || classDoc.equals(rendererClassDoc)) {
                RendererNode node = new RendererNode();
                node.doc = classDoc;

                MethodDoc[] methods = classDoc.methods();
                for (int j = 0; j < methods.length; j++) {
                    MethodDoc doc = methods[j];
                    
                    if (doc.name().startsWith("set") && doc.tags("property").length > 0) {
                        node.properties.add(doc);
                    }
                }
                
                this.renderers.put(node.doc.qualifiedName(), node);
            }
        }
        
        for (RendererNode node : this.renderers.values()) {
            if (node.doc.equals(rendererClassDoc)) {
                continue;
            }
            
            ClassDoc superDoc = node.doc.superclass();
            RendererNode superNode = this.renderers.get(superDoc.qualifiedName());
            
            node.parent = superNode;
            superNode.children.add(node);
        }
    }

    private void collectLayoutsAndTypes(RootDoc root) {
        FileInputStream input = null;
        
        final Set<Closeable> closeables = new HashSet<Closeable>();

        try {
            input = new FileInputStream(this.configuration.getConfiguration());

            closeables.add(input);

            SAXBuilder build = new SAXBuilder();
            build.setEntityResolver(new EntityResolver() {

                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    final FileInputStream fileInputStream = new FileInputStream(RenderersParser.this.configuration.getDtd());
                    closeables.add(fileInputStream);
                    return new InputSource(fileInputStream);
                }
                
            });
            
            Document document = build.build(input);
            Element rootElement = document.getRootElement();

            // used to keep the last found comment when processing a renderer element
            String lastComment = null;
            
            List contentList = rootElement.getContent();
            for (Object content : contentList) {
                if (content instanceof Comment) {
                    Comment comment = (Comment) content;

                    if (comment.getText().startsWith(COMMENT_PREFIX)) {
                        lastComment = comment.getText().substring(1);
                    }
                }
                else if (content instanceof Element) {
                    Element rendererElement = (Element) content;
                    
                    ConfigurationNode rendererConfiguration = createRendererConfiguration(lastComment, rendererElement);
                    
                    addToMapList(this.types,   rendererConfiguration.type,   rendererConfiguration);
                    addToMapList(this.layouts, rendererConfiguration.layout, rendererConfiguration);
                    
                    RendererNode node = this.renderers.get(rendererConfiguration.renderer);
                    node.uses.add(rendererConfiguration);
                    
                    lastComment = null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("unable to read configuration from " + this.configuration.getConfiguration(), e);
        }
        finally {
            IOException e = null;
            for (final Closeable closeable : closeables) {
                try {
                    closeable.close();
                } catch (IOException ex) {
                    e = ex;
                }
            }
            if (e != null) {
                throw new RuntimeException("unable to close configuration input stream", e);
            }
        }
    }

    private ConfigurationNode createRendererConfiguration(String lastComment, Element rendererElement) {
        ConfigurationNode rendererConfiguration = new ConfigurationNode();
        
        rendererConfiguration.mode     = rendererElement.getAttributeValue("mode");
        rendererConfiguration.layout   = rendererElement.getAttributeValue("layout");
        rendererConfiguration.type     = rendererElement.getAttributeValue("type");
        rendererConfiguration.renderer = rendererElement.getAttributeValue("class");
        rendererConfiguration.comment  = lastComment;

        List propertyElements = rendererElement.getChildren();
        for (Object object : propertyElements) {
            Element propertyElement = (Element) object;

            rendererConfiguration.properties.setProperty(
                    propertyElement.getAttributeValue("name"), 
                    propertyElement.getAttributeValue("value"));
        }
        
        if (rendererConfiguration.mode == null) {
            rendererConfiguration.mode = "output";
        }
        
        if (rendererConfiguration.layout == null) {
            rendererConfiguration.layout = "default";
        }
        
        if (rendererConfiguration.comment == null) {
            rendererConfiguration.comment = "";
        }
        
        return rendererConfiguration;
    }

    private void addToMapList(Hashtable<String, List<ConfigurationNode>> table, String key, ConfigurationNode value) {
        if (table.containsKey(key)) {
            table.get(key).add(value);
        } else {
            List<ConfigurationNode> list = new ArrayList<ConfigurationNode>();
            list.add(value);
            
            table.put(key, list);
        }
    }
}
