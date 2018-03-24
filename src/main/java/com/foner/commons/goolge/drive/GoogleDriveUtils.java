package com.foner.commons.goolge.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * The class GoogleDriveUtil.
 * 
 * @author manu
 */
public class GoogleDriveUtils {

	/** The logger. */
	private static final Logger	logger				= Logger.getLogger(GoogleDriveUtils.class);

	/** The constant ROOT. */
	private final String		ROOT				= "root";

	/** The constant SLASH. */
	private final String		SLASH				= "/";

	/** The constant BACKSLASH. */
	private final String		BACKSLASH			= "\\";

	/** The service account id (Credential). */
	private String				serviceAccountId	= null;

	/** The p12 file path (Credential). */
	private String				p12FilePath			= null;

	/** The drive. */
	private Drive				drive				= null;

	/**
	 * The default constructor.
	 * 
	 * @param serviceAccountId
	 *            the service account id
	 * @param p12FilePath
	 *            the p12 file path
	 */
	public GoogleDriveUtils(String serviceAccountId, String p12FilePath) {
		this.serviceAccountId = serviceAccountId;
		this.p12FilePath = p12FilePath;
		lookUpService();
	}

	/**
	 * Look up drive service.
	 * 
	 */
	private void lookUpService() {
		try {
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

			// Gets the credential
			Credential credential = getAuthorizeServiceAccount(httpTransport, jacksonFactory);

			// Sets up global Drive instance
			drive = new Drive.Builder(httpTransport, jacksonFactory, credential).build();
		} catch (Exception ex) {
			logger.error("Google drive could not be look up service: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Gets the authorize service account.
	 * 
	 * @param httpTransport
	 *            the http transport
	 * @param jacksonFactory
	 *            the jackson factory
	 * @return the credential
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	private Credential getAuthorizeServiceAccount(HttpTransport httpTransport, JacksonFactory jacksonFactory) throws Exception {
		Credential credential = null;
		try {
			// Checking the credential parameters
			if (StringUtils.isEmpty(serviceAccountId)) {
				throw new Exception("The service account id is mandatory");
			}
			if (StringUtils.isEmpty(p12FilePath)) {
				throw new Exception("The p12 file path is mandatory");
			}

			// Gets the p12 file
			File p12File = new File(p12FilePath);

			// Builds the scopes
			Set<String> scopes = new HashSet<>();
			scopes.add(DriveScopes.DRIVE);
			scopes.add(DriveScopes.DRIVE_APPDATA);
			scopes.add(DriveScopes.DRIVE_APPS_READONLY);
			scopes.add(DriveScopes.DRIVE_FILE);
			scopes.add(DriveScopes.DRIVE_METADATA_READONLY);
			scopes.add(DriveScopes.DRIVE_READONLY);
			scopes.add(DriveScopes.DRIVE_SCRIPTS);

			// Gets the credential
			credential = new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(jacksonFactory).setServiceAccountId(
					serviceAccountId).setServiceAccountScopes(scopes).setServiceAccountPrivateKeyFromP12File(p12File).build();
		} catch (IOException | GeneralSecurityException e) {
			logger.error("Google drive could not be get credential: " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
		return credential;
	}

	/**
	 * Uploads file.
	 * 
	 * @param fileName
	 *            the file name
	 * @param mimeType
	 *            the mime type
	 * @param folderDrivePath
	 *            the folder drive path
	 * @return the file id
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	public String uploadFile(String fileName, String mimeType, String folderDrivePath) throws Exception {
		String fileId = null;
		try {
			long time = System.currentTimeMillis();

			// Builds the file to upload
			com.google.api.services.drive.model.File fileToUpload = new com.google.api.services.drive.model.File();
			fileToUpload.setTitle(StringUtils.substringAfterLast(fileName, SLASH));
			fileToUpload.setMimeType(mimeType);

			String folderID = getFolderID(folderDrivePath);
			if (StringUtils.isNotEmpty(folderID)) {
				fileToUpload.setParents(Arrays.asList(new ParentReference().setId(folderID)));
			}

			// Gets the file content of the file to upload
			File fileOriginal = new File(fileName);
			FileContent fileContent = new FileContent(mimeType, fileOriginal);

			// insert file
			com.google.api.services.drive.model.File uploadedFile = drive.files().insert(fileToUpload, fileContent).execute();
			fileId = uploadedFile.getId();

			long elapsed = System.currentTimeMillis() - time;
			logger.info("Uploaded File :" + StringUtils.replace(uploadedFile.toString(), ",", ",\n"));
			logger.info("Elapsed time uploadding file (" + uploadedFile.getTitle() + ") in : " + elapsed + " ms");
		} catch (IOException e) {
			logger.error("Google drive could not be upload file: " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
		return fileId;
	}

	/**
	 * Download the file by id.
	 * 
	 * @param fileId
	 *            the file id
	 * @return the downloaded file
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	public File downloadFile(String fileId) throws Exception {
		File out = null;
		try {
			long time = System.currentTimeMillis();
			// Gets the file by id
			com.google.api.services.drive.model.File file = drive.files().get(fileId).execute();

			// Gets the file content
			if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
				HttpResponse response = drive.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();

				out = new File(file.getTitle());
				FileUtils.writeByteArrayToFile(out, IOUtils.toByteArray(response.getContent()));
			}

			long elapsed = System.currentTimeMillis() - time;
			logger.info("Elapsed time downloadding file ( " + fileId + ") in : " + elapsed + " ms");
		} catch (IOException e) {
			logger.error("Google drive could not be get file: " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
		return out;
	}

	/**
	 * Creates the folder.
	 * 
	 * @param folderName
	 *            the folder name
	 * @param folderDrive
	 *            the folder drive
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	public void createFolder(String folderName, String folderDrive) throws Exception {
		try {
			long time = System.currentTimeMillis();

			com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
			fileMetadata.setTitle(folderName);
			fileMetadata.setMimeType("application/vnd.google-apps.folder");

			String folderID = getFolderID(folderDrive);
			if (StringUtils.isNotEmpty(folderID)) {
				fileMetadata.setParents(Arrays.asList(new ParentReference().setId(folderID)));
			}

			drive.files().insert(fileMetadata).setFields("id").execute();

			long elapsed = System.currentTimeMillis() - time;
			logger.info("Elapsed time create folder ( " + folderName + ") in : " + elapsed + " ms");
		} catch (IOException e) {
			logger.error("Google drive could not be created folder: " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Deletes the folder content.
	 * 
	 * @param folderName
	 *            the folder name
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	public void deleteFolderContent(String folderName) throws Exception {
		try {
			String folderID = getFolderID(folderName);
			if (StringUtils.isNotEmpty(folderID)) {
				ChildList folderContents = drive.children().list(folderID).execute();

				if ((folderContents != null) && (folderContents.getItems() != null)) {
					for (ChildReference child : folderContents.getItems()) {
						long time = System.currentTimeMillis();

						drive.files().delete(child.getId()).execute();

						long elapsed = System.currentTimeMillis() - time;
						logger.info("Elapsed time deleting folder (" + child.getId() + ") in : " + elapsed + " ms");
					}
				}
			}
		} catch (IOException e) {
			logger.error("Google drive could not be delete: " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Checks if the folder exist.
	 * 
	 * @param folderPath
	 *            the folder path
	 * @return true, if the folder exist
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	public boolean existFolder(String folderPath) throws Exception {
		boolean exist = false;
		try {
			long time = System.currentTimeMillis();

			String folderID = getFolderID(folderPath);
			exist = StringUtils.isNotEmpty(folderID);

			long elapsed = System.currentTimeMillis() - time;
			logger.info("Elapsed time check exist the folder: " + folderPath + ", in : " + elapsed + " ms");
		} catch (Exception e) {
			logger.error("Google drive could not be checked exist folder: " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
		return exist;
	}

	/**
	 * Delete the file.
	 * 
	 * @param fileId
	 *            the file id
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	public void deleteFile(String fileId) throws Exception {
		try {
			long time = System.currentTimeMillis();

			drive.files().delete(fileId).execute();

			long elapsed = System.currentTimeMillis() - time;
			logger.info("Elapsed time deleting file ( " + fileId + ") in : " + elapsed + " ms");
		} catch (IOException e) {
			logger.error("Google drive could not be delete: " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Gets the folder id.
	 * 
	 * @param folderPath
	 *            the folder path
	 * @return the folder id
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	private String getFolderID(String folderPath) throws Exception {
		// Si la carpeta se llama root o es una barra: '/' o '\', devolvemos root, que es la manera de indicar a
		// Drive que busque en el directorio raiz.
		if (StringUtils.equalsIgnoreCase(ROOT, folderPath) || StringUtils.equals(SLASH, folderPath) || StringUtils.equals(BACKSLASH, folderPath)) {
			return "root";
		}

		// Gets the folder path id
		String parentFolderId = null;
		if (StringUtils.contains(folderPath, SLASH)) {
			parentFolderId = getFolderID(StringUtils.substringBeforeLast(folderPath, SLASH));
		}

		// Gets the folder name
		String folderName;
		if (StringUtils.contains(folderPath, SLASH)) {
			folderName = StringUtils.substringAfterLast(folderPath, SLASH);
		} else {
			folderName = folderPath;
		}

		// Checks the files
		FileList files;
		if (StringUtils.isNotEmpty(parentFolderId)) {
			files = executeDriveQuery(
					"mimeType='application/vnd.google-apps.folder' and '" + parentFolderId + "' in parents and title='" + folderName + "' and not trashed");
		} else {
			files = executeDriveQuery("mimeType='application/vnd.google-apps.folder' and title='" + folderName + "' and not trashed");
		}

		String folderID = null;
		if ((files.getItems() != null) && (files.getItems().size() > 0)) {
			if (files.getItems().size() == 1) {
				com.google.api.services.drive.model.File folder = files.getItems().get(0);
				folderID = folder.getId();
			} else {
				files.getItems().forEach((file) -> {
					logger.info(file.getTitle() + " - " + file.getId() + " - " + file.getMimeType());
				});
				throw new Exception("Google drive find More than one folder: " + folderName);
			}
		} else {
			logger.warn("Drive folder '" + folderName + "' not found");
		}
		return folderID;
	}

	/**
	 * Execute the drive query.
	 * 
	 * @param query
	 *            the query
	 * @return the file list
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	private FileList executeDriveQuery(String query) throws Exception {
		try {
			return drive.files().list().setQ(query).execute();
		} catch (IOException e) {
			logger.error("Google drive could not be execute query: " + e.getMessage(), e);
			throw new Exception(e);
		}
	}

	/**
	 * Gets the storage used info.
	 * 
	 * @return the storage used info
	 * @throws Exception
	 *             the jumbo white label platform exception
	 */
	public String getStorageUsedInfo() throws Exception {
		String storageUsedInfo = null;
		try {
			About about = drive.about().get().execute();
			BigDecimal percent = new BigDecimal((about.getQuotaBytesUsed().doubleValue() / about.getQuotaBytesTotal().doubleValue()) * 100);
			percent = percent.setScale(2, RoundingMode.HALF_UP);
			storageUsedInfo = percent + "%  [" + formatFileSize(about.getQuotaBytesUsed()) + " / " + formatFileSize(about.getQuotaBytesTotal()) + "]";
			logger.info("QuotaBytesUsed: " + about.getQuotaBytesUsed() + "  -->  " + formatFileSize(about.getQuotaBytesUsed()));
			logger.info("QuotaBytesTotal: " + about.getQuotaBytesTotal() + "  -->  " + formatFileSize(about.getQuotaBytesTotal()));
		} catch (IOException e) {
			logger.error("Google drive could not check the storage used: " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
		return storageUsedInfo;
	}

	/**
	 * Formats the file size.
	 * 
	 * @return the formated file size.
	 */
	private String formatFileSize(long size) {
		long KB = 1024; // Kilobyte
		long MB = 1024 * KB; // Megabyte
		long GB = 1024 * MB; // Gigabyte
		DecimalFormat formatter = new DecimalFormat("###.###");

		if (size < KB) {
			return size + " bytes";
		} else if (size < MB) {
			return formatter.format(size / KB) + " KB";
		} else if (size < GB) {
			return formatter.format((size / MB)) + " MB";
		}
		return formatter.format(size / GB) + " GB";
	}
}
