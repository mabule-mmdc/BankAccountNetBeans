/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mmdc.bankingapp;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author micahbule
 */
public class AccountList extends javax.swing.JPanel {
    private LoadedFiles loadedFiles;
    
    /**
     * Creates new form AccountList
     * @param loadedFiles
     */
    public AccountList(LoadedFiles loadedFiles) {
        this.loadedFiles = loadedFiles;
        
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        accountsList = new javax.swing.JTable();

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Account Details");

        accountsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(accountsList);
        accountsList.getAccessibleContext().setAccessibleName("accountsList");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable accountsList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    public void loadAccountsTable() {
        try {
            CSVReader holderReader = loadedFiles.readFile(loadedFiles.getHolderFilePath());
            CSVReader accountsReader = loadedFiles.readFile(loadedFiles.getAccountsFilePath());
            CSVReader transactionsReader = loadedFiles.readFile(loadedFiles.getTransactionsFilePath());

            List<String[]> holdersList = holderReader.readAll();
            List<String[]> accounts = accountsReader.readAll();
            List<String[]> transactions = transactionsReader.readAll();
            ArrayList<AccountHolder> accountHolders = new ArrayList<>();
            ArrayList<BankAccount> allBankAccounts = new ArrayList<>();

            for(int i = 0; i < holdersList.size(); i++) {
                accountHolders.add(new AccountHolder(holdersList.get(i)[1], holdersList.get(i)[2], holdersList.get(i)[0], accounts, i));
            }

            for (int i = 0; i < accountHolders.size(); i++) {
                allBankAccounts.addAll(accountHolders.get(i).getBankAccounts());
            }

            for (int i = 0; i < allBankAccounts.size(); i++) {
                BankAccount currentBankAccount = allBankAccounts.get(i);

                for(int j = 0; j < transactions.size(); j++) {
                    String[] currentTransaction = transactions.get(j);

                    if (currentBankAccount.getAccountNumber().matches(currentTransaction[1])) {
                        currentBankAccount.addTransaction(new Transaction(currentBankAccount, currentTransaction[0], currentTransaction[2], currentTransaction[3], currentTransaction[4], currentTransaction[5]));
                    }
                }

                allBankAccounts.set(i, currentBankAccount);
            }

            String[] columns = {
                    "Account Number",
                    "Account Name",
                    "Account Type",
                    "Balance"
            };

            DefaultTableModel model = new DefaultTableModel(columns, 0);

            for (int i = 0; i < allBankAccounts.size(); i++) {
                model.addRow(new String[]{allBankAccounts.get(i).getAccountNumber(), allBankAccounts.get(i).getAccountName(), allBankAccounts.get(i).getAccountType(), String.valueOf(allBankAccounts.get(i).getCurrentBalance())});
            }

            accountsList.setModel(model);

            holderReader.close();
            accountsReader.close();
            transactionsReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}