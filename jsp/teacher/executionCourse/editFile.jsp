<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="net.sourceforge.fenixedu.domain.Language"%>
<%@page import="net.sourceforge.fenixedu.domain.FileItemPermittedGroupType"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="item" type="net.sourceforge.fenixedu.domain.Item" name="item"/>
<bean:define id="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse" name="executionCourse"/>
<h2>
	<%= item.getName().getContent(Language.pt) %>
</h2>

<h3><bean:message key="label.teacher.siteAdministration.editItemFilePermissions.editPermissions"/></h3>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<bean:define id="url">/editItemFilePermissions.do?method=editItemFilePermissions&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
<html:form action="<%= url %>" enctype="multipart/form-data">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.currentSectionCode" property="currentSectionCode" value="<%= item.getSection().getIdInternal().toString() %>" />
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.itemCode"  property="itemID" value="<%= item.getIdInternal().toString() %>"/>
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="executionCourseID" value="<%= executionCourse.getIdInternal().toString() %>"/>
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
