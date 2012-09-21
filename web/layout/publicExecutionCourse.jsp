<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="net.sourceforge.fenixedu._development.PropertiesManager"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:html xhtml="true">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<logic:present name="dont-cache-pages-in-search-engines">
			<logic:equal name="dont-cache-pages-in-search-engines" value="true">
				<meta name="ROBOTS" content="NOINDEX, NOFOLLOW"/>
			</logic:equal>
		</logic:present>

		<%-- <link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" /> --%>
		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/execution_course.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/exam_map.css" />
		<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/iststyle_print.css" />
		<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/gesdis-print.css" />
		<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/print.css" />
		<link rel="stylesheet" type="text/css" media="screen,print" href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/istPublicPagesStyles.css"/>

		<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/expmenu.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
		<jsp:include page="/includeMathJax.jsp" />

		<tiles:insert attribute="page-context" ignore="true"/>
		<tiles:insert attribute="rss" ignore="true" />
		<tiles:insert attribute="keywords" ignore="true" />

		<logic:present name="executionCourse">
			<title><bean:write name="executionCourse" property="nome"/></title>
		</logic:present>
		<logic:notPresent name="executionCourse">
			<title><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%></title>
		</logic:notPresent>
		
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
	<% if (PropertiesManager.useBarraAsAuthenticationBroker()) { %>
	<script id="ist-bar" data-login="https://fenix.ist.utl.pt/loginPage.jsp" data-fluid="true" <% if(AccessControl.getUserView() == null) {%> data-use-offline="true" <%} %> data-next-param="service" src="https://barra.ist.utl.pt/site_media/static/js/barra.js"></script>
	<% } %>
	<jsp:include page="deployWarning.jsp" flush="true"/>
		<div id="header">
			<tiles:insert attribute="symbols_row" ignore="true" />
		</div>

		<div id="perfnav">
			<tiles:insert attribute="profile_navigation" ignore="true" />
		</div>

		<div id="holder">
			<logic:present name="executionCourse">
				<table id="bigtable" width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td id="latnav_container" width="155">
							<div id="latnav">
								<tiles:insert attribute="main_navigation" ignore="true"/>
							</div>
						</td>
						<td width="100%" colspan="3" id="main">
							<tiles:insert attribute="body_header" ignore="true"/>
                            <tiles:insert attribute="body-context" ignore="true"/>
							<tiles:insert attribute="body" ignore="true"/>
						</td>
					</tr>
				</table>
			</logic:present>
			
			
			<logic:notPresent name="executionCourse">
				<bean:message bundle="GLOBAL_RESOURCES" key="error.message.resource.not.found"/>
			</logic:notPresent>
			
		</div>

		<div id="footer">
			<tiles:insert attribute="footer" ignore="true"/>
		</div>
<script type="text/javascript">
	hideButtons()
</script>
	</body>

</html:html>