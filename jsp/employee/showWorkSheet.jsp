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

<span class="toprint"><br/></span>

<logic:present name="employee">
<div class="toprint">
	<fr:view name="employee" schema="show.employeeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="examMap" />
		</fr:layout>
	</fr:view>
	<br/>
</div>
</logic:present>

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


<logic:present name="workSheet">
	<logic:empty name="workSheet">
		<em><bean:message key="message.employee.noWorkSheet" /></em>
	</logic:empty>
	<logic:notEmpty name="workSheet">
		<fr:view name="workSheet" schema="show.workDaySheet">
			<fr:layout name="tabular">
			    <fr:property name="classes" value="tstyle1b printborder"/>
				<fr:property name="columnClasses" value="bgcolor3 acenter,,acenter,aright,aright,aleft,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
			
		<logic:present name="totalBalance">
			<p class="mvert05"><bean:message key="label.totalBalance" />: <b><bean:write name="totalBalance"/></b></p>
		</logic:present>
		<logic:present name="totalUnjustified">
			<p class="mvert05"><bean:message key="label.totalUnjustified" />: <b><bean:write name="totalUnjustified"/></b></p>
		</logic:present>
	</logic:notEmpty>

	<logic:notEmpty name="subtitles">
		<p class="mtop1 mbottom05"><em><bean:message key="label.subtitle"/>:</em></p>
		<logic:iterate name="subtitles" id="subtile">
			<p class="mvert05"><bean:write name="subtile"/></p>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="workSheet">
	<bean:message key="message.employee.noWorkSheet" />
</logic:notPresent>
