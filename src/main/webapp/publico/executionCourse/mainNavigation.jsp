<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherAdministrationViewerDispatchAction"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.messaging.ExecutionCourseAliasExpandingAction"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<bean:define id="executionCourseId" name="executionCourse" property="externalId"/>

<fr:view name="executionCourse" property="site" type="net.sourceforge.fenixedu.domain.Site" layout="side-menu"/>

<ul>
    <li>
        <html:link page="/executionCourse.do?method=rss" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId" styleClass="rss">
            <bean:message  key="link.rss"/>
        </html:link>
    </li>
</ul>

<logic:equal name="executionCourse" property="site.dynamicMailDistribution" value="true">
	<%
		StringBuilder buffer = new StringBuilder();
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
