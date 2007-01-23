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

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="sectionId" name="section" property="idInternal"/>

<jsp:include page="/commons/sites/siteQuota.jsp"/>

<h2>
	<bean:message key="label.section"/>
	<fr:view name="section" property="name" />
</h2>

<div class="mvert1">
    <html:link page="<%= String.format("%s?method=sections&amp;%s", actionName, context) %>">
        <bean:message key="link.breadCrumbs.top" bundle="SITE_RESOURCES"/>
    </html:link> &gt;
    
    <logic:iterate id="crumb" name="sectionBreadCrumbs">
        <html:link page="<%= String.format("%s?method=section&amp;%s", actionName, context) %>" paramId="sectionID" paramName="crumb" paramProperty="idInternal">
            <fr:view name="crumb" property="name"/>
        </html:link> &gt;
    </logic:iterate>
    
    <fr:view name="section" property="name"/>
</div>

<div class="infoop2">
<p>
    <span style="color: #888;">
        <bean:message key="label.section.availableFor" bundle="SITE_RESOURCES"/>:
        <fr:view name="section" property="permittedGroup" layout="null-as-label" type="net.sourceforge.fenixedu.domain.accessControl.Group">
            <fr:layout>
                <fr:property name="label" value="<%= String.format("label.%s", net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup.class.getName()) %>"/>
                <fr:property name="key" value="true"/>
                <fr:property name="bundle" value="SITE_RESOURCES"/>
                <fr:property name="subLayout" value="values"/>
                <fr:property name="subSchema" value="permittedGroup.class.text"/>
            </fr:layout>
        </fr:view>    
    </span>
</p>
</div>

<p>
	<span>
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<html:link page="<%= String.format("%s?method=editFunctionalitySection&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
			<bean:message key="button.editSection"/>
		</html:link>
	</span>

    <bean:define id="deleteSectionId" value="<%= "deleteSectionForm" + sectionId %>"/>

    <span class="switchNoneStyle">
        	<span class="pleft1">	
        		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
        		<html:link page="<%= String.format("%s?method=deleteSection&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
        			<bean:message key="button.deleteSection"/>
        		</html:link>
        	</span>
    </span>

    <span class="switchInline">
        <span class="pleft1">
            <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
            <html:link href="<%= String.format("javascript:showElement('%s');", deleteSectionId) %>">
                <bean:message key="button.deleteSection"/>
            </html:link>
        </span>
    </span>
</p>

<p class="mvert1">
    <span class="error">
        <html:errors property="section" bundle="SITE_RESOURCES"/>
    </span>
</p>

<div id="<%= deleteSectionId %>" class="dnone mvert05">
    <fr:form action="<%= String.format("%s?method=confirmSectionDelete&amp;%s&amp;sectionID=%s&amp;confirm=true", actionName, context, sectionId) %>">
        <p class="width550px">
        <span class="warning0 mright075">
            <logic:equal name="section" property="deletable" value="true">
                <bean:message key="message.functionalitySection.delete.confirm" bundle="SITE_RESOURCES"/>
            </logic:equal>
            <logic:notEqual name="section" property="deletable" value="true">
                <bean:message key="message.functionalitySection.delete.notAvailable" bundle="SITE_RESOURCES"/>
            </logic:notEqual>
        </span>                
        
        <logic:equal name="section" property="deletable" value="true">
            <html:submit>
                <bean:message key="button.confirm" bundle="SITE_RESOURCES"/>
            </html:submit>
        </logic:equal>
        <html:button bundle="HTMLALT_RESOURCES" altKey="button.hide" property="hide" onclick="<%= String.format("javascript:hideElement('%s');", deleteSectionId) %>">
            <bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
        </html:button>
        </p>
    </fr:form>
</div>

<fr:view name="section" layout="tabular" schema="site.functionalitySection.simpleDetails">
    <fr:layout>
        <fr:property name="classes" value="tstyle5 thlight thright"/>
    </fr:layout>
    
    <fr:destination name="functionalitySection.view" module="/manager/functionalities" path="/functionality/view.do?functionality=${functionality.idInternal}"/>
</fr:view>

<!-- Change item delete operation if possible -->
<script type="text/javascript">
    switchGlobal();
</script>