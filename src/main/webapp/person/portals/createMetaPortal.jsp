<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<fr:edit id="createPortal" name="bean" schema="edit.portal.using.bean"
action="/portalManagement.do?method=createPortal">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/> 
	</fr:layout>	
</fr:edit>

