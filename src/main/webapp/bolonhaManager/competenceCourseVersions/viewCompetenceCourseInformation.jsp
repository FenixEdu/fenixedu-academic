<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
			<fr:property name="classes" value="tstyle2 thlight thright thtop mtop05"/>
			<fr:property name="rowClasses" value=",,,bold,,,,,,,,,,"/>
	</fr:layout>
</fr:view>

<logic:notEmpty name="information" property="bibliographicReferences.bibliographicReferencesSortedByOrder">

<logic:notEmpty name="information"  property="bibliographicReferences.mainBibliographicReferences">
		<p class="mbottom05"><strong><bean:message key="label.primaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
		<fr:view name="information" property="bibliographicReferences.mainBibliographicReferences" schema="view.reference">	
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thright thcenter mtop05"/>
				<fr:property name="columnClasses" value=",,acenter,,"/>
			</fr:layout>
		</fr:view>
		</logic:notEmpty>

		<logic:notEmpty name="information" property="bibliographicReferences.secondaryBibliographicReferences">		
			<p class="mbottom05"><strong><bean:message key="label.secondaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
			<fr:view name="information" property="bibliographicReferences.secondaryBibliographicReferences" schema="view.reference">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thright thcenter mtop05"/>
					<fr:property name="columnClasses" value=",,acenter,,"/>
				</fr:layout>
			</fr:view>
	   </logic:notEmpty>
</logic:notEmpty>

<logic:empty name="information" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
	<p>
		<em><bean:message key="label.no.bibliography" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
	</p>
</logic:empty>