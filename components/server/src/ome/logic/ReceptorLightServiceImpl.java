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

package ome.logic;

import ome.annotations.RolesAllowed;
import ome.api.ServiceInterface;
import ome.api.IReceptorLightService;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


import org.springframework.transaction.annotation.Transactional;

import ome.model.IObject;
import ome.model.core.*;
import ome.model.enums.*;
import ome.logic.ReceptorLight.*;
import ome.system.PreferenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional(readOnly = true)
public class ReceptorLightServiceImpl extends AbstractLevel1Service implements IReceptorLightService
{	
    private final Logger log = LoggerFactory.getLogger(ReceptorLightServiceImpl.class);
    
    private PostgresHelper queryHelper;
    
    private PlasmidManager plasmidManager;
    private VectorManager vectorManager;
    private ExperimentManager experimentManager;
    private ChemicalSubstancesManager substanceManager;
    private ProteinManager proteinManager;
    private DnaManager dnaManager;
    private RnaManager rnaManager;
    private SolutionManager solutionManager;
    private RestrictionEnzymesManager restrictionEnzymesManager;
    private OligonucleotideManager oligonucleotideManager;
    private FluorescentProteinManager fluorescentProteinManager;
    private UsedMaterialManager usedMaterialManager;
    private SopManager sopManager;
    private UIHelper uiHelper;
    
    private transient PreferenceContext prefs;

    /**
     * Protects all access to the configuration properties.
     */
    private final transient ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public ReceptorLightServiceImpl()
    {
        
    }
    
    public Class<? extends ServiceInterface> getServiceInterface()
    {        
        return IReceptorLightService.class;
    }
    
    /**
     * {@link PreferenceContext} setter for dependency injection.
     * 
     * @param prefs the preference context
     * @see ome.services.util.BeanHelper#throwIfAlreadySet(Object, Object)
     */
    public final void setPreferenceContext(PreferenceContext prefs)
    {
        getBeanHelper().throwIfAlreadySet(this.prefs, prefs);
        this.prefs = prefs;
        
        try
        {
            String rl_db_name = getInternalValue("omero.db.rl.name");
            String rl_db_user = getInternalValue("omero.db.rl.user");
            String rl_db_pass = getInternalValue("omero.db.rl.pass");
            String omeroDataDir = getInternalValue("omero.data.dir");

            queryHelper = new PostgresHelper(log);
            queryHelper.openDatabase(rl_db_name, rl_db_user, rl_db_pass);
        	
            experimentManager = new ExperimentManager(queryHelper);
            plasmidManager = new PlasmidManager(queryHelper);
            vectorManager = new VectorManager(queryHelper);
            substanceManager = new ChemicalSubstancesManager(queryHelper);
            proteinManager = new ProteinManager(queryHelper);
            dnaManager = new DnaManager(queryHelper);
            rnaManager = new RnaManager(queryHelper);
            solutionManager = new SolutionManager(queryHelper);
            restrictionEnzymesManager = new RestrictionEnzymesManager(queryHelper);
            oligonucleotideManager = new OligonucleotideManager(queryHelper);
            fluorescentProteinManager = new FluorescentProteinManager(queryHelper);
            usedMaterialManager = new UsedMaterialManager(queryHelper);
            sopManager = new SopManager(queryHelper);
            uiHelper = new UIHelper(omeroDataDir);
        }
        catch(Exception ex)
        {
        	log.error("Exception while starting ReceptorLightService: " + ex.getMessage());
        }
    }

    private String getInternalValue(String key)
    {
        key = prefs.resolveAlias(key);
        lock.readLock().lock();
        try
        {
            if (prefs.checkDatabase(key))
            {
                return prefs.getProperty(key);
            }
            return null;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlPlasmid> getPlasmids() throws Exception
    {
        return plasmidManager.getPlasmids();
    }
    
    @Override
    @RolesAllowed("user")
    public RlPlasmid getPlasmid(long id) throws Exception
    {
        return plasmidManager.getPlasmid(id);
    }    
    
    @Override
    @RolesAllowed("user")
    public RlPlasmid createPlasmid(String name) throws Exception
    {
        return plasmidManager.createPlasmid(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlPlasmid savePlasmid(RlPlasmid plasmid) throws Exception
    {
        return plasmidManager.savePlasmid(plasmid);
    }
    
    @Override
    @RolesAllowed("user")
    public void deletePlasmid(long id) throws Exception
    {
        plasmidManager.deletePlasmid(id);
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlVector> getVectors() throws Exception
    {
        return vectorManager.getVectors();
    }
    
    @Override
    @RolesAllowed("user")
    public RlVector getVector(long id) throws Exception
    {
        return vectorManager.getVector(id);
    }
    
    @Override
    @RolesAllowed("user")
    public RlVector createVector(String name) throws Exception
    {
        return vectorManager.createVector(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlVector saveVector(RlVector vector) throws Exception
    {
        return vectorManager.saveVector(vector);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteVector(long id) throws Exception
    {
        vectorManager.deleteVector(id);
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlExperiment> getExperiments() throws Exception
    {    	
        return experimentManager.getExperiments();
    }
    
    @Override
    @RolesAllowed("user")
    public RlExperiment getExperiment(long id) throws Exception
    {
        return experimentManager.getExperiment(id);
    }

	@Override
    @RolesAllowed("user")
    public RlExperiment getExperimentByDatasetId(long datasetId) throws Exception
    {
        List<RlExperiment> experiments = experimentManager.getExperiments();
        List<RlExperiment> possibleExperiments = new ArrayList<>();
        List<RlExperiment> activeExperiments = new ArrayList<>();

		if(experiments == null)
			return null;

        //1. get all experiments, which are connected to given dataset
		for(RlExperiment exp : experiments)
		{
			long expDatasetId = exp.getDatasetId();
        	if(expDatasetId == datasetId)
				possibleExperiments.add(exp);
		}

        //2. get from experiments the one with active status
        for(RlExperiment exp : possibleExperiments)
        {
            long status = exp.getStatus();
            if((status & 0x01) == 0x01)
                activeExperiments.add(exp);        
        }

        //3 TODO: what happens if more than one experiment is active --> should never happens
        if(activeExperiments.size() > 0)
            return activeExperiments.get(0);

		return null;
    }
    
    @Override
    @RolesAllowed("user")
    public RlExperiment createExperiment(String name) throws Exception
    {
        return experimentManager.createExperiment(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlExperiment saveExperiment(RlExperiment experiment) throws Exception
    {
        return experimentManager.saveExperiment(experiment);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteExperiment(long id) throws Exception
    {
    	experimentManager.deleteExperiment(id);
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlChemicalSubstance> getChemicalSubstances() throws Exception
    {
        return substanceManager.getChemicalSubstances();
    }
    
    @Override
    @RolesAllowed("user")
    public RlChemicalSubstance getChemicalSubstance(long id) throws Exception
    {
        return substanceManager.getChemicalSubstance(id);
    }
    
    @Override
    @RolesAllowed("user")
    public RlChemicalSubstance createChemicalSubstance(String name) throws Exception
    {
        return substanceManager.createChemicalSubstance(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlChemicalSubstance saveChemicalSubstance(RlChemicalSubstance substance) throws Exception
    {
        return substanceManager.saveChemicalSubstance(substance);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteChemicalSubstance(long id) throws Exception
    {
        substanceManager.deleteChemicalSubstance(id);
    }
    
    @Override
    @RolesAllowed("user")
    public List<String> getMemberNames(IObject obj) throws Exception
    {
    	List<String> list = new ArrayList<String>();

        List<MemberInfo> memberInfos = MemberInfoHelper.getMemberInfosFromType(obj.getClass());

        for(MemberInfo mi : memberInfos)
            list.add(mi.name);
        
        return list;
    }

    @Override
    @RolesAllowed("user")
    public IObject setMemberIObjectValue(IObject obj, String memberName, IObject value) throws Exception
    {
        MemberInfoHelper.setValueToMethod("set" + memberName, value, obj);
        return obj;
    }
    
    @Override
    @RolesAllowed("user")
    public IObject setMemberStringValue(IObject obj, String memberName, String value) throws Exception
    {
        MemberInfoHelper.setValueToMethod("set" + memberName, value, obj);
        return obj;
    }
    
    @Override
    @RolesAllowed("user")
    public IObject setMemberIntValue(IObject obj, String memberName, int value) throws Exception
    {
        MemberInfoHelper.setValueToMethod("set" + memberName, value, obj);
        return obj;
    }
    
    @Override
    @RolesAllowed("user")
    public IObject getMemberIObjectValue(IObject obj, String memberName) throws Exception
    {
        return (IObject)MemberInfoHelper.getValueFromMethod("get" + memberName, obj);
    }
    
    @Override
    @RolesAllowed("user")
    public String getMemberStringValue(IObject obj, String memberName) throws Exception
    {
        return (String)MemberInfoHelper.getValueFromMethod("get" + memberName, obj);
    }
    
    @Override
    @RolesAllowed("user")
    public int getMemberIntValue(IObject obj, String memberName) throws Exception
    {
        return (int)MemberInfoHelper.getValueFromMethod("get" + memberName, obj);
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlProtein> getProteins() throws Exception
    {
        return proteinManager.getProteins();
    }
    
    @Override
    @RolesAllowed("user")
    public RlProtein getProtein(long id) throws Exception
    {
        return proteinManager.getProtein(id);
    }    
    
    @Override
    @RolesAllowed("user")
    public RlProtein createProtein(String name) throws Exception
    {
        return proteinManager.createProtein(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlProtein saveProtein(RlProtein protein) throws Exception
    {
        return proteinManager.saveProtein(protein);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteProtein(long id) throws Exception
    {
        proteinManager.deleteProtein(id);
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlDna> getDnas() throws Exception
    {
        return dnaManager.getDnas();
    }
    
    @Override
    @RolesAllowed("user")
    public RlDna getDna(long id) throws Exception
    {
        return dnaManager.getDna(id);
    }    
    
    @Override
    @RolesAllowed("user")
    public RlDna createDna(String name) throws Exception
    {
        return dnaManager.createDna(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlDna saveDna(RlDna dna) throws Exception
    {
        return dnaManager.saveDna(dna);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteDna(long id) throws Exception
    {
        dnaManager.deleteDna(id);
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlRna> getRnas() throws Exception
    {
        return rnaManager.getRnas();
    }
    
    @Override
    @RolesAllowed("user")
    public RlRna getRna(long id) throws Exception
    {
        return rnaManager.getRna(id);
    }    
    
    @Override
    @RolesAllowed("user")
    public RlRna createRna(String name) throws Exception
    {
        return rnaManager.createRna(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlRna saveRna(RlRna rna) throws Exception
    {
        return rnaManager.saveRna(rna);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteRna(long id) throws Exception
    {
        rnaManager.deleteRna(id);
    }

    @Override
    @RolesAllowed("user")
    public List<RlSolution> getSolutions() throws Exception
    {
        return solutionManager.getSolutions();
    }
    
    @Override
    @RolesAllowed("user")
    public RlSolution getSolution(long id) throws Exception
    {
        return solutionManager.getSolution(id);
    }    
    
    @Override
    @RolesAllowed("user")
    public RlSolution createSolution(String name) throws Exception
    {
        return solutionManager.createSolution(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlSolution saveSolution(RlSolution solution) throws Exception
    {
        return solutionManager.saveSolution(solution);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteSolution(long id) throws Exception
    {
        solutionManager.deleteSolution(id);
    }

    @Override
    @RolesAllowed("user")
    public List<RlRestrictionEnzyme> getRestrictionEnzymes() throws Exception
    {
        return restrictionEnzymesManager.getRestrictionEnzymes();
    }
    
    @Override
    @RolesAllowed("user")
    public RlRestrictionEnzyme getRestrictionEnzyme(long id) throws Exception
    {
        return restrictionEnzymesManager.getRestrictionEnzyme(id);
    }    
    
    @Override
    @RolesAllowed("user")
    public RlRestrictionEnzyme createRestrictionEnzyme(String name) throws Exception
    {
        return restrictionEnzymesManager.createRestrictionEnzyme(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlRestrictionEnzyme saveRestrictionEnzyme(RlRestrictionEnzyme restrictionEnzyme) throws Exception
    {
        return restrictionEnzymesManager.saveRestrictionEnzyme(restrictionEnzyme);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteRestrictionEnzyme(long id) throws Exception
    {
        restrictionEnzymesManager.deleteRestrictionEnzyme(id);
    }

    @Override
    @RolesAllowed("user")
    public List<RlOligonucleotide> getOligonucleotides() throws Exception
    {
        return oligonucleotideManager.getOligonucleotides();
    }
    
    @Override
    @RolesAllowed("user")
    public RlOligonucleotide getOligonucleotide(long id) throws Exception
    {
        return oligonucleotideManager.getOligonucleotide(id);
    }    
    
    @Override
    @RolesAllowed("user")
    public RlOligonucleotide createOligonucleotide(String name) throws Exception
    {
        return oligonucleotideManager.createOligonucleotide(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlOligonucleotide saveOligonucleotide(RlOligonucleotide oligonucleotide) throws Exception
    {
        return oligonucleotideManager.saveOligonucleotide(oligonucleotide);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteOligonucleotide(long id) throws Exception
    {
        oligonucleotideManager.deleteOligonucleotide(id);
    }

    @Override
    @RolesAllowed("user")
    public List<RlFluorescentProtein> getFluorescentProteins() throws Exception
    {
        return fluorescentProteinManager.getFluorescentProteins();
    }
    
    @Override
    @RolesAllowed("user")
    public RlFluorescentProtein getFluorescentProtein(long id) throws Exception
    {
        return fluorescentProteinManager.getFluorescentProtein(id);
    }    
    
    @Override
    @RolesAllowed("user")
    public RlFluorescentProtein createFluorescentProtein(String name) throws Exception
    {
        return fluorescentProteinManager.createFluorescentProtein(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlFluorescentProtein saveFluorescentProtein(RlFluorescentProtein fluorescentProtein) throws Exception
    {
        return fluorescentProteinManager.saveFluorescentProtein(fluorescentProtein);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteFluorescentProtein(long id) throws Exception
    {
        fluorescentProteinManager.deleteFluorescentProtein(id);
    }

    @Override
    @RolesAllowed("user")
    public List<RlUsedMaterial> getUsedMaterials() throws Exception
    {
        return usedMaterialManager.getUsedMaterials();
    }
    
    @Override
    @RolesAllowed("user")
    public RlUsedMaterial getUsedMaterial(long id) throws Exception
    {
        return usedMaterialManager.getUsedMaterial(id);
    }    
    
    @Override
    @RolesAllowed("user")
    public RlUsedMaterial createUsedMaterial() throws Exception
    {
        return usedMaterialManager.createUsedMaterial();
    }
    
    @Override
    @RolesAllowed("user")
    public RlUsedMaterial saveUsedMaterial(RlUsedMaterial usedMaterial) throws Exception
    {
        return usedMaterialManager.saveUsedMaterial(usedMaterial);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteUsedMaterial(long id) throws Exception
    {
        usedMaterialManager.deleteUsedMaterial(id);
    }

    @Override
    @RolesAllowed("user")
    public List<RlUsedMaterial> getUsedMaterialsBySourceId(long id) throws Exception
    {
        return usedMaterialManager.getUsedMaterialsBySourceId(id);
    }

    @Override
    @RolesAllowed("user")
    public List<RlUsedMaterial> getUsedMaterialsByTargetId(long id) throws Exception
    {
        return usedMaterialManager.getUsedMaterialsByTargetId(id);
    }

    @Override
    @RolesAllowed("user")
    public List<RlUsedMaterial> getUsedMaterialsBySourceType(RlUsedMaterialType sourceType) throws Exception
    {
        return usedMaterialManager.getUsedMaterialsBySourceType(sourceType);
    }

    @Override
    @RolesAllowed("user")
    public List<RlUsedMaterial> getUsedMaterialsByTargetType(RlUsedMaterialType targetType) throws Exception
    {
        return usedMaterialManager.getUsedMaterialsByTargetType(targetType);
    }

    @Override
    @RolesAllowed("user")
    public List<RlSop> getSops() throws Exception
    {
    	return sopManager.getSops();
    }

    @Override
    @RolesAllowed("user")
    public RlSop getSop(long id) throws Exception
    {       	
    	return sopManager.getSop(id);
    }

    @Override
    @RolesAllowed("user")
    public RlSop createSop(String name) throws Exception
    {        
        return sopManager.createSop(name);
    }
    
    @Override
    @RolesAllowed("user")
    public RlSop saveSop(RlSop sop) throws Exception
    {
        return sopManager.saveSop(sop);
    }

    @Override
    @RolesAllowed("user")
    public void deleteSop(long id) throws Exception
    {       	
    	sopManager.deleteSop(id);
    }

    @Override
    @RolesAllowed("user")
    public List<String> uihelpGetSectionsOfType(String typeName) throws Exception
    {
        return uiHelper.uihelpGetSectionsOfType(typeName);
    }

    @Override
    @RolesAllowed("user")
    public List<String> uihelpGetElementsOfSections(String typeName, String sectionName) throws Exception
    {
        return uiHelper.uihelpGetElementsOfSections(typeName, sectionName);
    }

    @Override
    @RolesAllowed("user")
    public RlUiElementInfo uihelpGetElementInfo(String typeName, String elementName) throws Exception
    {
        return uiHelper.uihelpGetElementInfo(typeName, elementName);
    }
}

