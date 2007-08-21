<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="title.payrollSectionStaff" /></em>
<h2><bean:message key="label.consultAndExportBonus" /></h2>

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


<logic:present name="bonusInstallment">
	<fr:form action="/anualInstallments.do" encoding="multipart/form-data">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showBonusInstallment"/>
		<logic:empty name="bonusInstallment" property="year">
			<fr:edit id="bonusInstallmentYear" name="bonusInstallment" schema="choose.bonusInstallment.year">
				<fr:destination name="bonusInstallmentYearPostBack" path="/anualInstallments.do?method=showBonusInstallment" />
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thmiddle"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:edit>
			<p><html:submit>
				<bean:message key="button.confirm" />
			</html:submit></p>
		</logic:empty>
		<logic:notEmpty name="bonusInstallment" property="year">
			<fr:edit id="bonusInstallmentInstallment" name="bonusInstallment" schema="choose.bonusInstallment.installment">
				<fr:destination name="bonusInstallmentPostBack" path="/anualInstallments.do?method=showBonusInstallment" />
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thmiddle"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:edit>
			<p><html:submit onclick="this.form.method.value='showBonusInstallment';">
				<bean:message key="button.confirm" />
			</html:submit>
			<html:cancel onclick="this.form.method.value='showBonusInstallment';">
				<bean:message key="button.cancel"/>
			</html:cancel>
			<logic:present name="bonusInstallment" property="bonusInstallmentList">
				<logic:notEmpty name="bonusInstallment" property="bonusInstallmentList">
					<html:submit onclick="this.form.method.value='exportBonusInstallment';">
						<bean:message key="button.export"/>
					</html:submit>
					<html:submit onclick="this.form.method.value='exportBonusInstallmentToGIAF';">
						<bean:message key="button.exportGIAF"/>
					</html:submit>
				</logic:notEmpty>
			</logic:present>
			</p>
		</logic:notEmpty>

		<logic:present name="bonusInstallment" property="bonusInstallmentList">
			<fr:view name="bonusInstallment" property="bonusInstallmentList" schema="show.employeeBonusInstallment">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thmiddle"/>
				</fr:layout>
			</fr:view>
		</logic:present>
	</fr:form>
</logic:present>
