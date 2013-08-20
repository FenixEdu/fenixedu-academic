<%@ page language="java" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="tsdID" name="tsd" property="externalId"/>
<bean:define id="tsdCourseId" name="tsdCourse" property="externalId"/>
<bean:define id="tsdProcessId" name="tsdProcess" property="externalId"/>

<logic:present name="curricularLoad">

<fr:edit id="curricularLoadEdition" name="curricularLoad" schema="edit.studentClassMethod"
action="<%= "/tsdCourse.do?method=prepareLinkForTSDCourse&tsd=" + tsdID + "&tsdProcess=" + tsdProcessId + "&tsdCourse=" + tsdCourseId + "&notTSDCourseViewLink=true"%>"
/>
</logic:present>