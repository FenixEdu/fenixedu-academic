<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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

		<!-- Publications -->
		<h3 id='publicationsTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.publicationsTitle"/> </span> </h3>
		<div style="padding: 00px 00px 00px 20px" >
			<logic:notEmpty name="resultPublications">
				<fr:view name="resultPublications">
					<fr:layout name="tabular-list">
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="result.simple.detail"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
			<logic:empty name="resultPublications">
				<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.emptyList"/></em></p>
			</logic:empty>
		</div>		
		
		<!-- Patents -->
		<h3 id='patentsTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.patentsTitle"/> </span> </h3>
		<div style="padding: 00px 00px 00px 20px" >
			<logic:notEmpty name="resultPatents">
				<fr:view name="resultPatents">
					<fr:layout name="tabular-list">
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="result.simple.detail"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
			<logic:empty name="resultPatents">
				<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.emptyList"/></em></p>
			</logic:empty>
		</div>
</logic:present>
