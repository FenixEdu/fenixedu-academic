<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<h3><bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.search.person.results"/></h3> 
<table class="search-clients">
	<logic:iterate id="person" name="people">
		<tr>
			<td class="search-clients-photo">
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME%>=/homepage&amp;uuid=<bean:write name="person" property="username"/></bean:define>
				<div>
					<html:link action="/paymentManagement.do?method=showPerson" paramId="personOid" paramName="person" paramProperty="externalId">
						<img width="60" height="60" src="<%= request.getContextPath() + url %>"/>
					</html:link>
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