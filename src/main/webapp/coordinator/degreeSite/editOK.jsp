<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>

<html:xhtml/>

<h2>
	<bean:message key="link.coordinator.degreeSite.management"/>
</h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<p><bean:message key="text.coordinator.degreeSite.editOK"/>
<br />
<br />
<a href="${master_degree.degree.site.fullPath}" target="_blank"><bean:message key="link.coordinator.degreeSite.management"/></a>
