/*
 * org.openmicroscopy.shoola.env.data.views.calls.Analyser 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2007 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
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
package org.openmicroscopy.shoola.env.data.views.calls;



//Java imports
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//Third-party libraries

//Application-internal dependencies
import ome.model.core.Pixels;
import org.openmicroscopy.shoola.env.data.OmeroImageService;
import org.openmicroscopy.shoola.env.data.views.BatchCall;
import org.openmicroscopy.shoola.env.data.views.BatchCallTree;
import org.openmicroscopy.shoola.util.roi.model.ROIShape;

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class Analyser
	extends BatchCallTree
{

	/** The pixels set to analyse. */
	private final Pixels		pixels;
	
	/** Collection of active channels. */
	private final List			channels; 
	
	/** The result of the call. */
    private Object				result;
    
    /** Loads the specified experimenter groups. */
    private BatchCall   		loadCall;
    
    /**
     * Creates a {@link BatchCall} to analyse the specified shapes.
     * 
     * @param shapes	Collection of shapes to analyse. 
     * @return The {@link BatchCall}.
     */
    private BatchCall analyseShapes(final List shapes)
    {
        return new BatchCall("Loading experimenter groups") {
            public void doCall() throws Exception
            {
            	OmeroImageService os = context.getImageService();
                Iterator i = shapes.iterator();
                ROIShape shape;
                Iterator j;
                Integer c;
                Map planes;
                Map shapeData =  new HashMap(shapes.size());
                while (i.hasNext()) {
					shape = (ROIShape) i.next();
					if (shape != null) {
						planes = new HashMap(channels.size());
						j = channels.iterator();
						while (j.hasNext()) {
							c = (Integer) j.next();
							planes.put(c, 
									os.getPlane(pixels.getId().longValue(), 
											shape.getZ(), 
										shape.getT(), c.intValue()));
						}
						shapeData.put(shape, planes);
					}
				}
            	result = shapeData;
            }
        };
    }
    
    /**
     * Adds the {@link #loadCall} to the computation tree.
     * @see BatchCallTree#buildTree()
     */
    protected void buildTree() { add(loadCall); }

    /**
     * Returns, in a <code>Map</code>.
     * 
     * @see BatchCallTree#getResult()
     */
    protected Object getResult() { return result; }
    
    /**
     * Creates a new instance.
     * 
     * @param pixels	The pixels set to analyse.
     * @param type		The type of the pixels set.
     * @param channels	Collection of active channels. 
     * 					Mustn't be <code>null</code>.
     * @param shapes	Collection of shapes to analyse. 
     * 					Mustn't be <code>null</code>.
     */
    public Analyser(Pixels pixels, List channels, List shapes)
    {
    	if (pixels == null) 
    		throw new IllegalArgumentException("No Pixels specified."); 
    	if (channels == null || channels.size() == 0)
			throw new IllegalArgumentException("No channels specified.");
		if (shapes == null || shapes.size() == 0)
			throw new IllegalArgumentException("No shapes specified.");
		this.pixels = pixels;
    	this.channels = channels;
		loadCall = analyseShapes(shapes);
    }
    
}
