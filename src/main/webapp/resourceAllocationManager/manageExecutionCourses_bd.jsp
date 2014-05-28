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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@ page import="java.util.List"%>

<h2><bean:message key="label.manager.executionCourses"/></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<fr:form action="/manageExecutionCourses.do?method=search">
	<div class="infoop3">
	<p class="mvert025"><bean:message key="information.disciplinesExecution.note"/></p>
	<p class="mvert025"><bean:message key="information.disciplinesExecution.exampleOne"/></p>
	<p class="mvert025"><bean:message key="information.disciplinesExecution.exampleTwo"/></p>
	</div>

	<fr:edit name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>" schema="executionCourseContext.choose">
		<fr:destination name="intervalPostBack" path="/manageExecutionCourses.do?method=choosePostBack" />
		<fr:destination name="degreePostBack" path="/manageExecutionCourses.do?method=choosePostBack" />
		<fr:destination name="yearPostBack" path="/manageExecutionCourses.do?method=choosePostBack" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.choose" />
	</html:submit></p>
</fr:form>

<jsp:include page="listExecutionCourses.jsp" />