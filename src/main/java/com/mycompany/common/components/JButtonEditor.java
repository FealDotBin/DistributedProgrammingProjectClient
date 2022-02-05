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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Catello
  * Editor of a JTable that contains JButtons in one of his columns
  * in particular this editor makes the button clickable and
  * can produce the event related to the click of the mouse
 */

public class JButtonEditor extends AbstractCellEditor implements TableCellEditor{
    private JButton button;
    private Object editorValue;
    private JTable table;

    /**
     * Create a table editor that displays the buttons with the 
     * text contained in the button text and adds the listener passed in the listener to each of them.
     * @param buttonText Text that will be displayed on buttons
     * @param listener Action listener for the action perfomed after that the button is clicked
     */
    public JButtonEditor(String buttonText, ActionListener listener) {
        this.button = new JButton(buttonText);
        button.addActionListener(listener);
        button.setActionCommand("edit");
        button.setBorderPainted(true);
    }
    /**
     * Create a table editor that displays the buttons with fixed text and no listener 
     * 
     */
    public JButtonEditor() {
        this.button = new JButton("Select");
        button.setActionCommand("edit");
        button.setBorderPainted(true);
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int i, int i1) {
        editorValue=o;
        table=jtable;
        return button; 
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }
    
}
