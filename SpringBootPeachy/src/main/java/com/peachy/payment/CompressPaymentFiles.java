package com.peachy.payment;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class CompressPaymentFiles {
	private static Logger logger = Logger.getLogger(CompressPaymentFiles.class
			.getName());

	public static void compressFiles(String path, List<String> files) throws IOException, SecurityException, IllegalArgumentException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		df.setLenient(false);
		String zipFileName = df.format(new Date()) + ".zip";
		String outFileName = "jar:file:" + path + zipFileName;
		Map<String, String> env = new HashMap<String, String>();
		env.put("create", "true");
		URI uri = URI.create(outFileName);
		try  (FileSystem zipfs = FileSystems.newFileSystem(uri, env)){
			for (String inFileName : files) {
				Path externalTxtFile = Paths.get(path + inFileName);
				Path pathInZipfile = zipfs.getPath("/" + inFileName);         
	    		// copy a file into the zip file
	    		Files.copy( externalTxtFile, pathInZipfile, 
	    				StandardCopyOption.REPLACE_EXISTING);
			}
		}
		/* The UNIX/LINUX file permissions is not supported by Windows */
		if (System.getProperty("os.name").startsWith("Windows") == true ){
			
			FilePermission readWrite = new FilePermission(path + "/-", "read, write");
			if (readWrite.implies(new FilePermission(path + zipFileName, "read, write")) == false) {
				logger.error("Cannot set permissions for " + path + zipFileName);
			}else{
				System.out.println(path + zipFileName + " was successfully set to read/write.");
			}
		}else{
			Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
		
			//Owner Permissions
			perms.add(PosixFilePermission.OWNER_READ);
			perms.add(PosixFilePermission.OWNER_WRITE);
		
			//Group Permissions
			perms.add(PosixFilePermission.GROUP_READ);
			perms.add(PosixFilePermission.GROUP_WRITE);
		
			//Other Permissions
			perms.add(PosixFilePermission.OTHERS_READ);
			perms.add(PosixFilePermission.OTHERS_WRITE);

			Files.setPosixFilePermissions(Paths.get(path + zipFileName), perms);
		
			perms = Files.getPosixFilePermissions(Paths.get(path + zipFileName));
			if (	perms.contains(PosixFilePermission.OWNER_READ) &&
					perms.contains(PosixFilePermission.OWNER_READ) &&
					perms.contains(PosixFilePermission.GROUP_READ) &&
					perms.contains(PosixFilePermission.GROUP_WRITE) &&
					perms.contains(PosixFilePermission.OTHERS_READ) &&
					perms.contains(PosixFilePermission.OTHERS_WRITE) ) {
				logger.debug(path + zipFileName + " was successfully set to read/write.");
											
			}else{
				logger.error("Cannot set permissions for " + path + zipFileName);
			}
		}
	}

	public static void deleteFileList(String path, List<String> files) {
		for (String fileName : files) {
			File file = new File(path + fileName);
			file.delete();
		}
	}
}
