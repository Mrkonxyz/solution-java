package win.everything.solution;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
    }

    public File zip(
            File sourceFile,
            String zipFileName,
            String password) throws IOException {
        return zip(sourceFile, zipFileName, password, ZoneId.of("Asia/Bangkok"));
    }

    public File zip(
            File sourceFile,
            String zipFileName,
            String password,
            ZoneId userZoneId) throws IOException {

        File zipFile = new File(
                Files.createTempDirectory("report").toFile(),
                zipFileName
        );

        ZoneId effectiveZone = (userZoneId != null) ? userZoneId : ZoneId.of("Asia/Bangkok");

        try (ZipFile zip = new ZipFile(zipFile, password.toCharArray())) {
            ZipParameters parameters = new ZipParameters();
            parameters.setEncryptFiles(true);

            parameters.setLastModifiedFileTime(calculateDosCompatibleEpochMilli(effectiveZone));
            parameters.setEncryptionMethod(EncryptionMethod.AES);

            zip.addFile(sourceFile, parameters);
        }

        return zipFile;
    }

    private long calculateDosCompatibleEpochMilli(ZoneId userZoneId) {
        LocalDateTime userLocalTime = LocalDateTime.now(userZoneId);
        return userLocalTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
