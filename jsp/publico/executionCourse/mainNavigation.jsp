<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherAdministrationViewerDispatchAction"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.messaging.ExecutionCourseAliasExpandingAction"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>

<fr:view name="executionCourse" property="site" type="net.sourceforge.fenixedu.domain.Site" layout="side-menu">
    <fr:layout>
        <fr:property name="sectionUrl" value="/executionCourse.do?method=section"/>
        <fr:property name="contextParam" value="executionCourseID"/>
    </fr:layout>
</fr:view>

<ul>
    <li>
        <html:link page="/executionCourse.do?method=rss" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal" styleClass="rss">
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
