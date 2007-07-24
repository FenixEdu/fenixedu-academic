<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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


<logic:present name="anualBonusInstallmentFactory">
	<logic:equal name="anualBonusInstallmentFactory" property="canEditAnualBonusInstallment" value="true">
		<fr:form action="/anualInstallments.do?method=editAnualInstallment" encoding="multipart/form-data">
			<fr:edit id="anualBonusInstallmentBean" name="anualBonusInstallmentFactory" visible="false"/>
			<logic:notPresent name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList">
				<fr:edit id="anualBonusInstallmentFactory" name="anualBonusInstallmentFactory" schema="edit.anualBonusInstallmentFactory">
				</fr:edit>
			</logic:notPresent>
			<logic:present name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList">
				<fr:view name="anualBonusInstallmentFactory" schema="edit.anualBonusInstallmentFactory"/>
				<fr:edit id="anualBonusInstallmentList" name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList" schema="edit.anualBonusInstallmentBean">
					<fr:layout  name="tabular">
					</fr:layout>
				</fr:edit>
			</logic:present>
			<p><html:submit>
				<bean:message key="button.confirm" />
			</html:submit></p>
		</fr:form>
	</logic:equal>
	<logic:equal name="anualBonusInstallmentFactory" property="canEditAnualBonusInstallment" value="false">
	</logic:equal>
</logic:present>

<logic:present name="anualBonusInstallmentsList">
	<fr:view name="anualBonusInstallmentsList" schema="edit.anualBonusInstallmentFactory">
		<fr:layout name="tabular">
		</fr:layout>
	</fr:view>
</logic:present>

