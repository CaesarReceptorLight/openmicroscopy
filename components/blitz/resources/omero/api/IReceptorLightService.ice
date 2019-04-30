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

#ifndef RECEPTOR_LIGHT_SERVICE_ICE
#define RECEPTOR_LIGHT_SERVICE_ICE

#include <omero/ServicesF.ice>

module omero
{
    module api
    {
        ["java:type:java.util.ArrayList<omero.model.RlPlasmid>:java.util.List<omero.model.RlPlasmid>"]
        sequence<omero::model::RlPlasmid> PlasmidList;
        
        ["java:type:java.util.ArrayList<omero.model.RlVector>:java.util.List<omero.model.RlVector>"]
        sequence<omero::model::RlVector> VectorList;

        ["java:type:java.util.ArrayList<omero.model.RlExperiment>:java.util.List<omero.model.RlExperiment>"]
        sequence<omero::model::RlExperiment> ExperimentList;
        
        ["java:type:java.util.ArrayList<omero.model.RlChemicalSubstance>:java.util.List<omero.model.RlChemicalSubstance>"]
        sequence<omero::model::RlChemicalSubstance> ChemicalSubstanceList;
        
        ["java:type:java.util.ArrayList<String>:java.util.List<String>"]
        sequence<string> StringList;
        
        ["java:type:java.util.ArrayList<omero.model.RlProtein>:java.util.List<omero.model.RlProtein>"]
        sequence<omero::model::RlProtein> ProteinList;
        
        ["java:type:java.util.ArrayList<omero.model.RlDna>:java.util.List<omero.model.RlDna>"]
        sequence<omero::model::RlDna> DnaList;
        
        ["java:type:java.util.ArrayList<omero.model.RlRna>:java.util.List<omero.model.RlRna>"]
        sequence<omero::model::RlRna> RnaList;

        ["java:type:java.util.ArrayList<omero.model.RlSolution>:java.util.List<omero.model.RlSolution>"]
        sequence<omero::model::RlSolution> SolutionList;

        ["java:type:java.util.ArrayList<omero.model.RlRestrictionEnzyme>:java.util.List<omero.model.RlRestrictionEnzyme>"]
        sequence<omero::model::RlRestrictionEnzyme> RestrictionEnzymeList;

        ["java:type:java.util.ArrayList<omero.model.RlFluorescentProtein>:java.util.List<omero.model.RlFluorescentProtein>"]
        sequence<omero::model::RlFluorescentProtein> FluorescentProteinList;

        ["java:type:java.util.ArrayList<omero.model.RlOligonucleotide>:java.util.List<omero.model.RlOligonucleotide>"]
        sequence<omero::model::RlOligonucleotide> OligonucleotideList;

        ["java:type:java.util.ArrayList<omero.model.RlUsedMaterial>:java.util.List<omero.model.RlUsedMaterial>"]
        sequence<omero::model::RlUsedMaterial> UsedMaterialList;

        ["java:type:java.util.ArrayList<omero.model.RlSop>:java.util.List<omero.model.RlSop>"]
        sequence<omero::model::RlSop> SopList;
    
        ["ami", "amd"] interface IReceptorLightService extends ServiceInterface
        {
            /*
            *   Plasmid methods
            */
            idempotent PlasmidList getPlasmids() throws ServerError;
            idempotent omero::model::RlPlasmid getPlasmid(long id) throws ServerError;
            idempotent omero::model::RlPlasmid createPlasmid(string name) throws ServerError;
            idempotent omero::model::RlPlasmid savePlasmid(omero::model::RlPlasmid plasmid) throws ServerError;
            idempotent void deletePlasmid(long id) throws ServerError;
            
            /*
            *   Vector methods
            */
            idempotent VectorList getVectors() throws ServerError;
            idempotent omero::model::RlVector getVector(long id) throws ServerError;
            idempotent omero::model::RlVector createVector(string name) throws ServerError;
            idempotent omero::model::RlVector saveVector(omero::model::RlVector vector) throws ServerError;
            idempotent void deleteVector(long id) throws ServerError;

            /*
            *   Experiment methods
            */
            idempotent ExperimentList getExperiments() throws ServerError;
            idempotent omero::model::RlExperiment getExperiment(long id) throws ServerError;
            idempotent omero::model::RlExperiment getExperimentByDatasetId(long datasetId) throws ServerError;
            idempotent omero::model::RlExperiment createExperiment(string name) throws ServerError;
            idempotent omero::model::RlExperiment saveExperiment(omero::model::RlExperiment experiment) throws ServerError;
            idempotent void deleteExperiment(long id) throws ServerError;
            
            /*
            *   ChemicalSubstance methods
            */
            idempotent ChemicalSubstanceList getChemicalSubstances() throws ServerError;
            idempotent omero::model::RlChemicalSubstance getChemicalSubstance(long id) throws ServerError;
            idempotent omero::model::RlChemicalSubstance createChemicalSubstance(string name) throws ServerError;
            idempotent omero::model::RlChemicalSubstance saveChemicalSubstance(omero::model::RlChemicalSubstance substance) throws ServerError;
            idempotent void deleteChemicalSubstance(long id) throws ServerError;
            
            /*
            *   Protein methods
            */
            idempotent ProteinList getProteins() throws ServerError;
            idempotent omero::model::RlProtein getProtein(long id) throws ServerError;
            idempotent omero::model::RlProtein createProtein(string name) throws ServerError;
            idempotent omero::model::RlProtein saveProtein(omero::model::RlProtein protein) throws ServerError;
            idempotent void deleteProtein(long id) throws ServerError;
            
            /*
            *   DNA methods
            */
            idempotent DnaList getDnas() throws ServerError;
            idempotent omero::model::RlDna getDna(long id) throws ServerError;
            idempotent omero::model::RlDna createDna(string name) throws ServerError;
            idempotent omero::model::RlDna saveDna(omero::model::RlDna dna) throws ServerError;
            idempotent void deleteDna(long id) throws ServerError;
            
            /*
            *   RNA methods
            */
            idempotent RnaList getRnas() throws ServerError;
            idempotent omero::model::RlRna getRna(long id) throws ServerError;
            idempotent omero::model::RlRna createRna(string name) throws ServerError;
            idempotent omero::model::RlRna saveRna(omero::model::RlRna Rna) throws ServerError;
            idempotent void deleteRna(long id) throws ServerError;

            /*
            *   Solution methods
            */
            idempotent SolutionList getSolutions() throws ServerError;
            idempotent omero::model::RlSolution getSolution(long id) throws ServerError;
            idempotent omero::model::RlSolution createSolution(string name) throws ServerError;
            idempotent omero::model::RlSolution saveSolution(omero::model::RlSolution solution) throws ServerError;
            idempotent void deleteSolution(long id) throws ServerError;

            /*
            *   RestrictionEnzyme methods
            */
            idempotent RestrictionEnzymeList getRestrictionEnzymes() throws ServerError;
            idempotent omero::model::RlRestrictionEnzyme getRestrictionEnzyme(long id) throws ServerError;
            idempotent omero::model::RlRestrictionEnzyme createRestrictionEnzyme(string name) throws ServerError;
            idempotent omero::model::RlRestrictionEnzyme saveRestrictionEnzyme(omero::model::RlRestrictionEnzyme restrictionEnzyme) throws ServerError;
            idempotent void deleteRestrictionEnzyme(long id) throws ServerError;

            /*
            *   FluorescentProtein methods
            */
            idempotent FluorescentProteinList getFluorescentProteins() throws ServerError;
            idempotent omero::model::RlFluorescentProtein getFluorescentProtein(long id) throws ServerError;
            idempotent omero::model::RlFluorescentProtein createFluorescentProtein(string name) throws ServerError;
            idempotent omero::model::RlFluorescentProtein saveFluorescentProtein(omero::model::RlFluorescentProtein fluorescentProtein) throws ServerError;
            idempotent void deleteFluorescentProtein(long id) throws ServerError;

            /*
            *   Oligonucleotide methods
            */
            idempotent OligonucleotideList getOligonucleotides() throws ServerError;
            idempotent omero::model::RlOligonucleotide getOligonucleotide(long id) throws ServerError;
            idempotent omero::model::RlOligonucleotide createOligonucleotide(string name) throws ServerError;
            idempotent omero::model::RlOligonucleotide saveOligonucleotide(omero::model::RlOligonucleotide oligonucleotide) throws ServerError;
            idempotent void deleteOligonucleotide(long id) throws ServerError;

            /*
            *   UsedMaterial methods
            */
            idempotent UsedMaterialList getUsedMaterials() throws ServerError;
            idempotent omero::model::RlUsedMaterial getUsedMaterial(long id) throws ServerError;
            idempotent omero::model::RlUsedMaterial createUsedMaterial() throws ServerError;
            idempotent omero::model::RlUsedMaterial saveUsedMaterial(omero::model::RlUsedMaterial usedMaterial) throws ServerError;
            idempotent void deleteUsedMaterial(long id) throws ServerError;
            idempotent UsedMaterialList getUsedMaterialsBySourceId(long id) throws ServerError;
            idempotent UsedMaterialList getUsedMaterialsByTargetId(long id) throws ServerError;
            idempotent UsedMaterialList getUsedMaterialsBySourceType(omero::model::RlUsedMaterialType sourceType) throws ServerError;
            idempotent UsedMaterialList getUsedMaterialsByTargetType(omero::model::RlUsedMaterialType targetType) throws ServerError;

            /*
            *   SOP methods
            */
            idempotent SopList getSops() throws ServerError;
            idempotent omero::model::RlSop getSop(long id) throws ServerError;
            idempotent omero::model::RlSop createSop(string name) throws ServerError;
            idempotent omero::model::RlSop saveSop(omero::model::RlSop sop) throws ServerError;
            idempotent void deleteSop(long id) throws ServerError;
			
            /*
            *   Helper methods for UserInterface
            */
            idempotent StringList uihelpGetSectionsOfType(string typeName) throws ServerError;
            idempotent StringList uihelpGetElementsOfSections(string typeName, string sectionName) throws ServerError;
            idempotent omero::model::RlUiElementInfo uihelpGetElementInfo(string typeName, string elementName) throws ServerError;
            
            /*
            *   Helper methods to get and get values
            */
            idempotent StringList getMemberNames(omero::model::IObject obj) throws ServerError;
            
            idempotent omero::model::IObject setMemberIObjectValue(omero::model::IObject obj, string memberName, omero::model::IObject value) throws ServerError;
            idempotent omero::model::IObject setMemberStringValue(omero::model::IObject obj, string memberName, string value) throws ServerError;
            idempotent omero::model::IObject setMemberIntValue(omero::model::IObject obj, string memberName, int value) throws ServerError;
            
            idempotent omero::model::IObject getMemberIObjectValue(omero::model::IObject obj, string memberName) throws ServerError;
            idempotent string getMemberStringValue(omero::model::IObject obj, string memberName) throws ServerError;
            idempotent int getMemberIntValue(omero::model::IObject obj, string memberName) throws ServerError;
        };
    };
};

#endif
