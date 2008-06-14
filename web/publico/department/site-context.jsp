<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<bean:define id="announcementActionVariable" value="/department/announcements.do" toScope="request"/>
<bean:define id="eventActionVariable" value="/department/events.do" toScope="request"/>
<bean:define id="announcementRSSActionVariable" value="/department/announcementsRSS.do" toScope="request"/>
<bean:define id="eventRSSActionVariable" value="/department/eventsRSS.do" toScope="request"/>

<bean:define id="siteActionName" value="/department/departmentSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="selectedDepartmentUnitID" toScope="request"/>
<bean:define id="siteContextParamValue" name="unit" property="idInternal" toScope="request"/>
<bean:define id="department" name="site" property="unit.department" toScope="request"/>

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
        
    <logic:notEmpty name="department" property="departmentUnit">
		&nbsp;&gt;&nbsp;
	    <app:contentLink name="department" property="departmentUnit.site" scope="request">
	        <fr:view name="department" property="nameI18n"/>    
	    </app:contentLink>
    </logic:notEmpty>
            
</div>

<h1 class="mbottom1">
	<fr:view name="department" property="nameI18n"/>
</h1>
