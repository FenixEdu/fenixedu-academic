<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.publications.Management"/></h2>
<html:form action="/prepareSearchPerson">
<logic:notPresent name="publicationTypesList">
	N�o existem os tipos
</logic:notPresent>
<logic:present name="publicationTypesList">
<br/>
<h3>

<bean:message key="message.publications.insertPublication" />

</h3>
<p class="infoop"><span class="emphasis-box">1</span>
	<bean:message key="message.publications.choosePublicationType" />
</p>
	<span class="error">
		<html:errors/>
	</span>
	<br />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.typePublication" property="typePublication"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareSearchPerson"/>

<table>
	<tr>
		<td><bean:message key="message.publications.publicationType"/> &nbsp;</td>
		<td></td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.infoPublicationTypeId" property="infoPublicationTypeId">
				<html:options collection="publicationTypesList" property="idInternal" labelProperty="publicationType"/>
			</html:select>
		</td>
	</tr>
	<tr>
		&nbsp;
	</tr>
	<tr>
		<td>
			<html:button bundle="HTMLALT_RESOURCES" altKey="button.idInternal" property="idInternal" value="Escolher" styleClass="inputbutton" onclick="this.form.submit()"/>
		</td>
	</tr>
</table>
<br/> 
</logic:present>
</html:form>