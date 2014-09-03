package com.rugobal.dt.server.servlet;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

public class MyFileUploadServlet extends UploadAction {

	private static final long serialVersionUID = 1L;

	//	  Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	//	  /**
	//	   * Maintain a list with received files and their content types. 
	//	   */
	//	  Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

	/**
	 * Override executeAction to save the received files in a custom place
	 * and delete this items from session.  
	 */
	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {

		String response = "";
		int cont = 0;
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				cont++;
				try {

					// Save the file in the system temp directory
					String separator = FileSystems.getDefault().getSeparator();
					String targetDir = System.getProperty(JAVA_IO_TMPDIR);
					String newFilePath = targetDir + separator + item.getFieldName();

					File file = new File(newFilePath);
					item.write(file);

					/// Compose a xml message with the full file information
					response += "<file-" + cont + "-field>" + item.getFieldName() + "</file-" + cont + "-field>\n";
					response += "<file-" + cont + "-name>" + item.getName() + "</file-" + cont + "-name>\n";
					response += "<file-" + cont + "-size>" + item.getSize() + "</file-" + cont + "-size>\n";
					response += "<file-" + cont + "-type>" + item.getContentType() + "</file-" + cont + "type>\n";
				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}
		}

		/// Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		/// Send information of the received files to the client.
		return "<response>\n" + response + "</response>\n";
	}

}
