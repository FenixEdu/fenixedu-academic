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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="processName" name="processName" />
<bean:define id="processId" name="process" property="externalId" />

<strong><bean:write name="process" property="displayName" /></strong>

<logic:notEmpty name="activities">
	<fr:view schema="caseHandling.list.activities" name="activities">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop15" />
			<fr:property name="linkFormat(executeActivity)" value='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecute${id}&amp;processId=" + processId.toString()%>' />
			<fr:property name="key(executeActivity)" value="link.execute.activity" />
			<fr:property name="bundle(executeActivity)" value="APPLICATION_RESOURCES" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="activities">
	<br/>
	<em><strong><bean:message key="label.no.activities" bundle="APPLICATION_RESOURCES" /></strong></em>
	<br/>
</logic:empty>

<br/>
<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcesses"%>'>
	<html:cancel><bean:message key='label.back' bundle="APPLICATION_RESOURCES"/></html:cancel>			
</html:form>
