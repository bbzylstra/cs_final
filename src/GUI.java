import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;


public class GUI extends JFrame implements ActionListener {
    private DrawGraphSquare canvas;
    public static final int CANVAS_WIDTH = 640;
    public static final int CANVAS_HEIGHT = 480;
    public CheckboxGroup cbg;
    private GameBoardSquare game;
    private GameBoardHex g_h;
    int x;
    int y;
    public GUI(){
        x =8;
        y =8;
        game = new GameBoardSquare(x,y);
        g_h = new GameBoardHex(x,y);
        canvas = new DrawGraphSquare(game);
        setLayout(new FlowLayout());
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cp = getContentPane();
        //Add the ubiquitous "Hello World" label.



        JPanel panel = new JPanel();
        add(panel);

        JPanel panel2 = new JPanel();
        panel.add(panel2);

        panel.setBounds(61, 11, 81, 140);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        TextField x_in, y_in;
        x_in = new TextField("8", 3);
        y_in = new TextField("8", 3);


        String[] comboStrings = {"Square Tiles", "Hex Tiles"};

        JComboBox tileBox = new JComboBox(comboStrings);
        tileBox.setSelectedIndex(0);

        tileBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String comboName = (String)cb.getSelectedItem();
                if(comboName.equals(comboStrings[0])){
                    game = new GameBoardSquare(x,y);
                    cp.remove(canvas);
                    canvas = new DrawGraphSquare(game);
                    canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
                    cp.add(canvas);
                    canvas.update_board(game);
                    canvas.repaint();
                    cp.validate();

                }
                else{
                    g_h = new GameBoardHex(x,y);
                    cp.remove(canvas);
                    canvas = new DrawGraphHex(g_h);
                    canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
                    cp.add(canvas);
                    canvas.update_board(g_h);
                    canvas.validate();
                    cp.validate();
                }


            }
        });
        panel2.add(x_in);
        panel2.add(y_in);

        Button set_size = new Button("Change Dimensions");
        set_size.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(canvas instanceof DrawGraphHex)) {
                    x = Integer.valueOf(x_in.getText());
                    y = Integer.valueOf(y_in.getText());

                    game = new GameBoardSquare(x,y);
                    cp.remove(canvas);
                    canvas = new DrawGraphSquare(game);
                    canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
                    cp.add(canvas);
                    canvas.update_board(game);
                    canvas.repaint();
                    cp.validate();
                }

                else{
                    x = Integer.valueOf(x_in.getText());
                    y = Integer.valueOf(y_in.getText());
                    g_h = new GameBoardHex(x,y);
                    cp.remove(canvas);
                    canvas = new DrawGraphHex(g_h);
                    canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
                    cp.add(canvas);
                    canvas.update_board(g_h);
                    canvas.validate();
                    cp.validate();
                }
            }
        });
        panel2.add(set_size);
        panel.add(tileBox);
        cbg = new CheckboxGroup();
        panel.add(new Checkbox("Block Tile", cbg, true));
        panel.add(new Checkbox("Start Tile", cbg, false));
        panel.add(new Checkbox("End Tile", cbg, false));
        panel.add(new Checkbox("Clear Tile", cbg, false));
        Button runB = new Button("Run!");
        runB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!(canvas instanceof DrawGraphHex)) {
                    AStar solver = new AStar(game);
                    List<Point> solvedList = solver.solve();
                    for (int i = 1; i < solvedList.size(); i++) {
                        game.set_tile(solvedList.get(i).getX(), solvedList.get(i).getY(), 4);
                    }
                    canvas.update_board(game);
                    canvas.repaint();
                }
                else{
                    AStar solver = new AStar(g_h);
                    List<Point> solvedList = solver.solve();
                    for (int i = 1; i < solvedList.size(); i++) {
                        g_h.set_tile(solvedList.get(i).getX(), solvedList.get(i).getY(), 4);
                    }

                    canvas.update_board(g_h);
                    canvas.repaint();
                }
            }
        });
        panel.add(runB);

        Button clearS = new Button("Clear");
        clearS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!(canvas instanceof DrawGraphHex)) {
                    for (int i = 0; i < game.getY_size(); i++) {
                        for (int j = 0; j < game.getX_size(); j++) {
                            if (game.get_tile(j, i) == 4) {
                                game.set_tile(j, i, 0);
                            }
                        }
                    }
                    canvas.update_board(game);
                    canvas.repaint();
                }
                else{
                    for (int i = 0; i < g_h.getY_size(); i++) {
                        for (int j = 0; j < g_h.getX_size(); j++) {
                            if (g_h.get_tile(j, i) == 4) {
                                g_h.set_tile(j, i, 0);
                            }
                        }
                    }
                    canvas.update_board(g_h);
                    canvas.repaint();
                }
            }
        });
        panel.add(clearS);
        cp.add(canvas);
        //Display the window.
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt){

    }



    private class DrawGraphSquare extends JPanel{

        private int x;
        private int y;
        private int [][] rects;
        private GameBoardSquare gameb;
        public List<Area> Shapes;

        public boolean contains(java.awt.Point test, java.awt.Point[] points){
            int i;
            int j;
            boolean result = false;
            for (i = 0, j = points.length - 1; i < points.length; j = i++) {
                if ((points[i].y > test.y) != (points[j].y > test.y) &&
                        (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y-points[i].y) + points[i].x)) {
                    result = !result;
                }
            }
            return result;

        }
        public DrawGraphSquare(GameBoardSquare game){
            gameb = game;
            x = gameb.getX_size();
            y = gameb.getY_size();
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x_pos = e.getX();
                    int y_pos = e.getY();
                    int x_click=-1;
                    int y_click=-1;
                    if(!(canvas instanceof DrawGraphHex)) {
                        if (x_pos < CANVAS_WIDTH - 10 && x_pos > 10 && y_pos < CANVAS_HEIGHT - 10 && y_pos > 10) {
                            x_click = (int) Math.floor(e.getX() * x / (CANVAS_WIDTH - 10));
                            y_click = (int) Math.floor(e.getY() * y / (CANVAS_HEIGHT - 10));
                            Checkbox selected = cbg.getSelectedCheckbox();
                            if (selected.getLabel().compareTo("Block Tile") == 0)
                                game.set_tile(x_click, y_click, 1);
                            else if (selected.getLabel().compareTo("Start Tile") == 0)
                                game.set_tile(x_click, y_click, 2);
                            else if (selected.getLabel().compareTo("End Tile") == 0)
                                game.set_tile(x_click, y_click, 3);
                            else
                                game.set_tile(x_click, y_click, 0);
                            canvas.update_board(game);
                            canvas.repaint();
                        }
                    }
                    else {
                        List<Area> shapeL = canvas.Shapes;
                        for(int i =0;i<shapeL.size();i++){
                            if(shapeL.get(i).contains(e.getPoint())){
                                x_click = (i%x >= x/2)?(2*(i%x)%x + 1):(2*(i%x)%x);
                                y_click = i/x;
                                System.out.println(x_click +", " + y_click + ", " +i);
                                System.out.println(e.getX() + ", " + e.getY());

                                Checkbox selected = cbg.getSelectedCheckbox();
                                if (selected.getLabel().compareTo("Block Tile") == 0)
                                    g_h.set_tile(x_click, y_click, 1);
                                else if (selected.getLabel().compareTo("Start Tile") == 0)
                                    g_h.set_tile(x_click, y_click, 2);
                                else if (selected.getLabel().compareTo("End Tile") == 0)
                                    g_h.set_tile(x_click, y_click, 3);
                                else
                                    g_h.set_tile(x_click, y_click, 0);
                                canvas.update_board(g_h);
                                canvas.repaint();
                            }
                        }



                    }
                }
                @Override
                public void mousePressed(MouseEvent e){

                }
                @Override
                public void mouseReleased(MouseEvent e){

                }
                @Override
                public void mouseEntered(MouseEvent e){

                }
                @Override
                public void mouseExited(MouseEvent e){

                }
            });
        }

        public void drawGrid(Graphics g){
            int left_lim = (CANVAS_WIDTH-20)%x;
            int bottom_lim = (CANVAS_HEIGHT-20)%y;
            System.out.println(left_lim);
            for(int i=10;i<CANVAS_WIDTH;i+=(CANVAS_WIDTH-20)/x){
                g.drawLine(i,10, i,CANVAS_HEIGHT-10-bottom_lim);
            }
            for(int i=10;i<CANVAS_HEIGHT;i+=(CANVAS_HEIGHT-20)/y){
                g.drawLine(10,i, CANVAS_WIDTH-10-left_lim,i);
            }
        }

        public List<Area> newDrawGrid(Graphics g){

            List<Area> ShapeList = new ArrayList<>();
            int height = (CANVAS_HEIGHT-20)/y;
            int width = (CANVAS_WIDTH-20)/x;
            for(int y_lim=0;y_lim<y;y_lim++){
                for(int x_lim=0;x_lim<x;x_lim++){
                    if(gameb.get_tile(x_lim,y_lim)==0) {
                        g.setColor(Color.BLACK);
                        g.drawRect(10 + width * x_lim, 10 + height * y_lim, width, height);
                    }
                    else if(gameb.get_tile(x_lim,y_lim)==1) {
                        g.setColor(Color.BLACK);
                        g.fillRect(10 + width * x_lim, 10 + height * y_lim, width, height);
                    }
                    else if(gameb.get_tile(x_lim,y_lim)==2) {
                        g.setColor(Color.GREEN);
                        g.fillRect(10 + width * x_lim, 10 + height * y_lim, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(10 + width * x_lim, 10 + height * y_lim, width, height);
                    }
                    else if(gameb.get_tile(x_lim,y_lim)==3) {
                        g.setColor(Color.RED);
                        g.fillRect(10 + width * x_lim, 10 + height * y_lim, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(10 + width * x_lim, 10 + height * y_lim, width, height);
                    }
                    else if(gameb.get_tile(x_lim,y_lim)==4) {
                        g.setColor(Color.BLUE);
                        g.fillRect(10 + width * x_lim, 10 + height * y_lim, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(10 + width * x_lim, 10 + height * y_lim, width, height);
                    }
                }
            }
            return ShapeList;

        }

        public void drawRect(int x_tile, int y_tile){
            int height = (CANVAS_HEIGHT-20)/y;
            int width = (CANVAS_WIDTH-20)/x;
            int x1 = 10+width*x_tile;
            int y1 = 10+height*y_tile;
            rects[0][0] = x1;
            rects[0][1] = y1;
            rects[0][2] = width;
            rects[0][3] = height;
            repaint();


        }

        public void update_board(GameBoardSquare g){
            gameb = g;
        }

        /*
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            drawGrid(g);
            for(int [] i : rects){
                System.out.println(i[0] + " " + i[1] + " " +i[2] + " " +i[3]);
                g.fillRect(i[0], i[1], i[2], i[3]);
            }

        }
        */
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Shapes = newDrawGrid(g);

        }
    }


    private class DrawGraphHex extends DrawGraphSquare{

        private GameBoardHex gameb;
        private int x;
        private int y;

        DrawGraphHex(GameBoardHex game){
            super(game);
            gameb = game;
            x = gameb.getX_size();
            y = gameb.getY_size();



        }

        @Override
        public List<Area> newDrawGrid(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            List<Area> HexList = new ArrayList<>();

            int[] x_p = new int[6];
            int[] y_p = new int[6];
            int height = (CANVAS_HEIGHT)/(y+2);
            int width = (CANVAS_WIDTH)/(x);
            int offset_w = width/4;
            int offset_h = height/2;

            if(Shapes == null) {
                for (int y_lim = 0; y_lim < 2*y; y_lim++) {
                    int int_xp = 10;
                    int int_yp = offset_h * y_lim + 10;
                    if (y_lim % 2 == 1) {
                        int_xp += width - offset_w;

                    }

                    for (int x_lim = 0; x_lim < x/2; x_lim++) {
                        x_p[0] = (int_xp + offset_w);
                        y_p[0] = int_yp;

                        x_p[1] = (int_xp + offset_w * 3);
                        y_p[1] = int_yp;

                        x_p[2] = (int_xp + offset_w * 4);
                        y_p[2] = int_yp + offset_h;

                        x_p[3] = (int_xp + offset_w * 3);
                        y_p[3] = int_yp + height;

                        x_p[4] = (int_xp + offset_w);
                        y_p[4] = int_yp + height;

                        x_p[5] = (int_xp);
                        y_p[5] = int_yp + offset_h;

                        Polygon polygon = new Polygon(x_p, y_p, 6);
                        Area a = new Area(polygon);
                        g2.draw(a);
                        HexList.add(a);
                        int_xp += width+2*offset_w;
                    }
                }
                return HexList;
            }
            else{
                for(int y_lim=0;y_lim<y;y_lim++){
                    for(int x_lim=0;x_lim<x;x_lim++){
                        int res = (x_lim%2==1)?(x_lim-1)/2+x/2:x_lim/2;
                        res+=y_lim*x;
                        Area poly = Shapes.get(res);
                        if(gameb.get_tile(x_lim,y_lim)==0) {
                            g2.setColor(Color.WHITE);
                            g2.fill(poly);
                            g2.setColor(Color.BLACK);
                            g2.draw(poly);
                        }
                        else if(gameb.get_tile(x_lim,y_lim)==1) {
                            g2.setColor(Color.BLACK);
                            g2.fill(poly);
                        }
                        else if(gameb.get_tile(x_lim,y_lim)==2) {
                            g2.setColor(Color.GREEN);
                            g2.fill(poly);
                            g2.setColor(Color.BLACK);
                            g2.draw(poly);
                        }
                        else if(gameb.get_tile(x_lim,y_lim)==3) {
                            g2.setColor(Color.RED);
                            g2.fill(poly);
                            g2.setColor(Color.BLACK);
                            g2.draw(poly);
                        }
                        else if(gameb.get_tile(x_lim,y_lim)==4) {
                            g2.setColor(Color.BLUE);
                            g2.fill(poly);
                            g2.setColor(Color.BLACK);
                            g2.draw(poly);
                        }

                    }
                }
                return Shapes;
            }


        }



    }



    public static void main(String [] args){
        new GUI();
    }
}
