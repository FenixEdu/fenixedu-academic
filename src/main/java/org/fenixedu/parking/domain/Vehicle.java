/**
 * Copyright © 2014 Instituto Superior Técnico
 *
 * This file is part of Fenix Parking.
 *
 * Fenix Parking is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Fenix Parking is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fenix Parking.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.parking.domain;

import java.util.ResourceBundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.parking.dto.VehicleBean;

public class Vehicle extends Vehicle_Base {

    public Vehicle() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Vehicle(VehicleBean vehicleBean) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setParkingParty(vehicleBean.getParkingParty());
        setVehicleMake(vehicleBean.getVehicleMake());
        setPlateNumber(vehicleBean.getVehiclePlateNumber());
    }

    public Vehicle(Vehicle parkingRequestVehicle) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setVehicleMake(parkingRequestVehicle.getVehicleMake());
        setPlateNumber(parkingRequestVehicle.getPlateNumber());
        setParkingParty(parkingRequestVehicle.getParkingRequest().getParkingParty());
        setPropertyRegistryDeliveryType(parkingRequestVehicle.getPropertyRegistryDeliveryType());
        setInsuranceDeliveryType(parkingRequestVehicle.getInsuranceDeliveryType());
        setOwnerIdDeliveryType(parkingRequestVehicle.getOwnerIdDeliveryType());
        setAuthorizationDeclarationDeliveryType(parkingRequestVehicle.getAuthorizationDeclarationDeliveryType());
        for (NewParkingDocument parkingDocument : parkingRequestVehicle.getNewParkingDocumentsSet()) {
            parkingDocument.copyParkingDocument(this);
        }
    }

    public void edit(VehicleBean vehicleBean) {
        setVehicleMake(vehicleBean.getVehicleMake());
        setPlateNumber(vehicleBean.getVehiclePlateNumber());
    }

    public void edit(Vehicle parkingRequestVehicle) {
        setVehicleMake(parkingRequestVehicle.getVehicleMake());
        setPlateNumber(parkingRequestVehicle.getPlateNumber());
        setParkingParty(parkingRequestVehicle.getParkingRequest().getParkingParty());
        setPropertyRegistryDeliveryType(parkingRequestVehicle.getPropertyRegistryDeliveryType());
        setInsuranceDeliveryType(parkingRequestVehicle.getInsuranceDeliveryType());
        setOwnerIdDeliveryType(parkingRequestVehicle.getOwnerIdDeliveryType());
        setAuthorizationDeclarationDeliveryType(parkingRequestVehicle.getAuthorizationDeclarationDeliveryType());
        for (NewParkingDocument parkingDocument : parkingRequestVehicle.getNewParkingDocumentsSet()) {
            parkingDocument.copyParkingDocument(this);
        }
    }

    public NewParkingDocument getParkingDocument(NewParkingDocumentType parkingDocumentType) {
        for (NewParkingDocument parkingDocument : getNewParkingDocumentsSet()) {
            if (parkingDocument.getParkingDocumentType() == parkingDocumentType) {
                return parkingDocument;
            }
        }
        return null;
    }

    public String getPropertyRegistryFileName() {
        for (NewParkingDocument parkingDocument : getNewParkingDocumentsSet()) {
            if (parkingDocument.getParkingDocumentType().equals(NewParkingDocumentType.VEHICLE_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getInsuranceFileName() {
        for (NewParkingDocument parkingDocument : getNewParkingDocumentsSet()) {
            if (parkingDocument.getParkingDocumentType().equals(NewParkingDocumentType.VEHICLE_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getOwnerIdFileName() {
        for (NewParkingDocument parkingDocument : getNewParkingDocumentsSet()) {
            if (parkingDocument.getParkingDocumentType().equals(NewParkingDocumentType.VEHICLE_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getAuthorizationDeclarationFileName() {
        for (NewParkingDocument parkingDocument : getNewParkingDocumentsSet()) {
            if (parkingDocument.getParkingDocumentType().equals(NewParkingDocumentType.DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getPropertyRegistryFileNameToDisplay() {
        for (NewParkingDocument parkingDocument : getNewParkingDocumentsSet()) {
            if (parkingDocument.getParkingDocumentType().equals(NewParkingDocumentType.VEHICLE_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getPropertyRegistryDeliveryType() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", I18N.getLocale());
            return bundle.getString(getPropertyRegistryDeliveryType().name());
        }
        return "";
    }

    public String getInsuranceFileNameToDisplay() {
        for (NewParkingDocument parkingDocument : getNewParkingDocumentsSet()) {
            if (parkingDocument.getParkingDocumentType().equals(NewParkingDocumentType.VEHICLE_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getInsuranceDeliveryType() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", I18N.getLocale());
            return bundle.getString(getInsuranceDeliveryType().name());
        }
        return "";
    }

    public String getOwnerIdFileNameToDisplay() {
        for (NewParkingDocument parkingDocument : getNewParkingDocumentsSet()) {
            if (parkingDocument.getParkingDocumentType().equals(NewParkingDocumentType.VEHICLE_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getOwnerIdDeliveryType() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", I18N.getLocale());
            return bundle.getString(getOwnerIdDeliveryType().name());
        }
        return "";
    }

    public String getAuthorizationDeclarationFileNameToDisplay() {
        for (NewParkingDocument parkingDocument : getNewParkingDocumentsSet()) {
            if (parkingDocument.getParkingDocumentType().equals(NewParkingDocumentType.DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getAuthorizationDeclarationDeliveryType() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", I18N.getLocale());
            return bundle.getString(getAuthorizationDeclarationDeliveryType().name());
        }
        return "";
    }

    public NewParkingDocument getPropertyRegistryDocument() {
        return getParkingDocument(NewParkingDocumentType.VEHICLE_PROPERTY_REGISTER);
    }

    public NewParkingDocument getInsuranceDocument() {
        return getParkingDocument(NewParkingDocumentType.VEHICLE_INSURANCE);
    }

    public NewParkingDocument getOwnerIdDocument() {
        return getParkingDocument(NewParkingDocumentType.VEHICLE_OWNER_ID);
    }

    public NewParkingDocument getDeclarationDocument() {
        return getParkingDocument(NewParkingDocumentType.DECLARATION_OF_AUTHORIZATION);
    }

    public void deleteUnnecessaryDocuments() {
        NewParkingDocument ownerIdDocument = getOwnerIdDocument();
        if (ownerIdDocument != null) {
            ownerIdDocument.delete();
        }
    }

    public void delete() {
        for (; getNewParkingDocumentsSet().size() != 0; getNewParkingDocumentsSet().iterator().next().delete()) {
            ;
        }
        setVehicleMake(null);
        setPlateNumber(null);
        setAuthorizationDeclarationDeliveryType(null);
        setInsuranceDeliveryType(null);
        setOwnerIdDeliveryType(null);
        setPropertyRegistryDeliveryType(null);
        setParkingParty(null);
        setParkingRequest(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }
}
