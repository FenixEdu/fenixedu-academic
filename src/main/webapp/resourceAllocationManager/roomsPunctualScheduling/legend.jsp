<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:xhtml/>

<p class="mtop15 mbottom025"><em><bean:message bundle="SOP_RESOURCES" key="label.legend"/>:</em></p>
<p class="mvert0"><em>[C] <bean:message bundle="SOP_RESOURCES" key="label.continuous"/></em></p>
<p class="mvert0"><em>[D] <bean:message bundle="SOP_RESOURCES" key="label.daily"/></em></p>
<p class="mvert0"><em>[D-S] <bean:message bundle="SOP_RESOURCES" key="label.daily.1"/></em></p>
<p class="mvert0"><em>[D-D] <bean:message bundle="SOP_RESOURCES" key="label.daily.2"/></em></p>
<p class="mvert0"><em>[D-SD] <bean:message bundle="SOP_RESOURCES" key="label.daily.3"/></em></p>
<p class="mvert0"><em>[S] <bean:message bundle="SOP_RESOURCES" key="label.weekly"/></em></p>
<p class="mvert0"><em>[Q] <bean:message bundle="SOP_RESOURCES" key="label.biweekly"/></em></p>