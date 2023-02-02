package com.beginnertechies.GoogleApiIntegration.Utils;

import com.beginnertechies.GoogleApiIntegration.Repository.DriverRepo;

import com.beginnertechies.GoogleApiIntegration.models.Drive1;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;


@Service
public class TEMS_apiUtil {

    private static DriverRepo repo;
    @Autowired
    public TEMS_apiUtil(DriverRepo repo) {
        this.repo = repo;
    }

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = List.of(SheetsScopes.SPREADSHEETS_READONLY, DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE_METADATA,DriveScopes.DRIVE_APPDATA,DriveScopes.DRIVE_APPDATA);
        //    Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/Drivefile.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = TEMS_apiUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {

      try{
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        Sheets sheetservice =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        // Print the names and IDs for up to 10 files.
        String str = "'" + "12B8TMt5eTBOstk8fQ4zk3SFd2lHR-wak" + "'" + " in parents";
        final String range = "A2:E";
        FileList result = service.files().list()
                .setQ(str)
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {

                System.out.printf("%s (%s)\n", file.getName(), file.getId());

                getFileDetails(file.getId());   // calling ReadDriveFileApplication to read the files of passed id frome here
            }
        }

//        System.out.println("Enter date : ");
//        String date = sc.next();
//        String dateFolder = "";
        for (File file : files) {

            String str1 = "'" + file.getId() + "'" + " in parents";
            FileList result1 = service.files().list()
                    .setQ(str1)
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();

            List<File> files1 = result1.getFiles();
            if (files == null || files.isEmpty()) {
                System.out.println("No files found.");
            } else {
//                System.out.println("Files:");
                for (File file1 : files1) {

                    System.out.printf("%s (%s)\n", file1.getName(), file1.getId());

                }
            }
        }
    }catch (Exception e){
        e.printStackTrace();
    }
}




    public static void getFileDetails ( final String spreadsheetId) //String... args)
            throws IOException, GeneralSecurityException {
        //SpringApplication.run(DriveListFileApplication.class, args);
        try{
            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        final String spreadsheetId = "1-f9IW8acyP7E1IbAsyMVksBFwsHM29wiWBEYfKtsitY";

            System.out.println(spreadsheetId);

            final String range = "A2:F1000";
            Sheets service =
                    new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                            .setApplicationName(APPLICATION_NAME)
                            .build();
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values == null || values.isEmpty()) {
                System.out.println("No data found.");
            } else {
                System.out.println("Name, Major");


                int count = 0;
                List<Drive1> data = new ArrayList<>();

                for (List row : values) {
//                 Print columns A and E, which correspond to indices 0 and 4.
//                System.out.printf("%s, %s\n", row.get(0), row.get(3), row.get(4));

                    System.out.println(row.get(0));//f
                    System.out.println(row.get(1));//l
                    System.out.println(row.get(2));//email
                    System.out.println(row.get(3));//dur
                    System.out.println(row.get(4));//join
                    System.out.println(row.get(5));//exit

                    Drive1 drive = new Drive1();
//
                    drive.setFirstName(String.valueOf(row.get(0)));
                    drive.setLastName(String.valueOf(row.get(1)));
                    drive.setEmail(String.valueOf(row.get(2)));
                    drive.setDuration(String.valueOf(row.get(3)));
                    drive.setTime_joined(String.valueOf(row.get(4)));
                    drive.setTime_exited(String.valueOf(row.get(5)));

                    data.add(drive);
//
                    repo.save(drive);


                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
