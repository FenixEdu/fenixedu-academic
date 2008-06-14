<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<table width="100%" cellspacing="0" cellpadding="5">
	<td class="infoop">
		<span class="emphasis-box">info</span>
	</td>
	<td class="infoop">
		<bean:message key="message.teacherCredits.explanation"/>
	</td>
</table>
<br />
<br />
<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber" />
<logic:empty name="infoCredits">
	<span class="error"><!-- Error messages go here --><bean:message key="message.teacherCredit.notFound"/></span>
</logic:empty>
<logic:notEmpty name="infoCredits">
	<table cellpadding="3" cellspacing="1">
		<tr>
			<th class="listClasses-header"><bean:message key="label.executionPeriod" /></th>
			<th class="listClasses-header" colspan="2"><bean:message key="label.creditsResume" /></th>
		</tr>
	<logic:iterate id="infoCredits" name="infoCredits">
		<tr>
			<td class="listClasses" >
				<bean:write name="infoCredits"  property="infoExecutionPeriod.description" />
			</td>
			<td class="listClasses" >
				<tiles:insert definition="creditsResume" flush="false">
					<tiles:put name="infoCredits" beanName="infoCredits"/>
				</tiles:insert>
			</td>
			<td class="listClasses" >
				<html:link page='<%= "/showTeacherCreditsSheet.do?teacherNumber=" + teacherNumber %>' paramId="executionPeriodId" paramName="infoCredits" paramProperty="infoExecutionPeriod.idInternal">
					<bean:message key="link.teacherCreditsDetails"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
	</table>
	<br />
	<br />
	<tiles:insert definition="creditsLegend" flush="true" />
</logic:notEmpty>
