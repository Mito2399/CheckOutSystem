import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class Account extends JFrame implements ActionListener {
    JPanel header, footer, sidePanel, userPanel, recordsPanel;
    JScrollPane scrollUser, scrollRecords;
    DefaultTableModel model, recordsModel;
    DefaultTableCellRenderer centerRenderer;
    JLabel welcome, groupName, users, records;
    JButton addButton, changeButton, hideButton, unhideButton, deleteButton, removeRecord, clearRecord, adminButton, loginButton;
    int selected, recordSelected;
    String[] userLabel, recordsLabel;
    String recordsFile = "./files/records.txt";
    static Object [][] userData, recordsData;
    static String userFiles = "./files/info.txt";
    static JTable userTable, recordsTable;


    Account () {
        //Data
        userData = getData(userFiles);
        userLabel = new String[] {"Username", "Password", "Name", "Age", "Address"};

        recordsData = getRecords(recordsFile);
        recordsLabel = new String[] {"Name", "Date", "Time-in", "Time-out"};

        //Model
        model = new DefaultTableModel(userData, userLabel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recordsModel = new DefaultTableModel(recordsData, recordsLabel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //Table
        userTable = new JTable(model);
        userTable.setPreferredScrollableViewportSize(new Dimension(600, 300));

        recordsTable = new JTable(recordsModel);
        recordsTable.setPreferredScrollableViewportSize(new Dimension(600, 300));

        //ScrollPanel
        scrollUser = new JScrollPane(userTable);
        scrollRecords = new JScrollPane(recordsTable);

        //DefaultTableCellRenderer
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );

        for (int tableColumns = 0 ; tableColumns < userTable.getColumnCount() ; tableColumns++) {
            userTable.getColumnModel().getColumn(tableColumns).setCellRenderer( centerRenderer );
        }

        for (int tableColumns = 0 ; tableColumns < recordsTable.getColumnCount() ; tableColumns++) {
            recordsTable.getColumnModel().getColumn(tableColumns).setCellRenderer( centerRenderer );
        }

        //Labels
        welcome = new JLabel();
        welcome.setText("ACCOUNT MANAGEMENT");
        welcome.setFont(new Font("Roboto", Font.BOLD, 20));
        welcome.setForeground(Color.white);
        welcome.setVerticalAlignment(JLabel.CENTER);
        welcome.setHorizontalAlignment(JLabel.CENTER);

        groupName = new JLabel();
        groupName.setText("Created by: Open Circle Group");
        groupName.setForeground(Color.white);
        groupName.setFont(new Font("Roboto", Font.PLAIN, 15));
        groupName.setVerticalAlignment(JLabel.CENTER);
        groupName.setHorizontalAlignment(JLabel.CENTER);

        users = new JLabel();
        users.setText("ACCOUNTS");
        users.setBounds(410, 100, 150, 20);
        users.setForeground(Color.white);
        users.setFont(new Font("Roboto", Font.BOLD, 20));

        records = new JLabel();
        records.setText("RECORDS");
        records.setBounds(420, 450, 100, 20);
        records.setForeground(Color.white);
        records.setFont(new Font("Roboto", Font.BOLD, 20));

        //Border
        Border whiteLine = BorderFactory.createLineBorder(Color.white);

        //Panels
        header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(new Color(179, 200, 207));
        header.setBounds(0, 0, 800, 80);

        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBackground(new Color(49, 54, 63));
        sidePanel.setBounds(0, 75, 150, 820);
        sidePanel.setBorder(whiteLine);

        userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userPanel.setBounds(170, 130, 600, 300);
        userPanel.setBackground(Color.white);

        recordsPanel = new JPanel();
        recordsPanel.setLayout(new BorderLayout());
        recordsPanel.setBounds(170, 480, 600, 300);
        recordsPanel.setBackground(Color.white);

        footer = new JPanel();
        footer.setLayout(new BorderLayout());
        footer.setBackground(new Color(179, 200, 207));
        footer.setBounds(0, 792, 800, 70);

        //Buttons
        addButton = new JButton("Add Account");
        addButton.setBounds(5, 100, 140, 50);
        addButton.addActionListener(this);
        addButton.setFocusable(false);

        deleteButton = new JButton("Delete Account");
        deleteButton.setBounds(5, 170, 140, 50);
        deleteButton.addActionListener(this);
        deleteButton.setFocusable(false);

        changeButton = new JButton("Change Password");
        changeButton.setBounds(5, 240, 140, 50);
        changeButton.addActionListener(this);
        changeButton.setFocusable(false);

        unhideButton = new JButton("Unhide Password");
        unhideButton.setBounds(5, 310, 140, 50);
        unhideButton.addActionListener(this);
        unhideButton.setFocusable(false);

        hideButton = new JButton("Hide Password");
        hideButton.setBounds(5, 380, 140, 50);
        hideButton.addActionListener(this);
        hideButton.setFocusable(false);

        removeRecord = new JButton("Remove Record");
        removeRecord.setBounds(5, 480, 140, 50);
        removeRecord.addActionListener(this);
        removeRecord.setFocusable(false);

        clearRecord = new JButton("Clear Record");
        clearRecord.setBounds(5, 550, 140, 50);
        clearRecord.addActionListener(this);
        clearRecord.setFocusable(false);

        adminButton = new JButton("Back to Admin");
        adminButton.setBounds(5, 620, 140, 50);
        adminButton.addActionListener(this);
        adminButton.setFocusable(false);

        loginButton = new JButton("Back to Login");
        loginButton.setBounds(5, 690, 140, 50);
        loginButton.addActionListener(this);
        loginButton.setFocusable(false);

        //Function
        securePassword();


        //Add others to Panel
        header.add(welcome);
        footer.add(groupName);
        userPanel.add(scrollUser);
        recordsPanel.add(scrollRecords);

        //Add others to Frame
        add(header);
        add(footer);
        add(users);
        add(records);
        add(userPanel);
        add(recordsPanel);
        add(addButton);
        add(deleteButton);
        add(changeButton);
        add(unhideButton);
        add(hideButton);
        add(removeRecord);
        add(clearRecord);
        add(adminButton);
        add(loginButton);
        add(sidePanel);
        


        //Customize Frame
        setSize(800,900);
        setLayout(null);
        setTitle("Check Out Sytem");
        getContentPane().setBackground(new Color(34, 40, 49));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        ImageIcon icon = new ImageIcon("./images/cart.png");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Commands
    @Override
    public void actionPerformed(ActionEvent e ){
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "Add Account":
                addAccount();
                dispose();
                new Account();
                break;
            case "Delete Account":
                selected = userTable.getSelectedRow();
                if (selected < 0) {
                    JOptionPane.showMessageDialog(this, "Select the account you want to delete.", "Delete Acccount", JOptionPane.INFORMATION_MESSAGE);
                }
                else if (selected == 0) {
                    JOptionPane.showMessageDialog(this, "The admin account cannot be deleted.", "Admin Account", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    deleteAccount(selected);
                    dispose();
                    new Account();
                }
                break;
            case "Change Password":
                selected = userTable.getSelectedRow();
                if (selected < 0) {
                    JOptionPane.showMessageDialog(this, "Please select a row you want to change.", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    changePassword(selected);
                    dispose();
                    new Account();
                }
                break;
            case "Unhide Password":
                unhidePassword();
                break;
            case "Hide Password":
                securePassword();
                break;
            case "Remove Record":
                recordSelected = recordsTable.getSelectedRow();
                Admin.removeProduct(recordSelected, recordsFile);
                dispose();
                new Account();
                break;
            case "Clear Record":
                clear(recordsFile);
                dispose();
                new Account();
                break;
            case "Back to Admin":
                dispose();
                new Admin();
                break;
            case "Back to Login":
                dispose();
                new Main();
                break;
            default:
                break;
        }
    }

    //Data
    static Object[][] getData(String fileLocation){
        ArrayList<Object[]> dataList = new ArrayList<>();
        try {
            BufferedReader accounts = new BufferedReader(new FileReader(fileLocation));
            String account;
            while ((account = accounts.readLine()) != null){
                String[] parts = account.split(",");
                if (parts.length == 5) {
                    dataList.add(new Object[]{parts[0], parts[1], parts[2], parts[3], parts[4]});
                }

            }
            accounts.close();
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
        Object[][] data = dataList.toArray(new Object[0][]);
        return data;
    }

    //Records
    static Object[][] getRecords(String fileLocation){
        ArrayList<Object[]> dataList = new ArrayList<>();
        try {
            BufferedReader records= new BufferedReader(new FileReader(fileLocation));
            String record;
            while ((record = records.readLine()) != null){
                String[] parts = record.split(",");
                if (parts.length == 3) {
                    dataList.add(new Object[]{parts[0], parts[1], parts[2]});
                }
                else if (parts.length == 4) {
                    dataList.add(new Object[]{parts[0], parts[1], parts[2], parts[3]});
                }
            }
            records.close();
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
        Object[][] data = dataList.toArray(new Object[0][]);
        return data;
    }

    //Hide Password
    static void securePassword() {
        String hide = "";
        for (int row = 0 ; row < userTable.getRowCount() ; row++) {
            String takePassword = userTable.getValueAt(row, 1).toString();
            for (int i = 1 ; i <= takePassword.length() ; i++) {
                hide += "*";
            }
            userTable.setValueAt(hide, row, 1);
            hide = "";
        }
    }

    //Unhide Password
    static void unhidePassword() {
        while (true) {
            String securityQuestion = JOptionPane.showInputDialog(null, "Who is The Master Coder Of the Universe?\nNote: Case sensitive.", "Security Question", JOptionPane.INFORMATION_MESSAGE);
            if (securityQuestion == null){
                break;
            }
            else if (securityQuestion.isEmpty() || !securityQuestion.equals("Nic Ureta")){
                JOptionPane.showMessageDialog(null, "Wrong answer!", "Security Question", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                for (int row = 0 ; row < userTable.getRowCount() ; row++){
                    String unhide = userData[row][1].toString().trim();
                    userTable.setValueAt(unhide, row, 1);
                }
                JOptionPane.showMessageDialog(null, "I'm glad you know!", "Success", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }


    //Add Account
    static void addAccount() {
        String username, password, name, Sage, confirmPassword;
        String address = "";
        int age = 0;
        boolean running = true;

        while (running) {
            boolean dupAcc = false;
            name = JOptionPane.showInputDialog(null, "Enter name", "Add Account", JOptionPane.INFORMATION_MESSAGE);
            if (name == null) {
                break;
            }
            else if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a name.", "Add Account", JOptionPane.INFORMATION_MESSAGE);
            }

            try {
                BufferedReader readAccount = new BufferedReader(new FileReader(userFiles));
                String account;
                while((account = readAccount.readLine()) != null) {
                    String[] parts = account.split(", ");
                    if (name.equalsIgnoreCase(parts[2])) {
                        JOptionPane.showMessageDialog(null, name + " already has an account.", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                        dupAcc = true;
                    }
                }
                readAccount.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (!dupAcc) {
                boolean enteringAge = true;
                boolean enteringAddress = true;
                boolean enteringUsername = true;

                while (enteringAge) {
                    Sage = JOptionPane.showInputDialog(null, "Enter user age", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                    if (Sage == null) {
                        enteringAge = false;
                        running = false;
                    }
                    else if (Sage.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Enter valid age.", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                        continue;
                    }
                    try {
                        age = Integer.parseInt(Sage);
                        if (age < 18) {
                            JOptionPane.showMessageDialog(null, "Too young.", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else if (age >= 60) {
                            JOptionPane.showMessageDialog(null, "Too Old", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            enteringAge = false;
                        }
                    }
                    catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Enter valid age.", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                while (enteringAddress) {
                    String Saddress = JOptionPane.showInputDialog(null, "Enter user address", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                    if (Saddress == null){
                        enteringAddress = false;
                        running = false;
                    }
                    else if (Saddress.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter an address", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                        continue;
                    }
                    else {
                        address = Saddress.substring(0, 1).toUpperCase() + Saddress.substring(1);
                        enteringAddress = false;
                    }
                }
                while (enteringUsername) {
                    boolean dupUser = false;
                    boolean enteringPassword = true;
                    boolean enteringConfirmPassword = true;

                    username = JOptionPane.showInputDialog(null, "Enter username", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                    if (username == null) {
                        enteringUsername = false;
                        break;
                    }
                    else if (username.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter username", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                        continue;
                    }
                    
                    try {
                        BufferedReader readUsername = new BufferedReader(new FileReader(userFiles));
                        String accountUser;
                        while((accountUser = readUsername.readLine()) != null) {
                            String[] parts = accountUser.split(", ");
                            if (name.equalsIgnoreCase(parts[0])) {
                                JOptionPane.showMessageDialog(null, username + " is already taken.", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                                dupUser = true;
                            }
                        }
                        readUsername.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (!dupUser) {
                        while (enteringPassword) {
                            password = JOptionPane.showInputDialog(null, "Enter user password", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                            if (password == null) {
                                enteringPassword = false;
                                enteringUsername = false;
                                running = false;
                            }
                            else if (password.isEmpty()){
                                JOptionPane.showMessageDialog(null, "Please enter a password", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                            }
                            else if (password.length() < 8 || password.length() > 15) {
                                JOptionPane.showMessageDialog(null, "Password must have 8 characters and not more than 15 characters.", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                            }
                            else {
                                while (enteringConfirmPassword) {
                                    confirmPassword = JOptionPane.showInputDialog(null, "Confirm Password", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                                    if (confirmPassword == null) {
                                        enteringConfirmPassword = false;
                                        enteringPassword = false;
                                        enteringUsername = false;
                                        break;
                                    }
                                    else if (confirmPassword.isEmpty()) {
                                        JOptionPane.showMessageDialog(null, "Please enter a confirmation password", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                    else if (!confirmPassword.equals(password)) {
                                        JOptionPane.showMessageDialog(null, "Password didn't match.", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                    else {
                                        try {
                                            BufferedWriter writeAccount = new BufferedWriter(new FileWriter(userFiles, true));
                                            writeAccount.append(username + ", " + password + ", " + name + ", " + age + ", " + address + "\n");
                                            writeAccount.close();
                                        }
                                        catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        JOptionPane.showMessageDialog(null, "Account Added", "Add Account", JOptionPane.INFORMATION_MESSAGE);
                                        enteringConfirmPassword = false;
                                        enteringPassword = false;
                                        enteringUsername = false;
                                        running = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Delete Account
    static void deleteAccount(int selected) {
        String name = userTable.getValueAt(selected, 2).toString();
        int yesNo = JOptionPane.showConfirmDialog(null, String.format("Are you sure you want to delete %s account?", name), "Confirmation", JOptionPane.YES_NO_OPTION);
        if (yesNo == JOptionPane.YES_OPTION) {
            Admin.removeProduct(selected, userFiles);
            JOptionPane.showMessageDialog(null, "Account Deleted", "Delete Acccount", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //Change Password
    static void changePassword(int selected) {
        ArrayList<String> info = new ArrayList<>();
        String updatedPassword = "";
        String oldPassword;
        String newPassword;
        String confirm;
        String name = userTable.getValueAt(selected, 2).toString();
        String selectedPassword = userData[selected][1].toString().trim();

        try {
            BufferedReader infos = new BufferedReader(new FileReader(userFiles));
            String data;
            while ((data = infos.readLine()) != null) {
                info.add(data);
            }
            infos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int yesNo = JOptionPane.showConfirmDialog(null, String.format("You want to change %s password?", name ), "Confirmation", JOptionPane.YES_NO_OPTION);
        if (yesNo == JOptionPane.YES_NO_OPTION) {
            boolean using = true;
            while (using) {
                oldPassword = JOptionPane.showInputDialog(null, "Enter old password", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                if (oldPassword == null){
                    using = false;
                }
                else if (oldPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a password.", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                }
                else if (!oldPassword.equals(selectedPassword)) {
                    JOptionPane.showMessageDialog(null, "Wrong password.", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    boolean newP = true;
                    while (newP) {
                        newPassword = JOptionPane.showInputDialog(null, "Enter new password", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                        if (newPassword == null) {
                            using = false;
                        }
                        else if (newPassword.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter a password.", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else if (newPassword.length() < 8 || newPassword.length() > 15) {
                            JOptionPane.showMessageDialog(null, "Password must have 8 characters and not more than 15 characters.", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            do {
                                confirm = JOptionPane.showInputDialog(null, "Confirm new password", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                                if (confirm == null) {
                                    using = false;
                                    break; 
                                }
                                else if (confirm.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "Cannot be empty.", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                                }
                                else if (!confirm.equals(newPassword)) {
                                    JOptionPane.showMessageDialog(null, "Password didn't match.", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                                }
                                else{
                                    for (String item : info) {
                                        String[] itemParts = item.split(", ");
                                        updatedPassword = itemParts[0] + ", " + newPassword + ", " + itemParts[2] + ", " + itemParts[3] + ", " + itemParts[4];
                                    }
                                    info.set(selected, updatedPassword);
                                    try {
                                        BufferedWriter writeUpdatedPassword = new BufferedWriter(new FileWriter(userFiles));
                                        for (String item : info) {
                                            writeUpdatedPassword.append(item + "\n");
                                        }
                                        writeUpdatedPassword.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                    securePassword();
                                    JOptionPane.showMessageDialog(null, "Password Changed", "Change Password", JOptionPane.INFORMATION_MESSAGE);
                                    using = false;
                                    newP = false;
                                }
                            }
                            while (!confirm.equals(newPassword));
                        }

                    }
                }
            }
        }
    }

    //Clear
    static void clear(String fileName){
        try {
            FileWriter fileWriter = new FileWriter(fileName, false);
            fileWriter.write("");
            fileWriter.close();
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null, "An error occurred while clearing the file.");
            e.printStackTrace();
        }
    }
}

