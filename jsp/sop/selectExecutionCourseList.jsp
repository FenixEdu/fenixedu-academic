<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="executionCourseList" name="<%= SessionConstants.EXECUTION_COURSE_LIST_KEY %>"/>
<html:select bundle="HTMLALT_RESOURCES" property="courseInitials" size="1">
	<html:option value=""><!-- w3c complient --></html:option>
	<html:options property="sigla" labelProperty="nome" collection="executionCourseList"/>
</html:select>
