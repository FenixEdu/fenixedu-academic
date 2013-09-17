<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html>
  <head>
    <title><bean:message key="title.error"/></title>
  </head>
  <body>
    <html:errors/>
   <center> <b><bean:message key="message.error.ocurred"/></b> </center>
     <br/> <bean:message key="message.error.sendEmail"/>
     <br/><bean:message key="message.error.goBack"/>
     <br/>
    <html:link page="/exceptionHandling.do?method=goBack">
   	 <center><b><bean:message key="link.goBack"/></b></center>
    </html:link>
    <html:form action="/exceptionHandling.do?method=sendEmail">
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
    	<html:textarea bundle="HTMLALT_RESOURCES" property="body" value=""/></td>
    	</tr>
    	
      </table>	
      <center>
    	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" >
   		 <bean:message key="label.submit"/>
    	</html:submit>
   	  </center>
    </html:form>
  </body>
</html>
