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

<p class="mtop15 mbottom05">
	<a href="<%= request.getContextPath() + "/alumni/searchAlumni.do?method=showAlumniList"%>"><bean:message key="link.search.alumni.newSearch" bundle="ALUMNI_RESOURCES" /></a>
</p>

<table class="tstyle2 thlight thleft thwhite">
	<tr>
		<th>
			<bean:message key="label.name" bundle="ALUMNI_RESOURCES" />:
		</th>
		<td>
			<fr:view name="alumniData" property="person.name" />
		</td>
	</tr>

	<logic:equal name="alumniData" property="person.availableEmail" value="true">

		<bean:define id="emailAddresses" name="alumniData" property="person.emailAddresses" />
		<bean:size id="size" name="emailAddresses" />
		<logic:notEmpty name="emailAddresses">
			<logic:iterate id="email" name="emailAddresses">
				<bean:define id="isAvailable" name="email" property="visibleToAlumni" />
				<logic:equal name="isAvailable" value="true">
					<tr>
						<th>
							<bean:message key="label.partyContacts.EmailAddress" />
							(<bean:message name="email" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):
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
				</logic:equal>

			</logic:iterate>
		</logic:notEmpty>

	</logic:equal>


	<tr>
		<th>
			<bean:message key="label.photo" bundle="ALUMNI_RESOURCES" />:
		</th>
		<td>
			<bean:define id="personId" name="alumniData" property="person.externalId" />
			<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode=" + personId.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
		</td>
	</tr>


	<fr:view name="alumniData" property="person.webAddresses">
	    <fr:layout name="contact-table">
	       	<fr:property name="publicSpace" value="false"/>
	        <fr:property name="bundle" value="ALUMNI_RESOURCES" />
	        <fr:property name="label" value="label.webpage" />
	    </fr:layout>
	</fr:view>

</table>