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

<%@ page import="java.util.Locale"%>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>

<%!
	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.mobility" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>


<h1><bean:write name="application.name" bundle="CANDIDATE_RESOURCES"/></h1>


<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error" >
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="<%= org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE %>" >
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>


<fr:form action='<%= mappingPath + ".do?method=bindEmailWithHashCodeAndSendMailWithLink" %>'>

	<p><bean:message key="message.email.required.begin.process" bundle="CANDIDATE_RESOURCES" /></p>
	
	<bean:message key="label.email" bundle="CANDIDATE_RESOURCES" />:
	
	<fr:edit id="PublicAccessCandidacy.preCreationForm" 
		 name="candidacyPreCreationBean" 
		 type="net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.IndividualCandidacyProcessPublicDA$CandidacyPreCreationBean"
		 slot="email">
		 <fr:layout>
		 	<fr:property name="classes" value="mvert05"/>
		 	<fr:property name="size" value="35"/>
		 	<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredEmailValidator"/>
			<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
				<fr:property name="type" value="character"/>
				<fr:property name="length" value="100"/>
			</fr:validator>
		 </fr:layout>
		 <fr:destination name="invalid" path='<%= mappingPath + ".do?method=preparePreCreationOfCandidacyInvalid" %>'/>
	</fr:edit>
	
	<p><span class="error0"><fr:message for="PublicAccessCandidacy.preCreationForm"/></span></p>
	
	<div class="mtop15 mbottom1">
		<label for="captcha"><bean:message key="label.captcha" bundle="ALUMNI_RESOURCES" />:</label>
		<div class="mbottom05">
			<img src="<%= request.getContextPath() + "/publico/jcaptcha.do" %>" style="border: 1px solid #bbb; padding: 5px;"/>
		</div>
		<span class="color777"><bean:message key="label.captcha.process" bundle="ALUMNI_RESOURCES" /></span><br/>
		<input type="text" name="j_captcha_response" size="30" style="margin-bottom: 1em;"/>
	</div>

	<p class="mtop1 mbottom0"><html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" locale="en"/> »</html:submit></p>

</fr:form>

