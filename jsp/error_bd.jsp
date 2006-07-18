<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
    <title><bean:message key="title.error"/></title>
    <html:errors/>
    <center> <b><bean:message key="message.error.ocurred"/></b> </center>
     <br/> <bean:message key="message.error.sendEmail"/>
     <br/><bean:message key="message.error.goBack"/>
     <br/>
    <html:link page="/exceptionHandlingAction.do?method=goBack">
   	 <center><b><bean:message key="link.goBack"/></b></center>
    </html:link>
    <html:form action="/exceptionHandlingAction.do?method=sendEmail">
    <table align="center" >
    	<tr><td>
    	<bean:message key="property.email"/></td><td>
    	<html:text bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" value=""/> </td>
    	</tr>
    	<tr><td>
    	<bean:message key="property.subject"/></td><td>
   		<html:text bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" value=""/></td>
   		</tr>
    	<tr><td>
   		<bean:message key="property.message"/></td><td>
    	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.body" property="body" value=""/></td>
    	</tr>	
      </table>	
      <center>
    	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" >
   		 <bean:message key="label.submit"/>
    	</html:submit>
   	  </center>
    </html:form>