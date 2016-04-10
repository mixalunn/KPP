package Life;

import java.util.Arrays;

/**
 * Model game "Life"
 * Surface of the field - toroid. ie all opposing edges are a continuation of each other
 * For simulation, using double buffering: data are taken from main array <b>mainField</b>,
 * after the calculation result is added to sub array  <b>backField</b>.
 * After calculating arrays are swapped.
 * The arrays are stored values: 0, if cell is dead, and 1 if alive
 */
public class LifeModel {
  /**
   * Total amount of neighboring cells.
   */
  private static final int NEIGHBORS_CELL_AMOUNT = 8;
  /**
   * Main array.
   */
  private byte[] mainField = null;
  /**
   * Sub array
   */
  private byte[] backField = null;
  /**
   * Number cell on width and height
   */
  private int width, height;
  /**
   * Offsets array
   */
  private int[] neighborOffset = null;
  /**
   * Offsets array on height and wight
   */
  private int[][] neighborXYOffset = null;

  /**
   * Initialization model.
   *
   * @param width  widht data field.
   * @param height height data field.
   */
  public LifeModel(int width, int height) {
    this.width = width;
    this.height = height;
    mainField = new byte[width * height];
    backField = new byte[width * height];
    neighborOffset = new int[]{-width - 1, -width, -width + 1, -1, 1, width - 1, width, width + 1};
    neighborXYOffset = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
  }

  /**
   * @return Number cell on wight.
   */
  public int getWidth() {
    return width;
  }

  /**
   * @return Number cell on height.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Clear cells array.
   */
  public void clear() {
    Arrays.fill(mainField, (byte) 0);
  }

  /**
   * Setting the value of a single cell in array that is get {@link #getCell(int, int)}
   *
   * @param x cell number on height.
   * @param y cell number om wight.
   * @param c value cell: 0 - dead, 1 - alive.
   */
  public void setCell(int x, int y, byte c) {
    mainField[y * width + x] = c;
  }

  /**
   * Getting the value of a single cell in array that is set {@link #setCell(int, int, byte)}
   *
   * @param x cell number on height.
   * @param y - cell number om wight.
   * @return Value cell in byte: 0 - dead, 1 - alive.
   */
  public byte getCell(int x, int y) {
    return mainField[y * width + x];
  }

  /**
   * Setting array cells that is get {@link #getField()}
   *
   * @param temp - new array.
   */
  public void setField(byte[] temp) {
    mainField = temp;
  }

  /**
   * Getting array cells that is set {@link #setField}
   *
   * @return main array <b>mainField</b>
   */
  public byte[] getField() {
    return mainField;
  }

  /**
   * Random filling of the array of cells.
   */
  public void randomByte() {
    for (int i = 0; i < width * height; i++) {
      mainField[i] = (byte) (0 + (int) (Math.random() * ((1 - 0) + 1)));
    }
  }

  /**
   * One step simulation.
   */
  public void simulate() {
    for (int y = 1; y < height - 1; y++) {
      for (int x = 1; x < width - 1; x++) {
        int j = y * width + x;
        byte n = countnei(j);
        backField[j] = simulateCell(mainField[j], n);
      }
    }

    for (int x = 0; x < width; x++) {
      int j = width * (height - 1);
      byte n = countBorderNeighbors(x, 0);
      backField[x] = simulateCell(mainField[x], n);
      n = countBorderNeighbors(x, height - 1);
      backField[x + j] = simulateCell(mainField[x + j], n);
    }

    for (int y = 1; y < height - 1; y++) {
      int j = width * y;
      byte n = countBorderNeighbors(0, y);
      backField[j] = simulateCell(mainField[j], n);
      n = countBorderNeighbors(width - 1, y);
      backField[j + width - 1] = simulateCell(mainField[j + width - 1], n);
    }
    byte[] t = mainField;
    mainField = backField;
    backField = t;
  }

  /**
   * Counting the neighbors to not touch the edges of cells.
   *
   * @param j offset in array.
   * @return amount alive neighbors.
   */
  private byte countnei(int j) {
    byte n = 0;
    for (int i = 0; i < NEIGHBORS_CELL_AMOUNT; i++) {
      n += mainField[j + neighborOffset[i]];
    }
    return n;
  }

  /**
   * Counting neighbors for the boundary cells.
   *
   * @param x cell number on wight.
   * @param y cell number on height.
   * @return Amount alive neighbors.
   */
  private byte countBorderNeighbors(int x, int y) {
    byte n = 0;
    for (int i = 0; i < NEIGHBORS_CELL_AMOUNT; i++) {
      int bx = (x + neighborXYOffset[i][0] + width) % width;
      int by = (y + neighborXYOffset[i][1] + height) % height;
      n += mainField[by * width + bx];
    }
    return n;
  }

  /**
   * Definition of a new state of cell.
   *
   * @param self      own cell condition.
   * @param neighbors number of neighbors.
   * @return New cell condition.
   */
  private byte simulateCell(byte self, byte neighbors) {
    return (byte) (self == 0 ? (neighbors == 3 ? 1 : 0) : neighbors == 2 || neighbors == 3 ? 1 : 0);
  }
}