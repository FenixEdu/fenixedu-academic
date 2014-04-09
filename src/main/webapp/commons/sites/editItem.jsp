<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
	<bean:message key="title.item.edit" bundle="SITE_RESOURCES"/>
</h2>

<logic:messagesPresent message="true">
    <div class="mvert15">
        <span class="error0">
            <html:messages id="error" message="true" bundle="APPLICATION_RESOURCES"> 
                <bean:write name="error"/>
            </html:messages>
        </span>
    </div>
</logic:messagesPresent>


<bean:define id="siteId" name="site" property="externalId"/>
<bean:define id="sectionId" name="item" property="section.externalId"/>

<fr:form action="<%= String.format("%s?method=section&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
    <fr:edit id="edit-item" name="item" type="net.sourceforge.fenixedu.domain.Item" schema="net.sourceforge.fenixedu.domain.ItemEditor">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
    </fr:edit>

    <p class="mtop15">
	    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.editItemButton" property="editItemButton">
	        <bean:message key="button.item.edit.submit" bundle="SITE_RESOURCES"/>
	    </html:submit>
	
	    <html:cancel>
	        <bean:message key="button.item.edit.cancel" bundle="SITE_RESOURCES"/>
	    </html:cancel>
    </p>
</fr:form>
