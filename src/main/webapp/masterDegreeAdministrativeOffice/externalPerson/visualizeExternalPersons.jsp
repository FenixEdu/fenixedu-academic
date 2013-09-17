<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants" %>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.visualize"/></h2>
<center>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<br/>
<logic:present name="<%= PresentationConstants.WORK_LOCATIONS_LIST %>" scope="request">
	<html:form action="/visualizeExternalPersons.do?method=visualize" >
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
		<table>
			<tr>
				<td>
					<bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/>:&nbsp;					
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.institutionId" property="institutionId">
			    		<html:options collection="<%= PresentationConstants.WORK_LOCATIONS_LIST %>" property="value" labelProperty="label" />
			   		</html:select> 
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<!-- Submit button -->
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbuttonSmall">
						<bean:message key="button.submit.masterDegree.externalPerson.find"/>
					</html:submit>	
				</td>
			</tr>
		</table>
	</html:form>
</logic:present>
</center>