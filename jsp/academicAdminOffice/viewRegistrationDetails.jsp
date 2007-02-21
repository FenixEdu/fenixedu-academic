<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.visualizeRegistration" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<ul class="mtop2 list5">
	<li>
		<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="registration" paramProperty="student.idInternal">
			<bean:message key="link.student.backToSudentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</li>
</ul>

<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<%--
	<span style="background-color: #fafade; border: 1px solid #f2f2a6; padding: 0.4em 0.6em;">
--%>



<%-- Registration Details --%>
<logic:present name="registration" property="ingressionEnum">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",tdhl1,,,"/>
	</fr:layout>
</fr:view>
</logic:present>
<logic:notPresent name="registration" property="ingressionEnum">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",tdhl1,,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<p class="mtop0">
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/registration.do?method=viewRegistrationCurriculum" paramId="registrationID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.registration.viewCurriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/manageRegistrationState.do?method=prepare" paramId="registrationId" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.student.manageRegistrationState" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</span>
	<span class="pleft1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/manageIngression.do?method=prepare" paramId="registrationId" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.student.manageIngression" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</span>
	<span class="pleft1">	
		<logic:equal name="registration" property="degreeType.name" value="BOLONHA_ADVANCED_FORMATION_DIPLOMA">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="/manageEnrolmentModel.do?method=prepare" paramId="registrationID" paramName="registration" paramProperty="idInternal">
				<bean:message key="link.student.manageEnrolmentModel" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</logic:equal>
	</span>
</p>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p class="mtop15">
		<span class="warning0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>


<%-- Curricular Plans --%>

<h3 class="mbottom05 mtop2"><bean:message key="label.studentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<fr:view name="registration" property="studentCurricularPlans" schema="student.studentCurricularPlans" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight thcenter mtop05"/>
		<fr:property name="groupLinks" value="false"/>
		<fr:property name="linkFormat(enrol)" value="/studentEnrolments.do?method=prepare&amp;scpID=${idInternal}" />
		<fr:property name="key(enrol)" value="link.student.enrolInCourses"/>
		<fr:property name="bundle(enrol)" value="ACADEMIC_OFFICE_RESOURCES"/>
		<fr:property name="visibleIf(enrol)" value="enrolable"/>
		<fr:property name="contextRelative(enrol)" value="true"/>      
		<fr:property name="order(enrol)" value="1"/>      					
<%-- 
		<fr:property name="linkFormat(dismissal)" value="/studentDismissals.do?method=manage&amp;scpID=${idInternal}" />
		<fr:property name="key(dismissal)" value="link.student.dismissal.management"/>
		<fr:property name="bundle(dismissal)" value="ACADEMIC_OFFICE_RESOURCES"/>
		<fr:property name="visibleIf(dismissal)" value="enrolable"/>
		<fr:property name="contextRelative(dismissal)" value="true"/>      	
		<fr:property name="order(dismissal)" value="2"/>
--%>		
	</fr:layout>
</fr:view>

<p class="mtop0">
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/viewCurriculum.do?method=prepare" paramId="registrationOID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.registration.viewStudentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</span>
	<logic:equal name="registration" property="active" value="true">
		<span class="pleft1">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link action="/addNewStudentCurricularPlan.do?method=prepareCreateSCP" paramName="registration" paramProperty="idInternal" paramId="registrationId">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.registration.newSCP" />
			</html:link>
		</span>
	</logic:equal>
</p>



<%-- Academic Services --%>

<h3 class="mtop2 mbottom05 separator2"><bean:message key="academic.services" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<bean:define id="registration" name="registration" scope="request" type="net.sourceforge.fenixedu.domain.student.Registration"/>
<p>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.createDocumentRequest"/>:
	<html:link action="/documentRequestsManagement.do?method=prepareCreateDocumentRequest&schema=DocumentRequestCreateBean.chooseDocumentRequestQuickType" paramId="registrationId" paramName="registration" paramProperty="idInternal">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="declarations"/>
	</html:link>	
	|
	<html:link action="/documentRequestsManagement.do?method=prepareCreateDocumentRequest&schema=DocumentRequestCreateBean.chooseDocumentRequestType" paramId="registrationId" paramName="registration" paramProperty="idInternal">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="certificates"/>
	</html:link>	
</p>

<p>
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link action="/academicServiceRequestsManagement.do?method=viewRegistrationAcademicServiceRequestsHistoric" paramId="registrationID" paramName="registration" paramProperty="idInternal">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/>
	</html:link>	
</p>

<p class="mtop2">
	<b><bean:message key="new.requests" bundle="ACADEMIC_OFFICE_RESOURCES"/></b>
	<bean:define id="newAcademicServiceRequests" name="registration" property="newAcademicServiceRequests"/>
	<logic:notEmpty name="newAcademicServiceRequests">
		<fr:view name="newAcademicServiceRequests" schema="AcademicServiceRequest.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop0" />
				<fr:property name="linkFormat(reject)" value="/academicServiceRequestsManagement.do?method=prepareRejectAcademicServiceRequest&amp;academicServiceRequestId=${idInternal}&amp;registrationID=${registration.idInternal}"/>
				<fr:property name="key(reject)" value="reject"/>
 				<%--<fr:property name="visibleIf(reject)" value="availableForEmployeeToActUpon"/>--%>
				<fr:property name="linkFormat(processing)" value="/academicServiceRequestsManagement.do?method=processNewAcademicServiceRequest&amp;academicServiceRequestId=${idInternal}"/>
				<fr:property name="key(processing)" value="processing"/>
				<%--<fr:property name="visibleIf(processing)" value="availableForEmployeeToActUpon"/>--%>
				<fr:property name="linkFormat(view)" value="/academicServiceRequestsManagement.do?method=viewAcademicServiceRequest&amp;academicServiceRequestId=${idInternal}&amp;backAction=student&amp;backMethod=visualizeRegistration"/>
				<fr:property name="key(view)" value="view"/>
				<fr:property name="sortBy" value="creationDate=desc, activeSituation.creationDate=desc, urgentRequest=desc, description=asc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="newAcademicServiceRequests">
		<p>
			<em>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.new.academic.service.requests"/>
			</em>
		</p>
	</logic:empty>
</p>


<p class="mtop2">
	<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="processing.requests"/></b>
	<bean:define id="processingAcademicServiceRequests" name="registration" property="processingAcademicServiceRequests"/>
	<logic:notEmpty name="processingAcademicServiceRequests">
		<fr:view name="processingAcademicServiceRequests" schema="AcademicServiceRequest.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop0" />
				<fr:property name="linkFormat(reject)" value="/academicServiceRequestsManagement.do?method=prepareRejectAcademicServiceRequest&amp;academicServiceRequestId=${idInternal}&amp;registrationID=${registration.idInternal}"/>
				<fr:property name="key(reject)" value="reject"/>
				<%--<fr:property name="visibleIf(reject)" value="availableForEmployeeToActUpon"/>--%>				
				<fr:property name="linkFormat(conclude)" value="/academicServiceRequestsManagement.do?method=prepareConcludeAcademicServiceRequest&amp;academicServiceRequestId=${idInternal}"/>
				<fr:property name="key(conclude)" value="conclude"/>
				<%--<fr:property name="visibleIf(conclude)" value="availableForEmployeeToActUpon"/>--%>				
				<fr:property name="linkFormat(view)" value="/academicServiceRequestsManagement.do?method=viewAcademicServiceRequest&amp;academicServiceRequestId=${idInternal}&amp;backAction=student&amp;backMethod=visualizeRegistration"/>
				<fr:property name="key(view)" value="view"/>
				<fr:property name="sortBy" value="creationDate=desc, activeSituation.creationDate=desc, urgentRequest=desc, description=asc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="processingAcademicServiceRequests">
		<p>
			<em>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.processing.academic.service.requests"/>
			</em>
		</p>
	</logic:empty>
</p>


<p class="mtop2">
	<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="concluded.requests"/></b>
	<bean:define id="concludedAcademicServiceRequests" name="registration" property="concludedAcademicServiceRequests"/>
	<logic:notEmpty name="concludedAcademicServiceRequests">
		<fr:view name="concludedAcademicServiceRequests" schema="AcademicServiceRequest.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop0" />
				<fr:property name="linkFormat(deliver)" value="/academicServiceRequestsManagement.do?method=deliveredAcademicServiceRequest&amp;academicServiceRequestId=${idInternal}"/>
				<fr:property name="key(deliver)" value="deliver"/>
				<%--<fr:property name="visibleIf(deliver)" value="availableForEmployeeToActUpon"/>--%>
				<fr:property name="linkFormat(print)" value="/documentRequestsManagement.do?method=printDocument&amp;documentRequestId=${idInternal}&amp;"/>
				<fr:property name="key(print)" value="print"/>
				<fr:property name="visibleIf(print)" value="documentRequestPrintedInFenix"/>
				<fr:property name="linkFormat(view)" value="/academicServiceRequestsManagement.do?method=viewAcademicServiceRequest&amp;academicServiceRequestId=${idInternal}&amp;backAction=student&amp;backMethod=visualizeRegistration"/>
				<fr:property name="key(view)" value="view"/>
				<fr:property name="order(view)" value="1"/>
				<fr:property name="order(print)" value="2"/>
				<fr:property name="order(deliver)" value="3"/>
				<fr:property name="sortBy" value="creationDate=desc, activeSituation.creationDate=desc, urgentRequest=desc, description=asc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="concludedAcademicServiceRequests">
		<p>
			<em>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.concluded.academic.service.requests"/>
			</em>
		</p>
	</logic:empty>
</p>


<%-- Precedence Info --%>

<logic:present name="registration" property="studentCandidacy">
	<h3 class="mtop2 mbottom05 separator2"><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" property="studentCandidacy.precedentDegreeInformation" schema="student.precedentDegreeInformation" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		</fr:layout>
	</fr:view>
</logic:present>


<%--
<ul class="mtop2">
	<li>
	<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="registration" paramProperty="student.idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
	</li>
</ul>
--%>
