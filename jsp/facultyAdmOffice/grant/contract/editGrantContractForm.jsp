<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html:form action="/editGrantContract" style="display:inline">

<logic:messagesPresent>
<span class="error">
	<html:errors/>
</span>
</logic:messagesPresent>

<table>
	<tr>
		<td align="left">
			<bean:message key="label.grant.contract.beginDate"/>:&nbsp;
		</td>
		<td>
			<html:text property="dateBeginContract"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.contract.endDate"/>:&nbsp;
		</td>
		<td>
			<html:text property="dateEndContract"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.contract.type"/>:&nbsp;
		</td>
		<td>
			<html:text property="grantType"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.contract.endMotive"/>:&nbsp;
		</td>
		<td>
			<html:text property="endContractMotive"/>
		</td>
	</tr>
	<tr>
		<td align="left">
			<bean:message key="label.grant.contract.responsibleTeacher"/>:&nbsp;
		</td>
		<td>
			<html:text property="grantResponsibleTeacher"/>
		</td>
	</tr>

	<tr>
		<td align="left">
			<bean:message key="label.grant.contract.orientationTeacher"/>:&nbsp;
		</td>
		<td>
			<html:text property="grantOrientationTeacher"/>
		</td>
	</tr>
</table>
<br>
<table>
	<tr>
	<td>
		<html:submit styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
		</html:form>
	</td>
	<td>
		<html:form action="/searchGrantOwner?page=1&amp;method=searchForm" style="display:inline">
			<html:submit styleClass="inputbutton" style="display:inline">
				<bean:message key="button.cancel"/>
			</html:submit>
		</html:form>
	</td>
	</tr>
</table>