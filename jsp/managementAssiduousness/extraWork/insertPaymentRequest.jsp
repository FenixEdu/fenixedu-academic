<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.extraWork" /></em>
<h2><bean:message key="link.paymentRequest" /></h2>
<p class="mtop2"><span class="error0"><html:errors /></span></p>
<bean:define id="employee" name="UserView" property="person.employee" />
<logic:present name="extraWorkRequestFactory">
	<fr:view name="extraWorkRequestFactory" schema="show.extraWorkRequestHeader">
		<fr:layout>
			<fr:property name="classes" value="thlight thleft" />
		</fr:layout>
	</fr:view>
	<br/>
	<span class="error0 mtop0"><html:messages id="message" message="true">
		<bean:write name="message" />
		<br />
	</html:messages></span>
	<logic:equal name="extraWorkRequestFactory" property="isMonthClosedForExtraWork" value="false">
	<fr:form action="/extraWorkPaymentRequest.do?method=insertPaymentRequest" encoding="multipart/form-data">
	<table>
	<tr align="left"><td>
		<strong><bean:message key="label.hoursNumbersAuthorizedAndRealized" /></strong>
	</td></tr>
	<tr align="right"><td>
		<fr:edit id="nightHours" name="extraWorkRequestFactory" schema="insert.nightHours">
			<fr:layout name="flow">
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
			<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
		</fr:edit>
		</td></tr><tr align="right"><td>
		<fr:edit id="extraNightDays" name="extraWorkRequestFactory" schema="insert.extraNightDays">
			<fr:layout name="flow">
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
		</fr:edit>
		</td></tr><tr align="right"><td>
		<fr:edit id="saturdayHours" name="extraWorkRequestFactory" schema="insert.saturdayHours">
			<fr:layout name="flow">
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
		</fr:edit>
		</td></tr><tr align="right"><td>
		<fr:edit id="holidayHours" name="extraWorkRequestFactory" schema="insert.holidayHours">
			<fr:layout name="flow">
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
		</fr:edit>
		</td></tr><tr align="right"><td>
		<fr:edit id="sundayHours" name="extraWorkRequestFactory" schema="insert.sundayHours">
			<fr:layout name="flow">
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
		</fr:edit>
		</td></tr><tr align="right"><td>
		<fr:edit id="workdayHours" name="extraWorkRequestFactory" schema="insert.workdayHours">
			<fr:layout name="flow">
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
		</fr:edit>
		</td></tr>
		<tr align="left"><td>
			<strong><bean:message key="label.remunerationOption" /></strong>
		</td></tr>
		<tr align="right"><td>
		<fr:edit id="addToWeekRestTime" name="extraWorkRequestFactory" schema="insert.addToWeekRestTime">
			<fr:layout name="flow">
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
		</fr:edit>
		</td></tr><tr align="right"><td>
		<fr:edit id="addToVacations" name="extraWorkRequestFactory" schema="insert.addToVacations">
			<fr:layout name="flow">
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
		</fr:edit>
		</td></tr></table>
		<br/>
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			styleClass="invisible">
			<bean:message key="button.confirm" />
		</html:submit></p>
	</fr:form>
	</logic:equal>
	<logic:notEqual name="extraWorkRequestFactory" property="isMonthClosedForExtraWork" value="false">
		<logic:notEmpty name="extraWorkRequestFactory" property="extraWorkRequest">	
			<table>
			<tr align="left"><td>
			<strong><bean:message key="label.hoursNumbersAuthorizedAndRealized" /></strong>
			<logic:notEmpty name="extraWorkRequestFactory" property="nightHours">
				</td></tr><tr align="right"><td>
				<bean:message key="label.nightHours" bundle="ASSIDUOUSNESS_RESOURCES"/>
				<strong><fr:view name="extraWorkRequestFactory" property="nightHours"/></strong>
			</logic:notEmpty>
			<logic:notEmpty name="extraWorkRequestFactory" property="extraNightDays">
				</td></tr><tr align="right"><td>
				<bean:message key="label.extraNightDays" bundle="ASSIDUOUSNESS_RESOURCES"/>
				<strong><fr:view name="extraWorkRequestFactory"  property="extraNightDays"/></strong>
				<bean:message key="label.extraNightHours" bundle="ASSIDUOUSNESS_RESOURCES"/>
				<strong><fr:view name="extraWorkRequestFactory"  property="extraNightHours"/></strong>
			</logic:notEmpty>
			<logic:notEmpty name="extraWorkRequestFactory" property="saturdayHours">				
				</td></tr><tr align="right"><td>
				<bean:message key="label.saturdayHours" bundle="ASSIDUOUSNESS_RESOURCES"/>
				<strong><fr:view name="extraWorkRequestFactory" property="saturdayHours"/></strong>
			</logic:notEmpty>
			<logic:notEmpty name="extraWorkRequestFactory" property="holidayHours">
				</td></tr><tr align="right"><td>
				<bean:message key="label.holidayHours" bundle="ASSIDUOUSNESS_RESOURCES"/>
				<strong><fr:view name="extraWorkRequestFactory" property="holidayHours"/></strong>
			</logic:notEmpty>
			<logic:notEmpty name="extraWorkRequestFactory" property="sundayHours">
				</td></tr><tr align="right"><td>
				<bean:message key="label.sundayHours" bundle="ASSIDUOUSNESS_RESOURCES"/>
				<strong><fr:view name="extraWorkRequestFactory" property="sundayHours"/></strong>
			</logic:notEmpty>
			<logic:notEmpty name="extraWorkRequestFactory" property="workdayHours">
				</td></tr><tr align="right"><td>
				<bean:message key="label.extraNightDays" bundle="ASSIDUOUSNESS_RESOURCES"/>
				<strong><fr:view name="extraWorkRequestFactory" property="workdayHours"/></strong>
			</logic:notEmpty>
			</td></tr><tr align="left"><td>		
			<strong><bean:message key="label.remunerationOption" /></strong>
			</td></tr><tr align="right"><td>
			<bean:message key="label.addToWeekRestTime" bundle="ASSIDUOUSNESS_RESOURCES"/>
			<fr:view name="extraWorkRequestFactory" property="addToWeekRestTime"/>
			</td></tr><tr align="right"><td>
			<bean:message key="label.addToVacations" bundle="ASSIDUOUSNESS_RESOURCES"/>
			<fr:view name="extraWorkRequestFactory" property="addToVacations"/>
			</td></tr></table>
		</logic:notEmpty>
		<logic:empty name="extraWorkRequestFactory" property="extraWorkRequest">	
		<p><bean:message key="message.noPaymentRequestInClosedMonth" bundle="ASSIDUOUSNESS_RESOURCES"/></p>
		</logic:empty>
		<br/>
	</logic:notEqual>
</logic:present>
