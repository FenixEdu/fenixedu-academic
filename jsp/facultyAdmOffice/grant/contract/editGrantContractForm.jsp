<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.grant.contract.edition"/></p></strong><br/>

<html:form action="/editGrantContract" style="display:inline">
	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden property="method" value="doEdit"/>
	<html:hidden property="page" value="1"/>

	<%-- contract --%>
	<html:hidden property="idGrantContract"/>
	<html:hidden property="contractNumber"/>
	<html:hidden property="grantOrientationTeacherIdInternal"/>

	<%-- grant owner --%>
	<html:hidden property="idInternal"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.beginDate"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateBeginContract" size="10"/>
				<bean:message key="label.requiredfield"/>&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.endDate"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateEndContract" size="10"/>
				<bean:message key="label.requiredfield"/>&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.type"/>:&nbsp;
			</td>
			<td>
				<html:select property="grantType">
					<html:options collection="grantTypeList" property="sigla" labelProperty="name"/>
				</html:select>*
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.endMotive"/>:&nbsp;
			</td>
			<td>
				<html:text property="endContractMotive" size="40"/>
			</td>
		</tr>	
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.orientationTeacher"/>:&nbsp;
			</td>
			<td>
				<html:text property="grantOrientationTeacher" size="10"/>
				<bean:message key="label.requiredfield"/>
				&nbsp;<html:link page='<%= "/showTeachersList.do?method=showForm" %>' target="_blank">
					<bean:message key="link.teacher.showList"/>
				</html:link>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.dateSendDispatchCC"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateSendDispatchCC" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.dateDispatchCC"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateDispatchCC"  size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.dateSendDispatchCD"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateSendDispatchCD" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.dateDispatchCD"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateDispatchCD" size="10"/>
				&nbsp;
				<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.dateAcceptTerm"/>:&nbsp;
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
				<html:form action="/manageGrantContract" style="display:inline">
				<html:hidden property="method" value="prepareManageGrantContractForm"/>
				<html:hidden property="page" value="1"/>
				<html:hidden property="idInternal" value='<%= request.getAttribute("idInternal").toString() %>'/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>