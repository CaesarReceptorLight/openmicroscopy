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
import ome.model.enums.*;

public interface IReceptorLightService extends ServiceInterface
{
    /*
    *   Plasmid methods
    */
    List<RlPlasmid> getPlasmids() throws Exception;
    RlPlasmid getPlasmid(long id) throws Exception;
    RlPlasmid createPlasmid(String name) throws Exception;
    RlPlasmid savePlasmid(RlPlasmid plasmid) throws Exception;
    void deletePlasmid(long id) throws Exception;
    
    /*
    *   Vector methods
    */
    List<RlVector> getVectors() throws Exception;
    RlVector getVector(long id) throws Exception;
    RlVector createVector(String name) throws Exception;
    RlVector saveVector(RlVector vector) throws Exception;
    void deleteVector(long id) throws Exception;

    /*
    *   Experiment methods
    */
    List<RlExperiment> getExperiments() throws Exception;
    RlExperiment getExperiment(long id) throws Exception;
    RlExperiment getExperimentByDatasetId(long datasetId) throws Exception;
    RlExperiment createExperiment(String name) throws Exception;
    RlExperiment saveExperiment(RlExperiment experiment) throws Exception;
    void deleteExperiment(long id) throws Exception;
    
    /*
    *   Chemical Substance methods
    */
    List<RlChemicalSubstance> getChemicalSubstances() throws Exception;
    RlChemicalSubstance getChemicalSubstance(long id) throws Exception;
    RlChemicalSubstance createChemicalSubstance(String name) throws Exception;
    RlChemicalSubstance saveChemicalSubstance(RlChemicalSubstance substance) throws Exception;
    void deleteChemicalSubstance(long id) throws Exception;
    
    /*
    *   Protein methods
    */
    List<RlProtein> getProteins() throws Exception;
    RlProtein getProtein(long id) throws Exception;
    RlProtein createProtein(String name) throws Exception;
    RlProtein saveProtein(RlProtein rrotein) throws Exception;
    void deleteProtein(long id) throws Exception;
    
    /*
    *   DNA methods
    */
    List<RlDna> getDnas() throws Exception;
    RlDna getDna(long id) throws Exception;
    RlDna createDna(String name) throws Exception;
    RlDna saveDna(RlDna dna) throws Exception;
    void deleteDna(long id) throws Exception;
    
    /*
    *   RNA methods
    */
    List<RlRna> getRnas() throws Exception;
    RlRna getRna(long id) throws Exception;
    RlRna createRna(String name) throws Exception;
    RlRna saveRna(RlRna rna) throws Exception;
    void deleteRna(long id) throws Exception;

    /*
    *   Solution methods
    */
    List<RlSolution> getSolutions() throws Exception;
    RlSolution getSolution(long id) throws Exception;
    RlSolution createSolution(String name) throws Exception;
    RlSolution saveSolution(RlSolution solution) throws Exception;
    void deleteSolution(long id) throws Exception;

    /*
    *   RestrictionEnzymes methods
    */
    List<RlRestrictionEnzyme> getRestrictionEnzymes() throws Exception;
    RlRestrictionEnzyme getRestrictionEnzyme(long id) throws Exception;
    RlRestrictionEnzyme createRestrictionEnzyme(String name) throws Exception;
    RlRestrictionEnzyme saveRestrictionEnzyme(RlRestrictionEnzyme restrictionEnzyme) throws Exception;
    void deleteRestrictionEnzyme(long id) throws Exception;

    /*
    *   Oligonucleotide methods
    */
    List<RlOligonucleotide> getOligonucleotides() throws Exception;
    RlOligonucleotide getOligonucleotide(long id) throws Exception;
    RlOligonucleotide createOligonucleotide(String name) throws Exception;
    RlOligonucleotide saveOligonucleotide(RlOligonucleotide oligonucleotide) throws Exception;
    void deleteOligonucleotide(long id) throws Exception;

    /*
    *   FluorescentProtein methods
    */
    List<RlFluorescentProtein> getFluorescentProteins() throws Exception;
    RlFluorescentProtein getFluorescentProtein(long id) throws Exception;
    RlFluorescentProtein createFluorescentProtein(String name) throws Exception;
    RlFluorescentProtein saveFluorescentProtein(RlFluorescentProtein fluorescentProtein) throws Exception;
    void deleteFluorescentProtein(long id) throws Exception;

    /*
    *   UsedMaterial methods
    */
    List<RlUsedMaterial> getUsedMaterials() throws Exception;
    RlUsedMaterial getUsedMaterial(long id) throws Exception;
    RlUsedMaterial createUsedMaterial() throws Exception;
    RlUsedMaterial saveUsedMaterial(RlUsedMaterial usedMaterial) throws Exception;
    void deleteUsedMaterial(long id) throws Exception;
    List<RlUsedMaterial> getUsedMaterialsBySourceId(long id) throws Exception;
    List<RlUsedMaterial> getUsedMaterialsByTargetId(long id) throws Exception;
    List<RlUsedMaterial> getUsedMaterialsBySourceType(RlUsedMaterialType sourceType) throws Exception;
    List<RlUsedMaterial> getUsedMaterialsByTargetType(RlUsedMaterialType targetType) throws Exception;
    
    /*
    *   SOP methods
    */
    List<RlSop> getSops() throws Exception;
    RlSop getSop(long id) throws Exception;
    RlSop createSop(String name) throws Exception;
    RlSop saveSop(RlSop sop) throws Exception;
    void deleteSop(long id) throws Exception;
    
    /*
    *   Helper methods for UserInterface
    */
    List<String> uihelpGetSectionsOfType(String typeName) throws Exception;
    List<String> uihelpGetElementsOfSections(String typeName, String sectionName) throws Exception;
    RlUiElementInfo uihelpGetElementInfo(String typeName, String elementName) throws Exception;
    
    /*
    *   Helper methods to get and set values
    */
    List<String> getMemberNames(IObject obj) throws Exception;
    
    IObject setMemberIObjectValue(IObject obj, String memberName, IObject value) throws Exception;
    IObject setMemberStringValue(IObject obj, String memberName, String value) throws Exception;
    IObject setMemberIntValue(IObject obj, String memberName, int value) throws Exception;
    
    IObject getMemberIObjectValue(IObject obj, String memberName) throws Exception;
    String getMemberStringValue(IObject obj, String memberName) throws Exception;
    int getMemberIntValue(IObject obj, String memberName) throws Exception;
}

