package net.sourceforge.fenixedu.domain;

import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.ByteArray;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

@Deprecated
public class DSpaceFileStorage extends DSpaceFileStorage_Base {

    protected DSpaceFileStorage() {
        super();
        setName(DSpaceFileStorage.class.getSimpleName());
    }

    public static DSpaceFileStorage getInstance() {
        if (Bennu.getInstance().getDSpaceFileStorage() == null) {
            initDSpaceFileStorage();
        }
        return Bennu.getInstance().getDSpaceFileStorage();
    }

    @Atomic(mode = TxMode.WRITE)
    private static void initDSpaceFileStorage() {
        Bennu.getInstance().setDSpaceFileStorage(new DSpaceFileStorage());
    }

    @Override
    public String store(String uniqueIdentification, byte[] content) {
        if (content == null) {
            new DeleteFileRequest(AccessControl.getPerson(), uniqueIdentification, true);
        }
        throw new UnsupportedOperationException("dspace storage is read-only");
    }

    @Override
    public byte[] read(String uniqueIdentification) {
        InputStream inputStream = null;
        try {
            inputStream = FileManagerFactory.getFactoryInstance().getFileManager().retrieveFile(uniqueIdentification);
            return ByteArray.toBytes(inputStream);
        } catch (IOException e) {
            throw new Error(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new Error(e);
                }
            }
        }
    }

    @Override
    public InputStream readAsInputStream(String uniqueIdentification) {
        return FileManagerFactory.getFactoryInstance().getFileManager().retrieveFile(uniqueIdentification);
    }

}
