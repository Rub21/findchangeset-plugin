package org.openstreetmap.josm.plugins.findchangeset;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    final DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();

    public FindChangesetDialog() {

        super(tr("Find Changeset"), "iconfindchangeset", tr("Open Find Changeset window."),
                Shortcut.registerShortcut("tool:findchangeset", tr("Toggle: {0}", tr("Find Changeset")),
                        KeyEvent.VK_F, Shortcut.CTRL_SHIFT), 75);

        JPanel valuePanel = new JPanel(new GridLayout(1, 1));
        valuePanel.setBackground(Color.DARK_GRAY);
        JPanel jcontenpanel = new JPanel(new GridLayout(1, 0));
        final JTextField jTextField = new JTextField();
        valuePanel.add(jTextField);

        final JScrollPane jScrollPane = new JScrollPane();

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
                    if (obj.getChangesetId() == Integer.parseInt(jTextField.getText())) {
                        omsobj_list.add(obj);
                    }

                    comboBoxModel.addElement(obj.getChangesetId());

                }
                if (omsobj_list.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Not Found " + jTextField.getText() + " Changeset");
                    return;
                }

                final JComboBox jComboBox = new JComboBox(comboBoxModel);
                jScrollPane.add(jComboBox);
                jComboBox.setSelectedIndex(0);

                layer.data.setSelected(omsobj_list);

                AutoScaleAction.zoomToSelection();
            }
        });

        createLayout(jcontenpanel, false, Arrays.asList(new SideButton[]{
            button
        }));

        valuePanel.add(jScrollPane);
        jcontenpanel.add(valuePanel);

    }
}
