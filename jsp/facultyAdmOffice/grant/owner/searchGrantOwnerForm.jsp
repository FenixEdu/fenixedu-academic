<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html:form action="/searchGrantOwner">
	<logic:messagesPresent>
	<p align="center"><span class="error">
	<html:errors/>
	</span></p><br>
	</logic:messagesPresent>    

	<html:hidden property="method" value="doSearch"/>
	<html:hidden property="page" value="1"/>
	
	<%--
	<table class="listClasses" align="center">
		<tr><td>Para fazer uma procura insira o nome e/ou os dados de documento de identificação da pessoa bolseira a gerir,</td></tr>
		<tr><td>ou</td></tr>
		<tr><td>Insira o número do bolseiro</td></tr>
	</table> --%>
	
	<br/>
	<table>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.name"/>:&nbsp;</td>
			<td><html:text property="name" size="70"/></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.idNumber"/>:&nbsp;</td>
			<td><html:text property="idNumber" size="15"/></td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.idType"/>:&nbsp;</td>
			<td>
				<html:select property="idType">
						<html:options collection="documentTypeList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>
	
	<p>
		<html:submit styleClass="inputbutton">
			<bean:message key="button.search"/>
		</html:submit>
	</p>
</html:form>
<br><br>
<html:form action="/searchGrantOwnerByNumber" style="display:inline">
	<html:hidden property="method" value="searchGrantOwner"/>
	<html:hidden property="page" value="1"/>
	
	<table>
		<tr>
			<td align="left"><bean:message key="label.grant.owner.idInternal"/>:&nbsp;</td>
			<td><html:text property="idGrantOwner"/></td>
		</tr>
	</table>
	
	<p>
		<html:submit styleClass="inputbutton">
			<bean:message key="button.search"/>
		</html:submit>
	</p>
</html:form>
