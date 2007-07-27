package net.sourceforge.fenixedu.applicationTier.Servico.protocol;

import java.io.File;
import java.io.FileNotFoundException;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory.FilePermissionType;
import net.sourceforge.fenixedu.domain.protocols.Protocol;

public class AddFileToProtocol extends Service {

    public Protocol run(Protocol protocol, File file, String fileName,
            FilePermissionType filePermissionType) throws FileNotFoundException {
        protocol.addFile(file, fileName, filePermissionType);
        return protocol;
    }
}