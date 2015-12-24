package org.openstreetmap.josm.plugins.findchangeset;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.AutoScaleAction;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.event.AbstractDatasetChangedEvent;
import org.openstreetmap.josm.data.osm.event.DataSetListenerAdapter;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import static org.openstreetmap.josm.gui.mappaint.mapcss.ExpressionFactory.Functions.tr;
import org.openstreetmap.josm.tools.Shortcut;

public class FindChangesetDialog extends ToggleDialog implements ActionListener, DataSetListenerAdapter.Listener, MapView.LayerChangeListener {

    private final SideButton button;

    Vector comboBoxItems = new Vector();
    final DefaultComboBoxModel model = new DefaultComboBoxModel(comboBoxItems);
    JComboBox comboBox = new JComboBox(model);
    ArrayList<Integer> ids = new ArrayList<>();

    public FindChangesetDialog() {

        super(tr("Find Changeset"), "iconfindchangeset", tr("Open Find Changeset window."),
                Shortcut.registerShortcut("tool:findchangeset", tr("Toggle: {0}", tr("Find Changeset")),
                        KeyEvent.VK_F, Shortcut.CTRL_SHIFT), 75);

        MapView.addLayerChangeListener(this);
        JPanel valuePanel = new JPanel(new GridLayout(2, 1));
        JPanel jcontenpanel = new JPanel(new GridLayout(1, 0));
        // final JTextField jTextField = new JTextField();
        // valuePanel.add(jTextField);
        valuePanel.add(comboBox);
        comboBox.addActionListener(this);

        button = new SideButton(new AbstractAction() {
            {
                putValue(NAME, tr("Find"));
                putValue(SHORT_DESCRIPTION, tr("Find"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
//                if (Main.main == null || Main.main.getEditLayer() == null) {
//                    return;
//                }
//                OsmDataLayer layer = Main.main.getEditLayer();
//                Set<OsmPrimitive> omsobj_list = new HashSet<>();
//                for (OsmPrimitive obj : layer.data.allPrimitives()) {
//                    if (obj.getChangesetId() == Integer.parseInt(jTextField.getText())) {
//                        omsobj_list.add(obj);
//                    }
//                }
//                if (omsobj_list.isEmpty()) {
//                    JOptionPane.showMessageDialog(null, "Not Found " + jTextField.getText() + " Changeset");
//                    return;
//                }
//                layer.data.setSelected(omsobj_list);
//                AutoScaleAction.zoomToSelection();
            }
        });
        createLayout(jcontenpanel, false, Arrays.asList(new SideButton[]{
            button
        }));
        jcontenpanel.add(valuePanel);

//        comboBox.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                select((Item) comboBox.getSelectedItem());
//            }
//        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // JOptionPane.showConfirmDialog(null,model.getSize());
        if (model.getSize() != 0) {
            select((Item) comboBox.getSelectedItem());
        }

    }

    @Override
    public void processDatasetEvent(AbstractDatasetChangedEvent adce) {
        // JOptionPane.showConfirmDialog(null, "test3");
    }

    @Override
    public void activeLayerChange(Layer layer, Layer layer1) {
        //JOptionPane.showConfirmDialog(null, "test2");
        //model.removeAllElements();
        //comboBox.removeAllItems();
    }

    @Override
    public void layerAdded(Layer layer) {
        model.removeAllElements();
        comboBox.removeAllItems();
        model.addElement(new Item(0, "Select the chanseet"));

        if (layer instanceof OsmDataLayer) {

            fill_combo((OsmDataLayer) layer);
        }

    }

    @Override
    public void layerRemoved(Layer layer) {
        model.removeAllElements();
        comboBox.removeAllItems();
        model.addElement(new Item(0, "Select the chanseet"));

    }

    public void fill_combo(OsmDataLayer layer) {
        for (OsmPrimitive obj : layer.data.allPrimitives()) {
            try {
                if (ids.indexOf(obj.getChangesetId()) < 0) {
                    String text = obj.getChangesetId() + "-" + obj.getUser().getName() + "-" + obj.getTimestamp();
                    model.addElement(new Item(obj.getChangesetId(), text));
                    ids.add(obj.getChangesetId());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // model.
        ids.clear();
    }

    private void select(Item item) {
        //   JOptionPane.showConfirmDialog(null, item.getId());
        if (item.getId() != 0) {
            String id_changeset = String.valueOf(item.getId());

            if (Main.main == null || Main.main.getEditLayer() == null) {
                return;
            }
            OsmDataLayer layer = Main.main.getEditLayer();
            Set<OsmPrimitive> omsobj_list = new HashSet<>();
            for (OsmPrimitive obj : layer.data.allPrimitives()) {
                if (obj.getChangesetId() == Integer.parseInt(id_changeset)) {
                    omsobj_list.add(obj);
                }
            }
            if (omsobj_list.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Not Found " + id_changeset + " Changeset");
                return;
            }
            layer.data.setSelected(omsobj_list);
            AutoScaleAction.zoomToSelection();
        }

    }

}
