<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="message.alumni.data.reminder.title" bundle="APPLICATION_RESOURCES"/>
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=<%= net.sourceforge.fenixedu._development.PropertiesManager.DEFAULT_CHARSET %>" />
	</head>
	<body>
	
		<div id="container">
			<div id="dotist_id">
				<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			<div id="txt">
				<h1><bean:message key="message.alumni.data.reminder.title" bundle="APPLICATION_RESOURCES"/></h1>
				<bean:message key="message.alumni.data.reminder.text" bundle="APPLICATION_RESOURCES" />	
				<bean:message key="message.alumni.data.reminder.advantages" bundle="APPLICATION_RESOURCES" />   	   	
   				<p><strong><bean:message key="message.alumni.data.reminder.fillInData" bundle="APPLICATION_RESOURCES" /></strong></p>   	
   				<p><bean:message key="message.alumni.data.reminder.moreInformation" bundle="APPLICATION_RESOURCES" />: <a href="http://www.ist.utl.pt/pt/alumni/" target="_blank">http://www.ist.utl.pt/pt/alumni/</a></p>   		
  			</div>
			
			<br />

			<div align="center">
				<table>
					<tr>
						<td>
							<fr:form action="/alumniRedirect.do">
								<html:hidden property="<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME%>" value="/comunicacao/comunicacao"/>
								<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.proceed"/></html:submit>
							</fr:form>
						</td>
					</tr>
				</table>
			</div>
		</div>
		
	</body>
</html:html>
