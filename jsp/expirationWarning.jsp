<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="pt" xml:lang="pt">
	<head>
		<title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="title.login" bundle="GLOBAL_RESOURCES" /></title>
		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	</head>

	<body>
		<div id="container">
			<div id="dotist_id">
				<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			
<style type="text/css">
.dnone { display: none; }
.dinline { display: inline; }
</style>

<script language="JavaScript">	
function check(e,v){
	if (e.className == "dnone")
  	{
	  e.className = "dinline";
	  v.value = "-";
	}
	else {
	  e.className = "dnone";
  	  v.value = "+";
	}
}
</script>
			<div id="txt">
				<h1><bean:message key="label.expiration.warning.change.password"/></h1>
				<p style="margin-bottom: 0.5em;"><bean:message key="label.expiration.warning.expiration.message"/> <b><bean:write name="days"/> <bean:message key="label.expiration.warning.days"/></b> (<bean:write name="dayString"/>).
					<br/>
					
					<bean:message key="message.expiration.warning.short"/> <a href="#" class="dnone" id="instructionsButton" onclick="javascript:check(document.getElementById('instructions'), document.getElementById('instructionsButton')); return false;"><bean:message key="link.expiration.warning.read.more"/></a>
				</p>
				<div id="instructions" class="dinline">
					<div style="color: #555; background-color: #f5f5f5; padding: 0.1em 0; margin: 1em 0;">
						<p>
							<bean:message key="message.expiration.warning.long"/>
							<p><a href="https://ciist.ist.utl.pt/normas/password.php" target="_blank"><bean:message key="link.expiration.warning.rules"/></a></p>
						</p>
					</div>
				</div>
				<p style="margin-bottom: 0.5em;"><bean:message key="message.expiration.warning"/></p>
				<p style="text-align: center; margin-top: 30px; margin-bottom: 30px;">
					<html:link styleClass="button" page="/person/changePasswordForward.do" ><bean:message key="link.expiration.warning.change.now"/></html:link>
					<bean:define id="path" name="path"/>
					<html:link page="<%= path.toString() %>" styleClass="button"><bean:message key="link.expiration.warning.change.later"/></html:link>
				</p>
			</div>
			<div id="txt">
				<center>
					<p>
						<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES"/>:
						<bean:message key="institution.email.support" bundle="GLOBAL_RESOURCES"/>
					</p>
				</center>
			</div>
			<script>
				check(document.getElementById('instructions'), document.getElementById('instructionsButton'));
				document.getElementById('instructionsButton').className="dinline";
			</script>	
		</div>
	</body>
</html>
