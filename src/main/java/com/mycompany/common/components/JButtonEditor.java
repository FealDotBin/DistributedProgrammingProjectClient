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
 * Editor di una JTable che contiene nella sua quinta colonna dei JButton
 * in particolare questo editor fa si che il bottone sia cliccabile e
 * posso produrre l'evento relativo al click del muose
 */

public class JButtonEditor extends AbstractCellEditor implements TableCellEditor{
    private JButton button;
    private Object editorValue;
    private JTable table;

    public JButtonEditor(String buttonText, ActionListener listener) {
        this.button = new JButton(buttonText);
        button.addActionListener(listener);
        button.setActionCommand("edit");
        button.setBorderPainted(true);
    }
    
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
    /**
     * Gestisce l'evento generato dalla pressione della cella.
     * Evidenzia la cella selezionata per poi recuperare la prima cella della riga selezionata
     * per poi istanziare le finestra successiva.
     * @param ae evento che rappresenta la pressione del bottone 
     */

      
        /*String site= table.getModel().getValueAt(row, 1).toString();
        String typology = table.getModel().getValueAt(row, 2).toString();
        int time=parseInt(table.getModel().getValueAt(row, 3).toString().trim());
        /*VerifyActivityGUI verify= new VerifyActivityGUI(planner, id.trim(), time, site,typology);
        verify.setVisible(true);
        verify.pack();
        verify.setLocationRelativeTo(null);
        verify.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.dispose();*/
    
    
}
