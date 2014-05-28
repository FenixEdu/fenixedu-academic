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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="title.tutorship.edit" bundle="PEDAGOGICAL_COUNCIL" /></h2>


<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="tutors">

	<bean:define id="studentId" name="studentId"/>
	<fr:form action="<%= "/viewTutorship.do?method=createNewTutorship&studentId=" + studentId %>">
		<fr:edit id="tutors" name="tutors">
			<fr:schema bundle="PEDAGOGICAL_COUNCIL"
				type="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TeacherTutorshipCreationBean">
				<fr:slot name="teacher" layout="menu-select"
					key="label.tutorship.tutor">
					<fr:property name="providerClass"
						value="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TeacherTutorshipCreationBean$TutorsProvider" />
					<fr:property name="format"
						value="${teacher.teacherId} - ${name}" />
					<fr:property name="sortBy" value="teacher.teacherId" />
				</fr:slot>
			</fr:schema>

			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thleft mtop0" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		
		<p>
			<html:submit property="create"><bean:message key="label.submit.create" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
		</p>
		
</fr:form>


</logic:present>
