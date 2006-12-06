<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" %> 
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>


<div style="float: right;">
	<bean:define id="personID" name="documentRequestCreateBean" property="registration.student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="documentRequestCreateBean" property="registration.student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<logic:present name="documentRequestCreateBean" property="registration.ingressionEnum">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="documentRequestCreateBean" property="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",tdhl1,,,"/>
	</fr:layout>
</fr:view>
</logic:present>
<logic:notPresent name="documentRequestCreateBean" property="registration.ingressionEnum">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="documentRequestCreateBean" property="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",tdhl1,,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>



<fr:form action="/documentRequestsManagement.do?method=viewDocumentRequestToCreate">

<p class="mbottom025"><strong><bean:message key="message.document.to.request" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong></p>

	<fr:edit name="documentRequestCreateBean" schema="DocumentRequestCreateBean.chooseDocumentRequestType" type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean">
		<fr:destination name="documentRequestTypeChoosedPostBack" path="/documentRequestsManagement.do?method=documentRequestTypeChoosedPostBack"/>	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom0"/>
			<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
		</fr:layout>	
	</fr:edit>
	
	<logic:present name="additionalInformationSchemaName">
		<bean:define id="additionalInformationSchemaName" name="additionalInformationSchemaName" type="java.lang.String"/>
		<fr:edit name="documentRequestCreateBean" schema="<%= additionalInformationSchemaName %>" type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mvert0"/>
				<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
			</fr:layout>	
		</fr:edit>			
	</logic:present>
	
	<logic:notEmpty name="documentRequestCreateBean" property="chosenDocumentRequestType">
	
		<fr:edit name="documentRequestCreateBean" schema="DocumentRequestCreateBean.purposes" type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mvert0"/>
				<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
			</fr:layout>	
		</fr:edit>
	
		<fr:edit name="documentRequestCreateBean" schema="DocumentRequestCreateBean.notes" type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mvert0"/>
				<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
			</fr:layout>	
		</fr:edit>
	
	</logic:notEmpty>
	
	<p class="mtop15">
		<html:submit><bean:message key="button.continue"/></html:submit>
	</p>
	
</fr:form>