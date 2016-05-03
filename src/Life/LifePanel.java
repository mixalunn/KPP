package Life;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Panel simulator from a field editor.
 * Left mouse button - put the cell, right - delete.
 * Editing is available at any time, even when simulation is running.
 * Simulation process is performed in a separate thread.
 * @see LifeSim
 */
public class LifePanel extends JPanel implements Runnable {

  /** Default window size. */
  private static final int SIZE_STANDART_PANEL = 100;
  /** CADET BLUE color HEX-code */
  private static final int CADBLUE = 0x5F9EA0;
  /** DARK GREEN color HEX-code */
  private static final int DARKGREEN = 0x006400;
  /** Thread for simulation. */
  private Thread simThread = null;
  /**
   * Model game.
   * @see LifeModel
   */
  private LifeModel life = null;
  /** Delay in milliseconds between steps simulation. */
  private int updateDelay = 100;
  /** Cell size on the screen. */
  private int cellSize = 8;
  /** The spacing between a cells. */
  private int cellGap = 1;
  /** Dead cell color. */
  private static final Color colorField = new Color(CADBLUE);
  /** Alive cell color. */
  private static final Color colorCell = new Color(DARKGREEN);
  /** Byte for saving in thread {@link #saveToFile()} */
  private int saveByte = 0;
  /** Byte for loading in thread {@link #simulateFromFile()} */
  private int readByte = 0;
  /** File for saving. {@link #saveToFile()} */
  private File savefile;
  /** File for loading. {@link #simulateFromFile()} */
  private File loadfile;

  /**
   * Object game panel creation and setting event handlers mouse
   */
  public LifePanel() {
    setBackground(Color.BLACK);
    MouseAdapter ma = new MouseAdapter() {
      private boolean pressedLeft = false;
      private boolean pressedRight = false;

      @Override
      public void mouseDragged(MouseEvent e) {
        setCell(e);
      }

      @Override
      public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
          pressedLeft = true;
          pressedRight = false;
          setCell(e);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
          pressedLeft = false;
          pressedRight = true;
          setCell(e);
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
          pressedLeft = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
          pressedRight = false;
        }
      }

      private void setCell(MouseEvent e) {
        if (life != null) {
          synchronized (life) {
            int x = e.getX() / (cellSize + cellGap);
            int y = e.getY() / (cellSize + cellGap);
            if (x >= 0 && y >= 0 && x < life.getWidth() && y < life.getHeight()) {
              if (pressedLeft == true) {
                life.setCell(x, y, (byte) 1);
                repaint();
              }
              if (pressedRight == true) {
                life.setCell(x, y, (byte) 0);
                repaint();
              }
            }
          }
        }
      }
    };
    addMouseListener(ma);
    addMouseMotionListener(ma);
  }

  /**
   * Gets model game.
   * @return Game model class.
   */
  public LifeModel getLifeModel() {
    return life;
  }

  /**
   * Initialization new model with the set parameters.
   * @param width  amount cell in wight.
   * @param height amount cell in height.
   */
  public void initialize(int width, int height) {
    life = new LifeModel(width, height);
  }

  /**
   * Setting value byte for saving in thread {@link #saveToFile()}
   * @param g value byte.
   */
  public void setSaveByte(int g) {
    saveByte = g;
  }

  /**
   * Setting value byte for loading in thread {@link #simulateFromFile()}
   * @param g value byte.
   */
  public void setLoadByte(int g) {
    readByte = g;
  }

  /**
   * Setting file for saving in thread {@link #saveToFile()}
   * @param f selected file.
   */
  public void setSaveFile(File f) {
    savefile = f;
  }

  /**
   * Setting file for loading in thread {@link #simulateFromFile()}
   * @param f selected file.
   */
  public void setLoadFile(File f) {
    loadfile = f;
  }

  /**
   * Setting value cell size.
   * @param s - cell size.
   */
  public void setCellSize(int s) {
    synchronized (life) {
      life.simulate();
    }
    cellSize = s;
  }

  /**
   * Setting delay updating game field.
   * @param updateDelay delay in ms.
   */
  public void setUpdateDelay(int updateDelay) {
    this.updateDelay = updateDelay;
  }

  /** Start thread for simulation game */
  public void startSimulation() {
    if (simThread == null) {
      simThread = new Thread(this);
      if(saveByte == 1)
        saveToFile();
      simThread.start();
    }
  }

  /** Stop thread for simulation game.*/
  public void stopSimulation() {
    simThread = null;
  }

  /**
   * Status check of running thread.
   * @return True if thread is running.
   */
  public boolean isSimulating() {
    return simThread != null;
  }

  /** Saving in file. */
  public void saveToFile() {
    byte[] tempField = life.getField();
    try {
      if (!savefile.exists()) {
        savefile.createNewFile();
      }
      PrintWriter out = new PrintWriter(
              new OutputStreamWriter(
                      new FileOutputStream(savefile.getAbsolutePath(), true), "UTF-8"));
      try {
        out.println(savefile.getName());
        out.println(Integer.toString(life.getWidth()));
        out.println(Integer.toString(life.getHeight()));
        out.println(Integer.toString(cellSize));
        for (int i = 0; i < life.getHeight(); i++) {
          for (int g = 0; g < (life.getWidth()); g++) {
            out.print(tempField[i * life.getWidth() + g]);
          }
          out.println();
        }
      } finally {
        out.close();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Load and simulation from file. */
  public void simulateFromFile() {
    int hight, widht, tempCellSize;
    byte[] tempField;
    try {
      BufferedReader inTextStream =
              new BufferedReader(new FileReader(loadfile.getAbsoluteFile()));
      try {
        char arrayValueCharField[];
        while (true) {
          Thread.sleep(updateDelay);

          if (readByte == 0) { break; }
          if (inTextStream.readLine()  == null) { break; }

          widht = Integer.parseInt(inTextStream.readLine());
          hight = Integer.parseInt(inTextStream.readLine());
          tempCellSize = Integer.parseInt(inTextStream.readLine());
          tempField = new byte[widht * hight];

          int numberCell = 0;
          for (int i = 0; i < hight; i++) {
            arrayValueCharField = inTextStream.readLine().toCharArray();
            for (int g = 0; g < widht; g++) {
              if (arrayValueCharField[g] == '1') {
                tempField[numberCell] = 1;
              } else {
                tempField[numberCell] = 0;
              }
              numberCell++;
            }
          }

          initialize(widht, hight);
          setCellSize(tempCellSize);
          getLifeModel().setField(tempField);
          repaint();
        }
      } finally {
        inTextStream.close();
      }
    } catch (InterruptedException e) {
      System.out.println("Delayerr");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    setLoadByte(0);
    stopSimulation();
  }

  /** Setting display field and check save/load in the thread. */
  @Override
  public void run() {
    while (simThread != null) {
      synchronized (life) {
        if (readByte == 0) {
          life.simulate();
          if (saveByte == 1) {
            saveToFile();
          }
        } else {
          simulateFromFile();
        }
        repaint();
      }
      try {
        Thread.sleep(updateDelay);
      } catch (InterruptedException e) {
        System.out.println("Delayerr");
      }
    }
  }

  /**
   * Getting  size of the panel based on field size and cells.
   * @return Dimension panel.
   */
  @Override
  public Dimension getPreferredSize() {
    if (life != null) {
      Insets b = getInsets();
      return new Dimension(
              (cellSize + cellGap) * life.getWidth() + cellGap + b.left + b.right,
              (cellSize + cellGap) * life.getHeight() + cellGap + b.top + b.bottom);
    } else
      return new Dimension(SIZE_STANDART_PANEL, SIZE_STANDART_PANEL);
  }

  /** Redrawing content pane. */
  @Override
  protected void paintComponent(Graphics g) {
    if (life != null) {
      synchronized (life) {
        super.paintComponent(g);
        Insets b = getInsets();
        for (int y = 0; y < life.getHeight(); y++) {
          for (int x = 0; x < life.getWidth(); x++) {
            byte c = (byte) life.getCell(x, y);
            g.setColor(c == 1 ? colorCell : colorField);
            g.fillRect(b.left + cellGap + x * (cellSize + cellGap),
                    b.top + cellGap + y * (cellSize + cellGap), cellSize, cellSize);
          }
        }
      }
    }
  }
}
