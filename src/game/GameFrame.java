package game;

import domain.Poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class GameFrame extends JFrame implements ActionListener {
    public Container container = null;
    JButton[] landlord = new JButton[2];
    JButton[] publishCard = new JButton[2];

    int landlordFlag;
    int turn;

    JLabel landlordIcon;

    ArrayList<ArrayList<Poker>> currentList = new ArrayList<>();
    ArrayList<ArrayList<Poker>> playerList = new ArrayList<>();
    ArrayList<Poker> lordList = new ArrayList<>();
    ArrayList<Poker> pokerList = new ArrayList<>();
    JTextField[] time = new JTextField[3];

    PlayerOperation po;

    boolean nextPlayer = false;

    public GameFrame() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("image\\landlord.png"));
        initJframe();
        initView();
        this.setVisible(true);
        new Thread(this::initCard).start();
        initGame();
    }

    public void initJframe() {
        this.setTitle("Landlords");
        this.setSize(830, 620);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        container = this.getContentPane();
        container.setLayout(null);
        container.setBackground(Color.LIGHT_GRAY);
    }


    public void initView() {
        JButton claimBut = new JButton("Claim");
        claimBut.setBounds(320, 400, 90, 20);
        claimBut.addActionListener(this);
        claimBut.setVisible(false);
        landlord[0] = claimBut;
        container.add(claimBut);

        JButton noBut = new JButton("Not Claim");
        noBut.setBounds(420, 400, 90, 20);
        noBut.addActionListener(this);
        noBut.setVisible(false);
        landlord[1] = noBut;
        container.add(noBut);

        JButton outCardBut = new JButton("Publish");
        outCardBut.setBounds(320, 400, 90, 20);
        outCardBut.addActionListener(this);
        outCardBut.setVisible(false);
        publishCard[0] = outCardBut;
        container.add(outCardBut);

        JButton noCardBut = new JButton("Pass");
        noCardBut.setBounds(420, 400, 90, 20);
        noCardBut.addActionListener(this);
        noCardBut.setVisible(false);
        publishCard[1] = noCardBut;
        container.add(noCardBut);

        for (int i = 0; i < 3; i++) {
            time[i] = new JTextField("Countdown:");
            time[i].setEditable(false);
            time[i].setVisible(false);
            container.add(time[i]);
        }
        time[0].setBounds(140, 230, 60, 20);
        time[1].setBounds(374, 360, 60, 20);
        time[2].setBounds(620, 230, 60, 20);

        landlordIcon = new JLabel(new ImageIcon("image\\landlord.png"));
        landlordIcon.setVisible(false);
        landlordIcon.setSize(40, 40);
        container.add(landlordIcon);
    }

    public void initCard() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 13; j++) {
                if ((i == 5) && (j > 2)) {
                    break;
                } else {
                    Poker poker = new Poker(this, i + "-" + j, false);
                    poker.setLocation(350, 150);

                    pokerList.add(poker);
                    container.add(poker);
                }
            }
        }

        Collections.shuffle(pokerList);

        ArrayList<Poker> player0 = new ArrayList<>();
        ArrayList<Poker> player1 = new ArrayList<>();
        ArrayList<Poker> player2 = new ArrayList<>();

        for (int i = 0; i < pokerList.size(); i++) {
            Poker poker = pokerList.get(i);

            if (i <= 2) {
                Common.move(poker, poker.getLocation(), new Point(270 + (75 * i), 10));
                lordList.add(poker);
                continue;
            }
            if (i % 3 == 0) {
                Common.move(poker, poker.getLocation(), new Point(50, 60 + i * 5));
                player0.add(poker);
            } else if (i % 3 == 1) {
                Common.move(poker, poker.getLocation(), new Point(180 + i * 7, 450));
                player1.add(poker);
                poker.turnFront();
            } else {
                Common.move(poker, poker.getLocation(), new Point(700, 60 + i * 5));
                player2.add(poker);
            }
            playerList.add(player0);
            playerList.add(player1);
            playerList.add(player2);

            container.setComponentZOrder(poker, 0);
        }

        for (int i = 0; i < 3; i++) {
            Common.order(playerList.get(i));
            Common.rePosition(this, playerList.get(i), i);
        }
    }

    private void initGame() {
        for (int i = 0; i < 3; i++) {
            ArrayList<Poker> list = new ArrayList<>();
            currentList.add(list);
        }

        landlord[0].setVisible(true);
        landlord[1].setVisible(true);

        time[1].setVisible(true);
        po = new PlayerOperation(this, 30);
        po.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == landlord[0]) {
            time[1].setText("Claim");
            po.isRun = false;
        } else if (e.getSource() == landlord[1]) {
            time[1].setText("Not Claim");
            po.isRun = false;
        } else if (e.getSource() == publishCard[1]) {
            this.nextPlayer = true;
            currentList.get(1).clear();
            time[1].setText("Pass");
        } else if (e.getSource() == publishCard[0]) {
            ArrayList<Poker> c = new ArrayList<>();
            ArrayList<Poker> player2 = playerList.get(1);
            for (Poker poker : player2) {
                if (poker.isClicked()) {
                    c.add(poker);
                }
            }
            int flag = 0;
            if (time[0].getText().equals("pass") && time[2].getText().equals("pass")) {
                if (Common.judgeType(c) != PokerType.c0) {
                    flag = 1;
                }
            } else {
                flag = Common.checkCards(c, currentList, this);
            }

            if (flag == 1) {
                currentList.set(1, c);
                player2.removeAll(c);
                Point point = new Point();
                point.x = (770 / 2) - (c.size() + 1) * 15 / 2;
                point.y = 300;
                for (Poker poker : c) {
                    Common.move(poker, poker.getLocation(), point);
                    point.x += 15;
                }
                Common.rePosition(this, player2, 1);
                time[1].setVisible(false);
                this.nextPlayer = true;
            }
        }
    }
}
