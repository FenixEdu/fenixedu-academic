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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

	<h1>Validação de Email</h1>
	
	<logic:equal name="state" value="VALID">
		O seu email foi validado com sucesso!
	</logic:equal>			
	<logic:equal name="state" value="INVALID">
		Não foi possível validar o seu email. Verifique o link que lhe foi enviado. 
		Dispõe de <bean:write name="tries"/> tentativas.
	</logic:equal>
	<logic:equal name="state" value="REFUSED">
		O seu pedido de validação foi recusado. Execedeu o número de tentativas possíveis.
	</logic:equal>
