<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="net.sourceforge.fenixedu.domain.Instalation"%>
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:html xhtml="true">

<head>
	<title>
		<%=Instalation.getInstance().getInstalationName() %> - <bean:message key="title.login" bundle="GLOBAL_RESOURCES" />
	</title>

	<link href="<%=request.getContextPath()%>/CSS/login.css" rel="stylesheet" type="text/css" />
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>

<body>
	<div id="container">
		
		<div id="header">
			<div id="dotist_id_container">
				<img id="logo_image"
					alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
					src="<bean:message key="fenix.logo.location" bundle="GLOBAL_RESOURCES" arg0="<%=request.getContextPath()%>"/>" />
			</div>
		</div>
		
		<div id="content">
			<h1>
				<bean:message key="title.login" bundle="GLOBAL_RESOURCES" />
			</h1>
			
<!-- 			Version with Table -->
			<html:form action="/login" focus="username">
				<table align="center" border="0">
					<tr>
						<td colspan="2"><span class="error"><!-- Error messages go here --><html:errors property="username" /></span></td>
					</tr>
					<tr>
						<td><span class="label"><bean:message key="label.username" bundle="GLOBAL_RESOURCES" />:</span></td>
						<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" /></td>
					</tr>
					<html:hidden property="pendingRequest" value="<%=request.getParameter("pendingRequest")%>" />
					<tr>
						<td colspan="2"><span class="error"><!-- Error messages go here --><html:errors property="password" /></span></td>
					</tr>
					<tr>
						<td><span class="label"><bean:message key="label.password" bundle="GLOBAL_RESOURCES" />:</span></td>
						<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.password" property="password" redisplay="false" /></td>
					</tr>

				</table>
			</html:form>
<!-- 			END -->

<!-- 			Version with divs -->
<%-- 			<html:form action="/login" focus="username"> --%>
<!-- 				<div style="display: block; width: 45%; margin: 0 auto;"> -->
					
<%-- 					<p><span class="error"><!-- Error messages go here --><html:errors property="username" /></span></p> --%>
					
<!-- 					<div> -->
<%-- 						<p><span class="label"><bean:message key="label.username" bundle="GLOBAL_RESOURCES" /></span></p> --%>
<%-- 						<html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" /> --%>
<!-- 					</div> -->
<%-- 					<html:hidden property="pendingRequest" value="<%=request.getParameter("pendingRequest")%>" /> --%>
<!-- 					<div> -->
<%-- 						<span class="error"><!-- Error messages go here --><p><html:errors property="password" /></p></span> --%>
<!-- 					</div> -->
<!-- 					<div> -->
<%-- 						<p><span class="label"><bean:message key="label.password" bundle="GLOBAL_RESOURCES" /></span></p> --%>
<%-- 						<html:password bundle="HTMLALT_RESOURCES" altKey="password.password" property="password" redisplay="false" /> --%>
<!-- 					</div> -->
<!-- 				</div> -->
<%-- 			</html:form> --%>
			<!-- 			END -->
			
			<div id="footer">					
				<table>
					<tr>
						<td>
							<span id="support">
								<a href="<bean:message key="institution.email.support" bundle="GLOBAL_RESOURCES" />">
									<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES" />
								</a>
							</span>
						</td>
						<td>
							<div id="login_button">
								<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" styleClass="button" property="ok" >
									<bean:message key="button.submit" />
								</html:submit>
							</div>
						</td>
	
					</tr>
				</table>
			</div>
			
			<div class="clear_both" />
		</div>
	</div>
</body>
</html:html>
