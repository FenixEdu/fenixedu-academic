<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
  <bean:define id="personalInfo" name="<%= SessionConstants.PERSONAL_INFO_KEY %>" scope="session"/>
  <html:form action="/changePersonalInfoDispatchAction?method=change">

		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<h2><bean:message key="label.person.title.changePersonalInfo" /></h2>
        <table width="100%" cellspacing="0">
			<tr>
				<td class="infoop" ><span class="emphasis-box">info</span>
        		<td class="infoop">
        			<bean:message key="message.person.changeContacts.info" /> 
				</td>
         	</tr>
         </table>
	<br />
	<table>
          <!-- Telemovel -->
          <tr>
            <td width="15%"><bean:message key="label.person.mobilePhone" /></td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.mobilePhone" property="mobilePhone"/></td>
          </tr>
          <!-- Work Phome -->
          <tr>
            <td width="15%"><bean:message key="label.person.workPhone" /></td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.workPhone" property="workPhone" maxlength="20"/>&nbsp;
			<bean:message key="label.person.publicData" /></td>
          </tr>
          <!-- E-Mail -->
          <tr>
            <td><bean:message key="label.person.email" /></td>
	        <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.email" property="email"/>
	        	&nbsp;<bean:message key="label.person.availableEmail" />
	        	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.availableEmail" property="availableEmail" value="true"/></td>
	      </tr>
          <!-- WebPage -->
          <tr>
            <td><bean:message key="label.person.webSite" /></td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.webSite" property="webSite"/>
            	&nbsp;<bean:message key="label.person.availableWebSite" />
	        	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.availableWebSite" property="availableWebSite" value="true"/></td>
          </tr>
          <!-- Photo -->

          <tr valign="top">
          	<td><bean:message key="label.person.photo" /></td>
            <td>
 				<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
 				&nbsp;<bean:message key="label.person.availablePhoto" />
	        	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.availablePhoto" property="availablePhoto" value="true"/></td>
          </tr>

   	</table>
<br /><br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.Alterar" property="Alterar" styleClass="inputbutton"><bean:message key="label.change"/></html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.Reset" property="Reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
      </html:form>  
  </body>
