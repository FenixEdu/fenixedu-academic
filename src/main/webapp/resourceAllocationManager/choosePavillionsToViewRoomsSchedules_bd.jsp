<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2>Listagem de Horários por Salas</h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<html:form action="/viewAllRoomsSchedulesDA">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden alt="<%=PresentationConstants.ACADEMIC_INTERVAL%>" property="<%=PresentationConstants.ACADEMIC_INTERVAL%>" value="<%= ""+request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL)%>" />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="list"/>

	<p class="mbottom2">
		<bean:message key="label.select.pavillions" />
	</p>
	
	<p>
	   	<bean:message key="property.context.pavillion"/>:
   	</p>

	
	<logic:present name="<%= PresentationConstants.PAVILLIONS_NAMES_LIST %>" scope="request">
		<p>
		<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.selectAllPavillions" property="selectAllPavillions">
			<strong><bean:message key="checkbox.show.all.pavillions"/></strong>
		</html:checkbox>
		</p>

		<logic:iterate id="item" name="<%= PresentationConstants.PAVILLIONS_NAMES_LIST %>">
			<p class="mvert025">
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedPavillions" property="selectedPavillions">
					<bean:write name="item"/>
				</html:multibox>
				<bean:write name="item"/>
			</p>
		</logic:iterate>
	</logic:present>

	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
			<bean:message key="label.list"/>
		</html:submit>
	</p>
</html:form>