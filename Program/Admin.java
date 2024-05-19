import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Admin extends JFrame implements ActionListener{
    JPanel header, footer, scrollHolder;
    JLabel headerMessage, groupName;
    JScrollPane scrollPane;
    JButton productRemove, productAdd, productChange, backButton, exitButton, userButton;
    DefaultTableModel model, changeModel;
    int selected;   
    Object [][] data;
    String[] tableLabel;
    DefaultTableCellRenderer centerRenderer;
    static JTable table;
    static String fileLocation = "./files/products.txt";
    static int ItemsID = -1;

    Admin() {
        //Data
        tableLabel = new String[] {"Product ID", "Name", "Price", "Quantity"};
        data = getData(fileLocation);

        //Model
        model = new DefaultTableModel(data,tableLabel) {
            @Override
            public boolean isCellEditable(int row, int colum) {
                return colum!=0 && colum!=1;
            }
        };

        //Table
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));

       
        //DefaultTableCellRenderer
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );

        //ScrollPane
        scrollPane = new JScrollPane(table);    

        //Panels
        header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(new Color(179, 200, 207));
        header.setBounds(0, 0, 800, 80);
    
        scrollHolder = new JPanel();
        scrollHolder.setLayout(new BorderLayout());
        scrollHolder.setBounds(15, 110, 750, 350);
        scrollHolder.setBackground(Color.white);

        footer = new JPanel();
        footer.setLayout(new BorderLayout());
        footer.setBackground(new Color(179, 200, 207));
        footer.setBounds(0, 792, 800, 70);
    
        //Button
        productAdd = new JButton("Add");
        productAdd.setBounds(15, 490, 200, 50);
        productAdd.addActionListener(this);
        productAdd.setFocusable(false);

        productRemove = new JButton("Remove");
        productRemove.setBounds(290, 490, 200, 50);
        productRemove.addActionListener(this);
        productRemove.setFocusable(false);

        productChange = new JButton("Apply Changes");
        productChange.setBounds(565, 490, 200, 50);
        productChange.addActionListener(this);
        productChange.setFocusable(false);

        userButton = new JButton("Account");
        userButton.setBounds(15, 560, 200, 50);
        userButton.addActionListener(this);
        userButton.setFocusable(false);

        backButton = new JButton("Back to Login");
        backButton.setBounds(290, 560, 200, 50);
        backButton.addActionListener(this);
        backButton.setFocusable(false);

        exitButton = new JButton("Exit");
        exitButton.setBounds(565, 560, 200, 50);
        exitButton.addActionListener(this);
        exitButton.setFocusable(false);
    
        //Label
        headerMessage = new JLabel();
        headerMessage.setText("ADMIN");
        headerMessage.setFont(new Font("Roboto", Font.BOLD, 20));
        headerMessage.setForeground(Color.white);
        headerMessage.setVerticalAlignment(JLabel.CENTER);
        headerMessage.setHorizontalAlignment(JLabel.CENTER);

        groupName = new JLabel();
        groupName.setText("Created by: Open Circle's Group");
        groupName.setForeground(Color.white);
        groupName.setFont(new Font("Roboto", Font.PLAIN, 15));
        groupName.setVerticalAlignment(JLabel.CENTER);
        groupName.setHorizontalAlignment(JLabel.CENTER);
    
        //Add others to Panel
        header.add(headerMessage);
        scrollHolder.add(scrollPane);
        footer.add(groupName);
    
        //Add others to Frame
        add(header);
        add(scrollHolder);
        add(productAdd);
        add(productRemove);
        add(productChange);
        add(userButton);
        add(backButton);
        add(exitButton);
        add(footer);
    
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
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Add":
                add(fileLocation);
                itemsCount(fileLocation);
                dispose();
                new Admin();
                break;
            case "Remove":
                selected = table.getSelectedRow();
                if (selected < 0) {
                    JOptionPane.showMessageDialog(this, "Please select an item you want to remove.");
                }
                else {
                    removeProduct(selected, fileLocation);
                    itemsCount(fileLocation);
                    dispose();
                    sorter(fileLocation);
                    new Admin();
                }
                break;
            case "Apply Changes":
                selected = table.getSelectedRow();
                if (selected < 0) {
                    JOptionPane.showMessageDialog(this, "Please select an item you want to change or edit.");
                }
                else {
                    JOptionPane.showMessageDialog(this, "Note: Make sure to select an edited item before clicking Apply Changes button.");
                    changeProduct(selected, fileLocation);
                    dispose();
                    sorter(fileLocation);
                    new Admin();
                }
                break;
            case "Account":
                dispose();
                new Account();
                break;
            case "Back to Login":
                new Main();
                dispose();
                break;
            case "Exit":
                dispose();
                break;
            default:
                break;
        }
    }

    //Get data
    static Object[][] getData(String fileLocation){
        ArrayList<Object[]> dataList = new ArrayList<>();
        try {
            BufferedReader products = new BufferedReader(new FileReader(fileLocation));
            String items;
            while ((items = products.readLine()) != null){
                String[] parts = items.split(",");
                if (parts.length == 4) {
                    dataList.add(new Object[]{parts[0], parts[1], parts[2], parts[3]});
                }
            }
    
            products.close();
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }

        Object[][] data = dataList.toArray(new Object[0][]); 
        return data;
    }


    //Item count 
    static void itemsCount(String fileLocation){
        ArrayList<String> ids = new ArrayList<>();
        try {
            BufferedReader ItemsCount = new BufferedReader(new FileReader(fileLocation));
            String itemsNumber;
            while((itemsNumber = ItemsCount.readLine()) != null){
                String[] counter = itemsNumber.split(", ");
                ids.add(counter[0].trim());
            }
            ItemsCount.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        int counts = ids.size() - 1;
        for (int i = 0 ; i <= counts; i++){
            boolean alreadyHave = ids.contains(String.valueOf(i));
            if (!alreadyHave) {
                ItemsID = i - 1;
                break;
            }
            else {
                ItemsID = i;
            }
        }
    }


    //Add
    static void add(String fileLocation){
        ImageIcon icon = new ImageIcon(new ImageIcon("./images/add.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        String addItemName, addCapitalLetter, priceInput, QuantityInput;
        double addItemPrice;
        int productQuantity;
        boolean running = true;
        while(running) {
            boolean itemExist = false;
            addItemName = (String) JOptionPane.showInputDialog(null, "Enter product name: ","Add item", JOptionPane.QUESTION_MESSAGE, icon, null, "");
            if (addItemName == null || addItemName.isEmpty()){
                break;
            }
            addCapitalLetter = addItemName.substring(0,1).toUpperCase() + addItemName.substring(1);

            try {
                BufferedReader productName = new BufferedReader(new FileReader(fileLocation));
                String itemNames;
                while((itemNames = productName.readLine()) != null){
                    String[] parts = itemNames.split(", ");
                    if (parts.length == 4 && parts[1].trim().equalsIgnoreCase(addCapitalLetter)) {
                        JOptionPane.showMessageDialog(null, addCapitalLetter + " is already in the list of products.");
                        itemExist = true;
                    }
                }
                productName.close();
            }
            catch(IOException e){
                JOptionPane.showMessageDialog(null, "Error occured while adding an item.");
                return;
            }

            if (!itemExist) {
                itemsCount(fileLocation);
                priceInput = (String) JOptionPane.showInputDialog(null, "Enter product price: ", "Add item", JOptionPane.QUESTION_MESSAGE, icon, null, "");
                if (priceInput == null || priceInput.isEmpty()){
                    break;
                }
                QuantityInput = (String) JOptionPane.showInputDialog(null, "Enter product quantity: ", "Add item", JOptionPane.QUESTION_MESSAGE, icon, null, "");
                if (QuantityInput == null || priceInput.isEmpty()){
                    break;
                }
                try{
                    addItemPrice = Double.parseDouble(priceInput); 
                    productQuantity = Integer.parseInt(QuantityInput);
                }
                catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Enter a valid number.");
                    continue;
                }
                try{
                    BufferedWriter productAdd = new BufferedWriter(new FileWriter(fileLocation, true));
                    ItemsID++;
                    productAdd.append(ItemsID + ", " + addCapitalLetter + ", " + addItemPrice + ", " + productQuantity + "\n");
                    productAdd.close();
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error occured while adding an item.");
                    return;
                }
                JOptionPane.showMessageDialog(null, addCapitalLetter + " is added Successfully with the price of PHP " + addItemPrice);
                sorter(fileLocation);
                running = false;
            }
        }
    }

    //Sorter
    static void sorter(String fileLocation){
        ArrayList<String> productList = new ArrayList<>();
        ArrayList<String> updateID = new ArrayList<>();

        try {
            BufferedReader productsCount = new BufferedReader(new FileReader(fileLocation));
            String products;
            while((products = productsCount.readLine()) != null){
                productList.add(products);
                String[] productId = products.split(", ");
                updateID.add(productId[0]);
            }
            productsCount.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }


        Map<String, String> productMap = new HashMap<>();
        for (int i = 0; i < updateID.size(); i++) {
            productMap.put(updateID.get(i), productList.get(i));
        }

        Collections.sort(updateID, (a, b) -> Integer.compare(Integer.parseInt(a), Integer.parseInt(b)));

        File file = new File(fileLocation);
        file.delete();
        
        try{
            BufferedWriter productUpdate = new BufferedWriter(new FileWriter(fileLocation, true));
            for (String id : updateID) {
                productUpdate.append(productMap.get(id) + "\n");
            }
            productUpdate.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Remove 
    static void removeProduct(int selected, String fileLocation){
        ArrayList<String> products = new ArrayList<>();
        try {
            BufferedReader productsCount = new BufferedReader(new FileReader(fileLocation));
            String itemsNumber;
            while((itemsNumber = productsCount.readLine()) != null){
                products.add(itemsNumber);
            }
            productsCount.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        products.remove(selected);

        File oldFile = new File(fileLocation);
        oldFile.delete();
        try{
            BufferedWriter productAdd = new BufferedWriter(
                new FileWriter(fileLocation, true));

                for (String itemsProduct : products){
                    productAdd.append(itemsProduct + "\n");
                }
            productAdd.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        products.clear();
    }

    //Change 
    static void changeProduct(int selected, String fileLocation){
        ArrayList<String> products = new ArrayList<>();
        String id = String.valueOf(table.getValueAt(selected, 0));
        String name = String.valueOf(table.getValueAt(selected, 1));
        double price;
        int quantity;

        try {
            price = Double.parseDouble(table.getValueAt(selected, 2).toString().trim());
        }
        catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Failed to update. Make sure you input valid number.");
            return;
        }

        try {
            quantity = Integer.parseInt(table.getValueAt(selected, 3).toString().trim());
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Failed to update. Make sure you input valid number.");
            return;
        }

        try {
            BufferedReader productsCount = new BufferedReader(new FileReader(fileLocation));
            String itemsNumber;
            while((itemsNumber = productsCount.readLine()) != null){
                products.add(itemsNumber);
            }
            productsCount.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        products.set(selected, String.format("%s,%s, %,.2f, %d", id, name, price, quantity));

        File oldFile = new File(fileLocation);
        oldFile.delete();
        try{
            BufferedWriter productAdd = new BufferedWriter(
                new FileWriter(fileLocation, true));

                for (String itemsProduct : products){
                    productAdd.append(itemsProduct + "\n");
                }
            productAdd.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        products.clear();
    }
}