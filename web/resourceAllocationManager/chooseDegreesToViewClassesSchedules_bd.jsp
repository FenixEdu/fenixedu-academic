<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2>Listagem de Horários por Turmas</h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/viewAllClassesSchedulesDA">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="list"/>
	<html:hidden alt="<%=SessionConstants.EXECUTION_PERIOD_OID%>" property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
	<bean:define id="infoExecutionDegreesList" name="<%=SessionConstants.INFO_EXECUTION_DEGREE_LIST%>" scope="request"/>
	<html:hidden alt="<%=SessionConstants.INFO_EXECUTION_DEGREE_LIST%>" property="<%=SessionConstants.INFO_EXECUTION_DEGREE_LIST%>" value="infoExecutionDegreesList"/>
	
	<p>
		<bean:message key="label.select.degrees" />
	</p>
	
	<p class="mtop15">
	   	<bean:message key="property.context.degree"/>:
   	</p>
	
	<logic:present name="<%= SessionConstants.INFO_EXECUTION_DEGREE_LIST %>" scope="request">
		<p>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.selectAllDegrees" property="selectAllDegrees"/> 
			<strong><bean:message key="checkbox.show.all.degrees"/></strong>
		</p>


		<%int index = 0;%>
		<logic:iterate id="infoExecutionDegree" name="<%= SessionConstants.INFO_EXECUTION_DEGREE_LIST %>">
			<p class="mvert025">
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedDegrees" property="selectedDegrees" value="<%= new String(""+index)%>"/>
				<bean:write name="infoExecutionDegree" property="label"/><br/>
				<%index++;%>			
			</p>			
		</logic:iterate>
	</logic:present>

	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
			<bean:message key="label.list"/>
		</html:submit>
	</p>
</html:form>