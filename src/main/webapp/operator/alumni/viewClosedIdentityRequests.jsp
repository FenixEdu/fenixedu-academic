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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<!-- viewClosedIdentityRequests.jsp -->

<h2><bean:message key="alumni.closed.identity.requests" bundle="MANAGER_RESOURCES"/></h2>


<html:link page="/alumni.do?method=prepareIdentityRequestsList">
	<bean:message key="label.back" bundle="MANAGER_RESOURCES"/>
</html:link>

	<logic:empty name="identityRequestsList">
		<p><em><bean:message key="alumni.no.identity.requests" bundle="MANAGER_RESOURCES" /></em></p>
	</logic:empty>

	<logic:notEmpty name="identityRequestsList">
		<p class="mbottom05"><bean:message key="label.closed.requests" bundle="MANAGER_RESOURCES"/>:</p>
		<fr:view name="identityRequestsList" layout="tabular" schema="alumni.identity.closed.request.list" >
			<fr:layout>
				<fr:property name="classes" value="tstyle1 tdcenter mtop05"/>
				<fr:property name="columnClasses" value="nowrap acenter,aleft,acenter,acenter,acenter,"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="decisionDateTime,fullName"/>
	           	<fr:property name="sortBy" value="creationDateTime=desc" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
