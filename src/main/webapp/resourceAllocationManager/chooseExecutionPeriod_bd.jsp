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

<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page
    import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants"%><html:xhtml />

<html:xhtml />

<h2><bean:message key="title.manage.schedule" /></h2>

<p>
	<span class="error">
		<html:messages id="message" message="true">
    		<bean:write name="message" />
		</html:messages>
		 <!-- Error messages go here -->
		<html:errors />
	</span>
</p>

<fr:form id="executionSemesterSelectionForm" action="/chooseExecutionPeriod.do?method=choose">
	<fr:edit id="executionSemesterSelectionFormEdit" schema="academicIntervalSelectionBean.choosePostBack"
		name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>">
		<fr:layout name="flow">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<html:link styleClass="btn btn-primary" page="/chooseContext.do?method=prepare&academicInterval=${academicInterval}">
		<bean:message key="link.schedules.chooseContext"></bean:message>
	</html:link>
</fr:form>

<br/>
<br/>

<logic:present name="executionYear">
	<html:link page="/chooseExecutionPeriod.do?method=toggleFirstYearShiftsCapacity" paramId="executionYearId" paramName="executionYear" paramProperty="OID">
		<bean:message bundle="SOP_RESOURCES" key="link.toggleFirstYearShiftsCapacity" /> <bean:write name="executionYear" property="name"/>
	</html:link>
</logic:present>


