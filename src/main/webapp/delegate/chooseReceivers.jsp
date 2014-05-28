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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.sendMailToStudents" bundle="DELEGATES_RESOURCES" /></h2>

<bean:define id="year" name="currentExecutionYear" property="executionYear.externalId"/>
<fr:form action="/sendEmailToDelegateStudents.do?method=chooseExecutionYearCurricularCourseList">
	<fr:edit schema="choose.execution.year" name="currentExecutionYear" id="chooseExecutionYear" layout="tabular">
		<fr:destination name="postBackChooseExecutionYear" path="/sendEmailToDelegateStudents.do?method=chooseExecutionYearCurricularCourseList"/>
	</fr:edit>
</fr:form>

<!-- AVISOS E ERROS -->
<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="DELEGATES_RESOURCES">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<p class="mtop2 mbottom05">
	<b><bean:message key="label.sendMailToStudents.chooseReceivers" bundle="DELEGATES_RESOURCES" /></b></p>
<p class="color888 mtop05 mbottom0">
	<bean:message key="label.sendMailToStudents.chooseReceivers.help" bundle="DELEGATES_RESOURCES" /></p>
<logic:present name="curricularCoursesList" >
	<p class="color888 mtop0 mbottom05">
		<bean:message key="label.sendMailToStudents.chooseCurricularCourses.help" bundle="DELEGATES_RESOURCES" /></p>
</logic:present>	

<p class="mtop05 mbottom05">
	<b><bean:message key="label.delegates.sendMailTo" bundle="DELEGATES_RESOURCES" /></b>
	<html:link page='<%= "/sendEmailToDelegateStudents.do?method=prepare&amp;year=" + year%>'>
		<bean:message key="link.sendToDelegateStudents" bundle="DELEGATES_RESOURCES"/>
	</html:link>,
	<html:link page='<%= "/sendEmailToDelegateStudents.do?method=prepareSendToStudentsFromSelectedCurricularCourses&amp;year=" + year %>'>
		<bean:message key="link.sendToStudentsFromCurricularCourses" bundle="DELEGATES_RESOURCES"/>
	</html:link>
</p>

<logic:present name="curricularCoursesList" >
	<logic:notEmpty name="curricularCoursesList">
		<fr:form action='<%= "/sendEmailToDelegateStudents.do?method=sendToStudentsFromSelectedCurricularCourses&amp;year=" + year %>'>
			<fr:view name="curricularCoursesList" layout="tabular" schema="delegates.sendEmail.showCurricularCourses.curricularCourseInfo">
				<fr:layout>
					<fr:property name="classes" value="tstyle1 thlight tdcenter mtop05 mbottom05"/>
					<fr:property name="columnClasses" value=",width250px aleft nowrap,,,"/>
					<fr:property name="suffixes" value=",º ano,º sem"/>
					<fr:property name="checkable" value="true" />
					<fr:property name="checkboxName" value="selectedCurricularCourses" />
					<fr:property name="checkboxValue" value="curricularCourse.externalId" />
					<fr:property name="selectAllShown" value="true" />
					<fr:property name="selectAllLocation" value="bottom" />
				</fr:layout>
				<fr:destination name="invalid" path="/sendEmailToDelegateStudents.do?method=prepareSendToStudentsFromSelectedCurricularCourses" />
			</fr:view>
			<div class="mtop1">
				<html:submit><bean:message key="button.continue" bundle="DELEGATES_RESOURCES"/></html:submit>
			</div>
		</fr:form>
	</logic:notEmpty>
</logic:present>




