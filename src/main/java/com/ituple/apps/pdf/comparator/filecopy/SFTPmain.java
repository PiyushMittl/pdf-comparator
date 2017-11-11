package com.ituple.apps.pdf.comparator.filecopy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.ituple.apps.pdf.comparator.Constant;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class SFTPmain {
	static JSCHLogger jschLogger=new JSCHLogger();
	static String SFTPHOST       = Constant.SFTPHOST;
	static int    SFTPPORT       = Integer.parseInt(Constant.SFTPPORT.toString().trim());
	static String SFTPUSER       = Constant.SFTPUSER;
	static String SFTPPASS       = Constant.SFTPPASS;
	static String SFTPWORKINGDIR = Constant.SFTPWORKINGDIR;
	static Session     session     = null;
	static Channel     channel     = null;
	static ChannelSftp channelSftp = null;
	public static void downloadBundleSFTP() throws IOException, JSchException, SftpException{
		Properties prop = new Properties();
		Map<String,String> folderMap=JSCHutil.getMap();
		try{
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");

			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;
			List<String> folderList=getFolderList(SFTPWORKINGDIR);
			//String dest="c:\\SALIX\\";

			String dest=Constant.INCOMING_PDF_BUNDLE;

			//putFolderNameInProperty(SFTPWORKINGDIR,prop,output);
			//call cd one by one for each folder and set read flag on

			for(String folder:folderList){
				//channelSftp.cd(SFTPWORKINGDIR);
				if(JSCHutil.isExist(folder,folderMap)){//check folder status true or false from properties file
					jschLogger.JSCHlogger.info("#######  skipping folder it is already dowloaded=  "+folder);
					//updateFolderStatus(folder,prop,output);
					//setFolderStatus(folder,prop,output)
					//put folder status true in properties file
				}
				else{
					jschLogger.JSCHlogger.info("@@@@@@@ undownloaded folder found folder copy start=  "+folder);
					jschLogger.JSCHlogger.info("Source=  "+SFTPWORKINGDIR+folder);
					jschLogger.JSCHlogger.info("destination=  "+dest+folder);
					channelSftp.cd((SFTPWORKINGDIR+folder));
					new File(dest+folder).mkdirs();
					downloadDir((SFTPWORKINGDIR+folder),(dest+folder));
					folderMap.put(folder, "true");
					JSCHutil.setMapToProperty(folderMap);
					jschLogger.JSCHlogger.info("putting folder status "+folder+" true in map");
				}
			}
			jschLogger.JSCHlogger.info("*************************program ends*************************");
		}catch(Exception ex){
			ex.printStackTrace();
			jschLogger.JSCHlogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$exception occured in main"+ex);
		}
		finally{
			JSCHutil.setMapToProperty(folderMap);
			jschLogger.JSCHlogger.info("finally called ");
		}
	}

	static void downloadDir(String sourcePath, String destPath) throws SftpException, JSchException { // With subfolders and all files.
		// Copy remote folders one by one.
		jschLogger.JSCHlogger.info("calling -> IsFolderCopy");
		lsFolderCopy(sourcePath, destPath); 
		// Separated because loops itself inside for subfolders.
		channelSftp.lcd(destPath);
	}
	static void lsFolderCopy(String sourcePath, String destPath) throws SftpException, JSchException { // List source (remote, sftp) directory and create a local copy of it - method for every single directory.
		jschLogger.JSCHlogger.info("start method -> IsFolderCopy");
		Vector<ChannelSftp.LsEntry> list = channelSftp.ls(sourcePath); // List source directory structure.
		for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the list to get file/folder names.
			if (!oListItem.getAttrs().isDir()) { // If it is a file (not a directory).
				if (!(new File(destPath + "/" + oListItem.getFilename())).exists() || (oListItem.getAttrs().getMTime() > Long.valueOf(new File(destPath + "/" + oListItem.getFilename()).lastModified() / (long) 1000).intValue())) { // Download only if changed later.
					new File(destPath + "/" + oListItem.getFilename());
					try{
						channelSftp.get(sourcePath + "/" + oListItem.getFilename(), destPath + "/" + oListItem.getFilename()); // Grab file from source ([source filename], [destination filename]).

					}
					catch(Exception e){
						jschLogger.JSCHlogger.info("file may contains special character or location does not exist \n"+e);
					}
					jschLogger.JSCHlogger.info("copied file:  "+sourcePath + "/" + oListItem.getFilename());
				}
			} else if (!(".".equals(oListItem.getFilename()) || "..".equals(oListItem.getFilename()))) {
				new File(destPath + "/" + oListItem.getFilename()).mkdirs(); // Empty folder copy.
				lsFolderCopy(sourcePath + "/" + oListItem.getFilename(), destPath + "/" + oListItem.getFilename()); // Enter found folder on server to read its contents and create locally.
			}
		}
	}

	private static List<String> getFolderList(String sourcePath) throws JSchException, SftpException {
		Vector<ChannelSftp.LsEntry> list = channelSftp.ls(sourcePath);
		List<String> folderList=new ArrayList<String>();
		try {
			for (ChannelSftp.LsEntry oListItem : list)
			{
				if (oListItem.getAttrs().isDir()) {
					folderList.add(oListItem.getFilename());
				}
			} 
		}catch (Exception io) 
		{
			jschLogger.JSCHlogger.info("$$$ Exception in getFolderList"+"/n exception is-->"+io);
			System.exit(0);
			io.printStackTrace();
		}	
		return folderList;
	}

	private static void putFolderNameInProperty(String sourcePath, Properties prop, OutputStream output) throws JSchException, SftpException {
		Vector<ChannelSftp.LsEntry> list = channelSftp.ls(sourcePath);
		try {
			for (ChannelSftp.LsEntry oListItem : list)
			{
				if (oListItem.getAttrs().isDir()) {
					prop.setProperty(oListItem.getFilename(),"");
				}
			} 
			prop.store(output, null);
		}catch (IOException io) 
		{
			jschLogger.JSCHlogger.info("$$$Exception in putFolderNameInProperty"+"/n exception is-->"+io);
			System.out.println("Exception in putFolderNameInProperty");
			io.printStackTrace();

		} finally 
		{
			if (output != null) 
			{
				try 
				{
					output.close();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}}


	private static String getFolderName(String pathName){
		String folderName=pathName.substring(pathName.lastIndexOf("/") + 1).trim();
		return folderName;
	}

	public static void setFolderStatus(String folderName, Properties prop, OutputStream output) throws IOException{
		try {
			prop.setProperty(folderName,"true");
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					jschLogger.JSCHlogger.info("$$$Exception in setFolderStatus"+"/n exception is-->"+e);
					e.printStackTrace();
				}
			}
		}
	}

	public static void updateFolderStatus(String folderName, Properties prop, OutputStream output) throws IOException{
		try {

			FileInputStream in = new FileInputStream("c:\\config.properties");
			prop.load(in);
			in.close();
			prop.setProperty(folderName, "true");
			prop.store(output, null);
			output.close();
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					jschLogger.JSCHlogger.info("$$$Exception in updateFolderStatus"+"/n exception is-->"+e);
					e.printStackTrace();
				}
			}
		}
	}

	public static String getFolderStatus(String folderName, Properties prop){
		String status = "false";
		try {
			status=prop.getProperty(folderName);
		} catch (Exception io) {
			jschLogger.JSCHlogger.info("$$$Exception in getFolderStatus"+"/n exception is-->"+io);
			io.printStackTrace();
		} 
		return status;
	}
}

