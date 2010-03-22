/*
 * ome.formats.model.ChannelProcessor
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2008 University of Dundee. All rights reserved.
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */

package ome.formats.model;

import static omero.rtypes.rint;
import static omero.rtypes.rstring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import ome.util.LSID;
import omero.RInt;
import omero.RString;
import omero.model.Channel;
import omero.model.Filter;
import omero.model.Image;
import omero.model.Laser;
import omero.model.LightSource;
import omero.model.LogicalChannel;
import omero.model.Pixels;
import omero.model.TransmittanceRange;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Processes the pixels sets of an IObjectContainerStore and ensures
 * that LogicalChannel containers are present in the container cache, and
 * populating channel name and colour where appropriate.
 *   
 * @author Chris Allan <callan at blackcat dot ca>
 * @author Jean-Marie <jburel at dundee dot ac dot uk>
 *
 */
public class ChannelProcessor implements ModelProcessor
{
	
	/** Name of the <code>red</code> component when it is a graphics image. */
	public static final String RED_TEXT = "Red";
	
	/** Name of the <code>green</code> component when it is a graphics image. */
	public static final String GREEN_TEXT = "Green";
	
	/** Name of the <code>blue</code> component when it is a graphics image. */
	public static final String BLUE_TEXT = "Blue";
	
	/** Name of the <code>blue</code> component when it is a graphics image. */
	public static final String ALPHA_TEXT = "Alpha";
	
	/** Logger for this class */
	private Log log = LogFactory.getLog(ChannelProcessor.class);
	
	/** Container store we're currently working with. */
	private IObjectContainerStore store;
	
	/** Bio-Formats reader implementation we're currently working with. */
	private IFormatReader reader;
	
	/**
     * Populates the default color for the channel if one does not already
     * exist.
     * 
     * @param channelData Channel data to use to set the color.
     * @param isGraphicsDomaind Whether or not the image is in the graphics
	 * domain according to Bio-Formats.
     */
    private void populateDefault(ChannelData channelData,
    		                   boolean isGraphicsDomain)
    {
    	int channelIndex = channelData.getChannelIndex();
    	Channel channel = channelData.getChannel();
    	LogicalChannel lc = channelData.getLogicalChannel();
    	if (isGraphicsDomain)
		{
    	    log.debug("Setting color channel to RGB.");
    	    switch (channelIndex) {
				case 0: //red
					channel.setRed(rint(255));
	    	        channel.setGreen(rint(0));
	    	        channel.setBlue(rint(0));
	    	        channel.setAlpha(rint(255));
	    	        if (lc.getName() == null) 
	    	        	lc.setName(rstring(RED_TEXT));
	    	        break;
				case 1: //green
					channel.setRed(rint(0));
	    	        channel.setGreen(rint(255));
	    	        channel.setBlue(rint(0));
	    	        channel.setAlpha(rint(255));
	    	        if (lc.getName() == null) 
	    	        	lc.setName(rstring(GREEN_TEXT));
	    	        break;
				case 2: //blue
					channel.setRed(rint(0));
	    	        channel.setGreen(rint(0));
	    	        channel.setBlue(rint(255));
	    	        channel.setAlpha(rint(255));
	    	        if (lc.getName() == null) 
	    	        	lc.setName(rstring(BLUE_TEXT));
	    	        break;
				case 3: //alpha
					channel.setRed(rint(0));
	    	        channel.setGreen(rint(0));
	    	        channel.setBlue(rint(0));
	    	        channel.setAlpha(rint(0)); //transparent
	    	        if (lc.getName() == null) 
	    	        	lc.setName(rstring(ALPHA_TEXT));
			}
    	    return;
		}
    	
    	Integer red = getValue(channel.getRed());
        Integer green = getValue(channel.getGreen());
        Integer blue = getValue(channel.getBlue());
        Integer alpha = getValue(channel.getAlpha());
        RString name;
        //color already set by Bio-formats
        if (red != null && green != null && blue != null && alpha != null) {
        	//Try to set the name.
        	if (lc.getName() == null) {
        		name = getChannelName(channelData);
        		if (name != null) lc.setName(name);	
        	}
        	return;
        }    	
        //First we check the emission wavelength.
    	Integer value = getValue(lc.getEmissionWave());
    	if (value != null) {
    		setChannelColor(channel, channelIndex, 
    				ColorsFactory.determineColor(value));
    		if (lc.getName() == null) 
	        	lc.setName(rstring(value.toString()));
    		return;
    	}
    	
    	//First check the emission filter.
    	//First check if filter
    	value = getValueFromFilter(channelData.getFilterSetEmissionFilter());
    	if (value != null) {
    		setChannelColor(channel, channelIndex, 
    				ColorsFactory.determineColor(value));
    		if (lc.getName() == null) {
    			name = getNameFromFilter(
    					channelData.getFilterSetEmissionFilter());
    			if (name != null) lc.setName(name);
    		}
    		return;
    	}
    	value = getValueFromFilter(
				channelData.getSecondaryEmissionFilter());
    	
    	if (value != null) {
    		setChannelColor(channel, channelIndex,
    				ColorsFactory.determineColor(value));
    		if (lc.getName() == null) {
    			name = getNameFromFilter(
    					channelData.getSecondaryEmissionFilter());
    			if (name != null) lc.setName(name);
    		}
    		return;
    	}
    	//Laser
    	if (channelData.getLightSource() != null) {
    		LightSource ls = channelData.getLightSource();
    		if (ls instanceof Laser) {
    			value = getValue(((Laser) ls).getWavelength());
    			if (value != null) {
    	    		setChannelColor(channel, channelIndex, 
    	    				ColorsFactory.determineColor(value));
    	    		if (lc.getName() == null) {
    	    			lc.setName(rstring(value.toString()));
    	    		}
    	    		return;
    	    	}
    		}
    	}
    	//Excitation
    	value = getValue(lc.getExcitationWave());
    	if (value != null) {
    		setChannelColor(channel, channelIndex, 
    				ColorsFactory.determineColor(value));
    		if (lc.getName() == null) 
    			lc.setName(rstring(value.toString()));
    		return;
    	}
    	value = getValueFromFilter(channelData.getFilterSetExcitationFilter());
    	if (value != null) {
    		setChannelColor(channel, channelIndex, 
    				ColorsFactory.determineColor(value));
    		if (lc.getName() == null) {
    			name = getNameFromFilter(
    					channelData.getFilterSetExcitationFilter());
    			if (name != null) lc.setName(name);
    		}
    		return;
    	}
    	value = getValueFromFilter(
				channelData.getSecondaryExcitationFilter());
    	
    	if (value != null) {
    		setChannelColor(channel, channelIndex, 
    				ColorsFactory.determineColor(value));
    		if (lc.getName() == null) {
    			name = getNameFromFilter(
    					channelData.getSecondaryExcitationFilter());
    			if (name != null) lc.setName(name);
    		}
    		return;
    	}
    	
    	//not been able to set the color
    	 
    	setDefaultChannelColor(channel, channelIndex);
    }
    
    /**
     * Sets the default color of the channel.
     * 
     * @param channel The channel to handle.
     * @param index   The index of the channel.
     */
    private void setDefaultChannelColor(Channel channel, int index)
    {
    	//not been able to set the color
    	switch (index) {
	    	case 0: //red
	    		channel.setRed(rint(255));
    	        channel.setGreen(rint(0));
    	        channel.setBlue(rint(0));
    	        channel.setAlpha(rint(255));
    	        break;
	    	case 1: //blue
	    		channel.setRed(rint(0));
    	        channel.setGreen(rint(0));
    	        channel.setBlue(rint(255));
    	        channel.setAlpha(rint(255));
    	        break;
	    	default: //green
	    		channel.setRed(rint(0));
    	        channel.setGreen(rint(255));
    	        channel.setBlue(rint(0));
    	        channel.setAlpha(rint(255));
    	}
    }
    
    /**
     * Sets the color of the channel.
     * 
     * @param channel The channel to handle.
     * @param index   The index of the channel.
     * @param rgba    The color to set.
     */
    private void setChannelColor(Channel channel, int index, int[] rgba)
    {
    	if (rgba == null) {
    		setDefaultChannelColor(channel, index);
    		return;
    	}
    	channel.setRed(rint(rgba[0]));
    	channel.setGreen(rint(rgba[1]));
    	channel.setBlue(rint(rgba[2]));
    	channel.setAlpha(rint(rgba[3]));
    }
    
    /**
     * Returns a channel name string from a given filter.
     * 
     * @param filter Filter to retrieve a channel name from.
     * @return See above.
     */
    private RString getNameFromFilter(Filter filter)
    {
        if (filter == null)
        {
        	return null;
        }
        TransmittanceRange t = filter.getTransmittanceRange();
        return t == null? null : 
        	rstring(String.valueOf(t.getCutIn().getValue()));
    }
    
    /**
     * Returns the range of the wavelength or <code>null</code>.
     * 
     * @param filter The filter to handle.
     * @return See above.
     */
    private Integer getValueFromFilter(Filter filter)
    {
    	if (filter == null) return null;
    	TransmittanceRange transmittance = filter.getTransmittanceRange();
    	if (transmittance == null) return null;
    	Integer cutIn = getValue(transmittance.getCutIn());
    	Integer cutOut = getValue(transmittance.getCutOut());
    	if (cutIn == null) return null;
    	if (cutOut == null || cutOut == 0) cutOut = cutIn+20;
    	return (cutIn+cutOut)/2;
    }
    
    /**
     * Returns the concrete value of an OMERO rtype.
     * 
     * @param value OMERO rtype to get the value of.
     * @return Concrete value of <code>value</code> or <code>null</code> if
     * <code>value == null</code>.
     */
    private Integer getValue(RInt value)
    {
    	return value == null? null : value.getValue();
    }
    
    /**
     * Determines the name of the channel. 
     * This method should only be invoked when a color was assigned by 
     * Bio-formats.
     * 
     * @param channelData The channel to handle.
     * @return See above.
     */
    private RString getChannelName(ChannelData channelData)
    {
    	LogicalChannel lc = channelData.getLogicalChannel();
    	Integer value = getValue(lc.getEmissionWave());
    	RString name;
    	if (value != null)
    	{
    		return rstring(value.toString());
    	}
    	name = getNameFromFilter(channelData.getFilterSetEmissionFilter());
    	if (name != null)
    	{
    		return name;
    	}
    	name = getNameFromFilter(channelData.getSecondaryEmissionFilter());
    	if (name != null)
    	{
    		return name;
    	}
    	//Laser
    	LightSource ls = channelData.getLightSource();
    	if (ls != null)
    	{
    		if (ls instanceof Laser)
    		{
    			Laser laser = (Laser) ls;
    			value = getValue(laser.getWavelength());
    			if (value != null)
    			{
    				return rstring(value.toString());
    			}
    		}
    	}
    	value = getValue(lc.getExcitationWave());
    	if (value != null)
    	{
    		return rstring(value.toString());
    	}
    	name = getNameFromFilter(channelData.getFilterSetExcitationFilter());
    	if (name != null)
    	{
    		return name;
    	}
    	return getNameFromFilter(channelData.getSecondaryExcitationFilter());
    }

    /**
     * Processes the OMERO client side metadata store.
     * @param store OMERO metadata store to process.
     * @throws ModelException If there is an error during processing.
     */
    public void process(IObjectContainerStore store)
    	throws ModelException
    {
    	this.store = store;
    	reader = this.store.getReader();
    	if (reader == null)
    	{
    		throw new ModelException("Unexpected null reader.");
    	}
    	
    	List<Image> images = store.getSourceObjects(Image.class);
    	String[] domains = reader.getDomains();
    	boolean isGraphicsDomain = false;
    	for (String domain : domains)
    	{
    		if (domain.equals(FormatTools.GRAPHICS_DOMAIN))
    		{
    			log.debug("Images are of the grahpics domain.");
    			isGraphicsDomain = true;
    			break;
    		}
    	}
    	List<Boolean> values;
    	boolean v;
    	Map<ChannelData, Boolean> m;
    	Pixels pixels;
    	int sizeC;
    	ChannelData channelData;
    	Iterator<ChannelData> k;
    	for (int i = 0; i < images.size(); i++)
    	{
    		pixels = (Pixels) store.getSourceObject(new LSID(Pixels.class, i));
    		if (pixels == null)
    		{
    			throw new ModelException("Unable to locate Pixels:" + i);
    		}
    		//Require to reset transmitted light
    		values = new ArrayList<Boolean>();
    		m = new HashMap<ChannelData, Boolean>();
    		
    		//Think of strategy for images with high number of channels
    		//i.e. > 6
    		sizeC = pixels.getSizeC().getValue();
    		for (int c = 0; c < sizeC; c++)
    		{
    			channelData = ChannelData.fromObjectContainerStore(store, i, c);
    			//Color section
    			populateDefault(channelData, isGraphicsDomain);

                //only retrieve if not graphics
                if (!isGraphicsDomain) {
                	//Determine if the channel same emission wavelength.
                	v = ColorsFactory.hasEmissionData(channelData);
                	if (!v)
                	{
                		values.add(v);
                	}
                    m.put(channelData, v);
                }
   			    //populateName(channelData, isGraphicsDomain);
    		}
    		
	    	//Need to reset the color of transmitted light
	    	//i.e. images with several "emission channels"
	    	//check if 0 size
	    	if (values.size() > 0 && values.size() != m.size()) {
	    		k = m.keySet().iterator();
	    		while (k.hasNext()) {
	    			channelData = k.next();
	    			if (!m.get(channelData)) {
	    				int[] defaultColor = ColorsFactory.newWhiteColor();
	    				Channel channel = channelData.getChannel();
	    				channel.setRed(
	    						rint(defaultColor[ColorsFactory.RED_INDEX]));
	    				channel.setGreen(
	    						rint(defaultColor[ColorsFactory.GREEN_INDEX]));
	    				channel.setBlue(
	    						rint(defaultColor[ColorsFactory.BLUE_INDEX]));
	    				channel.setAlpha(
	    						rint(defaultColor[ColorsFactory.ALPHA_INDEX]));
	    			}
	    		}
	    	}
    	}
    }
    
    
}
