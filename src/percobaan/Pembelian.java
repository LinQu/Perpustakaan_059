package percobaan;

import connection059.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;

public class Pembelian extends JFrame{
    private JTextField txtPembelian;
    private JPanel Pembelian;
    private JPanel jpCald;
    private JComboBox cmbSupplier;
    private JButton btnPembelian;
    private JTable table1;
    private JTextField txtTotal;
    private JButton btnSimpan;
    private JButton btnCancel;

    DBConnect connection = new DBConnect();
    JDateChooser datechose = new JDateChooser();
    private DefaultTableModel model = new DefaultTableModel();

    public Pembelian(){
        setContentPane(Pembelian);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(560,500);

        jpCald.add(datechose);

        table1.setModel(model);
        model.addColumn("Kode Buku");
        model.addColumn("Judul Buku");
        model.addColumn("Harga");
        model.addColumn("Jumlah");
        tampilSupplier();

        btnPembelian.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addRow(new Object[]{"", "" , "", ""});
            }
        });
        table1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String kode;
                String judulBuku;
                String harga;
                int i = table1.getSelectedRow();
                if(i==-1){
                    return;
                }

                kode = (String) model.getValueAt(i,0);
                try{
                    connection.stat = connection.conn.createStatement();

                    String sql = "SELECT judulBuku, harga, stock FROM tbBuku where kodeBuku ='"+kode+"'";
                    connection.result = connection.stat.executeQuery(sql);
                    while ((connection.result.next())){
                        judulBuku = connection.result.getString("judulBuku");
                        harga = connection.result.getString("harga");
                        model.setValueAt(judulBuku, i,1);
                        model.setValueAt(harga, i, 2);
                    }
                    connection.stat.close();
                    connection.result.close();
                }
                catch (Exception e1){
                    System.out.println("Terjadi Error saat mengambil data judul dan harga buku "+ e1);
                }
            }
        });
        txtTotal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Double temp, total = 0.0;
                int i = table1.getSelectedRow();
                if(i==-1){
                    return;
                }
                int j = table1.getModel().getRowCount();

                for(int k = 0;k < j;k++){
                    temp = (Double.parseDouble((String) model.getValueAt(k,2))) * (Double.parseDouble((String) model.getValueAt(k,3)));
                    total = total + temp;

                }
                txtTotal.setText(String.valueOf(total));
            }
        });
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //membuat statement untuk input ke database
                String noBeli,tanggal,kodeSupplier,total;
                int stock = 0, stock2 = 0;
                double harga;
                Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                int j = table1.getModel().getRowCount(); //knowing how many row on tableBuku di layar
                noBeli = txtPembelian.getText();
                tanggal = formatter.format(datechose.getDate());
                total = txtTotal.getText();
                kodeSupplier = "";
                try{
                    //mencari kode supplier dari supplier yang ada di tabe, karena yg masuk ke tabel master adalah kode supplier
                    connection.stat = connection.conn.createStatement();
                    String sql = "SELECT kodeSupplier FROM tbSupplier WHERE namaSupplier = '"+cmbSupplier.getSelectedItem()+"'";
                    connection.result = connection.stat.executeQuery(sql);
                    while (connection.result.next()){
                        kodeSupplier =(String) connection.result.getString("kodeSupplier");
                    }
                    String sql2 = "INSERT INTO masterBeli VALUES (?,?,?,?)";
                    connection.pstat = connection.conn.prepareStatement(sql2);
                    connection.pstat.setString(1,noBeli);
                    connection.pstat.setString(2,tanggal);
                    connection.pstat.setString(3,kodeSupplier);
                    connection.pstat.setString(4,total);
                    connection.pstat.executeUpdate(); //inser ke tabel master

                    //insert ke tabel detail, looping sebanyak row yang ada di layar
                    for(int k = 0; k < j; k++){
                        harga =  (Double.parseDouble((String)model.getValueAt(k,2))) * (Double.parseDouble((String)model.getValueAt(k,3)));
                        String sql3 = "INSERT INTO detilBeli VALUES (?,?,?,?)";
                        connection.pstat = connection.conn.prepareStatement(sql3);
                        connection.pstat.setString(1,noBeli);
                        connection.pstat.setString(2,(String) model.getValueAt(k,0)); //kodeBuku
                        connection.pstat.setString(3,(String) model.getValueAt(k,3)); //jumlah beli
                        connection.pstat.setString(4,(String.valueOf(harga))); //jumlah * harga
                        connection.pstat.executeUpdate(); //insert tabel detilBeli

                        //mencari nilai stack ditbael buku saat ini dan menambahkan dengan nilai di inputan
                        String sql4 = "SELECT stock FROM tbBuku WHERE kodeBuku = '"+(String) model.getValueAt(k,0)+"'";
                        connection.result = connection.stat.executeQuery(sql4);
                        while (connection.result.next()){
                            stock = connection.result.getInt("stock");
                            stock2 = stock + Integer.parseInt((String) model.getValueAt(k,3));
                        }
                        //update stack di tabel buku
                        System.out.println("Nilai stock setelah ditambah : "+String.valueOf(stock2));
                        String sql5 = "UPDATE tbBuku SET stock =? WHERE kodeBuku =?";
                        connection.pstat = connection.conn.prepareStatement(sql5);
                        connection.pstat.setString(1,String.valueOf(stock2));
                        connection.pstat.setString(2,(String) model.getValueAt(k,0));
                        connection.pstat.executeUpdate(); //update tabel buku
                    }
                    connection.pstat.close(); //close connection
                    JOptionPane.showMessageDialog(null,"Insert data Buku berhasil");
                }catch (SQLException ex){
                    System.out.println("Terajadi error saat insert :"+ex);
                }

            }
        });
    }

    public static void main(String[] args) {
        new Pembelian().setVisible(true);
    }

    public void tampilSupplier(){
        try{
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT kodeSupplier, namaSupplier FROM tbSupplier";
            connection.result = connection.stat.executeQuery(sql);

            while(connection.result.next()){
                cmbSupplier.addItem(connection.result.getString("namaSupplier"));
            }
            connection.stat.close();
            connection.result.close();
        }
        catch (Exception e){
            System.out.println("Terjadi error saat load data supplier"+e);
        }
    }
}
