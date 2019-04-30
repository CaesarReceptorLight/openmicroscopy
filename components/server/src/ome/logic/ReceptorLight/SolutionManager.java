package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlSolution;

public class SolutionManager
{	
    private static final String TABLE_NAME = "solutiontable";
    
    private PostgresHelper postgresHelper;
    
    public SolutionManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlSolution.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlSolution> getSolutions() throws Exception
    {
    	return (List<RlSolution>)postgresHelper.getAllObjects(TABLE_NAME, RlSolution.class);
    }

    public RlSolution getSolution(long id) throws Exception
    {       	
    	return (RlSolution)postgresHelper.getObjectById(TABLE_NAME, id, RlSolution.class);
    }

    public RlSolution createSolution(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createSolution] name is NULL");
        
        RlSolution solution = (RlSolution) MemberInfoHelper.createInstanceFromClass(RlSolution.class);
        solution.setName(name);
        solution.setUId(postgresHelper.createNewId(TABLE_NAME));
        solution.setStatus(1);
        solution.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, solution);
		return solution;
    }
    
    public RlSolution saveSolution(RlSolution solution) throws Exception
    {
    	if(solution == null)
            throw new Exception("[saveSolution] solution is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, solution.getUId(), solution);
        return solution;
    }
    
    public void deleteSolution(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

