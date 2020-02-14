/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java大作业;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lenovo
 */
public class ticket {
    void test(){
        try {
                // TODO add your handling code here:
                String sql2="select Cap from Theater where TheName='"+""+"'";
                //String sql="update Ticket set status='0' where row='"+btn.getActionCommand().charAt(0)+"' and col='"+btn.getActionCommand().charAt(2)+"' and TicID in (select TicID from Ticket where TicID like 'S01%')";
                        //('"+TicID+"','"+row+"','"+col+"','"+SchID+"','"+status+"');
                    System.out.println(sql2);
                 jdbc.modifyBySql(sql2);
                for (int i = 1; i < 7; i++) {
                    for (int j = 1; j <7 ; j++) {
                        
                    
                String TicID="S07"+"-00"+((i-1)*6+j);
                int row=i;
                int col=j;
                String SchID="S007";
                String status="1";
                String sql="insert into Ticket values('"+TicID+"','"+row+"','"+col+"','"+SchID+"','"+status+"')";
                //String sql="update Ticket set status='0' where row='"+btn.getActionCommand().charAt(0)+"' and col='"+btn.getActionCommand().charAt(2)+"' and TicID in (select TicID from Ticket where TicID like 'S01%')";
                        //('"+TicID+"','"+row+"','"+col+"','"+SchID+"','"+status+"');
                    System.out.println(sql);
                 jdbc.modifyBySql(sql);
                  //DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
                  
                Vector v = new Vector();
                v.add(TicID);
                v.add(row);
                v.add(col);
                v.add(SchID);
                v.add(status);
                //dtm.addRow(v);
                    }
                }
                } catch (SQLException ex) {
                   // Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    public static int ticketopear(String TheID,String StuID){
        int cap=0,col=0,row=0;
        try{
            String sql="select Cap from Theater where TheID='"+TheID+"'";
            ResultSet rs = jdbc.queryBySql(sql);
               while(rs.next()){
                  cap= rs.getInt(1);
                  System.out.print(cap);
               }
            
        for(int i=0;col*row<cap;i++){
            col=i+1;
            if(col*row<cap){
                row=i+1;
                continue;
            }
        }
        System.out.print(col+""+row+"\n");
        
        for(int i=1;i<=row;i++){
            //System.out.print(i);
            for(int j=1;j<=col;j++){
                String TicID=StuID+"-00"+((i-1)*row+j);
                String sql2="insert into Ticket values('"+TicID+"','"+i+"','"+j+"','"+StuID+"','1')";
                jdbc.modifyBySql(sql2);
                System.out.print(TicID);
            }
            System.out.print("\n");
        }
        }catch(Exception e){
            return 0;
        }
        return 1;
        }
    public static void ticketlog(String s){
        java.sql.Time sqlTime = new java.sql.Time(System.currentTimeMillis());
        String mas=sqlTime.toString()+"售出："+s;
        try{
			File file=new File("ticket.log");
			FileOutputStream fos=new FileOutputStream(file,true);
			OutputStreamWriter osw=new OutputStreamWriter(fos);
			//写入文件并换行
			osw.write(mas);
                        osw.write("\r\n");
			osw.close();
		}catch(FileNotFoundException ex){
			System.out.println("log文件不存在");
		}catch(IOException ex){
			System.out.println("读写异常:");
			ex.printStackTrace();
		}
    }
    public static void main(String[] args) {
        //ticketlog();
    }
}
