/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.components;
import com.mycompany.common.components.*;
import com.mycompany.common.components.*;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
/**
 *
 * @author CATELLO
 * This class represent a graphic components. This is a text field with placeholder
 */
public class JTextFieldPlaceholder extends JTextField{
    
    private String placeholder;
    /**
     * Create a text label with no placeholder
     */
    public JTextFieldPlaceholder(){
        
    }
    
    /**
     * Set the appearance of the text field.
     * After that it adds a foscus listener that allows when the label takes the focus to eliminate the placeholder
     * while when it loses the focus to put it back if the text field is empty.
     * @param placeholder Text to show as placeholder in text field 
     */
    public JTextFieldPlaceholder(String placeholder){
        super();
        this.placeholder = placeholder;
        this.setBackground(new java.awt.Color(60, 63, 65));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(80,80,80)));
        this.setText(placeholder);
        this.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 12));
        this.setForeground(new Color(255,255,255,70));
        this.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e) {
                if(JTextFieldPlaceholder.this.getText().trim().equals(JTextFieldPlaceholder.this.placeholder)){
                    JTextFieldPlaceholder.this.setText("");
                    JTextFieldPlaceholder.this.setForeground(new Color(200,200,200));
                    JTextFieldPlaceholder.this.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
                    
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(JTextFieldPlaceholder.this.getText().equals("")){
                    JTextFieldPlaceholder.this.setForeground(new Color(255,255,255,70));
                    JTextFieldPlaceholder.this.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 12));
                    JTextFieldPlaceholder.this.setText(JTextFieldPlaceholder.this.placeholder);
                    
                }
                
            }
            
        });
        
    }
    /**
     * If ignorePlaceholder is true it returns the text contained in the text field.
     * If ignorePlaceholder is false it returns the text contained in the text field 
     * if it is different from the placeholder otherwise it returns an empty string
     * @param ignorePlaceholder true for ignoring placeholer, false otherwise
     * @return  Text contained in the text field. 
     */
    public String getText(Boolean ignorePlaceholder) {
        if(ignorePlaceholder)
            if(super.getText().equals(this.placeholder))
                return "";
            else
                return super.getText();
        else
            return super.getText();
    }
    
    /**
     * Forces the text field to show the placeholder even without the focus event occurring
     */
    public void clear(){
        JTextFieldPlaceholder.this.setForeground(new Color(255,255,255,70));
        JTextFieldPlaceholder.this.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 12));
        JTextFieldPlaceholder.this.setText(JTextFieldPlaceholder.this.placeholder);
    }
    
}
