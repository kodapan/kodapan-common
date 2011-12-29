package se.kodapan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author kalle
 * @since 2011-12-29 09:10
 */
public class Properties {

  private Logger log;
  public static String defaultPropertiesFileCharacterEncoding = "UTF8";

  public InputStream getDefaultProperties() {
    return null;
  }

  private File file;
  private String propertiesFileCharacterEncoding;

  private java.util.Properties properties;

  protected Properties(File file) throws IOException {
    this(file, defaultPropertiesFileCharacterEncoding);
  }

  protected Properties(File file, String propertiesFileCharacterEncoding) throws IOException {
    log = LoggerFactory.getLogger(file.getName());
    this.file = file;
    this.propertiesFileCharacterEncoding = propertiesFileCharacterEncoding;
    if (!file.exists()) {
      log.info("Creating new properties file " + file.getAbsolutePath());
      if (!file.getParentFile().exists()) {
        if (!file.getParentFile().mkdirs()) {
          throw new IOException("Could not mkdirs " + file.getParentFile().getAbsolutePath());
        }
      }
      InputStream defaultProperties = getDefaultProperties();
      if (defaultProperties == null) {
        log.warn("No default properties.");
        properties = new java.util.Properties();
      } else {
        properties.load(new InputStreamReader(new FileInputStream(file), propertiesFileCharacterEncoding));
      }
    }

  }

  public String getProperty(String key) {
    return getProperty(key, null);
  }

  public String getProperty(String key, String defaultValue) {
    String value = properties.getProperty(key, null);
    if (value == null && defaultValue != null) {
      value = defaultValue;

      try {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file, true), propertiesFileCharacterEncoding);
        writer.write(key);
        writer.write("=");
        writer.write(value);
        writer.write("\n");
        writer.close();
      } catch (IOException ioe) {
        log.error("Exception ignored while attempting to append to " + file.getAbsolutePath() + " with " + key +"="+value);
      }

    }
    return value;
  }


}
