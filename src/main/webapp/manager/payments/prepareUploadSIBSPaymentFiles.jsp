<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:messages id="message" message="true" bundle="MANAGER_RESOURCES" property="error">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message"/></span>
	</p>
</html:messages>

<fr:edit id="uploadBean" name="uploadBean" schema="SIBSPaymentFileUpload" action="/SIBSPayments.do?method=uploadSIBSPaymentFiles">
	<fr:destination name="cancel" path="/SIBSPayments.do?method=prepareUploadSIBSPaymentFiles"/>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<html:messages id="message" message="true" bundle="MANAGER_RESOURCES" property="message">
	<p>
		<bean:write name="message" />
	</p>
</html:messages>
