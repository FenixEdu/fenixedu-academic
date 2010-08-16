<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<br/><br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<strong><bean:message  key="label.phd.messages" bundle="PHD_RESOURCES"/></strong><br/>
<logic:notEmpty name="alertMessages">
	<fr:view schema="PhdAlertMessage.view.for.process" name="alertMessages">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="linkFormat(markAsReaded)" value="/phdIndividualProgramProcess.do?method=markAlertMessageAsReaded&global=false&processId=${process.externalId}&alertMessageId=${externalId}"/>
				<fr:property name="key(markAsReaded)" value="label.mark.alert.message.as.readed"/>
				<fr:property name="bundle(markAsReaded)" value="PHD_RESOURCES"/>
				<fr:property name="order(markAsReaded)" value="0"/>
				<fr:property name="visibleIfNot(markAsReaded)" value="readed"/>
				<fr:property name="confirmationKey(markAsReaded)" value="message.confirm.alertMessage.mark.as.readed" />
				<fr:property name="confirmationBundle(markAsReaded)" value="PHD_RESOURCES" />
				
				<fr:property name="sortBy" value="whenCreated=desc" />
		</fr:layout>
	</fr:view>	
</logic:notEmpty>
<logic:empty name="alertMessages">
	<bean:message  key="label.phd.noAlertMessages" bundle="PHD_RESOURCES"/>
</logic:empty>

<%--  ### End of Operation Area  ### --%>
