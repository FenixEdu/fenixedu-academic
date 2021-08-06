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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.extraordinarySeason" /></h2>

<logic:notPresent role="role(STUDENT)">
	<span class="error"><bean:message key="error.exception.notAuthorized" bundle="STUDENT_RESOURCES" /></span>
</logic:notPresent>


<logic:messagesPresent message="true" property="warning" >
	<div class="warning0" style="padding: 0.5em;">
	<p class="mvert0"><strong><bean:message bundle="STUDENT_RESOURCES" key="label.enrollment.warnings.in.enrolment" />:</strong></p>
	<ul class="mvert05">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="warning">
			<li><span><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	</div>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="error">
	<div class="error0" style="padding: 0.5em;">
	<p class="mvert0"><strong><bean:message bundle="STUDENT_RESOURCES" key="label.enrollment.achtung.in.enrolment" />:</strong></p>
	<ul class="mvert05">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="error">
			<li><span><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	</div>
	<bean:define id="noFurtherAccess" value="true"/>
</logic:messagesPresent>


<logic:present role="role(STUDENT)">
	<div class="infoop2">
		<bean:message key="label.student.ExtraordinarySeasonEnrollment.description" bundle="STUDENT_RESOURCES"/><br/><br/>
	</div>
	<logic:notPresent name="disableContinue">
		<html:link page="/enrollment/evaluations/extraordinarySeason.do?method=pickSCP"><bean:message key="button.continue" bundle="STUDENT_RESOURCES"/></html:link>
	</logic:notPresent>
</logic:present>