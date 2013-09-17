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
   <center> <b><bean:message key="message.error.notAuthorized"/></b> </center>
     <br/>
     <br/>
     <br/>
     <br/>
     <br/>
    <html:link page="/">
   	 <center><b><bean:message key="link.login"/></b></center>
    </html:link>
  </body>
</html>
