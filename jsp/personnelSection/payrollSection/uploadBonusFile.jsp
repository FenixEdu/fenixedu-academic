<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="title.payrollSectionStaff" /></em>
<h2><bean:message key="label.uploadBonusFile" /></h2>

<logic:messagesPresent message="true" property="message">
	<html:messages id="message" message="true">
		<p><span class="error0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>
<logic:messagesPresent message="true" property="successMessage">
	<html:messages id="successMessage" message="true">
		<p><span class="success0"><bean:write name="successMessage" /></span></p>
	</html:messages>
</logic:messagesPresent>

<fr:form action="/uploadAnualInstallments.do?method=uploadAnualInstallment" encoding="multipart/form-data">
	<logic:empty name="bonusInstallmentFileBean" property="year">
		<fr:edit id="bonusInstallmentFileBeanYear" name="bonusInstallmentFileBean" schema="upload.bonusInstallmentFileBean.year">
			<fr:destination name="bonusInstallmentFileBeanPostBack" path="/uploadAnualInstallments.do?method=chooseYear" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thmiddle"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
	</logic:empty>
	<logic:notEmpty name="bonusInstallmentFileBean" property="year">
		<fr:edit id="bonusInstallmentFileBean" name="bonusInstallmentFileBean" schema="upload.bonusInstallmentFileBean">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thmiddle"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	<p><html:submit>
		<bean:message key="button.confirm" />
	</html:submit></p>
	<p><html:submit onclick="this.form.method.value='prepareUploadAnualInstallment'">
		<bean:message key="button.cancel"/>
	</html:submit></p>
</fr:form>
