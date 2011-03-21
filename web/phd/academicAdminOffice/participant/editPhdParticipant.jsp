<%@page import="net.sourceforge.fenixedu.domain.phd.PhdParticipant"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.view.participants" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="process" name="process" />
<bean:define id="processId" name="process" property="externalId" />

<p>
	<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
		« <bean:message bundle="PHD_RESOURCES" key="label.back" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>
 
<p><strong><bean:message key="label.phd.edit.participant" bundle="PHD_RESOURCES"/></strong></p>


<fr:form action="<%= "/phdIndividualProgramProcess.do?method=editPhdParticipant&amp;processId=" + processId %>" >
		<fr:edit id="phdParticipantBean" name="phdParticipantBean" visible="false" />
	
		<fr:edit name="phdParticipantBean">
			<logic:equal name="phdParticipantBean" property="participant.internal" value="true">
				<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant">
					<fr:slot name="title" />
					<fr:slot name="category" />
					<fr:slot name="workLocation" />
					<fr:slot name="institution" />
				</fr:schema>
			</logic:equal>
			
			<logic:equal name="phdParticipantBean" property="participant.internal" value="false">
				<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.ExternalPhdParticipant">
					<fr:slot name="name" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
					<fr:slot name="title" />
					<fr:slot name="qualification" />
					<fr:slot name="category" />
					<fr:slot name="workLocation" />
					<fr:slot name="institution" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
					<fr:slot name="address" />
					<fr:slot name="email">
						<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
						<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.EmailValidator" />
					</fr:slot>
				</fr:schema>
			</logic:equal>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			
			<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=editPhdParticipantInvalid&amp;processId=" + processId %>"/>
			<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewPhdParticipants&amp;processId=" + processId  %>" />		
		</fr:edit>
	
	<html:submit><bean:message key="label.edit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
	
</fr:form>

</logic:present>
