/**
 * Sep 29, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class SearchPartyCarPlate extends Service {

    public List<Party> run(String nameSearch, String carPlateNumber) {
        List<Party> result = new ArrayList<Party>();

        if (!StringUtils.isEmpty(carPlateNumber) || !StringUtils.isEmpty(nameSearch)) {
            List<ParkingParty> parkingParties = rootDomainObject.getParkingParties();
            for (ParkingParty parkingParty : parkingParties) {
                if (parkingParty.getParty() != null) {
                    if (!StringUtils.isEmpty(carPlateNumber)) {
                        if (parkingParty.hasVehicleContainingPlateNumber(carPlateNumber.trim())) {
                            if (StringUtils.isEmpty(nameSearch)) {
                                result.add(parkingParty.getParty());
                            } else {
                                String[] nameValues = StringNormalizer.normalize(nameSearch)
                                        .toLowerCase().split("\\p{Space}+");
                                if (areNamesPresent(parkingParty.getParty().getName(), nameValues)) {
                                    result.add(parkingParty.getParty());
                                }
                            }
                        }
                    } else { // filled in the name and not the plate number
                        String[] nameValues = StringNormalizer.normalize(nameSearch).toLowerCase()
                                .split("\\p{Space}+");
                        if (areNamesPresent(parkingParty.getParty().getName(), nameValues)) {
                            result.add(parkingParty.getParty());
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean areNamesPresent(String name, String[] searchNameParts) {
        String nameNormalized = StringNormalizer.normalize(name).toLowerCase();
        for (int i = 0; i < searchNameParts.length; i++) {
            String namePart = searchNameParts[i];
            if (!nameNormalized.contains(namePart)) {
                return false;
            }
        }
        return true;
    }

}
