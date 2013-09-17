<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="content">


<bean:define id="contentId" name="content" property="externalId"/>

	<fr:edit name="content" schema="edit.contents.Section.add.InitialContent"
	action="<%="/contentManagement.do?method=viewContainer&amp;contentId=" + contentId%>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5"/>
		</fr:layout>
	</fr:edit>

</logic:present>

