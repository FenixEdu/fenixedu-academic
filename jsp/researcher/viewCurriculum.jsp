<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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
		
		<ul style="list-style: none;">
			<li>
				<strong> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.name"/> : </strong> &nbsp;
			</li>
			<li>
				<strong> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.number"/> : </strong> &nbsp;
			</li>
			<li>
				<strong> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.category"/> : </strong> &nbsp;
			</li>			
		</ul>
		
		<!-- Event Participation -->
		<h3 id='eventParticipationTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.eventParticipationTitle"/> </span> </h3>
		
		<!-- Project Participation -->
		<h3 id='projectParticipationTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.projectParticipationTitle"/> </span> </h3>
		
		<!-- Publications -->
		<h3 id='publicationsTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.publicationsTitle"/> </span> </h3>
		
		<!-- Patents -->
		<h3 id='patentsTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.patentsTitle"/> </span> </h3>
		
		<!-- Products -->
		<h3 id='productsTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.productsTitle"/> </span> </h3>
		

</logic:present>
		
<br/>
<br/>