package constant;

public class Filepath
{
    public static final String LOG_DIR = SysProp.UD + SysProp.FS + Directory.LOG;
    public static final String LOG = LOG_DIR + SysProp.FS + Filename.LOGFILE;
    public static final String SECURITY_DIR = SysProp.UD + SysProp.FS + Directory.SECURITY;
    public static final String KEYSTORE = SECURITY_DIR + SysProp.FS + Filename.KEYSTORE;
    public static final String ROOT_CERT = SECURITY_DIR + SysProp.FS + Filename.ROOT_CERT;
    
    public static final String TEMP_CERTS_DIR = SECURITY_DIR + SysProp.FS + Directory.TEMP_CERTS;
}
