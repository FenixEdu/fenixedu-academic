package net.sourceforge.fenixedu.domain.parking;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
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
        setPhone(((Person) parkingParty.getParty()).getWorkPhone());
        setMobile(((Person) parkingParty.getParty()).getMobile());
        setEmail(((Person) parkingParty.getParty()).getEmail());
        setFirstRequest(true);
    }

    public ParkingRequest(ParkingRequestFactoryCreator creator) {
        setRootDomainObject(RootDomainObject.getInstance());
        setParkingRequestState(ParkingRequestState.PENDING);
        setParkingParty(creator.getParkingParty());
        setCreationDate(new DateTime());
        setPhone(((Person) creator.getParkingParty().getParty()).getWorkPhone());
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
        private transient InputStream driverLicenseInputStream;
        private String firstCarPropertyRegistryFileName;
        private transient InputStream firstCarPropertyRegistryInputStream;
        private String firstCarOwnerIdFileName;
        private transient InputStream firstCarOwnerIdInputStream;
        private String firstDeclarationAuthorizationFileName;
        private transient InputStream firstDeclarationAuthorizationInputStream;
        private String firstInsuranceFileName;
        private transient InputStream firstInsuranceInputStream;
        private String secondCarPropertyRegistryFileName;
        private transient InputStream secondCarPropertyRegistryInputStream;
        private String secondCarOwnerIdFileName;
        private transient InputStream secondCarOwnerIdInputStream;
        private String secondDeclarationAuthorizationFileName;
        private transient InputStream secondDeclarationAuthorizationInputStream;
        private String secondInsuranceFileName;
        private transient InputStream secondInsuranceInputStream;
        private long driverLicenseFileSize;
        private long firstCarPropertyRegistryFileSize;
        private long firstInsuranceFileSize;
        private long firstCarOwnerIdFileSize;
        private long firstDeclarationAuthorizationFileSize;
        private long secondCarPropertyRegistryFileSize;
        private long secondInsuranceFileSize;
        private long secondCarOwnerIdFileSize;
        private long secondDeclarationAuthorizationFileSize;

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

        public String getFirstDeclarationAuthorizationFileName() {
            return firstDeclarationAuthorizationFileName;
        }

        public void setFirstDeclarationAuthorizationFileName(String firstDeclarationAuthorizationFileName) {
            this.firstDeclarationAuthorizationFileName = firstDeclarationAuthorizationFileName;
        }

        public InputStream getFirstDeclarationAuthorizationInputStream() {
            return firstDeclarationAuthorizationInputStream;
        }

        public void setFirstDeclarationAuthorizationInputStream(
                InputStream firstDeclarationAuthorizationInputStream) {
            this.firstDeclarationAuthorizationInputStream = firstDeclarationAuthorizationInputStream;
        }

        public String getSecondDeclarationAuthorizationFileName() {
            return secondDeclarationAuthorizationFileName;
        }

        public void setSecondDeclarationAuthorizationFileName(
                String secondDeclarationAuthorizationFileName) {
            this.secondDeclarationAuthorizationFileName = secondDeclarationAuthorizationFileName;
        }

        public InputStream getSecondDeclarationAuthorizationInputStream() {
            return secondDeclarationAuthorizationInputStream;
        }

        public void setSecondDeclarationAuthorizationInputStream(
                InputStream secondDeclarationAuthorizationInputStream) {
            this.secondDeclarationAuthorizationInputStream = secondDeclarationAuthorizationInputStream;
        }

        public String getFirstInsuranceFileName() {
            return firstInsuranceFileName;
        }

        public void setFirstInsuranceFileName(String firstInsuranceFileName) {
            this.firstInsuranceFileName = firstInsuranceFileName;
        }

        public InputStream getFirstInsuranceInputStream() {
            return firstInsuranceInputStream;
        }

        public void setFirstInsuranceInputStream(InputStream firstInsuranceInputStream) {
            this.firstInsuranceInputStream = firstInsuranceInputStream;
        }

        public String getSecondInsuranceFileName() {
            return secondInsuranceFileName;
        }

        public void setSecondInsuranceFileName(String secondInsuranceFileName) {
            this.secondInsuranceFileName = secondInsuranceFileName;
        }

        public InputStream getSecondInsuranceInputStream() {
            return secondInsuranceInputStream;
        }

        public void setSecondInsuranceInputStream(InputStream secondInsuranceInputStream) {
            this.secondInsuranceInputStream = secondInsuranceInputStream;
        }

        protected void writeParkingFiles(ParkingRequest parkingRequest, FilePath filePath) {                      
            writeParkingFile(parkingRequest, filePath, getDriverLicenseFileName(), ParkingDocumentType.DRIVER_LICENSE,
                    getDriverLicenseInputStream());
            writeParkingFile(parkingRequest, filePath, getFirstCarOwnerIdFileName(),
                    ParkingDocumentType.FIRST_CAR_OWNER_ID, getFirstCarOwnerIdInputStream());
            writeParkingFile(parkingRequest, filePath, getFirstCarPropertyRegistryFileName(),
                    ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER,
                    getFirstCarPropertyRegistryInputStream());
            writeParkingFile(parkingRequest, filePath, getFirstDeclarationAuthorizationFileName(),
                    ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION,
                    getFirstDeclarationAuthorizationInputStream());
            writeParkingFile(parkingRequest, filePath, getFirstInsuranceFileName(),
                    ParkingDocumentType.FIRST_CAR_INSURANCE, getFirstInsuranceInputStream());
            writeParkingFile(parkingRequest, filePath, getSecondCarOwnerIdFileName(),
                    ParkingDocumentType.SECOND_CAR_OWNER_ID, getSecondCarOwnerIdInputStream());
            writeParkingFile(parkingRequest, filePath, getSecondCarPropertyRegistryFileName(),
                    ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER,
                    getSecondCarPropertyRegistryInputStream());
            writeParkingFile(parkingRequest, filePath, getSecondDeclarationAuthorizationFileName(),
                    ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION,
                    getSecondDeclarationAuthorizationInputStream());
            writeParkingFile(parkingRequest, filePath, getSecondInsuranceFileName(),
                    ParkingDocumentType.SECOND_CAR_INSURANCE, getSecondInsuranceInputStream());        
        }

        protected void writeParkingFile(ParkingRequest parkingRequest, FilePath filePath,
                String filename, ParkingDocumentType documentType, InputStream inputStream) {            
            String externalIdentifier = parkingRequest.getParkingFileExternalIdentifier(documentType);
            if(externalIdentifier != null){
                FileManagerFactory.getFileManager().deleteFile(externalIdentifier);
            }
            if(inputStream != null){
                final Group group = getGroup(parkingRequest.getParkingParty().getParty());
                final FileMetadata fileMetadata = new FileMetadata(filename, getParkingParty().getParty()
                        .getName());
                final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(filePath,
                        filename, true, fileMetadata, inputStream);
    
                final ParkingFile parkingFile = new ParkingFile(filename, filename, fileDescriptor
                        .getMimeType(), fileDescriptor.getChecksum(), fileDescriptor.getChecksumAlgorithm(),
                        fileDescriptor.getSize(), fileDescriptor.getUniqueId(), group);
    
                new ParkingDocument(documentType, parkingFile, parkingRequest);
            }
        }

        private Group getGroup(Party party) {
            final PersonGroup personGroup = new PersonGroup((Person) party);
            final RoleGroup roleGroup = new RoleGroup(Role.getRoleByRoleType(RoleType.PARKING_MANAGER));
            return new GroupUnion(personGroup, roleGroup);
        }

        protected FilePath getFilePath(final Integer requestID) {
            Party party = getParkingParty().getParty();
            final FilePath filePath = new FilePath();

            filePath.addNode(new Node("ParkingFiles", "Parking Files"));

            filePath.addNode(new Node("Party" + party.getIdInternal(), party.getName()));
            filePath.addNode(new Node("PR" + requestID, "Parking Request ID"));

            return filePath;
        }

        public long getDriverLicenseFileSize() {
            return driverLicenseFileSize;
        }

        public void setDriverLicenseFileSize(long driverLicenseFileSize) {
            this.driverLicenseFileSize = driverLicenseFileSize;
        }

        public long getFirstCarOwnerIdFileSize() {
            return firstCarOwnerIdFileSize;
        }

        public void setFirstCarOwnerIdFileSize(long firstCarOwnerIdFileSize) {
            this.firstCarOwnerIdFileSize = firstCarOwnerIdFileSize;
        }

        public long getFirstCarPropertyRegistryFileSize() {
            return firstCarPropertyRegistryFileSize;
        }

        public void setFirstCarPropertyRegistryFileSize(long firstCarPropertyRegistryFileSize) {
            this.firstCarPropertyRegistryFileSize = firstCarPropertyRegistryFileSize;
        }

        public long getFirstDeclarationAuthorizationFileSize() {
            return firstDeclarationAuthorizationFileSize;
        }

        public void setFirstDeclarationAuthorizationFileSize(long firstDeclarationAuthorizationFileSize) {
            this.firstDeclarationAuthorizationFileSize = firstDeclarationAuthorizationFileSize;
        }

        public long getFirstInsuranceFileSize() {
            return firstInsuranceFileSize;
        }

        public void setFirstInsuranceFileSize(long firstInsuranceFileSize) {
            this.firstInsuranceFileSize = firstInsuranceFileSize;
        }

        public long getSecondCarOwnerIdFileSize() {
            return secondCarOwnerIdFileSize;
        }

        public void setSecondCarOwnerIdFileSize(long secondCarOwnerIdFileSize) {
            this.secondCarOwnerIdFileSize = secondCarOwnerIdFileSize;
        }

        public long getSecondCarPropertyRegistryFileSize() {
            return secondCarPropertyRegistryFileSize;
        }

        public void setSecondCarPropertyRegistryFileSize(long secondCarPropertyRegistryFileSize) {
            this.secondCarPropertyRegistryFileSize = secondCarPropertyRegistryFileSize;
        }

        public long getSecondDeclarationAuthorizationFileSize() {
            return secondDeclarationAuthorizationFileSize;
        }

        public void setSecondDeclarationAuthorizationFileSize(long secondDeclarationAuthorizationFileSize) {
            this.secondDeclarationAuthorizationFileSize = secondDeclarationAuthorizationFileSize;
        }

        public long getSecondInsuranceFileSize() {
            return secondInsuranceFileSize;
        }

        public void setSecondInsuranceFileSize(long secondInsuranceFileSize) {
            this.secondInsuranceFileSize = secondInsuranceFileSize;
        }
    }

    public static class ParkingRequestFactoryCreator extends ParkingRequestFactory {

        public ParkingRequestFactoryCreator(ParkingParty parkingParty) {
            super(parkingParty);
        }

        public ParkingRequest execute() {
            ParkingRequest parkingRequest = new ParkingRequest(this);
            FilePath filePath = getFilePath(parkingRequest.getIdInternal());

            writeParkingFiles(parkingRequest, filePath);
            return parkingRequest;
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
            setDriverLicenseFileName(parkingRequest.getDriverLicenseFileName());
            setFirstCarOwnerIdFileName(parkingRequest.getFirstCarOwnerIdFileName());
            setFirstCarPropertyRegistryFileName(parkingRequest.getFirstCarPropertyRegistryFileName());
            setFirstDeclarationAuthorizationFileName(parkingRequest.getFirstDeclarationAuthorizationFileName());
            setFirstInsuranceFileName(parkingRequest.getFirstCarInsuranceFileName());
            setSecondCarOwnerIdFileName(parkingRequest.getSecondCarOwnerIdFileName());
            setSecondCarPropertyRegistryFileName(parkingRequest.getSecondCarPropertyRegistryFileName());
            setSecondDeclarationAuthorizationFileName(parkingRequest.getSecondDeclarationAuthorizationFileName());
            setSecondInsuranceFileName(parkingRequest.getSecondCarInsuranceFileName());
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
            ParkingRequest parkingRequest = getParkingRequest();
            parkingRequest.edit(this);
            FilePath filePath = getFilePath(parkingRequest.getIdInternal());
            writeParkingFiles(parkingRequest, filePath);
            return parkingRequest;
        }

    }

    public void edit(ParkingRequestFactoryEditor parkingRequestFactoryEditor) {
        setFirstCarMake(parkingRequestFactoryEditor.getFirstCarMake());
        setFirstCarPlateNumber(parkingRequestFactoryEditor.getFirstCarPlateNumber());
        setSecondCarMake(parkingRequestFactoryEditor.getSecondCarMake());
        setSecondCarPlateNumber(parkingRequestFactoryEditor.getSecondCarPlateNumber());
    }

    public String getDriverLicenseFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.DRIVER_LICENSE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getFirstCarPropertyRegistryFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getFirstCarInsuranceFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.FIRST_CAR_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }
    
    public String getFirstCarOwnerIdFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.FIRST_CAR_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }


    public String getFirstDeclarationAuthorizationFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }
    
    public String getSecondCarPropertyRegistryFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getSecondCarInsuranceFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.SECOND_CAR_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }
    
    public String getSecondCarOwnerIdFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.SECOND_CAR_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }


    public String getSecondDeclarationAuthorizationFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }
    
    public String getParkingFileExternalIdentifier(ParkingDocumentType parkingDocumentType) {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(parkingDocumentType)) {
                return parkingDocument.getParkingFile().getExternalStorageIdentification();
            }
        }
        return null;
    }  

    public boolean getHasFirstCar() {
        return (getFirstCarMake() != null && getFirstCarMake().length() > 0)
            || (getFirstCarPlateNumber() != null && getFirstCarPlateNumber().length() > 0)
            || (getFirstCarPropertyRegistryFileName()  != null && getFirstCarPropertyRegistryFileName().length() > 0)
            || (getFirstCarOwnerIdFileName() != null && getFirstCarOwnerIdFileName().length() > 0)
            || (getFirstDeclarationAuthorizationFileName() != null && getFirstDeclarationAuthorizationFileName().length() > 0);
    }

    public boolean getHasSecondCar() {
        return (getSecondCarMake() != null && getSecondCarMake().length() > 0)
            || (getSecondCarPlateNumber() != null && getSecondCarPlateNumber().length() > 0)
            || (getSecondCarPropertyRegistryFileName()  != null && getSecondCarPropertyRegistryFileName().length() > 0) 
            || (getSecondCarOwnerIdFileName() != null && getSecondCarOwnerIdFileName().length() > 0)
            || (getSecondDeclarationAuthorizationFileName() != null && getSecondDeclarationAuthorizationFileName().length() > 0);
    }

    public boolean getHasDriverLicense() {
        return getDriverLicenseFileName() != null && getDriverLicenseFileName().length() > 0;
    }
    
}
