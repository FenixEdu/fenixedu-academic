<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2 class="mbottom1"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="document.print" /></h2>

<bean:define id="academicServiceRequest" name="academicServiceRequest" scope="request" type="net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest"/>

<div style="float: right;">
	<bean:define id="personID" name="academicServiceRequest" property="registration.student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="academicServiceRequest" property="registration.student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.information"/></strong></p>
<bean:define id="simpleClassName" name="academicServiceRequest" property="class.simpleName" />
<fr:view name="academicServiceRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
  		<fr:property name="rowClasses" value=",,,,,"/>
	</fr:layout>
</fr:view>


<logic:present name="academicServiceRequest" property="activeSituation">
	<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.situation"/></strong></p>
	<fr:view name="academicServiceRequest" property="activeSituation" schema="AcademicServiceRequestSituation.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
	  		<fr:property name="rowClasses" value=",,tdhl1,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p class="mtop1">
		<span class="warning0">
			<bean:write name="message" />
		</span>
	</p>
</html:messages>

<bean:define id="documentRequest" name="academicServiceRequest" type="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest"/>
<p>
	<logic:equal name="documentRequest" property="toBePrintedInAplica" value="true">
		<span class="warning0">
			<bean:message key="print.preBolonha.documentRequest.in.aplica" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</span>
	</logic:equal>
	<logic:equal name="documentRequest" property="toBePrintedInAplica" value="false">
		<span class="gen-button">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/documentRequestsManagement.do?method=printDocument" paramId="documentRequestId" paramName="academicServiceRequest" paramProperty="idInternal">
			<bean:message key="print" bundle="APPLICATION_RESOURCES"/>
		</html:link>
		</span>
	</logic:equal>
</p>

<bean:define id="registrationID" name="academicServiceRequest" property="registration.idInternal" />

<fr:form action="<%= "/academicServiceRequestsManagement.do?academicServiceRequestId=" + academicServiceRequest.getIdInternal().toString() + "&amp;registrationID=" + registrationID.toString() %>">
	<html:hidden name="academicServiceRequestsManagementForm" property="method" value="concludeAcademicServiceRequest" />

	<p class="mtop15 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="documentRequest.confirmDocumentSuccessfulPrinting"/></strong></p>
	<logic:equal name="documentRequest" property="pagedDocument" value="true">
		<fr:edit id="documentRequestConclude" name="documentRequest" schema="DocumentRequest.conclude-info">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle thright thlight mtop025 mbottom1"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="<%="/documentRequestsManagement.do?method=prepareConcludeDocumentRequest&amp;academicServiceRequestId=" + academicServiceRequest.getIdInternal().toString() %>"/>
		</fr:edit>
	</logic:equal>
	<%-- 
	<strong><bean:message key="label.serviceRequests.sendEmailToStudent" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong><html:radio name="academicServiceRequestsManagementForm" property="sendEmailToStudent" value="true"><bean:message key="label.yes" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:radio><html:radio name="academicServiceRequestsManagementForm" property="sendEmailToStudent" value="false"><bean:message key="label.no" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:radio>
	<br/>
	<br/>
	--%>
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	<html:cancel onclick="this.form.method.value='backToViewRegistration'"><bean:message key="back" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>	
</fr:form>

