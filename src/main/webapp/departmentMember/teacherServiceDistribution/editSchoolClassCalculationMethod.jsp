<%@ page language="java" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<bean:define id="tsdID" name="tsd" property="externalId"/>
<bean:define id="tsdCourseId" name="tsdCourse" property="externalId"/>
<bean:define id="tsdProcessId" name="tsdProcess" property="externalId"/>

<logic:present name="curricularLoad">

<fr:edit id="curricularLoadEdition" name="curricularLoad" schema="edit.studentClassMethod"
action="<%= "/tsdCourse.do?method=prepareLinkForTSDCourse&tsd=" + tsdID + "&tsdProcess=" + tsdProcessId + "&tsdCourse=" + tsdCourseId + "&notTSDCourseViewLink=true"%>"
/>
</logic:present>