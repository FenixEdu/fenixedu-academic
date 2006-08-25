<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<logic:present name="parents">
    <%
        String pathParam = "";
    %>

    <logic:iterate id="p" indexId="index" name="parents">
        <logic:greaterThan name="index" value="0">»</logic:greaterThan>
        
        <bean:define id="parentId" name="p" property="idInternal"/>
        <html:link page="<%= "/websiteManagement.do?method=edit&oid=" + parentId + "&path=" + pathParam %>">
            <bean:write name="p" property="name"/>
        </html:link>
        
        <%
            pathParam = pathParam.length() == 0 ? String.valueOf(parentId) : pathParam + "/" + parentId;
        %>
    </logic:iterate>
</logic:present>

<h3><bean:message key="cms.websiteManagement.section.create" bundle="CMS_RESOURCES"/></h3>

<bean:define id="oid" name="parent" property="idInternal"/>
<bean:define id="path" name="path" type="java.lang.String"/>

<%
    if (path.lastIndexOf("/") != -1) {
        path = path.substring(0, path.lastIndexOf("/"));
    }
%>

<fr:create type="net.sourceforge.fenixedu.domain.cms.website.WebsiteSection" schema="cms.content.basic.input"
           action="<%= "/websiteManagement.do?method=edit&oid=" + oid + "&path=" + path %>">
    <fr:hidden slot="parents" multiple="true" name="parent"/>
    <fr:hidden slot="cms" name="parent" property="cms"/>
</fr:create>

