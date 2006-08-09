package pt.utl.ist.renderers.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Template {
    private String resource;

    private Map<String, List<AttributeValue>> attributes;

    public Template(String resource) {
        this.resource = resource;

        this.attributes = new HashMap<String, List<AttributeValue>>();
    }

    protected String getResource() {
        return this.resource;
    }

    public void addAttribute(String attribute, String value) {
        addAttributeValue(attribute, new StringAttributeValue(attribute, value));
    }

    public void addAttribute(String attribute, Template template) {
        addAttributeValue(attribute, new TemplateAttributeValue(attribute, template));
    }

    protected void addAttributeValue(String attribute, AttributeValue value) {
        if (hasAttribute(attribute)) {
            this.attributes.get(attribute).add(value);
        } else {
            List<AttributeValue> values = new ArrayList<AttributeValue>();

            values.add(value);
            this.attributes.put(attribute, values);
        }
    }

    public List<AttributeValue> getAttributeValues(String attribute) {
        return this.attributes.get(attribute);
    }

    public boolean hasAttribute(String attribute) {
        return this.attributes.containsKey(attribute);
    }

    public void removeAttribute(String attribute) {
        this.attributes.remove(attribute);
    }

    public Reader getContentReader() {
        InputStream stream = getClass().getResourceAsStream(getResource());

        if (stream == null) {
            throw new RuntimeException("could not find resource " + getResource());
        }

        return new TemplateReader(this, stream);
    }

    public void save(String filename) throws IOException {
        File file = new File(filename);

        BufferedReader reader = new BufferedReader(getContentReader());
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line + System.getProperty("line.separator"));
        }
        
        reader.close();
        writer.close();
    }
}
