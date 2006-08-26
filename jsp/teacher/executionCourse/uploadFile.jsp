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

<h3><bean:message key="label.teacher.siteAdministration.uploadFile.insertFile"/></h3>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<bean:define id="url">/fileUpload.do?method=fileUpload&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&amp;itemID=<bean:write name="item" property="idInternal"/></bean:define>
<html:form action="<%= url %>" enctype="multipart/form-data">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.currentSectionCode" property="currentSectionCode" value="<%= item.getSection().getIdInternal().toString() %>" />
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.itemCode"  property="itemID" value="<%= item.getIdInternal().toString() %>"/>
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="executionCourseID" value="<%= executionCourse.getIdInternal().toString() %>"/>
 
	<strong><bean:message key="label.teacher.siteAdministration.uploadFile.fileDisplayName"/>:</strong>
	<br/>	
 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.displayName" property="displayName" size="55"/>
 	<br/> 	
 	<br/>
 	<strong><bean:message key="label.teacher.siteAdministration.uploadFile.file"/>:</strong>
 	<br/>
 	<html:file bundle="HTMLALT_RESOURCES" altKey="file.theFile" property="theFile" size="40"/>
	<br />
	<br />
	<br />
	<strong><bean:message key="label.teacher.siteAdministration.uploadFile.permissions"/>:</strong><br/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.fileItemPermittedGroupType" property="fileItemPermittedGroupType" value="PUBLIC" /><bean:message key="<%=FileItemPermittedGroupType.PUBLIC.toString()%>" bundle="ENUMERATION_RESOURCES"/><br/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.fileItemPermittedGroupType" property="fileItemPermittedGroupType" value="INSTITUTION_PERSONS"/><bean:message key="<%=FileItemPermittedGroupType.INSTITUTION_PERSONS.toString()%>" bundle="ENUMERATION_RESOURCES"/><br/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.fileItemPermittedGroupType" property="fileItemPermittedGroupType" value="EXECUTION_COURSE_TEACHERS_AND_STUDENTS"/><bean:message key="<%=FileItemPermittedGroupType.EXECUTION_COURSE_TEACHERS_AND_STUDENTS.toString()%>" bundle="ENUMERATION_RESOURCES"/><br/>
	<br />
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
	<bean:message key="label.clear"/>
	</html:reset>
</html:form> 
