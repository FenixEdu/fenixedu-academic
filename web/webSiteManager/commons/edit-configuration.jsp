<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.UnitSite"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2><bean:message key="title.site.configurationOptions" bundle="WEBSITEMANAGER_RESOURCES"/></h2>

<div class="infoop2 mbottom15">
    <p class="mtop0">
        <bean:message key="label.site.configuration" bundle="WEBSITEMANAGER_RESOURCES"/>
       	<a class="switchInline" href="javascript:showElement('site-banners-message');"><bean:message key="link.site.banners.add.expand" bundle="WEBSITEMANAGER_RESOURCES"/>...</a>
    </p>

	<div class="switchNone" id="site-banners-message">
	    <p class="mbottom05">
		    <bean:message key="label.site.configuration.instructions" bundle="WEBSITEMANAGER_RESOURCES"/>
	    </p>
    </div>
</div>


<div class="mtop15 mbottom05">
    <strong><bean:message key="title.site.configuration.i18n" bundle="WEBSITEMANAGER_RESOURCES"/>:</strong>
</div>

<logic:present name="i18nChanged">
    <p class="mbottom05">
        <span class="success0">
            <bean:message key="message.site.configuration.i18n.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<fr:form action="<%= actionName + "?method=updateI18n&amp;" + context %>">
	<fr:edit id="i18n" name="site" schema="custom.unitSite.i18n">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit>
		<bean:message key="button.save"/>
	</html:submit>
</fr:form>

<p class="mtop2 mbottom05">
    <strong><bean:message key="title.site.configuration.visualization" bundle="WEBSITEMANAGER_RESOURCES"/>:</strong>
</p>

<logic:present name="visualizationChanged">
    <p class="mbottom05">
        <span class="success0">
            <bean:message key="message.site.configuration.visualization.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<fr:form action="<%= actionName + "?method=updateConfiguration&amp;" + context %>">
    <fr:edit id="visualization" name="site" schema="custom.unitSite.visualization">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
            <fr:property name="columnClasses" value=",,tdclear"/>
        </fr:layout>
    </fr:edit>
    
    <html:submit>
        <bean:message key="button.save"/>
    </html:submit>
</fr:form>


<p class="mtop2 mbottom05">
    <strong><bean:message key="title.site.configuration.layout" bundle="WEBSITEMANAGER_RESOURCES"/>:</strong>
</p>

<bean:define id="imageBase" value="<%= request.getContextPath() + "/images/site/layouts/" %>"/>

<logic:equal name="site" property="layout" value="BANNER_INTRO">
    <bean:define id="layoutClass1" toScope="request" value="aimageselected"/>
</logic:equal>

<logic:equal name="site" property="layout" value="INTRO_BANNER">
    <bean:define id="layoutClass2" toScope="request" value="aimageselected"/>
</logic:equal>

<logic:equal name="site" property="layout" value="BANNER_INTRO_COLLAPSED">
    <bean:define id="layoutClass3" toScope="request" value="aimageselected"/>
</logic:equal>

<div>
    <html:link page="<%= String.format("%s?method=changeLayout&amp;layout=BANNER_INTRO&amp;%s", actionName, context) %>" 
               styleClass="<%= (request.getAttribute("layoutClass1") == null ? "aimage" : request.getAttribute("layoutClass1")).toString() %>">
        <img src="<%= imageBase + "pos1.gif" %>"/>
    </html:link>
    
    <html:link page="<%= String.format("%s?method=changeLayout&amp;layout=INTRO_BANNER&amp;%s", actionName, context) %>"
               styleClass="<%= (request.getAttribute("layoutClass2") == null ? "aimage" : request.getAttribute("layoutClass2")).toString() %>">
        <img src="<%= imageBase + "pos2.gif" %>"/>
    </html:link>
    
    <html:link page="<%= String.format("%s?method=changeLayout&amp;layout=BANNER_INTRO_COLLAPSED&amp;%s", actionName, context) %>"
               styleClass="<%= (request.getAttribute("layoutClass3") == null ? "aimage" : request.getAttribute("layoutClass3")).toString() %>">
        <img src="<%= imageBase + "pos3.gif" %>"/>
    </html:link>
</div>

<script type="text/javascript">
	switchGlobal();
</script>