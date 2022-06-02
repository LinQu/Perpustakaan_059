package perpustakaan059;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import connection059.DBConnect;

public class LaporanPeminjaman extends JFrame{
    private JPanel Laporan;
    private JTable tblLaporanPeminjaman;
    private DefaultTableModel model = new DefaultTableModel();
    DBConnect connection = new DBConnect();

    public static void main(String[] args) {
    new LaporanPeminjaman().setVisible(true);
    }

    public LaporanPeminjaman(){
        setContentPane(Laporan);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        tblLaporanPeminjaman.setModel(model);
        pack();
        addColumn();
        loadData();
    }

    public void loadData() {
        //menghapus seluruh data ditampilkan (jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();
        //memberi tahu data telah ksoong
        model.fireTableDataChanged();

        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblDetilPeminjaman ";
            connection.result = connection.stat.executeQuery(query);

            //lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[3];
                obj[0] = connection.result.getString("trans_kd");
                obj[1] = connection.result.getString("bk_kd");
                obj[2] = connection.result.getString("trans_qty");
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data buku: " + e);
        }
    }

    public void addColumn(){
        model.addColumn("Kode Transaksi");
        model.addColumn("Kode Buku");
        model.addColumn("Jumlah");
    }
}
