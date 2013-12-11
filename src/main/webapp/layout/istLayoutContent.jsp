<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>
<%@page import="pt.ist.bennu.core.security.Authenticate"%>
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

</head>

<body>
<% if (FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
<script id="ist-bar" data-login="https://fenix.ist.utl.pt/loginPage.jsp" data-fluid="true" <% if(Authenticate.getUser() == null) {%> data-use-offline="true" <%} %> data-next-param="service" src="https://barra.ist.utl.pt/site_media/static/js/barra.js"></script>
<% } %>
<jsp:include page="deployWarning.jsp" flush="true"/>
<tiles:insert attribute="page-context" ignore="true"/>

<!-- BEGIN BROWSER UPGRADE MESSAGE -->
<div class="browser_upgrade">
  <p><strong>Aviso:</strong>
  Se est&aacute; a ler esta mensagem, provavelmente, o browser que utiliza n&atilde;o &eacute; compat&iacute;vel
  com os &quot;standards&quot; recomendados pela <a href="http://www.w3.org">W3C</a>.
  Sugerimos vivamente que actualize o seu browser para ter uma melhor experi&ecirc;ncia
  de utiliza&ccedil;&atilde;o deste &quot;website&quot;.
  Mais informa&ccedil;&otilde;es em <a href="http://www.webstandards.org/upgrade/">webstandards.org</a>.</p>
  <p><strong>Warning:</strong> If you are reading this message, probably, your
    browser is not compliant with the standards recommended by the <a href="http://www.w3.org">W3C</a>. We suggest
    that you upgrade your browser to enjoy a better user experience of this website.
    More informations on <a href="http://www.webstandards.org/upgrade/">webstandards.org</a>.</p>
</div>
<!-- END BROWSER UPGRADE MESSAGE -->

<!-- SYMBOLSROW -->
<h1>IST_LAYOUT_CONTENT</h1>
<div id="header">
	<tiles:insert attribute="symbols_row" ignore="true"/>
</div>

<!-- PROFILE NAVIGATION -->
<div id="perfnav">
	<tiles:insert attribute="profile_navigation" ignore="true"/>
</div>

<div id="holder">
<table id="bigtable" width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>

<td id="latnav_container">
<!--MAIN NAVIGATION -->   
<div id="latnav">
	<tiles:insert attribute="main_navigation" ignore="true"/>
</div>
</td>

<!-- DEGREE SITE -->
<td width="100%" colspan="3" id="main">

<tiles:useAttribute id="hideLanguage" name="hideLanguage" ignore="true"/>
<logic:notPresent name="hideLanguage">
	<jsp:include page="../i18n.jsp"/>
</logic:notPresent>

<tiles:insert attribute="body-context" ignore="true"/>
<tiles:insert attribute="body" ignore="true"/>
<tiles:getAsString name="body-inline" ignore="true"/>

</td>

</tr>
</table>
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
