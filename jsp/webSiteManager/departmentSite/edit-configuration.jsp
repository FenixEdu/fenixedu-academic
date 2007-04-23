<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<jsp:include page="../commons/edit-configuration.jsp"/>

<div class="mtop15 mbottom1">
    <strong><bean:message key="title.site.configuration.sections" bundle="WEBSITEMANAGER_RESOURCES"/>:</strong>
</div>

<logic:present name="optionalSectionsChanged">
    <p>
        <span class="success0">
            <bean:message key="message.site.configuration.visualization.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<bean:define id="oid" name="site" property="idInternal"/>

<fr:form action="<%= "/manageDepartmentSite.do?method=changeOptionalSections&amp;oid=" + oid %>">
    <fr:edit id="optionalSections" name="site" schema="departmentSite.optional.sections">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear"/>
        </fr:layout>
    </fr:edit>
    
    <html:submit>
        <bean:message key="button.save"/>
    </html:submit>
</fr:form>
