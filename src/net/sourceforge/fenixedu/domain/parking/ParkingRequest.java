package net.sourceforge.fenixedu.domain.parking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ResourceBundle;

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
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.Node;
import pt.utl.ist.fenix.tools.util.FileUtils;

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
        setDriverLicenseDocumentState(creator.getDriverLicenseDocumentState());
        setFirstCarDeclarationDocumentState(creator.getFirstCarDeclarationDocumentState());
        setFirstCarInsuranceDocumentState(creator.getFirstCarInsuranceDocumentState());
        setFirstCarOwnerIdDocumentState(creator.getFirstCarOwnerIdDocumentState());
        setFirstCarPropertyRegistryDocumentState(creator.getFirstCarPropertyRegistryDocumentState());
        setSecondCarDeclarationDocumentState(creator.getSecondCarDeclarationDocumentState());
        setSecondCarInsuranceDocumentState(creator.getSecondCarInsuranceDocumentState());
        setSecondCarOwnerIdDocumentState(creator.getSecondCarOwnerIdDocumentState());
        setSecondCarPropertyRegistryDocumentState(creator.getSecondCarPropertyRegistryDocumentState());
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

        private byte[] driverLicenseByteArray;

        private String firstCarPropertyRegistryFileName;

        private transient InputStream firstCarPropertyRegistryInputStream;

        private byte[] firstCarPropertyRegistryByteArray;

        private String firstCarOwnerIdFileName;

        private transient InputStream firstCarOwnerIdInputStream;

        private byte[] firstCarOwnerIdByteArray;

        private String firstDeclarationAuthorizationFileName;

        private transient InputStream firstDeclarationAuthorizationInputStream;

        private byte[] firstDeclarationAuthorizationByteArray;

        private String firstInsuranceFileName;

        private transient InputStream firstInsuranceInputStream;

        private byte[] firstInsuranceByteArray;

        private String secondCarPropertyRegistryFileName;

        private transient InputStream secondCarPropertyRegistryInputStream;

        private byte[] secondCarPropertyRegistryByteArray;

        private String secondCarOwnerIdFileName;

        private transient InputStream secondCarOwnerIdInputStream;

        private byte[] secondCarOwnerIdByteArray;

        private String secondDeclarationAuthorizationFileName;

        private transient InputStream secondDeclarationAuthorizationInputStream;

        private byte[] secondDeclarationAuthorizationByteArray;

        private String secondInsuranceFileName;

        private transient InputStream secondInsuranceInputStream;

        private byte[] secondInsuranceByteArray;

        private long driverLicenseFileSize;

        private long firstCarPropertyRegistryFileSize;

        private long firstInsuranceFileSize;

        private long firstCarOwnerIdFileSize;

        private long firstDeclarationAuthorizationFileSize;

        private long secondCarPropertyRegistryFileSize;

        private long secondInsuranceFileSize;

        private long secondCarOwnerIdFileSize;

        private long secondDeclarationAuthorizationFileSize;

        ParkingDocumentState driverLicenseDocumentState;

        ParkingDocumentState firstCarPropertyRegistryDocumentState;

        ParkingDocumentState firstCarInsuranceDocumentState;

        ParkingDocumentState firstCarOwnerIdDocumentState;

        ParkingDocumentState firstCarDeclarationDocumentState;

        ParkingDocumentState secondCarPropertyRegistryDocumentState;

        ParkingDocumentState secondCarInsuranceDocumentState;

        ParkingDocumentState secondCarOwnerIdDocumentState;

        ParkingDocumentState secondCarDeclarationDocumentState;

        public ParkingRequestFactory(ParkingParty parkingParty) {
            super();
            setParkingParty(parkingParty);
        }

        public void saveInputStreams() {
            driverLicenseByteArray = getByteArray(driverLicenseInputStream, driverLicenseByteArray);
            firstCarPropertyRegistryByteArray = getByteArray(firstCarPropertyRegistryInputStream,
                    firstCarPropertyRegistryByteArray);
            firstCarOwnerIdByteArray = getByteArray(firstCarOwnerIdInputStream, firstCarOwnerIdByteArray);
            firstDeclarationAuthorizationByteArray = getByteArray(
                    firstDeclarationAuthorizationInputStream, firstDeclarationAuthorizationByteArray);
            firstInsuranceByteArray = getByteArray(firstInsuranceInputStream, firstInsuranceByteArray);
            secondCarPropertyRegistryByteArray = getByteArray(secondCarPropertyRegistryInputStream,
                    secondCarPropertyRegistryByteArray);
            secondCarOwnerIdByteArray = getByteArray(secondCarOwnerIdInputStream,
                    secondCarOwnerIdByteArray);
            secondDeclarationAuthorizationByteArray = getByteArray(
                    secondDeclarationAuthorizationInputStream, secondDeclarationAuthorizationByteArray);
            secondInsuranceByteArray = getByteArray(secondInsuranceInputStream, secondInsuranceByteArray);
        }

        private byte[] getByteArray(final InputStream inputStream, byte[] b) {
            if (inputStream == null)
                return b;
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                try {
                    FileUtils.copy(inputStream, byteArrayOutputStream);
                    byteArrayOutputStream.flush();
                    b = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    inputStream.close();
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return b;
        }

        public String getDriverLicenseFileName() {
            return driverLicenseFileName;
        }

        public void setDriverLicenseFileName(String driverLicenseFileName) {
            this.driverLicenseFileName = driverLicenseFileName;
        }

        public InputStream getDriverLicenseInputStream() {
            return driverLicenseByteArray == null ? null : new ByteArrayInputStream(
                    driverLicenseByteArray);
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
            return firstCarOwnerIdByteArray == null ? null : new ByteArrayInputStream(
                    firstCarOwnerIdByteArray);
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
            return firstCarPropertyRegistryByteArray == null ? null : new ByteArrayInputStream(
                    firstCarPropertyRegistryByteArray);
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
            return secondCarOwnerIdByteArray == null ? null : new ByteArrayInputStream(
                    secondCarOwnerIdByteArray);
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
            return secondCarPropertyRegistryByteArray == null ? null : new ByteArrayInputStream(
                    secondCarPropertyRegistryByteArray);
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
            return firstDeclarationAuthorizationByteArray == null ? null : new ByteArrayInputStream(
                    firstDeclarationAuthorizationByteArray);
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
            return secondDeclarationAuthorizationByteArray == null ? null : new ByteArrayInputStream(
                    secondDeclarationAuthorizationByteArray);
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
            return firstInsuranceByteArray == null ? null : new ByteArrayInputStream(
                    firstInsuranceByteArray);
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
            return secondInsuranceByteArray == null ? null : new ByteArrayInputStream(
                    secondInsuranceByteArray);
        }

        public void setSecondInsuranceInputStream(InputStream secondInsuranceInputStream) {
            this.secondInsuranceInputStream = secondInsuranceInputStream;
        }

        protected void writeParkingFiles(ParkingRequest parkingRequest, FilePath filePath) {
            writeParkingFile(parkingRequest, filePath, getDriverLicenseFileName(),
                    ParkingDocumentType.DRIVER_LICENSE, getDriverLicenseInputStream(),
                    getDriverLicenseDocumentState());
            writeParkingFile(parkingRequest, filePath, getFirstCarOwnerIdFileName(),
                    ParkingDocumentType.FIRST_CAR_OWNER_ID, getFirstCarOwnerIdInputStream(),
                    getFirstCarOwnerIdDocumentState());
            writeParkingFile(parkingRequest, filePath, getFirstCarPropertyRegistryFileName(),
                    ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER,
                    getFirstCarPropertyRegistryInputStream(), getFirstCarPropertyRegistryDocumentState());
            writeParkingFile(parkingRequest, filePath, getFirstDeclarationAuthorizationFileName(),
                    ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION,
                    getFirstDeclarationAuthorizationInputStream(), getFirstCarDeclarationDocumentState());
            writeParkingFile(parkingRequest, filePath, getFirstInsuranceFileName(),
                    ParkingDocumentType.FIRST_CAR_INSURANCE, getFirstInsuranceInputStream(),
                    getFirstCarInsuranceDocumentState());
            writeParkingFile(parkingRequest, filePath, getSecondCarOwnerIdFileName(),
                    ParkingDocumentType.SECOND_CAR_OWNER_ID, getSecondCarOwnerIdInputStream(),
                    getSecondCarOwnerIdDocumentState());
            writeParkingFile(parkingRequest, filePath, getSecondCarPropertyRegistryFileName(),
                    ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER,
                    getSecondCarPropertyRegistryInputStream(),
                    getSecondCarPropertyRegistryDocumentState());
            writeParkingFile(parkingRequest, filePath, getSecondDeclarationAuthorizationFileName(),
                    ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION,
                    getSecondDeclarationAuthorizationInputStream(),
                    getSecondCarDeclarationDocumentState());
            writeParkingFile(parkingRequest, filePath, getSecondInsuranceFileName(),
                    ParkingDocumentType.SECOND_CAR_INSURANCE, getSecondInsuranceInputStream(),
                    getSecondCarInsuranceDocumentState());
        }

        protected void writeParkingFile(ParkingRequest parkingRequest, FilePath filePath,
                String filename, ParkingDocumentType documentType, InputStream inputStream,
                ParkingDocumentState documentState) {

            ParkingDocument parkingDocument = parkingRequest.getParkingDocument(documentType);
            if (parkingDocument != null
                    && (inputStream != null || documentState != ParkingDocumentState.ELECTRONIC_DELIVERY)) {
                String externalIdentifier = parkingDocument.getParkingFile()
                        .getExternalStorageIdentification();
                parkingDocument.delete();
                if (externalIdentifier != null) {
                    FileManagerFactory.getFileManager().deleteFile(externalIdentifier);
                }
            }
            if (documentState == ParkingDocumentState.ELECTRONIC_DELIVERY && inputStream != null) {
                final Group group = getGroup(parkingRequest.getParkingParty().getParty());
                final FileMetadata fileMetadata = new FileMetadata(filename, getParkingParty()
                        .getParty().getName());
                final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
                        filePath, filename, true, fileMetadata, inputStream);

                final ParkingFile parkingFile = new ParkingFile(filename, filename, fileDescriptor
                        .getMimeType(), fileDescriptor.getChecksum(), fileDescriptor
                        .getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor.getUniqueId(),
                        group);

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

        public ParkingDocumentState getDriverLicenseDocumentState() {
            return driverLicenseDocumentState;
        }

        public void setDriverLicenseDocumentState(ParkingDocumentState driverLicenseDocumentState) {
            this.driverLicenseDocumentState = driverLicenseDocumentState;
        }

        public ParkingDocumentState getFirstCarDeclarationDocumentState() {
            return firstCarDeclarationDocumentState;
        }

        public void setFirstCarDeclarationDocumentState(
                ParkingDocumentState firstCarDeclarationDocumentState) {
            this.firstCarDeclarationDocumentState = firstCarDeclarationDocumentState;
        }

        public ParkingDocumentState getFirstCarInsuranceDocumentState() {
            return firstCarInsuranceDocumentState;
        }

        public void setFirstCarInsuranceDocumentState(ParkingDocumentState firstCarInsuranceDocumentState) {
            this.firstCarInsuranceDocumentState = firstCarInsuranceDocumentState;
        }

        public ParkingDocumentState getFirstCarOwnerIdDocumentState() {
            return firstCarOwnerIdDocumentState;
        }

        public void setFirstCarOwnerIdDocumentState(ParkingDocumentState firstCarOwnerIdDocumentState) {
            this.firstCarOwnerIdDocumentState = firstCarOwnerIdDocumentState;
        }

        public ParkingDocumentState getFirstCarPropertyRegistryDocumentState() {
            return firstCarPropertyRegistryDocumentState;
        }

        public void setFirstCarPropertyRegistryDocumentState(
                ParkingDocumentState firstCarPropertyRegistryDocumentState) {
            this.firstCarPropertyRegistryDocumentState = firstCarPropertyRegistryDocumentState;
        }

        public ParkingDocumentState getSecondCarDeclarationDocumentState() {
            return secondCarDeclarationDocumentState;
        }

        public void setSecondCarDeclarationDocumentState(
                ParkingDocumentState secondCarDeclarationDocumentState) {
            this.secondCarDeclarationDocumentState = secondCarDeclarationDocumentState;
        }

        public ParkingDocumentState getSecondCarInsuranceDocumentState() {
            return secondCarInsuranceDocumentState;
        }

        public void setSecondCarInsuranceDocumentState(
                ParkingDocumentState secondCarInsuranceDocumentState) {
            this.secondCarInsuranceDocumentState = secondCarInsuranceDocumentState;
        }

        public ParkingDocumentState getSecondCarOwnerIdDocumentState() {
            return secondCarOwnerIdDocumentState;
        }

        public void setSecondCarOwnerIdDocumentState(ParkingDocumentState secondCarOwnerIdDocumentState) {
            this.secondCarOwnerIdDocumentState = secondCarOwnerIdDocumentState;
        }

        public ParkingDocumentState getSecondCarPropertyRegistryDocumentState() {
            return secondCarPropertyRegistryDocumentState;
        }

        public void setSecondCarPropertyRegistryDocumentState(
                ParkingDocumentState secondCarPropertyRegistryDocumentState) {
            this.secondCarPropertyRegistryDocumentState = secondCarPropertyRegistryDocumentState;
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
            setFirstDeclarationAuthorizationFileName(parkingRequest
                    .getFirstDeclarationAuthorizationFileName());
            setFirstInsuranceFileName(parkingRequest.getFirstCarInsuranceFileName());
            setSecondCarOwnerIdFileName(parkingRequest.getSecondCarOwnerIdFileName());
            setSecondCarPropertyRegistryFileName(parkingRequest.getSecondCarPropertyRegistryFileName());
            setSecondDeclarationAuthorizationFileName(parkingRequest
                    .getSecondDeclarationAuthorizationFileName());
            setSecondInsuranceFileName(parkingRequest.getSecondCarInsuranceFileName());
            setDriverLicenseDocumentState(parkingRequest.getDriverLicenseDocumentState());
            setFirstCarDeclarationDocumentState(parkingRequest.getFirstCarDeclarationDocumentState());
            setFirstCarInsuranceDocumentState(parkingRequest.getFirstCarInsuranceDocumentState());
            setFirstCarOwnerIdDocumentState(parkingRequest.getFirstCarOwnerIdDocumentState());
            setFirstCarPropertyRegistryDocumentState(parkingRequest
                    .getFirstCarPropertyRegistryDocumentState());
            setSecondCarDeclarationDocumentState(parkingRequest.getSecondCarDeclarationDocumentState());
            setSecondCarInsuranceDocumentState(parkingRequest.getSecondCarInsuranceDocumentState());
            setSecondCarOwnerIdDocumentState(parkingRequest.getSecondCarOwnerIdDocumentState());
            setSecondCarPropertyRegistryDocumentState(parkingRequest
                    .getSecondCarPropertyRegistryDocumentState());
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
        setDriverLicenseDocumentState(parkingRequestFactoryEditor.getDriverLicenseDocumentState());
        setFirstCarDeclarationDocumentState(parkingRequestFactoryEditor
                .getFirstCarDeclarationDocumentState());
        setFirstCarInsuranceDocumentState(parkingRequestFactoryEditor
                .getFirstCarInsuranceDocumentState());
        setFirstCarOwnerIdDocumentState(parkingRequestFactoryEditor.getFirstCarOwnerIdDocumentState());
        setFirstCarPropertyRegistryDocumentState(parkingRequestFactoryEditor
                .getFirstCarPropertyRegistryDocumentState());
        setSecondCarDeclarationDocumentState(parkingRequestFactoryEditor
                .getSecondCarDeclarationDocumentState());
        setSecondCarInsuranceDocumentState(parkingRequestFactoryEditor
                .getSecondCarInsuranceDocumentState());
        setSecondCarOwnerIdDocumentState(parkingRequestFactoryEditor.getSecondCarOwnerIdDocumentState());
        setSecondCarPropertyRegistryDocumentState(parkingRequestFactoryEditor
                .getSecondCarPropertyRegistryDocumentState());
    }

    public ParkingDocument getParkingDocument(ParkingDocumentType documentType) {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(documentType)) {
                return parkingDocument;
            }
        }
        return null;
    }

    public String getDriverLicenseFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.DRIVER_LICENSE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getDriverLicenseFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.DRIVER_LICENSE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getDriverLicenseDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getDriverLicenseDocumentState().name());
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

    public String getFirstCarPropertyRegistryFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getFirstCarPropertyRegistryDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getFirstCarPropertyRegistryDocumentState().name());
        }
        return null;
    }

    public String getFirstCarInsuranceFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.FIRST_CAR_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getFirstCarInsuranceFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.FIRST_CAR_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getFirstCarInsuranceDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getFirstCarInsuranceDocumentState().name());
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

    public String getFirstCarOwnerIdFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.FIRST_CAR_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getFirstCarOwnerIdDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getFirstCarOwnerIdDocumentState().name());
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

    public String getFirstDeclarationAuthorizationFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getFirstCarDeclarationDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getFirstCarDeclarationDocumentState().name());
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

    public String getSecondCarPropertyRegistryFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getSecondCarPropertyRegistryDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getSecondCarPropertyRegistryDocumentState().name());
        }
        return null;
    }

    public String getSecondCarInsuranceFileName() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType()
                    .equals(ParkingDocumentType.SECOND_CAR_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        return null;
    }

    public String getSecondCarInsuranceFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType()
                    .equals(ParkingDocumentType.SECOND_CAR_INSURANCE)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getSecondCarInsuranceDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getSecondCarInsuranceDocumentState().name());
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

    public String getSecondCarOwnerIdFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(ParkingDocumentType.SECOND_CAR_OWNER_ID)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getSecondCarOwnerIdDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getSecondCarOwnerIdDocumentState().name());
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

    public String getSecondDeclarationAuthorizationFileNameToDisplay() {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(
                    ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION)) {
                return parkingDocument.getParkingFile().getFilename();
            }
        }
        if (getSecondCarDeclarationDocumentState() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getSecondCarDeclarationDocumentState().name());
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
        return !StringUtils.isEmpty(getFirstCarMake());
    }

    public boolean getHasSecondCar() {
        return !StringUtils.isEmpty(getSecondCarMake());
    }

    public boolean getHasDriverLicense() {
        return (getDriverLicenseFileName() != null && getDriverLicenseFileName().length() > 0)
                || getDriverLicenseDocumentState() != null;
    }
 
    public boolean getIsFileUploaded(String documentType){
        ParkingDocumentType parkingDocumentType = ParkingDocumentType.valueOf(documentType);
        return getParkingDocument(parkingDocumentType) != null;
    }
    
    private void deleteFirstCar() {
        setFirstCarMake(null);
        setFirstCarPlateNumber(null);
        setFirstCarDeclarationDocumentState(null);
        setFirstCarInsuranceDocumentState(null);
        setFirstCarOwnerIdDocumentState(null);
        setFirstCarPropertyRegistryDocumentState(null);

        deleteFile(ParkingDocumentType.FIRST_CAR_INSURANCE);
        deleteFile(ParkingDocumentType.FIRST_CAR_OWNER_ID);
        deleteFile(ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER);
        deleteFile(ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION);
    }
    
    private void deleteSecondCar() {
        setSecondCarMake(null);
        setSecondCarPlateNumber(null);
        setSecondCarDeclarationDocumentState(null);
        setSecondCarInsuranceDocumentState(null);
        setSecondCarOwnerIdDocumentState(null);
        setSecondCarPropertyRegistryDocumentState(null);

        deleteFile(ParkingDocumentType.SECOND_CAR_INSURANCE);
        deleteFile(ParkingDocumentType.SECOND_CAR_OWNER_ID);
        deleteFile(ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER);
        deleteFile(ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION);
    }

    private void deleteFile(ParkingDocumentType documentType) {
        ParkingDocument parkingDocument = getParkingDocument(documentType);
        if (parkingDocument != null) {
            String externalIdentifier = parkingDocument.getParkingFile()
                    .getExternalStorageIdentification();
            parkingDocument.delete();
            if (externalIdentifier != null) {
                FileManagerFactory.getFileManager().deleteFile(externalIdentifier);
            }
        }
    }
    
    public void delete() {
        if(canBeDeleted()){
            deleteFile(ParkingDocumentType.DRIVER_LICENSE);
            deleteFirstCar();
            deleteSecondCar();
            setParkingParty(null);
            deleteDomainObject();
        }        
    }
    
    private boolean canBeDeleted() {
        return getParkingRequestState() != ParkingRequestState.ACCEPTED;
    }
}
