package pt.utl.ist.renderers.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class TemplateReader extends Reader {

    private Template template;
    private BufferedReader reader;
    private PushbackReader current;
    
    public TemplateReader(Template template, InputStream stream) {
        this.template = template;
        this.reader = new BufferedReader(new InputStreamReader(stream));
    }
    
    protected BufferedReader getReader() {
        return this.reader;
    }

    protected Template getTemplate() {
        return this.template;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        Reader reader = getCurrentReader();

        if (reader == null) {
            return -1;
        }
        
        return reader.read(cbuf, off, len);
    }
    
    protected Reader getCurrentReader() throws IOException {
        if (this.current == null || empty(this.current)) {
            this.current = new PushbackReader(getNextReader());
        }
        
        return this.current;
    }
    
    private boolean empty(PushbackReader reader) throws IOException {
        int value = reader.read();
        
        if (value != -1) {
            reader.unread(value);
        }
        
        return value == -1;
    }

    protected Reader getNextReader() throws IOException {
        ChainReader reader = new ChainReader();

        String line = getReader().readLine();
        if (line != null) {
            //  reintroduce the line break removed by readLine()
            expandLine(reader, line + System.getProperty("line.separator")); 
        }
        
        return reader;
    }
    
    protected void expandLine(ChainReader reader, String line) throws IOException {
        String prefix;
        String attribute;
        String remaining;
        
        int start = line.indexOf("${");
        if (start != -1) {
            prefix = line.substring(0, start);
            
            int end = line.indexOf("}", start + 2);
            if (end != -1) {
                // ${attribute} found 
                attribute = line.substring(start + 2, end);
                remaining = line.substring(end + 1);
            }
            else {
                // start ${ but no } then assume that no attribute was found
                prefix = line; 
                attribute = null;
                remaining = null;
            }
        }
        else {
            // no attribute found
            prefix = line;
            attribute = null;
            remaining = null;
        }

        reader.addReader(new StringReader(prefix));
        
        if (attribute != null) {
            expandAttribute(reader, attribute);
        }

        if (remaining == null) {
            return;
        }
        
        expandLine(reader, remaining);
    }
    
    private void expandAttribute(ChainReader reader, String attribute) throws IOException {
        if (! getTemplate().hasAttribute(attribute)) {
            return;
        }
        
        List<AttributeValue> values = getTemplate().getAttributeValues(attribute);
        for (AttributeValue value : values) {
            reader.addReader(value.getReader());
        }
    }
    
    @Override
    public void close() throws IOException {
        getReader().close();
    }
}
