package Life;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/** Creation and opening field size change window */
public class SizeWindow {
  JDialog usersSizeWindow = new JDialog();
  public SizeWindow(String name, LifePanel lifeP)
  {
    final int FIELD_SIZE = 7;
    final int WINDOW_SIZE_HEIGHT = 70;
    final int WINDOW_SIZE_WIGHT = 300;

    usersSizeWindow.setTitle(name);
    usersSizeWindow.setLayout(new BorderLayout());
    JButton okButton = new JButton("Ok");
    JButton cancelButton = new JButton("Cancel");
    JTextField fieldHight = new JTextField(FIELD_SIZE);
    JTextField fieldWight = new JTextField(FIELD_SIZE);
    JTextField cellSize = new JTextField(FIELD_SIZE);
    JLabel markerLabel = new JLabel("");
    usersSizeWindow.setSize(new Dimension(WINDOW_SIZE_WIGHT, WINDOW_SIZE_HEIGHT));
    usersSizeWindow.setLayout(new GridLayout(3, 3));
    usersSizeWindow.add(new JLabel("  Field size"));
    usersSizeWindow.add(fieldHight);
    usersSizeWindow.add(fieldWight);
    usersSizeWindow.add(new JLabel("  Cell size"));
    usersSizeWindow.add(cellSize);
    usersSizeWindow.add(markerLabel);
    usersSizeWindow.add(okButton);
    usersSizeWindow.add(cancelButton);

    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        usersSizeWindow.dispose();
      }
    });

    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          lifeP.initialize(Integer.parseInt(fieldHight.getText()),
                  Integer.parseInt(fieldWight.getText()));
          lifeP.setCellSize(Integer.parseInt(cellSize.getText()));
          lifeP.repaint();
          usersSizeWindow.dispose();
        } catch (NumberFormatException m) {
          markerLabel.setText("  Error");
        }
      }
    });
    usersSizeWindow.pack();
    usersSizeWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    usersSizeWindow.setVisible(true);
  }
}