package game;

import domain.User;
import util.CaptchaUtil;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LoginFrame extends JFrame implements MouseListener {
    public Container container = null;
    static ArrayList<User> allUsers = new ArrayList<>();

    static {
        allUsers.add(new User("player1", "player1"));
        allUsers.add(new User("player2", "player2"));
    }


    JButton login = new JButton();
    JButton register = new JButton();
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JTextField captcha = new JTextField();
    JLabel rightCaptcha = new JLabel();


    public LoginFrame() {
        initFrame();
        initView();
        this.setVisible(true);
    }

    public void initFrame() {
        this.setSize(633, 423);
        this.setTitle("Landlords Login");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setLayout(null);
        container = this.getContentPane();
    }

    public void initView() {
        Font usernameFont = new Font("Arial", Font.BOLD, 14);
        JLabel usernameText = new JLabel("username");
        usernameText.setForeground(Color.white);
        usernameText.setFont(usernameFont);
        usernameText.setBounds(140, 55, 70, 22);
        container.add(usernameText);

        username.setBounds(223, 46, 200, 30);
        container.add(username);

        JLabel passwordText = new JLabel("password");
        Font passwordFont = new Font("Arial", Font.BOLD, 14);
        passwordText.setForeground(Color.white);
        passwordText.setFont(passwordFont);
        passwordText.setBounds(140, 95, 70, 22);
        container.add(passwordText);

        password.setBounds(223, 87, 200, 30);
        container.add(password);

        JLabel codeText = new JLabel("captcha");
        Font codeFont = new Font("Arial", Font.BOLD, 14);
        codeText.setForeground(Color.white);
        codeText.setFont(codeFont);
        codeText.setBounds(215, 142, 55, 22);
        container.add(codeText);

        captcha.setBounds(291, 133, 100, 30);
        container.add(captcha);

        String codeStr = CaptchaUtil.getCaptcha();
        Font rightCodeFont = new Font("Arial", Font.BOLD, 13);
        rightCaptcha.setForeground(Color.RED);
        rightCaptcha.setFont(rightCodeFont);
        rightCaptcha.setText(codeStr);
        rightCaptcha.addMouseListener(this);
        rightCaptcha.setBounds(400, 133, 100, 30);
        container.add(rightCaptcha);

        login.setBounds(123, 310, 128, 47);
        login.setIcon(new ImageIcon("image\\login\\login.png"));
        login.setBorderPainted(false);
        login.setContentAreaFilled(false);
        login.addMouseListener(this);
        container.add(login);

        register.setBounds(256, 310, 128, 47);
        register.setIcon(new ImageIcon("image\\login\\signin.png"));
        register.setBorderPainted(false);
        register.setContentAreaFilled(false);
        register.addMouseListener(this);
        container.add(register);


        JLabel background = new JLabel(new ImageIcon("image\\login\\background.png"));
        background.setBounds(0, 0, 633, 423);
        container.add(background);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == login) {
            String usernameInput = username.getText();
            String passwordInput = password.getText();
            String captchaInput = captcha.getText();

            if (captchaInput.length() == 0) {
                showJDialog("Captcha could not be empty!");
                return;
            }

            if (usernameInput.length() == 0 || passwordInput.length() == 0) {
                showJDialog("Username or password could not be empty!");
                return;
            }

            if (!captchaInput.equalsIgnoreCase(rightCaptcha.getText())) {
                showJDialog("Captcha is wrong!");
                return;
            }

            User userInfo = new User(usernameInput, passwordInput);
            if (allUsers.contains(userInfo)) {
                this.setVisible(false);
                new GameFrame();
            } else {
                showJDialog("Username or password is wrong!");
            }
        } else if (e.getSource() == register) {
            System.out.println("register");
        } else if (e.getSource() == rightCaptcha) {
            String captcha = CaptchaUtil.getCaptcha();
            rightCaptcha.setText(captcha);
        }
    }

    public void showJDialog(String content) {
        JDialog jDialog = new JDialog();
        jDialog.setSize(400, 150);
        jDialog.setAlwaysOnTop(true);
        jDialog.setLocationRelativeTo(null);
        jDialog.setModal(true);

        JLabel warning = new JLabel(content);
        warning.setBounds(30, 0, 400, 150);
        jDialog.getContentPane().add(warning);
        jDialog.setVisible(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == login) {
            login.setIcon(new ImageIcon("image\\login\\login_on.png"));
        } else if (e.getSource() == register) {
            register.setIcon(new ImageIcon("image\\login\\signin_on.png"));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == login) {
            login.setIcon(new ImageIcon("image\\login\\login.png"));
        } else if (e.getSource() == register) {
            register.setIcon(new ImageIcon("image\\login\\signin.png"));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
