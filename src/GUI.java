import sun.java2d.loops.DrawRect;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;


public class GUI extends JFrame implements ActionListener {
    private DrawGraph canvas;
    public static final int CANVAS_WIDTH = 640;
    public static final int CANVAS_HEIGHT = 480;
    public GUI(){
        int x =5;
        int y =8;
        GameBoard game = new GameBoard(x,y);
        canvas = new DrawGraph(game);
        setLayout(new FlowLayout());
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cp = getContentPane();
        //Add the ubiquitous "Hello World" label.

        cp.add(canvas);

        JPanel panel = new JPanel();
        add(panel);
        panel.setBounds(61, 11, 81, 140);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        CheckboxGroup cbg = new CheckboxGroup();
        panel.add(new Checkbox("Block Tile", cbg, true));
        panel.add(new Checkbox("Start Tile", cbg, false));
        panel.add(new Checkbox("End Tile", cbg, false));
        panel.add(new Checkbox("Clear Tile", cbg, false));
        //Display the window.
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x_pos = e.getX();
                int y_pos = e.getY();
                int x_click=-1;
                int y_click=-1;
                if(x_pos < CANVAS_WIDTH-10 && x_pos>10 && y_pos <CANVAS_HEIGHT-10 && y_pos>10){
                    x_click= (int)Math.floor(e.getX()*x/(CANVAS_WIDTH-10));
                    y_click=(int)Math.floor(e.getY()*y/(CANVAS_HEIGHT-10));
                    Checkbox selected = cbg.getSelectedCheckbox();
                    if(selected.getLabel().compareTo("Block Tile")==0)
                        game.set_tile(x_click, y_click, 1);
                    else if (selected.getLabel().compareTo("Start Tile")==0)
                        game.set_tile(x_click, y_click, 2);
                    else if (selected.getLabel().compareTo("End Tile")==0)
                        game.set_tile(x_click, y_click, 3);
                    else
                        game.set_tile(x_click, y_click, 0);
                    canvas.update_board(game);
                    canvas.repaint();


                }

                System.out.println(x_click+","+y_click);//these co-ords are relative to the component
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
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt){

    }



    private class DrawGraph extends JPanel{

        private int x;
        private int y;
        private int [][] rects;
        private GameBoard gameb;
        public DrawGraph(GameBoard game){
            gameb = game;
            x = gameb.getX_size();
            y = gameb.getY_size();

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

        public void newDrawGrid(Graphics g){
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
                    }
                    else if(gameb.get_tile(x_lim,y_lim)==3) {
                        g.setColor(Color.RED);
                        g.fillRect(10 + width * x_lim, 10 + height * y_lim, width, height);
                    }
                }
            }

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

        public void update_board(GameBoard g){
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
            newDrawGrid(g);

        }
    }



    public static void main(String [] args){
        new GUI();
    }
}
