package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadBuildings;
import net.sourceforge.fenixedu.dataTransferObject.InfoBuilding;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.SpaceClassification;

public class Util {

    public static List<LabelValueBean> readExistingBuldings(String name, String value) throws FenixServiceException {
        List<LabelValueBean> edificios = new ArrayList<LabelValueBean>();

        if (name != null) {
            edificios.add(new LabelValueBean(name, value));
        }

        final List<InfoBuilding> infoBuildings = ReadBuildings.run();
        Collections.sort(infoBuildings, new BeanComparator("name"));

        for (InfoBuilding infoBuilding : infoBuildings) {
            edificios.add(new LabelValueBean(infoBuilding.getName(), infoBuilding.getName()));
        }

        return edificios;
    }

    public static List<LabelValueBean> readTypesOfRooms(String name, String value) {
        List<LabelValueBean> tipos = new ArrayList<LabelValueBean>();

        if (name != null) {
            tipos.add(new LabelValueBean(name, value));
        }

        Collection<SpaceClassification> roomClassifications = Bennu.getInstance().getRootClassificationSet();
        for (SpaceClassification classification : SpaceUtils.sortByRoomClassificationAndCode(roomClassifications)) {
            if (classification.getParent() != null) {
                tipos.add(new LabelValueBean(classification.getAbsoluteCode() + " - " + classification.getName().getContent(),
                        classification.getExternalId().toString()));
            }
        }

        return tipos;
    }
}