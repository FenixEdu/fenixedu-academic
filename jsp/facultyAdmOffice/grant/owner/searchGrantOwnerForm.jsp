<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:form action="/searchGrantOwner">
    <logic:messagesPresent>
    <span class="error">
    	<html:errors/>
    </span>
    </logic:messagesPresent>    
	<html:hidden property="method" value="doSearch"/>
	<html:hidden property="page" value="1"/>
	<table>
		<tr>
			<td>
				<bean:message key="label.grant.owner.name"/>
			</td>
			<td>
				<html:text property="name" size="70"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.owner.idNumber"/>
			</td>
			<td>
				<html:text property="idNumber" size="15"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.grant.owner.idType"/>
			</td>
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