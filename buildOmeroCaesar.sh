#!/bin/bash

sourceDir="/home/omero/GIT/openmicroscopy/"
destinationDir="/home/omero/OMERO.server/"

shopt -s nocasematch

if [[ $1 == "clean" ]]
then
    python ${sourceDir}build.py clean
    exit 0
fi

python ${sourceDir}build.py $1

if [ $? -eq 0 ]
then

#own generated types
ownTypes=('RlBaseObject')
ownTypes+=('StringValue')
ownTypes+=('StringValueList')
ownTypes+=('IntValue')
ownTypes+=('IntValueList')
ownTypes+=('RlExperiment')
ownTypes+=('RlVector')
ownTypes+=('RlPlasmid')
ownTypes+=('RlProtein')
ownTypes+=('RlChemicalSubstance')
ownTypes+=('RlSolution')
ownTypes+=('RlDna')
ownTypes+=('RlRna')
ownTypes+=('RlRestrictionEnzyme')
ownTypes+=('RlFluorescentProtein')
ownTypes+=('RlOligonucleotide')
ownTypes+=('RlFileInformation')
ownTypes+=('RlSearchResult')
ownTypes+=('RlFileType')
ownTypes+=('RlUiElementInfo')
ownTypes+=('RlUsedMaterialType')
ownTypes+=('RlUsedMaterial')
ownTypes+=('RlSop')

filesToCopy=('include/omero/api/IReceptorLightService.ice')
filesToCopy+=('include/omero/api/IReceptorLightFileManager.ice')
filesToCopy+=('include/omero/api/IReceptorLightSearchService.ice')
filesToCopy+=('include/omero/API.ice')
filesToCopy+=('include/omero/Constants.ice')
filesToCopy+=('include/omero/ModelF.ice')
filesToCopy+=('include/omero/ServicesF.ice')
filesToCopy+=('include/omero/model/Units.ice')

for ownF in "${ownTypes[@]}"
  do
    b1="include/omero/model/"
    b2=".ice"
    filesToCopy+=($b1$ownF$b2)
    b1="include/omero/model/"
    b2="I.h"
    filesToCopy+=($b1$ownF$b2)
done

filesToCopy+=('lib/client/blitz.jar')
filesToCopy+=('lib/client/common.jar')
filesToCopy+=('lib/client/model-psql.jar')
filesToCopy+=('lib/client/insight.jar')

filesToCopy+=('lib/python/omero/all.py')
filesToCopy+=('lib/python/omero/ObjectFactoryRegistrar.py')
filesToCopy+=('lib/python/omero_API_ice.py')
filesToCopy+=('lib/python/omero_api_IReceptorLightService_ice.py')
filesToCopy+=('lib/python/omero_api_IReceptorLightFileManager_ice.py')
filesToCopy+=('lib/python/omero_api_IReceptorLightSearchService_ice.py')
filesToCopy+=('lib/python/omero_Constants_ice.py')
filesToCopy+=('lib/python/omero_ModelF_ice.py')
filesToCopy+=('lib/python/omero_ServicesF_ice.py')
filesToCopy+=('lib/python/omero_version.py')
filesToCopy+=('lib/python/omero_model_Units_ice.py')
filesToCopy+=('lib/python/omero/gateway/__init__.py')

for ownF in "${ownTypes[@]}"
  do
    b1="lib/python/omero_model_"
    b2="_ice.py"
    filesToCopy+=($b1$ownF$b2)
    b1="lib/python/omero_model_"
    b2="I.py"
    filesToCopy+=($b1$ownF$b2)
done

filesToCopy+=('lib/server/blitz.jar')
filesToCopy+=('lib/server/common.jar')
filesToCopy+=('lib/server/dsl.jar')
filesToCopy+=('lib/server/model-psql.jar')
filesToCopy+=('lib/server/rendering.jar')
filesToCopy+=('lib/server/romio.jar')
filesToCopy+=('lib/server/server.jar')

dst="dist/"
for file2copy in "${filesToCopy[@]}"
  do
    cp ${sourceDir}$dst${file2copy} ${destinationDir}${file2copy}
    echo Copy $file2copy
done

${destinationDir}bin/omero admin restart

fi

