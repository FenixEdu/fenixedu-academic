<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>
<bean:define id="contextPrefix" value="<%= "/executionCourseForumManagement.do?executionCourseID=" + executionCourseId %>" type="java.lang.String" toScope="request"/>
<bean:define id="module" value="/teacher" type="java.lang.String" toScope="request"/>