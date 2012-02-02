<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.contents.Content"%>
<%@page import="pt.ist.fenixframework.pstm.AbstractDomainObject"%>
<%@page import="net.sourceforge.fenixedu.domain.Site"%>
<%@page import="net.sourceforge.fenixedu.domain.UnitSite"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>


<%@page import="net.sourceforge.fenixedu.domain.Person"%><html:xhtml/>

<ul>
	<li><html:link page="/viewCurriculum.do"> <bean:message bundle="RESEARCHER_RESOURCES" key="link.viewCurriculum"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</html:link> </li>
	<li><html:link page="/publications/search.do?method=prepareSearchPublication"> <bean:message bundle="RESEARCHER_RESOURCES" key="label.search"/> </html:link>
</ul>

<ul style="margin-top: 0.75em;">
	<li><html:link page="/interests/interestsManagement.do?method=prepare"><bean:message bundle="RESEARCHER_RESOURCES" key="link.interestsManagement"/></html:link></li>
	<li><html:link page="/resultPublications/listPublications.do"><bean:message bundle="RESEARCHER_RESOURCES" key="link.Publications"/></html:link></li>
	<li><html:link page="/resultPatents/management.do"><bean:message bundle="RESEARCHER_RESOURCES" key="link.patentsManagement"/></html:link></li>			
	<li><html:link page="/activities/activitiesManagement.do?method=listActivities"><bean:message bundle="RESEARCHER_RESOURCES" key="link.activitiesManagement"/></html:link></li>
	<li><html:link page="/prizes/prizeManagement.do?method=listPrizes"><bean:message bundle="RESEARCHER_RESOURCES" key="label.prizes"/></html:link></li>
    <li><html:link page="/career/careerManagement.do?method=showCareer"><bean:message bundle="RESEARCHER_RESOURCES" key="label.career"/></html:link></li>
	<%--
	<li class="navheader"><bean:message bundle="RESEARCHER_RESOURCES" key="link.participationsTitle"/></li>
	--%>
	<%-- 
	<li><html:link page="/resultPublications/listPublications.do"><bean:message bundle="RESEARCHER_RESOURCES" key="link.Publications"/></html:link></li>
	<li><html:link page="/projects/projectsManagement.do?method=listProjects"><bean:message bundle="RESEARCHER_RESOURCES" key="link.projectsManagement"/></html:link></li>
	--%>
</ul>

<bean:define id="autoEvalProcesses" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.teacherEvaluationProcessFromEvalueeSet" type="java.util.List"/>
<bean:define id="evalProcesses" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.teacherEvaluationProcessFromEvaluator" type="java.util.List"/>
<%
	final Person person = AccessControl.getPerson();
	if (person.isTeacherEvaluationCoordinatorCouncilMember() || !autoEvalProcesses.isEmpty() || !evalProcesses.isEmpty()) {
%>
		<ul style="margin-top: 0.75em;">
			<li class="navheader"><bean:message key="label.teacher.evaluation.title" bundle="RESEARCHER_RESOURCES"/></li>
			<logic:notEmpty name="autoEvalProcesses">
				<li>
					<html:link page="/teacherEvaluation.do?method=viewAutoEvaluation">
						<bean:message key="label.teacher.evaluation.autoevaluation.title" bundle="RESEARCHER_RESOURCES"/>
					</html:link>
				</li>
			</logic:notEmpty>
			<logic:notEmpty name="evalProcesses">
				<li>
					<html:link page="/teacherEvaluation.do?method=viewEvaluees">
						<bean:message key="label.teacher.evaluation.evaluation.title" bundle="RESEARCHER_RESOURCES"/>
					</html:link>
				</li>
			</logic:notEmpty>
<%
			if (person.isTeacherEvaluationCoordinatorCouncilMember()) {
%>
				<li>
					<html:link page="/teacherEvaluation.do?method=viewManagementInterface">
						<bean:message key="label.teacher.evaluation.management.title" bundle="RESEARCHER_RESOURCES"/>
					</html:link>
				</li>
<%
			}
%>
		</ul>
<%
	}
%>

<logic:present role="RESEARCHER">
	<ul style="margin-top: 0.75em;">
		<li class="navheader"><bean:message key="researcher.find.an.expert" bundle="RESEARCHER_RESOURCES"/></li>
		<li><html:link page="/researcherManagement.do?method=prepare"><bean:message key="label.options" bundle="RESEARCHER_RESOURCES"/></html:link></li>
	</ul>
</logic:present>
	
<bean:define id="workingResearchUnits" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.workingResearchUnitsAndParents" type="java.util.List"/>
<logic:notEmpty name="workingResearchUnits">
<ul>
	<li class="navheader"><bean:message key="label.researchUnits" bundle="RESEARCHER_RESOURCES"/></li>
	<logic:iterate id="unitIter" name="workingResearchUnits">
		<bean:define id="unitID" name="unitIter" property="idInternal" type="java.lang.Integer"/>
		<bean:define id="unitExternalId" name="unitIter" property="externalId"/>
		<bean:define id="unitName" name="unitIter" property="name" type="java.lang.String"/>
		<li> 
			<html:link title="<%= unitName %>" page="<%= "/researchUnitFunctionalities.do?method=prepare&unitId=" + unitID %>">
						<bean:write name="unitIter" property="acronym"/>
			</html:link>

			<logic:present name="unit">
				<logic:equal name="unit" property="idInternal" value="<%= unitID.toString() %>">
			<ul>
				<li>
					<html:link page="<%= "/sendEmailToResearchUnitGroups.do?method=prepare&contentContextPath_PATH=/comunicacao/comunicacao&unitExternalId=" + unitExternalId %>">
						<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
					 </html:link>
			    </li>	
		    <logic:equal name="unitIter" property="currentUserAbleToInsertOthersPublications" value="true">
				<li>
					<html:link page="<%= "/researchUnitFunctionalities.do?method=preparePublications&unitId=" + unitID %>">
							<bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES"/>
					</html:link>
				</li>
			</logic:equal>
			<logic:equal name="unitIter" property="currentUserAbleToDefineGroups" value="true">
				<li>
					<html:link page="<%= "/researchUnitFunctionalities.do?method=configureGroups&unitId=" + unitID %>"><bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
					</html:link>
				</li>
			</logic:equal>
				<li><html:link page="<%= "/researchUnitFunctionalities.do?method=manageFiles&unitId=" + unitID %>"><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/></html:link></li>						
		</ul>
			</logic:equal>
		</logic:present>
	</logic:iterate>
</ul>
</logic:notEmpty>	

<logic:present role="RESEARCHER">
	<ul>
		<li class="navheader">
			<bean:message key="link.manage.finalWork" bundle="APPLICATION_RESOURCES"/>
		</li>
		<li>
			<html:link page="/finalWorkManagement.do?method=chooseDegree">
				<bean:message key="link.manage.finalWork.candidacies" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/thesisDocumentConfirmation.do?method=showThesisList">
				<bean:message key="link.manage.thesis.document.confirmation" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</li>
	</ul>
</logic:present>