<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
<html:form action="/viewAllClassesSchedulesDA">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="list"/>
    <table width="100%">
    	<tr>
			<td class="infoop"><bean:message key="label.select.degrees" /></td>    	
        </tr>
    </table>
	<br />
   	<bean:message key="property.context.degree"/>:
   	<br />
	<br />
	
	<logic:present name="<%= SessionConstants.DEGREE_NAMES_LIST %>" scope="request">
		<html:checkbox property="selectAllDegrees">
			<bean:message key="checkbox.show.all.degrees"/><br />
		</html:checkbox>
		<logic:iterate id="item" name="<%= SessionConstants.DEGREE_NAMES_LIST %>">
			<html:multibox property="selectedDegrees">
				<bean:write name="item"/>
			</html:multibox>
			<bean:write name="item"/><br/>
		</logic:iterate>
	</logic:present>
	<br/>
   <p><html:submit value="Submeter" styleClass="inputbutton">
   		<bean:message key="label.list"/>
   </html:submit></p>
</html:form>