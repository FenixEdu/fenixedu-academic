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

<html:link page="/manageRegistrationState.do?method=prepare" paramId="registrationId" paramName="registration" paramProperty="idInternal">
	<bean:message key="link.student.manageRegistrationState" bundle="ACADEMIC_OFFICE_RESOURCES"/>
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
		<fr:property name="visibleIf(enrol)" value="enrolable"/>
		<fr:property name="contextRelative(enrol)" value="true"/>      	
	</fr:layout>
</fr:view>
<logic:equal name="registration" property="active" value="true">
<ul>
	<li>
		<html:link action="/addNewStudentCurricularPlan.do?method=prepareCreateSCP" paramName="registration" paramProperty="idInternal" paramId="registrationId">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.registration.addNewSCP" />
		</html:link>
	</li>
</ul>
</logic:equal>

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

<br/>
<h2><strong><bean:message key="academic.services" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<bean:define id="registration" name="registration" scope="request" type="net.sourceforge.fenixedu.domain.student.Registration"/>
<p>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.createDocumentRequest"/>:
	<html:link action="/documentRequestsManagement.do?method=prepareCreateDocumentRequest" paramId="registrationId" paramName="registration" paramProperty="idInternal">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.document"/>
	</html:link>	
</p>
<p>
	<html:link action="/academicServiceRequestsManagement.do?method=viewRegistrationAcademicServiceRequestsHistoric" paramId="registrationID" paramName="registration" paramProperty="idInternal">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/>
	</html:link>	
</p>
<p>
	<b><bean:message key="new.requests" bundle="ACADEMIC_OFFICE_RESOURCES"/></b>
	<bean:define id="newAcademicServiceRequests" name="registration" property="newAcademicServiceRequests"/>
	<logic:notEmpty name="newAcademicServiceRequests">
		<fr:view name="newAcademicServiceRequests" schema="AcademicServiceRequest.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright" />
				
				<fr:property name="linkFormat(cancel)" value="/academicServiceRequestsManagement.do?method=prepareCancelAcademicServiceRequest&academicServiceRequestId=${idInternal}&registrationID=${registration.idInternal}"/>
				<fr:property name="key(cancel)" value="cancel"/>
		
				<fr:property name="linkFormat(reject)" value="/academicServiceRequestsManagement.do?method=prepareRejectAcademicServiceRequest&academicServiceRequestId=${idInternal}&registrationID=${registration.idInternal}"/>
				<fr:property name="key(reject)" value="reject"/>
		
				<fr:property name="linkFormat(processing)" value="/academicServiceRequestsManagement.do?method=processNewAcademicServiceRequest&academicServiceRequestId=${idInternal}"/>
				<fr:property name="key(processing)" value="processing"/>
		
				<fr:property name="linkFormat(view)" value="/academicServiceRequestsManagement.do?method=viewAcademicServiceRequest&academicServiceRequestId=${idInternal}&backAction=student&backMethod=visualizeRegistration"/>
				<fr:property name="key(view)" value="view"/>
		
				<fr:property name="sortBy" value="creationDate=desc, urgentRequest=desc, description=asc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="newAcademicServiceRequests">
		<p>
			<span class="warning0">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.new.academic.service.requests"/>
			</span>
		</p>
	</logic:empty>
</p>
<p>
	<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="processing.requests"/></b>
	<bean:define id="processingAcademicServiceRequests" name="registration" property="processingAcademicServiceRequests"/>
	<logic:notEmpty name="processingAcademicServiceRequests">
		<fr:view name="processingAcademicServiceRequests" schema="AcademicServiceRequest.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright" />
				
				<fr:property name="linkFormat(cancel)" value="/academicServiceRequestsManagement.do?method=prepareCancelAcademicServiceRequest&academicServiceRequestId=${idInternal}&registrationID=${registration.idInternal}"/>
				<fr:property name="key(cancel)" value="cancel"/>
		
				<fr:property name="linkFormat(reject)" value="/academicServiceRequestsManagement.do?method=prepareRejectAcademicServiceRequest&academicServiceRequestId=${idInternal}&registrationID=${registration.idInternal}"/>
				<fr:property name="key(reject)" value="reject"/>
		
				<fr:property name="linkFormat(conclude)" value="/academicServiceRequestsManagement.do?method=prepareConcludeAcademicServiceRequest&academicServiceRequestId=${idInternal}"/>
				<fr:property name="key(conclude)" value="conclude"/>
		
				<fr:property name="linkFormat(view)" value="/academicServiceRequestsManagement.do?method=viewAcademicServiceRequest&academicServiceRequestId=${idInternal}&backAction=student&backMethod=visualizeRegistration"/>
				<fr:property name="key(view)" value="view"/>
		
				<fr:property name="sortBy" value="creationDate=desc, urgentRequest=desc, description=asc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="processingAcademicServiceRequests">
		<p>
			<span class="warning0">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.processing.academic.service.requests"/>
			</span>
		</p>
	</logic:empty>
</p>
<p>
	<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="concluded.requests"/></b>
	<bean:define id="concludedAcademicServiceRequests" name="registration" property="concludedAcademicServiceRequests"/>
	<logic:notEmpty name="concludedAcademicServiceRequests">
		<fr:view name="concludedAcademicServiceRequests" schema="AcademicServiceRequest.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright" />
				
				<fr:property name="linkFormat(deliver)" value="/academicServiceRequestsManagement.do?method=deliveredAcademicServiceRequest&academicServiceRequestId=${idInternal}"/>
				<fr:property name="key(deliver)" value="deliver"/>
		
				<fr:property name="linkFormat(view)" value="/academicServiceRequestsManagement.do?method=viewAcademicServiceRequest&academicServiceRequestId=${idInternal}&backAction=student&backMethod=visualizeRegistration"/>
				<fr:property name="key(view)" value="view"/>
		
				<fr:property name="sortBy" value="creationDate=desc, urgentRequest=desc, description=asc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="concludedAcademicServiceRequests">
		<p>
			<span class="warning0">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.concluded.academic.service.requests"/>
			</span>
		</p>
	</logic:empty>
</p>

<p style="margin-top: 4em;">
	<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="registration" paramProperty="student.idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>
