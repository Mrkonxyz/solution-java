package win.everything.solution;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private TimeZone originalTimeZone;

    @BeforeEach
    void setUp() {
        originalTimeZone = TimeZone.getDefault();
    }

    @AfterEach
    void tearDown() {
        TimeZone.setDefault(originalTimeZone);
    }

    @Test
    void testZipWithJvmUtcAndUserInBangkok(@TempDir Path tempDir) throws IOException {
        // Server JVM runs in UTC, user is in Bangkok
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        runZipTestWithUserZone("UTC", ZoneId.of("Asia/Bangkok"), "output-jvmUTC-userBangkok.zip", tempDir);
    }

    @Test
    void testZipWithJvmUtcAndUserInTokyo(@TempDir Path tempDir) throws IOException {
        // Server JVM runs in UTC, user is in Tokyo (+9)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        runZipTestWithUserZone("UTC", ZoneId.of("Asia/Tokyo"), "output-jvmUTC-userTokyo.zip", tempDir);
    }

    @Test
    void testZipWithJvmUtcAndUserInLondon(@TempDir Path tempDir) throws IOException {
        // Server JVM runs in UTC, user is in London
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        runZipTestWithUserZone("UTC", ZoneId.of("Europe/London"), "output-jvmUTC-userLondon.zip", tempDir);
    }

    @Test
    void testZipWithJvmBangkokAndUserInTokyo(@TempDir Path tempDir) throws IOException {
        // Server JVM runs in Bangkok, user is in Tokyo (+9)
        // Must produce identical DOS time as JVM in UTC with user in Tokyo!
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Bangkok"));
        runZipTestWithUserZone("Asia/Bangkok", ZoneId.of("Asia/Tokyo"), "output-jvmBKK-userTokyo.zip", tempDir);
    }

    private void runZipTestWithUserZone(String jvmTimeZoneId, ZoneId userZoneId, String zipFileName, Path tempDir) throws IOException {
        System.out.println("==================================================");
        System.out.println("Server JVM TimeZone: " + TimeZone.getDefault().getID());
        System.out.println("Client User TimeZone: " + userZoneId);

        // Arrange
        Main main = new Main();
        Path sampleFilePath = tempDir.resolve("sample.txt");
        Files.writeString(sampleFilePath, "Report for user in timezone: " + userZoneId);
        File sourceFile = sampleFilePath.toFile();
        String password = "SecretPassword123!";

        LocalDateTime expectedUserLocalTime = LocalDateTime.now(userZoneId);

        // Act
        File zipFile = main.zip(sourceFile, zipFileName, password, userZoneId);

        // Copy to project root
        Path rootZipFile = Path.of(System.getProperty("user.dir"), zipFileName);
        Files.copy(zipFile.toPath(), rootZipFile, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Exported Zip: " + rootZipFile.toAbsolutePath());
        System.out.println("Expected User Local Hour: " + expectedUserLocalTime.getHour());

        // Verify with Zip4j
        try (ZipFile verifyZip = new ZipFile(rootZipFile.toFile(), password.toCharArray())) {
            assertTrue(verifyZip.isValidZipFile());
            List<FileHeader> fileHeaders = verifyZip.getFileHeaders();
            assertFalse(fileHeaders.isEmpty());

            FileHeader header = fileHeaders.get(0);
            long dosTime = header.getLastModifiedTime();
            int sec = (int) (dosTime & 0x1f) * 2;
            int min = (int) ((dosTime >> 5) & 0x3f);
            int hour = (int) ((dosTime >> 11) & 0x1f);
            int day = (int) ((dosTime >> 16) & 0x1f);
            int month = (int) ((dosTime >> 21) & 0x0f);
            int year = (int) (((dosTime >> 25) & 0x7f) + 1980);
            String dosFormatted = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, min, sec);

            System.out.println("ZIP Header DOS Time: " + dosFormatted);
            System.out.println("==================================================");

            assertEquals(expectedUserLocalTime.getYear(), year);
            assertEquals(expectedUserLocalTime.getMonthValue(), month);
            assertEquals(expectedUserLocalTime.getDayOfMonth(), day);
            assertEquals(expectedUserLocalTime.getHour(), hour);
            assertEquals(expectedUserLocalTime.getMinute(), min);
        }
    }
}
