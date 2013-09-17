<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<logic:present name="<%= PresentationConstants.DEGREE_LIST %>">
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
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.masterDegree" property="degreeCurricularPlanID">
               		<html:options collection="<%= PresentationConstants.DEGREE_LIST %>" property="value" labelProperty="label"/></html:select>
				</td>
	   		</tr>
	</table>
	<br />
	<input alt="input.method" type="hidden" name="method" value="chooseMasterDegree"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidateID" property="candidateID" />
   	  	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/></td>
	</html:form> 
</logic:present>