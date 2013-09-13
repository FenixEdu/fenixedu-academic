<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="administrative.office.services" bundle="STUDENT_RESOURCES"/></em>
<h2><bean:message key="documents.requirement.consult"/></h2>

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
		<fr:property name="linkFormat(view)" value="/documentRequest.do?method=viewDocumentRequest&documentRequestId=${externalId}"/>
		<fr:property name="key(view)" value="label.view"/>
		<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
		<fr:property name="linkFormat(cancel)" value="/documentRequest.do?method=prepareCancelAcademicServiceRequest&academicServiceRequestId=${externalId}&registrationID=${registration.externalId}"/>
		<fr:property name="key(cancel)" value="label.cancel"/>
		<fr:property name="bundle(cancel)" value="APPLICATION_RESOURCES"/>
		<fr:property name="visibleIf(cancel)" value="loggedPersonCanCancel"/>
		<fr:property name="sortBy" value="requestDate=desc, academicServiceRequestType=asc, urgentRequest=desc"/>
	</fr:layout>
</fr:view>
