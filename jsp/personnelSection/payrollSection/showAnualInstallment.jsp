<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.payrollSectionStaff" /></em>
<h2><bean:message key="title.bonusInstallmentsManagement"/></h2>


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
	<fr:form action="/anualInstallments.do?method=editAnualInstallment" encoding="multipart/form-data">
		<fr:edit id="anualBonusInstallmentBean" name="anualBonusInstallmentFactory" visible="false">
			<fr:layout>
				<fr:property name="classes" value="thlight" />
			</fr:layout>
		</fr:edit>
		<logic:notPresent name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList">
			<logic:notPresent name="anualBonusInstallmentFactory" property="year">
				<fr:edit id="anualBonusInstallmentFactory" name="anualBonusInstallmentFactory" schema="edit.anualBonusInstallmentFactory.year">
					<fr:layout>
						<fr:property name="classes" value="thlight" />
					</fr:layout>
				</fr:edit>
			</logic:notPresent>
			<logic:present name="anualBonusInstallmentFactory" property="year">
				<fr:edit id="anualBonusInstallmentFactory" name="anualBonusInstallmentFactory" schema="edit.anualBonusInstallmentFactory">
					<fr:layout>
						<fr:property name="classes" value="thlight" />
					</fr:layout>
				</fr:edit>
			</logic:present>
			<p>
				<html:submit><bean:message key="button.choose" /></html:submit>
			</p>
		</logic:notPresent>

		<logic:present name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList">
			<logic:equal name="anualBonusInstallmentFactory" property="canEditAnualBonusInstallment" value="false">
				<fr:view name="anualBonusInstallmentFactory" schema="edit.anualBonusInstallmentFactory">
					<fr:layout>
						<fr:property name="classes" value="tstyle2 thlight thright thwhite tdbold" />
					</fr:layout>
				</fr:view>
				<table class="tstyle1 thlight thright ulindent075">
					<tr>
					<th><bean:message key="label.installment" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
					<logic:iterate id="anualBonusInstallmentBean" name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList" indexId="index">
						<bean:define id="myIndex" value="<%=new Integer( index.intValue()+1).toString()%>"/>
						<th style="text-align: center !important;"><bean:write name="myIndex"/>ª</th>
					</logic:iterate>
					</tr><tr><th><bean:message key="label.payments" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
					<logic:iterate id="anualBonusInstallmentBean" name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList">
						<td class="acenter">
						<fr:view name="anualBonusInstallmentBean" property="paymentYearMonth" layout="values" schema="show.date"/>
						</td>
					</logic:iterate>
					</tr><tr><th><bean:message key="label.months" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
					<logic:iterate id="anualBonusInstallmentBean" name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList">
						<td class="valigntop">
						<fr:view  name="anualBonusInstallmentBean" property="yearMonths" layout="option-select">
							<fr:layout>
								<fr:property name="eachLayout" value="values" />
								<fr:property name="saveOptions" value="true" />
								<fr:property name="eachSchema" value="show.date" />
								<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.personnelSection.payrollSection.bonus.InstallmentYearMonthAsProvider" />
							</fr:layout>
						</fr:view>
						</td>
					</logic:iterate>
				</tr>
				</table>
				<p>
					<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back">
						<bean:message key="button.back"/>
					</html:cancel>
				</p>
			</logic:equal>


			<logic:equal name="anualBonusInstallmentFactory" property="canEditAnualBonusInstallment" value="true">
				<fr:view name="anualBonusInstallmentFactory" schema="edit.anualBonusInstallmentFactory"/>
				<table class="tstyle1 thlight thright ulindent05">
				<tr>
					<th><bean:message key="label.installment" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
					<logic:iterate id="anualBonusInstallmentBean" name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList" indexId="index">
						<bean:define id="myIndex" value="<%=new Integer( index.intValue()+1).toString()%>"/>
						<th style="text-align: center !important;"><bean:write name="myIndex"/>ª</th>
					</logic:iterate>
					</tr><tr><th><bean:message key="label.payments" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
					<logic:iterate id="anualBonusInstallmentBean" name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList">
						<td>
						<fr:edit id="anualBonusInstallmentBean" name="anualBonusInstallmentBean" slot="paymentYearMonth" layout="menu-select" schema="choose.date"
							validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
							<fr:layout>
								<fr:property name="eachLayout" value="values" />
								<fr:property name="saveOptions" value="true" />
								<fr:property name="eachSchema" value="show.date" />
								<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.personnelSection.payrollSection.bonus.PaymentYearMonthAsProvider" />
							</fr:layout>
						</fr:edit>
						</td>
					</logic:iterate>
					</tr><tr><th><bean:message key="label.months" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
					<logic:iterate id="anualBonusInstallmentBean" name="anualBonusInstallmentFactory" property="anualBonusInstallmentBeanList">
						<td>
						<fr:edit id="anualBonusInstallmentBean" name="anualBonusInstallmentBean" slot="yearMonths" layout="option-select" 
							validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
							<fr:layout>
								<fr:property name="eachLayout" value="values" />
								<fr:property name="saveOptions" value="true" />
								<fr:property name="eachSchema" value="show.date" />
								<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.personnelSection.payrollSection.bonus.InstallmentYearMonthAsProvider" />
							</fr:layout>
						</fr:edit>
						</td>
					</logic:iterate>
				</tr>
				</table>
				
				<p>
					<html:submit><bean:message key="button.choose" /></html:submit>
					<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back">
						<bean:message key="button.cancel"/>
					</html:cancel>
				</p>
			</logic:equal>
		</logic:present>		
	</fr:form>
</logic:present>

<logic:present name="anualBonusInstallmentsList">
	<fr:view name="anualBonusInstallmentsList" schema="edit.anualBonusInstallmentFactory">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight printborder" />
			<fr:property name="columnClasses" value="acenter" />
			<fr:property name="headerClasses" value="acenter" />
            <fr:property name="link(edit)" value="<%="/anualInstallments.do?method=editAnualInstallment"%>" />
			<fr:property name="key(edit)" value="label.edit" />
			<fr:property name="param(edit)" value="year" />
			<fr:property name="bundle(edit)" value="ASSIDUOUSNESS_RESOURCES" />
		</fr:layout>
	</fr:view>
</logic:present>

