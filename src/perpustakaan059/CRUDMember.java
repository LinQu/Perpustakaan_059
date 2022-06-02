package perpustakaan059;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import connection059.DBConnect;

import java.awt.event.*;

public class CRUDMember extends JFrame{
    private JPanel CRUDMember;
    private JTextField txtKode1;
    private JTable tableView;
    private JTextField txtKode;
    private JButton btnCancel;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTextField txtNIM;
    private JTextField txtNama;
    private JTextField txtNoTelp;
    private JTextField txtEmail;
    private DefaultTableModel model = new DefaultTableModel();
    DBConnect connection = new DBConnect();
    String Kode;
    String Nama;
    String NIM;
    String NoTelp;
    String Email;

    public static void main(String[] args) {
        new CRUDMember().setVisible(true);
    }

    public CRUDMember(){
        setContentPane(CRUDMember);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(700,400);
        tableView.setModel(model);
        addColomn();
        loadData();
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kode = txtKode1.getText();
                NIM = txtNIM.getText();
                Nama = txtNama.getText();
                NoTelp = txtNoTelp.getText();
                Email = txtEmail.getText();
                try{
                    String query = "INSERT INTO tblMember VALUES (?,?,?,?,?)";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1,Kode);
                    connection.pstat.setString(2,NIM);
                    connection.pstat.setString(3,Nama);
                    connection.pstat.setString(4,NoTelp);
                    connection.pstat.setString(5,Email);


                    connection.pstat.executeUpdate(); //insert ke database
                    connection.pstat.close();   //menutup kembali koneksi db

                    JOptionPane.showMessageDialog(null,"Insert data Member Berhasil");
                } catch (Exception ex) {
                    System.out.println("Terjadi Erorr pada saat insert data  : "+ ex);
                }

                clear();
                loadData();
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
                txtKode1.setEnabled(false);
                int i = tableView.getSelectedRow();
                if(i == -1){
                    return;
                }

                txtKode1.setText((String) model.getValueAt(i,0));
                txtNIM.setText((String) model.getValueAt(i,1));
                txtNama.setText((String) model.getValueAt(i,2));
                txtNoTelp.setText((String) model.getValueAt(i,3));
                txtEmail.setText((String) model.getValueAt(i,4));
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtKode1.setEnabled(true);
                Kode = txtKode1.getText();
                NIM = txtNIM.getText();
                Nama = txtNama.getText();
                NoTelp = txtNoTelp.getText();
                Email = txtEmail.getText();
                try{
                    connection.stat = connection.conn.createStatement();
                    String query = "UPDATE tblMember SET mb_nim=?,mb_nama=?,mb_no_hp=?,mb_email=? WHERE mb_kd=?";
                    connection.pstat = connection.conn.prepareStatement(query);

                    connection.pstat.setString(1,NIM);
                    connection.pstat.setString(2,Nama);
                    connection.pstat.setString(3,NoTelp);
                    connection.pstat.setString(4,Email);
                    connection.pstat.setString(5,Kode);

                    connection.pstat.executeUpdate();
                    connection.pstat.close();
                    clear();
                    JOptionPane.showMessageDialog(null,"Update Data Berhasil");

                }
                catch (Exception e1){
                    System.out.println("Terjadi error saat update "+e1);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtKode1.setEnabled(true);
                Kode = txtKode1.getText();
                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "DELETE FROM tblMember WHERE mb_kd=?";
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

    public void addColomn(){
        model.addColumn("Kode Member");
        model.addColumn("NIM");
        model.addColumn("Nama");
        model.addColumn("No Handhphone");
        model.addColumn("Email");
    }

    public void clear(){
        txtKode1.setText("");
        txtNIM.setText("");
        txtNama.setText("");
        txtNoTelp.setText("");
        txtEmail.setText("");
    }

    public void loadData(){
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();

        //memberi tahu data yang kosong

        model.fireTableDataChanged();

        try{

            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblMember";
            connection.result = connection.stat.executeQuery(query);

            //lakukan baris perbaris
            while(connection.result.next()){
                Object[] obj = new  Object[5];
                obj[0] = connection.result.getString("mb_kd");
                obj[1] = connection.result.getString("mb_nim");
                obj[2] = connection.result.getString("mb_nama");
                obj[3] = connection.result.getString("mb_no_hp");
                obj[4] = connection.result.getString("mb_email");

                model.addRow(obj);

            }
            connection.stat.close();
            connection.result.close();

        }
        catch (Exception e){
            System.out.println("Terjadi error saat load data member: "+e);
        }


    }

    public void SearchByKode(){
        model.getDataVector().removeAllElements();

        //memberi tahu data yang kosong

        model.fireTableDataChanged();

        try{

            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblMember WHERE mb_kd LIKE '%"+txtKode.getText()+"%'";
            connection.result = connection.stat.executeQuery(query);

            //lakukan baris perbaris
            while(connection.result.next()){
                String TempStatus = "";
                Object[] obj = new  Object[5];
                obj[0] = connection.result.getString("mb_kd");
                obj[1] = connection.result.getString("mb_nim");
                obj[2] = connection.result.getString("mb_nama");
                obj[3] = connection.result.getString("mb_no_hp");
                obj[4] = connection.result.getString("mb_email");


                model.addRow(obj);

            }
            connection.stat.close();
            connection.result.close();

        }
        catch (Exception e){
            System.out.println("Terjadi error saat load data buku: "+e);
        }
    }
}
