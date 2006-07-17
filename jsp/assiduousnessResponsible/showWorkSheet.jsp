<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousnessResponsible" bundle="ASSIDUOUSNESS_RESOURCES"/></em>
<h2><bean:message key="link.showEmployeeWorkSheet" bundle="ASSIDUOUSNESS_RESOURCES"/></h2>

<div class="warning0">
<bean:message key="message.employee.testPhase" bundle="ASSIDUOUSNESS_RESOURCES"/>
</div>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="ASSIDUOUSNESS_RESOURCES">
		<p><span class="error0"><bean:write name="message"/></span></p>
	</html:messages>
	<br/>
</logic:messagesPresent>

<logic:present name="employeeWorkSheet">
	<span class="toprint"><br/></span>
	<fr:view name="employeeWorkSheet" property="employee" schema="show.employeeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="examMap" />
		</fr:layout>
	</fr:view>
	<br/>
	<!-- escrever mes ano -->
	<logic:empty name="employeeWorkSheet" property="workDaySheetList">
		<bean:message key="message.employee.noWorkSheet" bundle="ASSIDUOUSNESS_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="employeeWorkSheet" property="workDaySheetList">
		<fr:view name="employeeWorkSheet" property="workDaySheetList" schema="show.workDaySheet">
			<fr:layout name="tabular">
			    <fr:property name="classes" value="tstyle1b printborder"/>
				<fr:property name="columnClasses" value="bgcolor3 acenter,,acenter,aright,aright,aleft,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
			
		<logic:present name="employeeWorkSheet" property="totalBalance">
			<p class="mvert05"><bean:message key="label.totalBalance" bundle="ASSIDUOUSNESS_RESOURCES"/>: <b><bean:write name="employeeWorkSheet" property="totalBalanceString"/></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="unjustifiedBalance">
			<p class="mvert05"><bean:message key="label.totalUnjustified" bundle="ASSIDUOUSNESS_RESOURCES"/>: <b><bean:write name="employeeWorkSheet" property="unjustifiedBalanceString"/></b></p>
		</logic:present>
	</logic:notEmpty>
</logic:present>