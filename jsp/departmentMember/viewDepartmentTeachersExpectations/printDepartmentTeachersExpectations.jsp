<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<style>
	.header {
	background-color: #eee;
	padding: 0.5em;
	font-size: 1.4em;
	}
	.block {
	padding: 0 0.5em;
	}
	.indent {
	padding: 0 2em;
	}
	.limbottom li {
	padding-bottom: 8px;			
	
	}
</style>


<fr:view name="teacherPersonalExpectations" scope="request">
			<fr:layout name="flowLayout">
		<fr:property name="eachLayout" value="printDepartmentTeachersExpectationsNestedTemplateLayout" />
		<fr:property name="htmlSeparator" value="<div class=\"break-before\"></div>"/>
	</fr:layout>
</fr:view>







