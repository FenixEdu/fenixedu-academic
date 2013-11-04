-- Conselho Directivo -> Técnico Lisboa (Conselho Directivo)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Conselho Directivo)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Conselho Directivo';

-- Gabinete de Comunicação e Relações Públicas  -> Técnico Lisboa (Gabinete de Comunicação e Relações Públicas)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Gabinete de Comunicação e Relações Públicas)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Gabinete de Comunicação e Relações Públicas';

-- Conselho de Gestão ->  Técnico Lisboa (Conselho de Gestão)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Conselho de Gestão)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Conselho de Gestão';

-- Conselho de Escola  -> Técnico Lisboa (Conselho de Escola)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Conselho de Escola)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Conselho de Escola';
 
-- Assembleia de Escola  -> Técnico Lisboa (Assembleia de Escola)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Assembleia de Escola)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Assembleia de Escola';
   
-- Gabinete de Relações Internacionais -> Técnico Lisboa (Gabinete de Relações Internacionais)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Gabinete de Relações Internacionais)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Gabinete de Relações Internacionais';

-- Direcção de Recursos Humanos -> Técnico Lisboa (Direcção de Recursos Humanos)    

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Direcção de Recursos Humanos)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Direcção de Recursos Humanos';

-- Presidente do Instituto Superior Técnico -> Técnico Lisboa (Presidente)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Presidente)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Presidente do Instituto Superior Técnico';

-- Núcleo do Relações Internacionais -> Técnico Lisboa (Núcleo do Relações Internacionais)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Núcleo do Relações Internacionais)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Núcleo do Relações Internacionais';

-- Gabinete de Organização Pedagógica -> Técnico Lisboa (Gabinete de Organização Pedagógica)               

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Gabinete de Organização Pedagógica)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Gabinete de Organização Pedagógica';

-- IST Alumni -> Técnico Lisboa (Alumni)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Alumni)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='IST Alumni';

-- Direção de Serviços de Informática -> Técnico Lisboa (Direção de Serviços de Informática)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Direção de Serviços de Informática)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Direção de Serviços de Informática';

-- Direcção do ADIST -> Técnico Lisboa (Direcção do ADIST)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Direcção do ADIST)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Direcção do ADIST';

-- Direção Académica -> Técnico Lisboa (Direção Académica)                          

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Direção Académica)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Direção Académica';

-- Gabinete de Apoio ao Tutorado -> Técnico Lisboa (Gabinete de Apoio ao Tutorado)

UPDATE SENDER
SET FROM_NAME='Técnico Lisboa (Gabinete de Apoio ao Tutorado)'
WHERE (OID >> 32) = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.util.email.Sender') AND FROM_NAME='Gabinete de Apoio ao Tutorado';
