<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<fr:edit id="createPortal" name="bean" schema="edit.portal.using.bean"
action="/portalManagement.do?method=createPortal">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/> 
	</fr:layout>	
</fr:edit>

