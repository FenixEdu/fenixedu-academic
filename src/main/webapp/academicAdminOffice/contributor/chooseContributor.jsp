<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<html:xhtml/>

<bean:define id="title" name="<%= PresentationConstants.CONTRIBUTOR_ACTION %>"/>    

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message name="title"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<span class="error"><html:messages id="m" message="true">
	<bean:write name="m"/>
</html:messages>
</span>
<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<html:form action="<%=path%>">
	<input alt="input.method" type="hidden" value="getContributors" name="method"/>
	<input alt="input.action" type="hidden" value="visualize" name="action"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>        
	<table class="mtop15">
	       <!-- Contributor Number -->
	       <tr>
	         <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/>: </td>
	         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorNumber" /></td>
	       </tr>
	   </table>
	
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Escolher" property="ok"/>
		</p>
</html:form>