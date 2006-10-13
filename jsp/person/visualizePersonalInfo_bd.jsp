<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
<html:xhtml/>
 
 <table width="100%">
 	<tr valign="bottom">
 		<td valign="top"><h2><bean:message key="label.person.title.personalConsult" /></h2></td>
 		<td align="right">
 			<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
 		</td>
 	</tr>
 </table>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<br/>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">1</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.contactInfo" /></strong></td>
	</tr>
</table>
<br />
<fr:form>
	<fr:edit id="contact" name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.contact.info">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thleft thlight"/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
		<bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
	</html:submit>
</fr:form>

<br/>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">2</span></td>
		<td class="infoop"><strong><bean:message key="label.person.information.to.publish" /></strong></td>
	</tr>
</table>
<br />
<fr:form>
	<fr:edit id="photo" name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.information.to.publish">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thleft thlight"/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
		<bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
	</html:submit>
</fr:form>

<br/>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">3</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.personal.info" /></strong></td>
	</tr>
</table>
<fr:form>
	<br/>
	<bean:message key="label.homepage.name.instructions" bundle="HOMEPAGE_RESOURCES"/>
	<fr:edit id="nickname" name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.nickname">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thleft thlight"/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
		<bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
	</html:submit>
</fr:form>
<fr:view name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.personal.info">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thleft thlight"/>
	</fr:layout>	
</fr:view>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">4</span></td>
		<td class="infoop"><strong><bean:message key="label.person.login.info" /></strong></td>
	</tr>
</table>
<fr:view name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.user.info">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thleft thlight"/>
	</fr:layout>	
</fr:view>

<br/>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">5</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.filiation" /></strong></td>
	</tr>
</table>
<fr:view name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.family">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thleft thlight"/>
	</fr:layout>	
</fr:view>

<br/>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">6</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.addressInfo" /></strong></td>
	</tr>
</table>
<fr:view name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.residence">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thleft thlight"/>
	</fr:layout>	
</fr:view>
