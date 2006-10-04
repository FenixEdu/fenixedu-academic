<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<html:xhtml/>

<br/>
<h2><strong><bean:message key="link.create.external.person"/></strong></h2>

<span class="error"><!-- Error messages go here --><html:errors/><br /></span>

<fr:form action="/externalPerson.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>

	<h2><strong><bean:message key="label.search.for.external.person" bundle="MANAGER_RESOURCES" /></strong></h2>
	<fr:edit id="anyPersonSearchBean" name="anyPersonSearchBean" schema="net.sourceforge.fenixedu.domain.Person.AnyPersonSearchBean" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>

	<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>

<br/>
<br/>

<logic:equal name="anyPersonSearchBean" property="hasBeenSubmitted" value="true">
	<bean:define id="people" name="anyPersonSearchBean" property="search"/>
		<table class="tstyle1"><tbody>
			<tr>
				<th scope="col"><bean:message key="label.name" bundle="MANAGER_RESOURCES"/></th>
				<th scope="col"><bean:message key="label.identification" bundle="MANAGER_RESOURCES"/></th>
				<th scope="col"><bean:message key="label.person.unit.info" bundle="MANAGER_RESOURCES"/></th>
				<th scope="col"><bean:message key="label.has.parking.party"/></th>
				<th scope="col"></th>
			</tr>
			<logic:iterate id="person" name="people">
				<bean:size id="numberUnitsTemp" name="person" property="parentsSet"/>
				<bean:define id="numberUnits"><bean:write name="numberUnitsTemp"/></bean:define>
				<logic:empty name="person" property="parentsSet">
					<bean:define id="numberUnits" value="1"/>
				</logic:empty>
				<tr>
					<td colspan="<%= numberUnits %>"><bean:write name="person" property="name"/></td>
					<bean:define id="docIDTitle" type="java.lang.String"><logic:present name="person" property="idDocumentType"><bean:message name="person" property="idDocumentType.name" bundle="ENUMERATION_RESOURCES"/></logic:present></bean:define>
					<td colspan="<%= numberUnits %>" title="<%= docIDTitle %>"><bean:write name="person" property="documentIdNumber"/></td>
					<logic:empty name="person" property="parentsSet">
						<td>
						</td>
					</logic:empty>
					<logic:iterate id="accountability" name="person" property="parentsSet">
						<td>
							<bean:write name="accountability" property="parentParty.name"/>
							<br/>
						</td>
					</logic:iterate>
					<logic:present name="person" property="parkingParty">
						<td colspan="<%= numberUnits %>">
							<bean:message key="label.yes"/>
						</td>
						<td colspan="<%= numberUnits %>">
						</td>
					</logic:present>
					<logic:notPresent name="person" property="parkingParty">
						<td colspan="<%= numberUnits %>">
							<bean:message key="label.no"/>
						</td>
						<td colspan="<%= numberUnits %>">
							<bean:define id="url" type="java.lang.String">/externalPerson.do?method=createParkingParty&amp;personID=<bean:write name="person" property="idInternal"/></bean:define>
							<html:link page="<%= url %>"><bean:message key="link.create.external.person" /></html:link>
						</td>
					</logic:notPresent>
				</tr>
			</logic:iterate>
		</tbody></table>
	<br/>
	<br/>
	<bean:define id="url" type="java.lang.String">/externalPerson.do?method=prepareCreate&amp;name=<bean:write name="anyPersonSearchBean" property="name"/>&amp;idDocumentType=<bean:write name="anyPersonSearchBean" property="idDocumentType"/>&amp;documentIdNumber=<bean:write name="anyPersonSearchBean" property="documentIdNumber"/></bean:define>
	<html:link action="<%= url %>">
		<bean:message key="link.create.external.person.because.does.not.exist" bundle="MANAGER_RESOURCES"/>
	</html:link>
</logic:equal>
