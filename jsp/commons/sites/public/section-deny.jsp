<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="section">
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
    
    <h2>
        <fr:view name="section" property="name" type="net.sourceforge.fenixedu.util.MultiLanguageString"/>
        <span class="permalink1">(<app:contentLink name="section"><bean:message key="label.link" bundle="SITE_RESOURCES"/></app:contentLink>)</span>
    </h2>

    <p>
       <em><bean:message key="message.section.view.notAllowed" bundle="SITE_RESOURCES"/></em>
    </p>
</logic:present>
