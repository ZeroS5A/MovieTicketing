/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java大作业;

import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author lenovo
 */
public class Admin extends javax.swing.JFrame {
    JButton[] btnNums= new JButton[64];
    JFrame frame;
    DefaultMutableTreeNode[] Movie=new DefaultMutableTreeNode[64];
    DefaultMutableTreeNode[] Sch=new DefaultMutableTreeNode[128];
    /**
     * Creates new form MovieDemo
     */
    public Admin() {
        initComponents();
        setTitle("电影城出票系统");
        this.setLocationRelativeTo(null);
        jTextPane1.setOpaque(false);
        initTree(jTree1,Movie,Sch);
        
    }
     void initBtnNums(int maxcol,int cap, JButton btnNums[]){ /*先  实例化按钮 + 布局安排*/
      //JButton[] btnNums = new JButton[cap];
      //确认布局
      jPanel2.setLayout(new GridLayout(0,maxcol));
      
        for (int i = 0; i < cap; i++) {
            //实例化数字按钮组里的每个按钮
            btnNums[i] = new JButton("" + i);
            //设置数字按钮表面上的字体
            btnNums[i].setFont(new java.awt.Font("宋体", Font.BOLD, 14));
            //为数字按钮添加监听器
            //btnNums[i].setEnabled(false);
            btnNums[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    JButton btn = (JButton) event.getSource();
        DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        int i=JOptionPane.showConfirmDialog(frame, "确定售出"+btn.getActionCommand()+"的票吗？", "售票确认", JOptionPane.YES_NO_OPTION);
        //0--是，1--否
        if(i==0){
            try{
                
            if(selectedNode.isLeaf()){
            int row=0;
            String z=null, t=null;
            String Theater = selectedNode.toString().substring(0, 3);
            String Schudule = selectedNode.toString().substring(4, 23)+"%";
            String sql="select SchID from Schedule left outer join Theater on Schedule.TheID=Theater.TheID "+"where Convert(varchar,SchTime,120) LIKE '"+Schudule+"' and TheName ='"+Theater+"'";
                //System.out.println(sql);
            ResultSet rs= jdbc.queryBySql(sql);//输出带有时间ID 1条或以上
            while(rs.next()){
               row++;
               z=rs.getString(1);
               //t=rs.getString(2);
            }
                String sql1="update Ticket set status='0' where row='"+btn.getActionCommand().charAt(0)+"' and col='"+btn.getActionCommand().charAt(2)+
                        "' and TicID in (select TicID from Ticket where SchID = '"+z+"')";
                //System.out.println(sql1);
                jdbc.modifyBySql(sql1);
                btn.setEnabled(false);
                btn.setContentAreaFilled(false);
                
                String sql2="Select TicID from Ticket where row='"+btn.getActionCommand().charAt(0)+"' and col='"+btn.getActionCommand().charAt(2)+
                        "' and SchID = '"+z+"'";
                //System.out.println(sql2);
                ResultSet rs2= jdbc.queryBySql(sql2);//输出带有时间ID 1条或以上
                while(rs2.next()){
                    t=rs2.getString(1);
                    ticket.ticketlog(t);
                }
            }
            //jButton1.setEnabled(false);
            } catch (SQLException ex) {
            }
        }
                    
                }
            });
        }
    }
     void printshow(){
        DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        
        try {
            String sql="select "+"*"+" from Movie "+"where Movname = '"+selectedNode.getParent().toString()+"'";
            ResultSet rs=jdbc.queryBySql(sql);
            if(rs.next()){
                jLabel2.setIcon(new ImageIcon(getClass().getResource("/java大作业/"+rs.getString(5))));
                jTextPane1.setText("\n\n电影名称\t"+rs.getString(2)+"\r\n"+
                        "导    演\t"+rs.getString(3)+"\r\n"+
                        "主要演员\t"+rs.getString(4)+"\r\n"+
                        "票    价\t"+rs.getString(7)+"\r\n");
                
              } 
              rs.close();
          } catch (Exception ex) {
              
          
          }
     }
    void initTree(JTree jTree1,DefaultMutableTreeNode movie[],DefaultMutableTreeNode Sch[]){
        try{
        int i=0,j=0;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTree1.getModel().getRoot();
        TreePath path=new TreePath(node.getPath());
        //expandAll(jTree1, new TreePath(node), true); 
        String sql="select Movname,TheName,SchTime from Schedule a,Movie b,Theater c where a.MovID=b.MovID and a.TheID=c.TheID";
        ResultSet rs=jdbc.queryBySql(sql);
        String sql1="select Movname from Movie";
        ResultSet rs1=jdbc.queryBySql(sql1);
            //System.out.println("java大作业.新建JFrame1.initTree()");
        while(rs.next()){
                   //.add(new DefaultMutableTreeNode(rs.getString(1)));//添加电影名字枝结点
                j++;
                while(rs1.next()){
                    i++;
                    movie[i]= new DefaultMutableTreeNode (rs1.getString(1));
                    node.add(movie[i]);
                //node.add(new DefaultMutableTreeNode(rs1.getString(1)));
                }
            for(int z=1;z<i+1;z++){
                if(movie[z].toString().equals(rs.getString(1))){
                    Sch[z]=new DefaultMutableTreeNode (rs.getString(2)+" "+rs.getString(3).substring(0, 19));
                    movie[z].add(Sch[z]);
                }
            }
            //System.out.println("java大作业.新建JFrame1.initTree(00)");
        }
        
    jTree1.updateUI();
    jTree1.expandPath(path);
    }catch(SQLException ex){
        
    }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setOpaque(false);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("电影放映信息");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setOpaque(false);
        jTree1.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
            }
            public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
                jTree1TreeExpanded(evt);
            }
        });
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jTree1.addTreeWillExpandListener(new javax.swing.event.TreeWillExpandListener() {
            public void treeWillCollapse(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {
            }
            public void treeWillExpand(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {
                jTree1TreeWillExpand(evt);
            }
        });
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridLayout(6, 6));

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane2.setOpaque(false);

        jTextPane1.setEditable(false);
        jTextPane1.setFont(new java.awt.Font("宋体", 1, 18)); // NOI18N
        jTextPane1.setEnabled(false);
        jTextPane1.setOpaque(false);
        jScrollPane2.setViewportView(jTextPane1);

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(3, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel1, gridBagConstraints);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/java大作业/31u58PICxMC_1024.jpg"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jLabel1, gridBagConstraints);

        jMenu1.setText("系统管理");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem7.setText("用户管理");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("票务管理");

        jMenuItem5.setText("退票");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("重置");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("电影信息");

        jMenuItem2.setText("电影信息维护");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem3.setText("电影放映场次信息维护");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("放映厅");

        jMenuItem4.setText("放映厅维护");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jTree1TreeExpanded(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jTree1TreeExpanded
        // TODO add your handling code here:   
        //printshow();
    }//GEN-LAST:event_jTree1TreeExpanded

    private void jTree1TreeWillExpand(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {//GEN-FIRST:event_jTree1TreeWillExpand
        // TODO add your handling code here:
        //printshow();
    }//GEN-LAST:event_jTree1TreeWillExpand

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        try{
            if(selectedNode.isLeaf()){
                printshow();
                jPanel2.removeAll();
                jPanel2.repaint();
            String j=null,z=null;
            int row=0,cap=0,h=-1,maxcol=0;
            String Theater = selectedNode.toString().substring(0, 3);
            String Schudule = selectedNode.toString().substring(4, 23)+"%";
            String sql="select SchID,Cap from Schedule left outer join Theater on Schedule.TheID=Theater.TheID "+
                    "where Convert(varchar,SchTime,120) LIKE '"+Schudule+"' and TheName ='"+Theater+"'";
            ResultSet rs= jdbc.queryBySql(sql);//输出带有时间ID 1条或以上
            while(rs.next()){
               row++;
               z=rs.getString(1);//SchID
               cap=rs.getInt(2);//座位数
            }
            JButton[] btnNums= new JButton[cap];
            String sql2="Select max(col)最多列 from Ticket GROUP BY SchID HAVING SchID='"+z+"'";
            ResultSet rs2= jdbc.queryBySql(sql2);
            while(rs2.next()) {maxcol=rs2.getInt(1);}
            String sql1="Select row,col,status from Ticket where SchID = '"+z+"' order by row,col asc";
            ResultSet rs1= jdbc.queryBySql(sql1);//时间安排表的座位信息
            if(row==1){
            initBtnNums(maxcol,cap,btnNums);
             jPanel2.revalidate();
             jPanel2.updateUI();
                while(rs1.next()){
                    h+=1;
                    if(rs1.getInt(3)==0){//0---卖出
                        btnNums[h].setText(rs1.getString(1)+"-"+rs1.getString(2));
                        btnNums[h].setContentAreaFilled(false);      
                        jPanel2.add(btnNums[h]).setEnabled(false);}//要在添加的时候决定按钮是否可用
                    else
                    {   btnNums[h].setText(rs1.getString(1)+"-"+rs1.getString(2));
                        jPanel2.add(btnNums[h]).setEnabled(true);}
                }
            }
            } 
        }catch (SQLException ex) {
            }
        
    }//GEN-LAST:event_jTree1MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        dispose();
        Movieadd frame1 =new Movieadd();
        frame1.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        dispose();
        TicketAdd ta= new TicketAdd();
        ta.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        dispose();
        reticket Frame= new reticket();
        Frame.setVisible(true);

    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        dispose();
        FilmHallFrame TheFrame=new FilmHallFrame();
        TheFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        // TODO add your handling code here:
        //printshow();
    }//GEN-LAST:event_jTree1ValueChanged

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        String s="error";
        if(JOptionPane.showConfirmDialog(frame, "确定重置所有票吗", "确认", JOptionPane.YES_NO_OPTION)==0){
            s=JOptionPane.showInputDialog("请输入超级密码");
            System.out.print(s);
            String sql="select * from Users where UserID='U001' and Password='"+s+"'";
            try {
                ResultSet rs=jdbc.queryBySql(sql);
                if(rs.next()){
                    String sql1="update Ticket set status='1'";
                    try {
                        jdbc.modifyBySql(sql1);
                    } catch (SQLException ex) {
                        
                    }
                    JOptionPane.showMessageDialog(this, "重置成功！", "标题", JOptionPane.INFORMATION_MESSAGE);
                    rs.close();
                }else{
                    JOptionPane.showMessageDialog(this, "密码错误！", "标题", JOptionPane.ERROR_MESSAGE);
                }
            }catch(Exception e){}
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        dispose();
        User Frame=new User();
        Frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
