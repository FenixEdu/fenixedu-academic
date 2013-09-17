<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<bean:define id="siteActionName" value="/manageExecutionCourseSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="executionCourseID" toScope="request"/>
<bean:define id="siteContextParamValue" name="executionCourse" property="externalId" toScope="request"/>
