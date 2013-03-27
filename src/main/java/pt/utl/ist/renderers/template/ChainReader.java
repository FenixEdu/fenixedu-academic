package pt.utl.ist.renderers.template;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChainReader extends Reader {

    private List<Reader> readers;
    
    public ChainReader() {
        this.readers = new ArrayList<Reader>();
    }
    
    public void addReader(Reader reader) {
        this.readers.add(reader);
    }

    protected List<Reader> getReaders() {
        return this.readers;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        Iterator<Reader> iterator = getReaders().iterator();
        
        while (iterator.hasNext()) {
            Reader reader = iterator.next();
            int count = reader.read(cbuf, off, len);
            
            if (count != -1) {
                return count;
            }
            
            iterator.remove();
            reader.close();
        }
        
        return -1;
    }

    @Override
    public void close() throws IOException {
        IOException exception = null;
        
        for (Reader reader : getReaders()) {
            try {
                reader.close();
            } catch (IOException e) {
                exception = e;
            }
        }
        
        if (exception != null) {
            throw exception;
        }
    }
}
