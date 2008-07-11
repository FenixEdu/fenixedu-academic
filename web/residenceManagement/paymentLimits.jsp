<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>



<fr:form action="/residenceManagement.do?method=importData">
<fr:edit id="paymentLimits" name="paymentLimits" schema="residence.bean.selectYear">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
	<fr:destination name="postback" path="/residenceManagement.do?method=configurePaymentLimits"/>
</fr:edit>

<logic:present name="paymentLimits" property="residenceYear">
	<table class="tstyle5 thleft thlight">
			<logic:iterate id="month" name="paymentLimits" property="residenceYear.sortedMonths">
				<tr>
					<td><fr:view name="month" property="month"/></td>
					<td>
					
					<logic:equal name="month" property="ableToEditPaymentLimitDate" value="true">
							<fr:edit name="month" slot="paymentLimitDay"> 
							 		<fr:layout>
							 			<fr:property name="size" value="3"/>
							 			<fr:property name="maxLength" value="2"/>
							 		</fr:layout>
							 </fr:edit>
					</logic:equal>
					<logic:equal name="month" property="ableToEditPaymentLimitDate" value="false">
							<fr:view name="month" property="paymentLimitDay"/> 
					</logic:equal>
					</td>
				</tr>
			</logic:iterate>
		</table>
</logic:present>

<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
</fr:form>

