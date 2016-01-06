
# Build a basic plugin for JOSM

This a brief introduction how to build a plugin for JOSM, which will find the OSM objects by id of changeset;

Ref : https://josm.openstreetmap.de/wiki/DevelopersGuide/DevelopingPlugins

#### Requirement

- [SVN](https://subversion.apache.org/)
- [ANT](http://ant.apache.org/)


#### Build latest JOSM version. 

- Clone  the repository

```
 $ svn co http://svn.openstreetmap.org/applications/editors/josm

```

- Build josm-custom.jar file


```
$ cd josm/core/ && ant
```

It will create the file josm/core/dist/josm-custom.jar.  this is usually the latest version.


#### Crete first files for you plugin

- Make a copy from `00_plugin_dir_template` , this is the basic template to start a plugin.

```
$ cd ..
$ cp -r plugins/00_plugin_dir_template/ plugins/findchangeset-plugin

```

- Create the package when we are going to add the Java classes.

```
$ mkdir -p plugins/findchangeset-plugin/src/org/openstreetmap/josm/plugins/findchangeset

```


#### Configure the build.xml file

Open your `findchangeset-plugin/build.xml` file and add the basic configuration, the configuration include settings for start the plugin without restart JOSM.



```
<?xml version="1.0" encoding="utf-8"?>
<project name="findchangeset" default="dist" basedir=".">
    <property name="commit.message" value="Find changes by id of changeset"/>
    <property name="plugin.main.version" value="8900"/>
    <property name="plugin.author" value="Rub21"/>
    <property name="plugin.class" value="org.openstreetmap.josm.plugins.findchangeset.FindChangesetPlugin"/>
    <property name="plugin.description" value="Find changes in the actual layer by id of changeset"/>
    <property name="plugin.icon" value="images/iconfindchangeset.png"/>
    <property name="plugin.link" value="https://github.com/rub21/findchangeset-plugin"/>
    <property name="plugin.canloadatruntime" value="true"/>
    <import file="../build-common.xml"/>  
</project>

```

**Note:** 
We have to add a image in  `images` folder on this case we will use the [image](https://raw.githubusercontent.com/JOSM/tofix-plugin/master/images/icontofix.png) from [tofix-plugin](https://github.com/JOSM/tofix-plugin)

#### Start Coding

- Create a java class called `FindChangesetPlugin.java` in `org.openstreetmap.josm.plugins.findchangeset` and copy and paste the below code on it.

The `FindWaysPlugin.java` class is the main class where the plugin is going to start the execution.


```
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
```


- Create a java class called `FindChangesetDialog.java` in `org.openstreetmap.josm.plugins.findchangeset` and copy and paste the below code on it.

The `FindChangesetDialog.java` the class will create a window in the sidebar of JOSM.

```
package org.openstreetmap.josm.plugins.findchangeset;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.AutoScaleAction;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import static org.openstreetmap.josm.gui.mappaint.mapcss.ExpressionFactory.Functions.tr;
import org.openstreetmap.josm.tools.Shortcut;

public class FindChangesetDialog extends ToggleDialog {

    private final SideButton button;

    public FindChangesetDialog() {

        super(tr("Find Changeset"), "icontofix", tr("Open Find Changeset window."),
                Shortcut.registerShortcut("tool:findchangeset", tr("Toggle: {0}", tr("Find Changeset")),
                        KeyEvent.VK_F, Shortcut.CTRL_SHIFT), 75);

        JPanel valuePanel = new JPanel(new GridLayout(1, 1));
        valuePanel.setBackground(Color.DARK_GRAY);
        JPanel jcontenpanel = new JPanel(new GridLayout(1, 0));
        final JTextField jTextField = new JTextField();
        valuePanel.add(jTextField);

        button = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Find"));               
                putValue(SHORT_DESCRIPTION, tr("Find"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Main.main == null || Main.main.getEditLayer() == null) {
                    return;
                }
                OsmDataLayer layer = Main.main.getEditLayer();
                Set<OsmPrimitive> omsobj_list = new HashSet<>();
                for (OsmPrimitive obj : layer.data.allPrimitives()) {
                    if (obj.getChangesetId()==Integer.parseInt(jTextField.getText())) {
                        omsobj_list.add(obj);
                    }
                }
                if (omsobj_list.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Not Found " + jTextField.getText() + " Changeset");
                    return;
                }
                layer.data.setSelected(omsobj_list);
                AutoScaleAction.zoomToSelection();
            }
        });
        createLayout(jcontenpanel, false, Arrays.asList(new SideButton[]{
            button
        }));
        jcontenpanel.add(valuePanel);
    }
}

```


#### Testing

```
$ cd jsom/plugins/findchangeset-plugin/
$ ant install

```

Open your JOSM and active the plugin, in `Edit/preferences` the plugin will active without restart JOSM

#### Result

![plugin-result2](https://cloud.githubusercontent.com/assets/1152236/11529300/1ded5404-9912-11e5-8933-631db0349f69.gif)


## How to run the plugin in NetBeans

https://www.youtube.com/watch?v=kDSP-eol8to
