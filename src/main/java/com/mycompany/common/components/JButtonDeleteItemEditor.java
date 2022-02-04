/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.components;

import com.mycompany.common.components.*;
import com.mycompany.common.components.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Catello
  * Editor of a JTable that contains JButtons in one of his columns
  * in particular this editor makes the button clickable and
  * can produce the event related to the click of the mouse.
  * It also allows you to delete the row to which the pressed button belongs
 */

public class JButtonDeleteItemEditor extends AbstractCellEditor implements TableCellEditor{
    private JButton button;
    private String buttonText;
    boolean isButtonPushed;
    /**
     * Create a table editor that displays the buttons with the 
     * text contained in the button text and adds the listener passed in the listener to each of them.
     * @param buttonText Text that will be displayed on buttons
     * @param listener Action listener for the action perfomed after that the button is clicked
     */
    public JButtonDeleteItemEditor(String buttonText, ActionListener listener) {
        button = new JButton();
        button.addActionListener(listener);
        
    }
    
    /**
     * Create a table editor that displays the buttons with fixed text and no listener 
     */
    public JButtonDeleteItemEditor() {
        this.button = new JButton("Select");
        button.setActionCommand("edit");
        button.setBorderPainted(true);
    }

    /**
     * Returns the editor for a certain table cell
     * @param table  Create a table editor that displays the buttons with fixed text and no listener 
     * @param value  Cell tye componente
     * @param isSelected True if the cell selected, false otherwise
     * @param row row number which cell belongs
     * @param column column number which cell belongs
     * @return Editated component for a certain cell
     */
    @Override
    public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText(buttonText);
        button.setBackground(new java.awt.Color(44, 73, 129));
        button.setFont(new java.awt.Font("Segoe UI", 1, 14));
        button.setForeground(new Color(200,200,200));
        isButtonPushed = false;
        return button; 
    }

    /**
     * Returns true if the button in the table is pushed
     * @return true if the button in the table is pushed
     */
    @Override
    public Object getCellEditorValue() {
        return isButtonPushed;
    }
    
    /**
     * Force button pressure outside this class
     */
    public void setIsButtonPressed(){
        isButtonPushed = true;
    }
    
    /**
     * Listener that allows to delete row from table
     */
    public class DeleteButtonListener implements ActionListener {

        @Override
        /**
         * When a button in the table is clicked stop editing cell. In this way is possibile to delete row without expetion
         */
        public void actionPerformed(ActionEvent e) {
            isButtonPushed = true;
            stopCellEditing();
        }
    }
}

