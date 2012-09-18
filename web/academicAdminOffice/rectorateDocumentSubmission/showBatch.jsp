<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />


<logic:present role="RECTORATE">
	<em><bean:message key="label.rectorate" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.rectorateIncoming" /></h2>
	<p><html:link action="/rectorateIncomingBatches.do?method=index">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.rectorateSubmission.backToIndex" />
	</html:link></p>
</logic:present>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.rectorateSubmission" /></h2>
	<p><html:link action="/rectorateDocumentSubmission.do?method=index">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.rectorateSubmission.backToIndex" />
	</html:link></p>
</logic:present>

<bean:define id="creation" name="batch" property="creation" type="org.joda.time.DateTime" />
<bean:define id="count" name="batch" property="documentRequestCount" />
<logic:present role="RECTORATE">
	<bean:define id="count" name="batch" property="diplomaDocumentRequestCount" />
</logic:present>
<h3 class="mtop15 mbottom05"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
    key="label.rectorateSubmission.batchDetails" arg0="<%= creation.toString("dd-MM-yyyy") %>"
    arg1="<%= count.toString() %>" /></h3>


<logic:empty name="requests">
    <p><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectorateSubmission.noRequestsInBatch" /></p>
</logic:empty>

<bean:define id="batchOid" name="batch" property="externalId" />
<bean:define id="confirmMessage">
    <bean:message key="label.rectorateSubmission.confirmActions" bundle="ACADEMIC_OFFICE_RESOURCES" />
</bean:define>


<logic:notEmpty name="actions">
    <ul class="mtop1">
        <logic:iterate id="action" name="actions">
        	<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
				<li><html:link
					action="<%= "/rectorateDocumentSubmission.do?method=" + action +  "&amp;batchOid=" + batchOid %>">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
						key="<%= "link.rectorateSubmission." + action %>" />
				</html:link></li>
			</logic:present>
			<logic:present role="RECTORATE">
				<li><html:link
					action="<%= "/rectorateIncomingBatches.do?method=" + action +  "&amp;batchOid=" + batchOid %>">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
						key="<%= "link.rectorateSubmission." + action %>" />
				</html:link></li>
			</logic:present>
        </logic:iterate>
    </ul>
</logic:notEmpty>


<logic:notEmpty name="confirmActions">
    <ul class="mtop1">
        <logic:iterate id="action" name="confirmActions">
            <li><html:link onclick="<%= "return confirm('" + confirmMessage + "');" %>"
                action="<%= "/rectorateDocumentSubmission.do?method=" + action +  "&amp;batchOid=" + batchOid %>">
                <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="<%= "link.rectorateSubmission." + action %>" />
            </html:link></li>
        </logic:iterate>
    </ul>
</logic:notEmpty>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
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
            
		<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
            <fr:property name="linkFormat(view)" value="${viewStudentProgrammeLink}" />
            <fr:property name="param(view)" value="${viewStudentProgrammeParameters}" />
            <fr:property name="key(view)" value="link.rectorateSubmission.viewRegistration" />
            <fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
		</logic:present>
		
		<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
            <fr:property name="link(print)" value="/documentRequestsManagement.do?method=printDocument" />
            <fr:property name="param(print)" value="idInternal/documentRequestId" />
            <fr:property name="key(print)" value="link.rectorateSubmission.reprint" />
            <fr:property name="bundle(print)" value="ACADEMIC_OFFICE_RESOURCES" />
        </logic:present>
<%-- 
        <logic:present role="RECTORATE">
            <fr:property name="link(print)" value="<%= "/rectorateIncomingBatches.do?method=printDocument&batchOid=" + batchOid %>"/>
        </logic:present>
--%>

		<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
            <fr:property name="linkFormat(receive)" value="${receivedActionLink}" />
            <fr:property name="param(receive)" value="idInternal/academicServiceRequestId" />
            <fr:property name="key(receive)" value="link.rectorateSubmission.receive" />
            <fr:property name="bundle(receive)" value="ACADEMIC_OFFICE_RESOURCES" />
            <fr:property name="visibleIf(receive)" value="activeSituation.sentToExternalEntity" />
		</logic:present>
		
            <fr:property name="link(delay)" value="/rectorateDocumentSubmission.do?method=delayRequest" />
            <fr:property name="param(delay)" value="externalId/academicServiceRequestOid" />
            <fr:property name="key(delay)" value="link.rectorateSubmission.delay" />
            <fr:property name="bundle(delay)" value="ACADEMIC_OFFICE_RESOURCES" />
            <fr:property name="visibleIf(delay)" value="rectorateSubmissionBatch.closed" />
            <fr:property name="visibleIfNot(delay)" value="piggyBackedOnRegistry" />
            <fr:property name="confirmationKey(delay)" value="label.rectorateSubmission.delay" />
            <fr:property name="confirmationBundle(delay)" value="ACADEMIC_OFFICE_RESOURCES" />
        </fr:layout>
    </fr:view>
</logic:notEmpty>