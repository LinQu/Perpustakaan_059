package perpustakaan059;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.sql.SQLException;
import com.toedter.calendar.JDateChooser;
import connection059.DBConnect;
import javax.swing.table.DefaultTableModel;
import java.text.Format;
import java.text.SimpleDateFormat;

public class Peminjaman extends JFrame{
    private JTextField txtTanggal;
    private JTextField txtPinjam;
    private JPanel Peminjaman;
    private JPanel Transaksi;
    private JTextField txtkode;
    private JComboBox cbmember;
    private JPanel jptglkembali;
    private JButton tambahPeminjamanButton;
    private JComboBox cbpilih;
    private JButton tambahBukuButton;
    private JButton simpanButton;
    private JButton batalButton;
    private JTable tblpinjam;
    private DefaultTableModel model = new DefaultTableModel();
    DBConnect connection = new DBConnect();
    JDateChooser datechose = new JDateChooser();
    String nm_bk;
    String kd_bk;
    String penerbit;
    String pengarang;
    String thn_terbit;
    String trkode, trtgl, memberkd, tglpinjam, tglkembali;

    public static void main(String[] args) {
        new Peminjaman().setVisible(true);
    }


    public Peminjaman(){
        LocalDate date = LocalDate.now();
        txtPinjam.setText(""+date);
        txtTanggal.setText(""+date);
        setContentPane(Peminjaman);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cbpilih.setEnabled(false);
        tambahBukuButton.setEnabled(false);
        tblpinjam.setModel(model);
        addColumn();
        jptglkembali.add(datechose);
        tampilMember();
        autokode();
        tampilBuku();
        cbmember.setSelectedItem(null);
        cbpilih.setSelectedItem(null);
        pack();
        batalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//membuat statement untuk input ke database
                int stock = 0, stock2 = 0;
                double harga;
                Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                int j = tblpinjam.getModel().getRowCount(); //knowing how many row on tabelBuku dilayar
                trtgl= txtTanggal.getText();
                trkode = txtkode.getText();
                tglpinjam= txtPinjam.getText();
                tglkembali=formatter.format(datechose.getDate());
                memberkd = "";
                try {
                    //mencari kode supplier dari supplier yang ada di tabel, karena yang masuk ke tabel master adalah kode supplier
                    connection.stat = connection.conn.createStatement();
                    String sql = "SELECT mb_kd FROM tblMember WHERE mb_nama = '" + cbmember.getSelectedItem() + "'";
                    connection.result = connection.stat.executeQuery(sql);

                    while (connection.result.next()) {
                        memberkd = (String) connection.result.getString("mb_kd");
                    }

                    //INSERT ke tabel master
                    String sql2 = "INSERT INTO tblMasterPeminjaman VALUES (?, ?, ?, ?, ?, ?)";
                    connection.pstat = connection.conn.prepareStatement(sql2);
                    connection.pstat.setString(1, trkode);
                    connection.pstat.setString(2, trtgl);
                    connection.pstat.setString(3, memberkd);
                    connection.pstat.setString(4, tglpinjam);
                    connection.pstat.setString(5, tglkembali);
                    connection.pstat.setInt(6, 1);
                    connection.pstat.executeUpdate(); //insert ke tabel
                    //insert ke tabel detail, looping sebanyak row yang ada di layar
                    for(int k = 0; k < j; k++) {
                        String sql3 = "INSERT INTO tblDetilPeminjaman VALUES (?, ?, ?)";
                        connection.pstat = connection.conn.prepareStatement(sql3);
                        connection.pstat.setString(1, trkode);
                        connection.pstat.setString(2, (String) model.getValueAt(k, 0)); //kodebuku
                        connection.pstat.setString(3, (String) model.getValueAt(k, 5)); //quantity
                        connection.pstat.executeUpdate(); //insert tabel detilBeli

                        //mencari nilai stock ditabel buku saat ini dan menabmahkan dengan nilai di inputan
                        String sql4 = "SELECT bk_stock FROM tblBuku WHERE bk_kd = '" + (String) model.getValueAt(k, 0) + "'";
                        connection.result = connection.stat.executeQuery(sql4);
                        while (connection.result.next()) {
                            stock  = connection.result.getInt("bk_stock");
                            stock2 = stock - Integer.parseInt((String) model.getValueAt(k, 5));
                        }

                        //update stack di tabel buku
                        System.out.println("Nilai quantity setelah dikurang = " + String.valueOf(stock2));
                        String sql5 = "UPDATE tblBuku SET bk_stock = ? WHERE bk_kd =?";
                        connection.pstat = connection.conn.prepareStatement(sql5);
                        connection.pstat.setString(1, String.valueOf(stock2));
                        connection.pstat.setString(2, (String) model.getValueAt(k, 0));
                        connection.pstat.executeUpdate(); //update tabel buku
                    }

                    connection.pstat.close(); //close connection
                    JOptionPane.showMessageDialog(null, "Insert data Buku Berhasil");
                    clear();
                    autokode();
                } catch (SQLException ex) {
                    System.out.println("Terjadi error saat insert " +ex);
                }
            }
        });
        tambahBukuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //mencari kode supplier dari supplier yang ada di tabel, karena yang masuk ke tabel master adalah kode supplier
                    connection.stat = connection.conn.createStatement();
                    String sql = "SELECT * FROM tblBuku WHERE bk_judul = '" + cbpilih.getSelectedItem() + "'";
                    connection.result = connection.stat.executeQuery(sql);

                    while (connection.result.next()) {
                        Object[] obj = new Object[5];
                        obj[0] = kd_bk = (String) connection.result.getString("bk_kd");
                        obj[1] = nm_bk = (String) connection.result.getString("bk_judul");
                        obj[2] = penerbit = (String) connection.result.getString("bk_penerbit");
                        obj[3] = pengarang = (String) connection.result.getString("bk_pengarang");
                        obj[4] = thn_terbit = (String) connection.result.getString("bk_tahun_penerbit");
                        model.addRow(obj);
                    }
                } catch (Exception e1) {
                    System.out.println("Terjadi error pada saat penambahan table :" + e1);
                }
            }
        });
        tambahPeminjamanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cbpilih.setEnabled(true);
                tambahBukuButton.setEnabled(true);
            }
        });
    }

    public void clear()
    {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        datechose.setDate(null);
        cbmember.setSelectedItem(null);
        cbpilih.setSelectedItem(null);
        cbpilih.setEnabled(false);
        tambahBukuButton.setEnabled(false);
    }

    public void addColumn() {
        model.addColumn("Kode Buku");
        model.addColumn("Judul  Buku");
        model.addColumn("Pengarang");
        model.addColumn("Penerbit");
        model.addColumn("Tahun Terbit");
        model.addColumn("Jumlah Pinjam");
    }

    public void tampilMember() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT mb_kd, mb_nim, mb_nama, mb_no_hp, mb_email FROM tblMember";
            connection.result = connection.stat.executeQuery(sql);
            while (connection.result.next()) {
                cbmember.addItem(connection.result.getString("mb_nama"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data supplier" + ex);
        }
    }

    public void autokode(){
        try{
            String sql = "SELECT * FROM tblMasterPeminjaman ORDER BY trans_kd desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                trkode = connection.result.getString("trans_kd").substring(5);
                String AN = "" + (Integer.parseInt(trkode) + 1);
                String nol = "";

                if (AN.length() == 1) {
                    nol = "000";
                } else if (AN.length() == 2) {
                    nol = "00";
                } else if (AN.length() == 3) {
                    nol = "0";
                }else if (AN.length() == 4) {
                    nol = "";
                }
                txtkode.setText("TR" + nol + AN);
                txtkode.setEnabled(false);

            }else {
                txtkode.setText("TR0001");
                txtkode.setEnabled(false);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e1){
            System.out.println("Terjadi error pada transaksi: " + e1);
        }
    }

    public void tampilBuku() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT bk_kd, bk_judul, bk_pengarang, bk_penerbit, bk_tahun_penerbit, bk_stock, jns_kd, bk_status FROM tblBuku";
            connection.result = connection.stat.executeQuery(sql);
            while (connection.result.next()) {
                cbpilih.addItem(connection.result.getString("bk_judul"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data supplier" + ex);
        }
    }
}
