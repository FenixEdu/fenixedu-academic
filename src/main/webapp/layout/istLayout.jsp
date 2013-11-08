<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="net.sourceforge.fenixedu.util.StringUtils"%>
<%@page import="net.sourceforge.fenixedu._development.PropertiesManager"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="<bean:message key="meta.keywords" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="GLOBAL_RESOURCES"/>" />
<meta name="description" content="<bean:message key="meta.description" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="GLOBAL_RESOURCES"/>" />
<logic:present name="dont-cache-pages-in-search-engines">
	<logic:equal name="dont-cache-pages-in-search-engines" value="true">
		<meta name="ROBOTS" content="NOINDEX, NOFOLLOW"/>
	</logic:equal>
</logic:present>

<%-- <link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css"/> --%>
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/execution_course.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/exam_map.css" />
<link rel="stylesheet" type="text/css" media="screen,print" href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/general.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/istPublicPagesStyles.css"/>
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/iststyle_print.css"/>
<tiles:insert attribute="css-headers" ignore="true" />

<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/expmenu.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
<script src="<%= request.getContextPath() %>/javaScript/jquery/jquery.js" type="text/javascript" >
</script>
<script src="<%= request.getContextPath() %>/javaScript/jquery/jquery-ui.js" type="text/javascript">
</script>
<jsp:include page="/includeMathJax.jsp" />

<tiles:insert attribute="page-context" ignore="true"/>
<tiles:insert attribute="rss" ignore="true" />
<tiles:insert attribute="keywords" ignore="true" />

<title>
	<tiles:insert name="title" ignore="true" />
	<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>
</title> <%-- TITLE --%>

<!-- Link -->
<link rel="shortcut icon" href="<%= request.getContextPath() %>/images/newImage2012/favicon.png" type="image/x-icon" />

<!-- For third-generation iPad with high-resolution Retina display: -->
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="<%= request.getContextPath() %>/images/newImage2012/apple-touch-icon-144x144-precomposed.png" />

<!-- For iPhone with high-resolution Retina display: -->
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="<%= request.getContextPath() %>/images/newImage2012/apple-touch-icon-114x114-precomposed.png" />

<!-- For first- and second-generation iPad: -->
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="<%= request.getContextPath() %>/images/newImage2012/apple-touch-icon-72x72-precomposed.png" />

<!-- For non-Retina iPhone, iPod Touch, and Android 2.1+ devices: -->
<link rel="apple-touch-icon-precomposed" href="<%= request.getContextPath() %>/images/newImage2012/apple-touch-icon-precomposed.png" />

<% if (PropertiesManager.hasGoogleAnalytics()) { %>
       <script type="text/javascript">
       		<%= PropertiesManager.getProperty("google.analytics.snippet") %>
       </script>
<% } %>

</head>

<body>
<% if (PropertiesManager.useBarraAsAuthenticationBroker()) { %>
<script id="ist-bar" data-logout="https://fenix.ist.utl.pt/logoff.do" data-login="https://fenix.ist.utl.pt/loginPage.jsp" data-fluid="true" data-lang="<%= Language.getLocale().getLanguage() %>" <% if(AccessControl.getUserView() == null) {%> data-use-offline="true" <%} %> data-next-param="service" src="https://barra.ist.utl.pt/site_media/static/js/barra.js"></script>
<% } %>


<jsp:include page="deployWarning.jsp" flush="true"/>
<jsp:include page="devMode.jsp" flush="true"/>

<tiles:insert attribute="page-context" ignore="true"/>
<jsp:include page="browserUpgradeMessage.jsp" flush="true"/>

<!-- SYMBOLSROW -->
<!-- h1>IST_LAYOUT &emsp; + &emsp; IST_LAYOUT_CONTENT &emsp; + &emsp; PUBLIC_EXECUTION_COURSE &emsp; + &emsp; PUBLIC_GES_DIS_LAYOUT_2COL &emsp; + &emsp; UNIT_SITE_LAYOUT</h1-->
<div id="header">
	<tiles:insert attribute="symbols_row" ignore="true"/>
</div>

<!-- PROFILE NAVIGATION -->
<div id="perfnav">
	<tiles:insert attribute="profile_navigation" ignore="true"/>
</div>

<div id="holder">
	<logic:present name="courseSite">
		<logic:notPresent name="executionCourse">
			<bean:message bundle="GLOBAL_RESOURCES" key="error.message.resource.not.found"/>
		</logic:notPresent>
	</logic:present>
	
	<logic:notPresent name="courseSite">
		<table id="bigtable" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
		
		<td rowspan="3" id="latnav_container">
			<!--MAIN NAVIGATION -->   
			<div id="latnav">
				<tiles:insert attribute="main_navigation" ignore="true"/>
				<tiles:insert attribute="lateral_nav" ignore="true" />
			</div>
		</td>
		
		<!-- DEGREE SITE -->
		<tiles:useAttribute id="unitSite" name="unitSite" ignore="true"/>
		<logic:notPresent name="unitSite">
			<td width="100%" colspan="3" id="main">
		</logic:notPresent>
		<logic:present name="unitSite">
			<td width="100%" colspan="3" id="main" class="usitemain">
		</logic:present>
			<tiles:useAttribute id="hideLanguage" name="hideLanguage" ignore="true"/>
			<logic:notPresent name="hideLanguage">
				<jsp:include page="../i18n.jsp"/>
			</logic:notPresent>
			
			<tiles:insert attribute="body_header" ignore="true"/>
			<tiles:insert attribute="body-context" ignore="true"/>
			<tiles:insert attribute="banner" ignore="true"/>
			<tiles:insert attribute="executionCourseName" ignore="true" />
			<tiles:insert attribute="executionCoursePeriod" ignore="true" />
			<tiles:insert attribute="body" ignore="true"/>
			<tiles:getAsString name="body-inline" ignore="true"/>
		</td>
		
		</tr>
		</table>
	</logic:notPresent>
	
</div>
<!-- FOOTER --> 
<div id="footer">
<tiles:insert attribute="footer" ignore="true"/>
</div>

<script type="text/javascript">
	hideButtons()
</script>

<tiles:insert attribute="analytics" ignore="true"/>

</body>
</html:html>
