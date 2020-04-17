package test3;

import java.text.DecimalFormat;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel{

	 String[] n;
	 Object[][] p;
	 
	 public void buildModel(String[] s, Object[][] o) {
		 n = s;
		 p = o;
	 }
	 
     public void addTotalRow(String[] s){
    	 Object[][] np = null;
    	 int l;
    	 int cl = n.length;;
    	 if(p == null) {
    		 l = 1;
    		 np = new Object[l][cl]; 
    	 }   		 
    	 else {
    		 l = p.length + 1 ;
        	 np = new Object[l][cl];   	   	        	 
        	 for(int i = 0; i < p.length; i++) {
        		 for (int j = 0 ; j < n.length; j ++){
        			 np[i][j] = p[i][j];
        		 }
        	 }
        	 
    	 }
    	 for(int i = 0; i < s.length; i ++) {
    		 np[l-1][i] = s[i];
    	 }
    	 
    	 p = np;
 	 
     }
     
     public void addRow(String[] s) {
    	 int l = p.length + 1 ;
    	 int cl = n.length;
    	 Object[][] np = new Object[l][cl];   	   	        	 
    	 for(int i = 0; i < p.length-1; i++) {
    		 for (int j = 0 ; j < n.length; j++){
    			 np[i][j] = p[i][j];
    		 }
    	 }    	 
    	 for(int i = 0; i < s.length; i ++) {
    		 np[l-2][i] = s[i];
    	 }    
    	 for(int i = 0; i < n.length; i++) {
    		 np[l-1][i] = p[l-2][i];   		 
    	 }
    	 p = np;      	 
     }
     
     public void deleteRow(int row) {
    	 int l = p.length - 1 ;
    	 int cl = n.length;
    	 Object[][] np = new Object[l][cl];  
    	 for(int i = 0; i < row; i++) {
    		 for (int j = 0 ; j < n.length; j++){
    			 np[i][j] = p[i][j];
    		 }
    	 }
    	 for(int i = row-1; i < p.length-1; i++) {
    		 for (int j = 0 ; j < n.length; j++){
    			 np[i][j] = p[i+1][j];
    		 }
    	 } 
    	 p = np;
    	 
     }
     
     public boolean isContained(String s) {
    	 for(int i = 0; i < p.length; i++) {
    		 if(p[i][0] == s)
    			 return true;
    	 }
    	 return false;
     }
     
     public int rowOfElement(String s) {
    	 for(int i = 0; i < p.length; i++) {
    		 if(p[i][0] == s)
    			 return i+1;
    	 }
    	 return -1;
     }
     
     @Override
     public int getRowCount() {
    	 if(p == null)
    		 return 0;
    	 else
    		 return p.length;
     }

     @Override
     public int getColumnCount() {
         return n.length;
     }

     @Override
     public Object getValueAt(int row, int col) {
         return p[row][col];
     }

     @Override
     public String getColumnName(int col) {
         return n[col];
     }

     @Override
     public Class<?> getColumnClass(int c) {
         return getValueAt(0, c).getClass();
     }

     @Override
     public boolean isCellEditable(int rowIndex, int columnIndex) {  
    	 String s = "<html>Animal <br> (type) </html>";
    	 String colName = n[columnIndex].toString();
    	 if(columnIndex == 0  || s == colName )   			 
    		 return false;
    	 else
    		 return true;
     }	 

     @Override
     public void setValueAt(Object value, int row, int col) {
         p[row][col] = value;
         fireTableCellUpdated(row, col);
     }

     public void mySetValueAt(Object value, int row, int col) {
         p[row][col] = value;
     }
     
     public double getSum(int column) {	
	    if(p == null)
	    	return Double.parseDouble(new DecimalFormat("0.00").format(0.0));
	    else {
	    	double sum1 = 0.0;
		    for(int i = 0; i < getRowCount(); i++) {
		        String ddd = (String) getValueAt(i, column);	
		        for(int j = 0; j < ddd.length(); j++) {
		        	if(!Character.isDigit(ddd.charAt(j)) && ddd.charAt(j) != '.') {
		        		return 0.0;
		        	}
		        }
		        Double val = Double.parseDouble(ddd);
		        sum1 += val;	       
		    }	   
		    DecimalFormat df = new DecimalFormat("0.00");	    
	        return Double.parseDouble(df.format(sum1));	 	    	
	    }   	
	 }
     
     public double getNewSum(int column) {    	 
    	 if(p == null)    		
    		 return 0.0;
    	 else {
    		 
    		 double sum1 = 0.0;
	 	     for(int i = 0; i < getRowCount() - 1 ; i++) {
	 	         String ddd = (String) getValueAt(i, column);		        
	 	         Double val = Double.parseDouble(ddd);
	 	         sum1 += val;	       
	 	     }	   
	 	     DecimalFormat df = new DecimalFormat("0.00");	    
	         return Double.parseDouble(df.format(sum1));	
    	 }    			     
 	 }
	
     public String[] getEachSum() {
		String [] eachSum = new String [getColumnCount()];
		eachSum[0] = "Total";
		for(int i = 1; i < getColumnCount(); i++) {
			double num = getSum(i);
			if(num == 0.0) {
				eachSum[i] = "N/A";
			}
			else
				eachSum[i] = Double.toString(getSum(i));			
		}
		return eachSum;
	}


}
