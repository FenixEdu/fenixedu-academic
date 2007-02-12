package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.streams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.collections.iterators.IteratorEnumeration;

/**
 * This request wrapper is used to ensure that the original request remains
 * unchanged. All elements that can be changed in a request are handled locally
 * and changes are not propagated to the wrapped request.
 * 
 * @author cfgi
 */
public class FetcherRequestWrapper extends HttpServletRequestWrapper {

    private String encoding;
    private Map<String, Object> attributes;
    
    public FetcherRequestWrapper(HttpServletRequest request) {
        super(request);
        
        this.attributes = new HashMap<String, Object>();
        
        Enumeration names = request.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            this.attributes.put(name, request.getAttribute(name));
        }
    }

    @Override
    public Object getAttribute(String name) {
        if (this.attributes.containsKey(name)) {
            return this.attributes.get(name);
        }
        else {
            return super.getAttribute(name);
        }
    }

    @Override
    public Enumeration getAttributeNames() {
        List<String> names = new ArrayList<String>();
        names.addAll(this.attributes.keySet());
        
        return new IteratorEnumeration(names.iterator());
    }

    @Override
    public void removeAttribute(String name) {
       this.attributes.remove(name);
    }

    @Override
    public void setAttribute(String name, Object o) {
        this.attributes.put(name, o);
    }

    @Override
    public String getCharacterEncoding() {
        if (this.encoding != null) {
            return this.encoding;
        }
        else {
            return super.getCharacterEncoding();
        }
    }

    @Override
    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        this.encoding = encoding;
    }
}
