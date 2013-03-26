<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="MANAGER">

<em><bean:message key="title.personal.ingression.data.viewer.unit.name" bundle="GEP_RESOURCES" /></em>

<h2><bean:message key="title.personal.ingression.data.viewer.student.raides.data.view" bundle="GEP_RESOURCES" /></h2>

<p><strong><bean:message key="message.personal.ingression.data.viewer.search.student.with.criteria" bundle="GEP_RESOURCES" /></strong></p>

<fr:form action="/personalIngressionDataViewer.do?method=findStudents">
	<fr:edit id="chooseStudentBean" name="chooseStudentBean" visible="false" />
	
	<fr:edit id="chooseStudentBean-choose" name="chooseStudentBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.gep.student.candidacy.personal.ingression.data.ChooseStudentBean" bundle="GEP_RESOURCES">
			<fr:slot name="number" key="label.personal.ingression.data.viewer.search.student.number" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
			</fr:slot>
			<fr:slot name="username" key="label.personal.ingression.data.viewer.search.student.username" />
			<fr:slot name="documentId" key="label.personal.ingression.data.viewer.search.document.id" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tdclear error1" />
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="link.search" bundle="APPLICATION_RESOURCES" /></html:submit>
</fr:form>

<logic:present name="students">

	<logic:empty name="students">
		<p><em><bean:message key="message.personal.ingression.data.viewer.student.not.found" bundle="GEP_RESOURCES" /></em></p>
	</logic:empty>
	
	<logic:notEmpty name="students">
	
		<fr:view name="students">
			<fr:schema type="net.sourceforge.fenixedu.domain.student.Student" bundle="GEP_RESOURCES">
				<fr:slot name="number" key="label.personal.ingression.data.viewer.student.number" />
				<fr:slot name="person.name" key="label.personal.ingression.data.viewer.student.name" />
			</fr:schema>
		</fr:view>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			
			<fr:link name="view" 
				link="/personalIngressionDataViewer.do?method=viewStudent&studentId=${externalId}" 
				label="link.view,APPLICATION_RESOURCES" />
					
		</fr:layout>
	</logic:notEmpty>

</logic:present>

</logic:present>
