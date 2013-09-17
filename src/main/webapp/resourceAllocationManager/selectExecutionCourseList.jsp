<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<bean:define id="executionCourseList" name="<%= PresentationConstants.EXECUTION_COURSE_LIST_KEY %>"/>
<html:select bundle="HTMLALT_RESOURCES" property="courseInitials" size="1">
	<html:option value=""><!-- w3c complient --></html:option>
	<html:options property="sigla" labelProperty="nome" collection="executionCourseList"/>
</html:select>
