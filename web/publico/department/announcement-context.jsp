<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="siteActionName" value="/department/departmentSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="selectedDepartmentUnitID" toScope="request"/>
<bean:define id="siteContextParamValue" name="unit" property="idInternal" toScope="request"/>

<div class="breadcumbs mvert0">
    <bean:define id="institutionUrl">
        <bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    <bean:define id="structureUrl">
        <bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    
    <html:link href="<%= institutionUrl %>">
        <bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link href="<%= institutionUrl + structureUrl %>">
        <bean:message key="structure" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link page="/department/showDepartments.faces">
        <bean:message key="academic.units" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:define id="context" name="contextPrefix" type="java.lang.String"/>
    <bean:define id="parameters" name="extraParameters" type="java.lang.String"/>
    
    <html:link page="<%= context + parameters + "&amp;method=viewAnnouncements" %>">
        <fr:view name="announcement" property="announcementBoard.name"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <fr:view name="announcement" property="subject"/>
</div>
