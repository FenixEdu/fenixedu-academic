package net.sourceforge.fenixedu.domain.parking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class ParkingRequest extends ParkingRequest_Base {

    public ParkingRequest(ParkingRequestFactoryCreator creator) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParkingRequestState(ParkingRequestState.PENDING);
        setDriverLicenseDeliveryType(getDriverLicenseDeliveryType());
        setParkingParty(creator.getParkingParty());
        setCreationDate(new DateTime());
        setPhone(((Person) creator.getParkingParty().getParty()).getWorkPhone());
        setMobile(((Person) creator.getParkingParty().getParty()).getMobile());
        setEmail(((Person) creator.getParkingParty().getParty()).getEmail());
        setFirstRequest(true);
        setRequestedAs(creator.getRequestAs());
        setLimitlessAccessCard(creator.isLimitlessAccessCard());
    }

    public ParkingRequestFactoryEditor getParkingRequestFactoryEditor() {
        return new ParkingRequestFactoryEditor(this);
    }

    public static abstract class ParkingRequestFactory implements Serializable, FactoryExecutor {
        private DomainReference<ParkingParty> parkingParty;

        private Integer firstVechicleID;

        private Integer secondVechicleID;

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

        DocumentDeliveryType driverLicenseDeliveryType;

        DocumentDeliveryType firstCarPropertyRegistryDeliveryType;

        DocumentDeliveryType firstCarInsuranceDeliveryType;

        DocumentDeliveryType firstCarOwnerIdDeliveryType;

        DocumentDeliveryType firstCarDeclarationDeliveryType;

        DocumentDeliveryType secondCarPropertyRegistryDeliveryType;

        DocumentDeliveryType secondCarInsuranceDeliveryType;

        DocumentDeliveryType secondCarOwnerIdDeliveryType;

        DocumentDeliveryType secondCarDeclarationDeliveryType;

        RoleType requestAs;

        boolean limitlessAccessCard;

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

        protected void writeFirstVehicleDocuments(Vehicle vehicle, VirtualPath filePath) {
            writeVehicleFile(vehicle, filePath, getFirstCarOwnerIdFileName(),
                    NewParkingDocumentType.VEHICLE_OWNER_ID, getFirstCarOwnerIdInputStream(),
                    getFirstCarOwnerIdDeliveryType());
            writeVehicleFile(vehicle, filePath, getFirstCarPropertyRegistryFileName(),
                    NewParkingDocumentType.VEHICLE_PROPERTY_REGISTER,
                    getFirstCarPropertyRegistryInputStream(), getFirstCarPropertyRegistryDeliveryType());
            writeVehicleFile(vehicle, filePath, getFirstDeclarationAuthorizationFileName(),
                    NewParkingDocumentType.DECLARATION_OF_AUTHORIZATION,
                    getFirstDeclarationAuthorizationInputStream(), getFirstCarDeclarationDeliveryType());
            writeVehicleFile(vehicle, filePath, getFirstInsuranceFileName(),
                    NewParkingDocumentType.VEHICLE_INSURANCE, getFirstInsuranceInputStream(),
                    getFirstCarInsuranceDeliveryType());
        }

        protected void writeSecondVehicleDocuments(Vehicle vehicle, VirtualPath filePath) {
            writeVehicleFile(vehicle, filePath, getSecondCarOwnerIdFileName(),
                    NewParkingDocumentType.VEHICLE_OWNER_ID, getSecondCarOwnerIdInputStream(),
                    getSecondCarOwnerIdDeliveryType());
            writeVehicleFile(vehicle, filePath, getSecondCarPropertyRegistryFileName(),
                    NewParkingDocumentType.VEHICLE_PROPERTY_REGISTER,
                    getSecondCarPropertyRegistryInputStream(),
                    getSecondCarPropertyRegistryDeliveryType());
            writeVehicleFile(vehicle, filePath, getSecondDeclarationAuthorizationFileName(),
                    NewParkingDocumentType.DECLARATION_OF_AUTHORIZATION,
                    getSecondDeclarationAuthorizationInputStream(),
                    getSecondCarDeclarationDeliveryType());
            writeVehicleFile(vehicle, filePath, getSecondInsuranceFileName(),
                    NewParkingDocumentType.VEHICLE_INSURANCE, getSecondInsuranceInputStream(),
                    getSecondCarInsuranceDeliveryType());
        }

        private void writeVehicleFile(Vehicle vehicle, VirtualPath filePath, String filename,
                NewParkingDocumentType parkingDocumentType, InputStream inputStream,
                DocumentDeliveryType documentDeliveryType) {
            NewParkingDocument parkingDocument = vehicle.getParkingDocument(parkingDocumentType);
            if (parkingDocument != null
                    && (inputStream != null || documentDeliveryType != DocumentDeliveryType.ELECTRONIC_DELIVERY)) {
                parkingDocument.delete();
            }
            if (documentDeliveryType == DocumentDeliveryType.ELECTRONIC_DELIVERY && inputStream != null) {
                final Group group = getGroup(vehicle.getParkingRequest().getParkingParty().getParty());
                final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
                        filePath, filename, false, getParkingParty()
                        .getParty().getName(), filename, inputStream);

                final ParkingFile parkingFile = new ParkingFile(filename, filename, fileDescriptor
                        .getMimeType(), fileDescriptor.getChecksum(), fileDescriptor
                        .getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor.getUniqueId(),
                        group);

                new NewParkingDocument(parkingDocumentType, parkingFile, vehicle);
            }
        }

        protected void writeDriverLicenseFile(ParkingRequest parkingRequest, VirtualPath filePath) {
            NewParkingDocument parkingDocument = parkingRequest.getDriverLicenseDocument();
            DocumentDeliveryType documentDeliveryType = getDriverLicenseDeliveryType();
            String filename = getDriverLicenseFileName();
            if (parkingDocument != null
                    && (getDriverLicenseInputStream() != null || documentDeliveryType != DocumentDeliveryType.ELECTRONIC_DELIVERY)) {
                String externalIdentifier = parkingDocument.getParkingFile()
                        .getExternalStorageIdentification();
                parkingDocument.delete();
                if (externalIdentifier != null) {
                    FileManagerFactory.getFileManager().deleteFile(externalIdentifier);
                }
            }
            if (documentDeliveryType == DocumentDeliveryType.ELECTRONIC_DELIVERY
                    && getDriverLicenseInputStream() != null) {
                final Group group = getGroup(getParkingParty().getParty());
                final FileDescriptor fileDescriptor = FileManagerFactory.getFileManager().saveFile(
                        filePath, filename, false, getParkingParty()
                        .getParty().getName(), filename, getDriverLicenseInputStream());

                final ParkingFile parkingFile = new ParkingFile(filename, filename, fileDescriptor
                        .getMimeType(), fileDescriptor.getChecksum(), fileDescriptor
                        .getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor.getUniqueId(),
                        group);

                new NewParkingDocument(NewParkingDocumentType.DRIVER_LICENSE, parkingFile,
                        parkingRequest);
            }
        }

        private Group getGroup(Party party) {
            final PersonGroup personGroup = new PersonGroup((Person) party);
            final RoleGroup roleGroup = new RoleGroup(Role.getRoleByRoleType(RoleType.PARKING_MANAGER));
            return new GroupUnion(personGroup, roleGroup);
        }

        protected VirtualPath getFilePath(final Integer requestID) {
            Party party = getParkingParty().getParty();
            final VirtualPath filePath = new VirtualPath();

            filePath.addNode(new VirtualPathNode("ParkingFiles", "Parking Files"));

            filePath.addNode(new VirtualPathNode("Party" + party.getIdInternal(), party.getName()));
            filePath.addNode(new VirtualPathNode("PR" + requestID, "Parking Request ID"));

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

        public RoleType getRequestAs() {
            return requestAs;
        }

        public void setRequestAs(RoleType requestAs) {
            this.requestAs = requestAs;
        }

        public boolean isLimitlessAccessCard() {
            return limitlessAccessCard;
        }

        public void setLimitlessAccessCard(boolean limitlessAccessCard) {
            this.limitlessAccessCard = limitlessAccessCard;
        }

        public Integer getFirstVechicleID() {
            return firstVechicleID;
        }

        public void setFirstVechicleID(Integer firstVechicleID) {
            this.firstVechicleID = firstVechicleID;
        }

        public Integer getSecondVechicleID() {
            return secondVechicleID;
        }

        public void setSecondVechicleID(Integer secondVechicleID) {
            this.secondVechicleID = secondVechicleID;
        }

        public DocumentDeliveryType getFirstCarDeclarationDeliveryType() {
            return firstCarDeclarationDeliveryType;
        }

        public void setFirstCarDeclarationDeliveryType(
                DocumentDeliveryType firstCarDeclarationDeliveryType) {
            this.firstCarDeclarationDeliveryType = firstCarDeclarationDeliveryType;
        }

        public DocumentDeliveryType getFirstCarInsuranceDeliveryType() {
            return firstCarInsuranceDeliveryType;
        }

        public void setFirstCarInsuranceDeliveryType(DocumentDeliveryType firstCarInsuranceDeliveryType) {
            this.firstCarInsuranceDeliveryType = firstCarInsuranceDeliveryType;
        }

        public DocumentDeliveryType getFirstCarOwnerIdDeliveryType() {
            return firstCarOwnerIdDeliveryType;
        }

        public void setFirstCarOwnerIdDeliveryType(DocumentDeliveryType firstCarOwnerIdDeliveryType) {
            this.firstCarOwnerIdDeliveryType = firstCarOwnerIdDeliveryType;
        }

        public DocumentDeliveryType getFirstCarPropertyRegistryDeliveryType() {
            return firstCarPropertyRegistryDeliveryType;
        }

        public void setFirstCarPropertyRegistryDeliveryType(
                DocumentDeliveryType firstCarPropertyRegistryDeliveryType) {
            this.firstCarPropertyRegistryDeliveryType = firstCarPropertyRegistryDeliveryType;
        }

        public DocumentDeliveryType getSecondCarDeclarationDeliveryType() {
            return secondCarDeclarationDeliveryType;
        }

        public void setSecondCarDeclarationDeliveryType(
                DocumentDeliveryType secondCarDeclarationDeliveryType) {
            this.secondCarDeclarationDeliveryType = secondCarDeclarationDeliveryType;
        }

        public DocumentDeliveryType getSecondCarInsuranceDeliveryType() {
            return secondCarInsuranceDeliveryType;
        }

        public void setSecondCarInsuranceDeliveryType(DocumentDeliveryType secondCarInsuranceDeliveryType) {
            this.secondCarInsuranceDeliveryType = secondCarInsuranceDeliveryType;
        }

        public DocumentDeliveryType getSecondCarOwnerIdDeliveryType() {
            return secondCarOwnerIdDeliveryType;
        }

        public void setSecondCarOwnerIdDeliveryType(DocumentDeliveryType secondCarOwnerIdDeliveryType) {
            this.secondCarOwnerIdDeliveryType = secondCarOwnerIdDeliveryType;
        }

        public DocumentDeliveryType getSecondCarPropertyRegistryDeliveryType() {
            return secondCarPropertyRegistryDeliveryType;
        }

        public void setSecondCarPropertyRegistryDeliveryType(
                DocumentDeliveryType secondCarPropertyRegistryDeliveryType) {
            this.secondCarPropertyRegistryDeliveryType = secondCarPropertyRegistryDeliveryType;
        }

        public DocumentDeliveryType getDriverLicenseDeliveryType() {
            return driverLicenseDeliveryType;
        }

        public void setDriverLicenseDeliveryType(DocumentDeliveryType driverLicenseDeliveryType) {
            this.driverLicenseDeliveryType = driverLicenseDeliveryType;
        }
    }

    public static class ParkingRequestFactoryCreator extends ParkingRequestFactory {

        public ParkingRequestFactoryCreator(ParkingParty parkingParty) {
            super(parkingParty);
        }

        public ParkingRequest execute() {
            if (!getParkingParty().hasFirstTimeRequest()) {
                ParkingRequest parkingRequest = new ParkingRequest(this);
                VirtualPath filePath = getFilePath(parkingRequest.getIdInternal());

                writeDriverLicenseFile(parkingRequest, filePath);

                Vehicle firstVehicle = new Vehicle();
                firstVehicle.setParkingRequest(parkingRequest);
                firstVehicle.setPlateNumber(getFirstCarPlateNumber());
                firstVehicle.setVehicleMake(getFirstCarMake());
                firstVehicle.setPropertyRegistryDeliveryType(getFirstCarPropertyRegistryDeliveryType());
                firstVehicle.setInsuranceDeliveryType(getFirstCarInsuranceDeliveryType());
                firstVehicle.setOwnerIdDeliveryType(getFirstCarOwnerIdDeliveryType());
                firstVehicle
                        .setAuthorizationDeclarationDeliveryType(getFirstCarDeclarationDeliveryType());
                writeFirstVehicleDocuments(firstVehicle, filePath);

                if (getSecondCarMake() != null) {
                    Vehicle secondVehicle = new Vehicle();
                    secondVehicle.setParkingRequest(parkingRequest);
                    secondVehicle.setPlateNumber(getSecondCarPlateNumber());
                    secondVehicle.setVehicleMake(getSecondCarMake());
                    secondVehicle
                            .setPropertyRegistryDeliveryType(getSecondCarPropertyRegistryDeliveryType());
                    secondVehicle.setInsuranceDeliveryType(getSecondCarInsuranceDeliveryType());
                    secondVehicle.setOwnerIdDeliveryType(getSecondCarOwnerIdDeliveryType());
                    secondVehicle
                            .setAuthorizationDeclarationDeliveryType(getSecondCarDeclarationDeliveryType());
                    writeSecondVehicleDocuments(secondVehicle, filePath);
                }

                return parkingRequest;
            } else {
                return null;
            }
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

            setDriverLicenseFileName(parkingRequest.getDriverLicenseFileName());
            setDriverLicenseDeliveryType(parkingRequest.getDriverLicenseDeliveryType());

            Vehicle firstVehicle = parkingRequest.getVehicles().get(0);
            setFirstVechicleID(firstVehicle.getIdInternal());
            setFirstCarMake(firstVehicle.getVehicleMake());
            setFirstCarPlateNumber(firstVehicle.getPlateNumber());
            setFirstCarPropertyRegistryFileName(firstVehicle.getPropertyRegistryFileName());
            setFirstInsuranceFileName(firstVehicle.getInsuranceFileName());
            setFirstCarOwnerIdFileName(firstVehicle.getOwnerIdFileName());
            setFirstDeclarationAuthorizationFileName(firstVehicle.getAuthorizationDeclarationFileName());
            setFirstCarPropertyRegistryDeliveryType(firstVehicle.getPropertyRegistryDeliveryType());
            setFirstCarInsuranceDeliveryType(firstVehicle.getInsuranceDeliveryType());
            setFirstCarOwnerIdDeliveryType(firstVehicle.getOwnerIdDeliveryType());
            setFirstCarDeclarationDeliveryType(firstVehicle.getAuthorizationDeclarationDeliveryType());

            if (parkingRequest.getVehicles().size() > 1) {
                Vehicle secondVehicle = parkingRequest.getVehicles().get(1);
                setSecondVechicleID(secondVehicle.getIdInternal());
                setSecondCarMake(secondVehicle.getVehicleMake());
                setSecondCarPlateNumber(secondVehicle.getPlateNumber());
                setSecondCarPropertyRegistryFileName(secondVehicle.getPropertyRegistryFileName());
                setSecondInsuranceFileName(secondVehicle.getInsuranceFileName());
                setSecondCarOwnerIdFileName(secondVehicle.getOwnerIdFileName());
                setSecondDeclarationAuthorizationFileName(secondVehicle
                        .getAuthorizationDeclarationFileName());
                setSecondCarPropertyRegistryDeliveryType(secondVehicle.getPropertyRegistryDeliveryType());
                setSecondCarInsuranceDeliveryType(secondVehicle.getInsuranceDeliveryType());
                setSecondCarOwnerIdDeliveryType(secondVehicle.getOwnerIdDeliveryType());
                setSecondCarDeclarationDeliveryType(secondVehicle
                        .getAuthorizationDeclarationDeliveryType());
            }

            setRequestAs(parkingRequest.getRequestedAs());
            setLimitlessAccessCard(parkingRequest.getLimitlessAccessCard());
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
            VirtualPath filePath = getFilePath(parkingRequest.getIdInternal());

            writeDriverLicenseFile(parkingRequest, filePath);

            Vehicle firstVehicle = (Vehicle) RootDomainObject.readDomainObjectByOID(Vehicle.class,
                    getFirstVechicleID());

            if (firstVehicle != null) {
                firstVehicle.setPlateNumber(getFirstCarPlateNumber());
                firstVehicle.setVehicleMake(getFirstCarMake());
                firstVehicle.setPropertyRegistryDeliveryType(getFirstCarPropertyRegistryDeliveryType());
                firstVehicle.setInsuranceDeliveryType(getFirstCarInsuranceDeliveryType());
                firstVehicle.setOwnerIdDeliveryType(getFirstCarOwnerIdDeliveryType());
                firstVehicle
                        .setAuthorizationDeclarationDeliveryType(getFirstCarDeclarationDeliveryType());
                writeFirstVehicleDocuments(firstVehicle, filePath);
            }

            if (getSecondVechicleID() != null) {
                Vehicle secondVehicle = (Vehicle) RootDomainObject.readDomainObjectByOID(Vehicle.class,
                        getSecondVechicleID());

                if (getSecondCarMake() == null) {
                    secondVehicle.delete();
                } else {
                    secondVehicle.setPlateNumber(getSecondCarPlateNumber());
                    secondVehicle.setVehicleMake(getSecondCarMake());
                    secondVehicle
                            .setPropertyRegistryDeliveryType(getSecondCarPropertyRegistryDeliveryType());
                    secondVehicle.setInsuranceDeliveryType(getSecondCarInsuranceDeliveryType());
                    secondVehicle.setOwnerIdDeliveryType(getSecondCarOwnerIdDeliveryType());
                    secondVehicle
                            .setAuthorizationDeclarationDeliveryType(getSecondCarDeclarationDeliveryType());
                    writeSecondVehicleDocuments(secondVehicle, filePath);
                }
            } else if (getSecondCarMake() != null) {
                Vehicle secondVehicle = new Vehicle();
                secondVehicle.setParkingRequest(parkingRequest);
                secondVehicle.setPlateNumber(getSecondCarPlateNumber());
                secondVehicle.setVehicleMake(getSecondCarMake());
                secondVehicle
                        .setPropertyRegistryDeliveryType(getSecondCarPropertyRegistryDeliveryType());
                secondVehicle.setInsuranceDeliveryType(getSecondCarInsuranceDeliveryType());
                secondVehicle.setOwnerIdDeliveryType(getSecondCarOwnerIdDeliveryType());
                secondVehicle
                        .setAuthorizationDeclarationDeliveryType(getSecondCarDeclarationDeliveryType());                
                writeSecondVehicleDocuments(secondVehicle, filePath);
            }
            return parkingRequest;
        }
    }

    public boolean hasVehicleContainingPlateNumber(String plateNumber) {
        String plateNumberLowerCase = plateNumber.toLowerCase();
        for (Vehicle vehicle : getVehicles()) {
            if (vehicle.getPlateNumber().toLowerCase().contains(plateNumberLowerCase)) {
                return true;
            }
        }
        return false;
    }

    public void edit(ParkingRequestFactoryEditor parkingRequestFactoryEditor) {
        setDriverLicenseDeliveryType(parkingRequestFactoryEditor.getDriverLicenseDeliveryType());
        setRequestedAs(parkingRequestFactoryEditor.getRequestAs());
        setLimitlessAccessCard(parkingRequestFactoryEditor.isLimitlessAccessCard());
    }

    public String getDriverLicenseFileName() {
        NewParkingDocument driverLicenseDocument = getDriverLicenseDocument();
        if (driverLicenseDocument != null) {
            return driverLicenseDocument.getParkingFile().getFilename();
        }
        return null;
    }

    public String getDriverLicenseFileNameToDisplay() {
        NewParkingDocument driverLicenseDocument = getDriverLicenseDocument();
        if (driverLicenseDocument != null) {
            return driverLicenseDocument.getParkingFile().getFilename();
        } else if (getDriverLicenseDeliveryType() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                    .getLocale());
            return bundle.getString(getDriverLicenseDeliveryType().name());
        }
        return "";
    }

    public String getDeclarationDocumentLink() {
        NewParkingDocument parkingDocument = getDriverLicenseDocument();
        if (parkingDocument != null
                && parkingDocument.getParkingDocumentType() == NewParkingDocumentType.DRIVER_LICENSE) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            return FileManagerFactory.getFileManager().formatDownloadUrl(
                    parkingFile.getExternalStorageIdentification(), parkingFile.getFilename());
        }
        return "";
    }

    public ParkingDocument getParkingDocument(ParkingDocumentType documentType) {
        for (ParkingDocument parkingDocument : getParkingDocuments()) {
            if (parkingDocument.getParkingDocumentType().equals(documentType)) {
                return parkingDocument;
            }
        }
        return null;
    }

    public void deleteDriverLicenseDocument() {
        NewParkingDocument parkingDocument = getDriverLicenseDocument();
        if (parkingDocument != null) {
            parkingDocument.delete();
        }
    }

    public void delete() {
        if (canBeDeleted()) {
            for (; getVehicles().size() != 0; getVehicles().get(0).delete());
            deleteDriverLicenseDocument();
            setParkingParty(null);
            deleteDomainObject();
        }
    }

    private boolean canBeDeleted() {
        return getParkingRequestState() != ParkingRequestState.ACCEPTED;
    }
}
