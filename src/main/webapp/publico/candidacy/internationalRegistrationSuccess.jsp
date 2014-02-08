<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>
<%@page import="net.sourceforge.fenixedu.domain.Instalation"%>
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="java.util.Locale"%>

<html:xhtml/>

<h2><bean:message key ="title.register.user" bundle="APPLICATION_RESOURCES"/></h2>

<div class="infoop2 mvert15">
	<bean:define id="username" name="person" property="username" type="java.lang.String"/>
	<p><bean:message key="message.register.success" bundle="APPLICATION_RESOURCES" arg0="<%= username %>" /></p>

</div>



