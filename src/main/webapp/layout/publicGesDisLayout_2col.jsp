<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>
<%@page import="pt.ist.bennu.core.security.Authenticate"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>

<html:html xhtml="true">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<%-- <link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" /> --%>
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/iststyle_print.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/execution_course.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/exam_map.css" />
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/gesdis-print.css" />
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/print.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/istPublicPagesStyles.css"/>

<tiles:insert attribute="rss" ignore="true" />

<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" />
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/expmenu.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
<jsp:include page="/includeMathJax.jsp" />

<title>
<tiles:useAttribute name="title" id="titleK" ignore="true" />
<tiles:useAttribute name="bundle" id="bundleT" ignore="true" />
<logic:present name="bundleT">
	<logic:present name="titleK">
		<bean:message name="titleK" bundle="<%= bundleT.toString() %>" /> -
	</logic:present>
	<logic:present name="<%= FilterFunctionalityContext.CONTEXT_KEY %>">
		<bean:define id="contentContext" name="<%= FilterFunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" type="net.sourceforge.fenixedu.domain.contents.Content"/>
		<logic:equal name="contentContext" property="unitSite" value="true">
			<bean:write name="contentContext" property="unit.partyName"/> -
		</logic:equal>
	</logic:present>
</logic:present>
<logic:notPresent name="bundleT">
	<tiles:getAsString name="title" ignore="true"/> -
	<logic:present name="<%= FilterFunctionalityContext.CONTEXT_KEY %>">
		<bean:define id="contentContext" name="<%= FilterFunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" type="net.sourceforge.fenixedu.domain.contents.Content"/>
		<logic:equal name="contentContext" property="unitSite" value="true">
			<bean:write name="contentContext" property="unit.partyName"/> -
		</logic:equal>
	</logic:present>
</logic:notPresent>
<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>
</title>
<%-- TITLE --%>

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

</head>

<body>
<% if (FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
<script id="ist-bar" data-logout="https://fenix.ist.utl.pt/logoff.do" data-login="https://fenix.ist.utl.pt/loginPage.jsp" data-fluid="true" <% if(Authenticate.getUser() == null) {%> data-use-offline="true" <%} %> data-next-param="service" src="https://barra.ist.utl.pt/site_media/static/js/barra.js"></script>
<% } %>
<jsp:include page="deployWarning.jsp" flush="true"/>
<jsp:include page="devMode.jsp" flush="true"/>

<!-- BEGIN BROWSER UPGRADE MESSAGE -->
<div class="browser_upgrade">
<p><strong>Aviso:</strong> Se est&aacute; a ler esta mensagem,
provavelmente, o browser que utiliza n&atilde;o &eacute;
compat&iacute;vel com os &quot;standards&quot; recomendados pela <a
	href="http://www.w3.org">W3C</a>. Sugerimos vivamente que actualize o
seu browser para ter uma melhor experi&ecirc;ncia de
utiliza&ccedil;&atilde;o deste &quot;website&quot;. Mais
informa&ccedil;&otilde;es em <a
	href="http://www.webstandards.org/upgrade/">webstandards.org</a>.</p>
<p><strong>Warning:</strong> If you are reading this message, probably,
your browser is not compliant with the standards recommended by the <a
	href="http://www.w3.org">W3C</a>. We suggest that you upgrade your
browser to enjoy a better user experience of this website. More
informations on <a href="http://www.webstandards.org/upgrade/">webstandards.org</a>.</p>
</div>
<!-- END BROWSER UPGRADE MESSAGE -->

<!-- SYMBOLSROW -->
<h1>PUBLIC_GES_DIS_LAYOUT_2COL</h1>
<div id="header"><tiles:insert attribute="symbols_row" ignore="true" />
</div>

<!-- PROFILE NAVIGATION -->
<div id="perfnav"><tiles:insert attribute="profile_navigation"
	ignore="true" /></div>

<div id="holder">
<table id="bigtable" width="100%" border="0" cellpadding="0"
	cellspacing="0">
	<tr>
		<td id="latnav_container" width="155"><!-- LATERAL NAVIGATION -->
		<div id="latnav"><tiles:insert attribute="lateral_nav" ignore="true" />
		</div>
		</td>

		<!-- DEGREE SITE -->
		<td width="100%" colspan="3" id="main"><tiles:insert
			attribute="executionCourseName" ignore="true" /> <tiles:insert
			attribute="executionCoursePeriod" ignore="true" /> <!-- CONTEXTUAL NAVIGATION -->
		<tiles:insert attribute="body" ignore="true" /></td>

	</tr>
</table>
</div>


<!-- FOOTER -->
<div id="footer"><tiles:insert attribute="footer" ignore="true" /></div>

<script type="text/javascript">
	hideButtons()
</script>

</body>
</html:html>
