<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="siteId" name="site" property="idInternal"/>

<h2>
	<bean:message key="label.section"/>
	<fr:view name="section" property="name" />
</h2>

<logic:notEmpty name="section" property="associatedSections">
	<p class="mtop15">
	    <span class="warning0">
	        <bean:message key="message.section.subSection.count" bundle="SITE_RESOURCES" 
	                      arg0="<%= String.valueOf(section.getAssociatedSectionsCount()) %>"/>
	    </span>
    </p>
</logic:notEmpty>

<logic:notEmpty name="section" property="associatedItems">
	<p class="mtop15">
	    <span class="warning0">
	        <bean:message key="message.section.items.count" bundle="SITE_RESOURCES"
	                      arg0="<%= String.valueOf(section.getAssociatedItemsCount()) %>"/>
	    </span>
    </p>
</logic:notEmpty>

<fr:form action="<%= String.format("%s?method=confirmSectionDelete&%s&sectionID=%s", actionName, context, section.getIdInternal()) %>">
    <p class="mtop15">
        <bean:message key="message.section.delete.confirm" bundle="SITE_RESOURCES"/>
    </p>
    
    <p class="mtop1">
	    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
	        <bean:message key="button.confirm" bundle="SITE_RESOURCES"/>
	    </html:submit>
	    
	    <html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" property="cancel">
	        <bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
	    </html:cancel>
    </p>
</fr:form>
