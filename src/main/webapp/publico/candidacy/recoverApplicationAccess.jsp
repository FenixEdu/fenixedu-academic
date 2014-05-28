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


<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name" bundle="CANDIDATE_RESOURCES"/></h1>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:form action='<%= mappingPath + ".do?method=recoverApplicationAccess" %>'>
	<p><bean:message key="message.email.recovery.access.process" bundle="CANDIDATE_RESOURCES"/></p>
	
	<bean:message key="label.email" bundle="CANDIDATE_RESOURCES"/>:
	
	<fr:edit id="PublicAccessCandidacy.recoveryAccessForm" 
		 name="candidacyRecoveryForm" 
		 type="net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.IndividualCandidacyProcessPublicDA$CandidacyPreCreationBean"
		 slot="email">
		 <fr:layout>
		 	<fr:property name="size" value="30"/>
			<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
				<fr:property name="type" value="character"/>
				<fr:property name="length" value="100"/>
			</fr:validator>
		 </fr:layout>
	</fr:edit>
	
	<p class="mtop1 mbottom0"><html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /> »</html:submit></p>
</fr:form>
