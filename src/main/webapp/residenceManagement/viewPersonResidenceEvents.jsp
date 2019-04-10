<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.resident" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>


<bean:define id="monthOID" name="month" property="OID"/>

<p>
	<html:link page="<%= "/residenceEventManagement.do?method=manageResidenceEvents&monthOID="  + monthOID + (request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "")%>"> 
		&laquo; <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>

<fr:view name="person" schema="show.residence.person">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom2" />
	</fr:layout>
</fr:view>

<logic:notEmpty name="residenceEvents">
	<fr:view name="residenceEvents" schema="show.person.residenceEvents">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 tdcenter thlight mtop05" />
			<fr:property name="sortBy" value="residenceMonth.year.year=desc,residenceMonth.month=desc"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="residenceEvents">
	<p class="mvert05">
		<em><bean:message key="message.person.has.no.events" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>.</em>
	</p>
</logic:empty>
