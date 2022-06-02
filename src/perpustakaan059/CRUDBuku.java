package perpustakaan059;

import com.toedter.calendar.JDateChooser;
import connection059.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.Format;
import java.text.SimpleDateFormat;

public class CRUDBuku extends JFrame {
    private JPanel CRUDBuku;
    private JTextField txtKode1;
    private JTextField txtJudul;
    private JTextField txtPengarang;
    private JTextField txtPenerbit;
    private JTable tableView;
    private JTextField txtKode;
    private JButton btnCancel;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTextField txtJumlah;
    private JPanel jpCald;
    private JComboBox cbJenis;
    private JComboBox cbStatus;
    private DefaultTableModel model = new DefaultTableModel();
    DBConnect connection = new DBConnect();
    JDateChooser datechose = new JDateChooser();
    String Kode;
    String Judul;
    String Pengarang;
    String Penerbit;
    String Tahun;
    String Jumlah;
    String Jenis;
    String Status;

    public static void main(String[] args) {
    new CRUDBuku().setVisible(true);
    }

    public CRUDBuku(){
        setContentPane(CRUDBuku);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(700,400);
        tableView.setModel(model);
        jpCald.add(datechose);
        addColomn();
        loadData();
        tampilJenis();
        cbStatus.addItem("Tidak Tersedia");
        cbStatus.addItem("Tersedia");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Kode = txtKode1.getText();
                Judul = txtJudul.getText();
                Pengarang = txtPengarang.getText();
                Penerbit = txtPenerbit.getText();
                Tahun = formatter.format(datechose.getDate());
                Jumlah = txtJumlah.getText();
                Status = String.valueOf(cbStatus.getSelectedIndex());

                String temp = (String) cbJenis.getSelectedItem();
                try{
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT jns_kd from tblJenisBuku where jns_desc = '"+ temp+"'";
                    connection.result = connection.stat.executeQuery(query);

                    while(connection.result.next()){
                        Jenis = connection.result.getString("jns_kd");
                    }

                    connection.stat.close();
                    connection.result.close();

                }catch (Exception e1){
                    System.out.println("Terjadi Erorr pada saat combo box  : "+ e1);

                }

                try{
                    String query = "INSERT INTO tblBuku VALUES (?,?,?,?,?,?,?,?)";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1,Kode);
                    connection.pstat.setString(2,Judul);
                    connection.pstat.setString(3,Pengarang);
                    connection.pstat.setString(4,Penerbit);
                    connection.pstat.setString(5,Tahun);
                    connection.pstat.setString(6,Jumlah);
                    connection.pstat.setString(7,Jenis);
                    connection.pstat.setString(8,Status);


                    connection.pstat.executeUpdate(); //insert ke database
                    connection.pstat.close();   //menutup kembali koneksi db

                    JOptionPane.showMessageDialog(null,"Insert data  Berhasil");
                } catch (Exception ex) {
                    System.out.println("Terjadi Erorr pada saat insert data  : "+ ex);
                }

                clear();
                loadData();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtKode1.setEnabled(true);
                Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Kode = txtKode.getText();
                Judul = txtJudul.getText();
                Pengarang = txtPengarang.getText();
                Penerbit = txtPenerbit.getText();
                Tahun = formatter.format(datechose.getDate());
                Jumlah = txtJumlah.getText();

                Status = String.valueOf(cbStatus.getSelectedIndex());
                String temp = (String) cbJenis.getSelectedItem();
                try{
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT jns_kd from tblJenisBuku where jns_desc = '"+ temp+"'";
                    connection.result = connection.stat.executeQuery(query);

                    while(connection.result.next()){
                        Jenis = connection.result.getString("jns_kd");
                    }




                }catch (Exception e1){
                    System.out.println("Terjadi Erorr pada saat combo box  : "+ e1);

                }
                try{
                    connection.stat = connection.conn.createStatement();
                    String query = "UPDATE tblBuku SET bk_judul=?,bk_pengarang=?,bk_penerbit=?,bk_tahun_penerbit=?,bk_stock=?,jns_kd=?,bk_status=? WHERE bk_kd=?";
                    connection.pstat = connection.conn.prepareStatement(query);


                    connection.pstat.setString(1,Judul);
                    connection.pstat.setString(2,Pengarang);
                    connection.pstat.setString(3,Penerbit);
                    connection.pstat.setString(4,Tahun);
                    connection.pstat.setString(5,Jumlah);
                    connection.pstat.setString(6,Jenis);
                    connection.pstat.setString(7,Status);
                    connection.pstat.setString(8,Kode);

                    connection.pstat.executeUpdate();
                    connection.pstat.close();
                    connection.result.close();


                }
                catch (Exception e1){
                    System.out.println("Terjadi error saat update "+e1);
                }
                clear();
                JOptionPane.showMessageDialog(null,"Update Data Berhasil");
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
                    String query = "DELETE FROM tblBuku WHERE bk_kd=?";
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
                txtJudul.setText((String) model.getValueAt(i,1));
                txtPengarang.setText((String) model.getValueAt(i,2));
                txtPenerbit.setText((String) model.getValueAt(i,3));
                txtJumlah.setText((String) model.getValueAt(i,5));
            }
        });
        txtKode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                SearchByKode();
            }
        });
    }



    public void addColomn(){
        model.addColumn("Kode Buku");
        model.addColumn("Judul");
        model.addColumn("Pengarang");
        model.addColumn("Penerbit");
        model.addColumn("Tahun Penerbit");
        model.addColumn("Jumlah");
        model.addColumn("Jenis Buku");
        model.addColumn("Status");
    }

    public void clear(){
        txtKode.setText("");
        txtJudul.setText("");
        txtPengarang.setText("");
        txtPenerbit.setText("");
        txtJumlah.setText("");

    }

    public void tampilJenis(){
        try{
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT jns_kd, jns_desc FROM tblJenisBuku";
            connection.result = connection.stat.executeQuery(sql);

            while(connection.result.next()){
                cbJenis.addItem(connection.result.getString("jns_desc"));
            }
            connection.stat.close();
            connection.result.close();
        }
        catch (Exception e){
            System.out.println("Terjadi error saat load data supplier"+e);
        }
    }

    public void SearchByKode(){
        model.getDataVector().removeAllElements();

        //memberi tahu data yang kosong

        model.fireTableDataChanged();

        try{

            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblBuku WHERE bk_kd LIKE '%"+txtKode.getText()+"%'";
            connection.result = connection.stat.executeQuery(query);

            //lakukan baris perbaris
            while(connection.result.next()){
                String TempStatus = "";
                Object[] obj = new  Object[8];
                obj[0] = connection.result.getString("bk_kd");
                obj[1] = connection.result.getString("bk_judul");
                obj[2] = connection.result.getString("bk_pengarang");
                obj[3] = connection.result.getString("bk_penerbit");
                obj[4] = connection.result.getString("bk_tahun_penerbit");
                obj[5] = connection.result.getString("bk_stock");
                obj[6] = connection.result.getString("jns_kd");
                Status = connection.result.getString("bk_status");
                if(Status.equals("1")){
                    TempStatus = "Tersedia";
                }else {
                    TempStatus = "Tidak Tersedia";
                }
                obj[7] = TempStatus;
                model.addRow(obj);

            }
            connection.stat.close();
            connection.result.close();

        }
        catch (Exception e){
            System.out.println("Terjadi error saat load data buku: "+e);
        }
    }

    public void loadData(){
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();

        //memberi tahu data yang kosong

        model.fireTableDataChanged();

        try{

            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblBuku";
            connection.result = connection.stat.executeQuery(query);

            //lakukan baris perbaris
            while(connection.result.next()){
                String TempStatus = "";
                Object[] obj = new  Object[8];
                obj[0] = connection.result.getString("bk_kd");
                obj[1] = connection.result.getString("bk_judul");
                obj[2] = connection.result.getString("bk_pengarang");
                obj[3] = connection.result.getString("bk_penerbit");
                obj[4] = connection.result.getString("bk_tahun_penerbit");
                obj[5] = connection.result.getString("bk_stock");
                obj[6] = connection.result.getString("jns_kd");
                Status = connection.result.getString("bk_status");
                if(Status.equals("1")){
                    TempStatus = "Tersedia";
                }else {
                    TempStatus = "Tidak Tersedia";
                }
                obj[7] = TempStatus;


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
