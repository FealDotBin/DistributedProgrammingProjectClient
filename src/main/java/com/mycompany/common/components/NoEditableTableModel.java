/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.components;

import com.mycompany.common.components.*;
import com.mycompany.common.components.*;
import java.awt.Color;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Catello
 * DefaultTableModel che non permette di modificare all'utente le proprie celle
 */

public class NoEditableTableModel extends DefaultTableModel{
        
        private int editableColumn;
    
        public NoEditableTableModel(Object[][] os, Object[] os1) {
            super(os, os1);
        }
        
        public NoEditableTableModel(Vector header, int editableColumn) {
            super(new Vector(), header);
            this.editableColumn = editableColumn;
        }
        
        public NoEditableTableModel(Object[][] os, Object[] os1, int editableColumn) {
            super(os, os1);
            this.editableColumn = editableColumn;
        }
        
        @Override
        public boolean isCellEditable(int i, int i1) {
            if(i1 != editableColumn)
                return false;
            else
                return true;
        }
        
       

    public void setEditableColumn(int editableColumn) {
        this.editableColumn = editableColumn;
    }
        
    }
