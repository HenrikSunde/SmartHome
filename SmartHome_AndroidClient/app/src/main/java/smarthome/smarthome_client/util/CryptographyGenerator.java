package smarthome.smarthome_client.util;

import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x500.X500NameBuilder;
import org.spongycastle.asn1.x500.style.BCStyle;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.X509v3CertificateBuilder;
import org.spongycastle.cert.jcajce.JcaX509CertificateConverter;
import org.spongycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openssl.PEMParser;
import org.spongycastle.openssl.jcajce.JcaPEMWriter;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;
import org.spongycastle.pkcs.PKCS10CertificationRequest;
import org.spongycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.spongycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.spongycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

/***************************************************************************************************
 *
 **************************************************************************************************/
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
        BigInteger serialNumber = new BigInteger(String.valueOf(timeNow));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(validityBegin);
        calendar.add(Calendar.YEAR, validYears);
        Date validityEnd = calendar.getTime();

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, serialNumber, validityBegin, validityEnd, dnName, keyPair.getPublic());

        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").setProvider(BouncyCastleProvider.PROVIDER_NAME).build(keyPair.getPrivate());
        return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(certBuilder.build(signer));
    }

    public static JcaPKCS10CertificationRequest generateCSR(KeyPair keyPair, String CN) throws OperatorCreationException
    {
        X500NameBuilder nameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        nameBuilder.addRDN(BCStyle.CN, CN);
        PKCS10CertificationRequestBuilder requestBuilder = new JcaPKCS10CertificationRequestBuilder(nameBuilder.build(), keyPair.getPublic());
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = csBuilder.build(keyPair.getPrivate());
        PKCS10CertificationRequest csr = requestBuilder.build(signer);
        return new JcaPKCS10CertificationRequest(csr);
    }

    public static X509Certificate signCSR(PrivateKey privateKey, X509Certificate rootCert, JcaPKCS10CertificationRequest csr) throws NoSuchAlgorithmException, InvalidKeyException, OperatorCreationException, CertificateException, NoSuchProviderException, SignatureException
    {
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(rootCert, serial, rootCert.getNotBefore(), rootCert.getNotAfter(), csr.getSubject(), csr.getPublicKey());
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").setProvider(BouncyCastleProvider.PROVIDER_NAME).build(privateKey);
        X509CertificateHolder holder = certBuilder.build(signer);
        X509Certificate signedCert = new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(holder);
        signedCert.verify(rootCert.getPublicKey());
        return signedCert;
    }

    public static String pemObjectToString(Object PEMObject) throws IOException
    {
        StringWriter writer = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        pemWriter.writeObject(PEMObject);
        pemWriter.flush();
        pemWriter.close();
        return writer.toString();
    }

    public static Object stringToPemObject(String pemObjectString) throws IOException, CertificateException
    {
        StringReader reader = new StringReader(pemObjectString);
        PEMParser pemParser = new PEMParser(reader);
        Object obj = pemParser.readObject();
        pemParser.close();
        if (obj instanceof X509CertificateHolder)
        {
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter().setProvider("BC");
            return certConverter.getCertificate((X509CertificateHolder) obj);
        }
        else if (obj instanceof PKCS10CertificationRequest)
        {
            return new JcaPKCS10CertificationRequest((PKCS10CertificationRequest) obj);
        }
        else
        {
            return obj;
        }
    }
}
