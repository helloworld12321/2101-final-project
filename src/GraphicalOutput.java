import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class GraphicalOutput extends JFrame{

    private SolitaireGame s = new SolitaireGame();
    private ArrayList<Shape> topCards = new ArrayList<>();
    private int firstCard;
    private boolean firstCardClicked = false;

    public GraphicalOutput(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) (screenSize.getHeight() * .9);
        setSize(new Dimension(screenWidth, screenWidth));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Solitaire");
        setLocationRelativeTo(null);
        setVisible(true);
        JPanel gamePanel = new MainPanel();
        Color backgroundColor = new Color(0, 150, 0);
        gamePanel.setBackground(backgroundColor);
        gamePanel.setSize(screenWidth, screenWidth);
        getContentPane().add(gamePanel);
    }

    private class MainPanel extends JPanel{

        private MainPanel(){
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    super.mouseClicked(e);
                    //Checks if the shape clicked is one of the clickable shapes
                    for(Shape t : topCards){
                        //Checks if the card clicked is the first one and in a tableau
                        if((t.contains(e.getPoint())) && (!firstCardClicked) && (topCards.indexOf(t) != 11)){
                            firstCard = topCards.indexOf(t);
                            firstCardClicked = true;
                            topCards.clear();
                            break;
                        }
                        //Makes moves for waste and tableaus
                        else if((t.contains(e.getPoint())) && (firstCardClicked)){
                            //Moving from top of tableau to tableau
                            if((topCards.indexOf(t) != firstCard) && (firstCard < 7) && (topCards.indexOf(t) < 7)){
                                try {
                                    s.makeMove(new Move(Move.PileType.TABLEAU, firstCard, Move.PileType.TABLEAU, topCards.indexOf(t)));
                                } catch (IllegalMoveException ex) {
                                    ex.printStackTrace();
                                }
                                firstCardClicked = false;
                                topCards.clear();
                                break;
                            }
                            //Waste pile moves
                            else if((topCards.indexOf(t) != firstCard) && (firstCard == 12)){
                                //Moving from waste to tableau
                                if(topCards.indexOf(t) < 7) {
                                    try {
                                        s.makeMove(new Move(Move.PileType.WASTE, 0, Move.PileType.TABLEAU, topCards.indexOf(t)));
                                    } catch (IllegalMoveException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                //Moving from waste to foundation
                                else if((topCards.indexOf(t) > 6) && (topCards.indexOf(t) < 11)){
                                    try {
                                        s.makeMove(new Move(Move.PileType.WASTE, 0, Move.PileType.FOUNDATION, topCards.indexOf(t) - 7));
                                    } catch (IllegalMoveException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                firstCardClicked = false;
                                topCards.clear();
                                break;
                            }
                            //Moving from tableau to foundation
                            else if((topCards.indexOf(t) > 6) && (topCards.indexOf(t) < 11)){
                                try {
                                    s.makeMove(new Move(Move.PileType.TABLEAU, firstCard, Move.PileType.FOUNDATION, topCards.indexOf(t) - 7));
                                } catch (IllegalMoveException ex) {
                                    ex.printStackTrace();
                                }
                                firstCardClicked = false;
                                topCards.clear();
                                break;
                            }
                            //Moving from foundation to tableau
                            else if((topCards.indexOf(t) < 7) && (6 < firstCard) && (firstCard < 11)){
                                try {
                                    s.makeMove(new Move(Move.PileType.FOUNDATION, firstCard, Move.PileType.TABLEAU, topCards.indexOf(t) - 7));
                                } catch (IllegalMoveException ex) {
                                    ex.printStackTrace();
                                }
                                firstCardClicked = false;
                                topCards.clear();
                                break;
                            }
                            //If you click on the same card, it deselects it
                            else if(topCards.indexOf(t) == firstCard) {
                                firstCardClicked = false;
                                topCards.clear();
                                break;
                            }
                        }
                        //Checks if the card clicked is the stock card
                        else if((t.contains(e.getPoint())) && (topCards.indexOf(t) == 11)){
                            try {
                                s.makeMove(new Move(Move.PileType.STOCK, 0, Move.PileType.WASTE, 0));
                            } catch (IllegalMoveException ex) {
                                ex.printStackTrace();
                            }
                            topCards.clear();
                            break;
                        }
                        //If no card is clicked and a card is selected, deselects the card
                        else if((!t.contains(e.getPoint())) && (firstCardClicked) && (topCards.indexOf(t) == topCards.size() - 1)){
                            firstCardClicked = false;
                            topCards.clear();
                            break;
                        }
                    }
                    repaint();
                }
            });
        }

        //Draws the necessary components in the frame
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            drawCards(g);
            if(firstCardClicked) {
                cardClick(g);
            }
        }
    }

    private void drawCards(Graphics g){
        //Draws the base cards
        for(int x = 0; x < 7; x++ ){
            if(!s.getTableau(x).isEmpty())
                for(int y = 0; y < s.getTableau(x).size(); y++)
                    tableauCard(x, y, g);
            else
                emptyTableauCard(x, g);
        }
        //Draws the foundation cards
        for(int x = 0; x < 4; x++)
            foundCard(g, x);
        //Draws the stock card
        stockCards(g);
    }

    //Draws the cards for the tableau and adds the top one to the clickable shape array list
    private void tableauCard(int x, int y, Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Shape card = new RoundRectangle2D.Double((90 * x) + 140, (30 * y) + 30 , 60, 110, 10, 10);
        g2d.setPaint(Color.WHITE);
        g2d.fill(card);
        g2d.setPaint(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(card);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        if(!s.getTableau(x).get(y).isShowing())
            g2d.setPaint(Color.GRAY);
        else if(s.getTableau(x).get(y).getColor() == 1)
            g2d.setPaint(Color.RED);
        else if(s.getTableau(x).get(y).getColor() == 0)
            g2d.setPaint(Color.BLACK);
        g.drawString(s.getTableau(x).get(y).toString(), (90 * x) + 145, (30 * y) + 50);
        if(y == s.getTableau(x).size() - 1) {
            topCards.add(card);
        }
    }
    //Draws the empty tableau cards where there are no cards in the stack
    private void emptyTableauCard(int x, Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Shape card = new RoundRectangle2D.Double(140 + (90 * x), 30 , 60, 110, 10, 10);
        g2d.setPaint(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(card);
        topCards.add(card);
    }
    //Draws the foundation cards and the starting empty stacks
    private void foundCard(Graphics g, int x){
        Graphics2D g2d = (Graphics2D) g;
        Shape card = new RoundRectangle2D.Double(410 + (90 * x), 600 , 60, 110, 10, 10);
        topCards.add(card);
        g2d.setPaint(Color.WHITE);
        g2d.fill(card);
        g2d.setPaint(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(card);
        if(s.getFoundation(x).isEmpty()) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            switch(x){
                case 0:
                    g2d.setPaint(Color.BLACK);
                    g.drawString("♣", 423 + (90 * x), 671);
                    break;
                case 1:
                    g2d.setPaint(Color.RED);
                    g.drawString("♢", 423 + (90 * x), 671);
                    break;
                case 2:
                    g2d.setPaint(Color.BLACK);
                    g.drawString("♠", 423 + (90 * x), 671);
                    break;
                case 3:
                    g2d.setPaint(Color.RED);
                    g.drawString("♡", 423 + (90 * x), 671);
                    break;
            }
        }
        else{
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            if(s.getFoundation(x).peek().getColor() == 0)
                g2d.setPaint(Color.BLACK);
            else
                g2d.setPaint(Color.RED);
            g.drawString(s.getFoundation(x).peek().toString(), 415 + (90 * x), 620);
        }
    }
    //Draws the stock and waste card
    private void stockCards(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gp1 = new GradientPaint(1, 1,
                Color.red, 10, 10, Color.green, true);
        Shape scard = new RoundRectangle2D.Double(130, 600, 60, 110, 10, 10);
        Shape ccard = new RoundRectangle2D.Double(220, 600, 60, 110, 10, 10);
        if(!s.getStock().isEmpty()) {
            topCards.add(scard);
            g2d.setPaint(gp1);
            g2d.fill(scard);
            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(scard);
        }
        else if(s.getStock().isEmpty()){
            topCards.add(scard);
            g2d.setPaint(Color.WHITE);
            g2d.fill(scard);
            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(scard);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            g.drawString("⟳", 140, 650);
        }
        if(!s.getWaste().isEmpty()) {
            topCards.add(ccard);
            g2d.setPaint(Color.WHITE);
            g2d.fill(ccard);
            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(ccard);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            if(s.getWaste().peek().getColor() == 1)
                g2d.setPaint(Color.RED);
            g.drawString(s.getWaste().peek().toString(), 225, 620);
        }
    }
    //Draws an outline on a card when it is clicked
    private void cardClick(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.YELLOW);
        g2d.setStroke(new BasicStroke(5));
        g2d.draw(topCards.get(firstCard));
    }
}
