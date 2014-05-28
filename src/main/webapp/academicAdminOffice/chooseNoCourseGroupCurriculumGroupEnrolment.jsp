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

<bean:define id="type" name="enrolmentBean" property="groupType"/>
<bean:define id="actionName" name="actionName" />

<h2><strong><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="<%= type.toString() %>" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<br/>


<fr:form action='<%= "/" + actionName + ".do" %>'>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="back2"/>
	<fr:edit id="enrolmentBean" name="enrolmentBean" visible="false" />
	
	
	<fr:edit id="enrolmentBean-search" name="enrolmentBean" schema="studentOptionalEnrolment.base">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
		    <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
		<fr:destination name="postBack" path='<%= "/" + actionName + ".do?method=postBack" %>' />
	</fr:edit>

	<html:submit><bean:message key="back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>

	<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
		<br/>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
		<br/>
	</html:messages>
	
	<html:messages id="error" message="true" bundle="APPLICATION_RESOURCES" property="enrolmentError" >
		<br/>
		<span class="error"><!-- Error messages go here --><bean:write name="error" /></span>
		<br/>
	</html:messages>
	
	<br />
	<br />
	
	<logic:present name="enrolmentBean" property="degreeCurricularPlan">
		<fr:edit id="degreeCurricularPlan" name="enrolmentBean">
			<fr:layout name="student-optional-enrolments">
				<fr:property name="methodName" value="enrol" />
			</fr:layout>
		</fr:edit>
		<br/>
		<br/>		
	</logic:present>

	<html:submit><bean:message key="back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>

</fr:form>
