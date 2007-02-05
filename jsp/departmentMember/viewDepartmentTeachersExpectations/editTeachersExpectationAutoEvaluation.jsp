<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="executionYearId" name="expectation" property="executionYear.idInternal"/>
<fr:edit name="expectation" slot="autoEvaluation" layout="rich-text">
	<fr:layout name="rich-text">
		<fr:property name="config" value="simple"/>
	</fr:layout>
	<fr:destination name="success" path="<%= "/teacherExpectationAutoAvaliation.do?method=show&amp;executionYearId=" + executionYearId %>"/>
	<fr:destination name="cancel" path="<%= "/teacherExpectationAutoAvaliation.do?method=show&amp;executionYearId=" + executionYearId %>"/>
</fr:edit>