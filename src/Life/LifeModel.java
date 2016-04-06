package Life;

import java.util.Arrays;

/**
 * Модель игры "Жизнь".
 * Поверхность поля - тор. т.е все протиивоположные края являютя продолжением друг друга
 * Используется более медленный, но более понятный алгоритм - данные берутся из главного массива  и
 * после расчета результат складывается во вспомогательный массив.
 * По окончании расчета ссылки  массивов меняются местами.
 * В массивах хранятся значения: 0, если клетка мертва, и 1, если жива.
 */

public class LifeModel {
    private byte[] mainField = null; //главный массив
    private byte[] backField = null; //вспомогательный
    private int width, height; //ширина и высота поля данных
    private int[] nieborder = null; //массив смещения соседних полей по границе
    private int[][] neioffset = null; //масив смещения соседних полей

    /*
     Инициализация модели.
    */
    public LifeModel(int width, int height) {
        this.width = width;
        this.height = height;
        mainField = new byte[width * height];
        backField = new byte[width * height];
        nieborder = new int[]{-width - 1, -width, -width + 1, -1, 1, width - 1, width, width + 1};
        neioffset = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void clear() {
        Arrays.fill(mainField, (byte) 0);//очистка поля
    }


    public void setCell(int x, int y, byte c) {
        mainField[y * width + x] = c;
    }

    public byte getCell(int x, int y) {
        return mainField[y * width + x];
    }

    public void setField(byte[] temp) {
        mainField = temp;
    }

    public byte[] getField() {
        return mainField;
    }

    public void randomByte() {
        for (int i = 0; i < width * height; i++) {
            mainField[i] = (byte) (0 + (int) (Math.random() * ((1 - 0) + 1)));
        }
    }

    public void simulate() { //один шаг симуляции
        for (int y = 1; y < height - 1; y++) { // обрабатываем клетки, не касающиеся краев поля
            for (int x = 1; x < width - 1; x++) {
                int j = y * width + x;
                byte n = countnei(j);
                backField[j] = simulateCell(mainField[j], n);
            }
        }

        // обрабатываем граничные клетки
        // верхняя и нижняя строки
        for (int x = 0; x < width; x++) {
            int j = width * (height - 1);
            byte n = countBorderNeighbors(x, 0);
            backField[x] = simulateCell(mainField[x], n);
            n = countBorderNeighbors(x, height - 1);
            backField[x + j] = simulateCell(mainField[x + j], n);
        }
        // крайние левый и правый столбцы
        for (int y = 1; y < height - 1; y++) {
            int j = width * y;
            byte n = countBorderNeighbors(0, y);
            backField[j] = simulateCell(mainField[j], n);
            n = countBorderNeighbors(width - 1, y);
            backField[j + width - 1] = simulateCell(mainField[j + width - 1], n);
        }

        // обмениваем поля местами
        byte[] t = mainField;
        mainField = backField;
        backField = t;
    }

    /*
    Подсчет соседей для не касающихся краев клеток.
    j - смещение клетки в массиве
     */
    private byte countnei(int j) {
        byte n = 0;
        for (int i = 0; i < 8; i++) {
            n += mainField[j + nieborder[i]];
        }
        return n;
    }

    /*
     Подсчет соседей для граничных клеток.
     */
    private byte countBorderNeighbors(int x, int y) {
        byte n = 0;
        for (int i = 0; i < 8; i++) {
            int bx = (x + neioffset[i][0] + width) % width;
            int by = (y + neioffset[i][1] + height) % height;
            n += mainField[by * width + bx];
        }
        return n;
    }

    /*
      Симуляция для одной клетки.
      self собственное состояние клетки: 0/1
      neighbors кол-во соседей
      return новое состояние клетки: 0/1
     */
    private byte simulateCell(byte self, byte neighbors) {
        return (byte) (self == 0 ? (neighbors == 3 ? 1 : 0) : neighbors == 2 || neighbors == 3 ? 1 : 0);
    }
}