package Life;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Class creates an application window and user interface
 * @author Michail Yuncevich
 */
public class LifeSim extends JFrame {
  /** Height menu bar */
  private static final int SIZE_MENU_HEIGHT = 50;
  /** Maximal width slider */
  private static final int SIZE_SLIDER_WIDTH = 500;
  /** Maximal width button run/stop */
  private static final int SIZE_BUTTON_WIDTH = 100;
  /** Standard height panel */
  private static final int STANDARD_HEIGHT = 70;
  /** Standard wight panel */
  private static final int STANDARD_WIGHT = 100;
  /** Small height panel */
  private static final int SMALL_HEIGHT = 40;
  /** Small wight panel */
  private static final int SMALL_WIGHT = 70;
  /** Big height panel */
  private static final int BIG_HEIGHT = 80;
  /** Big wight panel */
  private static final int BIG_WIGHT = 150;
  /** Small cell size */
  private static final int SMALL_CELL = 5;
  /** Standard cell size */
  private static final int STANDARD_CELL = 8;
  /** Big cell size */
  private static final int BIG_CELL = 10;
  /** Minimal value delay in slider {@link #slider} */
  private static final int MIN_SLEEP = 1;
  /** Maximal value delay in slider {@link #slider} */
  private static final int MAX_SLEEP = 500;
  /** Starting value delay in slider {@link #slider} */
  private static final int START_SLEEP = 60;
  /** Game field */
  private LifePanel lifeP = new LifePanel();
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menuFile = new JMenu("Game");
  private JMenu menuView = new JMenu("View");
  private JMenu sizeMenu = new JMenu("Size");
  private JMenu frameSizeMenu = new JMenu("Field size");
  private JMenu cellSizeMenu = new JMenu("Cell size");
  private JMenuItem startItem = new JMenuItem("Run");
  private JMenuItem stopItem = new JMenuItem("Stop");
  private JMenuItem exitItem = new JMenuItem("Exit");
  private JMenuItem clearItem = new JMenuItem("Clear");
  private JMenuItem randItem = new JMenuItem("Randomize");
  private JMenuItem saveItem = new JMenuItem("Save as");
  private JMenuItem loadItem = new JMenuItem("Load");
  private JMenuItem defStyle = new JMenuItem("Standard");
  private JMenuItem ironStyle = new JMenuItem("Metall");
  private JMenuItem motifStyle = new JMenuItem("Motif");
  private JMenuItem nimbusStyle = new JMenuItem("Nimbus");
  private JMenuItem smallSize = new JMenuItem("Small-70x40");
  private JMenuItem mediumSize = new JMenuItem("Middle-100x70");
  private JMenuItem bigSize = new JMenuItem("Large-150x80");
  private JMenuItem cellSSize = new JMenuItem("Small");
  private JMenuItem cellMSize = new JMenuItem("Middle");
  private JMenuItem cellLSize = new JMenuItem("Large");
  private JMenuItem usersSize = new JMenuItem("Other");
  private JFrame usersSizeWindow = new JFrame("Users sizes");
  private JButton button1 = new JButton("Run");
  private JSlider slider =
          new JSlider(JSlider.HORIZONTAL, MIN_SLEEP, MAX_SLEEP, START_SLEEP);

  /**
   * Create new window, adding visual elements and handling actions
   * @param title - name new window
   */
  public LifeSim(String title) {
    super(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    lifeP.initialize(STANDARD_WIGHT, STANDARD_HEIGHT);
    add(lifeP);
    add(menuBar, BorderLayout.NORTH);
    menuBar.add(menuFile);
    menuBar.add(new JLabel("  "));
    menuBar.add(sizeMenu);
    menuFile.add(startItem);
    menuFile.add(stopItem);
    menuFile.add(clearItem);
    menuFile.addSeparator();
    menuFile.add(randItem);
    menuFile.addSeparator();
    menuFile.add(saveItem);
    menuFile.add(loadItem);
    menuFile.addSeparator();
    menuFile.add(exitItem);
    menuView.add(defStyle);
    menuView.add(ironStyle);
    menuView.add(motifStyle);
    menuView.add(nimbusStyle);
    sizeMenu.add(frameSizeMenu);
    sizeMenu.add(cellSizeMenu);
    sizeMenu.add(usersSize);
    frameSizeMenu.add(smallSize);
    frameSizeMenu.add(mediumSize);
    frameSizeMenu.add(bigSize);
    cellSizeMenu.add(cellSSize);
    cellSizeMenu.add(cellMSize);
    cellSizeMenu.add(cellLSize);
    menuBar.add(new JLabel("   "));
    menuBar.add(menuView);
    menuView.add(defStyle);
    menuView.add(ironStyle);
    menuView.add(motifStyle);
    menuView.add(nimbusStyle);
    menuBar.add(new JLabel("  "));
    menuBar.add(button1);
    menuBar.add(new JLabel("       Faster"));
    menuBar.add(slider);
    menuBar.add(new JLabel("Slower"));
    lifeP.setUpdateDelay(slider.getValue());
    button1.setMaximumSize(new Dimension(SIZE_BUTTON_WIDTH, SIZE_MENU_HEIGHT));
    slider.setMaximumSize(new Dimension(SIZE_SLIDER_WIDTH, SIZE_MENU_HEIGHT));
    slider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        lifeP.setUpdateDelay(slider.getValue());
      }
    });

    button1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (lifeP.isSimulating()) {
          lifeP.stopSimulation();
          button1.setText("Run");
          lifeP.setSaveByte(0);
          lifeP.setLoadByte(0);
        } else {
          lifeP.startSimulation();
          button1.setText("Stop");
        }
      }
    });

    startItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!lifeP.isSimulating()) {
          lifeP.startSimulation();
          button1.setText("Stop");
        }
      }
    });

    stopItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (lifeP.isSimulating()) {
          lifeP.stopSimulation();
          button1.setText("Run");
          lifeP.setSaveByte(0);
          lifeP.setLoadByte(0);
        }
      }
    });

    clearItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        synchronized (lifeP.getLifeModel()) {
          lifeP.getLifeModel().clear();
          lifeP.repaint();
        }
      }
    });

    randItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        synchronized (lifeP.getLifeModel()) {
          lifeP.getLifeModel().randomByte();
          lifeP.repaint();
        }
      }
    });

    saveItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          saveWindowFrame();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });

    loadItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          openWindowFrame();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });

    exitItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });

    defStyle.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception m) {
        }
        SwingUtilities.updateComponentTreeUI(getContentPane());
        pack();
      }
    });

    ironStyle.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception m) {
        }
        SwingUtilities.updateComponentTreeUI(getContentPane());
        pack();
      }
    });

    motifStyle.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (Exception m) {
        }
        SwingUtilities.updateComponentTreeUI(getContentPane());
        pack();
      }
    });

    nimbusStyle.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception m) {
        }
        SwingUtilities.updateComponentTreeUI(getContentPane());
        pack();
      }
    });

    smallSize.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        lifeP.initialize(SMALL_WIGHT, SMALL_HEIGHT);
        SwingUtilities.updateComponentTreeUI(getContentPane());
        pack();
      }
    });

    mediumSize.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        lifeP.initialize(STANDARD_WIGHT, STANDARD_HEIGHT);
        SwingUtilities.updateComponentTreeUI(getContentPane());
        pack();
      }
    });

    bigSize.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        lifeP.initialize(BIG_WIGHT, BIG_HEIGHT);
        SwingUtilities.updateComponentTreeUI(getContentPane());
        pack();
      }
    });

    cellSSize.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        lifeP.setCellSize(SMALL_CELL);
        SwingUtilities.updateComponentTreeUI(getContentPane());
        pack();
      }
    });

    cellMSize.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        lifeP.setCellSize(STANDARD_CELL);
        SwingUtilities.updateComponentTreeUI(getContentPane());
        pack();
      }
    });

    cellLSize.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        lifeP.setCellSize(BIG_CELL);
        SwingUtilities.updateComponentTreeUI(getContentPane());
        pack();
      }
    });

    usersSize.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        usersSizeWindow.setVisible(true);
      }
    });
    pack();
    setVisible(true);
    usersWindow();
  }

  /**
   * Opening dialog to save a file selection
   * @throws IOException Exception save in file
   */
  public void openWindowFrame() throws IOException {
    JFileChooser dialog1 = new JFileChooser();
    dialog1.showOpenDialog(this);
    lifeP.setLoadFile(dialog1.getSelectedFile());
    lifeP.setLoadByte(1);
  }

  /**
   * Opening dialog to load a file selection
   * @throws IOException Exception load from file
   */
  public void saveWindowFrame() throws IOException {
    JFileChooser dialog2 = new JFileChooser();
    dialog2.showSaveDialog(this);
    lifeP.setSaveFile(dialog2.getSelectedFile());
    lifeP.setSaveByte(1);
  }

  /** Creation and opening field size change window */
  public void usersWindow() {
    final int FIELD_SIZE = 7;
    final int WINDOW_SIZE_HEIGHT = 70;
    final int WINDOW_SIZE_WIGHT = 300;
    usersSizeWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    usersSizeWindow.setAlwaysOnTop(true);
    JButton okButton = new JButton("Ok");
    JButton cancelButton = new JButton("Cancel");
    JTextField fieldHight = new JTextField(FIELD_SIZE);
    JTextField fieldWight = new JTextField(FIELD_SIZE);
    JTextField cellSize = new JTextField(FIELD_SIZE);
    JLabel markerLabel = new JLabel("");
    usersSizeWindow.setSize(new Dimension(WINDOW_SIZE_WIGHT, WINDOW_SIZE_HEIGHT));
    usersSizeWindow.setResizable(true);
    usersSizeWindow.setUndecorated(true);
    usersSizeWindow.setLayout(new GridLayout(3, 3));
    usersSizeWindow.add(new JLabel("  Field size"));
    usersSizeWindow.add(fieldHight);
    usersSizeWindow.add(fieldWight);
    usersSizeWindow.add(new JLabel("  Cell size"));
    usersSizeWindow.add(cellSize);
    usersSizeWindow.add(markerLabel);
    usersSizeWindow.add(okButton);
    usersSizeWindow.add(cancelButton);
    usersSizeWindow.setVisible(true);

    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        markerLabel.setText("");
        fieldHight.setText("");
        fieldWight.setText("");
        cellSize.setText("");
        usersSizeWindow.setVisible(false);
      }
    });

    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          lifeP.initialize(Integer.parseInt(fieldHight.getText()),
                           Integer.parseInt(fieldWight.getText()));
          lifeP.setCellSize(Integer.parseInt(cellSize.getText()));
          markerLabel.setText("");
          fieldHight.setText("");
          fieldWight.setText("");
          cellSize.setText("");
          usersSizeWindow.setVisible(false);
          SwingUtilities.updateComponentTreeUI(getContentPane());
          pack();
        } catch (NumberFormatException m) {
          markerLabel.setText("  Error");
        }
      }
    });
    usersSizeWindow.setLocationRelativeTo(null);
    usersSizeWindow.setVisible(false);
    pack();
  }

  /**
   * Main method of the program. It sets the standard style of the application
   * and launches the main application thread with a new class LifeSim.
   */
  public static void main(String[] args) {
    System.out.println("Start");
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
    }

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new LifeSim("Life");
      }
    });
  }
}