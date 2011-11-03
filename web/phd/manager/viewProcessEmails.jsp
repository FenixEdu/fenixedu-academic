<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<logic:present role="MANAGER">

<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.SendPhdEmail"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmail" %>

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manage.emails" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="process" name="process" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<p>
	<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + processId %>">
		« <bean:message key="label.back" bundle="PHD_RESOURCES" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%-- ### Operational Area ### --%>

<%-- ### History Area ### --%>
<logic:empty name="process" property="phdIndividualProgramProcessEmails">
	<p>
		<em><bean:message key="message.phd.email.do.not.exist" bundle="PHD_RESOURCES" />.</em>
	</p>
</logic:empty>

<logic:notEmpty name="process" property="phdIndividualProgramProcessEmails">
	<fr:view name="process" property="phdIndividualProgramProcessEmails">
	
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdIndividualProgramProcessEmail.class.getName() %>">
			<fr:slot name="whenCreated" layout="year-month"/>
			<fr:slot name="formattedSubject" /> 
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:link name=""  label="label.view,PHD_RESOURCES"
				link="<%= "/phdIndividualProgramProcess.do?method=viewPhdEmail&phdEmailId=${externalId}&processId=" + processId %>"/>
	
		</fr:layout>
	</fr:view>
</logic:notEmpty>

</logic:present>