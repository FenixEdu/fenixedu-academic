<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.DEGREE_LIST %>">
	<logic:present name="executionYear" >
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
	<br />
	</logic:present>
	<bean:message key="title.masterDegree.administrativeOffice.chooseDegree" />
	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
	<html:form action="<%=path%>">
		<table>
		   <!-- MasterDegree -->
		   <tr>
			 <td><bean:message key="label.masterDegree.administrativeOffice.degree"/>:</td>
			 <td><html:select property="masterDegree">
					<html:options collection="<%= SessionConstants.DEGREE_LIST %>" property="infoDegreeCurricularPlan.infoDegree.sigla" labelProperty="infoDegreeCurricularPlan.infoDegree.nome"/>
				 </html:select>
			 </td>
		   </tr>
		</table>
		<br />
		<input type="hidden" name="method" value="chooseMasterDegree"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
		<html:hidden property="jspTitle" value="<%= pageContext.findAttribute("jspTitle").toString() %>" />
		
		<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
	</html:form>
</logic:present>