<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
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
<bean:define id="teacherId" name="infoTeacher" property="teacherId" />
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
				<html:link page='<%= "/showTeacherCreditsSheet.do?teacherId=" + teacherId %>' paramId="executionPeriodId" paramName="infoCredits" paramProperty="infoExecutionPeriod.externalId">
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
