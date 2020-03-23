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
	 
     public void addRow(String[] s){
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
    	 /*for(int i = 0; i < s.length; i ++) {
    		 np[l-1][i] = s[i];
    	 }*/
    	 np[l-1] = s;
    	 p = np;
 	 
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
