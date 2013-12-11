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
			
		<div class="institutionName"><%=Unit.getInstitutionName() %> </div>

			<html:form action="/login" focus="usernameField" styleId="authenticationForm">
				<table align="center" border="0">

					<tr>
						<td><html:text styleId="usernameField" bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" /></td>
					</tr>
					
					<tr>
						<td colspan="2"><div class="error"><!-- Error messages go here --><html:errors property="username" /></div></td>
					</tr>
					
					<html:hidden property="pendingRequest" value="<%=request.getParameter("pendingRequest")%>" />

					<tr>
						<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.password" property="password" redisplay="false" styleId="password"/></td>
					</tr>

					<tr>
						<td colspan="2"><div class="error"><!-- Error messages go here --><html:errors property="password" /></div></td>
					</tr>
				</table>
				
				<div id="footer">					
					<table>
						<tr>
							<td>
								<div id="support">
									<a href="<bean:message key="institution.email.support" bundle="GLOBAL_RESOURCES" />">
										<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES" />
									</a>
								</div>
							</td>
							<td>
								<div id="login_button">
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" styleClass="button" property="ok" >
										<bean:message key="title.login" bundle="GLOBAL_RESOURCES" />
									</html:submit>
								</div>
							</td>
		
						</tr>
					</table>
				</div>
			
			</html:form>
			
			<div class="clear_both" />
		</div>
	</div>
	
	<script type="text/javascript">
		document.getElementById("usernameField").setAttribute("placeholder", "<bean:message key="label.username" bundle="GLOBAL_RESOURCES" />");
		document.getElementById("password").setAttribute("placeholder", "<bean:message key="label.password" bundle="GLOBAL_RESOURCES" />");
	</script>
</body>
</html:html>
