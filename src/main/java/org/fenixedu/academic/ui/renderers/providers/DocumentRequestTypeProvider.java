/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.IDocumentRequestBean;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DocumentRequestTypeProvider implements DataProvider {

    static public class PreBolonhaTypes extends DocumentRequestTypeProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            return super.provide(source, currentValue, false, true);
        }
    }

    static public class QuickDeliveryTypes extends DocumentRequestTypeProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            return super.provide(source, currentValue, true, false);
        }
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        return provide(source, currentValue, false, false);
    }

    public Object provide(Object source, Object currentValue, boolean includeQuickDeliveryTypes, boolean includePreBolonhaTypes) {

        IDocumentRequestBean bean = (IDocumentRequestBean) source;
        final Collection<DocumentRequestType> result = new ArrayList<DocumentRequestType>();

        for (final DocumentRequestType documentRequestType : DocumentRequestType.values()) {
            if (includeQuickDeliveryTypes != documentRequestType.isAllowedToQuickDeliver()) {
                continue;
            }
            if (!includePreBolonhaTypes && documentRequestType.isPreBolonha()) {
                continue;
            }
            if (documentRequestType.isBolonhaOnly() && bean.hasRegistration() && !bean.getRegistration().isBolonha()) {
                continue;
            }

            if (documentRequestType.equals(DocumentRequestType.APPROVEMENT_CERTIFICATE)) {
                continue;
            }

            result.add(documentRequestType);
        }

        return result;
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

}
