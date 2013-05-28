<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="publications" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.researchResultPublications"/>
<bean:define id="personId" name="personId"/>

<em>
	<bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/>
</em>
<h2>
	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.associate.unit.all.publications"/>
</h2>

<logic:messagesPresent name="messages" message="true">
	<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<p>
			<span>
				<!-- Error messages go here -->
				<bean:write name="messages"/>
			</span>
		</p>
	</html:messages>
</logic:messagesPresent>

<ul class="mvert05">
	<li>
		<html:link action="/resultPublications/listPublications.do">
			<bean:message key="link.back" bundle="RESEARCHER_RESOURCES"/>
		</html:link>
	</li>
</ul>

<p class="mtop2 mbottom0">
	<b>
		<bean:message key="label.unit.suggestion" bundle="RESEARCHER_RESOURCES"/>
	</b>
</p>
<bean:define id="addConfirm">return confirm('<bean:message bundle="RESEARCHER_RESOURCES" key="message.confirm.add.unit.to.all.publications"/>')</bean:define>
<table class="tstyle5 mtop025">
	<logic:iterate id="unit" name="units">
		<tr>
			<td>
				<bean:write name="unit" property="name"/>
			</td>
			<td>
				<bean:define id="unitID" name="unit" property="externalId"/>
				<html:link action="<%="/publications/management.do?method=addUnitToAll&unitID=" + unitID + "&personId=" + personId %>" onclick='<%= pageContext.findAttribute("addConfirm").toString() %>'>
					<bean:message key="label.addUnit" bundle="RESEARCHER_RESOURCES"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>
