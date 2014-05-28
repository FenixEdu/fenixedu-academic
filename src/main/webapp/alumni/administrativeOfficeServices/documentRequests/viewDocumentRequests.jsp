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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="documents.requirement.consult" bundle="STUDENT_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>


<logic:empty name="documentRequests">
	<p class="mtop2"><em><bean:message key="no.document.requests" bundle="STUDENT_RESOURCES"/><bean:write name="student" property="person.username"/>.</em></p>
</logic:empty>

<fr:view name="documentRequests" schema="AcademicServiceRequest.view-for-given-registration">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight tdcenter" />
		<fr:property name="columnClasses" value="smalltxt,smalltxt,smalltxt,smalltxt,smalltxt,aleft smalltxt nowrap,smalltxt,smalltxt,smalltxt,smalltxt nowrap," />
		<fr:property name="linkFormat(view)" value="/viewDocumentRequests.do?method=viewDocumentRequest&documentRequestId=${externalId}"/>
		<fr:property name="key(view)" value="label.view"/>
		<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
		<fr:property name="linkFormat(cancel)" value="/viewDocumentRequests.do?method=prepareCancelAcademicServiceRequest&academicServiceRequestId=${externalId}&registrationID=${registration.externalId}"/>
		<fr:property name="key(cancel)" value="label.cancel"/>
		<fr:property name="bundle(cancel)" value="APPLICATION_RESOURCES"/>
		<fr:property name="visibleIf(cancel)" value="loggedPersonCanCancel"/>
		<fr:property name="sortBy" value="requestDate=desc, academicServiceRequestType=asc, urgentRequest=desc"/>
	</fr:layout>
</fr:view>
