/*
 * ################################################################
 *
 * FenixEdu: The Java(TM) Object-Oriented Framework for University
 *       Academic Applications
 *
 * Copyright (C) 2002-2003 IST/Technical University of Lisbon
 * Contact: suporte@dot.ist.utl.pt
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The Fenix Team
 *                               http://fenix-ashes.ist.utl.pt/
 *
 *  Contributor(s): José Pedro Pereira - Educare S.A. - http://www.linkare.com
 *
 * ################################################################
 */
package net.sourceforge.fenixedu.presentationTier.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.exception.MissingObjectException;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.DomainEntity;
import pt.ist.fenixframework.dml.DomainModel;
import pt.ist.fenixframework.dml.Role;
import pt.ist.fenixframework.dml.Slot;

@WebServlet(urlPatterns = "/domainbrowser/*")
public class DomainBrowserServlet extends HttpServlet {

    private static final long serialVersionUID = -3780027224510147325L;

    private static final Logger logger = LoggerFactory.getLogger(DomainBrowserServlet.class);

    private static DomainModel domainModel = FenixFramework.getDomainModel();
    private static HashMap<DomainClass, String> domainClassesDescAttr = new HashMap<DomainClass, String>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");

        PrintWriter out = res.getWriter();
        out.println("<HEAD><TITLE>Fenix Domain Browser</TITLE></HEAD><BODY>\n");

        String path = req.getPathInfo();
        try {
            if ("/showObj".equals(path)) {
                renderDomainObject(out, req.getParameter("OID"));
            } else if ("/listRole".equals(path)) {
                renderDomainObjectRole(out, RequestParams.parse(req, "OID", "role"));
            } else {
                renderDomainObject(out, null);
            }
        } catch (MissingObjectException | NumberFormatException e) {
            out.println("<P>No such object</P>");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServletException("Error: " + e.getMessage(), e);
        }

        out.println("</BODY>\n");
        out.close();
    }

    private void renderDomainObject(PrintWriter out, String oid) throws IOException, Exception {

        DomainObject domObj = oid == null ? FenixFramework.getDomainRoot() : FenixFramework.getDomainObject(oid);
        DomainClass domClass = getDomainClass(domObj);

        out.println("<H1>");
        out.println(domObj.getClass().getName());
        out.println(" Instance: ");
        out.println(getObjectDescription(domObj));
        out.println("</H1>\n");

        out.println("<H2>Attributes</H2>\n");
        out.println("<TABLE>\n");
        for (String attr : getAllAttrs(domClass)) {
            if (!isForeignKey(attr, domClass)) {
                out.println("<TR><TD class=\"attrname\">");
                out.println(attr);
                out.println("</TD><TD class=\"attrvalue\">");
                out.println(getAttributeValue(domObj, attr));
                out.println("</TD><TR>");
            }
        }
        out.println("</TABLE>\n");

        out.println("<H2>Relations</H2>");
        out.println("<TABLE>\n");
        for (Role role : getAllRoles(domClass)) {
            out.println("<TR><TD class=\"rolename\">");
            out.println(role.getName());
            out.println("</TD><TD class=\"rolevalue\">");
            Object roleValue = getAttributeValue(domObj, role.getName());
            if (role.getMultiplicityUpper() == 1) {
                if (roleValue == null) {
                    out.println("null");
                } else {
                    DomainEntity roleType = role.getType();
                    if (roleType instanceof DomainClass) {
                        renderObjectId(out, getDomainObject(roleValue));
                    } else {
                        out.println(roleValue);
                    }
                }
            } else {
                if (roleValue == null) {
                    out.println("null collection");
                } else {
                    out.print("<a href=\"listRole?OID=");
                    out.print(domObj.getExternalId());
                    out.print("&role=");
                    out.print(role.getName());
                    out.print("\">");
                    out.print(role.getType().getFullName());
                    out.print("[]</a>");
                }
            }

            out.println("</TD><TR>");
        }
        out.println("</TABLE>\n");
    }

    private boolean isForeignKey(String attrName, DomainClass domClass) {
        return (attrName.startsWith("key") && (domClass.findRoleSlot(firstCharToLower(attrName.substring(3))) != null));
    }

    private void renderDomainObjectRole(PrintWriter out, RequestParams params) throws IOException, Exception {
        if (params == null) {
            return;
        }

        DomainObject domObj = FenixFramework.getDomainObject(params.oid);

        out.println("<H1>");
        renderObjectId(out, domObj);
        out.println("'s ");
        out.println(params.roleName);
        out.println(":</H1>\n");

        Collection insts = (Collection) getAttributeValue(domObj, params.roleName);
        renderCollection(out, insts, params);
    }

    private void renderCollection(PrintWriter out, Collection insts, RequestParams params) throws IOException, Exception {
        if (insts == null) {
            out.println("<P>Null collection</P>");
            return;
        }

        int total = insts.size();
        int first = params.start;
        int last = Math.min(first + params.max - 1, total);

        out.println("<P>Total instances: ");
        out.println(total);
        out.println("</P>\n");

        if ((total > last) || (first > 1)) {
            out.println("<P>Showing from ");
            out.println(first);
            out.println(" to ");
            out.println(last);
            out.println("</P>\n");
        }

        if (first > total) {
            return;
        }

        if (last < total) {
            params.start = last + 1;

            out.println("<P><a href=\"");
            out.println(params.buildURL());
            out.println("\">Show next</a></P>");

            params.start = first;
        }

        out.println("<UL>\n");

        Iterator iter = insts.iterator();

        // skip initial values until reaching start
        for (int i = first; i > 1; i--) {
            iter.next();
        }

        // show the (eventually partial) list
        for (int count = first; count <= last; count++) {
            out.println("<LI>");
            renderObjectId(out, getDomainObject(iter.next()));
            out.println("</LI>\n");
        }
        out.println("</UL>\n");
    }

    private void renderObjectId(PrintWriter out, DomainObject domObj) throws IOException {
        out.println("<a href=\"showObj?OID=");
        out.println(domObj.getExternalId());
        out.println("\">");
        out.println(getObjectDescription(domObj));
        out.println("</a>");
    }

    private String getObjectDescription(DomainObject domObj) {
        Object value = getAttributeValue(domObj, getDescAttribute(getDomainClass(domObj)));
        return value == null ? domObj.getExternalId() : value.toString();
    }

    private DomainClass getDomainClass(DomainObject obj) {
        return domainModel.findClass(obj.getClass().getName());
    }

    private String getDescAttribute(DomainClass domClass) {
        String descAttribute = domainClassesDescAttr.get(domClass);
        if (descAttribute == null) {
            descAttribute = guessDescAttribute(domClass);
            domainClassesDescAttr.put(domClass, descAttribute);
        }
        return descAttribute;
    }

    private String guessDescAttribute(DomainClass domClass) {
        String descAttr = "externalId";

        for (String attr : getAllAttrs(domClass)) {
            if (attr.equals("name") || attr.equals("nome")) {
                descAttr = attr;
                break;
            }
        }

        return descAttr;
    }

    private Object getAttributeValue(DomainObject obj, String attr) {
        try {
            Method reader = obj.getClass().getMethod("get" + capitalize(attr));
            return reader.invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    private static HashMap<DomainClass, List<String>> domainClassesAttributes = new HashMap<DomainClass, List<String>>();

    private synchronized List<String> getAllAttrs(DomainClass domClass) {
        List<String> attrs = domainClassesAttributes.get(domClass);
        if (attrs == null) {
            attrs = new ArrayList<String>();

            DomainEntity domEntity = domClass;
            while (domEntity instanceof DomainClass) {
                DomainClass dClass = (DomainClass) domEntity;
                Iterator slots = dClass.getSlots();
                while (slots.hasNext()) {
                    attrs.add(((Slot) slots.next()).getName());
                }
                domEntity = dClass.getSuperclass();
            }
            Collections.sort(attrs);
            domainClassesAttributes.put(domClass, attrs);
        }
        return attrs;
    }

    private static HashMap<DomainClass, List<Role>> domainClassesRoles = new HashMap<DomainClass, List<Role>>();

    private static final Comparator<Role> ROLE_COMPARATOR = new Comparator<Role>() {
        @Override
        public int compare(Role r1, Role r2) {
            return r1.getName().compareTo(r2.getName());
        }
    };

    private List<Role> getAllRoles(DomainClass domClass) {
        List<Role> roles = domainClassesRoles.get(domClass);
        if (roles == null) {
            roles = new ArrayList<Role>();

            DomainEntity domEntity = domClass;
            while (domEntity instanceof DomainClass) {
                DomainClass dClass = (DomainClass) domEntity;
                Iterator roleSlots = dClass.getRoleSlots();
                while (roleSlots.hasNext()) {
                    Role role = (Role) roleSlots.next();
                    if (role.getName() != null) {
                        roles.add(role);
                    }
                }
                domEntity = dClass.getSuperclass();
            }
            Collections.sort(roles, ROLE_COMPARATOR);
            domainClassesRoles.put(domClass, roles);
        }
        return roles;
    }

    private String capitalize(String str) {
        if ((str == null) || Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
    }

    private String firstCharToLower(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    private DomainObject getDomainObject(Object obj) {
        return (DomainObject) obj;
    }

    static class RequestParams {
        private static final String[] OPTIONAL_PARAMS = { "start", "max", "showColSize" };

        public static final int DEFAULT_MAX = 100;

        String path;
        String classFullName = null;
        DomainClass domClass = null;
        Class javaClass = null;
        String oid = null;
        String roleName = null;
        Role role = null;
        int start = 1;
        int max = DEFAULT_MAX;
        boolean showColSize = false;

        String buildURL() {
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            String sep = "?";
            if (classFullName != null) {
                sb.append(sep);
                sb.append("domainClass=");
                sb.append(classFullName);
                sep = "&";
            }
            if (oid != null) {
                sb.append(sep);
                sb.append("OID=");
                sb.append(oid);
                sep = "&";
            }
            if (roleName != null) {
                sb.append(sep);
                sb.append("role=");
                sb.append(roleName);
                sep = "&";
            }
            if (start > 1) {
                sb.append(sep);
                sb.append("start=");
                sb.append(start);
                sep = "&";
            }
            if (max != DEFAULT_MAX) {
                sb.append(sep);
                sb.append("max=");
                sb.append(max);
                sep = "&";
            }
            return sb.toString();
        }

        static RequestParams parse(HttpServletRequest req, String... paramNames) {
            RequestParams params = new RequestParams();

            params.path = req.getPathInfo().substring(1);

            try {
                for (String name : paramNames) {
                    String value = req.getParameter(name);
                    if (value == null) {
                        return null;
                    }

                    if (name.equals("domainClass")) {
                        params.classFullName = value;
                        params.javaClass = Class.forName(value);
                        params.domClass = domainModel.findClass(value);
                        if (params.domClass == null) {
                            return null;
                        }
                    } else if (name.equals("OID")) {
                        params.oid = value;
                        DomainObject domObj = FenixFramework.getDomainObject(params.oid);
                        params.domClass = domainModel.findClass(domObj.getClass().getName());
                    } else if (name.equals("role")) {
                        params.roleName = value;
                        params.role = params.domClass.findRoleSlot(value);
                        if (params.role == null) {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
            } catch (Exception e) {
                return null;
            }

            // parse optional parameters
            for (String name : OPTIONAL_PARAMS) {
                String value = req.getParameter(name);
                if (value != null) {
                    try {
                        if (name.equals("start")) {
                            params.start = Integer.parseInt(value);
                        } else if (name.equals("max")) {
                            params.max = Integer.parseInt(value);
                        } else if (name.equals("showColSize")) {
                            params.showColSize = true;
                        }
                    } catch (Exception e) {
                        // ignore it
                    }
                }
            }

            return params;
        }
    }

}
