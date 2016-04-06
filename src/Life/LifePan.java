package Life;

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

import javax.swing.JPanel;

/*
 Панель симулятора с редактором поля.
 Левой кнопкой мыши можно ставить клетки, правой - стирать. Редактирование доступно в любое время, даже когда
 симуляция запущена.
 Процесс симуляции выполняется в отдельном потоке.
 */
public class LifePan extends JPanel implements Runnable {

    private Thread simThread = null;
    private Model life = null;
    //Задержка в мс между шагами симуляции.
    private int updateDelay = 100;
    //Размер клетки на экране.
    private int cellSize = 8;
    // Промежуток между клетками.
    private int cellGap = 1;
    //Цвет мертвой клетки.
    private static final Color c0 = new Color(0x5F9EA0);
    //Цвет живой клетки.
    private static final Color c1 = new Color(0x006400);

    //Создание переменных и файлов для сохранения
    private int saveByte = 0;
    private int readByte = 0;
    private File savefile;
    private File loadfile;
    private boolean endLoadFile = false;
    public LifePan() {
        setBackground(Color.BLACK);

        // редактор поля

        //создания класса для получения событий при работе с мышью
        MouseAdapter ma = new MouseAdapter() {
            private boolean pressedLeft = false;    // нажата левая кнопка мыши
            private boolean pressedRight = false;    // нажата правая кнопка мыши

            // Обработчик кординат нахождения курсора
            @Override
            public void mouseDragged(MouseEvent e) {
                setCell(e);
            }

            // Обработчикнажатия
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

            //Отпускание кнопки
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    pressedLeft = false;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    pressedRight = false;
                }
            }

            // Устанавливает/стирает клетку.
            private void setCell(MouseEvent e) {
                if (life != null) {
                    synchronized (life) {
                        // рассчитываем координаты клетки, на которую указывает курсор мыши
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

    //фунцкии доступа к обьектам и переменным вне класса
    public Model getLifeModel() {
        return life;
    }
    public boolean getEndLoadFile() {return endLoadFile;}

    public void initialize(int width, int height) {
        life = new Model(width, height);
    }

    public void setSaveByte(int g) {
        saveByte = g;
    }

    public void setLoadByte(int g) {
        readByte = g;
    }

    public void setSaveFile(File f) {
        savefile = f;
    }

    public void setLoadFile(File f) {
        loadfile = f;
    }

    //Установка значения размера клетки
    public void setCellSize(int s) {
        synchronized (life) {
            life.simulate();
        }
        cellSize = s;
    }

    public int getCellSize() {
        return cellSize;

    }

    public void setUpdateDelay(int updateDelay) {
        this.updateDelay = updateDelay;
    }

    // Запуск потока
    public void startSimulation() {
        if (simThread == null) {
            simThread = new Thread(this);
            simThread.start();
        }
    }

    //Остановка потока
    public void stopSimulation() {
        simThread = null;
    }

    //проверка работы потока
    public boolean isSimulating() {
        return simThread != null;
    }

    public void saveToFile() {
        {
            byte[] tempField = life.getField(); //Возвращаем масив в данный момент

            //проверка, существует ли выбранный файл если нет то создаем новый
            try {
                if (!savefile.exists()) {
                    savefile.createNewFile();
                }

                PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savefile.getAbsolutePath(), true), "UTF-8"));
                try { //записываем данные в файл
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
                    //
                    out.close();
            }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //симуляция из файла
    public void simulateFromFile() {
        String hight, widht, tempCellSize;
        byte[] tempField;

        //������ ��� ������ ����� � �����
        try {
            BufferedReader in = new BufferedReader(new FileReader(loadfile.getAbsoluteFile()));
            //� ����� ��������� ��������� ����
            try {
                char s[];
                String s1;

                while (true) {
                    try {
                        Thread.sleep(updateDelay);
                    }
                    catch (InterruptedException e) {}

                    if (readByte == 0) break;
                    if (in.readLine() == null) break;

                    widht = in.readLine();
                    hight = in.readLine();
                    tempCellSize = in.readLine();
                    tempField = new byte[Integer.parseInt(widht) * Integer.parseInt(hight)];
                    int k = 0;

                    for (int i = 0; i < Integer.parseInt(hight); i++) {
                        s1 = in.readLine();
                        s = s1.toCharArray();
                        for (int g = 0; g < Integer.parseInt(widht); g++) {
                            if (s[g] == '1') {
                                tempField[k] = 1;
                            } else {
                                tempField[k] = 0;
                            }

                            //s[g]
                            k++;
                        }
                    }
                    initialize(Integer.parseInt(widht), Integer.parseInt(hight));
                    setCellSize(Integer.parseInt(tempCellSize));
                    getLifeModel().setField(tempField);
                    repaint();
                }


            }
            finally {
                //закрытие потока ввода
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setLoadByte(0);
        endLoadFile = true;
    }



    @Override
    public void run() {
        repaint();
        while (simThread != null) {
            try {
                Thread.sleep(updateDelay);
            } catch (InterruptedException e) {
            }
            // синхронизация используется для того, чтобы метод paintComponent не выводил на экран
            // содержимое поля, которое в данный момент меняется
            synchronized (life) {
                if (readByte == 0) {
                    if (saveByte == 1) {
                        saveToFile();
                    }

                    life.simulate();
                } else {
                    simulateFromFile();
                }


                repaint();
                //setSaveByte((byte) 0);

            }
        }
        repaint();
    }

    //Возвращает размер панели с учетом размера поля и клеток.
    @Override
    public Dimension getPreferredSize() {
        if (life != null) {
            Insets b = getInsets();
            return new Dimension((cellSize + cellGap) * life.getWidth() + cellGap + b.left + b.right,
                    (cellSize + cellGap) * life.getHeight() + cellGap + b.top + b.bottom);
        } else
            return new Dimension(100, 100);
    }

    // Прорисовка содержимого панели.
    @Override
    protected void paintComponent(Graphics g) {
        if (life != null) {
            synchronized (life) {
                super.paintComponent(g);
                Insets b = getInsets();
                for (int y = 0; y < life.getHeight(); y++) {
                    for (int x = 0; x < life.getWidth(); x++) {
                        byte c = (byte) life.getCell(x, y);
                        g.setColor(c == 1 ? c1 : c0);
                        g.fillRect(b.left + cellGap + x * (cellSize + cellGap), b.top + cellGap + y
                                * (cellSize + cellGap), cellSize, cellSize);
                    }
                }
            }
        }
    }

}
