<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.type.edition"/></h2>

<html:form action="/editGrantType" style="display:inline">

	<%-- Presenting errors --%>
	<logic:messagesPresent>
		<html:errors/>
	</logic:messagesPresent>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doEdit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- grant type --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>

	<table class="tstyle5 thlight thright">
		<tr>
			<th>
				<bean:message key="label.grant.type.name"/>:&nbsp;
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="100"/> <bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.grant.type.sigla"/>:&nbsp;
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.sigla" property="sigla"/> <bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.grant.type.minPeriodDays"/>:&nbsp;
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minPeriodDays" property="minPeriodDays" size="10"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.grant.type.maxPeriodDays"/>:&nbsp;
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maxPeriodDays" property="maxPeriodDays" size="10"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.grant.type.indicativeValue"/>:&nbsp;
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.indicativeValue" property="indicativeValue"/>
			</td>
		</tr>

		<tr>
			<th>
				<bean:message key="label.grant.type.source"/>:&nbsp;
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.source" property="source" size="40"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.grant.type.state"/>:&nbsp;
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.state" property="state" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
	</table>



	<table>
		<tr>
			<td>
				<%-- Save button (edit/create) --%>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<html:form action="/manageGrantType" style="display:inline">
					<%-- button cancel --%>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantTypeForm"/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>