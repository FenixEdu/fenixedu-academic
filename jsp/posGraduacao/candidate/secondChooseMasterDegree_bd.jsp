<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>
<span class="error"><html:errors/></span>
<br />
<logic:present name="<%= SessionConstants.DEGREE_LIST %>">
	<logic:present name="executionYear" >
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
	<br />
	</logic:present>
	<bean:message key="title.masterDegree.administrativeOffice.chooseSecondMasterDegree" />
	<br /><br />
	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
	<table>
	<html:form action="<%=path%>">
	   <!-- MasterDegree -->
			<tr>
				<td>
					<html:select property="masterDegree">
               		<html:options collection="<%= SessionConstants.DEGREE_LIST %>" property="value" labelProperty="label"/></html:select>
				</td>
	   		</tr>
	</table>
	<br />
	<input type="hidden" name="method" value="chooseMasterDegree"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
		<html:hidden property="candidateID" />
	<html:submit value="Submeter" styleClass="inputbutton" property="ok"/></td>
	</html:form> 
</logic:present>