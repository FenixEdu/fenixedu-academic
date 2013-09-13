<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml/>

<h2><bean:message key="title.coordinator.main"/></h2>
<p><bean:message key="label.coordinator.main.welcome"/>.</p>
<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
<br /><br />