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
	<div class="dinline forminline">
	<fr:form action="/researcherManagement.do?method=prepare">
		
		<p class="mtop1 mbottom05"><strong><bean:message key="label.availability" bundle="RESEARCHER_RESOURCES"/>:</strong></p>
		<table class="tstyle5 mtop05 dinline">
				<tr>
						<td><bean:message key="label.available.to.search.question" bundle="RESEARCHER_RESOURCES"/>?</td>
						<td><fr:edit name="researcher" slot="allowsToBeSearched" >
										<fr:layout name="radio">
												<fr:property name="classes" value="nobullet liinline"/>		
										</fr:layout>						
								 </fr:edit>	
						</td>
				</tr>
		</table>
		
		<p class="mtop1 mbottom05"><strong><bean:message key="label.can.contact" bundle="RESEARCHER_RESOURCES"/>:</strong></p>
		<fr:edit id="allowsToBeContacted" name="researcher" schema="edit.expert.allowed.contacts">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 mtop05 thlight thleft dinline"/>
				<fr:property name="columnClasses" value=",,tdclear"/>
			</fr:layout>
		</fr:edit>




		<p class="mbottom05"><strong><bean:message key="label.available.contacts" bundle="RESEARCHER_RESOURCES"/>:</strong></p>
		<fr:edit id="contacts" name="researcher" slot="availableContacts">
			<fr:layout name="option-select">
				<fr:property name="classes" value="nobullet noindent liststyle5"/>
				<fr:property name="eachLayout" value="values-comma"/>
				<fr:property name="eachSchema" value="contacts.PartyContact"/>
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.researcher.PartyContactsForResearcher"/>
			</fr:layout>
			
		</fr:edit>
			
		<p class="mtop15"><strong><bean:message key="label.keywords" bundle="RESEARCHER_RESOURCES"/>:</strong></p>
		<table>
		<tr>
		<td>
		<fr:edit id="keywords" name="researcher" slot="keywords">
			<fr:layout name="longText">
				<fr:property name="rows" value="2"/>
				<fr:property name="columns" value="60"/>
				<fr:property name="classes" value="dinline"/>
			</fr:layout>
			<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
		</fr:edit>
		</td>
		<td>
		<fr:hasMessages for="keywords">
			<p class="mtop15"><span class="error0 mbottom0"><fr:message for="keywords" show="message"/></span></p>
		</fr:hasMessages>
		</td>
		</tr>
		</table>

		<br/>		
		<html:submit><bean:message key="button.save" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</fr:form>
	<fr:form action="/researcherManagement.do?method=prepare">
		<html:submit><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</fr:form>
	</div>
</logic:present>