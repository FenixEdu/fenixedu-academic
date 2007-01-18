<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="site" name="executionCourse" property="site" toScope="request"/>
<bean:define id="siteActionName" value="/executionCourse.do" toScope="request"/>
<bean:define id="siteContextParam" value="executionCourseID" toScope="request"/>
<bean:define id="siteContextParamValue" name="executionCourse" property="idInternal" toScope="request"/>
