package ScalaLife;


import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/** Sorted game table */
public class StatTable {
  JDialog dialog = null;
  JTable table = null;

  public StatTable( String[] fileArray, int[] maxField, int[] minField) {
    dialog = new JDialog();
    dialog.setTitle("Statistic");
    DefaultTableModel model = new DefaultTableModel(new Object[] {"File", "MaxAlive", "MaxDead"}, 0);
    table = new JTable(model);
    for (int i = 0; i < fileArray.length; i++) {
      model.addRow(new Object[] {fileArray[i], maxField[i], minField[i]});
    }
    model.addRow(new Object[] {"Average", });
    dialog.setLayout(new BorderLayout());
    dialog.add(new JScrollPane(table), BorderLayout.CENTER);

    dialog.pack();
    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    dialog.setVisible(true);

  }
}