<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<br />
<h2><bean:message key="link.workSheet" /></h2>
<br />
<br />
<logic:present name="yearMonth">
	<fr:form action="/assiduousnessRecords.do?method=showWorkSheet">
		<fr:edit name="yearMonth" schema="choose.date" />
		<br />
		<html:submit>
			<bean:message key="button.submit" />
		</html:submit>
	</fr:form>
</logic:present>
<br />
<br />
<logic:present name="workSheet">
	<logic:empty name="workSheet">
		<bean:message key="message.employee.noWorkSheet" />
	</logic:empty>
	<logic:notEmpty name="workSheet">
		<table class="tstyle1b">
			<th><bean:message key="label.day" /></th>
			<th><bean:message key="label.schedule" /></th>
			<th><bean:message key="label.balance" /></th>
			<th><bean:message key="label.unjustified" /></th>
			<th><bean:message key="label.notes" /></th>
			<bean:define id="maxClockingColumns" name="maxClockingColumns" />
			<th colspan="<%= maxClockingColumns %>"><bean:message
				key="link.clockings" /></th>
			<logic:iterate id="workDaySheet" name="workSheet">
				<tr>
					<td><bean:write name="workDaySheet" property="dateFormatted" /></td>
					<td><bean:write name="workDaySheet" property="workScheduleAcronym" /></td>
					<td><bean:write name="workDaySheet" property="balanceTimeFormatted" /></td>
					<td><bean:write name="workDaySheet"
						property="unjustifiedTimeFormatted" /></td>
					<td><bean:write name="workDaySheet" property="notes" /></td>
					<%int clockingColumns = 0;

                        %>
					<logic:iterate id="clocking" name="workDaySheet"
						property="clockings" type="org.joda.time.TimeOfDay">
						<%clockingColumns++;

                            %>
						<td><%=clocking.toString("HH:mm:ss")%></td>
					</logic:iterate>
					<%for (; clockingColumns < (Integer) maxClockingColumns; clockingColumns++) {

                        %>
					<td></td>
					<%}

                    %>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>
