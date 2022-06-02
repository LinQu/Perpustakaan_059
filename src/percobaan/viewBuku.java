package percobaan;

import connection059.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class viewBuku extends JFrame{
    private JTable tableView;
    private JPanel ViewData;
    private DefaultTableModel model = new DefaultTableModel();

    public static void main(String[] args) {
        new viewBuku().setVisible(true);
    }

    public viewBuku(){

        //menambahkan table model ke table

        tableView.setModel(model);

        addColomn();
        loadData();
        this.setContentPane(ViewData);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700,300);
    }

    public void addColomn(){
        model.addColumn("Kode Buku");
        model.addColumn("Judul Buku");
        model.addColumn("Pengarang");
        model.addColumn("Penerbit");
        model.addColumn("Harga");
        model.addColumn("Jumlah");
    }

    public void loadData(){
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();

        //memberi tahu data yang kosong

        model.fireTableDataChanged();

        try{

            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tbBuku";
            connection.result = connection.stat.executeQuery(query);

            //lakukan baris perbaris
            while(connection.result.next()){

                Object[] obj = new  Object[6];
                obj[0] = connection.result.getString("kodeBuku");
                obj[1] = connection.result.getString("judulBuku");
                obj[2] = connection.result.getString("pengarang");
                obj[3] = connection.result.getString("penerbit");
                obj[4] = connection.result.getString("harga");
                obj[5] = connection.result.getString("stock");

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
