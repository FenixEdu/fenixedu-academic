<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<style>
		table.search {
		background-color: #f5f5f5;
		border-collapse: collapse;
		}
		table.search tr td {
		border: 1px solid #fff;
		padding: 0.3em;
		}
		.leftcolumn {
		text-align: right;
		}
		
		h3.cd_heading {
		font-weight: normal;
		margin-top: 3em;
		border-top: 1px solid #e5e5e5;
		background-color: #fafafa;
		padding: 0.25em 0 0em 0.25em;
		padding: 0.5em 0.25em;
		}
		h3.cd_heading span {
		margin-top: 2em;
		border-bottom: 2px solid #fda;
		}
		
		div.cd_block {
		background-color: #fed;
		padding: 0.5em 0.5em 0.5em 0.5em;
		}
		
		table.cd {
		border-collapse: collapse;
		}
		table.cd th {
		border: 1px solid #ccc;
		background-color: #eee;
		padding: 0.5em;
		text-align: center;
		}
		table.cd td {
		border: 1px solid #ccc;
		background-color: #fff;
		padding: 0.5em;
		text-align: center;
		}
		
		p.insert {
		padding-left: 2em;
		}
		div.cd_float {
		width: 100%;
		float: left;
		padding: 0 2.5em;
		padding-bottom: 1em;
		}
		ul.cd_block {
		width: 43%;
		list-style: none;
		float: left; 
		margin: 0;
		padding: 0;
		padding: 1em;
		}
		ul.cd_block li {
		}
		ul.cd_nostyle {
		list-style: none;
		}
</style>


<logic:present role="RESEARCHER">		

		<h2 id='pageTitle'/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.title"/> </h2>
		
				
		<!-- Personal Information -->
		<h3 id='personalInformationTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.personalInformationTitle"/> </span> </h3>
		
		<fr:view name="UserView" property="person" layout="tabular" schema="researcher.person.personalInfo">
			<fr:layout>
				<fr:property name="columnClasses" value="labelColumnClass,valueColumnClass"/>
			</fr:layout>
		</fr:view>
		
		<!-- Research Interests -->

		<h3 id='researchInterestsTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.researchInterestsTitle"/> </span> </h3>
		<fr:view name="researchInterests" >
			<fr:layout>
				<fr:property name="ordered" value="true"/>
				<fr:property name="sortBy" value="order"/>
				<fr:property name="eachLayout" value="values"/>
				<fr:property name="eachSchema" value="researchInterest.title"/>
			</fr:layout>
		</fr:view>

		
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

<%--
		<!-- Publications -->
		
		<h3 id='publicationsTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.publicationsTitle"/> </span> </h3>
		
		<fr:view name="resultPublications">
			<fr:layout>
				<fr:property name="eachLayout" value="values-comma"/>
				<fr:property name="eachSchema" value="person.result"/>
			</fr:layout>
		</fr:view>
		
		<!-- Patents -->
		<h3 id='patentsTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.patentsTitle"/> </span> </h3>
		
		<fr:view name="resultPatents">
			<fr:layout>
				<fr:property name="eachLayout" value="values-comma"/>
				<fr:property name="eachSchema" value="person.result"/>
			</fr:layout>
		</fr:view>
		<logic:empty name="resultPatents">
			<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.listPatentsUseCase.emptyList"/></em></p>
		</logic:empty>
--%>	
</logic:present>
		
<br/>
<br/>