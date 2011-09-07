<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<br/>
<br/>
<table class="tstyle1" width="75%">
	<logic:iterate id="person" name="people">
		<tr>
			<th>
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="person" property="username"/></bean:define>
				<img width="60" height="60" src="<%= request.getContextPath() + url %>"/>
			</th>
			<td style="padding-left: 15px;">
				<strong>
					<bean:write name="person" property="name"/>
				</strong>
				(<bean:write name="person" property="username"/>)
				<br/>
				<html:link action="/paymentManagement.do?method=showPerson" paramId="personOid" paramName="person" paramProperty="externalId">
					<bean:message bundle="TREASURY_RESOURCES" key="link.view" />
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>