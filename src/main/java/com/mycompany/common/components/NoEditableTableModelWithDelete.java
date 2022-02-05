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
 * Table model that not allow cells modification except in one column
 * It also allows you to delete the row to which the pressed button belongs
 */

public class NoEditableTableModelWithDelete extends DefaultTableModel{
        
        private int editableColumn;
        
         /**
         * Creation of a table model that does not allow to modify any cells
         * @param os Dati contenuti nella tabella
         * @param os1 Header della tabella
         */
        public NoEditableTableModelWithDelete(Object[][] os, Object[] os1) {
            super(os, os1);
        }
        
        /**
         * Creation of a empty table model that hallow modification only on the cells that are in column editableColumn
         * @param header Table header
         * @param editableColumn Editable column number
         */
        public NoEditableTableModelWithDelete(Vector header, int editableColumn) {
            super(new Vector(), header);
            this.editableColumn = editableColumn;
        }
        
        /**
         * @param os contained in the table Table 
         * @param os1 Table header
         * @param editableColumn Editable column number
         */
        public NoEditableTableModelWithDelete(Object[][] os, Object[] os1, int editableColumn) {
            super(os, os1);
            this.editableColumn = editableColumn;
        }
        
        /**
         * Return if a table cell is editable or not. In this case only a cell that belongos to column 
         * with number editableColumn return true
         * @param i cell row number
         * @param i1 cell column number
         * @return true if the cell is editable, false otherwise
         */
        @Override
        public boolean isCellEditable(int i, int i1) {
            if(i1 != editableColumn)
                return false;
            else
                return true;
        }
        
        /**
         * If a button in column button is pushed this methos removes that row
         * @param aValue true if the button is presse, false otherwise
         * @param row row number to whom the button belongs
         * @param column column number to whom the button belongs
         */
        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (column == editableColumn && (aValue instanceof Boolean)) {
                boolean pushed = (boolean) aValue;
                if (pushed) {
                    removeRow(row);
                }
            }
        }  
        
    /**
    * Set the column number that is editable
    * @param editableColumn Desired Editable column number
    */
    public void setEditableColumn(int editableColumn) {
        this.editableColumn = editableColumn;
    }
        
    }
