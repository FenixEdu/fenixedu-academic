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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.view.curriculumLineLogs"/></h2>

<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="MANAGER_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:edit id="search" name="bean" schema="curriculumLineLog.search" action="/curriculumLineLogs.do?method=viewCurriculumLineLogs">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<logic:present name="curriculumLineLogs">
	<p>
		<logic:empty name="curriculumLineLogs">
			<em><bean:message bundle="MANAGER_RESOURCES" key="label.noCurriculumLineLogsFound"/></em>	
		</logic:empty>
		<logic:notEmpty name="curriculumLineLogs">
			<fr:view name="curriculumLineLogs" schema="curriculumLineLogs.list">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4" />
					<fr:property name="sortBy" value="dateDateTime=asc,description=asc"/>
				</fr:layout>
			</fr:view>	
		</logic:notEmpty>
	</p>
</logic:present>

<logic:present name="bean" property="executionPeriod">
	<html:link action="curriculumLineLogs.do?method=viewCurriculumLineLogStatistics" paramId="executionSemesterId" paramName="bean" paramProperty="executionPeriod.externalId">
		<bean:message bundle="MANAGER_RESOURCES" key="label.view.curriculumLineLogs.statistics"/>
	</html:link>
</logic:present>
