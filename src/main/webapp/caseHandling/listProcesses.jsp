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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="processName" name="processName"/>
<logic:equal name="canCreateProcess" value="true">
	<br/>
	<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareCreateNewProcess"%>'>
		<bean:message key='<%= "link.create.new.process." + processName.toString()%>' bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</logic:equal>

<logic:notEmpty name="processes">
	<br/>
	<br/>
	<fr:view name="processes" schema="caseHandling.list.processes">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop15" />
			<fr:property name="linkFormat(viewProcess)" value='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=${externalId}"%>' />
			<fr:property name="key(viewProcess)" value="link.list.processes"/>
			<fr:property name="bundle(viewProcess)" value="APPLICATION_RESOURCES"/>
		</fr:layout>	
	</fr:view>
</logic:notEmpty>
