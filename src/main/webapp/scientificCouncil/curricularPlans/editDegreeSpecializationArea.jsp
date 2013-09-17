<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="message.credits.masterDegree.title"/></h2>

<bean:define id="specializationArea"
	name="specializationArea" />
	
<bean:define id="officialPub" type="net.sourceforge.fenixedu.domain.DegreeOfficialPublication" name="specializationArea" property="specializationArea.officialPublication"/>	

<fr:form
	action='<%="/curricularPlans/editOfficialPublication.do?officialPubId=" + officialPub.getExternalId()%>'>
	<html:hidden property="method" value="updateArea"/>
	
	<fr:edit name="specializationArea" id="specializationArea">
		<fr:schema bundle="SCIENTIFIC_COUNCIL_RESOURCES"
			type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans.OfficialPublicationBean$SpecializationName">
			<fr:slot name="enName" key="degree.officialPublication.pt" />
			<fr:slot name="ptName" key="degree.officialPublication.en" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear" />
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		styleClass="inputbutton">
		<bean:message key="edit" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"
		onclick="this.form.method.value='preparePubs';">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel" />
	</html:cancel>
</fr:form>

