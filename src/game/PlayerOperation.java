package game;

import domain.Poker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerOperation extends Thread {

    GameFrame gameFrame;

    boolean isRun = true;

    int i;

    public PlayerOperation(GameFrame m, int i) {
        this.gameFrame = m;
        this.i = i;
    }

    @Override
    public void run() {
        while (i > -1 && isRun) {
            gameFrame.time[1].setText("Cd:" + i--);
            sleep(1);
        }
        if (i == -1) {
            gameFrame.time[1].setText("Pass");
        }
        gameFrame.landlord[0].setVisible(false);
        gameFrame.landlord[1].setVisible(false);
        for (Poker poker2 : gameFrame.playerList.get(1)) {
            poker2.setCanClick(true);
        }

        if (gameFrame.time[1].getText().equals("Dibs")) {
            gameFrame.playerList.get(1).addAll(gameFrame.lordList);
            openlord(true);
            sleep(2);
            Common.order(gameFrame.playerList.get(1));
            Common.rePosition(gameFrame, gameFrame.playerList.get(1), 1);
            gameFrame.publishCard[1].setEnabled(false);
            setlord(1);
        } else {
            if (Common.getScore(gameFrame.playerList.get(0)) < Common.getScore(gameFrame.playerList.get(2))) {
                gameFrame.time[2].setText("Dibs");
                gameFrame.time[2].setVisible(true);
                setlord(2);
                openlord(true);
                sleep(3);
                gameFrame.playerList.get(2).addAll(gameFrame.lordList);
                Common.order(gameFrame.playerList.get(2));
                Common.rePosition(gameFrame, gameFrame.playerList.get(2), 2);
                openlord(false);
            } else {
                gameFrame.time[0].setText("Dibs");
                gameFrame.time[0].setVisible(true);
                setlord(0);
                openlord(true);
                sleep(3);
                gameFrame.playerList.get(0).addAll(gameFrame.lordList);
                Common.order(gameFrame.playerList.get(0));
                Common.rePosition(gameFrame, gameFrame.playerList.get(0), 0);
                openlord(false);
            }
        }
        gameFrame.landlord[0].setVisible(false);
        gameFrame.landlord[1].setVisible(false);
        turnOn(false);
        for (int i = 0; i < 3; i++) {
            gameFrame.time[i].setText("Pass");
            gameFrame.time[i].setVisible(false);
        }
        gameFrame.turn = gameFrame.landlordFlag;
        while (true) {

            if (gameFrame.turn == 1) {

                if (gameFrame.time[0].getText().equals("Pass") && gameFrame.time[2].getText().equals("Pass"))
                    gameFrame.publishCard[1].setEnabled(false);
                else {
                    gameFrame.publishCard[1].setEnabled(true);
                }
                turnOn(true);
                timeWait(30, 1);
                turnOn(false);
                gameFrame.turn = (gameFrame.turn + 1) % 3;
                if (win())
                    break;
            }
            if (gameFrame.turn == 0) {
                computer0();
                gameFrame.turn = (gameFrame.turn + 1) % 3;
                if (win())
                    break;
            }
            if (gameFrame.turn == 2) {
                computer2();
                gameFrame.turn = (gameFrame.turn + 1) % 3;
                if (win())
                    break;
            }
        }
    }

    public void sleep(int i) {
        try {
            Thread.sleep(i * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void openlord(boolean is) {
        for (int i = 0; i < 3; i++) {
            if (is)
                gameFrame.lordList.get(i).turnFront();
            else {
                gameFrame.lordList.get(i).turnRear();
            }
            gameFrame.lordList.get(i).setCanClick(true);
        }
    }

    public void setlord(int i) {
        Point point = new Point();
        if (i == 1) {
            point.x = 80;
            point.y = 430;
            gameFrame.landlordFlag = 1;
        }
        if (i == 0) {
            point.x = 80;
            point.y = 20;
            gameFrame.landlordFlag = 0;
        }
        if (i == 2) {
            point.x = 700;
            point.y = 20;
            gameFrame.landlordFlag = 2;
        }
        gameFrame.landlordIcon.setLocation(point);
        gameFrame.landlordIcon.setVisible(true);
    }

    public void turnOn(boolean flag) {
        gameFrame.publishCard[0].setVisible(flag);
        gameFrame.publishCard[1].setVisible(flag);
    }

    public void computer0() {
        timeWait(1, 0);
        ShowCard(0);

    }

    public void computer2() {
        timeWait(1, 2);
        ShowCard(2);

    }

    public void ShowCard(int role) {
        int[] orders = new int[]{4, 3, 2, 1, 5};
        Model model = Common.getModel(gameFrame.playerList.get(role), orders);
        ArrayList<String> list = new ArrayList<>();
        if (gameFrame.time[(role + 1) % 3].getText().equals("不要") && gameFrame.time[(role + 2) % 3].getText().equals("pass")) {
            if (model.a123.size() > 0) {
                list.add(model.a123.get(model.a123.size() - 1));
            } else if (model.a3.size() > 0) {
                if (model.a1.size() > 0) {
                    list.add(model.a1.get(model.a1.size() - 1));
                } else if (model.a2.size() > 0) {
                    list.add(model.a2.get(model.a2.size() - 1));
                }
                list.add(model.a3.get(model.a3.size() - 1));
            } else if (model.a112233.size() > 0) {
                list.add(model.a112233.get(model.a112233.size() - 1));
            } else if (model.a111222.size() > 0) {
                String[] name = model.a111222.get(0).split(",");

                if (name.length / 3 <= model.a1.size()) {
                    list.add(model.a111222.get(model.a111222.size() - 1));
                    for (int i = 0; i < name.length / 3; i++)
                        list.add(model.a1.get(i));
                } else if (name.length / 3 <= model.a2.size()) {
                    list.add(model.a111222.get(model.a111222.size() - 1));
                    for (int i = 0; i < name.length / 3; i++)
                        list.add(model.a2.get(i));
                }

            } else if (model.a2.size() > (model.a111222.size() * 2 + model.a3.size())) {
                list.add(model.a2.get(model.a2.size() - 1));
            } else if (model.a1.size() > (model.a111222.size() * 2 + model.a3.size())) {
                list.add(model.a1.get(model.a1.size() - 1));
            } else if (model.a4.size() > 0) {
                int sizea1 = model.a1.size();
                int sizea2 = model.a2.size();
                if (sizea1 >= 2) {
                    list.add(model.a1.get(sizea1 - 1));
                    list.add(model.a1.get(sizea1 - 2));
                    list.add(model.a4.get(0));

                } else if (sizea2 >= 2) {
                    list.add(model.a2.get(sizea1 - 1));
                    list.add(model.a2.get(sizea1 - 2));
                    list.add(model.a4.get(0));

                } else {
                    list.add(model.a4.get(0));

                }

            }
        } else {

            if (role != gameFrame.landlordFlag) {
                int f = 0;
                if (gameFrame.time[gameFrame.landlordFlag].getText().equals("pass")) {
                    f = 1;
                }
                if ((role + 1) % 3 == gameFrame.landlordFlag) {
                    if ((Common.judgeType(gameFrame.currentList.get((role + 2) % 3)) != PokerType.c1
                            || Common.judgeType(gameFrame.currentList.get((role + 2) % 3)) != PokerType.c2)
                            && gameFrame.currentList.get(gameFrame.landlordFlag).size() < 1)
                        f = 1;
                    if (gameFrame.currentList.get((role + 2) % 3).size() > 0
                            && Common.getValue(gameFrame.currentList.get((role + 2) % 3).get(0)) > 13)
                        f = 1;
                }
                if (f == 1) {
                    gameFrame.time[role].setVisible(true);
                    gameFrame.time[role].setText("pass");
                    return;
                }
            }

            int can = 0;
            if (role == gameFrame.landlordFlag) {
                if (gameFrame.playerList.get((role + 1) % 3).size() <= 5 || gameFrame.playerList.get((role + 2) % 3).size() <= 5)
                    can = 1;
            } else {
                if (gameFrame.playerList.get(gameFrame.landlordFlag).size() <= 5)
                    can = 1;
            }

            ArrayList<Poker> player;
            if (gameFrame.time[(role + 2) % 3].getText().equals("pass"))
                player = gameFrame.currentList.get((role + 1) % 3);
            else
                player = gameFrame.currentList.get((role + 2) % 3);

            PokerType cType = Common.judgeType(player);

            if (cType == PokerType.c1) {
                if (can == 1)
                    model = Common.getModel(gameFrame.playerList.get(role), new int[]{1, 4, 3, 2, 5});
                AI_1(model.a1, player, list, role);
            } else if (cType == PokerType.c2) {
                if (can == 1)
                    model = Common.getModel(gameFrame.playerList.get(role), new int[]{2, 4, 3, 5, 1});
                AI_1(model.a2, player, list, role);
            } else if (cType == PokerType.c3) {
                AI_1(model.a3, player, list, role);
            } else if (cType == PokerType.c4) {
                AI_1(model.a4, player, list, role);
            } else if (cType == PokerType.c31) {
                if (can == 1)
                    model = Common.getModel(gameFrame.playerList.get(role), new int[]{3, 1, 4, 2, 5});
                AI_2(model.a3, model.a1, player, list, role);
            } else if (cType == PokerType.c32) {
                if (can == 1)
                    model = Common.getModel(gameFrame.playerList.get(role), new int[]{3, 2, 4, 5, 1});
                AI_2(model.a3, model.a2, player, list, role);
            } else if (cType == PokerType.c411) {
                AI_5(model.a4, model.a1, player, list, role);
            } else if (cType == PokerType.c422) {
                AI_5(model.a4, model.a2, player, list, role);
            } else if (cType == PokerType.c123) {
                if (can == 1)
                    model = Common.getModel(gameFrame.playerList.get(role), new int[]{5, 3, 2, 4, 1});
                AI_3(model.a123, player, list, role);
            } else if (cType == PokerType.c112233) {
                if (can == 1)
                    model = Common.getModel(gameFrame.playerList.get(role), new int[]{2, 4, 3, 5, 1});
                AI_3(model.a112233, player, list, role);
            } else if (cType == PokerType.c11122234) {
                AI_4(model.a111222, model.a1, player, list, role);
            } else if (cType == PokerType.c1112223344) {
                AI_4(model.a111222, model.a2, player, list, role);
            }
            if (list.size() == 0 && can == 1) {
                int len4 = model.a4.size();
                if (len4 > 0)
                    list.add(model.a4.get(len4 - 1));
            }

        }

        gameFrame.currentList.get(role).clear();
        if (list.size() > 0) {
            Point point = new Point();
            if (role == 0)
                point.x = 200;
            if (role == 2)
                point.x = 550;
            if (role == 1) {
                point.x = (770 / 2) - (gameFrame.currentList.get(1).size() + 1) * 15 / 2;
                point.y = 300;
            }
            point.y = (400 / 2) - (list.size() + 1) * 15 / 2;
            ArrayList<Poker> temp = new ArrayList<>();
            for (int i = 0, len = list.size(); i < len; i++) {
                List<Poker> pokers = getCardByName(gameFrame.playerList.get(role), list.get(i));
                for (Poker poker : pokers) {
                    temp.add(poker);
                }
            }
            temp = Common.getOrder2(temp);
            for (Poker poker : temp) {
                Common.move(poker, poker.getLocation(), point);
                point.y += 15;
                gameFrame.container.setComponentZOrder(poker, 0);
                gameFrame.currentList.get(role).add(poker);
                gameFrame.playerList.get(role).remove(poker);
            }
            Common.rePosition(gameFrame, gameFrame.playerList.get(role), role);
        } else {
            gameFrame.time[role].setVisible(true);
            gameFrame.time[role].setText("pass");
        }
        for (Poker poker : gameFrame.currentList.get(role))
            poker.turnFront();
    }

    public List getCardByName(List<Poker> list, String n) {
        String[] name = n.split(",");
        ArrayList cardsList = new ArrayList();
        int j = 0;
        for (int i = 0, len = list.size(); i < len; i++) {
            if (j < name.length && list.get(i).getName().equals(name[j])) {
                cardsList.add(list.get(i));
                i = 0;
                j++;
            }
        }
        return cardsList;
    }

    public void AI_3(List<String> model, List<Poker> player, List<String> list, int role) {

        for (String value : model) {
            String[] s = value.split(",");
            if (s.length == player.size() && getValueInt(value) > Common.getValue(player.get(0))) {
                list.add(value);
                return;
            }
        }
    }

    public void AI_4(List<String> model1, List<String> model2, List<Poker> player, List<String> list, int role) {
        player = Common.getOrder2(player);
        int len1 = model1.size();
        int len2 = model2.size();

        if (len1 < 1 || len2 < 1)
            return;
        for (String value : model1) {
            String[] s = value.split(",");
            String[] s2 = model2.get(0).split(",");
            if ((s.length / 3 <= len2) && (s.length * (3 + s2.length) == player.size())
                    && getValueInt(value) > Common.getValue(player.get(0))) {
                list.add(value);
                for (int j = 1; j <= s.length / 3; j++)
                    list.add(model2.get(len2 - j));
            }
        }
    }

    public void AI_5(List<String> model1, List<String> model2, List<Poker> player, List<String> list, int role) {
        player = Common.getOrder2(player);
        int len1 = model1.size();
        int len2 = model2.size();

        if (len1 < 1 || len2 < 2)
            return;
        for (int i = 0; i < len1; i++) {
            if (getValueInt(model1.get(i)) > Common.getValue(player.get(0))) {
                list.add(model1.get(i));
                for (int j = 1; j <= 2; j++)
                    list.add(model2.get(len2 - j));
            }
        }
    }

    public void AI_1(List<String> model, List<Poker> player, List<String> list, int role) {

        for (int len = model.size(), i = len - 1; i >= 0; i--) {
            if (getValueInt(model.get(i)) > Common.getValue(player.get(0))) {
                list.add(model.get(i));
                break;
            }
        }

    }

    public void AI_2(List<String> model1, List<String> model2, List<Poker> player, List<String> list, int role) {
        player = Common.getOrder2(player);
        int len1 = model1.size();
        int len2 = model2.size();
        if (len1 > 0 && model1.get(0).length() < 10) {
            list.add(model1.get(0));
            return;
        }
        if (len1 < 1 || len2 < 1)
            return;
        for (int i = len1 - 1; i >= 0; i--) {
            if (getValueInt(model1.get(i)) > Common.getValue(player.get(0))) {
                list.add(model1.get(i));
                break;
            }
        }
        list.add(model2.get(len2 - 1));
        if (list.size() < 2)
            list.clear();
    }

    public void timeWait(int n, int player) {

        if (gameFrame.currentList.get(player).size() > 0)
            Common.hideCards(gameFrame.currentList.get(player));
        if (player == 1) {
            int i = n;

            while (gameFrame.nextPlayer == false && i >= 0) {
                gameFrame.time[player].setText("Cd:" + i);
                gameFrame.time[player].setVisible(true);
                sleep(1);
                i--;
            }
            if (i == -1 && player == 1) {

                ShowCard(1);
            }
            gameFrame.nextPlayer = false;
        } else {
            for (int i = n; i >= 0; i--) {
                sleep(1);
                gameFrame.time[player].setText("Cd:" + i);
                gameFrame.time[player].setVisible(true);
            }
        }
        gameFrame.time[player].setVisible(false);
    }

    public int getValueInt(String n) {
        String name[] = n.split(",");
        String s = name[0];
        int i = Integer.parseInt(s.substring(2, s.length()));
        if (s.substring(0, 1).equals("5"))
            i += 3;
        if (s.substring(2, s.length()).equals("1") || s.substring(2, s.length()).equals("2"))
            i += 13;
        return i;
    }

    public boolean win() {
        for (int i = 0; i < 3; i++) {
            if (gameFrame.playerList.get(i).size() == 0) {
                String s;
                if (i == 1) {
                    s = "You won!";
                } else {
                    s = "The player" + i + "won!";
                }
                for (int j = 0; j < gameFrame.playerList.get((i + 1) % 3).size(); j++)
                    gameFrame.playerList.get((i + 1) % 3).get(j).turnFront();
                for (int j = 0; j < gameFrame.playerList.get((i + 2) % 3).size(); j++)
                    gameFrame.playerList.get((i + 2) % 3).get(j).turnFront();
                JOptionPane.showMessageDialog(gameFrame, s);
                return true;
            }
        }
        return false;
    }
}
