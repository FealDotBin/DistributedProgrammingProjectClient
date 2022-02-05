package com.mycompany.common.components;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Catello
 */
import com.mycompany.common.components.*;
import com.mycompany.common.components.*;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 
 * @author Catello
 * This class allows to render a certain table cell as a JButton
 */
public class JButtonRenderer extends DefaultTableCellRenderer{ 
    private int buttonColumn;
    private String buttonText;
    
    /**
     * Create a renderer that allows to render JButton in table column number button color with text buttonText
     * @param buttonColumn Table column number that will be render as a button
     * @param buttonText Text to display on rendered buttons
     */
    public JButtonRenderer(int buttonColumn, String buttonText){
        super();
        this.buttonColumn = buttonColumn;
        this.buttonText = buttonText;
    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
        JButton button= new JButton(buttonText);
        button.setBackground(new java.awt.Color(44, 73, 129));
        button.setFont(new java.awt.Font("Segoe UI", 1, 14));
        button.setForeground(new Color(200,200,200));
        button.setFocusable(true);
        if(i1==buttonColumn)
            return button;
        return this;
            
    }
    
}
