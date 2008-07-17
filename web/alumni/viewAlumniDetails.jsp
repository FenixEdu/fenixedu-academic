<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<!-- viewAlumniDetails.jsp -->
<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>


<em><bean:message key="label.alumni.main.title" bundle="ALUMNI_RESOURCES" /></em>
<h2><bean:message key="link.search.alumni" bundle="ALUMNI_RESOURCES" /></h2>


<table class="tstyle2 thlight thleft thwhite">
	<tr>
		<th>
			<bean:message key="label.name" bundle="ALUMNI_RESOURCES" />:
		</th>
		<td>
			<fr:view name="alumniData" layout="name-with-alias" property="person" />
		</td>
	</tr>

	<bean:define id="availableEmail" name="alumniData" property="person.availableEmail" />
	<logic:equal name="availableEmail" value="true">

		<bean:define id="emailAddresses" name="alumniData" property="person.emailAddresses" />
		<bean:size id="size" name="emailAddresses" />
		<logic:notEmpty name="emailAddresses">
			<logic:iterate id="email" name="emailAddresses">
				<tr>
					<th>
						<bean:message key="label.partyContacts.EmailAddress" /> (<bean:message
						name="email" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):
					</th>
					<td>
						<bean:write name="email" property="value" />
						<logic:equal name="email" property="defaultContact" value="true">
							<logic:notEqual name="size" value="1">
								(<bean:message key="label.partyContacts.defaultContact" />)
							</logic:notEqual>
						</logic:equal>
					</td>
				</tr>
			</logic:iterate>
		</logic:notEmpty>

	</logic:equal>

	<bean:define id="availablePhoto" name="alumniData" property="person.availablePhoto" />
	<bean:define id="personId" name="alumniData" property="person.idInternal" />
	<logic:equal name="availablePhoto" value="true">
		<tr>
			<th>
				<bean:message key="label.photo" bundle="ALUMNI_RESOURCES" />:
			</th>
			<td>
				<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode=" + personId.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
			</td>
		</tr>
	</logic:equal>

	<bean:define id="availableWebSite" name="alumniData" property="person.availableWebSite" />
	<logic:equal name="availableWebSite" value="true">
		<tr>
			<th>
				<bean:message key="label.webpage" bundle="ALUMNI_RESOURCES" />:
			</th>
			<td>
				<fr:view name="alumniData" property="person.webAddress" />
			</td>
		</tr>
	</logic:equal>

</table>