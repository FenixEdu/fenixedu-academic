<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2>Listagem de Hor�rios por Salas</h2>
<span class="error"><html:errors/></span>
<html:form action="/viewAllRoomsSchedulesDA">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden alt="<%=SessionConstants.EXECUTION_PERIOD_OID%>" property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="list"/>
    <table width="100%">
    	<tr>
			<td class="infoop"><bean:message key="label.select.pavillions" /></td>
        </tr>
    </table>
	<br />
   	<bean:message key="property.context.pavillion"/>:
   	<br />
	<br />
	
	<logic:present name="<%= SessionConstants.PAVILLIONS_NAMES_LIST %>" scope="request">
		<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.selectAllPavillions" property="selectAllPavillions">
			<bean:message key="checkbox.show.all.pavillions"/><br />
		</html:checkbox>
		<br />
		<logic:iterate id="item" name="<%= SessionConstants.PAVILLIONS_NAMES_LIST %>">
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedPavillions" property="selectedPavillions">
				<bean:write name="item"/>
			</html:multibox>
			<bean:write name="item"/><br />
		</logic:iterate>
	</logic:present>
	<br />
   <p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
   		<bean:message key="label.list"/>
   </html:submit></p>
</html:form>