<%@page import="net.sourceforge.fenixedu.domain.phd.email.PhdProgramEmail"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.phd.providers.PhdProgramsAssociatedToCoordinator"  %>

<html:xhtml/>

<logic:present role="COORDINATOR">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manage.emails" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<logic:empty name="phdEmailBean" property="phdProgram">
	<fr:form action="/phdIndividualProgramProcess.do?method=manageEmailBeanPostback">
		<fr:edit id="phdEmailBean" name="phdEmailBean">
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramEmail.class %>">
				<fr:slot name="phdProgram" layout="menu-select-postback">
					<fr:property name="providerClass" value="<%= PhdProgramsAssociatedToCoordinator.class %>" />
					<fr:property name="format" value="${name}" />
				</fr:slot>
			</fr:schema>
		</fr:edit>
	</fr:form>
</logic:empty>

<logic:notEmpty name="phdEmailBean" property="phdProgram" >

<bean:define id="program" name="phdEmailBean" property="phdProgram" /> 

<logic:empty name="program" property="phdProgramEmails">
	<p>
		<em><bean:message key="message.phd.email.do.not.exist" bundle="PHD_RESOURCES" />.</em>
	</p>
</logic:empty>

<logic:notEmpty name="program" property="phdProgramEmails">
	<fr:view name="program" property="phdProgramEmails">
	
		<fr:schema bundle="PHD_RESOURCES" type="<%= net.sourceforge.fenixedu.domain.phd.email.PhdProgramEmail.class.getName() %>">
			<fr:slot name="whenCreated" layout="year-month"/>
			<fr:slot name="subject" />
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:link name="view"  label="label.view,PHD_RESOURCES"
				link="/phdIndividualProgramProcess.do?method=viewPhdEmail&phdEmailId=${externalId}"/>
				
			<fr:link name="cancel" label="link.cancel,PHD_RESOURCES"
				link="/phdIndividualProgramProcess.do?method=cancelPhdEmail&phdEmailId=${externalId}"
				condition="cancelable"
				confirmation="message.net.sourceforge.fenixedu.domain.phd.email.PhdProgramEmail.cancel.confirmation" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

</logic:notEmpty>

</logic:present>
