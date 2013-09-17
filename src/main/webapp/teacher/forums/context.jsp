<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<bean:define id="executionCourseId" name="executionCourse" property="externalId"/>
<bean:define id="contextPrefix" value="<%= "/executionCourseForumManagement.do?executionCourseID=" + executionCourseId %>" type="java.lang.String" toScope="request"/>
<bean:define id="module" value="/teacher" type="java.lang.String" toScope="request"/>