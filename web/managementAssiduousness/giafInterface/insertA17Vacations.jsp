<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.A17Vacations" /></h2>

<html:messages id="message">
	<p><span class="error0"><bean:write name="message" /></span></p>
</html:messages>


<logic:present name="giafInterfaceBean">
	<fr:edit id="giafInterfaceBean" name="giafInterfaceBean" 
	schema="edit.giafInterfaceBean.file" action="/giafInterface.do?method=insertA17Vacations">
	</fr:edit>
</logic:present>




<logic:present name="giafInterfaceDocuments">
	<strong><bean:message key="title.exportedFilesToGiaf"/></strong>
	<fr:view name="giafInterfaceDocuments" schema="show.giafInterfaceDocuments" layout="tabular">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder" />
			<fr:property name="columnClasses" value="bgcolor3 acenter" />
			<fr:property name="headerClasses" value="acenter" />
		</fr:layout>
		<fr:schema bundle="ASSIDUOUSNESS_RESOURCES" type="net.sourceforge.fenixedu.domain.assiduousness.GiafInterfaceDocument">
			<fr:slot name="createdWhen" key="label.creationDate"/>
			<fr:slot name="modifiedBy.employeeNumber" key="label.employee"/>
			<fr:slot name="giafInterfaceFile" layout="link" key="label.file"/>
		</fr:schema>
	</fr:view>
</logic:present>
