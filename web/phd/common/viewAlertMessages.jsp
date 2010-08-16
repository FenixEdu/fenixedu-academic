<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<logic:notEmpty name="alertMessages">
	<fr:view schema="PhdAlertMessage.view" name="alertMessages">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
				
				<fr:property name="linkFormat(viewProcess)" value="/phdIndividualProgramProcess.do?method=viewProcess&backMethod=viewAlertMessages&processId=${process.externalId}"/>
				<fr:property name="key(viewProcess)" value="label.viewProcess"/>
				<fr:property name="bundle(viewProcess)" value="PHD_RESOURCES"/>
				<fr:property name="order(viewProcess)" value="0"/>

				<fr:property name="linkFormat(markAsReaded)" value="/phdIndividualProgramProcess.do?method=markAlertMessageAsReaded&global=true&processId=${process.externalId}&alertMessageId=${externalId}"/>
				<fr:property name="key(markAsReaded)" value="label.mark.alert.message.as.readed"/>
				<fr:property name="bundle(markAsReaded)" value="PHD_RESOURCES"/>
				<fr:property name="order(markAsReaded)" value="1"/>
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
