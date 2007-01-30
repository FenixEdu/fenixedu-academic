<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.clockings" /></h2>

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

<logic:messagesPresent message="true">
	<html:messages id="message" message="true">
		<p><span class="error0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>
	
<logic:present name="yearMonth">
	<div class="mvert1 invisible">
	<fr:form action="/assiduousnessRecords.do?method=showClockings">
		<fr:edit name="yearMonth" schema="choose.date">
			<fr:layout>
		        <fr:property name="classes" value="thlight thright"/>
			</fr:layout>
		</fr:edit>
		<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible" >
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


<logic:present name="clockings">
	<logic:empty name="clockings">
		<p>
			<em><bean:message key="message.employee.noClocking" /></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="clockings">
		<fr:view name="clockings" schema="show.clockingsDaySheet">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses" value="bgcolor3 acenter,acenter,aleft" />
				<fr:property name="headerClasses" value="acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="clockings">
	<p>
		<em><bean:message key="message.employee.noClocking" /></em>
	</p>
</logic:notPresent>
