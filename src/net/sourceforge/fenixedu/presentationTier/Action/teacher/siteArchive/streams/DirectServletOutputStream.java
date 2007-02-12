package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.streams;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class DirectServletOutputStream extends ServletOutputStream {

    private OutputStream stream;
    
    public DirectServletOutputStream(OutputStream stream) {
        super();

        this.stream = stream;
    }

    @Override
    public void write(int b) throws IOException {
        this.stream.write(b);
    }

    @Override
    public void flush() throws IOException {
        this.stream.flush();
    }

    @Override
    public void close() throws IOException {
        flush();
        stream.close();
    }
}
