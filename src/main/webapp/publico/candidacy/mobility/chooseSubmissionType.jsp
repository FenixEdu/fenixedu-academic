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

<p>
	If you have national citizen card you can submit your application with it. The application will read information from your citizen card which you can use to access your application process.
	Alternatively you can submit with an access link sent to you email.
</p>

<table>
	<tbody>
		<tr>
			<td>
				<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= f("%s/candidacies/erasmus/nationalCardSubmission", request.getContextPath()) %>'>
					<img src="<%= request.getContextPath() %>/images/stork/stork.jpg" alt="<bean:message key="stork.logo" bundle="IMAGE_RESOURCES" />" />
				</a>
			</td>
			<td>
				<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= f("%s/candidacies/erasmus/preregistration", request.getContextPath()) %>'>
					<img src="<%= request.getContextPath() %>/images/stork/email.jpg" alt="Email" />" />
				</a>
			</td>
		</tr>
		<tr>
			<td style="text-align: center;"><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= f("%s/candidacies/erasmus/nationalCardSubmission", request.getContextPath()) %>'><b>Submit with your European e-ID »</b></a></td>
			<td style="text-align: center;"><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= f("%s/candidacies/erasmus/preregistration", request.getContextPath()) %>'><b>Submit with the email access link »</b></a></td>
		</tr>
	</tbody>
</table>

