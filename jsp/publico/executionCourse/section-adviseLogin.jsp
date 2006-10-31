<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="section">
    <h2><fr:view name="section" property="name" type="net.sourceforge.fenixedu.util.MultiLanguageString"/></h2>

    <bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>
    <bean:define id="sectionId" name="section" property="idInternal"/>

    <p>
       <em><bean:message key="message.section.view.mustLogin" bundle="SITE_RESOURCES"/></em>
       <html:link page="<%= String.format("/executionCourse.do?method=sectionWithLogin&executionCourseID=%s&sectionID=%s", executionCourseId, sectionId) %>">
            <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
       </html:link>.
    </p>
</logic:present>
