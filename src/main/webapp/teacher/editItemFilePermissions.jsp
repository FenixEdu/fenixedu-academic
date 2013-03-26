<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.domain.FileItemPermittedGroupType" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
  
<h3><bean:message key="label.teacher.siteAdministration.editItemFilePermissions.editPermissions"/></h3>

 <html:form action="/editItemFilePermissions.do?method=editItemFilePermissions">
 	<bean:define id="itemCode" name="siteView" property="component.item.idInternal"/>
 	<bean:define id="objectCode" name="siteView" property="component.item.infoSection.infoSite.infoExecutionCourse.idInternal"/>
 	<bean:define id="currentSection" name="siteView" property="component.item.infoSection"/>
	<bean:define id="currentSectionCode" name="currentSection" property="idInternal"/>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.currentSectionCode" property="currentSectionCode" value="<%= currentSectionCode.toString() %>" />
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= objectCode.toString() %>"/>
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.itemCode"  property="itemCode" />
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.fileItemId"  property="fileItemId"/>
 	
 	<table>
 		<tr>	
 			<td><strong><bean:message key="label.teacher.siteAdministration.editItemFilePermissions.filename"/></strong></td>
 			<td>&nbsp;</td>
 			<td><bean:write name="fileItem" property="filename"/></td>
 		</tr>
 		 <tr>	
 			<td><strong><bean:message key="label.teacher.siteAdministration.editItemFilePermissions.displayName"/></strong></td>
 			<td>&nbsp;</td>
 			<td><bean:write name="fileItem" property="displayName"/></td>
 		</tr>
 	</table>
 	
 	<br/>
 	<br/>
 	 
	<strong><bean:message key="label.teacher.siteAdministration.editItemFilePermissions.fileAvailableFor"/>:</strong><br/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.permittedGroupType" property="permittedGroupType" value="PUBLIC" /><bean:message key="<%=FileItemPermittedGroupType.PUBLIC.toString()%>" bundle="ENUMERATION_RESOURCES"/><br/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.permittedGroupType" property="permittedGroupType" value="INSTITUTION_PERSONS"/><bean:message key="<%=FileItemPermittedGroupType.INSTITUTION_PERSONS.toString()%>" bundle="ENUMERATION_RESOURCES"/><br/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.permittedGroupType" property="permittedGroupType" value="EXECUTION_COURSE_TEACHERS_AND_STUDENTS"/><bean:message key="<%=FileItemPermittedGroupType.EXECUTION_COURSE_TEACHERS_AND_STUDENTS.toString()%>" bundle="ENUMERATION_RESOURCES"/><br/>
	<br />
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.save"/></html:submit>
 </html:form> 

