package ome.logic.ReceptorLight;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ome.model.core.RlUiElementInfo;

public class UIHelper
{
    private ArrayList<UiInfo> uiInfos;

    public UIHelper(String omeroDataPath)
    {    	
        try
        {
            uiInfos = new ArrayList<>();

			File fXmlFile;
			if(omeroDataPath.endsWith("/") == true)				
				fXmlFile = new File(omeroDataPath + "UiTypeInformation.xml");
			else
				fXmlFile = new File(omeroDataPath + "/UiTypeInformation.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList typeList = doc.getElementsByTagName("Type");

            //loop throw each type
            for (int curType = 0; curType < typeList.getLength(); curType++)
            {
                Node typeNode = typeList.item(curType);

                if (typeNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element typeElement = (Element) typeNode;

                    UiInfo uiInfo = new UiInfo();
                    uiInfo.typeName = typeElement.getAttribute("Name");
                    uiInfo.sectionInfo = new ArrayList<>();
                    NodeList sectionList = typeElement.getElementsByTagName("Section");

                    //loop throw each section
                    for (int curSec = 0; curSec < sectionList.getLength(); curSec++)
                    {
                        Node sectionNode = sectionList.item(curSec);

                        if (sectionNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element sectionElement = (Element) sectionNode;

                            SectionInfo section = new SectionInfo();
                            section.sectionName = sectionElement.getAttribute("Name");
                            section.elementInfos = new ArrayList<>();
                            NodeList elementList = sectionElement.getElementsByTagName("Element");

                            //loop throw each element
                            for (int curEle = 0; curEle < elementList.getLength(); curEle++)
                            {
                                Node elementNode = elementList.item(curEle);

                                if (elementNode.getNodeType() == Node.ELEMENT_NODE)
                                {
                                    Element eleElement = (Element) elementNode;

                                    ElementInfo elem = new ElementInfo();
                                    elem.rawName = eleElement.getAttribute("RawName");
                                    elem.uiName = eleElement.getAttribute("UiName");
                                    elem.uiHelp = eleElement.getAttribute("UiHelp");
                                    elem.index = Integer.parseInt(eleElement.getAttribute("Index"));
                                    if(eleElement.getAttribute("Visible").equals("True"))
                                        elem.isVisible = true;
                                    else
                                        elem.isVisible = false;

                                    section.elementInfos.add(elem);
                                }
                            }

                            uiInfo.sectionInfo.add(section);
                        }
                    }

                    uiInfos.add(uiInfo);

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public List<String> uihelpGetSectionsOfType(String typeName) throws Exception
    {
        UiInfo info = GetInfoByName(typeName);
        if (info != null)
        {
            List<String> sections = new ArrayList<String>();
            for (int s = 0; s < info.sectionInfo.size(); s++) {
                sections.add(info.sectionInfo.get(s).sectionName);
            }
            return sections;
        }

        return null;
    }

    public List<String> uihelpGetElementsOfSections(String typeName, String sectionName) throws Exception
    {
        SectionInfo section = GetSectionByName(GetInfoByName(typeName), sectionName);
        if (section != null)
        {
            List<String> elements = new ArrayList<String>();
            for (int s = 0; s < section.elementInfos.size(); s++) {
                elements.add(section.elementInfos.get(s).rawName);
            }
            return elements;
        }

        return null;
    }

    public RlUiElementInfo uihelpGetElementInfo(String typeName, String elementName) throws Exception
    {
        ElementInfo element = GetElementByName(GetInfoByName(typeName), elementName);
        if(element != null)
        {
            RlUiElementInfo info = new RlUiElementInfo();
            info.setUiName(element.uiName);
            info.setUiHelp(element.uiHelp);
            info.setIndex(element.index);
            info.setIsVisible(element.isVisible);
            return info;
        }
        return null;
    }

    public String uihelpGetUiNameOfElement(String typeName, String elementName) throws Exception
    {
        ElementInfo element = GetElementByName(GetInfoByName(typeName), elementName);
        if(element != null)
            return element.uiName;
        return "";
    }

    public String uihelpGetUiHelpOfElement(String typeName, String elementName) throws Exception
    {
        ElementInfo element = GetElementByName(GetInfoByName(typeName), elementName);
        if(element != null)
            return element.uiHelp;
        return "";
    }

    private UiInfo GetInfoByName(String name)
    {
        if (uiInfos != null)
        {
            for (int i = 0; i < uiInfos.size(); i++)
            {
                UiInfo info = uiInfos.get(i);
                if (info.typeName.equals(name))
                {                    
                    return info;
                }
            }
        }
        return null;
    }

    private SectionInfo GetSectionByName(UiInfo info, String sectionName)
    {
        if (info != null)
        {
            for (int i = 0; i < info.sectionInfo.size(); i++)
            {
                SectionInfo section = info.sectionInfo.get(i);
                if (section.sectionName.equals(sectionName))
                {                    
                    return section;
                }
            }
        }
        return null;
    }

    private ElementInfo GetElementByName(UiInfo info, String elementName)
    {
        if (info != null)
        {
            for (int i = 0; i < info.sectionInfo.size(); i++)
            {
                SectionInfo section = info.sectionInfo.get(i);
                for (int s = 0; s < section.elementInfos.size(); s++)
                {
                    ElementInfo element = section.elementInfos.get(s);
                    if(element.rawName.equals(elementName))
                        return  element;
                }
            }
        }
        return null;
    }

}

class UiInfo
{
    public String typeName;
    public ArrayList<SectionInfo> sectionInfo;
}

class SectionInfo
{
    public String sectionName;
    public ArrayList<ElementInfo> elementInfos;
}

class ElementInfo
{
    public String rawName;
    public String uiName;
    public String uiHelp;
    public int index;
    public boolean isVisible;
}

