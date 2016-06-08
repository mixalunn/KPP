package ScalaLife;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/** Sorted game table */
public class SortTable {
  JDialog dialog = null;
  JTable table = null;

  public SortTable(String name, String[] fileArray, int[] numbFieldsArray, String time) {
    dialog = new JDialog();
    dialog.setTitle(name);
    DefaultTableModel model = new DefaultTableModel(new Object[] {"File", "Cells"}, 0);
    table = new JTable(model);
    for (int i = 0; i < fileArray.length; i++) {
      model.addRow(new Object[] {fileArray[i], numbFieldsArray[i]});
    }
    dialog.setLayout(new BorderLayout());
    dialog.add(new JLabel("Time: " + time), BorderLayout.NORTH);
    dialog.add(new JScrollPane(table), BorderLayout.CENTER);

    dialog.pack();
    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    dialog.setVisible(true);

  }
}
