<%@ page language="java" %>
<%@page import="org.fenixedu.bennu.core.domain.Bennu"%>
<%@page import="net.sourceforge.fenixedu.domain.StrikeDay"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2>
	<bean:message bundle="MANAGER_RESOURCES" key="label.manage.strikeDays"/>
</h2>

<br/>
<br/>

<fr:create type="net.sourceforge.fenixedu.domain.StrikeDay" action="/manageStrikeDays.do?method=prepare">
	<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.StrikeDay">
		<fr:slot name="date"/>
	</fr:schema>
</fr:create>



<html:link action="/manageStrikeDays.do?method=reportNow">
	Gerar Estatísticas Agora!
</html:link>

<br/>
<br/>

<table class="style1">
	<tr>
		<th class="listClasses">
			Dia
		</th>
		<th class="listClasses">
		</th>
	</tr>
	<% for (final StrikeDay strikeDay : Bennu.getInstance().getStrikeDaySet()) { %>
		<tr>
			<td class="listClasses">
				<%= strikeDay.getDate().toString("yyyy-MM-dd") %>
			</td>
			<td class="listClasses">
				<html:link action="<%= "/manageStrikeDays.do?method=deleteStrikeDay&strikeDayOid=" + strikeDay.getExternalId() %>">
					<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
				</html:link>
			</td>
		</tr>
	<% } %>
</table>