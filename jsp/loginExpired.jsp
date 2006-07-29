<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:html xhtml="true" locale="true">
	<head>
		<title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - <bean:message key="title.login" bundle="GLOBAL_RESOURCES"/></title>
		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	</head>
	<body>
	<style>

	table.lp_table1 td {
	}
	table.lp_table1 td.lp_leftcol {
	width: 12em;
	}
	table.lp_table2 {
	/*background-color: #dee;*/
	border-top: 1px solid #ccc;
	padding-top: 0.1em;
	}
	table.lp_table2 td.lp_leftcol {
	width: 12em;
	}
	
	</style>
	
		<div id="container">
			<div id="dotist_id">
				<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
			</div>
			<div id="txt">
				<h1>Login</h1>

					<p>
						<bean:message key="message.expired.password"/>
					</p>
					
					<div style="margin: 0 4em; padding: 0.5em; background-color: #f5f5f5; border: 1px solid #eee;">		
						<strong><html:link href="https://ciist.ist.utl.pt/normas/autenticacao.php" target="_blank"><bean:message key="message.requirements"/>:</html:link></strong>
						<ul>
							<li><bean:message key="message.pass.size" /></li>
							<li><bean:message key="message.pass.classes" /></li>
							<li><bean:message key="message.pass.reuse" /></li>
							<li><bean:message key="message.pass.weak" /></li>
						</ul>
					</div>	
			</div>
			<table align="center" border="0">
				<tr>
					<td><span class="error"><!-- Error messages go here --><html:errors /></span></td>
				</tr>
			</table>
			<html:form action="/loginExpired" focus="username" >
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changePass"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.fromCAS" property="fromCAS"/>
				<table align="center" border="0" class="lp_table1">
					<tr>
						<td class="lp_leftcol"><bean:message key="label.username" bundle="GLOBAL_RESOURCES"/>:</td>
						<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" readonly="true"/></td>
					</tr>
					<tr>
						<td class="lp_leftcol"><bean:message key="label.password" bundle="GLOBAL_RESOURCES"/>:</td>
						<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.password" property="password" redisplay="false"/></td>
					</tr>
			   </table>					
				<div style="margin: 0.5em 0; ">
				<table align="center" border="0" class="lp_table2">
			        <tr>
				        <td class="lp_leftcol"><bean:message key="label.candidate.newPassword"/>:</td>
				        <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.newPassword" property="newPassword"/></td>
			        </tr>

			   
			       <!-- retype new password -->
			       <tr>
			           <td class="lp_leftcol"><bean:message key="label.candidate.reTypePassword"/>:</td>
				       <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.reTypeNewPassword" property="reTypeNewPassword"/></td>
			       </tr>
				</table>
				<br />
				<div class="wrapper">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" styleClass="button" property="ok">
						<bean:message key="button.submit" />
					</html:submit>
					<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="button">
						<bean:message key="button.clean" />
					</html:reset>
				</div>
			</html:form>
			<br />
			<div id="txt">
				<center>
				<p>
					<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES"/>:
					<bean:message key="institution.email.support" bundle="GLOBAL_RESOURCES"/>
				</p>
				</center>
			</div>
		</div>
		<%-- Invalidate session. This is to work with FenixActionServlet --%>
		<% 	try {
				session.invalidate();
			}catch (Exception e){}
		%>

	</body>

</html:html>
