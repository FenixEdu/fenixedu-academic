<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.view.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="<%= "/competenceCourses/manageVersions.do?method=showVersions&competenceCourseID="  + request.getParameter("competenceCourseID") %>">
			<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>		
		</html:link>
	</li>
</ul>
<fr:view name="information" schema="view.competenceCourseInformation.details">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright thtop mtop05"/>
			<fr:property name="columnClasses" value="width150px,"/>
	</fr:layout>
</fr:view>

<logic:notEmpty name="information" property="bibliographicReferences.bibliographicReferencesSortedByOrder">

<logic:notEmpty name="information"  property="bibliographicReferences.mainBibliographicReferences">
		<p class="mbottom05"><bean:message key="label.primaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></p>
		<fr:view name="information" property="bibliographicReferences.mainBibliographicReferences" schema="view.reference">	
			<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright thcenter mtop05"/>
			</fr:layout>
		</fr:view>
		</logic:notEmpty>

		<logic:notEmpty name="information" property="bibliographicReferences.secondaryBibliographicReferences">		
			<p class="mbottom05"><bean:message key="label.secundaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></p>
			<fr:view name="information" property="bibliographicReferences.secondaryBibliographicReferences" schema="view.reference">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright thcenter mtop05"/>
				</fr:layout>
			</fr:view>
	   </logic:notEmpty>
</logic:notEmpty>

<logic:empty name="information" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
	<bean:message key="label.no.bibliography" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
</logic:empty>