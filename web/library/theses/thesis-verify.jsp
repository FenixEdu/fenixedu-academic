<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="thesisId" name="thesis" property="idInternal"/>

<h2>
	<bean:message key="title.library.thesis.verify"/>
</h2>

<ul>
	<li>
		<logic:notEqual name="thesis" property="libraryConfirmation" value="true">
			<html:link page="/theses/list.do?method=prepare">
				<bean:message key="link.back"/>
			</html:link>
		</logic:notEqual>
		<logic:equal name="thesis" property="libraryConfirmation" value="true">
			<html:link page="/theses/listConfirmed.do?method=prepare">
				<bean:message key="link.back"/>
			</html:link>
		</logic:equal>
	</li>
</ul>

<logic:present name="edit">
	<fr:edit id="edit" name="thesis" schema="library.thesis.details.edit"
			 action="<%= "/theses/checkThesis.do?method=prepare&amp;thesisID=" + thesisId %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mbottom05"/>
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/theses/checkThesis.do?method=edit&amp;thesisID=" + thesisId %>"/>
		<fr:destination name="cancel" path="<%= "/theses/checkThesis.do?method=prepare&amp;thesisID=" + thesisId %>"/>
	</fr:edit>
</logic:present>

<logic:notPresent name="edit">
	<fr:view name="thesis" schema="library.thesis.details">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom0"/>
			<fr:property name="columnClasses" value="width12em,width35em,"/>
		</fr:layout>
	</fr:view>
</logic:notPresent>

<p>
	<html:link page="/theses/checkThesis.do?method=edit" paramId="thesisID" paramName="thesis" paramProperty="idInternal">
		<bean:message key="link.edit"/>
	</html:link>,
	<logic:equal name="thesis" property="libraryConfirmation" value="true">
		<html:link page="/theses/checkThesis.do?method=unconfirm" paramId="thesisID" paramName="thesis" paramProperty="idInternal">
			<bean:message key="link.unconfirm"/>
		</html:link>
	</logic:equal>
	<logic:notEqual name="thesis" property="libraryConfirmation" value="true">
		<html:link page="/theses/checkThesis.do?method=confirm" paramId="thesisID" paramName="thesis" paramProperty="idInternal">
			<bean:message key="link.confirm"/>
		</html:link>
	</logic:notEqual>
</p>
