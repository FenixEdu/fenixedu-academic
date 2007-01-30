package net.sourceforge.fenixedu.domain.parking;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.parking.VehicleBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.LanguageUtils;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public class Vehicle extends Vehicle_Base {

    public Vehicle() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Vehicle(VehicleBean vehicleBean) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParkingParty(vehicleBean.getParkingParty());
        setVehicleMake(vehicleBean.getVehicleMake());
        setPlateNumber(vehicleBean.getVehiclePlateNumber());
    }

    public Vehicle(Vehicle parkingRequestVehicle) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setVehicleMake(parkingRequestVehicle.getVehicleMake());
        setPlateNumber(parkingRequestVehicle.getPlateNumber());
        setParkingParty(parkingRequestVehicle.getParkingRequest().getParkingParty());
        setPropertyRegistryDeliveryType(parkingRequestVehicle.getPropertyRegistryDeliveryType());
        setInsuranceDeliveryType(parkingRequestVehicle.getInsuranceDeliveryType());
        setOwnerIdDeliveryType(parkingRequestVehicle.getOwnerIdDeliveryType());
        setAuthorizationDeclarationDeliveryType(parkingRequestVehicle.getAuthorizationDeclarationDeliveryType());
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
    }
    
    public NewParkingDocument getParkingDocument(NewParkingDocumentType parkingDocumentType){
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if(parkingDocument.getParkingDocumentType() == parkingDocumentType){
                return parkingDocument;
            }
        }
        return null;
    }
 
    public String getPropertyRegistryFileName() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    NewParkingDocumentType.VEHICLE_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }        
        return null;
    }
    
    public String getInsuranceFileName() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    NewParkingDocumentType.VEHICLE_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }        
        return null;
    }
    
    public String getOwnerIdFileName() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    NewParkingDocumentType.VEHICLE_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }       
        return null;
    }
    
    public String getAuthorizationDeclarationFileName() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    NewParkingDocumentType.DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }       
        return null;
    }
    
    public String getPropertyRegistryFileNameToDisplay() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    NewParkingDocumentType.VEHICLE_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getPropertyRegistryDeliveryType() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getPropertyRegistryDeliveryType().name());
        }
        return "";
    }
    
    public String getInsuranceFileNameToDisplay() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    NewParkingDocumentType.VEHICLE_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getInsuranceDeliveryType() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getInsuranceDeliveryType().name());
        }
        return "";
    }
    
    public String getOwnerIdFileNameToDisplay() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    NewParkingDocumentType.VEHICLE_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getOwnerIdDeliveryType() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getOwnerIdDeliveryType().name());
        }
        return "";
    }
    
    public String getAuthorizationDeclarationFileNameToDisplay() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    NewParkingDocumentType.DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getAuthorizationDeclarationDeliveryType() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getAuthorizationDeclarationDeliveryType().name());
        }
        return "";
    }
    
    public String getPropertyRegistryDocumentLink() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType() == NewParkingDocumentType.VEHICLE_PROPERTY_REGISTER) {
                ParkingFile parkingFile = parkingDocument.getParkingFile();
                return FileManagerFactory.getFactoryInstance().getFileManager().formatDownloadUrl(
                        parkingFile.getExternalStorageIdentification(), parkingFile.getFilename());
            }
        }
        return "";
    }
    
    public String getInsuranceDocumentLink() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType() == NewParkingDocumentType.VEHICLE_INSURANCE) {
                ParkingFile parkingFile = parkingDocument.getParkingFile();
                return FileManagerFactory.getFactoryInstance().getFileManager().formatDownloadUrl(
                        parkingFile.getExternalStorageIdentification(), parkingFile.getFilename());
            }
        }
        return "";
    }

    public String getOwnerIdDocumentLink() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType() == NewParkingDocumentType.VEHICLE_OWNER_ID) {
                ParkingFile parkingFile = parkingDocument.getParkingFile();
                return FileManagerFactory.getFactoryInstance().getFileManager().formatDownloadUrl(
                        parkingFile.getExternalStorageIdentification(), parkingFile.getFilename());
            }
        }
        return "";
    }
    
    public String getDeclarationDocumentLink() {
        for (NewParkingDocument parkingDocument : getNewParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType() == NewParkingDocumentType.DECLARATION_OF_AUTHORIZATION) {
                ParkingFile parkingFile = parkingDocument.getParkingFile();
                return FileManagerFactory.getFactoryInstance().getFileManager().formatDownloadUrl(
                        parkingFile.getExternalStorageIdentification(), parkingFile.getFilename());
            }
        }
        return "";
    }
    
    public void deleteDocuments(){
        for (; getNewParkingDocuments().size() != 0;getNewParkingDocuments().get(0).delete());
    }
    
    public void delete() {
        deleteDocuments();
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
