package net.sourceforge.fenixedu.domain.parking;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.Node;

public class ParkingRequest extends ParkingRequest_Base {

    public ParkingRequest(ParkingParty parkingParty) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParkingRequestState(ParkingRequestState.PENDING);
        setParkingParty(parkingParty);
        setCreationDate(new DateTime());
        setPhone(((Person) parkingParty.getParty()).getPhone());
        setMobile(((Person) parkingParty.getParty()).getMobile());
        setEmail(((Person) parkingParty.getParty()).getEmail());
        setFirstRequest(true);
    }

    public ParkingRequest(ParkingRequestFactoryCreator creator) {
        setRootDomainObject(RootDomainObject.getInstance());
        setParkingRequestState(ParkingRequestState.PENDING);
        setParkingParty(creator.getParkingParty());
        setCreationDate(new DateTime());
        setPhone(((Person) creator.getParkingParty().getParty()).getPhone());
        setMobile(((Person) creator.getParkingParty().getParty()).getMobile());
        setEmail(((Person) creator.getParkingParty().getParty()).getEmail());
        setFirstRequest(true);
        setFirstCarMake(creator.getFirstCarMake());
        setFirstCarPlateNumber(creator.getFirstCarPlateNumber());
        setSecondCarMake(creator.getSecondCarMake());
        setSecondCarPlateNumber(creator.getSecondCarPlateNumber());
    }

    public ParkingRequestFactoryEditor getParkingRequestFactoryEditor() {
        return new ParkingRequestFactoryEditor(this);
    }    
    
    public static abstract class ParkingRequestFactory implements Serializable, FactoryExecutor {
        private DomainReference<ParkingParty> parkingParty;
        private String firstCarPlateNumber;
        private String firstCarMake;
        private String secondCarPlateNumber;
        private String secondCarMake;
        private String driverLicenseFileName;
        private InputStream driverLicenseInputStream;
        private String firstCarPropertyRegistryFileName;
        private InputStream firstCarPropertyRegistryInputStream;
        private String firstCarOwnerIdFileName;
        private InputStream firstCarOwnerIdInputStream;
        private String secondCarPropertyRegistryFileName;
        private InputStream secondCarPropertyRegistryInputStream;
        private String secondCarOwnerIdFileName;
        private InputStream secondCarOwnerIdInputStream;
        
        public ParkingRequestFactory(ParkingParty parkingParty) {
            super();
            setParkingParty(parkingParty);
        }

        public String getDriverLicenseFileName() {
            return driverLicenseFileName;
        }

        public void setDriverLicenseFileName(String driverLicenseFileName) {
            this.driverLicenseFileName = driverLicenseFileName;
        }

        public InputStream getDriverLicenseInputStream() {
            return driverLicenseInputStream;
        }

        public void setDriverLicenseInputStream(InputStream driverLicenseInputStream) {
            this.driverLicenseInputStream = driverLicenseInputStream;
        }       

        public String getFirstCarMake() {
            return firstCarMake;
        }

        public void setFirstCarMake(String firstCarMake) {
            this.firstCarMake = firstCarMake;
        }

        public String getFirstCarOwnerIdFileName() {
            return firstCarOwnerIdFileName;
        }

        public void setFirstCarOwnerIdFileName(String firstCarOwnerIdFileName) {
            this.firstCarOwnerIdFileName = firstCarOwnerIdFileName;
        }

        public InputStream getFirstCarOwnerIdInputStream() {
            return firstCarOwnerIdInputStream;
        }

        public void setFirstCarOwnerIdInputStream(InputStream firstCarOwnerIdInputStream) {
            this.firstCarOwnerIdInputStream = firstCarOwnerIdInputStream;
        }

        public String getFirstCarPlateNumber() {
            return firstCarPlateNumber;
        }

        public void setFirstCarPlateNumber(String firstCarPlateNumber) {
            this.firstCarPlateNumber = firstCarPlateNumber;
        }

        public String getFirstCarPropertyRegistryFileName() {
            return firstCarPropertyRegistryFileName;
        }

        public void setFirstCarPropertyRegistryFileName(String firstCarPropertyRegistryFileName) {
            this.firstCarPropertyRegistryFileName = firstCarPropertyRegistryFileName;
        }

        public InputStream getFirstCarPropertyRegistryInputStream() {
            return firstCarPropertyRegistryInputStream;
        }

        public void setFirstCarPropertyRegistryInputStream(
                InputStream firstCarPropertyRegistryInputStream) {
            this.firstCarPropertyRegistryInputStream = firstCarPropertyRegistryInputStream;
        }        

        public ParkingParty getParkingParty() {
            return parkingParty == null ? null : parkingParty.getObject();
        }

        public void setParkingParty(ParkingParty parkingParty) {
            if (parkingParty != null) {
                this.parkingParty = new DomainReference<ParkingParty>(parkingParty);
            }
        }        

        public String getSecondCarMake() {
            return secondCarMake;
        }

        public void setSecondCarMake(String secondCarMake) {
            this.secondCarMake = secondCarMake;
        }

        public String getSecondCarOwnerIdFileName() {
            return secondCarOwnerIdFileName;
        }

        public void setSecondCarOwnerIdFileName(String secondCarOwnerIdFileName) {
            this.secondCarOwnerIdFileName = secondCarOwnerIdFileName;
        }

        public InputStream getSecondCarOwnerIdInputStream() {
            return secondCarOwnerIdInputStream;
        }

        public void setSecondCarOwnerIdInputStream(InputStream secondCarOwnerIdInputStream) {
            this.secondCarOwnerIdInputStream = secondCarOwnerIdInputStream;
        }

        public String getSecondCarPlateNumber() {
            return secondCarPlateNumber;
        }

        public void setSecondCarPlateNumber(String secondCarPlateNumber) {
            this.secondCarPlateNumber = secondCarPlateNumber;
        }

        public String getSecondCarPropertyRegistryFileName() {
            return secondCarPropertyRegistryFileName;
        }

        public void setSecondCarPropertyRegistryFileName(String secondCarPropertyRegistryFileName) {
            this.secondCarPropertyRegistryFileName = secondCarPropertyRegistryFileName;
        }

        public InputStream getSecondCarPropertyRegistryInputStream() {
            return secondCarPropertyRegistryInputStream;
        }

        public void setSecondCarPropertyRegistryInputStream(
                InputStream secondCarPropertyRegistryInputStream) {
            this.secondCarPropertyRegistryInputStream = secondCarPropertyRegistryInputStream;
        }
    }

    public static class ParkingRequestFactoryCreator extends ParkingRequestFactory {

        public ParkingRequestFactoryCreator(ParkingParty parkingParty) {
            super(parkingParty);
        }

        public ParkingRequest execute() {
            ParkingRequest parkingRequest = new ParkingRequest(this);
            FilePath filePath = getFilePath(parkingRequest.getIdInternal());
            
            if (getDriverLicenseInputStream() != null) {
                String filename = getDriverLicenseFileName();
                final FileMetadata fileMetadata = new FileMetadata(filename, getParkingParty().getParty().getName());
                
                final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
                        filePath, filename, true, fileMetadata, getDriverLicenseInputStream());

                final ParkingFile parkingFile = new ParkingFile(filename,
                        filename, fileDescriptor.getMimeType(), fileDescriptor.getChecksum(),
                        fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor
                                .getUniqueId(), null);
                
                new ParkingDocument(ParkingDocumentType.DRIVER_LICENSE,parkingFile,parkingRequest);
            }
            return parkingRequest;
        }
        
        private FilePath getFilePath(final Integer requestID) {
            Party party = getParkingParty().getParty();
            final FilePath filePath = new FilePath();

            filePath.addNode(new Node("ParkingFiles", "Parking Files"));

            filePath.addNode(new Node("Party" + party.getIdInternal(), party.getName()));
            filePath.addNode(new Node("PR" + requestID, "Parking Request ID"));

            return filePath;
        }
    }

    public static class ParkingRequestFactoryEditor extends ParkingRequestFactory {
        private DomainReference<ParkingRequest> parkingRequest;

        public ParkingRequestFactoryEditor(ParkingParty parkingParty) {
            super(parkingParty);
        }

        public ParkingRequestFactoryEditor(ParkingRequest parkingRequest) {
            super(parkingRequest.getParkingParty());
            setParkingRequest(parkingRequest);
            setFirstCarMake(parkingRequest.getFirstCarMake());
            setFirstCarPlateNumber(parkingRequest.getFirstCarPlateNumber());
            setSecondCarMake(parkingRequest.getSecondCarMake());
            setSecondCarPlateNumber(parkingRequest.getSecondCarPlateNumber());            
        }

        public ParkingRequest getParkingRequest() {
            return parkingRequest == null ? null : parkingRequest.getObject();
        }

        public void setParkingRequest(ParkingRequest parkingRequest) {
            if (parkingRequest != null) {
                this.parkingRequest = new DomainReference<ParkingRequest>(parkingRequest);
            }
        }

        public ParkingRequest execute() {
            getParkingRequest().edit(this);
            return null;
        }
    }

    public void edit(ParkingRequestFactoryEditor parkingRequestFactoryEditor) {
        setFirstCarMake(parkingRequestFactoryEditor.getFirstCarMake());
        setFirstCarPlateNumber(parkingRequestFactoryEditor.getFirstCarPlateNumber());
        setSecondCarMake(parkingRequestFactoryEditor.getSecondCarMake());
        setSecondCarPlateNumber(parkingRequestFactoryEditor.getSecondCarPlateNumber());
    }

    public String getDriverLicenseFileName() {
        for(ParkingDocument parkingDocument : getParkingDocuments()){
            if(parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.DRIVER_LICENSE)){
                return "driverLicense";
            }
        }
        return "";
    }

    public String getFirstCarPropertyRegistryFileName() {
        for(ParkingDocument parkingDocument : getParkingDocuments()){
            if(parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER)){
                return "firstCarPropertyRegistry";
            }
        }
        return "";
    }

    public String getFirstCarOwnerIdFileName() {
        for(ParkingDocument parkingDocument : getParkingDocuments()){
            if(parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.FIRST_CAR_OWNER_ID)){
                return "firstCarOwnerId";
            }
        }
        return "";
    }

    public String getSecondCarPropertyRegistryFileName() {
        for(ParkingDocument parkingDocument : getParkingDocuments()){
            if(parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER)){
                return "secondCarPropertyRegistry";
            }
        }
        return "";
    }

    public String getSecondCarOwnerIdFileName() {
        for(ParkingDocument parkingDocument : getParkingDocuments()){
            if(parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.SECOND_CAR_OWNER_ID)){
                return "secondCarOwnerId";
            }
        }
        return "";
    }
}
