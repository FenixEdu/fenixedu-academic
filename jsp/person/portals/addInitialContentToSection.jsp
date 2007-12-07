<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="content">


<bean:define id="contentId" name="content" property="idInternal"/>

	<fr:edit name="content" schema="edit.contents.Section.add.InitialContent"
	action="<%="/contentManagement.do?method=viewContainer&amp;contentId=" + contentId%>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5"/>
		</fr:layout>
	</fr:edit>

</logic:present>

