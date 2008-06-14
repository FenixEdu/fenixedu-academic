<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="title.teaching" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="label.version.information" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<bean:define id="competenceCourseID" name="changeRequest" property="competenceCourse.idInternal"/>
<bean:define id="changeRequestID" name="changeRequest" property="idInternal"/>

<ul>
	<li>
		<html:link page="<%= "/competenceCourses/manageVersions.do?method=displayRequest&departmentID=" + request.getParameter("departmentID")%>"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>
	</li>
</ul>



<logic:notEmpty name="changeRequest">

	<logic:notPresent name="changeRequest" property="approved">
		<p>
			<span class="warning0"><bean:message key="question.aprove.proposal" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span> 
			<html:link page="<%= "/competenceCourses/manageVersions.do?method=approveRequest&changeRequestID=" + changeRequestID + "&departmentID=" + request.getParameter("departmentID") %>">
				<bean:message key="label.approve" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
			</html:link>,
			<html:link page="<%= "/competenceCourses/manageVersions.do?method=rejectRequest&changeRequestID=" + changeRequestID + "&departmentID=" + request.getParameter("departmentID") %>">
				<bean:message key="label.reject" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
			</html:link>
		</p>
	</logic:notPresent>		
			
	<p class="mbottom05"><strong><bean:message key="label.generalInformation" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong>:</p>
	<fr:view name="changeRequest" schema="present.competenceCourseInformation.change.request">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright thtop mtop05"/>
			<fr:property name="columnClasses" value="width150px,"/>
			<fr:property name="rowClasses" value="bold,,,,,,tdhl1,,,,,,,,,,,"/>
		</fr:layout>
	</fr:view>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="label.loadInformation" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong>:</p>
	<fr:view name="changeRequest" schema="present.semester1.loads">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
		</fr:layout>
	</fr:view>
	
	<logic:equal name="changeRequest" property="regime" value="ANUAL">
		<fr:view name="changeRequest" schema="present.semester2.loads">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
			</fr:layout>
		</fr:view>			
	</logic:equal>
	
	<%--
	<p class="mtop15 mbottom05"><strong><bean:message key="label.bibliographyInformation" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong>:</p>
	--%>

	<logic:notEmpty name="changeRequest" property="bibliographicReferences.bibliographicReferencesSortedByOrder">

		<logic:notEmpty name="changeRequest"  property="bibliographicReferences.mainBibliographicReferences">
			<p class="mbottom05"><strong><bean:message key="label.primaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
			<fr:view name="changeRequest" property="bibliographicReferences.mainBibliographicReferences" schema="view.reference">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright thcenter mtop05"/>
					<fr:property name="columnClasses" value=",,acenter,,"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>

		<logic:notEmpty name="changeRequest" property="bibliographicReferences.secondaryBibliographicReferences">		
			<p class="mbottom05"><strong><bean:message key="label.secundaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
			<fr:view name="changeRequest" property="bibliographicReferences.secondaryBibliographicReferences" schema="view.reference">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright thcenter mtop05"/>
					<fr:property name="columnClasses" value=",,acenter,,"/>
				</fr:layout>
			</fr:view>
	   </logic:notEmpty>

	</logic:notEmpty>

	<logic:empty name="changeRequest" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
		<p>
			<em><bean:message key="label.no.bibliography" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.
		</p>
	</logic:empty>
	
</logic:notEmpty>

<logic:empty name="changeRequest">
	<p>
		<em><bean:message key="label.changeRequest.notAvailable" bundle="BOLONHA_MANAGER_RESOURCES"/></em>.
	</p>
</logic:empty>