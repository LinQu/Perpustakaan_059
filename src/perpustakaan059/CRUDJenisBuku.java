package perpustakaan059;

import connection059.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class CRUDJenisBuku extends JFrame {
    private JPanel CRUDJenis;
    private JTextField txtDeskripsi;
    private JTextField txtStatus;
    private JTable tableView;
    private JButton btnCancel;
    private JButton btnSave;
    private JTextField txtKode;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JComboBox cbStatus;
    private DefaultTableModel model = new DefaultTableModel();
    DBConnect connection = new DBConnect();
    String Deskripsi;
    String Status;
    String Kode;

    public void clear(){
        txtDeskripsi.setText("");
    }

    public void loadData(){
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();

        //memberi tahu data yang kosong

        model.fireTableDataChanged();

        try{

            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblJenisBuku";
            connection.result = connection.stat.executeQuery(query);

            //lakukan baris perbaris
            while(connection.result.next()){
                String TempStatus = "";
                Object[] obj = new  Object[3];
                obj[0] = connection.result.getString("jns_kd");
                obj[1] = connection.result.getString("jns_desc");
                Status = connection.result.getString("jns_status");
                if(Status.equals("1")){
                    TempStatus = "Tersedia";
                }else {
                    TempStatus = "Tidak Tersedia";
                }
                obj[2] = TempStatus;

                model.addRow(obj);

            }
            connection.stat.close();
            connection.result.close();

        }
        catch (Exception e){
            System.out.println("Terjadi error saat load data buku: "+e);
        }


    }

    public void addColomn(){
        model.addColumn("Kode Jenis");
        model.addColumn("Deskripsi");
        model.addColumn("Status");
    }

    public void SearchByKode(){
        model.getDataVector().removeAllElements();

        //memberi tahu data yang kosong

        model.fireTableDataChanged();

        try{

            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblJenisBuku WHERE jns_kd LIKE '%"+txtKode.getText()+"%'";
            connection.result = connection.stat.executeQuery(query);

            //lakukan baris perbaris
            while(connection.result.next()){
                String TempStatus = "";
                Object[] obj = new  Object[3];
                obj[0] = connection.result.getString("jns_kd");
                obj[1] = connection.result.getString("jns_desc");
                Status = connection.result.getString("jns_status");
                if(Status.equals("1")){
                    TempStatus = "Tersedia";
                }else {
                    TempStatus = "Tidak Tersedia";
                }
                obj[2] = TempStatus;

                model.addRow(obj);

            }
            connection.stat.close();
            connection.result.close();

        }
        catch (Exception e){
            System.out.println("Terjadi error saat load data buku: "+e);
        }
    }

    public static void main(String[] args) {
       new CRUDJenisBuku().setVisible(true);
    }


    public CRUDJenisBuku(){
        setContentPane(CRUDJenis);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setSize(600,400);
        tableView.setModel(model);
        addColomn();
        loadData();
        cbStatus.addItem("Tidak Tersedia");
        cbStatus.addItem("Tersedia");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Deskripsi = txtDeskripsi.getText();
                Status = String.valueOf(cbStatus.getSelectedIndex());
                try{
                    String query = "INSERT INTO tblJenisBuku VALUES (?,?)";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1,Deskripsi);
                    connection.pstat.setString(2,Status);


                    connection.pstat.executeUpdate(); //insert ke database
                    connection.pstat.close();   //menutup kembali koneksi db

                } catch (Exception ex) {
                    System.out.println("Terjadi Erorr pada saat insert data buku : "+ ex);
                }
                JOptionPane.showMessageDialog(null,"Insert data Buku Berhasil");
                clear();

            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        txtKode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                SearchByKode();
            }
        });
        tableView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);


                int i = tableView.getSelectedRow();
                if(i == -1){
                    return;
                }

                txtDeskripsi.setText((String) model.getValueAt(i,1));
                cbStatus.setSelectedItem((String) model.getValueAt(i,2));

            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Deskripsi = txtDeskripsi.getText();
                Status = String.valueOf(cbStatus.getSelectedIndex());
                Kode = txtKode.getText();
                try{
                    connection.stat = connection.conn.createStatement();
                    String query = "UPDATE tblJenisBuku SET jns_desc=?,jns_status=? WHERE jns_kd=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1,Deskripsi);
                    connection.pstat.setString(2,Status);
                    connection.pstat.setString(3,Kode);

                    connection.pstat.executeUpdate();
                    connection.pstat.close();
                    JOptionPane.showMessageDialog(null,"Update Data Berhasil");

                }
                catch (Exception e1){
                    System.out.println("Terjadi error saat update Buku "+e1);
                }

            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kode = txtKode.getText();
                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "DELETE FROM tblJenisBuku WHERE jns_kd=?";
                    connection.pstat = connection.conn.prepareStatement(query);

                    connection.pstat.setString(1, Kode);

                    connection.pstat.executeUpdate();
                    connection.pstat.close();
                    JOptionPane.showMessageDialog(null,"Hapus Data Berhasil");
                    clear();
                }
                catch (Exception e1){
                    System.out.println("Terjadi Error saat menghapus"+e1);
                }
            }
        });
    }
}