<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="org.fenixedu.bennu.portal.domain.PortalConfiguration"%>
<%@page
	import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:html xhtml="true">

<head>
<title><%=PortalConfiguration.getInstance().getApplicationTitle().getContent()%> - <bean:message
		key="title.login" bundle="GLOBAL_RESOURCES" /></title>

<link href="<%=request.getContextPath()%>/CSS/login.css"
	rel="stylesheet" type="text/css" />

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>

<body>
	<div id="container">
		<div id="logo">
			<img src="<%=request.getContextPath()%>/images/newImage2012/logo.png" />
		</div>
		<div id="content">
			<div class="institutionName"><%=Unit.getInstitutionName()%>
			</div>
				<div id="alert">
					<center>
						<p><span class="error"><!-- Error messages go here --><html:errors property="invalidAuthentication" /></span></p>
	                     <p><span class="error"><!-- Error messages go here --><html:errors property="errors.noAuthorization" /></span></p>
	                     <p><span class="error"><!-- Error messages go here --><html:errors property="error.invalid.session" /></span></p>
	                </center>
				</div>
			<html:form action="/login">
			
			<div class="inputContainer">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" styleId="username" />		
				<html:password bundle="HTMLALT_RESOURCES" altKey="password.password" property="password" redisplay="false" styleId="password" /></td>
			</div>
			
				<div id="footer">
								<div id="login_button">
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" styleClass="button" property="ok">
										&#10003;
									</html:submit>
								</div>
				</div>

			</html:form>

			<div class="clear_both" />
		</div>
	</div>

	<script type="text/javascript">
		document.getElementById("username").setAttribute("placeholder",	"<bean:message key="label.username" bundle="GLOBAL_RESOURCES" />");
		document.getElementById("password").setAttribute("placeholder",	"<bean:message key="label.password" bundle="GLOBAL_RESOURCES" />");
	</script>
</body>

</html:html>
