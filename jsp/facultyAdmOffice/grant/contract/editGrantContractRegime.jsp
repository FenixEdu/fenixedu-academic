<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.grant.contract.regime.edition"/></p></strong><br/>

<html:form action="/editGrantContractRegime" style="display:inline">
	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden property="method" value="doEdit"/>
	<html:hidden property="page" value="1"/>

	<%-- contract --%>
	<html:hidden property="idContract"/>
	<html:hidden property="contractNumber"/>

	<%-- contract regime --%>
	<html:hidden property="grantContractRegimeId"/>
	<html:hidden property="state"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.orientationTeacher"/>:&nbsp;
			</td>
			<td>
				<html:text property="grantContractRegimeTeacherNumber" size="10"/>
				&nbsp;<html:link page='<%= "/showTeachersList.do?method=showForm" %>' target="_blank">
					<bean:message key="link.teacher.showList"/>
				</html:link>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.beginDate"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateBeginContract" size="10"/>
				<bean:message key="label.requiredfield"/>&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.endDate"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateEndContract" size="10"/>
				<bean:message key="label.requiredfield"/>
				&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateSendDispatchCC"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateSendDispatchCC" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateDispatchCC"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateDispatchCC"  size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateSendDispatchCD"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateSendDispatchCD" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateDispatchCD"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateDispatchCD" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.regime.dateAcceptTerm"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateAcceptTerm" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantContractRegime" style="display:inline">
				<html:hidden property="method" value="prepareManageGrantContractRegime"/>
				<html:hidden property="page" value="1"/>
				<html:hidden property="idContract" value='<%= request.getAttribute("idContract").toString() %>'/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>