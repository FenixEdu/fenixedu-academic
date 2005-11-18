<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component"/>
	<bean:define id="section" name="component" property="section" />

	<h2><bean:write name="section" property="name" /></h2>

	<logic:notEmpty name="component" property="items">
		<logic:iterate id="item" name="component" property="items">
			<logic:equal name="item" property="urgent" value="true">
				<font color="red">
			</logic:equal>
			<h3><bean:write name="item" property="name"/></h3>
			<bean:write name="item" property="information" filter="false"/><br/>
			<logic:equal name="item" property="urgent" value="true">
				</font>
			</logic:equal>
			<logic:present name="item" property="links">
				<br/>
				<br/>
				<table>
				<logic:iterate id="infoLink" name="item" property="links">
				<bean:define id="itemCode" name="item" property="idInternal" type="java.lang.Integer"/>
				<bean:define id="link" name="infoLink" property="link" type="java.lang.String"/>
					<tr>
						<td><img src="<%= request.getContextPath() %>/images/list-bullet.gif" alt="" /></td>
						<td>
							<bean:write name="infoLink" property="linkName"/>
						</td>
<!--
						<td>
							<html:link page="<%= "/fileDownload.do?itemCode=" + itemCode + "&fileName=" + link %>" >
								<bean:write name="infoLink" property="linkName"/>
							</html:link>
						</td>
-->
						<td>
							<html:form action="/fileDownload">
								<html:hidden property="page" value="0"/>
								<html:hidden property="itemCode" value="<%= itemCode.toString() %>"/>
								<html:hidden property="fileName" value="<%= link %>"/>
								<html:submit styleClass="inputbutton">
									<bean:message key="button.download"/>
								</html:submit>
							</html:form>
						</td>
					</tr>	
				</logic:iterate>
				</table>
			</logic:present>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>