package domain;

import game.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Poker extends JLabel implements MouseListener {
    GameFrame gameFrame;
    String name;
    boolean front;
    boolean canClick = false;
    boolean clicked = false;

    public Poker(GameFrame m, String name, boolean front) {
        this.gameFrame = m;
        this.name = name;
        this.front = front;
        if (this.front) {
            this.turnFront();
        } else {
            this.turnRear();
        }
        //设置牌的宽高大小
        this.setSize(71, 96);
        //把牌显示出来
        this.setVisible(true);
        //给每一张牌添加鼠标监听
        this.addMouseListener(this);
    }

    public Poker() {
    }

    public Poker(GameFrame gameFrame, String name, boolean front, boolean canClick, boolean clicked) {
        this.gameFrame = gameFrame;
        this.name = name;
        this.front = front;
        this.canClick = canClick;
        this.clicked = clicked;
    }

    public void turnFront() {
        this.setIcon(new ImageIcon("image\\poker\\" + name + ".png"));
        this.front = true;
    }

    public void turnRear() {
        this.setIcon(new ImageIcon("image\\poker\\rear.png"));
        this.front = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (canClick) {
            Point from = this.getLocation();
            int step;
            if (clicked) {
                step = 20;
            } else {
                step = -20;
            }
            clicked = !clicked;
            Point to = new Point(from.x, from.y + step);
            this.setLocation(to);
        }
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFront() {
        return front;
    }


    public void setFront(boolean front) {
        this.front = front;
    }

    public boolean isCanClick() {
        return canClick;
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }


    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    @Override
    public String toString() {
        return "Poker{" +
                "gameFrame=" + gameFrame +
                ", name='" + name + '\'' +
                ", front=" + front +
                ", canClick=" + canClick +
                ", clicked=" + clicked +
                '}';
    }
}
