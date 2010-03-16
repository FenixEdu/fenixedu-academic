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

The application submission may be acomplished in two ways:

<h3>National Citizen Card</h3>

<p><img src="<%= request.getContextPath() %>/images/stork/stork.jpg" alt="<bean:message key="stork.logo" bundle="IMAGE_RESOURCES" />" width="83px" height="51px" /></p>
<p class="mtop1.5"> 
The STORK project is an effort to establish a European Identification Platform that will allow citizens to establish new electronic relations in European Union, 
just by presenting their national citizen card. So in this context you can identify yourself securely and easily. 
</p>

<div class="h_box">	
	<p class="mvert05">
		<img src="<%= request.getContextPath() %>/images/stork/icon_info.gif" />
		To fill your application with national citizen card you need "the machine" to authenticate yourself in the Identification Plataform.
	</p>
	<p class="mvert05 ">
		<img src="<%= request.getContextPath() %>/images/stork/icon_info.gif" />
		Only citizens of Spain  are able to authenticate in the European Identification Plataform <img src="<%= request.getContextPath() %>/images/stork/spain_flag.gif" width="" height="" />
	</p>
</div>

<div class="h_box">
	<p  class="mvert05">
		To submit your Erasmus Programme application with your national citizen card click the following link:
		<b><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= f("%s/candidacies/erasmus/nationalCardSubmission", request.getContextPath()) %>">Fill the application form »</a></b>
	</p>
</div>

<h3>Email registration</h3>

<p><img src="<%= request.getContextPath() %>/images/stork/email.jpg" alt="Email" width="83px" height="51px" /></p>
<p class="mtop1.5"> 
Alternatively you can submit the application via email registration. By registering in the system with the email you receive in your inbox a link that will give access to the application form.
</p>


<div class="h_box">
	<p  class="mvert05">
		To submit your Erasmus Programme application with your national citizen card click the following link:
		<b><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= f("%s/candidacies/erasmus/preregistration", request.getContextPath()) %>">Fill the application form »</a></b>
	</p>
</div>

<p class="mtop3"></p>

<h2>Access application form</h2>

<p class="mtop1.5"> 
After the submission you made view you alter your application form. The access depends in the way you submitted you application process. 
</p>

<h3>National Citizen Card</h3>

<p class="mtop1.5">
	If you submitted the application form with your national citizen card you can access it by authenticating in the Identification Plataform.
</p>

<div class="h_box">	
	<p class="mvert05">
		To access your application form please authenticate by following this link <b><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= f("%s/candidacies/erasmus/recoveraccess", request.getContextPath()) %>">Access to application form »</a></b>
	</p>
</div>


<h3>Email registration</h3>

<p class="mtop1.5">
	With your application submission an email was sent to your mailbox with a link which gives you access to your application form. 
</p>

<div class="h_box">	
	<p class="mvert05">
		<img src="<%= request.getContextPath() %>/images/stork/icon_info.gif" />
		If you lost you can recover by following the link <b><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= f("%s/candidacies/erasmus/recoveraccess", request.getContextPath()) %>">Recover access link »</a></b>
	</p>
</div>
