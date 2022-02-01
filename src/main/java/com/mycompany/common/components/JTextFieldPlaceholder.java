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
 */
public class JTextFieldPlaceholder extends JTextField{
    
    private String placeholder;
    
    public JTextFieldPlaceholder(){
        
    }
    
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
    
    public String getText(Boolean ignorePlaceholder) {
        if(ignorePlaceholder)
            if(super.getText().equals(this.placeholder))
                return "";
            else
                return super.getText();
        else
            return super.getText();
    }
    
    public void clear(){
        JTextFieldPlaceholder.this.setForeground(new Color(255,255,255,70));
        JTextFieldPlaceholder.this.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 12));
        JTextFieldPlaceholder.this.setText(JTextFieldPlaceholder.this.placeholder);
    }
    
}
