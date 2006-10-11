<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherAdministrationViewerDispatchAction"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.messaging.ExecutionCourseAliasExpandingAction"%>

<ul>
	<li>
		<html:link page="/executionCourse.do?method=firstPage"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.inicialPage"/>
		</html:link>
	</li>
	<li>
		<html:link page="/announcementManagement.do?method=start"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.announcements"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=lessonPlannings"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.lessonPlannings"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=summaries"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.summaries"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=objectives"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.objectives"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=program"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.program"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=evaluationMethod"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.evaluationMethod"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=bibliographicReference"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.bibliography"/>
		</html:link>
	</li>	
	<li>
		<html:link page="/executionCourse.do?method=schedule"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="label.schedule"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=shifts"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.shifts"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=evaluations"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.evaluations"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=groupings"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.groupings"/>
		</html:link>
	</li>
	<logic:notEmpty name="executionCourse" property="site.associatedSections">
		<bean:define id="sections" toScope="request" name="executionCourse" property="site.orderedTopLevelSections"/>
		<jsp:include page="sections.jsp"/>
	</logic:notEmpty>
	<li>
		<bean:define id="imageURL" type="java.lang.String">
			background: url(<%= request.getContextPath() %>/images/rss_ico.png) 10px 3px no-repeat; padding-left: 20px;
		</bean:define>
		<html:link page="/executionCourse.do?method=rss" style="<%=imageURL%>"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.rss"/>
		</html:link>
	</li>
</ul>

<logic:equal name="executionCourse" property="site.dynamicMailDistribution" value="true">
	<%
		StringBuffer buffer = new StringBuffer();
		buffer.append(ExecutionCourseAliasExpandingAction.emailAddressPrefix);
		buffer.append(request.getParameter("executionCourseID")).append("@");
		buffer.append(TeacherAdministrationViewerDispatchAction.mailingListDomainConfiguration());
	%>
	<bean:define id="advisoryText">
		<bean:message  key="send.email.dynamicMailDistribution.link" bundle="PUBLIC_DEGREE_INFORMATION"/>
	</bean:define>
	<div class="email"><p>
		<html:link href="<%="mailto:" +buffer.toString() %>" titleKey="send.email.dynamicMailDistribution.title" bundle="PUBLIC_DEGREE_INFORMATION">
			<bean:message key="send.email.dynamicMailDistribution.link" bundle="PUBLIC_DEGREE_INFORMATION"/>
		</html:link>
	</p></div>
</logic:equal>

<logic:notEqual name="executionCourse" property="site.dynamicMailDistribution" value="true">
	<logic:notEmpty name="executionCourse" property="site.mail" >	
		<bean:define id="siteMail" name="executionCourse" property="site.mail" />
		<div class="email"><p>
		<html:link href="<%= "mailto:" + pageContext.findAttribute("siteMail") %>" titleKey="send.email.singleMail.title" bundle="PUBLIC_DEGREE_INFORMATION">
			<bean:message key="send.email.dynamicMailDistribution.link" bundle="PUBLIC_DEGREE_INFORMATION"/>
		</html:link>
		</p></div>
	</logic:notEmpty>
</logic:notEqual>
