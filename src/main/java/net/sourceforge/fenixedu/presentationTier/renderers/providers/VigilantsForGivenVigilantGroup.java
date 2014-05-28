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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;

import org.apache.commons.collections.comparators.ComparatorChain;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class VigilantsForGivenVigilantGroup implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        VigilantGroupBean bean = (VigilantGroupBean) source;
        VigilantGroup vigilantGroup = bean.getSelectedVigilantGroup();
        List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();

        if (source instanceof ConvokeBean) {
            ConvokeBean convokeBean = (ConvokeBean) bean;
            vigilants.addAll(convokeBean.getVigilantsSugestion());
            WrittenEvaluation evaluation = convokeBean.getWrittenEvaluation();
            if (evaluation != null && evaluation.getVigilancies().size() > 0) {
                for (Vigilancy convoke : evaluation.getVigilancies()) {
                    vigilants.remove(convoke.getVigilantWrapper());
                }
            }
        } else {
            vigilants.addAll(vigilantGroup.getVigilantWrappers());
            ComparatorChain chain = new ComparatorChain();
            chain.addComparator(VigilantWrapper.CATEGORY_COMPARATOR);
            chain.addComparator(VigilantWrapper.USERNAME_COMPARATOR);
            Collections.sort(vigilants, chain);
        }

        return vigilants;

    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}