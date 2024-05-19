import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Main extends JFrame implements ActionListener{
    JLabel welcome, loginText, userLabel, passLabel, groupName, contactLabel;
    JPanel header, footer;
    JButton loginButton;
    JTextField usernameField;
    JPasswordField passwordField;
    ArrayList<String> userInfo = info();
    static Date dateAndTime = new Date();
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
    static String date = dateFormatter.format(dateAndTime);
    static String time = timeFormatter.format(dateAndTime);

    Main() {
        //Labels
        welcome = new JLabel();
        welcome.setText("CHECK OUT SYSTEM");
        welcome.setFont(new Font("Roboto", Font.BOLD, 20));
        welcome.setForeground(Color.white);
        welcome.setVerticalAlignment(JLabel.CENTER);
        welcome.setHorizontalAlignment(JLabel.CENTER);

        loginText = new JLabel();
        loginText.setText("Login");
        loginText.setBounds(360, 220, 110, 100);
        loginText.setFont(new Font("Roboto", Font.BOLD, 20));

        userLabel = new JLabel();
        userLabel.setText("Username: ");
        userLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        userLabel.setBounds(110, 320, 120, 100);

        passLabel = new JLabel();
        passLabel.setText("Password: ");
        passLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        passLabel.setBounds(110, 400, 300, 100);

        groupName = new JLabel();
        groupName.setText("Created by: Open Circle Group");
        groupName.setForeground(Color.white);
        groupName.setFont(new Font("Roboto", Font.PLAIN, 15));
        groupName.setVerticalAlignment(JLabel.CENTER);
        groupName.setHorizontalAlignment(JLabel.CENTER);

        contactLabel = new JLabel();
        contactLabel.setText("Forgot username/password? Contact the Administrator.");
        contactLabel.setFont(new Font("Roboto", Font.ITALIC, 10));
        contactLabel.setBounds(275, 520, 300, 50);


        //Buttons
        loginButton = new JButton("Login");
        loginButton.setBounds(355, 490, 70, 25);
        loginButton.addActionListener(this);
        loginButton.setFocusable(false);
        

        //Fields
        usernameField = new JTextField();
        usernameField.setFont(new Font("Roboto", Font.ROMAN_BASELINE, 20));
        usernameField.setBounds(225, 355, 350, 30);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Roboto", Font.ROMAN_BASELINE, 20));
        passwordField.setBounds(225, 435, 350, 30);
        
        //Panels
        header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(new Color(179, 200, 207));
        header.setBounds(0, 0, 800, 80);

        footer = new JPanel();
        footer.setLayout(new BorderLayout());
        footer.setBackground(new Color(179, 200, 207));
        footer.setBounds(0, 792, 800, 70);

        //Add others to Panel
        header.add(welcome, BorderLayout.CENTER);
        footer.add(groupName, BorderLayout.CENTER);
        
        //Add others to Frame
        add(header);
        add(loginText);
        add(userLabel);
        add(passLabel);
        add(usernameField);
        add(passwordField);
        add(loginButton);
        add(footer);
        add(contactLabel);

        //Customize Frame
        setSize(800,900);
        setLayout(null);
        setTitle("Check Out Sytem");
        getContentPane().setBackground(new Color(241, 238, 220));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        ImageIcon icon = new ImageIcon("./images/cart.png");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Commands
    @Override
    public void actionPerformed(ActionEvent e){
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "Login":
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());  

                for (int i = 0 ; i < userInfo.size(); i++) {
                    boolean even = i % 2 == 0;
                    if (username.equals(userInfo.get(0)) && password.equals(userInfo.get(1))) {
                        JOptionPane.showMessageDialog(this, "Login succefully as Admin");
                        dispose();
                        new Admin();
                        break;
                    }
                    else if (even && username.equals(userInfo.get(i)) && password.equals(userInfo.get(i+1))){
                        addRecords(username);
                        JOptionPane.showMessageDialog(this, "Login Succefully!");
                        dispose();
                        CheckOutSystem.clear("./files/buyer.txt");
                        new CheckOutSystem();
                        break;
                    }
                }
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Any field/s cannot be empty.");
                }
                else if (!userInfo.contains(username) || !userInfo.contains(password)) {
                    JOptionPane.showMessageDialog(this, "Invalid user or password.");
                }
            default:
                break;
        }
    }
    static ArrayList<String> info(){
        ArrayList<String> userInfo = new ArrayList<>();
        try {
            BufferedReader userData = new BufferedReader(new FileReader("./files/info.txt"));
            String line;
            while ((line = userData.readLine()) != null) {
                String[] parts = line.split(", ");
                userInfo.add(parts[0]);
                userInfo.add(parts[1]);
            }
            userData.close();
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return userInfo;
    }

    //Add to records
    static void addRecords(String username){
        ArrayList<String> userAndNAme = new ArrayList<>();
        try {
            BufferedReader readAccounts = new BufferedReader(new FileReader("./files/info.txt"));
            String account;
            while((account = readAccounts.readLine()) != null) {
                String[] parts = account.split(", ");
                userAndNAme.add(parts[0]);
                userAndNAme.add(parts[2]);
            }
            readAccounts.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (int i = 0 ; i < userAndNAme.size() ; i++) {
            if (username.equals(userAndNAme.get(i))) {
                String name = userAndNAme.get(i+1);
                try {
                    BufferedWriter writeRecord = new BufferedWriter(new FileWriter("./files/records.txt", true));
                    writeRecord.append(name + ", " + date + ", " + time + ", ");
                    writeRecord.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                break;
            }
        }
        
    }

    public static void main(String[] args) {
        new Main();
    }
}