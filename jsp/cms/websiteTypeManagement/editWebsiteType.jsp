<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present name="websiteType">
    <bean:write name="websiteType" property="name"/>

    <h3><bean:message bundle="CMS_RESOURCES" key="cms.websiteTypeManagement.edit.label" /></h3>

    <bean:define id="oid" name="websiteType" property="idInternal"/>

    <fr:edit action="<%= "/websiteTypeManagement.do?method=edit&oid=" + oid %>"
             name="websiteType" layout="tabular" schema="websiteType.edit">
        <fr:destination name="go" path="<%= "/testAdd.do?method=start&oid=" + oid %>"/>
    </fr:edit>
    
    <br/>

    <logic:empty name="websiteTypeChildren">
        <bean:message key="cms.websiteTypeManagement.websiteType.childContents.empty" bundle="CMS_RESOURCES"/>.
    </logic:empty>
    
    <logic:notEmpty name="websiteTypeChildren">
        <bean:size id="size" name="websiteTypeChildren"/>
        <bean:write name="size"/> <bean:message key="cms.websiteManagement.website.uncompromisingChildContents.label" bundle="CMS_RESOURCES"/>.
        
        <fr:view name="websiteTypeChildren" schema="cms.content.basic">
            <fr:layout name="tabular">
                <fr:property name="style" value="width: 100%;"/>
                <fr:property name="headerClasses" value="listClasses-header"/>
                <fr:property name="columnClasses" value="listClasses"/>
    
                <fr:property name="link(edit)" value="/websiteTypeManagement.do?method=editChild"/>
                <fr:property name="param(edit)" value="<%= "idInternal/child,oid=" + oid %>"/>
                <fr:property name="key(edit)" value="cms.websiteTypeManagement.child.edit"/>
                <fr:property name="bundle(edit)" value="CMS_RESOURCES"/>
                <fr:property name="order(edit)" value="0"/>
    
                <fr:property name="link(delete)" value="/websiteTypeManagement.do?method=deleteChild"/>
                <fr:property name="param(delete)" value="<%= "idInternal/target,oid=" + oid %>"/>
                <fr:property name="key(delete)" value="cms.websiteTypeManagement.child.delete"/>
                <fr:property name="bundle(delete)" value="CMS_RESOURCES"/>
                <fr:property name="order(delete)" value="1"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
    
    <br/>
    
    <html:form action="/websiteTypeManagement" method="get">
        <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createChild"/>
        
        <input alt="input.oid" type="hidden" name="oid" value="<%= oid %>"/>
        
        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.section" property="section"><bean:message key="cms.websiteTypeManagement.section.create" bundle="CMS_RESOURCES"/></html:submit>
        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.item" property="item"><bean:message key="cms.websiteTypeManagement.item.create" bundle="CMS_RESOURCES"/></html:submit>
    </html:form>
</logic:present>