<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.workSheet" /></h2>

<div class="toprint">

<logic:present name="employeeWorkSheet">
	<span class="toprint"><br/></span>
	<fr:view name="employeeWorkSheet" property="employee" schema="show.employeeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="showinfo1 thbold" />
		</fr:layout>
	</fr:view>
</div>

	<logic:messagesPresent message="true">
		<html:messages id="message" message="true">
			<p><span class="error0"><bean:write name="message" /></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<logic:present name="yearMonth">
		<div class="mvert1 invisible">
		<fr:form action="/assiduousnessRecords.do?method=showWorkSheet">
			<fr:edit name="yearMonth" schema="choose.date">
				<fr:layout>
			        <fr:property name="classes" value="thlight thright"/>
				</fr:layout>
			</fr:edit>
			<p>
			<html:submit>
				<bean:message key="button.submit" />
			</html:submit>
			</p>
		</fr:form>
		</div>
		
		<div class="toprint">
		<p class="bold mbottom0">
		<bean:define id="month" name="yearMonth" property="month"/>
		<bean:message key="<%=month.toString()%>" bundle="ENUMERATION_RESOURCES"/>
		<bean:write name="yearMonth" property="year"/>
		</p>
		<br/>
		</div>
	</logic:present>
		
	<logic:notEmpty name="hasToCompensateThisMonth">
		<p class="infoop2"><bean:message key="message.hasToCompensateThisMonth" bundle="ASSIDUOUSNESS_RESOURCES"/></p>
	</logic:notEmpty>
	<logic:notEmpty name="hasToCompensateNextMonth">
		<p class="infoop2"><bean:message key="message.hasToCompensateNextMonth" bundle="ASSIDUOUSNESS_RESOURCES"/></p>
	</logic:notEmpty>
	
	<logic:empty name="employeeWorkSheet" property="workDaySheetList">
		<p>
			<em><bean:message key="message.employee.noWorkSheet" /></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="employeeWorkSheet" property="workDaySheetList">
		<fr:view name="employeeWorkSheet" property="workDaySheetList" schema="show.workDaySheet">
			<fr:layout name="tabular">
			    <fr:property name="classes" value="tstyle1 printborder tpadding1"/>
				<fr:property name="columnClasses" value="bgcolor3 acenter,,acenter,aright,aright,aleft,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
			
		<logic:notEmpty name="displayCurrentDayNote">
			<bean:message key="message.employee.currentDayIgnored"/>
		</logic:notEmpty>
		<logic:present name="employeeWorkSheet" property="totalBalance">
			<p class="mvert05"><bean:message key="label.totalBalance" />: <b><bean:write name="employeeWorkSheet" property="totalBalanceString"/></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="unjustifiedBalance">
			<p class="mvert05"><bean:message key="label.totalUnjustified" />: <b><bean:write name="employeeWorkSheet" property="unjustifiedBalanceString"/></b></p>
		</logic:present>
<%-- 		<logic:present name="employeeWorkSheet" property="complementaryWeeklyRest">
			<p class="mvert05"><bean:message key="label.totalComplementaryWeeklyRest" />: <b><bean:write name="employeeWorkSheet" property="complementaryWeeklyRestString"/></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="weeklyRest">
			<p class="mvert05"><bean:message key="label.totalWeeklyRest" />: <b><bean:write name="employeeWorkSheet" property="weeklyRestString"/></b></p>
		</logic:present> --%>
	</logic:notEmpty>

</logic:present>

<logic:notPresent name="employeeWorkSheet">
	<p>
		<em><bean:message key="message.employee.noWorkSheet" /></em>
	</p>
</logic:notPresent>
