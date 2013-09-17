<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="message.gratuity.payments.reminder.title" bundle="APPLICATION_RESOURCES"/>
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<style type="text/css">
			.warning1 {
				background-color: #fbf2cc;
				color: #804500;
				border-top: 2px solid #e6d099;
				border-bottom: 2px solid #e6d099;
				padding: 4px 5px 5px 5px;
			}			
		</style>
	</head>
	<body>
	
		<div id="container">
			<div id="dotist_id">
				<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>

			<div id="txt">
				<h1><bean:message key="message.gratuity.payments.reminder.title" bundle="APPLICATION_RESOURCES"/></h1>
				<logic:equal name="remnantGratuity" value="true">
					<bean:define id="remainingPaymentEndDate" name="remainingPaymentEndDate" type="java.lang.String" />
					<bean:define id="remainingPaymentDebt" name="remainingPaymentDebt" type="java.lang.String" />
					<bean:define id="remainingPaymentCode" name="remainingPaymentCode" type="java.lang.String" />
					
					<div class="warning1" style="margin: 20px; padding: 20px">
						<bean:message key="message.delayed.debt" bundle="APPLICATION_RESOURCES" 
							arg0="<%= remainingPaymentEndDate %>" arg1="<%= remainingPaymentDebt %>" arg3="<%= remainingPaymentCode %>" />
					</div>
				</logic:equal>
				
				<bean:message key="message.gratuity.payments.reminder.text" bundle="APPLICATION_RESOURCES" />
			</div>
			<br />
			

			<div align="center">
				<table>
					<tr>
						<td>
							<fr:form action="<%="/student/payments.do?method=showEvents&" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.buildContextAttribute("/student")%>">
								<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.view.payments"/></html:submit>
							</fr:form>
						</td>
						<td>
							<fr:form action="/home.do">
								<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.proceed"/></html:submit>
							</fr:form>
						</td>
					</tr>
				</table>
			</div>
		</div>
		
	</body>
</html:html>
