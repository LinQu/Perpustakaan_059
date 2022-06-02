package percobaan;


import connection059.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class inputBuku extends JFrame{
    private JTextField txtKode;
    private JTextArea txtAreaJudul;
    private JTextField txtPengarang;
    private JTextField txtPenerbit;
    private JTextField txtHarga;
    private JTextField txtJumlah;
    private JButton cancelButton;
    private JButton OKButton;
    private JPanel JPInputBuku;

    DBConnect connection = new DBConnect();
    String kodeBuku;
    String judulBuku;
    String pengarang;
    String penerbit;
    String harga;
    String jumlah;

    public static void main(String[] args) {
    new inputBuku().setVisible(true);
    }

    public void Clear(){
        txtKode.setText("");
        txtAreaJudul.setText("");
        txtHarga.setText("");
        txtJumlah.setText("");
        txtPenerbit.setText("");
        txtPengarang.setText("");
    }

    public inputBuku() {
        pack();
        setSize(350,400);
        this.setContentPane(JPInputBuku);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kodeBuku = txtKode.getText();
                judulBuku = txtAreaJudul.getText();
                pengarang = txtPengarang.getText();
                penerbit = txtPenerbit.getText();
                harga = txtHarga.getText();
                jumlah = txtJumlah.getText();

                try{
                    String query = "INSERT INTO tbBuku VALUES (?,?,?,?,?,?)";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1,kodeBuku);
                    connection.pstat.setString(2,judulBuku);
                    connection.pstat.setString(3,pengarang);
                    connection.pstat.setString(4,penerbit);
                    connection.pstat.setString(5,harga);
                    connection.pstat.setString(6,jumlah);

                    connection.pstat.executeUpdate(); //insert ke database
                    connection.pstat.close();   //menutup kembali koneksi db

                } catch (Exception ex) {
                    System.out.println("Terjadi Erorr pada saat insert data buku : "+ ex);
                }
                JOptionPane.showMessageDialog(null,"Insert data Buku Berhasil");
                Clear();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Clear();
            }
        });
    }
}
