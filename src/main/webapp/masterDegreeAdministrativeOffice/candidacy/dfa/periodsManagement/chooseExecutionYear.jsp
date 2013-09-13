<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><strong><bean:message key="label.candidacy.dfa.periodsManagement" bundle="ADMIN_OFFICE_RESOURCES" /></strong></h2>
<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error">
		<bean:write name="message" />
	</span>
	<br />
</html:messages>
<br />
<html:form action="/dfaPeriodsManagement.do?method=showExecutionDegrees">
	<table>
		<tr>
			<td><bean:message key="label.executionYear" bundle="ADMIN_OFFICE_RESOURCES" /></td>
			<td><html:select property="executionYear">
				<html:options collection="executionYears" property="externalId" labelProperty="year"  />
			</html:select></td>
		</tr>
	</table>


	<html:submit>
		<bean:message key="button.proceed" bundle="ADMIN_OFFICE_RESOURCES" />
	</html:submit>

</html:form>
