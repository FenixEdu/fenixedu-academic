<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<div id="foot_links">
<bean:define id="institutionUrl"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<a href="<%= institutionUrl %>/html/instituto/index.shtml#cont"><bean:message key="link.contacts" bundle="GLOBAL_RESOURCES"/></a>  |  
<a href="&#109;&#97;&#105;&#108;&#116;&#111;&#58;&#119;&#101;&#98;&#109;&#97;&#115;&#116;&#101;&#114;&#64;&#105;&#115;&#116;&#46;&#117;&#116;&#108;&#46;&#112;&#116;">Webmaster</a></div>
<div id="foot_copy">Copyright <dt:format pattern="yyyy"><dt:currentTime/></dt:format> -
	<bean:message key="institution.name" bundle="GLOBAL_RESOURCES"/></div>
<div class="clear"></div>
