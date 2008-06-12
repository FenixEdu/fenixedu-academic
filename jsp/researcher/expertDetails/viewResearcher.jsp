<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.find.an.expert"/></h2>
<h3><bean:message key="label.options" bundle="RESEARCHER_RESOURCES"/></h3>	

<logic:present name="researcher">

	<div class="infoop2"><bean:message key="label.find.an.expert.intro" bundle="RESEARCHER_RESOURCES"/></div>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="label.availability" bundle="RESEARCHER_RESOURCES"/></strong></p>
	<p class="mtop05"><bean:message key="label.available.to.search.question" bundle="RESEARCHER_RESOURCES"/>: <b><fr:view name="researcher" property="allowsToBeSearched"/></b></p>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="label.can.contact" bundle="RESEARCHER_RESOURCES"/></strong></p>
	<p class="mvert05"><bean:message key="label.allowsContactByStudents" bundle="RESEARCHER_RESOURCES"/>: <b><fr:view name="researcher" property="allowsContactByStudents"/></b></p>
	<p class="mvert05"><bean:message key="label.allowsContactByMedia" bundle="RESEARCHER_RESOURCES"/>: <b><fr:view name="researcher" property="allowsContactByMedia"/></b></p>
	<p class="mtop05"><bean:message key="label.allowsContactByOtherResearchers" bundle="RESEARCHER_RESOURCES"/>: <b><fr:view name="researcher" property="allowsContactByOtherResearchers"/></b></p>	
	
	<p class="mtop15 mbottom05"><strong><bean:message key="label.available.contacts" bundle="RESEARCHER_RESOURCES"/></strong></p>
	<logic:iterate id="partyContact" name="researcher" property="availableContacts">
		<p class="mvert05">
			<fr:view name="partyContact" property="class">
				<fr:layout name="label">
					<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
				</fr:layout>
			</fr:view>
			(<fr:view name="partyContact" property="type"/>) <fr:view name="partyContact" property="presentationValue"/>
		</p>
	</logic:iterate>
	<logic:empty name="researcher" property="availableContacts">
		<p class="mtop05"><em><bean:message key="label.no.availableContacts" bundle="RESEARCHER_RESOURCES"/>.</em></p>
	</logic:empty>
	
		
	<p class="mtop15 mbottom05"><strong><bean:message key="label.keywords" bundle="RESEARCHER_RESOURCES"/></strong></p>
	<p class="mtop05"><fr:view name="researcher" property="keywords"/></p>
		
		<p><html:link page="/researcherManagement.do?method=edit"><bean:message key="label.change.options" bundle="RESEARCHER_RESOURCES"/></html:link></p>

</logic:present>