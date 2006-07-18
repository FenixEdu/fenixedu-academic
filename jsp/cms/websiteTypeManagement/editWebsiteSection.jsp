<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.cms.Bin,net.sourceforge.fenixedu.domain.cms.Content" %>
<%@ page import="java.util.List,java.util.ArrayList,java.util.Iterator" %>
<%@ page import="java.io.IOException" %>
<%@ page import="org.apache.struts.taglib.TagUtils" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present name="section">
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.cms.Bin"/>
    <bean:define id="websiteTypeId" name="websiteType" property="idInternal"/>

    <html:link page="<%= "/websiteTypeManagement.do?method=edit&oid=" +  websiteTypeId %>">
        <bean:write name="websiteType" property="name"/> 
    </html:link>
    <logic:present name="parents">
        <%
            String pathParam = "";
        %>
    
        <logic:iterate id="parent" indexId="index" name="parents">
            �
            <bean:define id="parentId" name="parent" property="idInternal"/>
            <html:link page="<%= "/websiteTypeManagement.do?method=editChild&child=" + parentId + "&oid=" + websiteTypeId + "&path=" + pathParam %>">
                <bean:write name="parent" property="name"/>
            </html:link>
            
            <%
                pathParam = pathParam.length() == 0 ? String.valueOf(parentId) : pathParam + "/" + parentId;
            %>
        </logic:iterate>
    </logic:present>
    �
    <bean:write name="section" property="name"/>

    <div style="float: right;">
        <ul>
            <li>
                <html:link page="<%= "/websiteTypeManagement.do?method=edit&oid=" +  websiteTypeId %>">
                    <bean:write name="websiteType" property="name"/> 
                </html:link>
                <ul>
                    <logic:present name="parents">
                            <bean:define id="parents" name="parents" type="java.util.List"/>
                            <%
                                printHierarchy(pageContext, out, section, (Bin) parents.get(0), new ArrayList());
                            %>
                    </logic:present>
                    <logic:notPresent name="parents">
                            <%
                                printHierarchy(pageContext, out, section, section, new ArrayList());
                            %>
                    </logic:notPresent>
                </ul>
            </li>
        </ul>
    </div>
    
    <h3><bean:message key="cms.websiteTypeManagement.section.edit" bundle="CMS_RESOURCES"/></h3>

    <bean:define id="oid" name="section" property="idInternal"/>
    <bean:define id="path" name="path" type="java.lang.String"/>

    <%
        String newPath = (path == null || path.length() == 0) ? "" + oid : path + "/" + oid;
    %>

    <fr:edit name="section" layout="tabular" schema="cms.content.basic.input"
             action="<%= "/websiteManagement.do?method=edit&oid=" + oid + "&path=" + path %>"/>
    <br/>

    <h3><bean:message key="cms.websiteTypeManagement.section.childContents.title" bundle="CMS_RESOURCES"/></h3>

    <div style="float: left; width: 60%;">
        <logic:empty name="section" property="children">
            <bean:message key="cms.websiteTypeManagement.section.childContents.empty" bundle="CMS_RESOURCES"/>.
        </logic:empty>
        
        <logic:notEmpty name="section" property="children">
            <bean:size id="size" name="section" property="children"/>
            <bean:write name="size"/> <bean:message key="cms.websiteTypeManagement.section.uncompromisingChildContents.label" bundle="CMS_RESOURCES"/>.
            
            <fr:view name="section" property="children" schema="cms.content.basic">
                <fr:layout name="tabular">
                    <fr:property name="style" value="width: 100%;"/>
                    <fr:property name="headerClasses" value="listClasses-header"/>
                    <fr:property name="columnClasses" value="listClasses"/>
    
                    <fr:property name="link(edit)" value="/websiteTypeManagement.do?method=editChild"/>
                    <fr:property name="param(edit)" value="<%= "idInternal/child,oid=" + websiteTypeId + ",parent=" + oid + ",path=" + newPath %>"/>
                    <fr:property name="key(edit)" value="cms.websiteTypeManagement.child.edit"/>
                    <fr:property name="bundle(edit)" value="CMS_RESOURCES"/>
                    <fr:property name="order(edit)" value="0"/>
    
                    <fr:property name="link(delete)" value="/websiteTypeManagement.do?method=deleteChild"/>
                    <fr:property name="param(delete)" value="<%= "idInternal/target,oid=" + websiteTypeId + ",child=" + oid + ",path=" + path %>"/>
                    <fr:property name="key(delete)" value="cms.websiteTypeManagement.child.delete"/>
                    <fr:property name="bundle(delete)" value="CMS_RESOURCES"/>
                    <fr:property name="order(delete)" value="1"/>
                </fr:layout>
            </fr:view>
        </logic:notEmpty>
        
        <br/>
        
        <html:form action="/websiteTypeManagement" method="get">
            <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createChild"/>
            
            <input alt="input.oid" type="hidden" name="oid" value="<%= websiteTypeId %>"/>
            <input alt="input.parent" type="hidden" name="parent" value="<%= oid %>"/>
            <input alt="input.path" type="hidden" name="path" value="<%= newPath %>"/>
            
            <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.section" property="section"><bean:message key="cms.websiteManagement.section.create" bundle="CMS_RESOURCES"/></html:submit>
            <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.item" property="item"><bean:message key="cms.websiteManagement.item.create" bundle="CMS_RESOURCES"/></html:submit>
        </html:form>
    </div>
</logic:present>

<%!
    private String getPath(Content root, List parents) {
        String path = "";
        
        for (int i=0; i<parents.size(); i++) {
            Bin content = (Bin) parents.get(i);
            
            String oid = String.valueOf(content.getIdInternal());
            path = path.length() == 0 ? oid : path + "/" + oid;
        }
        
        return path;
    }

    private void printLink(PageContext context, JspWriter out, Content current, Content content, List parents) throws IOException {
        if (current.equals(content)) {
            out.println(content.getName());
        }
        else {
            String page = "/websiteTypeManagement.do?method=editChild&child=" + content.getIdInternal();
            page = page + "&oid=" + context.getRequest().getParameter("oid");
            page = page + "&path=" + getPath(content, parents);
            
            String finalLink = TagUtils.getInstance().computeURL(context, null, null, page, null, null, null, null, false);
    
            out.println("<a href=\"" + finalLink + "\">");
            out.println(content.getName());
            out.println("</a>");
        }
    }
    
    private void printHierarchy(PageContext context, JspWriter out, Bin current, Bin root, List parents) throws IOException {
        
        out.println("<li>");
        printLink(context, out, current, root, parents);
        out.println("<ul>");

        List newParents = new ArrayList(parents);
        newParents.add(root);

        Iterator iterator = root.getChildrenIterator();
        while (iterator.hasNext()) {
            Content child = (Content) iterator.next();

            if (child instanceof Bin) {
                printHierarchy(context, out, current, (Bin) child, newParents);
            }
            else {
                out.println("<li>");
                printLink(context, out, current, child, newParents);
                out.println("</li>");
            }
        }
        
        out.println("</ul>");

        out.println("</li>");
    }
%>
