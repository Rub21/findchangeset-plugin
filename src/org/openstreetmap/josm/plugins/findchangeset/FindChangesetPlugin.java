package org.openstreetmap.josm.plugins.findchangeset;

import java.awt.GraphicsEnvironment;
import org.openstreetmap.josm.gui.IconToggleButton;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

public class FindChangesetPlugin  extends Plugin{

  private IconToggleButton btn;
    protected static FindChangesetDialog findChangeDialog;
 
    public FindChangesetPlugin(PluginInformation info) {
        super(info);
    }

    @Override
    public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame) {
        if (newFrame != null && !GraphicsEnvironment.isHeadless()) {
            newFrame.addToggleDialog(findChangeDialog = new FindChangesetDialog());
        }
    }
}