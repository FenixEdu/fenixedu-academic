<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><strong><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>

<table >
	<tr>
		<td>
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4"/>
			      	<fr:property name="columnClasses" value="listClasses,,"/>
				</fr:layout>
			</fr:view>
		</td>
		<td>
			<bean:define id="personID" name="registration" property="student.person.idInternal"/>
			<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
		</td>
	</tr>
</table>
<br/>

<logic:present name="registration" property="ingressionEnum">
<h2><strong><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
</logic:present>
<logic:notPresent name="registration" property="ingressionEnum">
<h2><strong><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<html:link page="/viewCurriculum.do?method=getCurriculum" paramId="studentNumber" paramName="registration" paramProperty="number">
	<bean:message key="link.student.viewCurriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/>
</html:link>

<logic:equal name="registration" property="degreeType.name" value="BOLONHA_ADVANCED_FORMATION_DIPLOMA">
	<br/>
	<html:link page="/manageEnrolmentModel.do?method=prepare" paramId="registrationID" paramName="registration" paramProperty="idInternal">
		<bean:message key="link.student.manageEnrolmentModel" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</logic:equal>

<br/><br/>
<h2><strong><bean:message key="label.studentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" property="studentCurricularPlans" schema="student.studentCurricularPlans" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
		<fr:property name="linkFormat(enrol)" value="/studentEnrolments.do?method=prepare&scpID=${idInternal}" />
		<fr:property name="key(enrol)" value="link.student.enrolInCourses"/>
		<fr:property name="bundle(enrol)" value="ACADEMIC_OFFICE_RESOURCES"/>
		<fr:property name="visibleIf(enrol)" value="active"/>
		<fr:property name="contextRelative(enrol)" value="true"/>      	
	</fr:layout>
</fr:view>
<br/>

<logic:present name="registration" property="studentCandidacy">
	<h2><strong><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
	<fr:view name="registration" property="studentCandidacy.precedentDegreeInformation" schema="student.precedentDegreeInformation" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	      	<fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<%-- 
<br/>
<h2><strong><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<p>
	<html:link action="/documentRequestsManagement.do?method=prepareCreateDocumentRequest" paramId="registrationId" paramName="registration" paramProperty="idInternal">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.createDocumentRequest"/>
	</html:link>	
</p>
<p>
	<html:link action="/documentRequestsManagement.do?method=viewRegistrationDocumentRequestsHistoric" paramId="registrationID" paramName="registration" paramProperty="idInternal">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="view.historic"/>
	</html:link>	
</p>

<p>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="new.document.requests"/>
	<bean:define id="registration" name="registration" scope="request" type="net.sourceforge.fenixedu.domain.student.Registration"/>
	<bean:define id="newDocumentRequests" name="registration" property="newDocumentRequests"/>
	<logic:notEmpty name="newDocumentRequests">

		<html:form action="<%= "/documentRequestsManagement.do?registrationID=" + registration.getIdInternal()%>">
			<html:hidden property="method"/>
			
			<fr:view name="newDocumentRequests" schema="DocumentRequest.for-given-registration">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thlight thcenter" />
					<fr:property name="checkable" value="true" />
					<fr:property name="checkboxName" value="documentIdsToProcess" />
					<fr:property name="checkboxValue" value="idInternal" />		
					<fr:property name="sortBy" value="urgentRequest=desc,creationDate=desc"/>
				</fr:layout>
			</fr:view>
			<html:submit onclick="this.form.method.value='processNewDocuments';" styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.process" /></html:submit>
		</html:form>
	</logic:notEmpty>
	<logic:empty name="newDocumentRequests">
		<p>
			<span class="warning0">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.noNewDocumentRequest"/>
			</span>
		</p>
	</logic:empty>
</p>

<p>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="processing.document.requests"/>
	<bean:define id="processingDocumentRequests" name="registration" property="processingDocumentRequests"/>
	<logic:notEmpty name="processingDocumentRequests">
		<fr:view name="processingDocumentRequests" schema="DocumentRequest.summary-view-by-student">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright" />
				
				<fr:property name="linkFormat(edit)" value="/documentRequestsManagement.do?method=prepareEditDocumentRequest&documentRequestId=${idInternal}"/>
				<fr:property name="key(edit)" value="edit"/>
		
				<fr:property name="linkFormat(print)" value="/documentRequestsManagement.do?method=printDocument&documentRequestId=${idInternal}"/>
				<fr:property name="key(print)" value="print"/>
		
				<fr:property name="sortBy" value="creationDate=desc, documentRequestType=asc, urgentRequest=desc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="processingDocumentRequests">
		<p>
			<span class="warning0">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.noProcessingDocumentRequest"/>
			</span>
		</p>
	</logic:empty>
</p>

--%>

<p style="margin-top: 4em;">
	<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="registration" paramProperty="student.idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>
