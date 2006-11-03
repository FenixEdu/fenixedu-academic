<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<bean:define id="academicServiceRequest" name="academicServiceRequest" type="net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest"/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:write name="academicServiceRequest" property="description"/></h2>

<hr style="margin-bottom: 2em;"/>

<html:messages id="messages" message="true">
	<p><span class="error0"><bean:write name="messages" bundle="ACADEMIC_OFFICE_RESOURCES"/></span></p>
</html:messages>

<p>
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.information"/></strong>
	<bean:define id="simpleClassName" name="academicServiceRequest" property="class.simpleName" />
	<fr:view name="academicServiceRequest" schema="<%= simpleClassName  + ".view"%>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value="listClasses,," />
		</fr:layout>
	</fr:view>
</p>

<logic:present name="academicServiceRequest" property="activeSituation">
	<p>
		<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.situation"/></strong>
		<fr:view name="academicServiceRequest" property="activeSituation" schema="AcademicServiceRequestSituation.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="columnClasses" value="listClasses,," />
			</fr:layout>
		</fr:view>
	</p>
</logic:present>

<bean:define id="url" name="url" type="java.lang.String"/>
<html:form action="<%=url%>">
	<html:hidden property="academicServiceRequestId" value="<%= academicServiceRequest.getIdInternal().toString() %>"/>
	<html:hidden property="registrationID" value="<%= academicServiceRequest.getRegistration().getIdInternal().toString() %>"/>
	
	<html:submit styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="back" /></html:submit>
</html:form>
