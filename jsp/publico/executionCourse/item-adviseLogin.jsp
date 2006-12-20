<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.SectionProcessor" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ItemProcessor" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="section">
    <bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>

    <h2>
        <fr:view name="section" property="name" type="net.sourceforge.fenixedu.util.MultiLanguageString"/>
		<span class="permalink1">(<a href="<%= request.getContextPath() + SectionProcessor.getSectionAbsolutePath(executionCourse, section) %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h2>

 	<logic:notEmpty name="section" property="orderedSubSections">
		<fr:view name="section" property="orderedSubSections" layout="list">
		    <fr:layout>
		        <fr:property name="eachLayout" value="values"/>
		        <fr:property name="eachSchema" value="site.section.name"/>
		    </fr:layout>
		    <fr:destination name="section.view" path="<%= "/executionCourse.do?method=section&amp;sectionID=${idInternal}&amp;executionCourseID=" + executionCourse.getIdInternal() %>"/>
		</fr:view>
    </logic:notEmpty>
    
    <bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>
            
	<h3 class="mtop2">
        <a name="<%= "item" + item.getIdInternal() %>" />
        <fr:view name="item" property="name"/>
   		<span class="permalink1">(<a href="<%= request.getContextPath() + ItemProcessor.getItemAbsolutePath(executionCourse, item) %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h3>

    <p>
        <em><bean:message key="message.item.view.mustLogin" bundle="SITE_RESOURCES"/></em>
        <html:link page="<%= String.format("/executionCourse.do?method=itemWithLogin&amp;executionCourseID=%s&amp;itemID=%s", executionCourse.getIdInternal(), item.getIdInternal()) %>">
            <bean:message key="link.item.view.login" bundle="SITE_RESOURCES"/>
       </html:link>.
    </p>
</logic:present>