package ServidorApresentacao.servlets.filters.cache;


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CacheResponseStream extends ServletOutputStream {
  protected boolean closed = false;
  protected HttpServletResponse response = null;
  protected ServletOutputStream output = null;
  protected OutputStream cache = null;

  public CacheResponseStream(HttpServletResponse response,
      OutputStream cache) throws IOException {
    super();
    closed = false;
    this.response = response;
    this.cache = cache;
  }

  public void close() throws IOException {
    if (closed) {
      throw new IOException(
        "This output stream has already been closed");
    }
    cache.close();
    //closed = true;
  }

  public void flush() throws IOException {
    if (closed) {
      throw new IOException(
        "Cannot flush a closed output stream");
    }
    cache.flush();
  }

  public void write(int b) throws IOException {
    if (closed) {
      throw new IOException(
        "Cannot write to a closed output stream");
    }
    cache.write((byte)b);
  }

  public void write(byte b[]) throws IOException {
    write(b, 0, b.length);
  }

  public void write(byte b[], int off, int len)
    throws IOException {
    if (closed) {
      throw new IOException(
       "Cannot write to a closed output stream");
    }
    cache.write(b, off, len);
  }

  public boolean closed() {
    return (this.closed);
  }
  
  public void reset() {
    //noop
  }
}
