<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<fr:view name="unit" property="site" type="net.sourceforge.fenixedu.domain.UnitSite" layout="unit-top-menu">
    <fr:layout>
        <fr:property name="sectionUrl" value="<%= request.getContextPath() + "/publico/department/departmentSite.do?method=section" %>"/>
        <fr:property name="contextParam" value="selectedDepartmentUnitID"/>
        <fr:property name="contextRelative" value="false"/>
    </fr:layout>
</fr:view>
