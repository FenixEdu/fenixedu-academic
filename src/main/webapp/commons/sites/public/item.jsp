<%@page import="pt.ist.fenixWebFramework.Config.CasConfig"%>
<%@page import="pt.ist.fenixWebFramework.FenixWebFramework"%>
<%@page import="pt.ist.fenixWebFramework.Config"%>
<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<logic:present name="item">
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
    <bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>

    <h2>
        <fr:view name="section" property="name" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
   		<app:defineContentPath id="sectionURL" name="section" toScope="request"/>
		<bean:define id="url" name="sectionURL" type="java.lang.String"/>
  		<span class="permalink1">(<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX%><a href="<%= request.getContextPath()  + url %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h2>

 	<logic:notEmpty name="section" property="orderedSubSections">
		<fr:view name="section" property="orderedSubSections" layout="list">
		    <fr:layout>
		        <fr:property name="eachLayout" value="values"/>
		        <fr:property name="eachSchema" value="site.section.name"/>
		    </fr:layout>
		    <fr:destination name="section.view" path="<%= actionName + "?method=section&amp;sectionID=${externalId}&amp;" + context %>"/>
		</fr:view>
    </logic:notEmpty>
   
    <bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>
            
	<h3 class="mtop2 mbottom05">
        <fr:view name="item" property="name"/>
		<app:defineContentPath id="itemURL" name="item" toScope="request"/>
		<bean:define id="url" name="itemURL" type="java.lang.String"/>
  		<span class="permalink1">(<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX%><a href="<%= request.getContextPath()  + url %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h3>

	<logic:equal name="itemAvailable" value="true">
	    <logic:notEmpty name="item" property="body">
	        	<fr:view name="item" property="body">
	        		<fr:layout>
	        			<fr:property name="escaped" value="false" />
	        			<fr:property name="newlineAware" value="false" />
	        		</fr:layout>
	        	</fr:view>
		</logic:notEmpty>
	    
		<logic:notEmpty name="item" property="sortedVisibleFileItems">
	        <fr:view name="item" property="sortedVisibleFileItems">
	            <fr:layout name="list">
	                <fr:property name="eachSchema" value="site.item.file.basic"/>
	                <fr:property name="eachLayout" value="values"/>
	                <fr:property name="style" value="<%= "list-style-image: url(" + request.getContextPath() + "/images/icon_file.gif);" %>"/>
	            </fr:layout>
	        </fr:view>
		</logic:notEmpty>
	</logic:equal>
	<logic:equal name="itemAvailable" value="false">
		<bean:define id="itemId" name="item" property="externalId"/>
		<p>
		<em><bean:message key="message.item.view.mustLogin" bundle="SITE_RESOURCES"/></em>
		<%
			final Config c = FenixWebFramework.getConfig();
			final String serverName = request.getServerName();
			final CasConfig casConfig = c.getCasConfig(serverName);
			if (casConfig != null && casConfig.isCasEnabled()) {
			    final String schema = request.getScheme();
			    final String server = request.getServerName();
			    final int port = request.getServerPort();
		%>
				<a href="<%= "https://barra.ist.utl.pt/login?next=https://id.ist.utl.pt/cas/login?service=" + schema + "://" + server + (port == 80 || port == 443 ? "" : ":" + port) + request.getContextPath() + item.getReversePath() %>">
            		<bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
       			</a>.
		<%
			} else {
		%>
		       <html:link page="<%= String.format("%s?method=itemWithLogin&amp;%s&amp;itemID=%s", actionName, context, itemId) %>">
		            <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
	    	   </html:link>.
       	<%
			}
       	%>

	    </p>
	    <bean:message key="label.permittedGroup" bundle="SITE_RESOURCES"/> <fr:view name="item" property="availabilityPolicy.targetGroup.name"/>
	</logic:equal>
</logic:present>