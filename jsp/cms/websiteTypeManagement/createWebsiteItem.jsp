<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="websiteTypeId" name="websiteType" property="idInternal"/>

<html:link page="<%= "/websiteTypeManagement.do?method=edit&oid=" +  websiteTypeId %>">
    <bean:write name="websiteType" property="name"/> 
</html:link>

<logic:present name="parents">
�
    <%
        String pathParam = "";
    %>

    <logic:iterate id="p" indexId="index" name="parents">
        <logic:greaterThan name="index" value="0">�</logic:greaterThan>
        
        <bean:define id="parentId" name="p" property="idInternal"/>
            <html:link page="<%= "/websiteTypeManagement.do?method=editChild&child=" + parentId + "&oid=" + websiteTypeId +"&path=" + pathParam %>">
            <bean:write name="p" property="name"/>
        </html:link>
        
        <%
            pathParam = pathParam.length() == 0 ? String.valueOf(parentId) : pathParam + "/" + parentId;
        %>
    </logic:iterate>
</logic:present>

<h3><bean:message key="cms.websiteTypeManagement.item.create" bundle="CMS_RESOURCES"/></h3>

<logic:present name="parent">
    <bean:define id="oid" name="parent" property="idInternal"/>
    <bean:define id="path" name="path" type="java.lang.String"/>
    
    <%
        if (path.lastIndexOf("/") != -1) {
            path = path.substring(0, path.lastIndexOf("/"));
        }
        else {
            path = "";
        }
    %>
    
    <fr:create type="net.sourceforge.fenixedu.domain.cms.website.WebsiteItem" schema="cms.content.item.input"
               action="<%= "/websiteTypeManagement.do?method=editChild&child=" + oid + "&oid=" + websiteTypeId + "&path=" + path %>">
        <fr:hidden slot="parents" multiple="true" name="parent"/>
        <fr:hidden slot="cms" name="cms"/>
    </fr:create>
</logic:present>

<logic:notPresent name="parent">
    <fr:create type="net.sourceforge.fenixedu.domain.cms.website.WebsiteItem" schema="cms.content.item.input"
               action="<%= "/websiteTypeManagement.do?method=edit&oid=" + websiteTypeId %>">
        <fr:hidden slot="websiteTypes" multiple="true" name="websiteType"/>
        <fr:hidden slot="cms" name="cms"/>
    </fr:create>
</logic:notPresent>