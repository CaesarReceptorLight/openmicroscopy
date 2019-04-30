/*
  *   $Id$
  *
  * Copyright (C) 2016 Friedrich Schiller University Jena.
  * Mathematics and Computer Science Department 
  * All rights reserved.
  *
  * Written by: Daniel Walther, Daniel.Walther(at)uni-jena.de
  *
  */

#ifndef RECEPTOR_LIGHT_FILE_MANAGER_ICE
#define RECEPTOR_LIGHT_FILE_MANAGER_ICE

#include <omero/ServicesF.ice>

module omero
{
    module api
    {
        ["java:type:java.util.ArrayList<omero.model.RlFileInformation>:java.util.List<omero.model.RlFileInformation>"]
        sequence<omero::model::RlFileInformation> FileInfoList;

        ["ami", "amd"] interface IReceptorLightFileManager extends ServiceInterface
        {
            idempotent long createFile(string fileName) throws ServerError;
            idempotent long createOmeroFile(string fileName, long omeroId) throws ServerError;
            idempotent long createDirectory(string directoryName) throws ServerError;
            idempotent void deleteFile(long fileId) throws ServerError;
            idempotent FileInfoList getFiles() throws ServerError;
            
            idempotent void openFile(long fileId, bool forWriting) throws ServerError;        
            idempotent void closeFile(long fileId) throws ServerError;
            
            idempotent void appendFileData(long fileId, Ice::ByteSeq data) throws ServerError;
            idempotent Ice::ByteSeq readFileData(long fileId, long dataLength) throws ServerError; 
            
            idempotent Ice::LongSeq searchFiles(string searchPattern) throws ServerError;
            idempotent omero::model::RlFileInformation getFileInformation(long fileId) throws ServerError;
            idempotent long setFileInformation(long fileId, omero::model::RlFileInformation information) throws ServerError;
            
            idempotent string getDataDir() throws ServerError;
        };
    };
};

#endif
