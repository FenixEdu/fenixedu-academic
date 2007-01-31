<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:xhtml/>

<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
	<bean:define id="infoExecutionDegreeID" name="infoExecutionDegree" property="idInternal"/>
	<bean:define id="infoDegreeID" name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.idInternal"/>
	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlan" property="idInternal" scope="request" />
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
			<html:link href="<%= request.getContextPath()+"/publico/showDegreeSite.do?method=showDescription&amp;degreeID=" + infoDegreeID.toString() %>" target="_blank">
			    <bean:message key="link.coordinator.degreeSite.viewSite" /></html:link>
		</li>
		<li>
			<html:link page="<%= "/degreeSiteManagement.do?method=viewHistoric&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="link.coordinator.degreeSite.historic" /></html:link>
		</li>
	</ul>

</logic:present>
