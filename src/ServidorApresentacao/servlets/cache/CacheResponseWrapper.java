package ServidorApresentacao.servlets.cache;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CacheResponseWrapper
    extends HttpServletResponseWrapper {
  protected HttpServletResponse origResponse = null;
  protected ServletOutputStream stream = null;
  protected PrintWriter writer = null;
  protected OutputStream cache = null;

  public CacheResponseWrapper(HttpServletResponse response,
      OutputStream cache) {
    super(response);
    origResponse = response;
    this.cache = cache;
  }

  public ServletOutputStream createOutputStream()
      throws IOException {
    return (new CacheResponseStream(origResponse, cache));
  }

  public void flushBuffer() throws IOException {
    stream.flush();
  }

  public ServletOutputStream getOutputStream()
      throws IOException {
    if (writer != null) {
      throw new IllegalStateException(
        "getWriter() has already been called!");
    }

    if (stream == null)
      stream = createOutputStream();
    return (stream);
  }

  public PrintWriter getWriter() throws IOException {
    if (writer != null) {
      return (writer);
    }

    if (stream != null) {
      throw new IllegalStateException(
        "getOutputStream() has already been called!");
    }

   stream = createOutputStream();
   writer = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
   return (writer);
  }
}