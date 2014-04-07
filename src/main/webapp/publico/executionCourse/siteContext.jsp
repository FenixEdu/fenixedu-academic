<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
 
<bean:define id="site" name="actual$site" />

<bean:define id="executionCourse" name="site" property="siteExecutionCourse" toScope="request"/>
<bean:define id="siteActionName" value="/executionCourse.do" toScope="request"/>
<bean:define id="siteContextParam" value="executionCourseID" toScope="request"/>
<bean:define id="siteContextParamValue" name="executionCourse" property="externalId" toScope="request"/>
