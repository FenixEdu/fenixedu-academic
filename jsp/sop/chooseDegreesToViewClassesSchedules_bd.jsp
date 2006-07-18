<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2>Listagem de Hor�rios por Turmas</h2>
<span class="error"><html:errors/></span>
<html:form action="/viewAllClassesSchedulesDA">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="list"/>
	<html:hidden alt="<%=SessionConstants.EXECUTION_PERIOD_OID%>" property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
	<bean:define id="infoExecutionDegreesList" name="<%=SessionConstants.INFO_EXECUTION_DEGREE_LIST%>" scope="request"/>
	<html:hidden alt="<%=SessionConstants.INFO_EXECUTION_DEGREE_LIST%>" property="<%=SessionConstants.INFO_EXECUTION_DEGREE_LIST%>" value="infoExecutionDegreesList"/>
    <table width="100%">
    	<tr>
			<td class="infoop"><bean:message key="label.select.degrees" /></td>    	
        </tr>
    </table>
	<br />
   	<bean:message key="property.context.degree"/>:
   	<br />
	<br />
	
	<logic:present name="<%= SessionConstants.INFO_EXECUTION_DEGREE_LIST %>" scope="request">
		<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.selectAllDegrees" property="selectAllDegrees">
			<bean:message key="checkbox.show.all.degrees"/><br />
		</html:checkbox>
		<br />
		<%int index = 0;%>
		<logic:iterate id="infoExecutionDegree" name="<%= SessionConstants.INFO_EXECUTION_DEGREE_LIST %>">
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedDegrees" property="selectedDegrees" value="<%= new String(""+index)%>"/>
			<bean:write name="infoExecutionDegree" property="label"/><br/>
			<%index++;%>			
		</logic:iterate>
	</logic:present>
	<br/>
   <p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
   		<bean:message key="label.list"/>
   </html:submit></p>
</html:form>