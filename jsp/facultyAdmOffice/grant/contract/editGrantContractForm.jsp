<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<table class="listClasses" align="center">
<tr><td>Preencha correctamente o formulário (não deixando campos obrigatórios em branco).</td></tr></table>
<br/><strong><p align="center">Edição de Contracto</p></strong><br/>

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
	<html:hidden property="grantResponsibleTeacherIdInternal"/>
	<html:hidden property="grantOrientationTeacherIdInternal"/>

	<%-- grant owner --%>
	<html:hidden property="idInternal"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.beginDate"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateBeginContract"/>* &nbsp;(dd/mm/aaaa)
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.endDate"/>:&nbsp;
			</td>
			<td>
				<html:text property="dateEndContract"/>* &nbsp;(dd/mm/aaaa)
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
				<html:text property="endContractMotive"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.responsibleTeacher"/>:&nbsp;
			</td>
			<td>
				<html:text property="grantResponsibleTeacher"/>*
			</td>
		</tr>
	
		<tr>
			<td align="left">
				<bean:message key="label.grant.contract.orientationTeacher"/>:&nbsp;
			</td>
			<td>
				<html:text property="grantOrientationTeacher"/>*
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
				<html:form action="/manageGrantOwner" style="display:inline">
				<html:hidden property="method" value="prepareManageGrantOwnerForm"/>
				<html:hidden property="page" value="1"/>
				<html:hidden property="idInternal" value='<%= request.getAttribute("idInternal").toString() %>'/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>