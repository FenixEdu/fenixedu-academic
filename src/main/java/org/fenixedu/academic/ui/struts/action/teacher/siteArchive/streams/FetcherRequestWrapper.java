/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        } else {
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
        } else {
            return super.getCharacterEncoding();
        }
    }

    @Override
    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        this.encoding = encoding;
    }
}
