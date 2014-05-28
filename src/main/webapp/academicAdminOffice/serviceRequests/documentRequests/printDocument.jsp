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
<html:xhtml/>

<h2 class="mbottom1"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="document.print" /></h2>

<bean:define id="academicServiceRequest" name="academicServiceRequest" scope="request" type="net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest"/>

<div style="float: right;">
	<bean:define id="personID" name="academicServiceRequest" property="registration.student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
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
	  		<fr:property name="rowClasses" value="tdhl1,,,"/>
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

<logic:equal name="documentRequest" property="toPrint" value="true">
<p>
	<span class="gen-button">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link page="/documentRequestsManagement.do?method=printDocument" paramId="documentRequestId" paramName="academicServiceRequest" paramProperty="externalId">
		<bean:message key="print" bundle="APPLICATION_RESOURCES"/>
	</html:link>
	</span>
</p>
</logic:equal>

<bean:define id="registrationID" name="academicServiceRequest" property="registration.externalId" />

<fr:form action="<%= "/academicServiceRequestsManagement.do?academicServiceRequestId=" + academicServiceRequest.getExternalId().toString() + "&amp;registrationID=" + registrationID.toString() %>">
	<html:hidden name="academicServiceRequestsManagementForm" property="method" value="concludeAcademicServiceRequest" />

	<p class="mtop15 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="documentRequest.confirmDocumentSuccessfulPrinting"/></strong></p>
	<logic:equal name="documentRequest" property="pagedDocument" value="true">
		<fr:edit id="documentRequestConclude" name="documentRequest" schema="DocumentRequest.conclude-info">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle thright thlight mtop025 mbottom1"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="<%="/documentRequestsManagement.do?method=prepareConcludeDocumentRequest&amp;academicServiceRequestId=" + academicServiceRequest.getExternalId().toString() %>"/>
		</fr:edit>
	</logic:equal>
	
	<logic:equal name="documentRequest" property="diploma" value="true">
		<fr:edit id="serviceRequestBean" name="serviceRequestBean" schema="AcademicServiceRequestBean.external.entity.edit">
			<fr:layout>
				<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
	</logic:equal>
	
	<%-- 
	<strong><bean:message key="label.serviceRequests.sendEmailToStudent" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong><html:radio name="academicServiceRequestsManagementForm" property="sendEmailToStudent" value="true"><bean:message key="label.yes" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:radio><html:radio name="academicServiceRequestsManagementForm" property="sendEmailToStudent" value="false"><bean:message key="label.no" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:radio>
	<br/>
	<br/>
	--%>
	<html:submit><bean:message key="label.documentRequestsManagement.conclude" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	<html:cancel onclick="this.form.method.value='backToViewRegistration'"><bean:message key="back" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>	
</fr:form>

