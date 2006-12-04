<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2 class="mbottom1"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="document.print" /></h2>

<p class="mbottom025"><strong><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
<bean:define id="academicServiceRequest" name="academicServiceRequest" scope="request" type="net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest"/>
<fr:view name="academicServiceRequest" property="registration" schema="AcademicServiceRequest.registration">
<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
  		<fr:property name="rowClasses" value="tdhl1,,,,"/>
	</fr:layout>
</fr:view>



<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.information"/></strong></p>
<bean:define id="simpleClassName" name="academicServiceRequest" property="class.simpleName" />
<fr:view name="academicServiceRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
  		<fr:property name="rowClasses" value=",tdhl1,,,,"/>
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

<logic:messagesNotPresent message="true">
	<p>
		<span class="gen-button">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/documentRequestsManagement.do?method=printDocument" paramId="documentRequestId" paramName="academicServiceRequest" paramProperty="idInternal">
			<bean:message key="print" bundle="APPLICATION_RESOURCES"/>
		</html:link>
		</span>
	</p>
</logic:messagesNotPresent>
<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p class="mvert0">
		<span class="warning0">
			<bean:write name="message" />
		</span>
	</p>
</html:messages>

<p class="mtop1 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="documentRequest.confirmDocumentSuccessfulPrinting"/></strong></p>
	<bean:define id="documentRequest" name="academicServiceRequest" type="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest"/>
	<logic:equal name="documentRequest" property="pagedDocument" value="true">
		<fr:edit id="documentRequestConclude" name="documentRequest" 
				schema="DocumentRequest.conclude-info"
				action="<%="/academicServiceRequestsManagement.do?method=concludeAcademicServiceRequest&academicServiceRequestId=" + academicServiceRequest.getIdInternal().toString()%>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/student.do?method=visualizeRegistration&registrationID=" + academicServiceRequest.getRegistration().getIdInternal().toString()%>"/>
		</fr:edit>
	</logic:equal>
	<logic:equal name="documentRequest" property="pagedDocument" value="false">
		<p>
			<span>
					<html:form action="<%="/academicServiceRequestsManagement.do?method=concludeAcademicServiceRequest&academicServiceRequestId=" + academicServiceRequest.getIdInternal().toString()%>">
						<html:submit styleClass="inputbutton"><bean:message key="conclude" bundle="APPLICATION_RESOURCES"/></html:submit>
					</html:form>		
			</span>
			<span>
					<html:form action="<%="/student.do?method=visualizeRegistration&registrationID=" + academicServiceRequest.getRegistration().getIdInternal().toString()%>">
						<html:submit styleClass="inputbutton"><bean:message key="cancel" bundle="APPLICATION_RESOURCES"/></html:submit>
					</html:form>
			</span>
		</p>
	</logic:equal>
