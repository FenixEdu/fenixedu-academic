<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
	<bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear" />
</logic:present>
<logic:notPresent name="jspTitle">
	<h2><bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear" /></h2>
	<br />
</logic:notPresent>
<span class="error"><html:errors/></span>
<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
<html:form action="<%=path%>">
    <bean:define id="executionYearList" name="<%= SessionConstants.EXECUTION_YEAR_LIST %>" scope="request" />
	<table>
       <!-- ExecutionYear -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:</td>
         <td><html:select property="executionYear">
                <html:options collection="executionYearList" property="value" labelProperty="label" />
             </html:select>
         </td>
       </tr>
	</table>
	<br />

	<input type="hidden" value="chooseExecutionYear" name="method"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="jspTitle" value="<%= pageContext.findAttribute("jspTitle").toString() %>" />

	<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
</html:form>