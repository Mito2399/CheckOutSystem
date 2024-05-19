import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.awt.*;
import java.text.SimpleDateFormat;



public class CheckOutSystem extends JFrame implements ActionListener, MouseListener, FocusListener{
    JPanel header, receiptPanel, dataHolder, productHold;
    JTextField cashText;
    JButton sortButton, payButton, clearButton, backToLogin, printButton, logoutButton; 
    JLabel headerMessage, totalLabel, cashLabel, balanceLabel, infoTotalLabel, infoBalanceLabel, 
    storeProduct, buyerProduct, storeReceipt, Ritems, Rqty, Rprice;
    DefaultTableCellRenderer centerRenderer;
    JScrollPane scrollPane, buyerPane, receiptPane;
    ImageIcon backIcon, logoutIcon;
    String[] tableLabel, selectedRows;
    Object[][] newData;
    Object[][] buyerData;
    static JTextArea receipTextArea;
    static DefaultTableModel model, buyerModel;
    static JTable table, buyerTable;
    static double total;
    static double balance = 0.00;
    static double cash = 0.00;
    static String dataLoc = "./files/products.txt";
    static String buyerDataLoc = "./files/buyer.txt";
    static String balanceTotal = "./files/balanceAndTotal.txt";
    static Date dateAndTime = new Date();
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
    static String date = dateFormatter.format(dateAndTime);
    static String time = timeFormatter.format(dateAndTime);
   

    CheckOutSystem() {
        //Data 
        tableLabel = new String[] {"Product ID", "Name", "Price", "Quantity"};
        newData = Admin.getData(dataLoc);
        buyerData = Admin.getData(buyerDataLoc);


        //Model
        model = new DefaultTableModel(newData,tableLabel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        buyerModel = new DefaultTableModel(buyerData,tableLabel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //Table
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));
        table.addMouseListener(this);
        buyerTable = new JTable(buyerModel);
        buyerTable.setPreferredScrollableViewportSize(new Dimension(400,200));

        //DefaultTableCellRenderer
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        center(table, centerRenderer);
        center(buyerTable, centerRenderer);

        //ScrollPane
        scrollPane = new JScrollPane(table);
        buyerPane = new JScrollPane(buyerTable);
        receiptPane = new JScrollPane(receipTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //Text Area
        receipTextArea = new JTextArea();
        receipTextArea.setFont(new Font("Roboto", Font.BOLD, 15));
        receipTextArea.setEditable(false);
        receipTextArea.setLineWrap(true);

        //Label
        headerMessage = new JLabel();
        headerMessage.setText("CHECK OUT SYSTEM");
        headerMessage.setFont(new Font("Roboto", Font.BOLD, 20));
        headerMessage.setForeground(Color.white);
        headerMessage.setVerticalAlignment(JLabel.CENTER);
        headerMessage.setHorizontalAlignment(JLabel.CENTER);

        storeProduct = new JLabel("Store's Product/s");
        storeProduct.setBounds(150,-45, 400, 250);
        storeProduct.setForeground(Color.WHITE);
        storeProduct.setFont(new Font("Roboto", Font.BOLD, 20));

        storeReceipt = new JLabel("Receipts");
        storeReceipt.setBounds(580,-45, 400, 250);
        storeReceipt.setForeground(Color.WHITE);
        storeReceipt.setFont(new Font("Roboto", Font.BOLD, 20));

        buyerProduct = new JLabel("Buyer Product/s");
        buyerProduct.setBounds(150,230, 400, 250);
        buyerProduct.setForeground(Color.white);
        buyerProduct.setFont(new Font("Roboto", Font.BOLD, 20));

        totalLabel = new JLabel("Total: ");
        totalLabel.setBounds(15,520, 400, 250);
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Roboto", Font.BOLD, 20));

        infoTotalLabel = new JLabel(String.format("%,.2f", total));
        infoTotalLabel.setBounds(150,520, 400, 250);
        infoTotalLabel.setForeground(Color.WHITE);
        infoTotalLabel.setFont(new Font("Roboto", Font.ROMAN_BASELINE, 20));

        cashLabel = new JLabel("Cash: ");
        cashLabel.setBounds(15,550, 400, 250);
        cashLabel.setForeground(Color.WHITE);
        cashLabel.setFont(new Font("Roboto", Font.BOLD, 20));

        balanceLabel = new JLabel("Balance: ");
        balanceLabel.setBounds(15,580, 400, 250);
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font("Roboto", Font.BOLD, 20));

        infoBalanceLabel = new JLabel(String.format("%,.2f", balance));
        infoBalanceLabel.setBounds(150,580, 400, 250);
        infoBalanceLabel.setForeground(Color.WHITE);
        infoBalanceLabel.setFont(new Font("Roboto", Font.ROMAN_BASELINE, 20));

        //Icons
        backIcon = new ImageIcon(new ImageIcon("./images/back.png").getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));
        logoutIcon = new ImageIcon(new ImageIcon("./images/turn-off.png").getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));

        //Panels
        header = new JPanel();
        header.setLayout(new BorderLayout());
        header.add(headerMessage, BorderLayout.CENTER);
        header.setBackground(new Color(179, 200, 207));
        header.setBounds(0, 0, 800, 60);

        dataHolder = new JPanel();
        dataHolder.setBounds(15, 100, 420, 240);
        dataHolder.setBackground(Color.white);

        productHold = new JPanel();
        productHold.setBounds(15, 375, 420, 240);
        productHold.setBackground(Color.white);

        receiptPanel = new JPanel();
        receiptPanel.setLayout(new BorderLayout());
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setBounds(470, 100, 300, 515);

        //Buttons
        sortButton = new JButton("Sort");
        sortButton.setBounds(280, 620, 80, 20);
        sortButton.addActionListener(this);
        sortButton.setFocusable(false);

        payButton = new JButton("Pay");
        payButton.setBounds(470, 640, 150, 50);
        payButton.addActionListener(this);
        payButton.setFocusable(false);

        printButton = new JButton("Print");
        printButton.setBounds(620, 640, 150, 50);
        printButton.addActionListener(this);
        printButton.setFocusable(false);

        clearButton = new JButton("Clear");
        clearButton.setBounds(355, 620, 80, 20);
        clearButton.addActionListener(this);
        clearButton.setFocusable(false);

        logoutButton = new JButton();
        logoutButton.setLayout(new BorderLayout());
        logoutButton.setIcon(logoutIcon);
        logoutButton.setBounds(15, 730, 25, 25);
        logoutButton.addActionListener(this);
        logoutButton.setFocusable(false);

        backToLogin = new JButton();
        backToLogin.setLayout(new BorderLayout());
        backToLogin.setIcon(backIcon);
        backToLogin.setBounds(45, 730, 25, 25);
        backToLogin.addActionListener(this);
        backToLogin.setFocusable(false);


        //TextField
        cashText = new JTextField(String.format("%,.2f",cash));
        cashText.setFont(new Font("Roboto", Font.ROMAN_BASELINE, 15));
        cashText.setBounds(150,660, 100, 25);
        cashText.addFocusListener(this);

        //Add others to Panel
        header.add(headerMessage);
        dataHolder.add(scrollPane);
        productHold.add(buyerPane);
        receiptPanel.add(receipTextArea, BorderLayout.CENTER);
        receiptPanel.add(receiptPane);

        //Add others to Frame
        add(header);
        add(dataHolder);
        add(buyerProduct);
        add(storeReceipt);
        add(receiptPanel);
        add(storeProduct);
        add(sortButton);
        add(printButton);
        add(payButton);
        add(totalLabel);
        add(cashLabel);
        add(balanceLabel);
        add(productHold);
        add(cashText);
        add(infoTotalLabel);
        add(infoBalanceLabel);
        add(clearButton);
        add(backToLogin);
        add(logoutButton);

        //Customize Frame
        setSize(800,800);
        setLayout(null);
        setTitle("Check Out Sytem");
        getContentPane().setBackground(new Color(57, 62, 70));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        ImageIcon icon = new ImageIcon("./images/cart.png");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setResizable(false);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
               addTimeOut();
            }
        });
        
        
    }

    //Commands
    @Override
    public void mouseClicked(MouseEvent event) {
        int row = table.rowAtPoint(event.getPoint());
        receipTextArea.setText("");
        addProductToPanel(row, buyerDataLoc);
        Admin.sorter(buyerDataLoc);
        infoTotalLabel.setText(String.format("%,.2f", total));
    }


    @Override
    public void mousePressed(MouseEvent event) {

    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void mouseExited(MouseEvent event) {

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToLogin) {
                addTimeOut();
                dispose();
                new Main();
        }
        else if (e.getSource() == logoutButton) {
            addTimeOut();
            dispose();
        }
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Clear":
                clear(buyerDataLoc);
                dispose();
                new CheckOutSystem();
                break;
            case "Pay":
                balance = compute(cashText);
                dispose();
                new CheckOutSystem();
                break;
            case "Sort":
                Admin.sorter(buyerDataLoc);
                dispose();
                new CheckOutSystem();
                break;
            case "Print":
                clear(buyerDataLoc);
                dispose();
                new CheckOutSystem();
            default:
                break;
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == cashText) {
            cashText.setText("");
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == cashText) {
            cashText = new JTextField(String.format("%,.2f",cash));
        }
    }

    //Center
    static void center(JTable table, DefaultTableCellRenderer centerRenderer){
        for (int i = 0 ; i < table.getColumnCount() ; i++ ) {
            table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
        }
    }

    //Clear
    static void clear(String fileName){
        try {
            FileWriter fileWriter = new FileWriter(fileName, false);
            fileWriter.write("");
            fileWriter.close();
            total = 0;
            balance = 0;
            cash = 0;
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null, "An error occurred while clearing the file.");
            e.printStackTrace();
        }
    }

    //Compute
    static double compute(JTextField cashText){
        try {
            cash = Double.parseDouble(cashText.getText());
            if (cash < total || cash <= 0){
                JOptionPane.showMessageDialog(null, "Not enought cash.");
                receipTextArea.setText("");
                return balance;
            }
            else if (total == 0) {
                JOptionPane.showMessageDialog(null, "Make a purchase first.");
                receipTextArea.setText("");
                return balance;
            }
            else {
                balance = cash - total;
                double computed = balance;
                computeProducts();
                JOptionPane.showMessageDialog(null, "Purchase Successful.");
                receipTextArea.setText(receipt());
                return computed;
            }
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Enter cash.");
            receipTextArea.setText("");
            return balance;
        }
    }


    //Add product to panel
    static void addProductToPanel(int row, String buyerDataLoc) {
        HashMap<String, String> products = new HashMap<>();
        String id = String.valueOf(table.getValueAt(row, 0)).trim();
        String name = String.valueOf(table.getValueAt(row, 1)).trim();
        double price = Double.parseDouble(String.valueOf(table.getValueAt(row, 2)).trim());
        int quantity = Integer.parseInt(String.valueOf(table.getValueAt(row, 3)).trim());
        boolean found = false;

        try (BufferedReader buyerProduct = new BufferedReader(new FileReader(buyerDataLoc))) {
            String items;
            while ((items = buyerProduct.readLine())!= null) {
                String[] parts = items.split(", ");
                products.put(parts[0].trim(), items);
            }
            buyerProduct.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        int newQuantity = 1;
        if (products.containsKey(id)) {
            String[] parts = products.get(id).split(", ");
            newQuantity = Integer.parseInt(parts[3].trim()) + 1;
        }

        if (newQuantity > quantity) {
            JOptionPane.showMessageDialog(null, "Can't add anymore.");
            return;
        }

        products.put(id, id + ", " + name + ", " + price + ", " + newQuantity);


        for (int rows = 0 ; rows < buyerTable.getRowCount() ; rows++){
            if (id.equals(String.valueOf(buyerTable.getValueAt(rows, 0)).trim())) {
                buyerTable.setValueAt(id, rows, 0);
                buyerTable.setValueAt(name, rows, 1);
                buyerTable.setValueAt(price, rows, 2);
                buyerTable.setValueAt(newQuantity, rows, 3);
                found = true;
                break;
            }
        }

        if (!found) {
            ((DefaultTableModel) buyerTable.getModel()).addRow(new Object[]{id, name, price, newQuantity});
            int newRow = buyerTable.getRowCount() - 1;
            buyerTable.setValueAt(id, newRow, 0);
            buyerTable.setValueAt(name, newRow, 1);
            buyerTable.setValueAt(price, newRow, 2);
            buyerTable.setValueAt(newQuantity, newRow, 3);
        }


        try (BufferedWriter productAdd = new BufferedWriter(new FileWriter(buyerDataLoc))) {
            for (String itemsProduct : products.values()) {
                productAdd.append(itemsProduct + "\n");
            }
            productAdd.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        };
        total+=price;
    }

    //Compute remaining product
    static void computeProducts(){
        HashMap<String, String> buyerProduct = new HashMap<>();
        HashMap<String, String> storeProduct = new HashMap<>();

        for (int row = 0 ; row < buyerTable.getRowCount() ; row++) {
            for (int col = 0 ; col < buyerTable.getColumnCount() ; col++) {
                if (col == 3) {
                    buyerProduct.put(buyerTable.getValueAt(row, 0).toString().trim(), buyerTable.getValueAt(row, 3).toString().trim());
                } 
            }
        }

        try (BufferedReader product = new BufferedReader(new FileReader(dataLoc))) {
            String items;
            while ((items = product.readLine())!= null) {
                String[] sets = items.split(", ");
                storeProduct.put(sets[0].trim(), items);
            }
            product.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        for (String id : buyerProduct.keySet()) {
            if (storeProduct.containsKey(id)){
                String[] parts = storeProduct.get(id).split(", ");
                int productQuantity = Integer.parseInt(parts[3].trim()) - Integer.parseInt(buyerProduct.get(id));
                storeProduct.remove(id);
                storeProduct.put(id, String.format("%s, %s, %s, %d", parts[0], parts[1], parts[2], productQuantity));
            }
        }


        try (BufferedWriter remainingProduct = new BufferedWriter(new FileWriter(dataLoc))) {
            for (String newItems : storeProduct.values()) {
                remainingProduct.append(newItems + "\n");
            }
            remainingProduct.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Receipt
    static String receipt(){
        ArrayList<String> Ritems = new ArrayList<>();
        ArrayList<String> Rqty = new ArrayList<>();
        ArrayList<Double> Rprice = new ArrayList<>();
        StringBuilder text = new StringBuilder();


        for (int rows = 0 ; rows < buyerTable.getRowCount(); rows++) {
            for (int cols = 1 ; cols < buyerTable.getColumnCount() ; cols++) {
                if (cols == 1) {
                    Ritems.add(buyerTable.getValueAt(rows, cols).toString());
                }
                else if (cols == 2) {
                    Rprice.add(Double.parseDouble(buyerTable.getValueAt(rows, cols).toString()) * Double.parseDouble(buyerTable.getValueAt(rows, 3).toString()));
                }
                else {
                    Rqty.add(buyerTable.getValueAt(rows, cols).toString());
                }
            }
        }
    
 
        text.append("""
    
               N   I   C   '   S                           S   H   O   P
            ====================================================================
            ------------------------------------------------------------------
                                    SALES RECEIPT                                    
            ------------------------------------------------------------------
             Qty  Items  Price\n                                               
            """);
    
        for (int items = 0; items < Ritems.size() ; items++) {
            text.append(String.format(" %s --- %s --- %,.2f%n", Rqty.get(items), Ritems.get(items), Rprice.get(items)));
        }

    
        for (int i = 1 ; i <= 50; i++) {
            text.append(" ");
        }
    
        text.append(String.format("""
    
                
                ------------------------------------------------------------------
                Total:       %,.2f
                Cash:        %,.2f
                Change:    %,.2f

                Date: %s
                Time: %s
                ------------------------------------------------------------------%n
                                          Thank You!
                """, total, cash, balance, date, time));

        return text.toString();
    }

    //Add Record
    static void addTimeOut() {

        try {
            BufferedWriter writeRecord = new BufferedWriter(new FileWriter("./files/records.txt", true));
            writeRecord.append(time + "\n");
            writeRecord.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}