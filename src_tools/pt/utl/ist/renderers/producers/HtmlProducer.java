package pt.utl.ist.renderers.producers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import pt.utl.ist.renderers.ConfigurationNode;
import pt.utl.ist.renderers.RendererNode;
import pt.utl.ist.renderers.RenderersParser;
import pt.utl.ist.renderers.template.Template;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

public class HtmlProducer implements DocumentationProducer {
    private static final String DOCUMENT_PROPERTIES = "/pt/utl/ist/renderers/html/document.properties";
    
    private static final String TYPES_PAGE = "types.html";
    private static final String LAYOUTS_PAGE = "layouts.html";
    private static final String RENDERERS_PAGE = "renderers.html";
    
    private Properties templates;
    
    private void initialiseProperties() {
        this.templates = new Properties();

        InputStream stream = getClass().getResourceAsStream(DOCUMENT_PROPERTIES);
        if (stream == null) {
            throw new RuntimeException("could not load document properties from " + DOCUMENT_PROPERTIES);
        }
        
        try {
            this.templates.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("error while loading document properties from " + DOCUMENT_PROPERTIES, e);
        }
    }
    
    private String getDocumentProperty(String name) {
        if (this.templates == null) {
            initialiseProperties();
        }
        
        if (! this.templates.containsKey(name)) {
            throw new RuntimeException("template '" + name + "' was not defined in the document properties '" + DOCUMENT_PROPERTIES + "'");
        }
        
        return this.templates.getProperty(name).trim();        
    }
    
    private Template createTemplate(String name) {
        String page = getDocumentProperty(name);
        String prefix = getDocumentProperty("package");
        
        return new Template(prefix + page);
    }
    
    public void produce(RenderersParser parser) {
        Template main      = createTemplate("main");
        Template header    = createTemplate("header");
        Template footer    = createTemplate("footer");
        Template cssPrint  = createTemplate("css-print");
        Template cssScreen = createTemplate("css-screen");
        
        main.addAttribute("css-print", cssPrint);
        main.addAttribute("css-screen", cssScreen);
        main.addAttribute("header", header);
        main.addAttribute("footer", footer);

        header.addAttribute("types", TYPES_PAGE);
        header.addAttribute("layouts", LAYOUTS_PAGE);
        header.addAttribute("renderers", RENDERERS_PAGE);
        
        footer.addAttribute("time", String.valueOf(new Date()));

        generateTypes(parser, main, header, footer);
        main.removeAttribute("title");
        main.removeAttribute("body");
        
        generateLayouts(parser, main, header, footer);
        main.removeAttribute("title");
        main.removeAttribute("body");
        
        generateRenderers(parser, main, header, footer);
        main.removeAttribute("title");
        main.removeAttribute("body");
    }

    private void generateTypes(RenderersParser parser, Template main, Template header, Template footer) {
        main.addAttribute("title", "Renderers Documentation: Types");

        Hashtable<String, List<ConfigurationNode>> typesTable = parser.getTypes();
        List<String> types = new ArrayList<String>(typesTable.keySet());
        Collections.sort(types);
        
        for (String type : types) {
            Template typeTemplate = createTemplate("types");
            
            typeTemplate.addAttribute("type", makeAnchor(type, type));
            
            for (ConfigurationNode node : typesTable.get(type)) {
                Template layoutTemplate = createTemplate("types-layouts");

                layoutTemplate.addAttribute("layout", makeLink(LAYOUTS_PAGE, node.layout, node.layout));
                layoutTemplate.addAttribute("mode", node.mode);
                layoutTemplate.addAttribute("renderer", makeLink(RENDERERS_PAGE, node.renderer, node.renderer));
                layoutTemplate.addAttribute("comment", node.comment);
                
                for (Object property : node.properties.keySet()) {
                    Template defaultsTemplate = createTemplate("layouts-defaults");

                    defaultsTemplate.addAttribute("property", (String) property);
                    defaultsTemplate.addAttribute("value", escapeHtml(node.properties.getProperty((String) property)));
                    
                    layoutTemplate.addAttribute("defaults", defaultsTemplate);
                }
                
                typeTemplate.addAttribute("layouts", layoutTemplate);
            }
            
            main.addAttribute("body", typeTemplate);
        }
        
        // save
        try {
            main.save(parser.getConfiguration().getDestination() + "/" + TYPES_PAGE);
        } catch (IOException e) {
            throw new RuntimeException("could not save types document", e);
        }        
    }

    private void generateLayouts(RenderersParser parser, Template main, Template header, Template footer) {
        main.addAttribute("title", "Renderers Documentation: Layouts");

        Hashtable<String, List<ConfigurationNode>> layoutsTable = parser.getLayouts();
        List<String> layouts = new ArrayList<String>(layoutsTable.keySet());
        Collections.sort(layouts);
        
        for (int i=0; i<layouts.size(); i++) {
            if (layouts.get(i).equals("default")) {
                layouts.add(0, layouts.remove(i));
            }
        }
        
        for (String layout : layouts) {
            Template layoutTemplate = createTemplate("layouts");
            
            layoutTemplate.addAttribute("layout", makeAnchor(layout, layout));

            for (ConfigurationNode node : layoutsTable.get(layout)) {
                Template typeTemplate =createTemplate("layouts-types");

                typeTemplate.addAttribute("type", makeLink(TYPES_PAGE, node.type, node.type));
                typeTemplate.addAttribute("mode", node.mode);
                typeTemplate.addAttribute("renderer", makeLink(RENDERERS_PAGE, node.renderer, node.renderer));
                typeTemplate.addAttribute("comment", node.comment);
                
                for (Object property : node.properties.keySet()) {
                    Template defaultsTemplate = createTemplate("layouts-defaults");

                    defaultsTemplate.addAttribute("property", (String) property);
                    defaultsTemplate.addAttribute("value", escapeHtml(node.properties.getProperty((String) property)));
                    
                    typeTemplate.addAttribute("defaults", defaultsTemplate);
                }
                
                layoutTemplate.addAttribute("types", typeTemplate);
            }

            main.addAttribute("body", layoutTemplate);
        }
        
        // save
        try {
            main.save(parser.getConfiguration().getDestination() + "/" + LAYOUTS_PAGE);
        } catch (IOException e) {
            throw new RuntimeException("could not save layouts document", e);
        }
    }

    private void generateRenderers(RenderersParser parser, Template main, Template header, Template footer) {
        main.addAttribute("title", "Renderers Documentation: Renderers");

        Hashtable<String, RendererNode> renderersTable = parser.getRenderers();
        List<String> renderers = new ArrayList<String>(renderersTable.keySet());
        Collections.sort(renderers);
        
        for (String renderer : renderers) {
            RendererNode node = renderersTable.get(renderer);
            Template renderersTemplate = createTemplate("renderers");

            renderersTemplate.addAttribute("renderer", 
                    makeAnchor(node.doc.qualifiedTypeName(), makeCvsLink(node.doc.qualifiedTypeName())));
            renderersTemplate.addAttribute("comment", getPlainComment(parser.getRootDoc(), node.doc));
            
            List<MethodDoc> properties = mergeProperties(node);
            for (MethodDoc property : properties) {
                Template propertiesTemplate = createTemplate("renderers-properties");
            
                propertiesTemplate.addAttribute("signature", generateSignature(property));
                propertiesTemplate.addAttribute("comment", getPlainComment(parser.getRootDoc(), property));
                
                renderersTemplate.addAttribute("properties", propertiesTemplate);
            }
            
            main.addAttribute("body", renderersTemplate);
        }
                
        // save
        try {
            main.save(parser.getConfiguration().getDestination() + "/" + RENDERERS_PAGE);
        } catch (IOException e) {
            throw new RuntimeException("could not save renderers document", e);
        }
    }

    private String generateSignature(MethodDoc property) {
        String name = property.name();
        
        return name.substring(3, 4).toLowerCase() + name.substring(4) + property.flatSignature();
    }

    private List<MethodDoc> mergeProperties(RendererNode node) {
        if (node == null) {
            return new ArrayList<MethodDoc>();
        }
        
        List<MethodDoc> properties = mergeProperties(node.parent);
        for (MethodDoc property : node.properties) {
            if (property.overriddenMethod() != null) {
                properties.remove(property.overriddenMethod());
            }
            
            properties.add(property);
        }
        
        return properties;
    }

    private String getPlainComment(RootDoc root, Doc doc) {
        StringBuilder builder = new StringBuilder();

        Tag[] tags = doc.inlineTags();
        for (int i = 0; i < tags.length; i++) {
            Tag tag = tags[i];
            
            if (tag.name().equals("Text")) {
                builder.append(tag.text());
            }
            else {
                builder.append(parseTag(root, doc, tag));
            }
        }
        
        return builder.toString();
    }
    
    /**
     * TODO: find how to do this with taglets 
     */
    private String parseTag(RootDoc root, Doc doc, Tag tag) {
        String name = tag.name();
        String text = tag.text();

        if (name.startsWith("@link")) { // @link, @linkplain
            String linkText = null;

            int sharpIndex = text.indexOf("#");
            if (sharpIndex != -1) {
                int parentesesIndex = text.indexOf('(');
                int spaceIndex = text.indexOf(' ', '#');
                
                if (spaceIndex < parentesesIndex && spaceIndex != -1) {
                    return text.substring(spaceIndex);
                }
                else {
                    parentesesIndex = text.indexOf(')', parentesesIndex);
                    
                    linkText = text.substring(parentesesIndex + 1);
                }
            }
            else {
                int spaceIndex = text.trim().indexOf(' ');
                
                if (spaceIndex != -1) {
                    linkText = text.substring(spaceIndex);
                }
            }
            
            String finalText;
            if (linkText != null && linkText.trim().length() > 0) {
                finalText = linkText;
            }
            else {
                finalText = text;
            }
            
            if (name.startsWith("@linkplain")) {
                return finalText;
            }
            else {
                return "<code>" + finalText + "</code>";
            }
        }
        
        if (name.equals("@literal")) {
            return escapeHtml(text);
        }
        
        if (name.equals("@code")) {
            return "<code>" + escapeHtml(text) + "</code>";
        }

        if (name.equals("@value")) {
            int sharpIndex = text.indexOf("#");
            if (sharpIndex != -1) {
                String className = text.substring(0, sharpIndex);
                String fieldName = text.substring(sharpIndex + 1).trim();
                
                if (className.trim().length() > 0) {
                    return text;
                }

                ClassDoc classDoc;
                
                if (doc instanceof ClassDoc) {
                    classDoc = (ClassDoc) doc;
                }
                else if (doc instanceof MethodDoc) {
                    classDoc = ((MethodDoc) doc).containingClass();
                }
                else {
                    classDoc = null;
                }
                
                if (classDoc == null) {
                    return text;
                }
                
                FieldDoc[] fields = classDoc.fields();
                for (int i = 0; i < fields.length; i++) {
                    FieldDoc fieldDoc = fields[i];
                    
                    if (fieldDoc.name().equals(fieldName)) {
                        if (fieldDoc.constantValue() != null) {
                            String fieldText = escapeHtml(String.valueOf(fieldDoc.constantValue()));
                            
                            if (fieldDoc.constantValue() instanceof String) {
                                return "\"" + fieldText + "\"";
                            }
                            else {
                                return fieldText;
                            }
                        }
                        else {
                            return escapeHtml(fieldDoc.constantValueExpression());
                        }
                    }
                }
            }
            
            return text;
        }

        if (name.equals("@inheritDoc")) {
            Doc superDoc = null;
            
            if (doc instanceof ClassDoc) {
                superDoc = ((ClassDoc) doc).superclass();
            }
            else if (doc instanceof MethodDoc) {
               superDoc = ((MethodDoc) doc).overriddenMethod();
            }
            
            if (superDoc != null) {
                return getPlainComment(root, superDoc);
            }
            else {
                return "";
            }
        }

        return text;
    }

    private String makeAnchor(String name, String text) {
        return "<span id=\"" + name + "\">" + text + "</span>";
    }

    private String makeLink(String base, String target, String text) {
        return "<a href=\"" + base + "#" + target + "\">" + text + "</a>";
    }

    private String makeCvsLink(String renderer) {
        String path = renderer.replace('.', '/');
        return "<a class=\"title\" href=\"https://fenix-cvs.ist.utl.pt/cgi-bin/cvsweb.cgi/fenix/src/" + path + ".java\">" + renderer +"</a>";
    }

    private String escapeHtml(String property) {
        // '<' '>' ' ' '&' 

        return property
            .replace("&", "&amp;") // must appear first
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace(" ", "&nbsp;");
    }

}
