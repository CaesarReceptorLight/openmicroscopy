/*
 * org.openmicroscopy.shoola.agents.browser.events.PiccoloActions
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */

/*------------------------------------------------------------------------------
 *
 * Written by:    Jeff Mellen <jeffm@alum.mit.edu>
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.agents.browser.events;

import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import org.openmicroscopy.shoola.agents.browser.images.Thumbnail;
import org.openmicroscopy.shoola.agents.browser.ui.SemanticZoomNode;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * A common repository of oft-used Piccolo actions.
 * 
 * @author Jeff Mellen, <a href="mailto:jeffm@alum.mit.edu">jeffm@alum.mit.edu</a><br>
 * <b>Internal version:</b> $Revision$ $Date$
 * @version 2.2
 * @since OME2.2
 */
public interface PiccoloActions
{
    /**
     * The common action for a node to be moved on the canvas when
     * dragged.
     */
    public static final PiccoloAction DRAG_MOVE_ACTION = new PiccoloAction()
    {
        public void execute(PInputEvent e)
        {
            PNode node = e.getPickedNode();
            Dimension2D d = e.getDeltaRelativeTo(node);
            node.translate(d.getWidth(),d.getHeight());
        }
    };
    
    /**
     * The common action to draw a semantic-zoom image over a thumbnail.
     */
    public static final PiccoloAction SEMANTIC_ZOOM_ACTION = new PiccoloAction()
    {
        public void execute(PInputEvent e)
        {
            if(e.getCamera().getViewScale() < 1)
            {
                Thumbnail t = (Thumbnail)e.getPickedNode();
                Image image = t.getImage();
                SemanticZoomNode semanticNode =
                    new SemanticZoomNode(t);
                
                Point2D point = new Point2D.Double(t.getOffset().getX()+
                                                   t.getBounds().getCenter2D().getX(),
                                                   t.getOffset().getY()+
                                                   t.getBounds().getCenter2D().getY());
                Point2D dummyPoint = new Point2D.Double(point.getX(),point.getY());
                Dimension2D size = t.getBounds().getSize();
                
                Point2D viewPoint = e.getCamera().viewToLocal(dummyPoint);
                
                semanticNode.setOffset(viewPoint.getX()-size.getWidth()/2,
                                       viewPoint.getY()-size.getHeight()/2);
                
                // invariant: should be one per (hopefully)
                PCamera camera = e.getCamera();
                for(int i=0; i<camera.getChildrenCount();i++)
                {
                    Object child = camera.getChild(i);
                    if(child instanceof SemanticZoomNode)
                    {
                        camera.removeChild((SemanticZoomNode)child);
                        i = camera.getChildrenCount();
                    }
                }
                
                camera.addChild(0,semanticNode);
            }
        }
    };
}
