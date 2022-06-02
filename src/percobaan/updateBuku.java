package percobaan;

import connection059.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class updateBuku extends JFrame{
    private JTable tblBuku;
    private JRadioButton radioJudul;
    private JRadioButton radioPengarang;
    private JTextField txtJudul;
    private JTextField txtPengarang;
    private JTextField txtKode;
    private JTextArea txtdtJudul;
    private JTextField txtdtPengarang;
    private JTextField txtdtPenerbit;
    private JTextField txtdtHarga;
    private JTextField txtdtJumlah;
    private JButton btnUbah;
    private JButton btnHapus;
    private JPanel updateBuku;
    private DefaultTableModel model = new DefaultTableModel();

    public static void main(String[] args) {
        new updateBuku().setVisible(true);
    }

    public void FrameRadioButton(){
        ButtonGroup grup =new ButtonGroup();

        grup.add(radioJudul);
        grup.add(radioPengarang);

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

    public void clear(){
        txtKode.setText("");
        txtdtHarga.setText("");
        txtdtJudul.setText("");
        txtPengarang.setText("");
        txtdtJumlah.setText("");
        txtdtPenerbit.setText("");
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

    public updateBuku() {
        addColomn();
        this.setContentPane(updateBuku);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        tblBuku.setModel(model);
        tblBuku.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblBuku.getSelectedRow();
                if(i == 1){
                    return;
                }
                txtKode.setText((String) model.getValueAt(i,0));
                txtdtJudul.setText((String) model.getValueAt(i, 1));
                txtdtPengarang.setText((String) model.getValueAt(i,2));
                txtdtPenerbit.setText((String) model.getValueAt(i,3));
                txtdtHarga.setText((String) model.getValueAt(i,4));
                txtdtJumlah.setText((String) model.getValueAt(i,5));
            }
        });
        radioJudul.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameRadioButton();
                showByJudul();
            }
        });
        btnUbah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String kodeBuku = txtKode.getText();
               String judulBuku = txtdtJudul.getText();
               String pengarang =  txtdtPengarang.getText();
               String penerbit = txtdtPenerbit.getText();
               String harga = txtdtHarga.getText();
               String jumlah = txtdtJumlah.getText();

               //update data table
                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "UPDATE tbBuku SET judulBuku=?,pengarang=?,penerbit=?,"+"harga=?,stock=? WHERE kodeBuku=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1,judulBuku);
                    connection.pstat.setString(2,pengarang);
                    connection.pstat.setString(3,penerbit);
                    connection.pstat.setString(4,harga);
                    connection.pstat.setString(5,jumlah);
                    connection.pstat.setString(6,kodeBuku);

                    connection.pstat.executeUpdate();
                    connection.pstat.close();

                }catch (Exception e1){
                    System.out.println("Terjadi error saat update Buku "+e1);
                }
                JOptionPane.showMessageDialog(null,"Update Data Berhasil");
            }
        });
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kodeBuku = txtKode.getText();
                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "DELETE FROM tbBuku WHERE kodeBuku=?";
                    connection.pstat = connection.conn.prepareStatement(query);

                    connection.pstat.setString(1, kodeBuku);

                    connection.pstat.executeUpdate();
                    connection.pstat.close();
                }
                catch (Exception e1){
                    System.out.println("Terjadi Error saat menghapus buku"+e1);
                }
                JOptionPane.showMessageDialog(null,"Hapus Data Berhasil");
                clear();
            }
        });
        radioPengarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameRadioButton();
                showByPengarang();
            }
        });
    }
}
