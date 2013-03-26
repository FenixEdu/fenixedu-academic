<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<b><bean:message key="link.manage.buildings"/></b>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />

<html:form action="/manageBuildings">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createBuilding"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="35"/>
	<html:select bundle="HTMLALT_RESOURCES" property="campusID" size="1">
		<html:options collection="campuss" property="idInternal" labelProperty="name"/>
	</html:select>

	<html:submit>
		<bean:message key="button.create"/>
	</html:submit>
</html:form>
<br />

<logic:present name="buildings">
	<table class="tstyle4">
		<logic:iterate id="building" name="buildings" type="net.sourceforge.fenixedu.domain.space.OldBuilding">
			<bean:define id="buildingId" name="building" property="idInternal"/>
			<tr>
				<td>
					<bean:write name="building" property="name"/>
				</td>
				<td>
					<html:form action="/manageBuildings">
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editBuilding"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.buildingID" property="buildingID" value="<%= buildingId.toString() %>"/>
					
						<bean:define id="campusID" name="building" property="campus.idInternal" type="java.lang.Integer"/>
						<html:select bundle="HTMLALT_RESOURCES" property="campusID" size="1" value="<%= campusID.toString() %>" onchange="this.form.submit();">
							<html:options collection="campuss" property="idInternal" labelProperty="name"/>
						</html:select>				
						
						<div class="switchNone">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
							<bean:message key="button.submit"/>
						</html:submit>
						</div>
					</html:form>
				</td>
				<td>
					<html:link page="<%= "/manageBuildings.do?method=deleteBuilding&amp;buildingId="
            	   				   			+ pageContext.findAttribute("buildingId")
  											%>">
						<bean:message key="link.delete"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
<script type="text/javascript" language="javascript">
switchGlobal();
</script>
</logic:present>

<logic:notPresent name="buildings">
	<bean:message key="message.no.buildings"/>
</logic:notPresent>