<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<logic:present name="item">
    <bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.cms.Content"/>
    <bean:define id="websiteTypeId" name="websiteType" property="idInternal"/>

    <html:link page="<%= "/websiteTypeManagement.do?method=edit&oid=" +  websiteTypeId %>">
        <bean:write name="websiteType" property="name"/> 
    </html:link>
    <logic:present name="parents">
    »
        <%
            String pathParam = "";
        %>
    
        <logic:iterate id="parent" indexId="index" name="parents">
            <logic:greaterThan name="index" value="0">»</logic:greaterThan>
            
            <bean:define id="parentId" name="parent" property="idInternal"/>
            <html:link page="<%= "/websiteTypeManagement.do?method=editChild&child=" + parentId + "&oid=" + websiteTypeId +"&path=" + pathParam %>">
                <bean:write name="parent" property="name"/>
            </html:link>
            
            <%
                pathParam = pathParam.length() == 0 ? String.valueOf(parentId) : pathParam + "/" + parentId;
            %>
        </logic:iterate>
        
        <logic:notEmpty name="parents">»</logic:notEmpty>
        <bean:write name="item" property="name"/>
    
    </logic:present>

    <h3><bean:message key="cms.websiteTypeManagement.item.edit" bundle="CMS_RESOURCES"/></h3>
    
    <logic:present name="parent">
        <bean:define id="oid" name="parent" property="idInternal"/>
        <bean:define id="path" name="path" type="java.lang.String"/>
    
        <%
            if (path.lastIndexOf("/") != -1) {
                path = path.substring(0, path.lastIndexOf("/"));
            }
        %>
    
        <fr:edit name="item" layout="tabular" schema="cms.content.item.input"
                 action="<%= "/websiteTypeManagement.do?method=editChild&child=" + oid + "&oid=" + websiteTypeId + "&path=" + path %>"/>
    </logic:present>
    <logic:notPresent name="parent">
        <fr:edit name="item" layout="tabular" schema="cms.content.item.input"
                 action="<%= "/websiteTypeManagement.do?method=edit&oid=" + websiteTypeId %>"/>
    </logic:notPresent>
</logic:present>
