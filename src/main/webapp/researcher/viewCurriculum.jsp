<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%>
<%@page import="net.sourceforge.fenixedu.domain.research.result.publication.PreferredPublicationPriority"%>
<html:xhtml/>

<%@page import="org.apache.struts.util.RequestUtils"%>

<em><bean:message key="label.researchPortal" bundle="RESEARCHER_RESOURCES"/></em>
<h2 id="header"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.title"/></h2>

<bean:define id="personId" name="person" property="externalId"/>

<fr:form action="<%="/viewCurriculum.do?personOID=" + personId %>">
<fr:edit id="executionYearIntervalBean" name="executionYearIntervalBean" visible="false"/>

<p class="mbottom025"><bean:message key="label.choosen.interval" bundle="RESEARCHER_RESOURCES"/>:</p>

<table class="tstyle5 mtop025">
<tr>
	<td>
		<bean:message key="label.start" bundle="RESEARCHER_RESOURCES"/>:	  
		<fr:edit id="firstYear" name="executionYearIntervalBean" slot="firstExecutionYear">
			<fr:layout name="menu-select">
			<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
			<fr:property name="format" value="${year}"/>
			<fr:property name="defaultText" value="label.undefined"/>
			<fr:property name="key" value="true"/>
			<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
		</fr:layout>
		</fr:edit>
	</td>						  
	<td>
		<bean:message key="label.end" bundle="RESEARCHER_RESOURCES"/>:

		<fr:edit id="finalYear" name="executionYearIntervalBean" slot="finalExecutionYear">
			<fr:layout name="menu-select">
			<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
			<fr:property name="format" value="${year}"/>
			<fr:property name="defaultText" value="label.undefined"/>
			<fr:property name="key" value="true"/>
			<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
		</fr:layout>
		</fr:edit>

		<html:submit><bean:message key="label.filter" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</td>
</tr>
</table>
</fr:form>

<p id="index" class="mbottom025"><bean:message key="label.index" />:</p>
<ol class="mtop025">
	<li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#personalInformationTitle"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.personalInformationTitle"/></a></li>
    <logic:notEmpty name="lectures">
    <li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#lecturesTitle"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.lecturedCoursesInformation"/></a></li>
	</logic:notEmpty>
	<logic:notEmpty  name="final_works">	
		<li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#guidancesTitle"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.orientationInformation"/></a></li>
	</logic:notEmpty>
	<logic:empty  name="final_works">	
    	<logic:notEmpty  name="guidances">	
			<li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#guidancesTitle"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.orientationInformation"/></a></li>
		</logic:notEmpty>
	</logic:empty>
	<logic:notEmpty name="functions">
	<li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#functionsTitle"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.functionsInformation"/></a></li>
	</logic:notEmpty>
	<logic:notEmpty name="researchInterests">
	<li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#researchInterestsTitle"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.researchInterestsTitle"/></a></li>
	</logic:notEmpty>
	<logic:notEmpty name="resultPublications">
	<li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#publicationsTitle"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.publicationsTitle"/></a></li>
	</logic:notEmpty>
	<logic:notEmpty name="resultPatents">
	<li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#patentsTitle"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.patentsTitle"/></a></li>
	</logic:notEmpty>
	<logic:notEmpty name="participations"> 
	<li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#activities"><bean:message bundle="RESEARCHER_RESOURCES" key="link.activitiesManagement"/></a></li>
	</logic:notEmpty>
	<logic:notEmpty name="prizes">
	<li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#prizes"><bean:message bundle="RESEARCHER_RESOURCES" key="label.prizes"/></a></li>
	</logic:notEmpty>
    <logic:notEmpty name="career">
    <li><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#career"><bean:message bundle="RESEARCHER_RESOURCES" key="label.career"/></a></li>
    </logic:notEmpty>
</ol>

<!-- Personal Information -->
<p id='personalInformationTitle' class="separator2" style="position: relative; width: 99%; margin-top: 2em;">
	<span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.personalInformationTitle"/></span>
	<span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
</p>

<ul>			
<li><bean:message key="researcher.viewCurriculum.name" bundle="RESEARCHER_RESOURCES"/>: <strong><fr:view name="person" property="nickname"/></strong> <logic:present name="person" property="teacher"> (<fr:view name="person" property="teacher.teacherId"/>)</li>
</logic:present>

<logic:present role="TEACHER">		
	<logic:present name="person" property="teacher">
		<logic:present name="person" property="teacher.category">
			<li><bean:message key="label.teacher.category" bundle="APPLICATION_RESOURCES"/>: <fr:view name="person" property="teacher.category.name"/></li>
		</logic:present>
	</logic:present>
</logic:present>
</ul>
	
<logic:present name="person" property="teacher">

<!-- Lectures -->
<logic:notEmpty name="lectures">
<p id='lecturesTitle' class="separator2" style="position: relative; width: 99%;">
	<span><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.lecturedCoursesInformation"/></span>
	<span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
</p>


	<ul>
    <logic:iterate id="lecture" name="lectures" type="net.sourceforge.fenixedu.domain.ExecutionCourse">
		<li>
		
		<app:contentLink name="lecture" property="site" target="blank">
				<fr:view name="lecture" property="nome"/>
		</app:contentLink>
		 (<fr:view name="lecture" property="executionYear.year"/>, <fr:view name="lecture" property="executionPeriod.name"/>, <fr:view name="lecture" property="degreePresentationString"/>)</li>
	</logic:iterate>
	</ul>
</logic:notEmpty>

<!-- Final Works -->
<logic:notEmpty  name="final_works">	
<p id='guidancesTitle' class="separator2" style="position: relative; width: 99%; margin-top: 2em;">
	<span><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.orientationInformation"/></span>
	<span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
</p>
</logic:notEmpty>
<logic:empty  name="final_works">	
    <logic:notEmpty  name="secondCycleThesis">	
	    <p id='guidancesTitle' class="separator2" style="position: relative; width: 99%; margin-top: 2em;">
	    	<span><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.orientationInformation"/></span>
	    	<span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
	    </p>
	</logic:notEmpty>
</logic:empty>

<logic:notEmpty  name="final_works">	
	<p class="indent1"><em><bean:message key="label.common.finalDegreeWorks" bundle="DEPARTMENT_MEMBER_RESOURCES"/></em></p>

	<logic:iterate id="final_work" name="final_works">
		<div style="float: left; width: 300px; margin: 0; padding: 0;">
		<ul style="margin-top: 0; margin-bottom: 0;">
		<li class="smalltxt" style="margin: 0; padding: 0;">
			<span class="color888"><fr:view name="final_work" property="startExecutionPeriod.executionYear.year"/></span> - 
			<span title="<fr:view name="final_work" property="student.person.name"/>"><fr:view name="final_work" property="student.person.firstAndLastName"/> (<fr:view name="final_work" property="student.number"/>)</span>
		</li>
		</ul>
		</div>
	</logic:iterate>
	<div style="clear: both;"></div>
</logic:notEmpty>

<logic:notEmpty name="secondCycleThesis">
<p class="indent1 mtop15"><em><bean:message key="label.common.masterDegree" bundle="DEPARTMENT_MEMBER_RESOURCES"/></em></p>
<ul>
<logic:notEmpty name="guidances">
<logic:iterate id="guidance" name="guidances">
	<li><fr:view name="guidance" property="dissertationTitle"/>, <fr:view name="guidance" property="masterDegreeThesis.studentCurricularPlan.student.person.name"/> (<bean:message key="label.teacher.details.orientationInformation.masterDegreeProofDate" bundle="DEPARTMENT_MEMBER_RESOURCES"/>:
	<logic:present name="guidance" property="masterDegreeThesis.activeMasterDegreeProofVersion">
	  <fr:view name="guidance" property="masterDegreeThesis.activeMasterDegreeProofVersion.proofDate" type="java.lang.String"/>)
	</logic:present>
	<logic:notPresent name="guidance" property="masterDegreeThesis.activeMasterDegreeProofVersion">
	  <bean:message bundle="RESEARCHER_RESOURCES" key="label.researcher.thesis.notEvaluated"/>)
	</logic:notPresent>
	</li>
</logic:iterate>
</logic:notEmpty>

<logic:notEmpty name="orientedThesis">
<logic:iterate id="thesis" name="orientedThesis">
   <li><fr:view name="thesis" property="finalTitle"/>, <fr:view name="thesis" property="student.person.name" /> (<bean:message key="label.teacher.details.orientationInformation.masterDegreeProofDate" bundle="DEPARTMENT_MEMBER_RESOURCES"/>:
       <fr:view name="thesis" property="evaluation" type="org.joda.time.DateTime">
           <fr:layout name="null-as-label">
               <fr:property name="key" value="true" />
               <fr:property name="bundle" value="RESEARCHER_RESOURCES" />
               <fr:property name="label" value="label.researcher.thesis.notEvaluated" />
           </fr:layout>
       </fr:view>)
   </li>
</logic:iterate>
</logic:notEmpty>
</ul>
</logic:notEmpty>


<!-- Functions -->	
<logic:notEmpty name="functions">
<p id='functionsTitle' class="separator2" style="position: relative; width: 99%; margin-top: 2em;">
	<span><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.functionsInformation"/></span>
	<span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
</p>

<ul>
<logic:iterate id="personFunction" name="functions">
	<li>
		<span class="color888"><fr:view name="personFunction" property="beginDateInDateType"/> <bean:message key="label.until" bundle="RESEARCHER_RESOURCES"/> <fr:view name="personFunction" property="endDateInDateType"/></span>, 
		<fr:view name="personFunction" property="function.name"/> (<fr:view name="personFunction" property="unit.name"/>)
	</li>
</logic:iterate>
</ul>
</logic:notEmpty>

</logic:present>
		
<!-- Research Interests -->
	<logic:notEmpty name="researchInterests">
    <p id='researchInterestsTitle' class="separator2" style="position: relative; width: 99%; margin-top: 2em;">
    	<span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.researchInterestsTitle"/></span>
    	<span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
    </p>

	<fr:view name="researchInterests" >
		<fr:layout>
			<fr:property name="ordered" value="true"/>
			<fr:property name="sortBy" value="order"/>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="researchInterest.title"/>
		</fr:layout>
	</fr:view>
	</logic:notEmpty>
	
	<%-- 
	<!-- Event Participation -->
	<h3 id='eventParticipationTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.eventParticipationTitle"/> </span> </h3>
	<fr:view name="events">
		<fr:layout>
			<fr:property name="eachLayout" value="values-dash"/>
			<fr:property name="eachSchema" value="event.summary"/>
		</fr:layout>
	</fr:view>
	
	<!-- Project Participation -->
	<h3 id='projectParticipationTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.projectParticipationTitle"/> </span> </h3>
	
	<fr:view name="projects">
		<fr:layout>
			<fr:property name="eachLayout" value="values-dash"/>
			<fr:property name="eachSchema" value="project.summary"/>
		</fr:layout>
	</fr:view>
	--%>
	
<!-- Publications -->

<logic:notEmpty name="resultPublications">
<p id='publicationsTitle' class="separator2" style="position: relative; width: 99%; margin-top: 2em;">
	<span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.publicationsTitle"/></span>
	<span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
</p>
	
<logic:notEmpty name="books">
	<p id='books' class="mtop2 mbottom0"><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Books"/></span></strong></p>
	<bean:define id="currentSchema" value="result.publication.presentation.Book" toScope="request"/>
	<bean:define id="results" name="books" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="inbooks">
	<p id='inbooks' class="mtop2 mbottom0"><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.BookParts"/></span></strong></p>
	<bean:define id="currentSchema" value="result.publication.presentation.BookPart" toScope="request"/>
	<bean:define id="results" name="inbooks" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
</logic:notEmpty>

<bean:define id="hasArticles" value="false" toScope="request"/>
<logic:notEmpty name="national-articles"> 
	<bean:define id="hasArticles" value="true" toScope="request"/>
</logic:notEmpty>
<logic:notEmpty name="international-articles"> 
	<bean:define id="hasArticles" value="true" toScope="request"/>
</logic:notEmpty>	

<logic:equal name="hasArticles" value="true">
	<p id='books' class="mtop2 mbottom0"/>
	<bean:define id="currentSchema" value="result.publication.presentation.Article" toScope="request"/>

	<logic:notEmpty name="international-articles">
	<p class="mtop2 mbottom0"><strong><bean:message key="label.internationalArticles" bundle="RESEARCHER_RESOURCES"/></strong></p>
	<bean:define id="results" name="international-articles" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="national-articles">
	<p class="mtop2 mbottom0"><strong><bean:message key="label.nationalArticles" bundle="RESEARCHER_RESOURCES"/></strong></p>
	<bean:define id="results" name="national-articles" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
	</logic:notEmpty>
</logic:equal>

<logic:notEmpty name="international-inproceedings">
	<p id='inproceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.InternationalInproceedings"/></span></strong></p>
	<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
	<bean:define id="results" name="international-inproceedings" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="national-inproceedings">
	<p id='inproceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.NationalInproceedings"/></span></strong></p>
	<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
	<bean:define id="results" name="national-inproceedings" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="proceedings">
	<p id='proceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Proceedings"/></span></strong></p>
	<bean:define id="currentSchema" value="result.publication.presentation.Proceedings" toScope="request"/>
	<bean:define id="results" name="proceedings" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="theses">
	<p id='theses' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Theses"/></span></strong></p>
	<bean:define id="currentSchema" value="result.publication.presentation.Thesis" toScope="request"/>
	<bean:define id="results" name="theses" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="manuals">
	<p id='manuals' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Manuals"/></span></strong></p>
	<bean:define id="currentSchema" value="result.publication.presentation.Manual" toScope="request"/>
	<bean:define id="results" name="manuals" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="technicalReports">
	<p id='technicalReports' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.TechnicalReports"/></span></strong></p>
	<bean:define id="currentSchema" value="result.publication.presentation.TechnicalReport" toScope="request"/>
	<bean:define id="results" name="technicalReports" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="otherPublications">
	<p id='otherPublications' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.OtherPublications"/></span></strong></p>
	<bean:define id="currentSchema" value="result.publication.presentation.OtherPublication" toScope="request"/>
	<bean:define id="results" name="otherPublications" toScope="request"/>
	<jsp:include page="result/publications/publicationsResume.jsp"/>
</logic:notEmpty>

<logic:notEmpty name="unstructureds">
	<p id='unstructureds' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Unstructureds"/></span></strong></p>
	<bean:define id="results" name="unstructureds" toScope="request"/>
    <table class="publications mtop1">
		<logic:iterate id="result" name="results" scope="request">
 			<bean:define id="resultId" name="result" property="externalId"/>
 			<tr>
 			   <td>
					<bean:define id="level" name="result" property="preferredLevel.name" type="java.lang.String" />
					<span title='<bean:message bundle="ENUMERATION_RESOURCES" key="<%= PreferredPublicationPriority.class.getSimpleName() + "." + level + ".description"  %>"/>'>
					<fr:view name="result" property="preferredLevel" />
					</span>
 			   </td>
 			   <td>
		 			<fr:view name="result" layout="values" schema="result.publication.presentation.Unstructured">
		 				<fr:layout>
		 					<fr:property name="htmlSeparator" value=", "/>
		 					<fr:property name="indentation" value="false"/>
		 				</fr:layout>
	 				</fr:view>
 				</td>
	 	     </tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<p><em>
    <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.preferredPublications.help2"/>
    <html:link page="/resultPublications/listPublications.do"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.management.title"/></html:link>
</em></p>
</logic:notEmpty>

<%-- 
<ul>
<logic:iterate id="result" name="resultPublications" type="net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication">

<li><fr:view name="result" layout="nonNullValues" schema="<%= result.getSchema() %>">
	<fr:layout>
		<fr:property name="classes" value="mbottom025"/>
		<fr:property name="htmlSeparator" value=", "/>
		<fr:property name="indentation" value="false"/>
	</fr:layout>
	<fr:destination name="view.publication" module="" path="<%="/researcher/resultPublications/showResultForOthers.do?resultId=" + result.getExternalId() %>"/>
</fr:view>
</li>
</logic:iterate>	
</ul>

</logic:notEmpty>
<!--  
<logic:empty name="resultPublications">
	<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.emptyList"/></em></p>
</logic:empty>
-->			
	
--%>
		
<!-- Patents -->
<logic:notEmpty name="resultPatents">
<p id='patentsTitle' class="separator2" style="position: relative; width: 99%; margin-top: 2em;">
	<span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.patentsTitle"/></span>
	<span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
</p>		


	<ul>
	<logic:iterate id="result" name="resultPatents" type="net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent">
		<li><strong>
		<a href="<%= request.getContextPath() + "/researcher/resultPublications/showResultForOthers.do?resultId=" + result.getExternalId() %>">
		<fr:view name="result" property="title"/></a>	 			
		</strong>
		<span style="color: #888;">
 			<bean:message key="label.registrationDate" bundle="RESEARCHER_RESOURCES"/>
 			<fr:view name="result" property="registrationDate"/>
 		</span>
 		 - 
		<span style="color: #888;">
			<bean:message key="label.approvalDate" bundle="RESEARCHER_RESOURCES"/>
 			<fr:view name="result" property="approvalDate"/>
 		</span></li>
	</logic:iterate>
	</ul>
</logic:notEmpty>
	<!--  
	<logic:empty name="resultPatents">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.emptyList"/></em></p>
	</logic:empty>
	-->

<logic:notEmpty name="participations">
    <p id='activities' class="separator2" style="position: relative; width: 99%; margin-top: 2em;">
    	<span><bean:message bundle="RESEARCHER_RESOURCES" key="link.activitiesManagement"/></span>
    	<span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
    </p>


	<logic:notEmpty name="international-events">
		<p id='events' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-events" toScope="request"/>
		<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
		<bean:define id="schema" value="researchEventParticipation.summary" toScope="request"/>
		<jsp:include page="activities/researchActivitiesList.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="national-events">
		<p id='events' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-events" toScope="request"/>
		<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
		<bean:define id="schema" value="researchEventParticipation.summary" toScope="request"/>
		<jsp:include page="activities/researchActivitiesList.jsp"/>
		
	</logic:notEmpty>
	
	<logic:notEmpty name="international-eventEditions">
		<p id='events' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditionsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-eventEditions" toScope="request"/>
		<bean:define id="forwardTo" value="EditEventEdition" toScope="request"/>
		<bean:define id="schema" value="researchEventEditionParticipation.summary" toScope="request"/>
		<jsp:include page="activities/researchActivitiesList.jsp"/>
	</logic:notEmpty>
		
	<logic:notEmpty name="national-eventEditions">
		<p id='events' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditionsTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-eventEditions" toScope="request"/>
		<bean:define id="forwardTo" value="EditEventEdition" toScope="request"/>
		<bean:define id="schema" value="researchEventEditionParticipation.summary" toScope="request"/>
		<jsp:include page="activities/researchActivitiesList.jsp"/>
		
	</logic:notEmpty>

	<logic:notEmpty name="international-journals">
		<p id='scientificJournals' class="mtop2 mbottom0">
		<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournalsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-journals" toScope="request"/>
		<bean:define id="forwardTo" value="EditScientificJournal" toScope="request"/>
		<bean:define id="schema" value="researchScientificJournalParticipation.summary" toScope="request"/>
		<jsp:include page="activities/researchActivitiesList.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="national-journals">
		<p id='scientificJournals' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournalsTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-journals" toScope="request"/>
		<bean:define id="forwardTo" value="EditScientificJournal" toScope="request"/>
		<bean:define id="schema" value="researchScientificJournalParticipation.summary" toScope="request"/>
		<jsp:include page="activities/researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="international-issues">
		<p id='issues' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.issueTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-issues" toScope="request"/>
		<bean:define id="forwardTo" value="EditJournalIssue" toScope="request"/>
		<bean:define id="schema" value="researchJournalIssueParticipation.summary" toScope="request"/>
		<jsp:include page="activities/researchActivitiesList.jsp"/>
	</logic:notEmpty>
		
	<logic:notEmpty name="national-issues">
		<p id='issues' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.issueTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-issues" toScope="request"/>
		<bean:define id="forwardTo" value="EditJournalIssue" toScope="request"/>
		<bean:define id="schema" value="researchJournalIssueParticipation.summary" toScope="request"/>
		<jsp:include page="activities/researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="cooperations">	
		<p id='cooperations' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperationsTitle" /></span></strong>
		</p>
		<bean:define id="participations" name="cooperations" toScope="request"/>
		<bean:define id="forwardTo" value="EditCooperation" toScope="request"/>
		<bean:define id="schema" value="researchCooperationParticipation.summary" toScope="request"/>
		<jsp:include page="activities/researchActivitiesList.jsp"/>
	</logic:notEmpty>
</logic:notEmpty>
	
<logic:notEmpty name="prizes">
	  <p id='prizes' class="separator2" style="position: relative; width: 99%; margin-top: 2em;">
    	<span><bean:message bundle="RESEARCHER_RESOURCES" key="label.prizes"/></span>
    	<span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
    	</p>
		
		<ul class="listresearch">
		<logic:iterate id="prize" name="prizes">
			<bean:define id="prizeID" name="prize" property="externalId"/>
			<li class="mtop1">
				<p class="mvert0">
		 			<strong>
						<html:link page="<%= "/prizes/prizeManagement.do?method=showPrize&oid=" + prizeID %>"><fr:view name="prize" property="name"/></html:link>	 			
		 			</strong>
		 			 , 
		 			<span>
		 				<fr:view name="prize" property="year"/>
			 		</span>
		 		</p>
		 		<logic:present name="prize" property="researchResult">
				<bean:define id="resultId" name="prize" property="researchResult.externalId"/>
		 		<p class="mvert0">
		 			<span>
		 				<html:link page="<%= "/resultPublications/showPublication.do?resultId=" + resultId %>">
			 				<fr:view name="prize" property="researchResult.title"/>
		 				</html:link>
			 		</span>
	 			</p>
	 			</logic:present>
	 			<logic:present name="prize" property="description">
				<p class="mvert0">
					<span class="color888">
						<fr:view name="prize" property="description"/>
					</span>
				</p>
				</logic:present>
 			</li>
 			
 			</logic:iterate>
		</ul>
		
		
</logic:notEmpty>

<logic:notEmpty name="career">
	<p id='career' class="separator2" style="position: relative; width: 99%; margin-top: 2em;">
	   <span><bean:message bundle="RESEARCHER_RESOURCES" key="label.career" /></span>
	   <span style="position: absolute; right: 5px;"><%=GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#header"><bean:message key="label.top" /></a></span>
	</p>

    <ul>
    <logic:iterate id="line" name="career">
		<li>
            <span class="color888"><fr:view name="line" property="beginYear"/><logic:present name="line" property="endYear"> <bean:message key="label.until" bundle="RESEARCHER_RESOURCES"/> <fr:view name="line" property="endYear"/></span></logic:present>, 
            <fr:view name="line" property="function"/> (<fr:view name="line" property="entity"/>)
        </li>
    </logic:iterate>
    </ul>
</logic:notEmpty>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
