<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<h2>
	<bean:message key="label.executionCourseManagement.menu.sections"/>
</h2>

<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
<html:link page="/manageExecutionCourse.do?method=createSection" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="link.createSection"/>
</html:link>

&nbsp;&nbsp;

<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
<html:link page="/manageExecutionCourse.do?method=prepareImportSections" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="label.import.sections"/>
</html:link>
