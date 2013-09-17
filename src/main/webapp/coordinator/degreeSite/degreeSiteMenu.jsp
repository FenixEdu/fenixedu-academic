<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<html:xhtml/>

<logic:present name="<%= PresentationConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= PresentationConstants.MASTER_DEGREE %>"/>
	<bean:define id="infoExecutionDegreeID" name="infoExecutionDegree" property="externalId"/>
	<bean:define id="infoDegreeID" name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.externalId"/>
	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlan" property="externalId" scope="request" />
	<br/>
	<ul>
		<li class="navheader">
			<bean:message key="link.coordinator.degreeSite.management"/>
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;info=description&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="label.description" />&nbsp;<bean:message key="label.degree" />
			</html:link>
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;info=acess&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="label.accessRequirements" />
			</html:link>
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;info=professionalStatus&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="label.professionalStatus" />
			</html:link>
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=viewDescriptionCurricularPlan&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="label.description" />&nbsp;<bean:message key="label.curricularPlan" /> 
			</html:link>
		</li>
		<li>
			<html:link page="<%= "/announcementsManagement.do?method=viewBoards&amp;tabularVersion=true&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="label.coordinator.degreeSite.announcements" /> 
			</html:link>
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=sections&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="label.coordinator.degreeSite.sections" /> 
			</html:link>
		</li>
		<br/>
		<li>
			<app:contentLink name="infoExecutionDegree" property="executionDegree.degreeCurricularPlan.degree.site">
				<bean:message key="link.coordinator.degreeSite.viewSite" />
			</app:contentLink>
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=viewHistoric&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="link.coordinator.degreeSite.historic" /></html:link>
		</li>
	</ul>

</logic:present>
