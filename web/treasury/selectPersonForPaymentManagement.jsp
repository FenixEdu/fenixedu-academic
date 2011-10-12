<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<table class="search-clients">
	<logic:iterate id="person" name="people">
		<tr>
			<td class="search-clients-photo">
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="person" property="username"/></bean:define>
				<div>
					<img width="60" height="60" src="<%= request.getContextPath() + url %>"/>
				</div>
			</td>
			<td class="search-clients-name">
				<p class="mvert025">
					<html:link action="/paymentManagement.do?method=showPerson" paramId="personOid" paramName="person" paramProperty="externalId">
						<bean:write name="person" property="name"/>
					</html:link>
				</p>
				<p class="mvert025">
					<bean:write name="person" property="username"/>
				</p>
			</td>
		</tr>
	</logic:iterate>
</table>