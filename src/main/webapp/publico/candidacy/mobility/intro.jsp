<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>



<h1><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></h1>



<p>Welcome to the application submission process!</p>

<h2>Required Documents</h2>

<p class="mvert05">After you fill and submit your application form, you are required to upload the following documents:</p>
<p class="mvert05">
	<ul>
		<li><b>Photo</b> (passport-like) - The photo will be used to generate <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> student card. Please upload a passport-like photo.</li>
		<li><b>Passport or identity card</b> (image scan)</li>
		<li><b>Curriculum vitae</b></li>
		<li><b>Transcript of records</b></li>
		<li><b>Learning agreement</b> - The learning agreement will be generated and available in your application process after the submission. You're required to download, sign and stamp.</li>
	</ul>
</p>


<h2>Application Form</h2>

<p>You can start the filling the application form in two ways: by email or using your National Citizen Card with a smart card reader.</p>


<div class="h_box">

	<h3 class="mtop05">Authentication with National Citizen Card</h3>
	
	<%--
	<p><img src="<%= request.getContextPath() %>/images/stork/stork.jpg" alt="<bean:message key="stork.logo" bundle="IMAGE_RESOURCES" />"/></p>
	--%>
	
	<p>The STORK project is an effort to establish an European Identification Platform that will allow citizens to establish new electronic relations in the European Union, just by presenting their national citizen card. So, in this context you can identify yourself securely and easily.</p>

	
	<p class="mvert05">
		<img src="<%= request.getContextPath() %>/images/stork/icon_info.gif" />
		To fill your application with national citizen card you need a smart card reader to authenticate in the Identification Platform.
	</p>
	<p class="mtop05">
		<img src="<%= request.getContextPath() %>/images/stork/icon_info.gif" />
		At this moment only citizens of Spain and Belgium are able to authenticate in the European Identification Platform <%-- <img src="<%= request.getContextPath() %>/images/stork/spain_flag.gif"/> --%>
	</p>

	<p  class="mtop05">
		To submit your Erasmus Programme application with your national citizen card click the following link:
	</p>

	<%--
	<b>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %>
		<a href="<%= f("%s/candidacies/erasmus/nationalCardSubmission", request.getContextPath()) %>">Fill the application form »</a>
	</b>
	--%>


	<h3 class="mtop05">Authentication with Email</h3>
	
	<%--
	<p><img src="<%= request.getContextPath() %>/images/stork/email.jpg" alt="Email"/></p>
	--%>
	
	<p> 
		If you don't have a "smart card reader "you may submit the application using email authentication. By registering in the system with the email you receive in your inbox a link that will give access to the application form.
	</p>

	
	
	<h3>Fill the application form</h3>

	<p>Choose the authentication method to start filling the application form:</p>	

	<p class="mtop1 mbottom05">
		<input type="button" value="Email authentication"/> or
		<input type="button" value="Citizen card authentication"/>
	</p>
	

</div>




<h2 class="mtop15">After submission</h2>

<p> 
	After you have submitted the application you can view and edit it. There are two ways to access the submitted application, depending on the authentication method you used.
</p>

<p>
	If you submitted the application authenticating your national citizen card you can 
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= f("%s/candidacies/erasmus/recoveraccess", request.getContextPath()) %>"><b>access it by authenticating using the identification platform</b></a>.
</p>

<p>
	If you submitted the application authenticating with your email you received an email message with a link which gives you access to your application form.
</p>
<p>	
	If you lost the email message you can recover the application link here: <%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= f("%s/candidacies/erasmus/recoveraccess", request.getContextPath()) %>"><b>recover access link</b></a> 
</p>



<%--

<h3>National Citizen Card</h3>

<p>
	If you submitted the application form using your national citizen card you can access it by authenticating in the Identification Plataform.
</p>

<p>
	To access your application form please follow this link: <%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= f("%s/candidacies/erasmus/nationalCardApplicationAccess", request.getContextPath()) %>"><b>Access to application form</b></a>
</p>



<h3>Email registration</h3>

<p>
	If you submitted the application form using your email you received an email message with a link which gives you access to your application form. 
</p>

<p>
	<img src="<%= request.getContextPath() %>/images/stork/icon_info.gif" />
	If you lost you can recover by following the link: <%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= f("%s/candidacies/erasmus/recoverLinkAccess", request.getContextPath()) %>"><b>Recover access link</b></a>
</p>

--%>