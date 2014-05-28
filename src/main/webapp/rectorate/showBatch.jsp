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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.rectorateIncoming" />
</h2>
<p>
	<html:link action="/rectorateIncomingBatches.do?method=index">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.rectorateSubmission.backToIndex" />
	</html:link>
</p>

<bean:define id="creation" name="batch" property="creation" type="org.joda.time.DateTime" />
<bean:define id="count" name="batch" property="diplomaDocumentRequestCount" />
<h3 class="mtop15 mbottom05">
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectorateSubmission.batchDetails"
		arg0="<%=creation.toString("dd-MM-yyyy")%>" arg1="<%=count.toString()%>" />
</h3>


<logic:empty name="requests">
	<p>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectorateSubmission.noRequestsInBatch" />
	</p>
</logic:empty>

<bean:define id="batchOid" name="batch" property="externalId" />
<bean:define id="confirmMessage">
	<bean:message key="label.rectorateSubmission.confirmActions" bundle="ACADEMIC_OFFICE_RESOURCES" />
</bean:define>


<logic:notEmpty name="actions">
	<ul class="mtop1">
		<logic:iterate id="action" name="actions">
			<li><html:link action="<%="/rectorateIncomingBatches.do?method=" + action + "&amp;batchOid=" + batchOid%>">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="<%="link.rectorateSubmission." + action%>" />
				</html:link></li>
		</logic:iterate>
	</ul>
</logic:notEmpty>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"> <!-- Error messages go here --> <bean:write name="message" />
		</span>
	</p>
</html:messages>

<logic:notEmpty name="requests">
	<fr:view name="requests">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES"
			type="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRectorateSubmissionBatchDocumentEntry">
			<fr:slot name="registryCode.code" key="label.rectorateSubmission.registryCode" />
			<fr:slot name="documentRequestType" key="label.rectorateSubmission.registryCode" />
			<fr:slot name="requestedCycle" key="label.rectorateSubmission.requestedCycle" />
			<fr:slot name="programmeTypeDescription" key="label.degreeType" />
			<fr:slot name="student.number" key="label.studentNumber" />
			<fr:slot name="person.name" key="label.Student.Person.name" />
			<fr:slot name="academicServiceRequestSituationType" key="label.currentSituation" />
			<fr:slot name="lastGeneratedDocument" layout="link" key="label.rectorateSubmission.document">
				<fr:property name="key" value="link.download" />
				<fr:property name="bundle" value="COMMON_RESOURCES" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thcenter" />
			<fr:property name="sortBy" value="registryCode.code=asc" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>