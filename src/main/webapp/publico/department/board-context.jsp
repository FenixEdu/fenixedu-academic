<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<div class="breadcumbs mvert0">
    <bean:define id="institutionUrl">
        <%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>
    </bean:define>
    <bean:define id="structureUrl">
        <bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    
    <html:link href="<%= institutionUrl %>">
        <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> 
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
    
    <bean:define id="site" name="actual$site"/>

	<bean:define id="unitId" name="site" property="unit.externalId"/>
	
    <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>">
        <fr:view name="site" property="unit.nameI18n"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <logic:present name="showingAnnouncements"> 
		<bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/>
    </logic:present>
    <logic:present name="showingEvents">
    	<bean:message key="label.messaging.events.title" bundle="MESSAGING_RESOURCES"/>
    </logic:present>

	<bean:define id="announcementActionVariable" value="/department/announcements.do" toScope="request"/>
	<bean:define id="eventActionVariable" value="/department/events.do" toScope="request"/>
	<bean:define id="announcementRSSActionVariable" value="/department/announcementsRSS.do" toScope="request"/>
	<bean:define id="eventRSSActionVariable" value="/department/eventsRSS.do" toScope="request"/>

</div>
