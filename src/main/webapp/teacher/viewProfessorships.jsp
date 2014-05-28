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
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<tiles:insert definition="df.layout.two-column.teacher" beanName="" flush="true">
  <tiles:put name="executionCourseName" value="/teacher/executionCourseName.jsp" />
  <tiles:put name="navGeral" value="/teacher/commonNavGeralTeacher.jsp" />
  <tiles:put name="body" value="/teacher/viewProfessorships_bd.jsp" />
  <tiles:put name="navLocal" value="/teacher/professorshipnavbar.jsp" type="page"/>
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>