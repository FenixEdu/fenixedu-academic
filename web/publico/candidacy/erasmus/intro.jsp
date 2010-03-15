<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">GRI</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/>
</div>



<h1><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></h1>

<p><b>Welcome to the application submission proccess.</b></p>


<h2>Application Form</h2>

<div class="h_box">
	<p class="mvert05">
		To submit your Erasmus Programme application click the following link:
		<b><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= f("%s/candidacies/erasmus/submission-type", request.getContextPath()) %>">Fill the application form »</a></b>
	</p>
</div>

<h3>Recover Email Link</h3>

<p>Alternatively you may access with the link in the submission confirmation message sent to your mailbox: <a href="">Recover access link</a></p>


<h3>Access Submitted Application</h3>

<p>If you have already submitted your application with your national citizen card you can access the application using the following link: <a href="">View application</a></p>
