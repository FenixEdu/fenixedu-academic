package net.sourceforge.fenixedu.applicationTier.Servico.protocol;

import java.io.FileNotFoundException;
import java.io.InputStream;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory.FilePermissionType;
import net.sourceforge.fenixedu.domain.protocols.Protocol;

public class AddFileToProtocol extends FenixService {

	public Protocol run(Protocol protocol, InputStream fileInputStream, String fileName, FilePermissionType filePermissionType)
			throws FileNotFoundException {
		protocol.addFile(fileInputStream, fileName, filePermissionType);
		return protocol;
	}
}