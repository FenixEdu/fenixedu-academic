<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.academicAdminOffice.alertMessages" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>

<%--  ### End Of Context Information  ### --%>


<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<logic:notEmpty name="alertMessages">
	<fr:view schema="PhdAlertMessage.view.detailed" name="alertMessages">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
				
				<fr:property name="linkFormat(viewProcess)" value="/phdIndividualProgramProcess.do?method=viewProcess&processId=${process.externalId}"/>
				<fr:property name="key(viewProcess)" value="label.viewProcess"/>
				<fr:property name="bundle(viewProcess)" value="PHD_RESOURCES"/>
				<fr:property name="order(viewProcess)" value="0"/>
				
				
				<fr:property name="linkFormat(delete)" value="/phdIndividualProgramProcess.do?method=deleteAlertMessage&alertMessageId=${externalId}"/>
				<fr:property name="key(delete)" value="label.delete"/>
				<fr:property name="bundle(delete)" value="PHD_RESOURCES"/>
				<fr:property name="confirmationKey(delete)" value="message.confirm.alertMessage.delete" />
				<fr:property name="confirmationBundle(delete)" value="PHD_RESOURCES" />
				<fr:property name="order(delete)" value="1" />
		</fr:layout>
	</fr:view>	
</logic:notEmpty>
<logic:empty name="alertMessages">
	<bean:message  key="label.phd.noAlertMessages" bundle="PHD_RESOURCES"/>
</logic:empty>


<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>