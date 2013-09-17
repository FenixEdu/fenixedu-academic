<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:message key="scientificCouncil"/>
<h2><bean:message key="label.title.officialPublication"/></h2>


	<bean:define id="officialPub"
		type="net.sourceforge.fenixedu.domain.DegreeOfficialPublication"
		name="officialPub" />

<bean:define id="pubBean"
	type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans.OfficialPublicationBean"
	name="pubBean" />

<bean:define id="pubBeanNames" name="pubBean" property="specializationNames" />
	<bean:define id="officialPubAreas" name="officialPub" property="specializationArea" />
	
	<br/>

<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
		<p><span class="success0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>	
	
<strong><bean:message key="label.edit.name.officialPublication"/></strong>
<fr:form
	action="/curricularPlans/editOfficialPublication.do?method=updateOfficialPub">
	<fr:edit name="referenceBean" id="referenceBean">
		<fr:schema bundle="SCIENTIFIC_COUNCIL_RESOURCES"
			type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans.OfficialPublicationBean">
			<fr:slot name="newReference" key="degree.officialPublication">
				<fr:property name="size" value="60" />
			</fr:slot>
			<fr:slot name="publication" required="true" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
		</fr:layout>
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		styleClass="inputbutton">
		<bean:message key="label.edit.name.officialPublication" />
	</html:submit>
</fr:form>

<br/>

<br/>
<strong><bean:message key="label.name.specializationArea"/></strong>



<fr:view name="officialPubAreas">
	<fr:schema bundle="SCIENTIFIC_COUNCIL_RESOURCES"
		type="net.sourceforge.fenixedu.domain.DegreeSpecializationArea">
		<fr:slot name="nameEn" key="label.name.specializationArea.nameEn" />
		<fr:slot name="namePt" key="label.name.specializationArea.namePt" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",,,tdclear" />

		<fr:property name="linkFormat(edit)"
			value="/curricularPlans/editOfficialPublication.do?method=editSpecializationArea&amp;specializationId=${externalId}" />
		<fr:property name="key(edit)" value="edit" />
		<fr:property name="bundle(edit)" value="SCIENTIFIC_COUNCIL_RESOURCES" />

		<fr:property name="linkFormat(delete)"
			value="/curricularPlans/editOfficialPublication.do?method=removeSpecializationArea&amp;specializationId=${externalId}" />
		<fr:property name="key(delete)" value="delete" />
		<fr:property name="bundle(delete)"
			value="SCIENTIFIC_COUNCIL_RESOURCES" />
		<fr:property name="confirmationKey(delete)"
			value="confirm.delete.specializationArea" />
		<fr:property name="confirmationBundle(delete)"
			value="SCIENTIFIC_COUNCIL_RESOURCES" />

	</fr:layout>
</fr:view>
<br/>

<strong><bean:message key="label.edit.degree.specializationArea"/></strong>
<fr:form
	action="/curricularPlans/editOfficialPublication.do?method=createNewSpecializationArea">
	<fr:edit name="pubBean" id="pubBean">
		<fr:schema bundle="SCIENTIFIC_COUNCIL_RESOURCES"
			type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans.OfficialPublicationBean">
			<fr:slot name="newNamePt" key="label.name.specializationArea.namePt" />
			<fr:slot name="newNameEn" key="label.name.specializationArea.nameEn" />
		</fr:schema>
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear" />
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		styleClass="inputbutton">
		<bean:message key="create" />
	</html:submit>
</fr:form>
<br/>

<html:link
	page="<%= "/curricularPlans/editDegree.faces?degreeId="
							+ officialPub.getDegree().getExternalId() + "&amp;" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.buildContextAttribute("/scientificCouncil") %>">
	<bean:message key="back" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
</html:link>