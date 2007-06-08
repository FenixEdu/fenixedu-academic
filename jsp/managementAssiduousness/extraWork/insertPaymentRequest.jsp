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
<logic:present name="employeeExtraWorkRequestFactory">
	<fr:view name="employeeExtraWorkRequestFactory" schema="show.extraWorkRequestHeader">
		<fr:layout>
			<fr:property name="classes" value="thlight thleft" />
		</fr:layout>
	</fr:view>
	<br/>
	<span class="error0 mtop0">
		<html:messages id="message" message="true">
			<bean:write name="message" />
			<br />
		</html:messages>
	</span>
	<logic:equal name="employeeExtraWorkRequestFactory" property="extraWorkRequestFactory.isMonthClosedForExtraWork" value="false">
		<fr:form action="/extraWorkPaymentRequest.do?method=insertPaymentRequest" encoding="multipart/form-data">
		
		<table>
		<tr align="left">
			<td><strong><bean:message key="label.hoursNumbersAuthorizedAndRealized" /></strong></td>
		</tr>
		<tr align="right">
			<td>
				<fr:edit id="nightHours" name="employeeExtraWorkRequestFactory" schema="insert.nightHours">
					<fr:layout name="flow">
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
					<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
				</fr:edit>
			</td>
		</tr>
		<tr align="right">
			<td>
				<fr:edit id="extraNightDays" name="employeeExtraWorkRequestFactory" schema="insert.extraNightDays">
					<fr:layout name="flow">
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr align="right">
			<td>
				<fr:edit id="saturdayHours" name="employeeExtraWorkRequestFactory" schema="insert.saturdayHours">
					<fr:layout name="flow">
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr align="right">
			<td>
				<fr:edit id="holidayHours" name="employeeExtraWorkRequestFactory" schema="insert.holidayHours">
					<fr:layout name="flow">
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr align="right">
			<td>
				<fr:edit id="sundayHours" name="employeeExtraWorkRequestFactory" schema="insert.sundayHours">
					<fr:layout name="flow">
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr align="right">
			<td>
				<fr:edit id="workdayHours" name="employeeExtraWorkRequestFactory" schema="insert.workdayHours">
					<fr:layout name="flow">
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr align="left">
			<td>
				<strong><bean:message key="label.remunerationOption" /></strong>
			</td>
		</tr>
		<tr align="right">
			<td>
				<fr:edit id="addToWeekRestTime" name="employeeExtraWorkRequestFactory" schema="insert.addToWeekRestTime">
					<fr:layout name="flow">
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr align="right">
			<td>
				<fr:edit id="addToVacations" name="employeeExtraWorkRequestFactory" schema="insert.addToVacations">
					<fr:layout name="flow">
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		</table>
		<br/>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
				<bean:message key="button.confirm" />
			</html:submit>
		</p>
		</fr:form>
	</logic:equal>
	
	<logic:notEqual name="employeeExtraWorkRequestFactory" property="extraWorkRequestFactory.isMonthClosedForExtraWork" value="false">
		<logic:notEmpty name="employeeExtraWorkRequestFactory" property="extraWorkRequest">	
			<table>
			<tr align="left">
				<td>
					<strong><bean:message key="label.hoursNumbersAuthorizedAndRealized" /></strong>
					<logic:notEmpty name="employeeExtraWorkRequestFactory" property="nightHours">
				</td>
			</tr>
			<tr align="right">
				<td>
					<bean:message key="label.nightHours" bundle="ASSIDUOUSNESS_RESOURCES"/>
					<strong><fr:view name="employeeExtraWorkRequestFactory" property="nightHours"/></strong>
				</logic:notEmpty>
				<logic:notEmpty name="employeeExtraWorkRequestFactory" property="extraNightDays">
				</td>
			</tr>
			<tr align="right">
				<td>
					<bean:message key="label.extraNightDays" bundle="ASSIDUOUSNESS_RESOURCES"/>
					<strong><fr:view name="employeeExtraWorkRequestFactory"  property="extraNightDays"/></strong>
					<bean:message key="label.extraNightHours" bundle="ASSIDUOUSNESS_RESOURCES"/>
					<strong><fr:view name="employeeExtraWorkRequestFactory"  property="extraNightHours"/></strong>
				</logic:notEmpty>
				<logic:notEmpty name="employeeExtraWorkRequestFactory" property="saturdayHours">				
				</td>
			</tr>
			<tr align="right">
				<td>
					<bean:message key="label.saturdayHours" bundle="ASSIDUOUSNESS_RESOURCES"/>
					<strong><fr:view name="employeeExtraWorkRequestFactory" property="saturdayHours"/></strong>
				</logic:notEmpty>
				<logic:notEmpty name="employeeExtraWorkRequestFactory" property="holidayHours">
				</td>
			</tr>
			<tr align="right">
				<td>
					<bean:message key="label.holidayHours" bundle="ASSIDUOUSNESS_RESOURCES"/>
					<strong><fr:view name="employeeExtraWorkRequestFactory" property="holidayHours"/></strong>
				</logic:notEmpty>
				<logic:notEmpty name="employeeExtraWorkRequestFactory" property="sundayHours">
				</td>
			</tr>
			<tr align="right">
				<td>
					<bean:message key="label.sundayHours" bundle="ASSIDUOUSNESS_RESOURCES"/>
					<strong><fr:view name="employeeExtraWorkRequestFactory" property="sundayHours"/></strong>
				</logic:notEmpty>
				<logic:notEmpty name="employeeExtraWorkRequestFactory" property="workdayHours">
				</td>
			</tr>
			<tr align="right">
				<td>
					<bean:message key="label.extraNightDays" bundle="ASSIDUOUSNESS_RESOURCES"/>
					<strong><fr:view name="employeeExtraWorkRequestFactory" property="workdayHours"/></strong>
				</logic:notEmpty>
				</td>
			</tr>
			<tr align="left">
				<td>
					<strong><bean:message key="label.remunerationOption" /></strong>
				</td>
			</tr>
			<tr align="right">
				<td>
					<bean:message key="label.addToWeekRestTime" bundle="ASSIDUOUSNESS_RESOURCES"/>
					<fr:view name="employeeExtraWorkRequestFactory" property="addToWeekRestTime"/>
				</td>
			</tr>
			<tr align="right">
				<td>
					<bean:message key="label.addToVacations" bundle="ASSIDUOUSNESS_RESOURCES"/>
					<fr:view name="employeeExtraWorkRequestFactory" property="addToVacations"/>
				</td>
			</tr>
			</table>
		</logic:notEmpty>
		<logic:empty name="employeeExtraWorkRequestFactory" property="extraWorkRequest">	
			<p><bean:message key="message.noPaymentRequestInClosedMonth" bundle="ASSIDUOUSNESS_RESOURCES"/></p>
		</logic:empty>
		<br/>
	</logic:notEqual>
</logic:present>
