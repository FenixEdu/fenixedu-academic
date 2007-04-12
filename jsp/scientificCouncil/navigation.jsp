<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present role="SCIENTIFIC_COUNCIL">
	<ul>
		<li class="navheader">
			<bean:message key="bolonha.process"/>
		</li>
		
		<li>
			<html:link page="/competenceCourses/competenceCoursesManagement.faces">
				<bean:message key="navigation.competenceCoursesManagement"/>
			</html:link>
		</li>

		<li>
			<html:link page="/curricularPlans/curricularPlansManagement.faces">
				<bean:message key="navigation.curricularPlansManagement"/>
			</html:link>
		</li>

        <li class="navheader">
            <bean:message key="scientificCouncil.thesis.process"/>
        </li>
        <li>
            <html:link page="/scientificCouncilManageThesis.do?method=listThesis">
                <bean:message key="navigation.list.jury.proposals"/>
            </html:link>  
        </li>

		<li class="navheader">
			<bean:message key="label.credits.navigation.header"/>
		</li>
		<li>
		  	<html:link page="/defineCreditsPeriods.do?method=showPeriods">
		  		<bean:message key="link.define.periods"/>
		  	</html:link>  
		</li>		
		<li>
			<html:link page="/functionsManagement/personSearchForFunctionsManagement.faces" >
				<bean:message key="link.managementPositions.management"/>
			</html:link>
		</li>
		<li>
			<html:link page="/masterDegreeCreditsManagement.do?method=prepare">
		  		<bean:message key="link.credits.masterDegree"/>
		  	</html:link>
		</li>
		<li>
		  	<html:link page="/showAllTeacherCreditsResume.do?method=prepareTeacherSearch">
		  		<bean:message key="link.teacher.sheet"/>
		  	</html:link>  
		</li>			
		<li>
			<html:link page="/creditsReport.do?method=prepare">
		  		<bean:message key="link.credits.viewReport"/>
		  	</html:link>
		</li>		
		<br />
		<li class="navheader">
			<bean:message key="label.research.activity.navigation.header"/>
		</li>
		<li>
			<html:link page="/editScientificJournal.do?method=prepare">
		  		<bean:message key="link.research.activity.edit.scientific.journal"/>
		  	</html:link>
		</li>		
		<li>
			<html:link page="/editEvent.do?method=prepare">
		  		<bean:message key="link.research.activity.edit.event"/>
		  	</html:link>
		</li>							
		<li>
			<html:link page="/mergeScientificJournal.do?method=prepare">
		  		<bean:message key="link.research.activity.merge.scientific.journal"/>
		  	</html:link>
		</li>				
		<li>
			<html:link page="/mergeEvents.do?method=prepare">
		  		<bean:message key="link.research.activity.merge.event"/>
		  	</html:link>
		</li>
				<br/>
		<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session
                    .getAttribute(net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants.U_VIEW);
            if (net.sourceforge.fenixedu.domain.ManagementGroups.isProtocolManagerMember(user.getPerson())) {

	    %>
			<li class="navheader">
				<bean:message key="label.protocols.navigation.header"/>
			</li>
			<li>
				<html:link page="/protocols.do?method=showProtocolAlerts">
			  		<bean:message key="link.protocols.showAlerts"/>
			  	</html:link>
			</li>
			<li>
				<html:link page="/protocols.do?method=showProtocols">
			  		<bean:message key="link.protocols.view"/>
			  	</html:link>
			</li>		
		<%}%>
	</ul> 
</logic:present>
