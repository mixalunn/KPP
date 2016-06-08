package Life;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ScalaLife.*;

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
  /** Directory for saving*/
  private String homeDir = "C:\\Users\\Mixalunn\\Documents\\SaveGame";

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

    JMenuBar menuBar = new JMenuBar();
    add(menuBar, BorderLayout.NORTH);
    JMenu menuFile = new JMenu("Game  ");
    menuBar.add(menuFile);
    JMenu sizeMenu = new JMenu("Size  ");
    menuBar.add(sizeMenu);
    JMenu menuView = new JMenu("View  ");
    menuBar.add(menuView);
    JMenu sortFile = new JMenu("Sort  ");
    menuBar.add(sortFile);

    JMenuItem startStopItem = new JMenuItem("Run");
    menuFile.add(startStopItem);
    JMenuItem clearItem = new JMenuItem("Clear");
    menuFile.add(clearItem);
    menuFile.addSeparator();
    JMenuItem randItem = new JMenuItem("Randomize");
    menuFile.add(randItem);
    menuFile.addSeparator();
    JMenuItem saveItem = new JMenuItem("Save as");
    menuFile.add(saveItem);
    JMenuItem loadItem = new JMenuItem("Load");
    menuFile.add(loadItem);
    menuFile.addSeparator();
    JMenuItem exitItem = new JMenuItem("Exit");
    menuFile.add(exitItem);

    JMenu frameSizeMenu = new JMenu("Field size");
    sizeMenu.add(frameSizeMenu);
    JMenuItem smallSize = new JMenuItem("Small-70x40");
    frameSizeMenu.add(smallSize);
    JMenuItem mediumSize = new JMenuItem("Middle-100x70");
    frameSizeMenu.add(mediumSize);
    JMenuItem bigSize = new JMenuItem("Large-150x80");
    frameSizeMenu.add(bigSize);

    JMenu cellSizeMenu = new JMenu("Cell size");
    sizeMenu.add(cellSizeMenu);
    JMenuItem cellSSize = new JMenuItem("Small");
    cellSizeMenu.add(cellSSize);
    JMenuItem cellMSize = new JMenuItem("Middle");
    cellSizeMenu.add(cellMSize);
    JMenuItem cellLSize = new JMenuItem("Large");
    cellSizeMenu.add(cellLSize);
    JMenuItem usersSize = new JMenuItem("Other");
    sizeMenu.add(usersSize);

    JMenuItem defStyle = new JMenuItem("Standard");
    menuView.add(defStyle);
    JMenuItem ironStyle = new JMenuItem("Metall");
    menuView.add(ironStyle);
    JMenuItem motifStyle = new JMenuItem("Motif");
    menuView.add(motifStyle);
    JMenuItem nimbusStyle = new JMenuItem("Nimbus");
    menuView.add(nimbusStyle);

    JMenuItem stat = new JMenuItem("Statistic");
    sortFile.add(stat);
    JMenuItem sortJava = new JMenuItem("Sort Java");
    sortFile.add(sortJava);
    JMenuItem sortScala = new JMenuItem("Sort Scala");
    sortFile.add(sortScala);
    JMenu genGame = new JMenu("Generation");
    sortFile.add(genGame);
    JMenuItem ten = new JMenuItem("10");
    ten.addActionListener(new SaveGeneratorListener());
    JMenuItem thousand = new JMenuItem("1000");
    thousand.addActionListener(new SaveGeneratorListener());
    JMenuItem tenThousand = new JMenuItem("10000");
    tenThousand.addActionListener(new SaveGeneratorListener());
    genGame.add(ten);
    genGame.add(thousand);
    genGame.add(tenThousand);


    JButton button = new JButton("Run");
    menuBar.add(button);
    menuBar.add(new JLabel("       Faster"));
    menuBar.add(slider);
    menuBar.add(new JLabel("Slower"));

    lifeP.setUpdateDelay(slider.getValue());
    button.setMaximumSize(new Dimension(SIZE_BUTTON_WIDTH, SIZE_MENU_HEIGHT));
    slider.setMaximumSize(new Dimension(SIZE_SLIDER_WIDTH, SIZE_MENU_HEIGHT));
    slider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        lifeP.setUpdateDelay(slider.getValue());
      }
    });

    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (lifeP.isSimulating()) {
          lifeP.stopSimulation();
          button.setText("Run");
          lifeP.setSaveByte(0);
          lifeP.setLoadByte(0);
        } else {
          lifeP.startSimulation();
          button.setText("Stop");
        }
      }
    });

    startStopItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (lifeP.isSimulating()) {
          lifeP.stopSimulation();
          button.setText("Run");
          startStopItem.setText("Start");
          lifeP.setSaveByte(0);
          lifeP.setLoadByte(0);
        } else {
          lifeP.startSimulation();
          button.setText("Stop");
          startStopItem.setText("Stop");
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
        new SizeWindow("UserSize", lifeP);
      }
    });

    sortJava.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        sortLoad(true);
      }
    });

    sortScala.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed (ActionEvent actionEvent) {
        sortLoad(false);
      }
    });

    stat.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed (ActionEvent actionEvent) {
        File[] files = new File(homeDir).listFiles();
        String[] fileName = new String[files.length];
        int[][] lineArray = new int[files.length][lifeP.getWidth()];
        char arrayValueCharField[];

        try {
          for (int i = 0; i < files.length; i++) {
            int count = 0;
            fileName[i] = files[i].getName();
            BufferedReader reader = new BufferedReader(new FileReader(files[i]));
            try {
              while (true) {
                int tempNumbCell = 0;
                if (reader.readLine() == null) {
                  break;
                }
                int widht = Integer.parseInt(reader.readLine());
                int hight = Integer.parseInt(reader.readLine());
                reader.readLine();
                for (int f = 0; f < hight; f++) {
                  arrayValueCharField = reader.readLine().toCharArray();
                  for (int g = 0; g < widht; g++) {
                    if (arrayValueCharField[g] == '1')
                      tempNumbCell++;
                  }
                }
                lineArray[i][count] = tempNumbCell;
                count++;
              }
            } finally {
              reader.close();
            }
          }
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        new ScalaStat().getStat(fileName,lineArray);
      }
    });

    pack();
    setVisible(true);
  }

  private class SaveGeneratorListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      lifeP.generateGames(Integer.parseInt(event.getActionCommand()));
    }
  }

  /**
   * Function loading files for sorting
   * @param typeSort true - Java sorting else Scala
   */
  private void sortLoad(boolean typeSort) {
    File[] files = new File(homeDir).listFiles();
    String[] fileName = new String[files.length];
    int[] fieldsArray = new int[files.length];

    JavaSort javaSort = new JavaSort();
    ScalaSort scalaSort = new ScalaSort();
    long time = System.currentTimeMillis();
    char arrayValueCharField[];

    try {
      for (int i = 0; i < files.length; i++) {
        fileName[i] = files[i].getName();
        BufferedReader reader = new BufferedReader(new FileReader(files[i]));
        try {
          int tempNumbCell = 0;
          while (true) {
            if (reader.readLine() == null) {
              break;
            }
            int widht = Integer.parseInt(reader.readLine());
            int hight = Integer.parseInt(reader.readLine());
            reader.readLine();
            for (int f = 0; f < hight; f++) {
              arrayValueCharField = reader.readLine().toCharArray();
              for (int g = 0; g < widht; g++) {
                if (arrayValueCharField[g] == '1')
                  tempNumbCell++;
              }
            }
          }
          fieldsArray[i] = tempNumbCell;
        } finally {
          reader.close();
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    String nameTable;
    if(typeSort) {
      javaSort.qSort(fileName, fieldsArray, 0, files.length - 1);
      nameTable = "JavaSort";
    } else {
      scalaSort.sort(fileName, fieldsArray);
      nameTable = "ScalaSort";
    }
    time = System.currentTimeMillis() - time;
    new SortTable(nameTable, fileName, fieldsArray, Long.toString(time));
  }


  /**
   * Opening uaersSizeWinow to save a file selection
   * @throws IOException Exception save in file
   */
  public void openWindowFrame() throws IOException {
    JFileChooser openDialog = new JFileChooser();
    openDialog.setCurrentDirectory(new File(homeDir));
    openDialog.showOpenDialog(this);
    lifeP.setLoadFile(openDialog.getSelectedFile());
    lifeP.setLoadByte(1);
  }

  /**
   * Opening uaersSizeWinow to load a file selection
   * @throws IOException Exception load from file
   */
  public void saveWindowFrame() throws IOException {
    JFileChooser saveDialog = new JFileChooser();
    saveDialog.setCurrentDirectory(new File(homeDir));
    saveDialog.showSaveDialog(this);
    lifeP.setSaveFile(saveDialog.getSelectedFile());
    lifeP.setSaveByte(1);
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
