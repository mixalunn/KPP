package Life;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;

public class Simulate extends JFrame {

    private Thread starstopTread = null;
    private LifePan lifeP = new LifePan();
    private JMenuBar menuBar    = new JMenuBar();

    private JMenu   menuFile    = new JMenu("Game");
    private JMenu   menuView    = new JMenu("View");
    private JMenu   sizeMenu    = new JMenu("Size");
    private JMenu   frameSizeMenu    = new JMenu("Field size");
    private JMenu   cellSizeMenu    = new JMenu("Cell size");

    private JMenuItem startItem = new JMenuItem("Run");
    private JMenuItem stopItem  = new JMenuItem("Stop");
    private JMenuItem exitItem  = new JMenuItem("Exit");
    private JMenuItem clearItem = new JMenuItem("Clear");
    private JMenuItem randItem = new JMenuItem("Randomize");
    private JMenuItem saveItem  = new JMenuItem("Save as");
    private JMenuItem loadItem  = new JMenuItem("Load");


    private JMenuItem defStyle  = new JMenuItem("Standard");
    private JMenuItem ironStyle  = new JMenuItem("Metall");
    private JMenuItem motifStyle  = new JMenuItem("Motif");
    private JMenuItem nimbusStyle = new JMenuItem("Nimbus");


    private JMenuItem smallSize   = new JMenuItem("Small-70x30");
    private JMenuItem mediumSize  = new JMenuItem("Middle-100x70");
    private JMenuItem bigSize     = new JMenuItem("Large-150x80");

    private JMenuItem cellSSize   = new JMenuItem("Small-4");
    private JMenuItem cellMSize   = new JMenuItem("Middle-8");
    private JMenuItem cellLSize   = new JMenuItem("Large-12");

    private JMenuItem usersSize    = new JMenuItem("Other");

    private JFrame    usersSizeWindow = new JFrame("Users sizes");

    private JButton				button1				= new JButton("Run");
    private JSlider				slider				= new JSlider(JSlider.HORIZONTAL,1, 500, 50);

    public Simulate(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // размеры поля
        lifeP.initialize(100, 60);
        add(lifeP);

        add(menuBar, BorderLayout.NORTH);

        //добавления пунктов меню "Игра"
        menuBar.add(menuFile);
        menuBar.add(new JLabel("     "));
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


        //добавления пунктов меню "Вид"
        menuView.add(defStyle);
        menuView.add(ironStyle);
        menuView.add(motifStyle);
        menuView.add(nimbusStyle);

        //добавления пунктов меню "Размер"
        sizeMenu.add(frameSizeMenu);
        sizeMenu.add(cellSizeMenu);
        sizeMenu.add(usersSize);

        //добавления пунктов меню "Погльзовательский Размер"
        frameSizeMenu.add(smallSize);
        frameSizeMenu.add(mediumSize);
        frameSizeMenu.add(bigSize);

        //добавления пунктов меню "Размер клетки"
        cellSizeMenu.add(cellSSize);
        cellSizeMenu.add(cellMSize);
        cellSizeMenu.add(cellLSize);

        menuBar.add(new JLabel("     "));
        menuBar.add(button1);
        menuBar.add(new JLabel("                       Faster"));
        menuBar.add(slider);
        menuBar.add(new JLabel("Slower"));

        // бегунок, регулирующий скорость симуляции (задержка в мс между шагами симуляции)

        lifeP.setUpdateDelay(slider.getValue());
        slider.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) {
                lifeP.setUpdateDelay(slider.getValue());
            }
        });



        menuBar.add(new JLabel("                      "));
        menuBar.add(menuView);
        menuView.add(defStyle);
        menuView.add(ironStyle);
        menuView.add(motifStyle);
        menuView.add(nimbusStyle);


        // запуск/остановка симуляции; попутно меняется надпись на кнопке
        button1.setMaximumSize(new Dimension(100, 50));
        button1.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
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

        //
        startItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (!lifeP.isSimulating()) {
                    lifeP.startSimulation();
                    button1.setText("Stop");
                }
            }
        });

        //
        stopItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (lifeP.isSimulating()) {
                    lifeP.stopSimulation();
                    button1.setText("Run");
                    lifeP.setSaveByte(0);
                    lifeP.setLoadByte(0);

                }
            }
        });

        // пункт меню "Игра"-> "Очистка"
        clearItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                synchronized (lifeP.getLifeModel()) {
                    lifeP.getLifeModel().clear();
                    lifeP.repaint();
                }
            }
        });

        // пункт меню "Игра"-> "Случаное заполнение"
        randItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                synchronized (lifeP.getLifeModel()) {
                    lifeP.getLifeModel().randomByte();
                    lifeP.repaint();
                }
            }
        });
        //пункт меню "Игра"-> "Сохранить"
        saveItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                saveWindowFrame();
            }
        });
        //пункт меню "Игра"-> "Загрузить"
        loadItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    openWindowFrame();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        //пункт меню "Игра"-> "Выход"
        exitItem.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // пункт подменю "Стиль"->	"Стандартный"
        defStyle.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception m) {}
                SwingUtilities.updateComponentTreeUI(getContentPane());
                pack();
            }
        });
        // пункт подменю "Стиль"-> "Iron"
        ironStyle.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (Exception m) {}
                SwingUtilities.updateComponentTreeUI(getContentPane());
                pack();
            }
        });
        // пункт подменю "Стиль"-> "Motif"
        motifStyle.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                } catch (Exception m) {}
                SwingUtilities.updateComponentTreeUI(getContentPane());
                pack();
            }
        });
        // пункт подменю "Стиль"-> "Nimbus"
        nimbusStyle.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception m) {}
                SwingUtilities.updateComponentTreeUI(getContentPane());
                pack();
            }
        });

        // пункт подменю "Размер"-> "Маленький"
        smallSize.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                lifeP.initialize(70, 30);
                SwingUtilities.updateComponentTreeUI(getContentPane());
                pack();

            }
        });
        // пункт подменю "Размер"-> "Средний"
        mediumSize.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                lifeP.initialize(100, 70);
                SwingUtilities.updateComponentTreeUI(getContentPane());
                pack();

            }
        });
        // пункт подменю "Размер"-> "Большой"
        bigSize.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                lifeP.initialize(150, 80);
                SwingUtilities.updateComponentTreeUI(getContentPane());
                pack();

            }
        });
        //  пункт подменю "Размер клетки"-> "Маленький"
        cellSSize.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                lifeP.setCellSize(4);
                SwingUtilities.updateComponentTreeUI(getContentPane());
                pack();

            }
        });
        //пункт подменю "Размер клетки"-> "Средний"
        cellMSize.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                lifeP.setCellSize(8);
                SwingUtilities.updateComponentTreeUI(getContentPane());
                pack();

            }
        });
        //пункт подменю "Размер клетки"-> "Большой"
        cellLSize.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                lifeP.setCellSize(12);
                SwingUtilities.updateComponentTreeUI(getContentPane());
                pack();

            }
        });

        usersSize.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                usersSizeWindow.setVisible(true);

            }
        });



        pack();
        setVisible(true);
        usersWindow();
        //openWindowFrame();
        //saveWindowFrame();
    }

    public void openWindowFrame() throws IOException{
        JFileChooser dialog1 = new JFileChooser();
        dialog1.showOpenDialog(this);
        lifeP.setLoadFile(dialog1.getSelectedFile());
        lifeP.setLoadByte(1);
    }

    public void saveWindowFrame(){
        JFileChooser dialog2 = new JFileChooser();
        dialog2.showSaveDialog(this);
        lifeP.setSaveFile(dialog2.getSelectedFile());
        lifeP.setSaveByte(1);
    }




    //создание отдельного окна изменения пользователького размера
    public void usersWindow()
    {
        usersSizeWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        usersSizeWindow.setAlwaysOnTop(true);
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cansel");
        JTextField fieldHight = new JTextField(7);
        JTextField fieldWight = new JTextField(7);
        JTextField cellSize = new JTextField(7);
        JLabel     markerLabel = new JLabel("");
        usersSizeWindow.setSize(new Dimension(300, 70));
        usersSizeWindow.setResizable(true);
        usersSizeWindow.setUndecorated(true);
        usersSizeWindow.setLayout(new GridLayout(3,3));
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
            @Override public void actionPerformed(ActionEvent e) {
                markerLabel.setText("");
                fieldHight.setText("");
                fieldWight.setText("");
                cellSize.setText("");
                usersSizeWindow.setVisible(false);
            }
        });
        okButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try{
                    lifeP.initialize(Integer.parseInt(fieldHight.getText()),Integer.parseInt(fieldWight.getText()));
                    lifeP.setCellSize(Integer.parseInt(cellSize.getText()));
                    markerLabel.setText("");
                    fieldHight.setText("");
                    fieldWight.setText("");
                    cellSize.setText("");
                    usersSizeWindow.setVisible(false);
                    SwingUtilities.updateComponentTreeUI(getContentPane());
                    pack();

                }catch(NumberFormatException m){
                    markerLabel.setText("  Error");
                }

            }
        });
        usersSizeWindow.setLocationRelativeTo(null);
        usersSizeWindow.setVisible(false);
        pack();
    }

    public static void main(String[] args) {

        System.out.println("Start");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Simulate("Life");
            }
        });
    }


}