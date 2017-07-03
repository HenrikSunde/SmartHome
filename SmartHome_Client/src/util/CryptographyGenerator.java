package util;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

public class CryptographyGenerator
{
    public static KeyPair generateRSAKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException
    {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }
    
    public static X509Certificate generateSelfSignedCert(String CN, int validYears, KeyPair keyPair) throws CertificateException, OperatorCreationException
    {
        X500NameBuilder nameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        nameBuilder.addRDN(BCStyle.CN, CN);
        X500Name dnName = nameBuilder.build();
        
        long timeNow = System.currentTimeMillis();
        Date validityBegin = new Date(timeNow);
        BigInteger serialNumber = new BigInteger(Long.toString(timeNow));
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(validityBegin);
        calendar.add(Calendar.YEAR, validYears);
        Date validityEnd = calendar.getTime();
        
        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, serialNumber, validityBegin, validityEnd, dnName, keyPair.getPublic());

        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").setProvider(BouncyCastleProvider.PROVIDER_NAME).build(keyPair.getPrivate());
        return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(certBuilder.build(signer));
    }

    public static PKCS10CertificationRequest generateCSR(KeyPair keyPair, String CN) throws OperatorCreationException
    {
        PKCS10CertificationRequestBuilder requestBuilder = new JcaPKCS10CertificationRequestBuilder(new X500Principal(CN), keyPair.getPublic());
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = csBuilder.build(keyPair.getPrivate());
        return requestBuilder.build(signer);
    }

    public static X509Certificate signCSR(X509Certificate rootCert, PKCS10CertificationRequestBuilder csr)
    {
        return null;
    }

    public static String certificateToString(X509Certificate certificate) throws IOException
    {
        StringWriter writer = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        pemWriter.writeObject(certificate);
        pemWriter.flush();
        pemWriter.close();
        return writer.toString();
    }
    
    public static X509Certificate stringToCertificate(String certString) throws IOException, CertificateException
    {
        StringReader reader = new StringReader(certString);
        PEMParser pemParser = new PEMParser(reader);
        Object obj = pemParser.readObject();
        pemParser.close();
        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter().setProvider("BC");
        return certConverter.getCertificate((X509CertificateHolder) obj);
    }
    
    
}
