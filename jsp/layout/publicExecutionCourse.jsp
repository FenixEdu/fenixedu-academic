<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:html xhtml="true" locale="true">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>

		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/execution_course.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/exam_map.css" />
		<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/print.css" />
		<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/gesdis-print.css" />
		<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/dotist_print.css" />
		<link rel="stylesheet" type="text/css" media="screen,print" href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" />

		<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/expmenu.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>

		<logic:present name="executionCourse">
			<title><bean:write name="executionCourse" property="nome"/></title>
		</logic:present>
		<logic:notPresent name="executionCourse">
			<title><bean:message bundle="GLOBAL_RESOURCES" key="institution.name"/></title>
		</logic:notPresent>
	</head>

	<body>
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
						<td id="latnav_container" width="155" nowrap="nowrap">
							<div id="latnav">
								<tiles:insert attribute="main_navigation" ignore="true"/>
							</div>
						</td>
						<td width="100%" colspan="3" id="main">
							<tiles:insert attribute="body_header" ignore="true"/>
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