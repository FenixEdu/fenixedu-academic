<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>

<em><bean:message key="message.evaluationElements" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="link.projects" bundle="APPLICATION_RESOURCES"/></h2>
<bean:define name="coursesProjects" id="coursesProjectsMap" type="java.util.Map" scope="request"/>

<logic:iterate id="entry" name="coursesProjectsMap">
	<bean:define id="course" name="entry" property="key"/>
	<bean:define id="courseID" name="course" property="idInternal"/>
	<bean:define id="projects" name="entry" property="value"/>
	<div class="mvert25">
		<h3>
			<bean:write name="course" property="name"/>
			<span style="color: gray;">
				<bean:write name="course" property="degreePresentationString"/>
			</span>
		</h3>
		<logic:iterate id="project" name="projects">
			<bean:define id="projectOID" name="project" property="externalId"/>
			<div class='mtop15 mbottom2'>
				<b><bean:write name="project" property="name"/></b>,
				<span class='color888'>
				<bean:message key="label.net.sourceforge.fenixedu.domain.Project.projectBeginDateTime" bundle="APPLICATION_RESOURCES"/>
			</span>
			<bean:write name="project" property="begin" format="dd/MM/yyyy"/>,
			<span class='color888'>
				<bean:message key="label.net.sourceforge.fenixedu.domain.Project.projectEndDateTime" bundle="APPLICATION_RESOURCES"/>
			</span>
			<bean:write name="project" property="end" format="dd/MM/yyyy"/>
			<logic:notEmpty name="project" property="description">
				<p>
					<bean:message key="label.description" bundle="APPLICATION_RESOURCES"/>: 
					<bean:write name="project" property="description"/>
				</p>
			</logic:notEmpty>
			<p>
			<bean:define id="viewProjectURL">
				<%= request.getContextPath() + "/departmentMember/departmentFunctionalities.do?method=viewLastProjectSubmissionForEachGroup&amp;executionCourseID=" + courseID + "&amp;projectOID=" + projectOID %>
			</bean:define>
			<html:link href="<%=viewProjectURL%>"> 
				<bean:message key="link.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissions" bundle="APPLICATION_RESOURCES"/>
			</html:link>
			</p>
			</div>		
		</logic:iterate>
	</div>
</logic:iterate>