<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
    <bean:message key="title.site.manage.functions" bundle="SITE_RESOURCES"/>
</h2>

<ul>
	<li>
		<html:link page="<%= String.format("%s?method=manageExistingFunctions&amp;%s", actionName, context) %>">
			<bean:message key="link.site.manage.functions" bundle="SITE_RESOURCES"/>
		</html:link>
	</li>
</ul>

<logic:notEmpty name="people">
	<fr:view name="people" schema="site.functions.person.table">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 tdtop thleft mtop2"/>
	
			<fr:property name="link(manage)" value="<%= String.format("%s?method=managePersonFunctions&amp;%s", actionName, context) %>"/>
			<fr:property name="param(manage)" value="person.idInternal/personID"/>
			<fr:property name="key(manage)" value="site.functions.person.edit"/>
			<fr:property name="bundle(manage)" value="SITE_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="people">
	<em><bean:message key="message.site.manage.functions.people.empty" bundle="SITE_RESOURCES"/></em>
</logic:empty>

<div class="mtop2">
	<p class="mbottom05">
	    <strong><bean:message key="title.site.functions.person.add" bundle="SITE_RESOURCES"/></strong>
    </p>
    
    <p class="mvert05">
        <em><bean:message key="message.site.functions.person.add" bundle="SITE_RESOURCES"/></em>
    </p>

	<logic:messagesPresent message="true" property="addPersonError">
	    <html:messages id="message" message="true" property="addPersonError" bundle="SITE_RESOURCES">
	        <p><span class="error0"><bean:write name="message"/></span></p>
	    </html:messages>
	</logic:messagesPresent>
	
    <fr:form action="<%= String.format("%s?method=managePersonFunctions&amp;%s", actionName, context) %>">
        <fr:edit id="addUserBean" name="addUserBean" schema="site.functions.addPerson">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdmiddle thlight thright thmiddle mbottom0"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="<%= String.format("%s?method=selectPersonFunctions&amp;%s", actionName, context) %>"/>
        </fr:edit>

        <html:submit styleClass="mtop05">
            <bean:message key="button.add" bundle="SITE_RESOURCES"/> 
        </html:submit>
    </fr:form>
</div>
