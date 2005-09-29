package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sourceforge.fenixedu.stm.Transaction;

public class CloseTransactionFilter implements Filter {

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }
      
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    Transaction.begin();
    Transaction.currentFenixTransaction().setReadOnly();
    try {
        chain.doFilter(request, response);
    } finally {
        Transaction.forceFinish();
    }
    }
}
