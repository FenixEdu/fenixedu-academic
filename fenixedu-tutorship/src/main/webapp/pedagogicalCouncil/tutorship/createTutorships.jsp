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
<%@ page import="java.util.List" %>


<h2><bean:message key="title.tutorship.create" bundle="PEDAGOGICAL_COUNCIL" /></h2>


<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>
<logic:present name="success">
		<p><bean:message key="label.submit.success" bundle="PEDAGOGICAL_COUNCIL"/></p>
</logic:present>

<fr:form action="/createTutorships.do?method=createTutorship">
	<fr:edit id="tutorateBean" name="tutorateBean" schema="tutorship.student.list">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thleft mtop0" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:destination name="degreePostBack" path="/createTutorships.do?method=prepareViewCreateTutorship"/>
		<fr:destination name="semesterPostBack" path="/createTutorships.do?method=prepareViewCreateTutorship"/>
		<fr:destination name="selectExecCourse" path="/createTutorships.do?method=prepareViewCreateTutorship"/>
		<fr:destination name="invalid" path="/createTutorships.do?method=prepareCreation"/>
</fr:edit>
	<logic:present name="students">
	<bean:define id="students" name="students" scope="request"/>
		
	<%= ((List)students).size()%> <bean:message key="label.tutorship.students.number" bundle="PEDAGOGICAL_COUNCIL"/>
	<fr:view name="students" layout="tabular" schema="tutorship.tutorate.student.create">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight tdcenter mtop05 mbottom05"/>
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="selectedPersons" />
				<fr:property name="checkboxValue" value="externalId" />
				<fr:property name="selectAllShown" value="true" />
				<fr:property name="selectAllLocation" value="top" />
			</fr:layout>
		</fr:view>
		<fr:edit id="tutorBean" name="tutorBean" schema="tutorship.tutor.list">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thleft mtop0" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		<p>
			<html:submit property="create"><bean:message key="label.submit.create" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
		</p>
	</logic:present>
</fr:form>
	

