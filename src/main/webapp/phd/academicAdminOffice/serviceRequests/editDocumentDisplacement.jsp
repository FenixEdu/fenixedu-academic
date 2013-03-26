<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType"%><html:xhtml/>

<bean:define id="phdAcademicServiceRequest" name="phdAcademicServiceRequest" />
<bean:define id="phdAcademicServiceRequestId" name="phdAcademicServiceRequest" property="externalId" />

<html:link action="/phdAcademicServiceRequestManagement.do?method=viewAcademicServiceRequest" paramId="phdAcademicServiceRequestId" paramName="phdAcademicServiceRequestId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>

<fr:form action="<%= "/phdAcademicServiceRequestManagement.do?method=editDocumentDisplacement&phdAcademicServiceRequestId=" + phdAcademicServiceRequestId %>" >
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="bean-edit" name="bean">
		<fr:schema type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestDisplacementBean" bundle="PHD_RESOURCES" >
			<fr:slot name="horizontalOffset">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
			</fr:slot>
			<fr:slot name="verticalOffset">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,error1 clear" />
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
	
</fr:form>
