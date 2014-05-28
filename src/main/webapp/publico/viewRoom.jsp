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
<tiles:insert definition="definition.public.mainPage" beanName="" flush="true">
	<tiles:put name="symbols_row" value="/publico/degreeSite/symbolsRow.jsp" />
	<tiles:put name="profile_navigation" value="/publico/degreeSite/profileNavigation.jsp" />
    <tiles:put name="lateral_nav" value="/publico/commonNavLocalPub.jsp" />
	<tiles:put name="body" value="/publico/viewRoom_bd.jsp" />
	<tiles:put name="footer" value="/publico/degreeSite/footer.jsp" />
</tiles:insert>
