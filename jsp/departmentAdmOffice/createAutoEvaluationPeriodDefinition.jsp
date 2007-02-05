<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="executionYearId" name="executionYear" property="idInternal"/>

<fr:create id="createInterval" type="net.sourceforge.fenixedu.domain.TeacherAutoEvaluationDefinitionPeriod" schema="editAutoAvaliationPeriod">
   <fr:hidden slot="executionYear" name="executionYear"/>
   <fr:hidden slot="department" name="department"/>
<fr:destination name="success" path="<%= "/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriod&executionYearId=" + executionYearId %>"/>
</fr:create>