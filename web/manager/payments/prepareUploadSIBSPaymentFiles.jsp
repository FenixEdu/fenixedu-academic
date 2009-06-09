<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">
	<fr:edit id="uploadBean" name="uploadBean" schema="SIBSPaymentFileUpload" action="/SIBSPayments.do?method=uploadSIBSPaymentFiles">
		<fr:destination name="cancel" path="/SIBSPayments.do?method=prepareUploadSIBSPaymentFiles"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
		<p>
			<bean:write name="message" />
		</p>
	</html:messages>
</logic:present>
