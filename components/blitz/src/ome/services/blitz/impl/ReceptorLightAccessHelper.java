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

package ome.services.blitz.impl;

import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ome.services.blitz.util.BlitzExecutor;
import ome.services.sessions.SessionManager;
import ome.system.EventContext;
import ome.system.Principal;

import omero.ServerError;
import omero.model.*;

import Ice.Current;

public class ReceptorLightAccessHelper
{
    final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public final int IS_PRIVATE = 0;
    public final int IS_READ_ONLY = 1;
    public final int IS_READ_ANNOTADE = 2;
    public final int IS_READ_WRITE = 3;
    public final int IS_READ_ONLY_GLOBAL = 4;
    public final int IS_READ_WRITE_GLOBAL = 5;
    
    public ReceptorLightAccessHelper()
    {
        
    }


    public Long getOwnUserId(ServiceFactoryI factory, SessionManager sm)
    {
        if((factory != null) && (sm != null))
        {
            String session = factory.sessionId().name;
            EventContext ectx = sm.getEventContext(new Principal(session));

            return ectx.getCurrentUserId();
        }

        return 0L;
    }
    
    public Long getOwnGroupId(ServiceFactoryI factory, SessionManager sm)
    {
        if((factory != null) && (sm != null))
        {
            String session = factory.sessionId().name;
            EventContext ectx = sm.getEventContext(new Principal(session));

            return ectx.getCurrentGroupId();
        }

        return 0L;
    }

    public List<Long> getOwnGroupIdList(ServiceFactoryI factory, SessionManager sm)
    {
        if((factory != null) && (sm != null))
        {
            String session = factory.sessionId().name;
            EventContext ectx = sm.getEventContext(new Principal(session));

            return ectx.getMemberOfGroupsList();
        }

        return null;
    }

    List<Long> getMembersOfGroup(Long groupId, Current current, ServiceFactoryI factory)
    {
        List<Long> members = new ArrayList<Long>();
        try
        {
            omero.api.IAdminPrx admin = factory.getAdminService(current);
            ExperimenterGroup group = admin.getGroup(groupId);
            List<Experimenter> experimenter = group.linkedExperimenterList();
            for (Experimenter e : experimenter)
            {
                members.add(e.getId().getValue());
            }
        }
        catch(Exception e)
        {

        }
        return members;
    }

    int getGroupPermission(Long groupId, Current current, ServiceFactoryI factory)
    {
        int permission = IS_PRIVATE;

        try
        {
            omero.api.IAdminPrx admin = factory.getAdminService(current);
            ExperimenterGroup group = admin.getGroup(groupId);
            Permissions perm = group.getDetails().getPermissions();

            if (perm.isGroupWrite())
                permission = IS_READ_WRITE;
            else if (perm.isGroupAnnotate())
                permission = IS_READ_ANNOTADE;
            else if (perm.isGroupRead())
                permission = IS_READ_ONLY;
            else
                permission = IS_PRIVATE;
        }
        catch(Exception e)
        {

        }

        return permission;
    }
    
    public List<? extends RlBaseObject> getReadableMembers(Current current, List<? extends RlBaseObject> inputList, ServiceFactoryI factory, SessionManager sm)
    {
        Long currentUserId = getOwnUserId(factory, sm);
        List<RlBaseObject> outputList = new ArrayList<RlBaseObject>();

	if(inputList == null)
            return outputList;

        for(RlBaseObject obj : inputList)
        {
            int objectPermission = getGroupPermission((long)obj.getOwnerGroupId().getValue(), current, factory);
            if(objectPermission == IS_PRIVATE) //if private, only owner can read it
            {
                if((long)obj.getOwnerId().getValue() == currentUserId)
                    outputList.add(obj);
            }
            else  //if not private all group member can read it
            {
                List<Long> objectGroupMembers = getMembersOfGroup((long) obj.getOwnerGroupId().getValue(), current, factory);
                if (objectGroupMembers.contains(currentUserId))
                    outputList.add(obj);
            }
        }

        return outputList;
    }

    public boolean isObjectReadable(RlBaseObject object, Current current, ServiceFactoryI factory, SessionManager sm)
    {
        Long currentUserId = getOwnUserId(factory, sm);

        int objectPermission = getGroupPermission((long)object.getOwnerGroupId().getValue(), current, factory);
        if(objectPermission == IS_PRIVATE) //if private, only owner can read it
        {
            return (long)object.getOwnerId().getValue() == currentUserId;
        }
        else  //if not private all group member can read it
        {
            List<Long> objectGroupMembers = getMembersOfGroup((long) object.getOwnerGroupId().getValue(), current, factory);
            return objectGroupMembers.contains(currentUserId);
        }
    }

    public boolean isObjectWritable(RlBaseObject object, Current current, ServiceFactoryI factory, SessionManager sm) throws ServerError
    {
        Long currentUserId = getOwnUserId(factory, sm);
        Long currentGroupId = getOwnGroupId(factory, sm);

        //the owner has always access
        if((long)object.getOwnerId().getValue() == currentUserId)
            return true;

        //the group the object is inside must have write or annotation permissions
        int objectPermission = getGroupPermission((long)object.getOwnerGroupId().getValue(), current, factory);
        if(objectPermission < IS_READ_WRITE)
            return false;

        //only in current group objects can modified
        if(currentGroupId != (long)object.getOwnerGroupId().getValue())
            return false;

        //only if user is inside objects group
        List<Long> objectGroupMembers = getMembersOfGroup((long)object.getOwnerGroupId().getValue(), current, factory);
        if(!objectGroupMembers.contains(currentUserId))
            return false;

        return true;
    }

    public boolean isObjectDeletable(RlBaseObject object, Current current, ServiceFactoryI factory, SessionManager sm)
    {
        Long currentUserId = getOwnUserId(factory, sm);

        //the owner has always access
        if((long)object.getOwnerId().getValue() == currentUserId)
            return true;

        //the groups admin has also always access
        try
        {
            omero.api.IAdminPrx admin = factory.getAdminService(current);
            ExperimenterGroup group = admin.getGroup((long) object.getOwnerGroupId().getValue());
            long groupOwnerId = group.getDetails(). getOwner().getId().getValue();
            if (currentUserId == groupOwnerId)
                return true;
        }
        catch(Exception e)
        {

        }

        return false;
    }
    
    public void initObject(RlBaseObject object, Long id, Long groupId)
    {
        object.setOwnerId(omero.rtypes.rint((int)(long)id));
        object.setOwnerGroupId(omero.rtypes.rint((int)(long)groupId));
        object.setCreationDateTime(omero.rtypes.rstring(GetUTCDateTimeAsString()));
    }
    
    public String GetUTCDateTimeAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        return utcTime;
    }
    
    public Date StringDateToDate(String StrDate)
    {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

        try
        {
            dateToReturn = (Date)dateFormat.parse(StrDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dateToReturn;
    }
}
