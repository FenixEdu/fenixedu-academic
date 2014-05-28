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

<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="java.util.Locale"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType"%>

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

<h1><bean:write name="application.name"/></h1>
<bean:define id="mobilityProgram" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationAgreement.description"/>
<h1><strong><bean:write name="mobilityProgram"/></strong></h1>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess"/>
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcess" property="OID"/>

<p><a href='<%= f("%s?method=backToViewCandidacy&individualCandidacyProcess=%s", fullPath, individualCandidacyProcessOID) %>'>« <bean:message key="label.back" bundle="CANDIDATE_RESOURCES"/></a></p>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateEmployee" value="true">
	<p><bean:message key="message.application.employee.edition.forbidden" bundle="CANDIDATE_RESOURCES"/></p>
</logic:equal>

<%--
<p><span><bean:message key="message.all.fields.are.required" bundle="CANDIDATE_RESOURCES"/></span></p>
--%>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="<%= ActionMessages.GLOBAL_MESSAGE %>">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>


<fr:form action='<%= mappingPath + ".do?method=editCandidacyInformation" %>' >
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
		<h2 class="mtop1"><bean:message key="label.erasmus.home.institution" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit 	id="erasmusIndividualCandidacyProcessBean.home.institution" 
					name="individualCandidacyProcessBean" 
					schema="ErasmusIndividualCandidacyProcess.exchange.coordinator.edit" 
					property="mobilityStudentDataBean">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= f("%s.do?method=editCandidacyInformationInvalid", mappingPath) %>'  />
		</fr:edit>
		
		<h2 class="mtop1"><bean:message key="label.erasmus.current.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit 	id="erasmusIndividualCandidacyProcessBean.current.study" 
					name="individualCandidacyProcessBean" 
					schema="ErasmusIndividualCandidacyProcess.current.study.edit" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= f("%s.do?method=editCandidacyInformationInvalid", mappingPath) %>'  />
		</fr:edit>
		
		<h2 class="mtop1"><bean:message key="label.erasmus.period.of.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit	id="erasmusIndividualCandidacyProcessBean.period.of.study"
					name="individualCandidacyProcessBean"
					schema="ErasmusIndividualCandidacyProcess.period.of.study.edit" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= f("%s.do?method=editCandidacyInformationInvalid", mappingPath) %>'  />
		</fr:edit>
	
		<logic:equal name="individualCandidacyProcessBean" property="candidacyProcess.forSemester" value="<%= ErasmusApplyForSemesterType.FIRST_SEMESTER.name() %>">
		<h2 class="mtop15 mbottom05"><bean:message key="label.erasmus.applyForSemester" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<p><em>Mark the semester's you're going to study at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></em></p>
		<fr:edit		id="erasmusStudentDataBean.applyForSemester.edit"
					name="individualCandidacyProcessBean"
					property="mobilityStudentDataBean"
					schema="ErasmusStudentDataBean.applyForSemester.edit">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop05"/>
		        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= f("%s.do?method=editCandidacyInformationInvalid", mappingPath) %>'  />
		</fr:edit>
		</logic:equal>
		
		<h2 class="mtop15 mbottom05"><bean:message key="label.observations" bundle="CANDIDATE_RESOURCES"/></h2>
		<fr:edit id="individualCandidacyProcessBean.observations"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.observations">
 		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>
		
	
	<p class="mtop15">	
		<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	</p>
</fr:form>
