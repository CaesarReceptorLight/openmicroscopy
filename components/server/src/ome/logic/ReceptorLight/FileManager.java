package ome.logic.ReceptorLight;

import java.io.*;
import java.nio.file.*;
import java.security.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import static java.nio.file.StandardOpenOption.*;

import ome.model.core.RlFileInformation;
import ome.model.core.IntValue;
import ome.model.core.IntValueList;
import ome.model.enums.RlFileType;
import ome.logic.ReceptorLight.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManager
{	
    private final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";    
    private static final String TABLE_NAME = "filetable";    
    private String omeroDataDir;
    private PostgresHelper postgresHelper;
    private Logger log;
    
    List<FileAccessItem> openedFiles;
    
    public FileManager(PostgresHelper postgresHelper, String omeroDataDir, Logger log)
    {    	
        this.postgresHelper = postgresHelper;
        this.omeroDataDir = omeroDataDir;
        this.log = log;
        try
        {
            openedFiles = new ArrayList<>();
        	this.postgresHelper.checkTable(TABLE_NAME, RlFileInformation.class);
        }
        catch(Exception e)
        {
        	//Todo
        }
    }
    
    public long createFile(String fileName) throws Exception
    {
        if(fileName == null)
            throw new Exception("[createFile] fileName is NULL");
        
        int newId = postgresHelper.createNewId(TABLE_NAME);
        
        File f = getFile(fileName, newId);
        if(f.exists() && ! f.isDirectory())
            throw new FileAlreadyExistsException("[createFile] file '" + fileName + "' already exists.");
		
        try
        {        
            f.getParentFile().mkdir();
            f.createNewFile();
            
            RlFileInformation fileInfo = (RlFileInformation) MemberInfoHelper.createInstanceFromClass(RlFileInformation.class);
            fileInfo.setName(fileName);
            fileInfo.setUId(newId);
            fileInfo.setCreationDateTime(GetUTCDateTimeAsString());
            fileInfo.setStatus(1);
            fileInfo.setOriginalObjectId(0);
            fileInfo.setFileType(new RlFileType("ReceptorLightFile"));
            fileInfo.setOmeroId(0);
            
            postgresHelper.insertObject(TABLE_NAME, fileInfo);
                return fileInfo.getUId();
        }
        catch(Exception ex)
        {
        	throw new Exception("[createFile] file '" + f.toPath() + "' throws exception: " + ex.toString());
        }
    }
	
	public long createOmeroFile(String fileName, long omeroId) throws Exception
    {
        if(fileName == null)
            throw new Exception("[createOmeroFile] fileName is NULL");
        
        try
        {
            int newId = postgresHelper.createNewId(TABLE_NAME);
            
            RlFileInformation fileInfo = (RlFileInformation) MemberInfoHelper.createInstanceFromClass(RlFileInformation.class);
            fileInfo.setName(fileName);
            fileInfo.setUId(newId);
            fileInfo.setCreationDateTime(GetUTCDateTimeAsString());
            fileInfo.setStatus(1);
            fileInfo.setOriginalObjectId(0);
            fileInfo.setFileType(new RlFileType("OmeroFile"));
            fileInfo.setOmeroId((int)omeroId);
            
            postgresHelper.insertObject(TABLE_NAME, fileInfo);
            return fileInfo.getUId();
        }
        catch(Exception ex)
        {
            throw new Exception("[createOmeroFile] file '" + fileName + "'(" + Long.toString(omeroId) + ") throws exception: " + ex.toString());
        }
    }

	public long createDirectory(String directoryName) throws Exception
    {
        if(directoryName == null)
            throw new Exception("[createDirectory] directoryName is NULL");
        
        try
        {
            int newId = postgresHelper.createNewId(TABLE_NAME);
            RlFileInformation fileInfo = (RlFileInformation) MemberInfoHelper.createInstanceFromClass(RlFileInformation.class);
            fileInfo.setName(directoryName);
            fileInfo.setUId(newId);
            fileInfo.setCreationDateTime(GetUTCDateTimeAsString());
            fileInfo.setStatus(1);
            fileInfo.setOriginalObjectId(0);
            fileInfo.setFileType(new RlFileType("Folder"));
            fileInfo.setOmeroId(0);
            
            postgresHelper.insertObject(TABLE_NAME, fileInfo);
            return fileInfo.getUId();
        }
        catch(Exception ex)
        {
            throw new Exception("[createDirectory] '" + directoryName + "' throws exception: " + ex.toString());
        }
    }
    
    public void deleteFile(long fileId) throws Exception
    {
		RlFileInformation fileInfo = (RlFileInformation)postgresHelper.getObjectById(TABLE_NAME, fileId, RlFileInformation.class);
        if(fileInfo == null)
            throw new FileNotFoundException("[deleteFile] file with id " + Long.toString(fileId) + " does not exists." );

		boolean isRlFile = fileInfo.getFileType().getValue() == "ReceptorLightFile";

        FileAccessItem thisItem = null;
        for(FileAccessItem s : openedFiles)
            if (s.id == fileId)
            {
                thisItem = s;
                break;
            }
            
        if(thisItem != null)
            throw new InvalidParameterException("[deleteFile] Cannot delete file with id " + Long.toString(fileId) + " because it's opened." );
            
        postgresHelper.deleteById(TABLE_NAME, fileId);
		
		if(isRlFile == true)        
		{
	        File f = getFile(fileInfo.getName(), fileId);
    	    if(!f.exists())
    	        throw new FileNotFoundException("[deleteFile] file with id " + Long.toString(fileId) + " does not exists." );
            
    	    f.delete();
		}
    }

    public List<RlFileInformation> getFiles() throws Exception
    {
        List<RlFileInformation> files = (List<RlFileInformation>)postgresHelper.getAllObjects(TABLE_NAME, RlFileInformation.class);            
        return files;
    }
    
    public void openFile(long fileId, boolean forWriting) throws Exception
    {
        RlFileInformation fileInfo = (RlFileInformation)postgresHelper.getObjectById(TABLE_NAME, fileId, RlFileInformation.class);
        if(fileInfo == null)
            throw new FileNotFoundException("[openFile] file with id " + Long.toString(fileId) + " does not exists." );
            
        boolean alreadyOpened = false;
        for(FileAccessItem s : openedFiles)
            if (s.id == fileId)
            {
                alreadyOpened = true;
                break;
            }
            
        if(alreadyOpened == true)
            throw new InvalidParameterException("[openFile] file with id " + Long.toString(fileId) + " is already opened." );
            
        File f = getFile(fileInfo.getName(), fileId);
        if(!f.exists())
            throw new FileNotFoundException("[openFile] file with id " + Long.toString(fileId) + " does not exists." );
            
        FileAccessItem stat = new FileAccessItem();
        stat.id = fileId;
        stat.theFile = f;
        stat.forWriting = forWriting;

        if(stat.forWriting == true)
            stat.output = new BufferedOutputStream(Files.newOutputStream(stat.theFile.toPath(), WRITE, APPEND ));
        else
            stat.input = new FileInputStream(stat.theFile);

        openedFiles.add(stat);
        log.info("Open file with id: '" + Long.toString(fileId) + "'.");
    }   
    
    public void closeFile(long fileId) throws Exception
    {
        FileAccessItem item = null;
        for(FileAccessItem fai : openedFiles)
            if(fai.id == fileId)
            {
                item = fai;
                break;
            }
            
        if(item == null)
            return;

        if(item.input != null)
            item.input.close();

        if(item.output != null)
            item.output.close();

        openedFiles.remove(item);
        item = null;
        log.info("Close file with id: '" + Long.toString(fileId) + "'.");
    }
    
    public void appendFileData(long fileId, byte[] data) throws Exception
    {
        if(data == null)
            throw new FileNotFoundException("[appendFileData] data is null." );
            
        RlFileInformation fileInfo = (RlFileInformation)postgresHelper.getObjectById(TABLE_NAME, fileId, RlFileInformation.class);
        if(fileInfo == null)
            throw new FileNotFoundException("[appendFileData] file with id " + Long.toString(fileId) + " does not exists." );
            
        FileAccessItem thisItem = null;
        for(FileAccessItem s : openedFiles)
            if (s.id == fileId)
            {
                thisItem = s;
                break;
            }
            
        if(thisItem == null)
            throw new InvalidParameterException("[appendFileData] file with id " + Long.toString(fileId) + " is not opened." );
        
        if(thisItem.forWriting == false)
            throw new InvalidParameterException("[appendFileData] file with id " + Long.toString(fileId) + " is not opened with write access." );
            
        thisItem.output.write(data);
        
        fileInfo.setSize(fileInfo.getSize() + data.length);
        fileInfo.setCreationDateTime(GetUTCDateTimeAsString());
        postgresHelper.updateObject(TABLE_NAME, fileId, fileInfo);
    }
    
    public byte[] readFileData(long fileId, long dataLength) throws Exception
    {            
        RlFileInformation fileInfo = (RlFileInformation)postgresHelper.getObjectById(TABLE_NAME, fileId, RlFileInformation.class);
        if(fileInfo == null)
            throw new FileNotFoundException("[readFileData] file with id " + Long.toString(fileId) + " does not exists." );
            
        FileAccessItem thisItem = null;
        for(FileAccessItem s : openedFiles)
            if (s.id == fileId)
            {
                thisItem = s;
                break;
            }
            
        if(thisItem == null)
            throw new InvalidParameterException("[readFileData] file with id " + Long.toString(fileId) + " is not opened." );
        
        if(thisItem.forWriting == true)
            throw new InvalidParameterException("[readFileData] file with id " + Long.toString(fileId) + " is not opened with read access." );
            
        byte[] data = new byte[(int)dataLength];
        thisItem.input.read(data);
        return data;
    } 
    
    public long[] searchFiles(String searchPattern) throws Exception
    {
        String pattern = searchPattern.toLowerCase();
        List<Long> allIds = new ArrayList<Long>();
        List<RlFileInformation> allFiles = (List<RlFileInformation>)postgresHelper.getAllObjects(TABLE_NAME, RlFileInformation.class);
        for(RlFileInformation fi : allFiles)
            if(fi.getName().toLowerCase().contains(pattern))
                allIds.add(fi.getUId().longValue());
                
        long[] resultList = new long[allIds.size()];
        Iterator<Long> iLong = allIds.iterator();
        for(int i = 0; i < resultList.length; i++)
            resultList[i] = iLong.next().longValue();
            
        return resultList;
    }
    
    public RlFileInformation getFileInformation(long fileId) throws Exception
    {
        return (RlFileInformation)postgresHelper.getObjectById(TABLE_NAME, fileId, RlFileInformation.class);
    }

    public long setFileInformation(long fileId, RlFileInformation information) throws Exception
    {
        postgresHelper.updateObject(TABLE_NAME, fileId, information);
        return fileId;
    }
    
    private File getFile(String fileName, long id)
    {
        if(omeroDataDir.endsWith("/") == true)
            return new File(omeroDataDir +"RL_FILES/" + Long.toString(id) + "_" + fileName);
        else
            return new File(omeroDataDir +"/RL_FILES/" + Long.toString(id) + "_" + fileName);
    }
    
    private String GetUTCDateTimeAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcDate = sdf.format(new Date());

        return utcDate;
    }
}

class FileAccessItem
{
    public long id;
    public File theFile;
    public OutputStream output;
    public InputStream input;
    public boolean forWriting;
}
