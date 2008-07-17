<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="label.debtManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>


	<p class="mtop15 mbottom025">
		<bean:message key="label.currentYear.prices" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>:
	</p>
	<table class="tstyle1 thright thlight mvert05">
	<tr>
		<th>
			<bean:message key="label.roomValues" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
		</th>
		<td>
			<logic:present name="currentResidenceYear" property="singleRoomValue">
				<fr:view name="currentResidenceYear" property="singleRoomValue"/> 
				<bean:message key="label.currency.symbol" bundle="GLOBAL_RESOURCES"/> | 
			</logic:present>
			<logic:present name="currentResidenceYear" property="doubleRoomValue">
				<fr:view name="currentResidenceYear" property="doubleRoomValue"/> <bean:message key="label.currency.symbol" bundle="GLOBAL_RESOURCES"/>
			</logic:present> 
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.paymentDate" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
		</th>
		<td>
			<fr:view name="currentResidenceYear" property="paymentLimitDay" type="java.lang.Integer">
				<fr:layout name="null-as-label">
					<property name="subLayout" value="values"/>
					<property name="label" value="-"/>			
				</fr:layout>
			</fr:view>
		</td>
	</tr>
	</table>


<fr:form action="/residenceEventManagement.do">
	<fr:edit id="searchEventMonth" name="searchBean" schema="edit.import.residence.bean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thleft thlight"/>
			</fr:layout>
			<fr:destination name="postback" path="/residenceEventManagement.do?method=manageResidenceEvents"/>
	</fr:edit>
</fr:form>


<logic:present name="searchBean">
	<logic:present name="searchBean" property="residenceMonth">
		<bean:define id="monthOID" name="searchBean" property="residenceMonth.OID"/>

		<p class="mtop05">
			<html:link page="<%=  "/residenceManagement.do?method=importData&monthOID=" + monthOID %>">
				<bean:message key="label.importFile" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
			</html:link>
			|
			<html:link page="<%=  "/residenceManagement.do?method=editPaymentLimitDay&monthOID=" + monthOID %>">
				<bean:message key="label.modify.limitPaymentDay" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
			</html:link>
			|
			<html:link page="<%=  "/residenceManagement.do?method=editRoomValues&monthOID=" + monthOID %>">
				<bean:message key="label.modify.roomValues" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
			</html:link>
		</p>

		<logic:notEmpty name="searchBean" property="residenceMonth.events">		
			<p  class="mtop15 mbottom05"><bean:message key="label.residents.with.paymentCodes" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>:</p>
			<logic:notEmpty name="searchBean" property="residenceMonth.eventsWithPaymentCodes">
				<fr:view name="searchBean" property="residenceMonth.eventsWithPaymentCodes" schema="show.residenceEvent.with.dates">	
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1 tdcenter thlight mtop05"/>
							<fr:property name="columnClasses" value=",,aleft,,,,,"/>
							<fr:property name="sortBy" value="person.student.number"/>
						</fr:layout>
						<fr:destination name="personLink" path="/residenceEventManagement.do?method=viewPersonResidenceEvents&person=${person.OID}&monthOID=${residenceMonth.OID}"/>
				</fr:view>
			</logic:notEmpty>
			<logic:empty name="searchBean" property="residenceMonth.eventsWithPaymentCodes">
				<p class="mtop05"><em><bean:message key="message.month.no.payments.with.codes" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>.</em></p>
			</logic:empty>
			
			<p  class="mtop2 mbottom05"><bean:message key="label.residents.with.no.paymentCodes" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>:</p>
			<logic:notEmpty name="searchBean" property="residenceMonth.eventsWithoutPaymentCodes">
				<fr:view name="searchBean" property="residenceMonth.eventsWithoutPaymentCodes" schema="show.residenceEvent">	
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1 tdcenter thlight mtop05"/>
							<fr:property name="columnClasses" value=",,aleft,,,,,"/>
							<fr:property name="sortBy" value="person.student.number"/>
						</fr:layout>
						<fr:destination name="personLink" path="/residenceEventManagement.do?method=viewPersonResidenceEvents&person=${person.OID}&monthOID=${residenceMonth.OID}"/>
				</fr:view>
			</logic:notEmpty>
			<logic:empty name="searchBean" property="residenceMonth.eventsWithoutPaymentCodes">
				<p class="mtop05"><em><bean:message key="message.month.no.payments.without.codes" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>.</em></p>
			</logic:empty>
			
		</logic:notEmpty>
		
		<logic:empty name="searchBean" property="residenceMonth.events">
			<p><em><bean:message key="message.month.empty.events" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>.</em></p>
		</logic:empty>
		
		
		
		
	</logic:present>
</logic:present>

