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

public interface IReceptorLightSearchService extends ServiceInterface
{
    List<RlSearchResult> search(String pattern) throws Exception;
    List<RlSearchResult> searchByName(String pattern, String memberName) throws Exception;
    List<RlSearchResult> searchByType(String pattern, String typeName) throws Exception;
    List<RlSearchResult> searchByNameAndType(String pattern, String memberName, String typeName) throws Exception;    
}
    
