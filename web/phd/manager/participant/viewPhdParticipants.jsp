<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<logic:present role="MANAGER">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.view.participants" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="process" name="process" />
<bean:define id="processId" name="process" property="externalId" />

<p>
	<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>
 
<p><strong><bean:message  key="label.phd.edit.participant" bundle="PHD_RESOURCES"/></strong></p>

<logic:empty name="process" property="participants" >
	<br />
	<bean:message key="label.phd.participants.empty" bundle="PHD_RESOURCES" />
</logic:empty>

<logic:notEmpty name="process" property="participants">
	<br/>
	
	<fr:view name="process" property="participants">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdParticipant">
			<fr:slot name="name" />
			<fr:slot name="title" />
			<fr:slot name="category" />
			<fr:slot name="workLocation" />
			<fr:slot name="institution" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10 thleft" />
		</fr:layout>
	</fr:view>

</logic:notEmpty>

</logic:present>