<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherAdministrationViewerDispatchAction"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.messaging.ExecutionCourseAliasExpandingAction"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
    <bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>

    <fr:view name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>"
             type="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext" layout="site-side-menu">
        <fr:layout>
            <fr:property name="sectionUrl" value="/executionCourse.do?method=section"/>
        </fr:layout>
    </fr:view>
</logic:present>

<ul>
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
