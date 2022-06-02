package percobaan;

import connection059.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class searchBuku extends  JFrame {
    private JRadioButton radioSemua;
    private JRadioButton radioJudul;
    private JRadioButton radioPengarang;
    private JTextField txtJudul;
    private JTextField txtPengarang;
    private JTable tableView;
    private JPanel searchBuku;
    private DefaultTableModel model = new DefaultTableModel();

    public static void main(String[] args) {
    new searchBuku().setVisible(true);
    }

    public void addColomn(){
        model.addColumn("Kode Buku");
        model.addColumn("Judul Buku");
        model.addColumn("Pengarang");
        model.addColumn("Penerbit");
        model.addColumn("Harga");
        model.addColumn("Jumlah");
    }

    public searchBuku(){
        tableView.setModel(model);
        addColomn();
        radioJudul.setEnabled(false);
        radioPengarang.setEnabled(false);
        this.setContentPane(searchBuku);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,450);


        radioJudul.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                FrameRadioButton();
                showByJudul();
                int count = tableView.getRowCount();
                if (count<=0){

                    JOptionPane.showMessageDialog(null, "Data Buku Tidak Ditemukan!");
                }
            }
        });
        txtJudul.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                radioJudul.setEnabled(true);

            }
        });
        txtPengarang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                radioPengarang.setEnabled(false);
            }
        });
        radioSemua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameRadioButton();

                loadData();
            }
        });

        radioPengarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameRadioButton();
                showByPengarang();
                int count = tableView.getRowCount();
                if (count<=0){
                    JOptionPane.showMessageDialog(null, "Data Buku Tidak Ditemukan!");
                }
            }
        });
        txtPengarang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                radioPengarang.setEnabled(true);
            }
        });
    }

    public void showByPengarang(){
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();

        //memberi tahu data yang kosong

        model.fireTableDataChanged();

        try{

            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tbBuku WHERE pengarang LIKE '%"+txtPengarang.getText()+"%'";
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
    public void showByJudul(){
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();

        //memberi tahu data yang kosong

        model.fireTableDataChanged();

        try{

            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tbBuku WHERE judulBuku LIKE '%"+txtJudul.getText()+"%'";
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

    public void FrameRadioButton(){
    ButtonGroup grup =new ButtonGroup();

    grup.add(radioJudul);
    grup.add(radioPengarang);
    grup.add(radioSemua);
    }
}
