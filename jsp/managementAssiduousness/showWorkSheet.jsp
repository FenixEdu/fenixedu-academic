<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.workSheet" /></h2>

<div class="warning0">
<bean:message key="message.employee.testPhase"/>
</div>
<logic:present name="employeeWorkSheet">
	<span class="toprint"><br/></span>
	<fr:view name="employeeWorkSheet" property="employee" schema="show.employeeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="examMap" />
		</fr:layout>
	</fr:view>
	<br/>

<logic:present name="yearMonth">
	<div class="mvert1 invisible">
	<fr:form action="/viewAssiduousness.do">
		<html:hidden name="employeeForm" property="method" value="showWorkSheet" />
		<html:hidden name="employeeForm" property="employeeNumber"/>				
		<html:hidden name="employeeForm" property="page" value="0"/>
		<fr:edit name="yearMonth" schema="choose.date">
			<fr:layout>
		        <fr:property name="classes" value="thlight thright"/>
			</fr:layout>
		</fr:edit>
		<p>
		<html:submit styleClass="invisible" >
			<bean:message key="button.submit"/>
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


	<logic:empty name="employeeWorkSheet" property="workDaySheetList">
		<bean:message key="message.employee.noWorkSheet" />
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
			<p class="mvert05"><bean:message key="label.totalBalance" />: <b><bean:write name="employeeWorkSheet" property="totalBalanceString"/></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="unjustifiedBalance">
			<p class="mvert05"><bean:message key="label.totalUnjustified" />: <b><bean:write name="employeeWorkSheet" property="unjustifiedBalanceString"/></b></p>
		</logic:present>
	</logic:notEmpty>

	<logic:notEmpty name="employeeWorkSheet" property="justificationMotives">
		<p class="mtop1 mbottom05"><em><bean:message key="label.subtitle"/>:</em></p>
		<logic:iterate name="employeeWorkSheet" property="justificationMotives" id="justificationMotive">
			<p class="mvert05"><bean:write name="justificationMotive" property="acronym"/> - <bean:write name="justificationMotive" property="description"/></p>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>

<logic:notPresent name="employeeWorkSheet">
	<bean:message key="message.employee.noWorkSheet" />
</logic:notPresent>
