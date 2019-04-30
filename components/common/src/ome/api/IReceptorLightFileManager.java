 /*
  *   $Id$
  *
  *   Copyright (C) 2016 Friedrich Schiller University Jena.
  *   Mathematics and Computer Science Department 
  *   All rights reserved.
  *
  *   Written by: Daniel Walther, Daniel.Walther(at)uni-jena.de
  *
  */

package ome.api;

import java.util.List;
import ome.model.IObject;
import ome.model.core.*;

public interface IReceptorLightFileManager extends ServiceInterface
{    
    long createFile(String fileName) throws Exception;
    long createOmeroFile(String fileName, long omeroId) throws Exception;
    long createDirectory(String directoryName) throws Exception;
    void deleteFile(long fileId) throws Exception;
    List<RlFileInformation> getFiles() throws Exception;
    
    void openFile(long fileId, boolean forWriting) throws Exception;        
    void closeFile(long fileId) throws Exception;
    
    void appendFileData(long fileId, byte[] data) throws Exception;
    byte[] readFileData(long fileId, long dataLength) throws Exception;
    
    long[] searchFiles(String searchPattern) throws Exception;
    RlFileInformation getFileInformation(long fileId) throws Exception;
    long setFileInformation(long fileId, RlFileInformation information) throws Exception;
    
    String getDataDir() throws Exception;
}
    
